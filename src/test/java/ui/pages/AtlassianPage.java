package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AtlassianPage extends BasePage {

    public By contactButton = By.cssSelector(".container.landing-first.animated.fadeIn button");


    public AtlassianPage(WebDriver driver) {
        super(driver);
        this.browserTitle = "Atlassian Solutions - Platinum Atlassian Partner - Partnership | OBSS Technology";
    }
}
