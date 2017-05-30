package nl.crowndov.displaydirect.virtual_screen.resources;

import nl.crowndov.displaydirect.commonclient.store.DataStore;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
@Path("/v1/departures")
@Produces(MediaType.APPLICATION_JSON)
public class DepartureResource {

    @GET
    public Response getDepartures() {
        return Response.ok(DataStore.getDepartures()).build();
    }

    @GET
    @Path("/all")
    public Response getAllDepartures() {
        return Response.ok(DataStore.getAll()).build();
    }
}
