package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LambdaTestPage;

import java.time.Duration;

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
        wait.until(ExpectedConditions.visibilityOf(page.searchBox));
    }

    @When("{string} için arama yaparım")
    public void aramaYaparim(String arananUrun) {
        System.out.println("\n🔍 Aranıyor: " + arananUrun);
        page.searchBox.clear();
        page.searchBox.sendKeys(arananUrun);
        System.out.println("⌨️ Arama terimi girildi: " + arananUrun);
        
        page.searchButton.click();
        System.out.println("🖱️ Arama butonuna tıklandı");
    }

    @Then("Arama sonuçları görüntülenir")
    public void aramaSonuclariGoruntulenir() {
        System.out.println("\n🔎 Arama sonuçları kontrol ediliyor...");
        wait.until(ExpectedConditions.visibilityOf(page.searchResultsContainer));
        Assert.assertTrue("Arama sonuçları görüntülenmiyor", page.searchResultsContainer.isDisplayed());
        System.out.println("✅ Arama sonuçları başarıyla görüntülendi");
    }

    @Then("Sonuçlarda {string} bulunur")
    public void sonuclardaBulunur(String beklenenUrun) {
        System.out.println("\n🔍 Sonuçlarda ürün aranıyor: " + beklenenUrun);
        
        long urunSayisi = page.productList.stream()
                .filter(product -> page.getProductTitle(product).toLowerCase()
                        .contains(beklenenUrun.toLowerCase()))
                .count();
        
        Assert.assertTrue("Ürün bulunamadı: " + beklenenUrun, page.isProductVisible(beklenenUrun));
        System.out.println("✅ " + beklenenUrun + " ile eşleşen " + urunSayisi + " ürün bulundu");
    }

    @When("Kategoriler menüsünü görüntülerim")
    public void kategorilerMenusunuGoruntulerim() {
        try {
            System.out.println("\n📋 Kategori menüsü yükleniyor...");
            Thread.sleep(2000);
            Assert.assertTrue("Kategoriler bulunamadı", !page.categoryLinks.isEmpty());
            System.out.println("📊 " + page.categoryLinks.size() + " kategori bulundu");
            
            System.out.println("🖱️ Kategoriler görünür hale getiriliyor");
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", 
                page.categoryLinks.get(0)
            );
            System.out.println("✅ Kategoriler menüsü görünür durumda");
            
        } catch (InterruptedException e) {
            System.out.println("❌ Kategoriler yüklenirken hata: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Then("Tüm ürün kategorileri listelenir")
    public void tumUrunKategorileriListelenir() {
        System.out.println("\n📋 Kategori listesi kontrol ediliyor...");
        Assert.assertTrue("Kategori listesi boş", !page.categoryLinks.isEmpty());
        System.out.println("✅ Toplam " + page.categoryLinks.size() + " kategori bulundu");
    }

    @Then("{string} kategorisi bulunur")
    public void kategorisiBulunur(String kategoriAdi) {
        System.out.println("\n🔍 Kategori aranıyor: " + kategoriAdi);
        page.printCategories();
        
        Assert.assertTrue(
            "Kategori bulunamadı: " + kategoriAdi, 
            page.isCategoryVisible(kategoriAdi)
        );
        System.out.println("✅ Kategori bulundu: " + kategoriAdi);
    }
}
