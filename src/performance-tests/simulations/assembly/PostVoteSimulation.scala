package assembly

import java.util.UUID

import helpers.Scenario
import io.gatling.core.Predef._
import io.gatling.core.feeder.FeederBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

class PostVoteSimulation extends Scenario {

    override def scenarioName: String = "Post vote"

    def randomUserId() : String = {
        return UUID.randomUUID.toString
    }

    override def requestBuilder: HttpRequestBuilder =  http("Post vote")
                    .post("/vote")
                    .header("Authorization", "Bearer ${authToken}")
                    .header("Content-Type", "application/json")
                    .body(ElFileBody("postVoteBody.json")).asJson
                    .check(status is 201)


    override def feeder: FeederBuilder = Iterator.continually(Map("user_id" -> randomUserId()))
}

