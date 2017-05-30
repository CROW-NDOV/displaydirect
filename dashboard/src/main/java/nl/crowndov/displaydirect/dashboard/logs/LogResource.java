package nl.crowndov.displaydirect.dashboard.logs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
@Path("/v1/log")
@Produces(MediaType.APPLICATION_JSON)
public class LogResource {

    @GET
    public Response getAll() {
        return Response.ok(LogStore.get()).build();
    }

    @GET
    @Path("{stopSystemId}")
    public Response getForStop(@PathParam("stopSystemId") String stopSystemId) {
        return Response.ok(LogStore.get(stopSystemId)).build();
    }
}
