package com.nexus.api;

import com.nexus.base.BaseTest;
import com.nexus.models.Claim;
import io.restassured.http.ContentType;
import org.testng.annotations.DataProvider;
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

    // The "Data Factory"
    @DataProvider(name = "claimScenarios")
    public Object[][] claimData() {
        return new Object[][]{
                {"CLM-AUTO-01", "POL-999", 1500.00, "Motor Accident"},
                {"CLM-HOME-02", "POL-888", 55000.00, "Property Fire"},
                {"CLM-PET-03", "POL-777", 120.50, "Vet Visit"}
        };
    }

    @Test(dataProvider = "claimScenarios", description = "NEX-301: Bulk Claim Validation")
    public void testBulkClaimSubmission(String id, String policy, Double amount, String type) {
        // We create a new Claim object for each row in the DataProvider
        Claim claim = new Claim(id, policy, amount, "SUBMITTED");

        System.out.println(">> Testing Insurance Type: " + type);

        given()
                .contentType(ContentType.JSON)
                .body(claim)
                .when()
                .post("/api/claims")
                .then()
                .statusCode(201)
                .body("status", equalTo("Accepted"));

    }

    @Test(description = "NEX-302: Verify rejection of zero-amount claims")
    public void testRejectedClaim() {
        // A claim that should trigger our new stub
        Claim invalidClaim = new Claim("CLM-FAIL", "POL-000", 0.0, "REJECTED");

        given()
                .contentType(ContentType.JSON)
                .body(invalidClaim)
                .when()
                .post("/api/claims")
                .then()
                .statusCode(400) // Expecting the rejection code
                .body("error", equalTo("InvalidAmount"));
    }

}