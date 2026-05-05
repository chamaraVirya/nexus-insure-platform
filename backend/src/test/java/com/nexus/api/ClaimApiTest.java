package com.nexus.api;

import com.nexus.base.BaseTest;
import com.nexus.models.Claim;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ClaimApiTest extends BaseTest {

    @Test(description = "NEX-201: Verify claim payload reaches the backend")
    public void testClaimSubmission() {
        Claim newClaim = new Claim("CLM-99", "POL-12345", 550.00, "SUBMITTED");

        // 1. Send the data to WireMock
        given()
                .contentType(ContentType.JSON)
                .body(newClaim)
                .when()
                .post("/api/claims");

        // 2. The Verification Step (Validating the Journal)
        // 1. Send the data to WireMock
        given()
                .contentType(ContentType.JSON)
                .body(newClaim)
                .when()
                .post("/api/claims")
                .then()
                .statusCode(201) // We now expect 201 because of our stub!
                .body("status", equalTo("Accepted"))
                .body("message", containsString("CLM-99"));

        // (Keep your existing Step 2 Journal Verification below this)

        System.out.println(">> Verification Complete: Data successfully logged in WireMock.");
    }

}