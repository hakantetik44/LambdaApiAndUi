package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Driver {
    private static ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();
    private static ThreadLocal<WebDriverWait> waitPool = new ThreadLocal<>();

    private Driver() {
    }

    public static WebDriver getDriver() {
        if (driverPool.get() == null) {
            String browser = System.getProperty("browser", "chrome"); // default to chrome if not specified
            
            switch (browser.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments(
                        "--remote-allow-origins=*",
                        "--disable-notifications",
                        "--disable-popup-blocking",
                        "--disable-infobars",
                        "--disable-gpu",
                        "--no-sandbox",
                        "--window-size=1920,1080"
                    );
                    WebDriver driver = new ChromeDriver(chromeOptions);
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
                    driverPool.set(driver);
                    break;
                    
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments(
                        "--disable-notifications",
                        "--disable-popup-blocking"
                    );
                    driverPool.set(new FirefoxDriver(firefoxOptions));
                    break;
                    
                case "safari":
                    driverPool.set(new SafariDriver());
                    break;
                    
                default:
                    throw new RuntimeException("Unsupported browser: " + browser);
            }
            
            driverPool.get().manage().window().maximize();
            
            waitPool.set(new WebDriverWait(driverPool.get(), Duration.ofSeconds(10)));
        }
        return driverPool.get();
    }

    public static WebDriverWait getWait() {
        if (waitPool.get() == null) {
            waitPool.set(new WebDriverWait(getDriver(), Duration.ofSeconds(10)));
        }
        return waitPool.get();
    }

    public static void closeDriver() {
        if (driverPool.get() != null) {
            try {
                driverPool.get().quit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            driverPool.remove();
            waitPool.remove();
        }
    }
}
