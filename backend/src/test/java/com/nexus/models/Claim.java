package com.nexus.models;

public class Claim {
    public String claimId;
    public String policyNumber;
    public Double amount;
    public String status;

    // A simple constructor for the 1%
    public Claim(String claimId, String policyNumber, Double amount, String status) {
        this.claimId = claimId;
        this.policyNumber = policyNumber;
        this.amount = amount;
        this.status = status;
    }
}