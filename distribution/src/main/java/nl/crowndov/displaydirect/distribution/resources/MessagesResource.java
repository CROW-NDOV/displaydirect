package nl.crowndov.displaydirect.distribution.resources;

import nl.crowndov.displaydirect.distribution.input.QuayDataProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
@Path("/v1/message")
public class MessagesResource {

    @GET
    public Response get() {
        return Response.ok(QuayDataProvider.getAllMessages()).build();
    }



}
