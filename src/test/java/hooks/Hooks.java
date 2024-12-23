package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import utilities.ApiTestLogger;
import utilities.VideoRecorder;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public class Hooks {
    private static WebDriver driver;
    private VideoRecorder videoRecorder;
    private long scenarioStartTime;

    @Before("not @api")
    public void setUp(Scenario scenario) {
        try {
            scenarioStartTime = System.currentTimeMillis();
            
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--start-maximized");
            
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
            driver.manage().window().maximize();
            
            // Video kaydını başlat
            videoRecorder = new VideoRecorder(driver);
            videoRecorder.startRecording(scenario.getName());
            
        } catch (Exception e) {
            System.err.println("Test başlatma sırasında hata: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Before("@api")
    public void setUpApi(Scenario scenario) {
        try {
            scenarioStartTime = System.currentTimeMillis();
            RestAssured.baseURI = "https://ecommerce-playground.lambdatest.io";
            ApiTestLogger.initializeTest(scenario.getName());
            ApiTestLogger.logEnvironmentInfo();
        } catch (Exception e) {
            System.err.println("API test başlatma sırasında hata: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @After("not @api")
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                // Ekran görüntüsü al
                final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", scenario.getName());
                Allure.addAttachment("Failed Screenshot", new ByteArrayInputStream(screenshot));
            }
            
            // Video kaydını durdur
            if (videoRecorder != null) {
                videoRecorder.stopRecording(scenario.getName());
            }
            
            // Test süresini hesapla
            long duration = System.currentTimeMillis() - scenarioStartTime;
            
            // Test sonuç özetini ekle
            String result = scenario.isFailed() ? "❌ FAILED" : "✅ PASSED";
            Allure.addAttachment("Test Sonucu", "text/plain",
                String.format("""
                    Senaryo: %s
                    Durum: %s
                    Süre: %.2f saniye
                    Hata: %s
                    """,
                    scenario.getName(),
                    result,
                    duration / 1000.0,
                    scenario.isFailed() ? scenario.getStatus() : "Yok"
                ));
            
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            System.err.println("Test sonlandırma sırasında hata: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @After("@api")
    public void tearDownApi(Scenario scenario) {
        try {
            // Test süresini hesapla
            long duration = System.currentTimeMillis() - scenarioStartTime;
            
            // API test sonuçlarını logla
            ApiTestLogger.logTestSummary(
                !scenario.isFailed(),
                scenario.isFailed() ? scenario.getStatus().toString() : null
            );
            
            // Test sonuç özetini ekle
            String result = scenario.isFailed() ? "❌ FAILED" : "✅ PASSED";
            Allure.addAttachment("API Test Sonucu", "text/plain",
                String.format("""
                    Senaryo: %s
                    Durum: %s
                    Süre: %.2f saniye
                    Hata: %s
                    """,
                    scenario.getName(),
                    result,
                    duration / 1000.0,
                    scenario.isFailed() ? scenario.getStatus() : "Yok"
                ));
        } catch (Exception e) {
            System.err.println("API test sonlandırma sırasında hata: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }
}
