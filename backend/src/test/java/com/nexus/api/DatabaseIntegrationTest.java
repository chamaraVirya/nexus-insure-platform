package com.nexus.api;

import com.nexus.base.BaseTest;
import io.restassured.RestAssured;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;

public class DatabaseIntegrationTest extends BaseTest {

    @Test(description = "NEX-501: Verify application response and enforce contract schema integrity")
    public void testSuccessfulDatabaseFetch() {
        // Fallback safety block to enforce localized network routing configurations
        if (RestAssured.baseURI == null) {
            RestAssured.baseURI = "http://127.0.0.1:8081";
        }

        System.out.println(">> Executing Contract Verification against: " + RestAssured.baseURI);

        given()
                .queryParam("client_id", "USR-99")
                .header("Accept", "application/json")
                .when()
                .get("/api/db/records")
                .then()
                .statusCode(200)
                .body("rows[0].name", equalTo("Alex Mercer"))
                .body("rows[0].status", equalTo("ACTIVE"))
                // CRITICAL: Forces structural payload validation against our JSON schema definition
                .body(matchesJsonSchemaInClasspath("schemas/database-record-schema.json"));

        System.out.println(">> [SUCCESS] Database response strictly complies with the structural contract schema.");
    }

    @Test(description = "NEX-502: Verify fault tolerance when the database throws a connection timeout")
    public void testDatabaseTimeoutResilience() {
        // Fallback safety block to enforce localized network routing configurations
        if (RestAssured.baseURI == null) {
            RestAssured.baseURI = "http://127.0.0.1:8081";
        }

        System.out.println(">> Executing Fault-Injection Simulation against: " + RestAssured.baseURI);

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