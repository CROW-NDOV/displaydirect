package nl.crowndov.displaydirect.distribution.resources;

import nl.crowndov.displaydirect.distribution.chb.StopStore;
import nl.crowndov.displaydirect.distribution.input.TimingPointProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
@Path("/v1/stop")
public class StopResource {

    @GET
    public Response get() {
        return Response.ok(StopStore.getStore()).build();
    }


    @GET
    @Path("/missing")
    public Response getMissing() {
        return Response.ok(StopStore.getMissing()).build();
    }

    @GET
    @Path("/timingpoint")
    public Response getTimingPoints() {
        return Response.ok(TimingPointProvider.getAll()).build();
    }

}
