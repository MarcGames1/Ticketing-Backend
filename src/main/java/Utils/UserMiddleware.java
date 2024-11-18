package Utils;

import Auth.AuthService;
import User.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.Path;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@WebFilter(filterName="userFilter", urlPatterns="/*")
public class UserMiddleware implements Filter {

    @Inject
    private AuthService service;

    @Inject
    private CurrentRequestData currentRequestData;

    private JWTVerifier jwtVerifier;

    private Set<SecuredRoute> securedRoutes = new HashSet<>();


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        RSAKeyProvider keyProvider = new CognitoRSAKeyProvider();
        Algorithm algorithm = Algorithm.RSA256(keyProvider);
        jwtVerifier = JWT.require(algorithm).build();

        // Collect secured URLs during initialization
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forJavaClassPath())
                .setScanners(Scanners.TypesAnnotated, Scanners.MethodsAnnotated)
                .forPackage("Auth"));

        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Path.class);


        for (Class<?> controllerClass : controllerClasses) {
            if (controllerClass.getName().contains("Controller")) {
                for (Method method : controllerClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Secured.class)) {
                        Path classPath = controllerClass.getAnnotation(Path.class);
                        Path methodPath = method.getAnnotation(Path.class);
                        String methodType = getMethodType(method);
                        if (classPath != null && methodPath != null && methodType != null) {
                            String fullPath = classPath.value() + methodPath.value();
                            securedRoutes.add(new SecuredRoute(fullPath, methodType));
                        } else if (classPath != null && methodType != null) {
                            securedRoutes.add(new SecuredRoute(classPath.value(), methodType));
                        }
                    }
                }
            }
        }
    }

    private String getMethodType(Method method) {
        if (method.isAnnotationPresent(jakarta.ws.rs.GET.class)) return HttpMethod.GET;
        if (method.isAnnotationPresent(jakarta.ws.rs.POST.class)) return HttpMethod.POST;
        if (method.isAnnotationPresent(jakarta.ws.rs.PUT.class)) return HttpMethod.PUT;
        if (method.isAnnotationPresent(jakarta.ws.rs.DELETE.class)) return HttpMethod.DELETE;
        if (method.isAnnotationPresent(jakarta.ws.rs.OPTIONS.class)) return HttpMethod.OPTIONS;
        if (method.isAnnotationPresent(jakarta.ws.rs.HEAD.class)) return HttpMethod.HEAD;
        if (method.isAnnotationPresent(jakarta.ws.rs.PATCH.class)) return HttpMethod.PATCH;
        return null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (isSecuredResource(httpRequest)) {
            if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            String token = authorizationHeader.substring(7);

            try {
                var data = jwtVerifier.verify(token);
                var timeToExpireInMinutes = TimeUnit.MINUTES.convert(data.getExpiresAt().getTime() - new Date().getTime(), TimeUnit.MILLISECONDS);

                if(timeToExpireInMinutes < 0){
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                var claims = data.getClaims();
                var user = service.getUserByEmail(claims.get("email").asString());
                if(user == null) {
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                currentRequestData.setUser(user);
            } catch (NumberFormatException e) {
                //e.printStackTrace();
            } catch (TokenExpiredException e){
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } catch (Exception ignored){
                ignored.printStackTrace();
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isSecuredResource(HttpServletRequest request) {
        String requestPath = request.getRequestURI().substring(request.getContextPath().length());

        // Remove context path if present
        if (requestPath.startsWith("/api/")) {
            requestPath = requestPath.split("/api")[1];
        }

        String method = request.getMethod();

        // Check if any secured route matches the request path and method
        String finalRequestPath = requestPath;
        var res = securedRoutes.stream()
                .anyMatch(securedRoute -> securedRoute.getMethod().equals(method) && securedRoute.matches(finalRequestPath));
        return res;
    }

    @Override
    public void destroy() {
    }
}

class SecuredRoute {
    private final String path;
    private final String method;

    public SecuredRoute(String path, String method) {
        this.path = path;
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecuredRoute that = (SecuredRoute) o;
        return Objects.equals(path, that.path) && Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, method);
    }

    @Override
    public String toString() {
        return method + " " + path;
    }

    // Method to check if a given request path matches this secured route
    public boolean matches(String requestPath) {
        // Remove context path if present
       // String cleanedRequestPath = requestPath.substring(requestPath.indexOf("/", 1));

        // Replace path variables with regex to match any value
        String regexPath = path.replaceAll("\\{[^/]+\\}", "[^/]+");

        // Check if the request path matches the secured route pattern
        return requestPath.matches(regexPath);
    }
}