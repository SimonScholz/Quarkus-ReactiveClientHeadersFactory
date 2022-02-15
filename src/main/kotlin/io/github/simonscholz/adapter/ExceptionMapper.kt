package io.github.simonscholz.adapter

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.Response

class ExceptionMapper : ResponseExceptionMapper<Throwable> {
    override fun toThrowable(response: Response): Throwable {
        return WebApplicationException(response)
    }

    override fun handles(status: Int, headers: MultivaluedMap<String, Any>?): Boolean {
        return status >= 400
    }

    override fun getPriority(): Int {
        return Int.MAX_VALUE
    }
}
