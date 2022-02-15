package io.github.simonscholz

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.ContainsPattern
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import io.github.simonscholz.adapter.auth.Token
import io.github.simonscholz.adapter.beverage.Beverage
import io.github.simonscholz.wiremock.InjectWireMock
import io.github.simonscholz.wiremock.WireMockQuarkusTestResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.inject.Inject
import javax.ws.rs.core.HttpHeaders

@QuarkusTest
@QuarkusTestResource(value = WireMockQuarkusTestResource::class, restrictToAnnotatedClass = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReactiveResourceTest {

    @Inject
    lateinit var objectMapper: ObjectMapper

    @InjectWireMock
    lateinit var wireMockServer: WireMockServer

    @BeforeAll
    fun init() {
        wireMockServer.stubFor(
            WireMock.post("/token").willReturn(
                WireMock.aResponse().withHeader("Content-Type", "application/json")
                    .withBody(
                        toJson(Token(
                            accessToken = "secret-token",
                            expiresIn = 3000,
                            tokenType = "Bearer",
                        ))?.trimIndent()
                    )
            )
        )

        val flens = Beverage(brand = "Flensburger", name = "Pils")

        val beverages = listOf(flens, flens.copy(name = "Edles Helles"), flens.copy(name = "Catch the cat"))

        wireMockServer.stubFor(
            WireMock.get("/v1/beverage/beverages").willReturn(
                WireMock.aResponse().withHeader("Content-Type", "application/json")
                    .withBody(
                        toJson(beverages)?.trimIndent()
                    )
            )
        )
        println("Started Wiremock")
    }

    @Test
    fun testAuthEndpoint() {
        given()
          .`when`().get("/auth")
          .then()
             .statusCode(200)
             .body(`is`("Token(accessToken=secret-token, expiresIn=3000, tokenType=Bearer)"))
    }

    @Test
    fun testBeveragesEndpoint() {
        given()
            .`when`().get("/beverages")
            .then()
            .statusCode(200)
            .body(`is`("[Beverage(brand=Flensburger, name=Pils), Beverage(brand=Flensburger, name=Edles Helles), Beverage(brand=Flensburger, name=Catch the cat)]"))

        val findAllRequests = wireMockServer.findAll(RequestPatternBuilder.allRequests())
        println(findAllRequests)

        wireMockServer.verify(RequestPatternBuilder().withHeader(HttpHeaders.AUTHORIZATION, ContainsPattern("Bearer")))
    }

    private fun toJson(requestObject: Any?): String? {
        return try {
            objectMapper.writeValueAsString(requestObject)
        } catch (e: Exception) {
            throw IllegalStateException("ObjectMapper was unable to convert requestObject to JSON in ITs.")
        }
    }
}
