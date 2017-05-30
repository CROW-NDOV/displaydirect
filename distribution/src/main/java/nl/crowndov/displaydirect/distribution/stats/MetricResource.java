package nl.crowndov.displaydirect.distribution.stats;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
@Path("/v1/metrics")
public class MetricResource {

    private static final MetricStore store = MetricStore.getInstance();

    @GET
    public Response getMetrics() {
       return Response.ok(store.getMetrics()).build();
    }

    @GET
    @Path("/{name}")
    public Response getMetric(@PathParam("name") String name) {
        return Response.ok(store.getMetric(name)).build();
    }

}
