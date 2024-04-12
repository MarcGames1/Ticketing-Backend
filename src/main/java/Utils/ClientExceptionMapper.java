package Utils;

import jakarta.json.Json;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ClientExceptionMapper implements ExceptionMapper<ClientErrorException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(final ClientErrorException exception) {
        final var jsonObject = Json.createObjectBuilder()
                .add("host",uriInfo.getAbsolutePath().getHost())
                .add("resource", uriInfo.getAbsolutePath().getPath())
                .add("title", "HTTP Errors")
                .add("status", exception.getResponse().getStatus())
                .add("message", exception.getMessage());

        return Response.status(exception.getResponse().getStatus()).entity(jsonObject.build()).build();
    }
}
