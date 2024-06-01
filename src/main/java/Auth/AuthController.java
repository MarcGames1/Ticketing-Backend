package Auth;

import Auth.DTO.LoginDTO;
import Auth.DTO.SignupDTO;
import Shared.DTO.AuthResponse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@Path("/auth")
@RequestScoped
public class AuthController {

    @Inject
    private AuthService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public AuthResponse login(@Valid LoginDTO dto){
        return service.login(dto);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/signup")
    public AuthResponse signup(@Valid SignupDTO dto){
        return service.signup(dto);
    }
}
