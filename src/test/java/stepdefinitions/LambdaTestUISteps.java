package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LambdaTestPage;

import java.time.Duration;
import java.util.List;

public class LambdaTestUISteps {
    private WebDriver driver;
    private WebDriverWait wait;
    private LambdaTestPage page;

    public LambdaTestUISteps() {
        this.driver = hooks.Hooks.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.page = new LambdaTestPage(driver);
    }

    @Given("LambdaTest anasayfasındayım")
    public void lambdaTestAnasayfasindayim() {
        System.out.println("\n🌐 LambdaTest anasayfasına gidiliyor...");
        driver.get("https://ecommerce-playground.lambdatest.io/");
        System.out.println("✅ Anasayfa başarıyla yüklendi");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='search']")));
    }

    @When("{string} için arama yaparım")
    public void aramaYaparim(String arananUrun) {
        System.out.println("\n🔍 Aranıyor: " + arananUrun);
        WebElement searchBox = driver.findElement(By.name("search"));
        searchBox.clear();
        searchBox.sendKeys(arananUrun);
        System.out.println("⌨️ Arama terimi girildi: " + arananUrun);
        
        WebElement searchButton = driver.findElement(By.cssSelector("button.type-text"));
        searchButton.click();
        System.out.println("🖱️ Arama butonuna tıklandı");
    }

    @Then("Arama sonuçları görüntülenir")
    public void aramaSonuclariGoruntulenir() {
        System.out.println("\n🔎 Arama sonuçları kontrol ediliyor...");
        WebElement searchResults = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("entry_212469")));
        Assert.assertTrue("Arama sonuçları görüntülenmiyor", searchResults.isDisplayed());
        System.out.println("✅ Arama sonuçları başarıyla görüntülendi");
    }

    @Then("Sonuçlarda {string} bulunur")
    public void sonuclardaBulunur(String beklenenUrun) {
        System.out.println("\n🔍 Sonuçlarda ürün aranıyor: " + beklenenUrun);
        List<WebElement> products = driver.findElements(By.cssSelector(".product-thumb"));
        boolean found = false;
        int urunSayisi = 0;
        
        for (WebElement product : products) {
            String productTitle = product.findElement(By.cssSelector(".title")).getText();
            System.out.println("📦 Bulunan ürün: " + productTitle);
            if (productTitle.toLowerCase().contains(beklenenUrun.toLowerCase())) {
                found = true;
                urunSayisi++;
            }
        }
        
        Assert.assertTrue("Ürün bulunamadı: " + beklenenUrun, found);
        System.out.println("✅ " + beklenenUrun + " ile eşleşen " + urunSayisi + " ürün bulundu");
    }

    @When("Kategoriler menüsünü görüntülerim")
    public void kategorilerMenusunuGoruntulerim() {
        try {
            System.out.println("\n📋 Kategori menüsü yükleniyor...");
            Thread.sleep(2000);
            List<WebElement> categories = driver.findElements(By.cssSelector("#widget-navbar-217834 .nav-link"));
            Assert.assertTrue("Kategoriler bulunamadı", categories.size() > 0);
            System.out.println("📊 " + categories.size() + " kategori bulundu");
            
            System.out.println("🖱️ Kategoriler görünür hale getiriliyor");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", categories.get(0));
            System.out.println("✅ Kategoriler menüsü görünür durumda");
            
        } catch (InterruptedException e) {
            System.out.println("❌ Kategoriler yüklenirken hata: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Then("Tüm ürün kategorileri listelenir")
    public void tumUrunKategorileriListelenir() {
        System.out.println("\n📋 Kategori listesi kontrol ediliyor...");
        List<WebElement> categories = driver.findElements(By.cssSelector("#widget-navbar-217834 .nav-link"));
        Assert.assertTrue("Kategori listesi boş", categories.size() > 0);
        System.out.println("✅ Toplam " + categories.size() + " kategori bulundu");
    }

    @Then("{string} kategorisi bulunur")
    public void kategorisiBulunur(String kategoriAdi) {
        System.out.println("\n🔍 Kategori aranıyor: " + kategoriAdi);
        List<WebElement> categories = driver.findElements(By.cssSelector("#widget-navbar-217834 .nav-link"));
        System.out.println("\n📋 Mevcut kategoriler:");
        for (WebElement category : categories) {
            System.out.println("  - " + category.getText());
        }
        
        boolean found = false;
        for (WebElement category : categories) {
            if (category.getText().toLowerCase().contains(kategoriAdi.toLowerCase())) {
                found = true;
                System.out.println("✅ Kategori bulundu: " + kategoriAdi);
                break;
            }
        }
        Assert.assertTrue("Kategori bulunamadı: " + kategoriAdi, found);
    }
}
