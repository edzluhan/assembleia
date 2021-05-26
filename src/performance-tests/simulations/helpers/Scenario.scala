package helpers

import io.gatling.core.Predef._
import io.gatling.core.feeder.FeederBuilder
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.request.builder.HttpRequestBuilder

abstract class Scenario extends Simulation {

  private val (users, duration) = TestParameters.getTestParameters

  private val httpConf: HttpProtocolBuilder = http
    .baseUrl(baseUrl)
    .contentTypeHeader("application/json")
    .userAgentHeader("performance-test")
    .acceptHeader("application/json,application/json;charset=utf-8")

  private val scn: ScenarioBuilder = scenario(scenarioName)
    .feed(feeder())

  setUp(
    scn.inject(
      constantUsersPerSec(users) during duration
    ).protocols(httpConf)
  )

  def requestBuilder: HttpRequestBuilder

  def feeder: FeederBuilder

  def scenarioName: String

  def baseUrl: String = "http://localhost:8080"
}
