package utilities;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiTestLogger {
    private static final ThreadLocal<Map<String, String>> testContext = new ThreadLocal<>();

    public static void initializeTest(String testName) {
        Map<String, String> context = new HashMap<>();
        context.put("testName", testName);
        context.put("startTime", String.valueOf(System.currentTimeMillis()));
        testContext.set(context);
    }

    public static void logRequest(RequestSpecification requestSpec) {
        try {
            QueryableRequestSpecification queryableRequest = SpecificationQuerier.query(requestSpec);
            
            StringBuilder requestDetails = new StringBuilder();
            requestDetails.append("Request Details:\n");
            requestDetails.append("Method: ").append(queryableRequest.getMethod()).append("\n");
            requestDetails.append("URL: ").append(queryableRequest.getURI()).append("\n");
            
            requestDetails.append("\nHeaders:\n");
            queryableRequest.getHeaders().forEach(header ->
                requestDetails.append(header.getName()).append(": ").append(header.getValue()).append("\n"));
            
            if (!queryableRequest.getQueryParams().isEmpty()) {
                requestDetails.append("\nQuery Parameters:\n");
                queryableRequest.getQueryParams().forEach((key, value) ->
                    requestDetails.append(key).append(": ").append(value).append("\n"));
            }
            
            if (queryableRequest.getBody() != null) {
                requestDetails.append("\nRequest Body:\n");
                try {
                    JSONObject jsonBody = new JSONObject(queryableRequest.getBody().toString());
                    requestDetails.append(jsonBody.toString(2));
                } catch (Exception e) {
                    requestDetails.append(queryableRequest.getBody().toString());
                }
            }
            
            Allure.addAttachment("API Request", "text/plain", requestDetails.toString());
            
        } catch (Exception e) {
            System.err.println("Request logging error: " + e.getMessage());
        }
    }

    public static void logResponse(Response response) {
        try {
            StringBuilder responseDetails = new StringBuilder();
            responseDetails.append("Response Details:\n");
            responseDetails.append("Status Code: ").append(response.getStatusCode()).append("\n");
            responseDetails.append("Status Line: ").append(response.getStatusLine()).append("\n");
            
            responseDetails.append("\nHeaders:\n");
            response.getHeaders().forEach(header ->
                responseDetails.append(header.getName()).append(": ").append(header.getValue()).append("\n"));
            
            responseDetails.append("\nResponse Time: ").append(response.getTime()).append("ms\n");
            
            responseDetails.append("\nResponse Body:\n");
            try {
                JSONObject jsonBody = new JSONObject(response.getBody().asString());
                responseDetails.append(jsonBody.toString(2));
            } catch (Exception e) {
                responseDetails.append(response.getBody().asString());
            }
            
            Allure.addAttachment("API Response", "text/plain", responseDetails.toString());
            
        } catch (Exception e) {
            System.err.println("Response logging error: " + e.getMessage());
        }
    }

    public static void logTestSummary(boolean success, String errorMessage) {
        try {
            Map<String, String> context = testContext.get();
            if (context != null) {
                long duration = System.currentTimeMillis() - Long.parseLong(context.get("startTime"));
                
                StringBuilder summary = new StringBuilder();
                summary.append("Test Summary:\n");
                summary.append("Test Name: ").append(context.get("testName")).append("\n");
                summary.append("Duration: ").append(duration).append("ms\n");
                summary.append("Status: ").append(success ? "✅ PASSED" : "❌ FAILED").append("\n");
                
                if (!success && errorMessage != null) {
                    summary.append("\nError Details:\n").append(errorMessage);
                }
                
                Allure.addAttachment("Test Summary", "text/plain", summary.toString());
                
                testContext.remove();
            }
        } catch (Exception e) {
            System.err.println("Test summary logging error: " + e.getMessage());
        }
    }

    @Attachment(value = "API Test Results", type = "text/plain")
    public static String getTestResults(String testName, Response response) {
        StringBuilder results = new StringBuilder();
        results.append("Test: ").append(testName).append("\n\n");
        
        results.append("Status Code: ").append(response.getStatusCode()).append("\n");
        results.append("Response Time: ").append(response.getTime()).append("ms\n");
        
        results.append("\nResponse Body:\n");
        try {
            JSONObject jsonBody = new JSONObject(response.getBody().asString());
            results.append(jsonBody.toString(2));
        } catch (Exception e) {
            results.append(response.getBody().asString());
        }
        
        return results.toString();
    }

    public static void logValidationResults(Map<String, Boolean> validations) {
        StringBuilder validationDetails = new StringBuilder();
        validationDetails.append("Validation Results:\n\n");
        
        validations.forEach((check, result) -> {
            String line = String.format("%s %s\n", 
                result ? "✅" : "❌",
                check);
            validationDetails.append(line);
        });
        
        Allure.addAttachment("Validations", "text/plain", validationDetails.toString());
    }

    public static void logEnvironmentInfo() {
        StringBuilder envInfo = new StringBuilder();
        envInfo.append("Environment Information:\n\n");
        
        envInfo.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        envInfo.append("OS: ").append(System.getProperty("os.name")).append("\n");
        envInfo.append("OS Version: ").append(System.getProperty("os.version")).append("\n");
        envInfo.append("REST Assured Version: ").append(io.restassured.RestAssured.class.getPackage().getImplementationVersion()).append("\n");
        
        Allure.addAttachment("Environment Info", "text/plain", envInfo.toString());
    }
}
