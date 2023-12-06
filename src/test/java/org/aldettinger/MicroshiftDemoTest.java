package org.aldettinger;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

@QuarkusTest
@QuarkusTestResource(ActiveMQTestResource.class)
class MicroshiftDemoTest {

    List<String> validStatuses = Arrays.asList("The whole factory line is DOWN",
            "The whole factory line is UP",
            "Some devices on the factory line are not ready");

    @Test
    void microshiftDemoShouldEndUpWithNonDefaultConsolidatedStatus() {
        Awaitility.await().atMost(Duration.ofSeconds(5)).until(() -> {
            String consolidateStatus = RestAssured
                    .when()
                    .get("/consolidated-status")
                    .then()
                    .extract().asString();

            return validStatuses.contains(consolidateStatus);
        });
    }
}
