package com.nexus.api;

import com.nexus.base.BaseTest;
import io.restassured.RestAssured;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;

public class DatabaseIntegrationTest extends BaseTest {

    @Test(description = "NEX-501: Verify application response when querying active user records")
    public void testSuccessfulDatabaseFetch() {
        // Ensure routing uses our verified loopback address
        if (RestAssured.baseURI == null) {
            RestAssured.baseURI = "http://127.0.0.1:8081";
        }

        System.out.println(">> Executing DB Integration Test against: " + RestAssured.baseURI);

        given()
                .queryParam("client_id", "USR-99")
                .header("Accept", "application/json")
                .when()
                .get("/api/db/records")
                .then()
                .statusCode(200)
                .body("rows[0].name", equalTo("Alex Mercer"))
                .body("rows[0].status", equalTo("ACTIVE"));

        System.out.println(">> [SUCCESS] Database query simulation verified successfully.");
    }

    @Test(description = "NEX-502: Verify fault tolerance when the database throws a connection timeout")
    public void testDatabaseTimeoutResilience() {
        given()
                .queryParam("client_id", "USR-ERR")
                .header("Accept", "application/json")
                .when()
                .get("/api/db/records")
                .then()
                .statusCode(500)
                .body("error", containsString("Connection timed out"));

        System.out.println(">> [SUCCESS] Fault tolerance mechanism handled the simulated database crash gracefully.");
    }
}
