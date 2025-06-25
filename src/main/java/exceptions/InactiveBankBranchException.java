package exceptions;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InactiveBankBranchException extends RuntimeException implements ExceptionMapper<InactiveBankBranchException> {

    @Override
    public Response toResponse(InactiveBankBranchException exception) {
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }

}

