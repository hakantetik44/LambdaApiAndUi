package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        features = "src/test/resources/features",
        glue = {"stepdefinitions", "hooks"},
        tags = ""  // Tüm testleri çalıştırmak için boş bırakıyoruz
)
public class TestRunner {
    
    @AfterClass
    public static void generateReport() {
        try {
            // Generate the report
            ProcessBuilder processBuilder = new ProcessBuilder("allure", "generate", "target/allure-results", "-o", "target/allure-report", "--clean");
            Process process = processBuilder.start();
            process.waitFor();

            // Open the report
            ProcessBuilder openBuilder = new ProcessBuilder("allure", "open", "target/allure-report");
            openBuilder.start();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
