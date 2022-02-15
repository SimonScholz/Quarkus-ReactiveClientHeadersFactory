package io.github.simonscholz.adapter.auth

import io.quarkus.rest.client.reactive.ReactiveClientHeadersFactory
import io.smallrye.mutiny.Uni
import javax.ws.rs.core.MultivaluedHashMap
import javax.ws.rs.core.MultivaluedMap

class AuthRequestHeaderFactory : ReactiveClientHeadersFactory() {

    override fun getHeaders(incomingHeaders: MultivaluedMap<String, String>?): Uni<MultivaluedMap<String, String>> {
        return Uni.createFrom().item(
            MultivaluedHashMap<String, String>().apply {
                add("Cache-Control", "no-cache")
                add("Accept-Encoding", "identity")
                add("Authorization", "Basic secret")
            }
        )
    }
}
