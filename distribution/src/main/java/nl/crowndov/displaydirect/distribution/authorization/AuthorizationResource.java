package nl.crowndov.displaydirect.distribution.authorization;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
@Path("authorize")
public class AuthorizationResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response validate(@QueryParam("token") String token) {
        if (token == null || token.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (AuthorizationWhitelist.validate(token)) {
            return Response.ok("Aanmelden succesvol").build();
        } else {
            return Response.ok("Fout tijdens het aanmelden").build();
        }

    }

}
