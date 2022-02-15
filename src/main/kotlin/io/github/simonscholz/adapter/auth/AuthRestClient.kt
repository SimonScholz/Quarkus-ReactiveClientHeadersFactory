package io.github.simonscholz.adapter.auth

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.simonscholz.adapter.ExceptionMapper
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.Consumes
import javax.ws.rs.FormParam
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@RegisterRestClient(configKey = "auth")
@RegisterProvider(ExceptionMapper::class)
@RegisterClientHeaders(AuthRequestHeaderFactory::class)
interface AuthRestClient {

    @POST
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    fun fetchClientToken(): Uni<Token>
}

data class Token(
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("expires_in") val expiresIn: Long,
    @JsonProperty("token_type") val tokenType: String,
)
