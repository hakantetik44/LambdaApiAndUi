package stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LambdaTestApiSteps {
    private Response response;
    private final String BASE_URL = "https://ecommerce-playground.lambdatest.io";
    private RequestSpecification request;
    private long requestStartTime;

    @Step("API isteği hazırlanıyor")
    private void prepareRequest() {
        request = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
        
        // İstek detaylarını Allure'a ekle
        Allure.addAttachment("API Request Details", "text/plain", 
            String.format("""
                Base URL: %s
                Headers:
                  Content-Type: application/json
                  Accept: application/json
                """, BASE_URL));
    }

    @When("{string} adresine GET isteği gönderiyorum")
    public void getIstegiGonderiyorum(String endpoint) {
        System.out.println("\n🌐 GET isteği gönderiliyor: " + BASE_URL + endpoint);
        
        prepareRequest();
        requestStartTime = System.currentTimeMillis();
        
        // İstek zamanını kaydet
        Allure.addAttachment("Request Time", "text/plain", 
            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        response = request.get(endpoint);
        
        // Yanıt süresini hesapla
        long responseTime = System.currentTimeMillis() - requestStartTime;
        
        // API yanıt detaylarını Allure'a ekle
        Allure.addAttachment("API Response Details", "application/json", 
            String.format("""
                Endpoint: %s
                Status Code: %d
                Response Time: %d ms
                Response Headers:
                %s
                
                Response Body:
                %s
                """, 
                endpoint,
                response.getStatusCode(),
                responseTime,
                response.getHeaders(),
                response.prettyPrint()));
        
        System.out.println("✅ İstek başarıyla gönderildi");
        System.out.println("📝 Yanıt Durum Kodu: " + response.getStatusCode());
    }

    @Then("Yanıt durum kodu {int} olmalı")
    public void yanitDurumKoduOlmali(int beklenenKod) {
        System.out.println("\n🔍 Yanıt durum kodu kontrol ediliyor...");
        int gercekKod = response.getStatusCode();
        
        // Durum kodu kontrolünü Allure'a ekle
        Allure.addAttachment("Status Code Verification", "text/plain",
            String.format("""
                Expected Status Code: %d
                Actual Status Code: %d
                Result: %s
                """, 
                beklenenKod, 
                gercekKod,
                beklenenKod == gercekKod ? "✅ PASSED" : "❌ FAILED"));
        
        Assert.assertEquals("Beklenmeyen durum kodu", beklenenKod, gercekKod);
        System.out.println("✅ Durum kodu beklenen değerde: " + beklenenKod);
    }

    @Then("Yanıt {string} ürünlerini içermeli")
    public void yanitUrunleriniIcermeli(String urunTipi) {
        System.out.println("\n🔍 Yanıtta " + urunTipi + " ürünleri aranıyor...");
        String responseBody = response.getBody().asString().toLowerCase();
        
        // Yanıt içeriğini Allure'a ekle
        Allure.addAttachment("Response Content Analysis", "text/plain",
            String.format("""
                Search Term: %s
                Response Size: %d characters
                Found in Response: %b
                
                Response Preview (first 1000 chars):
                %s
                """,
                urunTipi,
                responseBody.length(),
                responseBody.contains(urunTipi.toLowerCase()),
                responseBody.substring(0, Math.min(1000, responseBody.length()))));
        
        Assert.assertTrue("Yanıt " + urunTipi + " ürünlerini içermiyor", 
                        responseBody.contains(urunTipi.toLowerCase()));
        
        System.out.println("✅ " + urunTipi + " ürünleri yanıtta bulundu");
        System.out.println("📊 Yanıt Boyutu: " + responseBody.length() + " karakter");
    }
}
