package com.nexus.base;

import io.restassured.RestAssured;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class BaseTest {
    protected static Properties properties;
    protected static String wiremockAdminUri;

    @BeforeSuite(alwaysRun = true)
    public void setup() {
        System.out.println(">> [ORCHESTRATION] Initializing localized container architecture...");
        try {
            // Automatically spin up Docker Compose from Java
            ProcessBuilder processBuilder = new ProcessBuilder("docker-compose", "up", "-d");
            Process process = processBuilder.start();
            process.waitFor(); // Wait for the docker command to finish executing

            // Give the containers 3 seconds to fully initialize internal services
            Thread.sleep(3000);
            System.out.println(">> [ORCHESTRATION] Containers deployed successfully.");
        } catch (Exception e) {
            System.out.println(">> [WARNING] Automatic container deployment failed. Falling back to manual loopback configuration.");
        }

        // Load Properties
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            properties.load(fis);
            RestAssured.baseURI = properties.getProperty("base.uri");
            wiremockAdminUri = properties.getProperty("wiremock.admin.uri");
        } catch (IOException e) {
            throw new RuntimeException("Critical: Could not load config.properties");
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() {
        System.out.println(">> [ORCHESTRATION] Deprovisioning local container environment...");
        try {
            // Automatically tear down the infrastructure to free up system memory
            ProcessBuilder processBuilder = new ProcessBuilder("docker-compose", "down");
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println(">> [ORCHESTRATION] Environment pristine. All ports liberated.");
        } catch (Exception e) {
            System.out.println(">> [ERROR] Failed to automatically tear down containers.");
        }
    }
}