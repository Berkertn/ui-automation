package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    public By navMenusLocator = By.xpath("//*[@class='nav-menu-parent']");
    public By onlyNecCookiesLocator = By.id("cookieAcceptanceOnlyNecessary");
    public By subMenusLocator = By.cssSelector(".menu-item-group a");

    public HomePage(WebDriver driver) {
        super(driver);
        this.browserTitle = "";
    }
}
