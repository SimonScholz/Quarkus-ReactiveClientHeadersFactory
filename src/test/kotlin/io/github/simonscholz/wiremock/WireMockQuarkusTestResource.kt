package io.github.simonscholz.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager

class WireMockQuarkusTestResource : QuarkusTestResourceLifecycleManager {

    private lateinit var wireMockServer: WireMockServer
    /**
     * Start the test resource.
     *
     * @return A map of system properties that should be set for the running test
     */
    override fun start(): Map<String, String> {
        wireMockServer = WireMockServer(wireMockConfig().dynamicPort())
        wireMockServer.start()

        val baseUrl = wireMockServer.baseUrl()

        println(baseUrl)

        return mapOf(
            Pair("quarkus.rest-client.auth.url", wireMockServer.baseUrl()),
            Pair("quarkus.rest-client.beverage.url", wireMockServer.baseUrl())
        )
    }

    override fun stop() {
        wireMockServer.stop()
    }

    override fun inject(testInjector: QuarkusTestResourceLifecycleManager.TestInjector) {
        testInjector.injectIntoFields(
            wireMockServer,
            QuarkusTestResourceLifecycleManager.TestInjector.AnnotatedAndMatchesType(InjectWireMock::class.java, WireMockServer::class.java)
        )
    }
}