package com.nexus.api;

import com.nexus.base.BaseTest;
import io.restassured.RestAssured;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ClaimLifecycleTest extends BaseTest {

    @Test(description = "NEX-401: Verify claim status transition from PENDING to APPROVED")
    public void testClaimStatusTransition() {
        // Maintain the verified literal routing path
        RestAssured.baseURI = "http://127.0.0.1:8081";
        String claimId = "CLM-STATE-01";

        System.out.println(">> [PHASE 1] Fetching initial state for: " + claimId);

        // Call 1: Baseline Verification (Expects PENDING and triggers state shift)
        given()
                .header("Accept", "application/json")
                .when()
                .get("/api/claims/" + claimId)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("status", equalTo("PENDING"));

        System.out.println(">> [PHASE 2] Fetching mutated state for: " + claimId);

        // Call 2: State Transition Verification (Expects APPROVED)
        given()
                .header("Accept", "application/json")
                .when()
                .get("/api/claims/" + claimId)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("status", equalTo("APPROVED"));

        System.out.println(">> [SUCCESS] Finite State Machine transitioned smoothly.");
    }
}

