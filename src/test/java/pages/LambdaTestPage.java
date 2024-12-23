package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

import java.util.List;

public class LambdaTestPage {
    
    private WebDriver driver;

    public LambdaTestPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(name = "search")
    public WebElement searchBox;

    @FindBy(css = "button.type-text")
    public WebElement searchButton;

    @FindBy(id = "entry_212469")
    public WebElement searchResultsContainer;

    @FindBy(css = ".product-thumb")
    public List<WebElement> productList;

    @FindBy(css = ".product-thumb .title")
    public List<WebElement> productTitles;

    @FindBy(css = "#widget-navbar-217834 .nav-link")
    public List<WebElement> categoryLinks;

    // Yardımcı metodlar
    public String getProductTitle(WebElement product) {
        return product.findElement(By.cssSelector(".title")).getText();
    }

    public boolean isProductVisible(String productName) {
        return productList.stream()
                .anyMatch(product -> getProductTitle(product).toLowerCase()
                        .contains(productName.toLowerCase()));
    }

    public boolean isCategoryVisible(String categoryName) {
        return categoryLinks.stream()
                .anyMatch(category -> category.getText().toLowerCase()
                        .contains(categoryName.toLowerCase()));
    }

    public void printCategories() {
        System.out.println("\n📋 Mevcut kategoriler:");
        categoryLinks.forEach(category -> 
            System.out.println("  - " + category.getText())
        );
    }
}
