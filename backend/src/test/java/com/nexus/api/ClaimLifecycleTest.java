package com.nexus.api;

import com.nexus.base.BaseTest;
import io.restassured.RestAssured;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ClaimLifecycleTest extends BaseTest {

    @Test(description = "NEX-401: Verify claim status transition from PENDING to APPROVED")
    public void testClaimStatusTransition() {
        String claimId = "CLM-STATE-01";

        System.out.println("DEBUG RUNTIME URL: [" + RestAssured.baseURI + "/api/claims/" + claimId + "]");

        System.out.println(">> Target endpoint from configuration: " + RestAssured.baseURI);

        // Phase 1: Verify Initial State (Fires the first mapping)
        given()
                .header("Accept", "application/json")
                .when()
                .get("/api/claims/" + claimId)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("status", equalTo("PENDING"));

        // Phase 2: Verify Mutated State (Fires the second mapping via Scenario state)
        given()
                .header("Accept", "application/json")
                .when()
                .get("/api/claims/" + claimId)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("status", equalTo("APPROVED"));

        System.out.println(">> [SUCCESS] Verification complete for " + claimId);
    }

}

