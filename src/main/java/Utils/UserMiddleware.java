package Utils;

import User.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter(filterName="userFilter", urlPatterns="/*")
public class UserMiddleware implements Filter {

    @Inject
    private UserService service;

    @Inject
    private CurrentRequestData currentRequestData;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String userIdHeader = httpRequest.getHeader("UserId");

        if (userIdHeader != null) {
            try {
                Long userId = Long.valueOf(userIdHeader);
                var user = service.getById(userId);
                if (user != null) {
                    currentRequestData.setUser(user);
                }
            } catch (NumberFormatException e) {
                //e.printStackTrace();
            } catch (Exception ignored){

            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
