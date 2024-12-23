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

    @FindBy(css = "input[name='search']")
    public WebElement searchInput;

    @FindBy(css = ".search-button button[type='submit']")
    public WebElement searchButton;

    @FindBy(css = ".product-layout")
    public List<WebElement> searchResults;

    @FindBy(css = "#entry_217841 .nav-link")
    public List<WebElement> categoryList;
    
    @FindBy(css = "#entry_217834 button")
    public WebElement categoryMenuButton;
    
    @FindBy(css = ".product-thumb h4")
    public List<WebElement> productNames;
    
    @FindBy(css = ".price-new")
    public List<WebElement> productPrices;
}
