package io.github.simonscholz.adapter.beverage

import io.github.simonscholz.adapter.ExceptionMapper
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@RegisterRestClient(configKey = "beverage")
@RegisterProvider(ExceptionMapper::class)
@RegisterClientHeaders(BeverageRequestHeaderFactory::class)
interface BeverageRestClient {

    @GET
    @Path("/v1/beverage/beverages")
    @Produces(MediaType.APPLICATION_JSON)
    fun fetchBeverages(): Uni<List<Beverage>>
}

data class Beverage(
    val brand: String,
    val name: String,
)
