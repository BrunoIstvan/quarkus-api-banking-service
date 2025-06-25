package controller;

import domain.BankBranches;
import domain.entity.BankBranch;
import domain.entity.Address;
import exceptions.InactiveBankBranchException;
import exceptions.BankBranchNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;
import service.BankBranchService;

@Path("/agencias")
public class BankBranchController {

    private final BankBranchService service;

    public BankBranchController(BankBranchService service) {
        this.service = service;
    }

    @POST
    @Transactional
    public RestResponse<Void> register(BankBranch bankBranch, @Context UriInfo uriInfo)
            throws BankBranchNotFoundException, InactiveBankBranchException {
        this.service.register(bankBranch);
        return RestResponse.created(uriInfo.getAbsolutePath());
    }

    @PUT
    @Transactional
    public RestResponse<Void> update(BankBranch bankBranch) {
        System.out.println(bankBranch.toString());
        this.service.update(bankBranch);
        return RestResponse.accepted();
    }

    @PUT
    @Path("{idAgencia}/endereco")
    @Transactional
    public RestResponse<Void> updateAddress(@PathParam("idAgencia") int idAgencia, Address address) {
        this.service.updateAddress(idAgencia, address);
        return RestResponse.accepted();
    }

    @GET
    @Path("{id}")
    public RestResponse<BankBranch> getById(int id) {
        return RestResponse.ok(service.getById(id));
    }

    @GET
    public RestResponse<BankBranches> listAll() {
        return RestResponse.ok(service.listAll());
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public RestResponse<Void> deleteById(int id) {
        service.deleteById(id);
        return RestResponse.ok();
    }

}
