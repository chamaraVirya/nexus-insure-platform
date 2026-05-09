package com.nexus.api;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ClaimLifecycleTest {

    @Test(description = "NEX-401: Verify claim status transition")
    public void testClaimStatusTransition() {
        String claimId = "CLM-STATE-01";

        // Call 1: Expect PENDING
        given()
                .header("Accept", "application/json") // Placement: Inside the .given() block
                .header("Content-Type", "application/json")
                .when()
                .get("/api/claims/" + claimId)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("status", equalTo("PENDING"));

        // Call 2: Expect APPROVED (State has transitioned)
        given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .when()
                .get("/api/claims/" + claimId)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("status", equalTo("APPROVED"));
    }
}
