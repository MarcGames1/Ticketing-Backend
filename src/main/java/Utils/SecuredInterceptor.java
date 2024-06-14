package Utils;

import Enums.EmployeeRole;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Secured
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class SecuredInterceptor {

    @Inject
    private CurrentRequestData currentRequestData;

    @AroundInvoke
    public Object checkUserRole(InvocationContext context) throws Exception {
        var user = currentRequestData.getUser();
        if (user == null) {
            throw new NotFoundException("User not found in the current request");
        }

        var secured = context.getMethod().getAnnotation(Secured.class);
        if (secured == null) {
            secured = context.getTarget().getClass().getAnnotation(Secured.class);
        }

        if (secured != null) {
            Set<EmployeeRole> requiredRoles = new HashSet<>(Arrays.asList(secured.roles()));
            if (!requiredRoles.isEmpty() && (user.getRole() == null || !requiredRoles.contains(user.getRole()))) {
                throw new ForbiddenException("User does not have the necessary role");
            }
        }

        return context.proceed();
    }
}
