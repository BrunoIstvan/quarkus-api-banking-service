package service.http;

import domain.http.BankBranchHttp;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/situacao-cadastral")
@RegisterRestClient(configKey = "situacao-cadastral-api")
public interface RegistrationStatusHttpService {

    @GET
    @Path("{cnpj}")
    BankBranchHttp buscarPorCnpj(String cnpj);

}

