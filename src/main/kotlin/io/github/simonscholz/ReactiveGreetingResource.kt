package io.github.simonscholz

import io.github.simonscholz.adapter.auth.AuthRestClient
import io.github.simonscholz.adapter.auth.Token
import io.github.simonscholz.adapter.beverage.Beverage
import io.github.simonscholz.adapter.beverage.BeverageRestClient
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.eclipse.microprofile.rest.client.inject.RestClient
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/")
class ReactiveGreetingResource(
    @RestClient private val authRestClient: AuthRestClient,
    @RestClient private val beverageRestClient: BeverageRestClient,
) {

    @GET
    @Path("auth")
    @Produces(MediaType.TEXT_PLAIN)
    suspend fun auth(): String {
        val awaitSuspending: Token = authRestClient.fetchClientToken().awaitSuspending()
        return awaitSuspending.toString()
    }

    @GET
    @Path("beverages")
    @Produces(MediaType.TEXT_PLAIN)
    suspend fun beverages(): String {
        val awaitSuspending: List<Beverage> = beverageRestClient.fetchBeverages().awaitSuspending()
        return awaitSuspending.toString()
    }
}
