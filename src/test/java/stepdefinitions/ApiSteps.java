package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.FailureConfig;
import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import utilities.ApiTestLogger;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.config.RedirectConfig.redirectConfig;
import static io.restassured.config.FailureConfig.failureConfig;

public class ApiSteps {
    private RequestSpecification request;
    private Response response;
    private final String BASE_URL = "https://ecommerce-playground.lambdatest.io";

    @Given("API testi için request hazırlanır")
    public void prepareRequest() {
        System.out.println("🔍 API isteği hazırlanıyor...");
        RestAssured.baseURI = BASE_URL;
        request = RestAssured.given()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
        ApiTestLogger.logRequest(request);
        System.out.println("✅ API isteği hazır");
    }

    @When("API endpoint'ine GET isteği gönderilir")
    public void sendGetRequest() {
        System.out.println("🌐 API isteği gönderiliyor...");
        response = request.get("/index.php?route=product/category&path=33");
        logResponse("Kategori Görüntüleme");
    }

    @Then("API yanıtı başarılı olmalıdır")
    public void verifyResponse() {
        verifyStatusCode(200);
    }
    
    @When("API endpoint'ine {string} araması için GET isteği gönderilir")
    public void sendSearchRequest(String keyword) {
        System.out.println("🔍 '" + keyword + "' için API araması yapılıyor...");
        response = request.get("/index.php?route=product/search&search=" + keyword);
        logResponse("Ürün Arama");
    }
    
    @Then("Arama yanıtı {string} ürünlerini içermeli")
    public void verifySearchResponse(String keyword) {
        System.out.println("🔍 Arama sonuçları kontrol ediliyor...");
        String responseBody = response.getBody().asString().toLowerCase();
        Assert.assertTrue("Response should contain " + keyword, 
            responseBody.contains(keyword.toLowerCase()));
        
        // Response'u tekrar logla (teardown'da kullanılması için)
        ApiTestLogger.logResponse(response);
        System.out.println("✅ Arama sonuçları başarıyla doğrulandı");
    }
    
    @When("API endpoint'ine geçersiz kategori ID'si ile GET isteği gönderilir")
    public void sendInvalidCategoryRequest() {
        System.out.println("🌐 Geçersiz kategori ID'si ile API isteği gönderiliyor...");
        
        response = RestAssured.given()
            .filter(new AllureRestAssured())
            .baseUri(BASE_URL)
            .when()
            .get("/index.php?route=product/category&path=999999")
            .then()
            .extract()
            .response();
            
        System.out.println("📊 API yanıtı: " + response.getStatusLine());
        System.out.println("📊 Status Code: " + response.getStatusCode());
        
        String statusMessage = String.format("Status Code: %d, Reason: %s", 
            response.getStatusCode(), response.getStatusLine());
        Allure.addAttachment("Response Status", statusMessage);
        
        logResponse("Geçersiz Kategori");
    }
    
    @Then("API yanıtı {int} hata kodu dönmeli")
    public void verifyStatusCode(int expectedCode) {
        System.out.println("🔍 API yanıtı kontrol ediliyor...");
        
        if (response == null) {
            throw new AssertionError("API yanıtı null");
        }
        
        int actualCode = response.getStatusCode();
        String statusLine = response.getStatusLine();
        
        System.out.println("📊 Alınan yanıt: " + statusLine);
        System.out.println("📊 Beklenen kod: " + expectedCode + ", Alınan kod: " + actualCode);
        
        String statusMessage = String.format("Status Code: %d, Reason: %s", actualCode, statusLine);
        Allure.addAttachment("Response Status", statusMessage);
        
        Assert.assertEquals("Status code should be " + expectedCode, expectedCode, actualCode);
        System.out.println("✅ Status code doğrulandı: " + actualCode);
    }
    
    @And("Yanıt hata mesajı içermeli")
    public void verifyErrorMessage() {
        System.out.println("🔍 Hata mesajı kontrol ediliyor...");
        
        if (response == null) {
            throw new AssertionError("API yanıtı null");
        }
        
        String responseBody = response.getBody().asString();
        Assert.assertTrue("Response should contain error message", 
            responseBody != null && !responseBody.isEmpty());
        
        String errorMessage = String.format("Error Message: %s", responseBody);
        Allure.addAttachment("Error Message", errorMessage);
        
        System.out.println("✅ Hata mesajı doğrulandı");
    }
    
    private void logResponse(String operationType) {
        // Response detaylarını Allure'a ekle
        Allure.addAttachment("Operation Type", operationType);
        Allure.addAttachment("Response Status", response != null ? response.getStatusLine() : "null");
        Allure.addAttachment("Response Time", response != null ? response.getTime() + "ms" : "null");
        Allure.addAttachment("Response Headers", response != null ? response.getHeaders().toString() : "null");
        
        // Response'u ApiTestLogger'a kaydet
        ApiTestLogger.logResponse(response);
        System.out.println("✅ API yanıtı alındı: " + (response != null ? response.getStatusLine() : "null"));
    }
}
