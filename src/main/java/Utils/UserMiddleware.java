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
import jakarta.ws.rs.NotAuthorizedException;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@WebFilter(filterName="userFilter", urlPatterns="/*")
public class UserMiddleware implements Filter {

    @Inject
    private AuthService service;

    @Inject
    private CurrentRequestData currentRequestData;

    private JWTVerifier jwtVerifier;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        RSAKeyProvider keyProvider = new CognitoRSAKeyProvider();
        Algorithm algorithm = Algorithm.RSA256(keyProvider);
        jwtVerifier = JWT.require(algorithm).build();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
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
                if (user != null) {
                    currentRequestData.setUser(user);
                }
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

    @Override
    public void destroy() {
    }
}
