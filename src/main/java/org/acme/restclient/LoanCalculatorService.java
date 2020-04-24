package org.acme.restclient;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;



@RegisterRestClient

public interface LoanCalculatorService {

    @POST
    @Path("/containers/LoanPreQualification_1.0.0-SNAPSHOT/dmn")
    @Consumes("application/json")
    @ClientHeaderParam(name="Authorization", value="Basic cGFtQWRtaW46cmVkaGF0cGFtMSE=")
    String checkQual(String json);


}
