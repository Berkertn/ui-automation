package ui.pages;

import lombok.Getter;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;

import static ui.helpers.selenium.DriverManager.getBaseUrlTL;

import java.time.Duration;

public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String pageURL;
    @Getter
    protected String browserTitle;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.pageURL = getBaseUrlTL();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getPageUrl() {
        return this.pageURL;
    }

}