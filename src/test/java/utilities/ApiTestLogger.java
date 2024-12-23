package utilities;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiTestLogger {
    private static final ThreadLocal<Map<String, Object>> testContext = new ThreadLocal<Map<String, Object>>() {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<>();
        }
    };

    public static void initializeTest(String testName) {
        Map<String, Object> context = testContext.get();
        context.clear();
        context.put("testName", testName);
        context.put("startTime", System.currentTimeMillis());
        System.out.println(" Test başlatıldı: " + testName);
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
            e.printStackTrace();
        }
    }

    public static void logResponse(Response response) {
        try {
            Map<String, Object> context = testContext.get();
            
            if (response == null) {
                context.put("lastStatusCode", 0);
                context.put("lastResponseBody", null);
                System.out.println(" API Yanıtı null");
                return;
            }
            
            int statusCode = response.getStatusCode();
            String responseBody = response.getBody() != null ? response.getBody().asString() : "";
            
            // Son yanıt bilgilerini sakla
            context.put("lastStatusCode", statusCode);
            context.put("lastResponseBody", responseBody);
            
            System.out.println(" API Yanıtı kaydedildi - Status: " + statusCode);
            
            StringBuilder responseDetails = new StringBuilder();
            responseDetails.append("Response Details:\n");
            responseDetails.append("Status Code: ").append(statusCode).append("\n");
            responseDetails.append("Status Line: ").append(response.getStatusLine()).append("\n");
            
            responseDetails.append("\nHeaders:\n");
            response.getHeaders().forEach(header ->
                responseDetails.append(header.getName()).append(": ").append(header.getValue()).append("\n"));
            
            responseDetails.append("\nResponse Time: ").append(response.getTime()).append("ms\n");
            
            responseDetails.append("\nResponse Body:\n");
            try {
                if (responseBody != null && !responseBody.isEmpty()) {
                    if (responseBody.trim().startsWith("{")) {
                        JSONObject jsonBody = new JSONObject(responseBody);
                        responseDetails.append(jsonBody.toString(2));
                    } else if (responseBody.trim().startsWith("<!DOCTYPE html>")) {
                        responseDetails.append("HTML Response (truncated):\n")
                                    .append(responseBody.substring(0, Math.min(1000, responseBody.length())))
                                    .append("\n...");
                    } else {
                        responseDetails.append(responseBody);
                    }
                } else {
                    responseDetails.append("Empty response body");
                }
            } catch (Exception e) {
                responseDetails.append("Error parsing response body: ").append(e.getMessage())
                             .append("\nRaw response body:\n").append(responseBody);
            }
            
            Allure.addAttachment("API Response", "text/plain", responseDetails.toString());
            
        } catch (Exception e) {
            System.err.println("Response logging error: " + e.getMessage());
            e.printStackTrace();
            logError(e);
        }
    }

    public static int getLastResponseStatusCode() {
        Map<String, Object> context = testContext.get();
        return context.containsKey("lastStatusCode") ? (int) context.get("lastStatusCode") : 0;
    }

    public static String getLastResponseBody() {
        try {
            Map<String, Object> context = testContext.get();
            Object body = context.get("lastResponseBody");
            if (body != null) {
                String responseBody = (String) body;
                System.out.println(" Son yanıt body uzunluğu: " + responseBody.length());
                return responseBody;
            }
        } catch (Exception e) {
            System.err.println("Error getting last response body: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println(" Response body bulunamadı");
        return "";
    }

    public static void saveTestSummary(String scenarioName, boolean success) {
        Map<String, Object> context = testContext.get();
        int statusCode = getLastResponseStatusCode();
        String responseBody = (String) context.get("lastResponseBody");
        
        StringBuilder summary = new StringBuilder();
        summary.append("Senaryo: ").append(scenarioName).append("\n");
        summary.append("Durum: ").append(success ? "✅ PASSED" : "❌ FAILED").append("\n");
        summary.append("Süre: ").append((System.currentTimeMillis() - (long) context.get("startTime")) / 1000.0).append(" saniye\n");
        summary.append("Status Code: ").append(statusCode).append("\n");
        
        if (!success) {
            summary.append("Hata: Status Code ").append(statusCode);
        }
        
        summary.append("\n\nSon API Yanıtı:\n");
        if (responseBody != null && !responseBody.isEmpty()) {
            if (responseBody.length() > 500) {
                summary.append(responseBody.substring(0, 500)).append("...");
            } else {
                summary.append(responseBody);
            }
        } else {
            summary.append("Yanıt bulunamadı");
        }
        
        Allure.addAttachment("Test Özeti", summary.toString());
        System.out.println(" Test özeti kaydedildi");
    }

    public static void clearContext() {
        try {
            Map<String, Object> context = testContext.get();
            if (context != null) {
                System.out.println(" Test context temizleniyor...");
                context.clear();
            }
        } catch (Exception e) {
            System.err.println("Error clearing context: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void logEnvironmentInfo() {
        StringBuilder envInfo = new StringBuilder();
        envInfo.append("Environment Information:\n");
        envInfo.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        envInfo.append("OS: ").append(System.getProperty("os.name")).append("\n");
        envInfo.append("User: ").append(System.getProperty("user.name")).append("\n");
        
        Allure.addAttachment("Environment Info", "text/plain", envInfo.toString());
    }

    public static void logError(Exception e) {
        Map<String, Object> context = testContext.get();
        context.put("lastError", e);
        
        StringBuilder errorDetails = new StringBuilder();
        errorDetails.append("Error Details:\n");
        errorDetails.append("Error Type: ").append(e.getClass().getName()).append("\n");
        errorDetails.append("Error Message: ").append(e.getMessage()).append("\n");
        errorDetails.append("\nStack Trace:\n");
        
        for (StackTraceElement element : e.getStackTrace()) {
            errorDetails.append(element.toString()).append("\n");
        }
        
        Allure.addAttachment("API Error", "text/plain", errorDetails.toString());
    }
}
