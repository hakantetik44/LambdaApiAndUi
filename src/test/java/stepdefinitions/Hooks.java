package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utilities.Driver;

public class Hooks {
    
    @Before("@ui")
    public void setUp() {
        // Set up any required configurations before UI tests
    }
    
    @After("@ui")
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Screenshot");
        }
        Driver.closeDriver();
    }
    
    @Before("@api")
    public void setUpApi() {
        // Set up any required configurations before API tests
    }
    
    @After("@api")
    public void tearDownApi() {
        // Clean up after API tests
    }
}
