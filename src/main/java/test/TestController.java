package test;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/test")
@RequestScoped
public class TestController {
    @GET
    public  Response  getTestResponse() {
        try{
            Dotenv dotenv = Dotenv.load();
            String testVariable = dotenv.get("TEST_VARIABLE");
            return Response.ok(testVariable).build();
        } catch (Exception e){
        e.printStackTrace();

        return Response.serverError().build();
    }
}
}