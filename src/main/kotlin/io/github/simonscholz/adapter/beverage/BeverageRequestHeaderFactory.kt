package io.github.simonscholz.adapter.beverage

import io.github.simonscholz.adapter.auth.AuthRestClient
import io.quarkus.rest.client.reactive.ReactiveClientHeadersFactory
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.util.Base64
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MultivaluedHashMap
import javax.ws.rs.core.MultivaluedMap

class BeverageRequestHeaderFactory(
    @RestClient private val authRestClient: AuthRestClient,
) : ReactiveClientHeadersFactory() {

    override fun getHeaders(incomingHeaders: MultivaluedMap<String, String>?): Uni<MultivaluedMap<String, String>> {
        return authRestClient.fetchClientToken().onItem().transform {
            MultivaluedHashMap<String, String>().apply {
                add(HttpHeaders.CACHE_CONTROL, "no-cache")
                add(HttpHeaders.AUTHORIZATION, "${it.tokenType} ${it.accessToken}")
            }
        }
    }
}
