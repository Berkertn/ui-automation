package ui.tests;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.pages.AtlassianPage;
import ui.pages.HomePage;

import java.util.List;

public class HomePageTests extends BaseTest {

    HomePage homePage;
    AtlassianPage atlassianPage;

    @Test(enabled = true, description = "Home Page Welcome Test", priority = 0)
    public void HomePageTest() {
        homePage = new HomePage(driver);
        WebElement cookie = getElement(homePage.onlyNecCookiesLocator);
        if (cookie.isDisplayed()) {
            tapElement(cookie);
        }
        List<WebElement> menuItems = getElements(homePage.navMenusLocator);
        for (WebElement menuItem : menuItems) {
            if (getTextOfElement(menuItem).equals("SERVICES")) {
                menuItem.click();
            }
        }
        List<WebElement> subMenus = getElements(homePage.subMenusLocator);
        subMenus.forEach(subMenu -> {
            if (subMenu.getText().equals("Atlassian")) {
                subMenu.click();

            }
        });
        atlassianPage = new AtlassianPage(driver);
        Assert.assertEquals(atlassianPage.getBrowserTitle(), "Atlassian Solutions - Platinum Atlassian Partner - Partnership | OBSS Technology");

        tapElement(getElement(atlassianPage.contactButton));
        Assert.assertEquals("berj","sdgdf");
        // how can we assert something while its getting error we should be assert something not displaying or not there
    }

}

