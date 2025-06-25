package exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


@Provider
public class BankBranchNotFoundException extends RuntimeException implements ExceptionMapper<BankBranchNotFoundException> {

    @Override
    public Response toResponse(BankBranchNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
