package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.ApiTestLogger;
import org.junit.Assert;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;

import java.util.HashMap;
import java.util.Map;

public class ApiSteps {
    private RequestSpecification request;
    private Response response;

    @Given("API testi için request hazırlanır")
    public void prepareRequest() {
        request = RestAssured.given()
                .filter(new AllureRestAssured()) // Allure için request/response detaylarını otomatik ekler
                .header("Content-Type", "application/json");
        ApiTestLogger.logRequest(request);
    }

    @When("API endpoint'ine GET isteği gönderilir")
    public void sendGetRequest() {
        response = request.get("/api/products");
        ApiTestLogger.logResponse(response);
        
        // Response detaylarını Allure'a ekle
        Allure.addAttachment("Response Status", response.getStatusLine());
        Allure.addAttachment("Response Time", response.getTime() + "ms");
    }

    @Then("API yanıtı başarılı olmalıdır")
    public void verifyResponse() {
        Map<String, Boolean> validations = new HashMap<>();
        
        // Status code kontrolü
        boolean statusCheck = response.getStatusCode() == 200;
        validations.put("Status Code is 200", statusCheck);
        
        // Response time kontrolü
        boolean timeCheck = response.getTime() < 2000L;
        validations.put("Response Time < 2000ms", timeCheck);
        
        // Response body kontrolü
        String responseBody = response.getBody().asString();
        boolean bodyCheck = responseBody != null && !responseBody.isEmpty();
        validations.put("Response Body is not null", bodyCheck);
        
        // JSON formatı kontrolü
        boolean jsonCheck = true;
        try {
            response.then().assertThat().contentType("application/json");
        } catch (AssertionError e) {
            jsonCheck = false;
        }
        validations.put("Response is JSON format", jsonCheck);
        
        // Validasyon sonuçlarını logla
        ApiTestLogger.logValidationResults(validations);
        
        // Test sonuçlarını Allure'a ekle
        ApiTestLogger.getTestResults("Get Products API Test", response);
        
        // Validasyon özeti
        StringBuilder validationSummary = new StringBuilder();
        validationSummary.append("API Test Validations:\n\n");
        validations.forEach((check, result) -> 
            validationSummary.append(result ? "✅ " : "❌ ")
                           .append(check)
                           .append("\n")
        );
        Allure.addAttachment("Validation Summary", validationSummary.toString());
        
        // Assertion'ları kontrol et
        validations.forEach((message, result) -> Assert.assertTrue(message, result));
    }
}
