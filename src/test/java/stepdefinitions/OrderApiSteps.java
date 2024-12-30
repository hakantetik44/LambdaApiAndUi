package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.*;
import io.restassured.response.Response;
import models.Address;
import models.CardInfo;
import models.OrderRequest;
import org.testng.Assert;
import tests.OrderApiTest;
import utilities.ApiTestLogger;

import java.util.Map;

@Epic("E-Ticaret API Testleri")
@Feature("Sipariş İşlemleri")
public class OrderApiSteps {
    private OrderApiTest orderApiTest;
    private Response response;

    public OrderApiSteps() {
        orderApiTest = new OrderApiTest();
    }

    @Given("Kullanıcı API için giriş yapar")
    @Step("API'ye giriş yapılıyor")
    @Description("Kullanıcı bilgileri ile API'ye giriş yapılır ve token alınır")
    public void userLogsInToAPI() {
        ApiTestLogger.info("API'ye giriş yapılıyor");
        orderApiTest.loginAndGetToken();
        ApiTestLogger.info("Login başarılı, token: " + orderApiTest.getAuthToken());
        
        // Allure'a token bilgisini ekle
        saveTokenInfo(orderApiTest.getAuthToken());
    }

    @When("Sepete ürün eklenir")
    @Step("Sepete ürün ekleniyor")
    @Description("Belirtilen ürün sepete eklenir")
    public void addItemToCart(DataTable dataTable) {
        ApiTestLogger.info("Sepete ürün ekleniyor");
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        
        // Sepete eklenen ürün detaylarını Allure'a ekle
        saveCartItemDetails(data);
        
        orderApiTest.addItemToCart();
    }

    @When("Sipariş oluşturulur")
    @Step("Sipariş oluşturuluyor")
    @Description("Verilen bilgiler ile yeni bir sipariş oluşturulur")
    public void createOrder(DataTable dataTable) {
        ApiTestLogger.info("Sipariş oluşturuluyor");
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        // Sipariş detaylarını Allure'a ekle
        saveOrderDetails(data);
        
        orderApiTest.createOrder();
    }

    @Then("Sipariş başarıyla oluşturulmalı")
    @Step("Siparişin başarıyla oluşturulduğu kontrol ediliyor")
    public void orderShouldBeCreatedSuccessfully() {
        Assert.assertNotNull(orderApiTest.getOrderId(), "Sipariş ID boş olamaz");
        // Oluşturulan sipariş ID'sini Allure'a ekle
        saveOrderId(orderApiTest.getOrderId());
    }

    @Then("Sipariş detayları doğru olmalı")
    @Step("Sipariş detayları kontrol ediliyor")
    public void verifyOrderDetails(DataTable dataTable) {
        Map<String, String> expectedData = dataTable.asMap(String.class, String.class);
        // Beklenen ve gerçek sipariş detaylarını karşılaştır
        verifyOrderDetailsWithExpected(expectedData);
    }

    @Attachment(value = "Token Bilgisi", type = "text/plain")
    private String saveTokenInfo(String token) {
        return "API Token: " + token;
    }

    @Attachment(value = "Sepet Ürün Detayları", type = "text/plain")
    private String saveCartItemDetails(Map<String, String> data) {
        StringBuilder details = new StringBuilder();
        details.append("Ürün ID: ").append(data.get("productId")).append("\n");
        details.append("Miktar: ").append(data.get("quantity")).append("\n");
        details.append("Renk: ").append(data.get("color"));
        return details.toString();
    }

    @Attachment(value = "Sipariş Detayları", type = "text/plain")
    private String saveOrderDetails(Map<String, String> data) {
        StringBuilder details = new StringBuilder();
        details.append("Müşteri Adı: ").append(data.get("fullName")).append("\n");
        details.append("Adres: ").append(data.get("streetAddress")).append("\n");
        details.append("Şehir: ").append(data.get("city")).append("\n");
        details.append("İlçe: ").append(data.get("district")).append("\n");
        details.append("Posta Kodu: ").append(data.get("zipCode")).append("\n");
        details.append("Ödeme Yöntemi: ").append(data.get("paymentMethod"));
        return details.toString();
    }

    @Attachment(value = "Sipariş ID", type = "text/plain")
    private String saveOrderId(String orderId) {
        return "Oluşturulan Sipariş ID: " + orderId;
    }

    @Step("Sipariş detayları doğrulanıyor")
    private void verifyOrderDetailsWithExpected(Map<String, String> expectedData) {
        // Status kontrolü
        Assert.assertEquals("PENDING", expectedData.get("status"), "Sipariş durumu yanlış");
        
        // Item count kontrolü
        Assert.assertEquals("1", expectedData.get("itemCount"), "Ürün sayısı yanlış");
        
        // Total amount kontrolü
        Assert.assertTrue(Double.parseDouble(expectedData.get("totalAmount").replace(">", "")) > 0, 
            "Toplam tutar sıfırdan büyük olmalı");

        // Detayları Allure'a ekle
        saveVerificationResults(expectedData);
    }

    @Attachment(value = "Doğrulama Sonuçları", type = "text/plain")
    private String saveVerificationResults(Map<String, String> expectedData) {
        StringBuilder results = new StringBuilder();
        results.append("Beklenen Değerler:").append("\n");
        results.append("Status: ").append(expectedData.get("status")).append("\n");
        results.append("Ürün Sayısı: ").append(expectedData.get("itemCount")).append("\n");
        results.append("Minimum Toplam Tutar: ").append(expectedData.get("totalAmount"));
        return results.toString();
    }
}
