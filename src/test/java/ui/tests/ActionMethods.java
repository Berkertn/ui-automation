package ui.tests;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ui.helpers.selenium.DriverManager.getDriverTL;
import static ui.helpers.selenium.DriverManager.getSelectAllCommand;
import static ui.helpers.yaml.ConfigReader.getConfig;
import static ui.utils.LogUtil.LOG;

public class ActionMethods {
    protected WebDriver driver = getDriverTL();
    protected WebDriverWait wait;

    public WebElement getElement(By selector) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(selector));
        LOG.trace(String.format("\nSelector [%s] searching", selector));
        return driver.findElement(selector);
    }

    public String getElementText(WebElement element, By selector) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(selector));
        LOG.trace(String.format("\nElement [%s],search by selector of [%s] searching", element, selector));
        return element.findElement(selector).getText();
    }

    public String getElementText(WebElement element) {
        return element.getText();
    }

    public WebElement getParentElement(WebElement webElement) {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return arguments[0].parentNode;", webElement);
    }


    public List<WebElement> getElements(By elementBy) {
        List<WebElement> webElementList = null;
        try {
            webElementList = driver.findElements(elementBy);
            LOG.trace(String.format("\nElements [%s] have been found in count of [%s]", elementBy, webElementList.size()));
        } catch (Exception e) {
            LOG.fatal(String.format("\nElement [%s] could not found.\tError: %s", elementBy, e.getMessage()));
            Assert.fail(String.format("\nElement [%s] could not found.\tError: %s", elementBy, e.getMessage()));
        }
        return webElementList;
    }

    public void waitUntilClickable(WebElement webElement) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(webElement));
            LOG.info(String.format("\nElement [%s] is clickable", webElement));
        } catch (Exception e) {
            LOG.error("Error during element to be clickable: {}", e.getMessage());
            Assert.fail("Error during element to be clickable: " + e.getMessage());
        }
    }

    public void setWaitTime(int seconds) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        LOG.info(String.format("\nWait time has been set as [%s] seconds", seconds));
    }

    public void scrollToTheElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) getDriverTL();
        js.executeScript("arguments[0].scrollIntoView();", element);
    }

    public void hoverTheElement(WebElement element) {
        Actions actions = new Actions(getDriverTL());
        actions.moveToElement(element).perform();
    }

    public String takeScreenshot(String testMethod, String testClass) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"));
        String screenshotsDir = getConfig("screen-shots.screenshotDir");
        String extension = getConfig("screen-shots.ss_extension");
        File screenshot = ((TakesScreenshot) getDriverTL()).getScreenshotAs(OutputType.FILE);
        String filePathWOSSDir = "";
        try {
            filePathWOSSDir = testClass + "/" + testMethod + "_"
                    + date + extension;
            String path = screenshotsDir + filePathWOSSDir;
            FileUtils.copyFile(screenshot, new File(path));
            LOG.info("Screenshot taken: {}", testMethod);
        } catch (IOException e) {
            LOG.error("Failed to take screenshot: {}", e.getMessage());
        }
        return filePathWOSSDir;
    }

    public void highlightElement(WebElement element) {
        ((JavascriptExecutor) getDriverTL()).executeScript("arguments[0].style.border = '6px solid yellow'", element);
        // Wait 1 sec to can see the highlighted element
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOG.error("Error while highlighting element: {}", e.getMessage());
        }
        ((JavascriptExecutor) getDriverTL()).executeScript("arguments[0].style.border = ''", element);
    }

    public void tapElement(WebElement webElement) {
        try {
            webElement.click();
            LOG.info(String.format("\nClicked to the element [%s]", webElement));
        } catch (Exception e) {
            LOG.warn(String.format("\nFailed during tapping to the [%s], because { error : [%s] }", webElement, e.getMessage()));
            Assert.fail(String.format("\nFailed during tapping to the [%s], because { error : [%s] }", webElement, e.getMessage()));
        }
    }

    public void tapEnter(WebElement webElement) {
        try {
            webElement.sendKeys(Keys.ENTER);
            LOG.info(String.format("\nPressed 'Enter' on the element [%s]", webElement));
        } catch (Exception e) {
            LOG.warn(String.format("\nFailed during pressing 'Enter' on the [%s], because { error : [%s] }", webElement, e.getMessage()));
            Assert.fail(String.format("\nFailed during pressing 'Enter' on the [%s], because { error : [%s] }", webElement, e.getMessage()));
        }
    }

    public void deleteTextOfElement(WebElement webElement) {
        webElement.clear();
        /*typeElement(webElement, getSelectAllCommand(), false);
        typeElement(webElement, Keys.chord(Keys.BACK_SPACE), false);
        if (!getTextOfElement(webElement).isEmpty()) {
            LOG.fatal(String.format("\nText of the element [%s] could not be deleted", webElement));
            Assert.fail(String.format("\nText of the element [%s] could not be deleted", webElement));
        } else {
            LOG.info(String.format("\nDeleted text of the element [%s]", webElement));
        }*/
    }

    public void clearTextOfElement(WebElement webElement) {
        try {
            webElement.clear();
            if (!getTextOfElement(webElement).isEmpty()) {
                LOG.warn(String.format("\nText of the element [%s] could not cleared, attempting to delete the content", webElement));
                deleteTextOfElement(webElement);
            } else {
                LOG.info(String.format("\nCleared text of the element [%s]", webElement));
            }
        } catch (Exception e) {
            LOG.warn(String.format("\nFailed during clearing text of the [%s], because { error : [%s] }", webElement, e.getMessage()));
            Assert.fail(String.format("\nFailed during clearing text of the [%s], because { error : [%s] }", webElement, e.getMessage()));
        }
    }

    public void typeElement(WebElement webElement, String string, boolean isInteractive) {
        if (isInteractive) {
            Actions actions = new Actions(getDriverTL());
            try {
                actions.click();
                LOG.info(String.format("\nClicking to the element [%s]", webElement));
                actions.sendKeys(string);
                LOG.info(String.format("\nTyping to the element [%s] the sting [%s]", webElement, string));
                actions.perform();
                LOG.info(String.format("\nTyped to the element [%s] the sting [%s]", webElement, string));
            } catch (Exception e) {
                LOG.warn(String.format("\nFailed during typing to the [%s], because { error : [%s] }", webElement, e.getMessage()));
                Assert.fail(String.format("\nFailed during typing to the [%s], because { error : [%s] }", webElement, e.getMessage()));
            }
        } else {
            try {
                webElement.sendKeys(string);
                LOG.info(String.format("\nTyped to the element [%s] the sting [%s]", webElement, string));
            } catch (Exception e) {
                LOG.warn(String.format("\nFailed during typing to the [%s], because { error : [%s] }", webElement, e.getMessage()));
                Assert.fail(String.format("\nFailed during typing to the [%s], because { error : [%s] }", webElement, e.getMessage()));
            }
        }
    }

    public String getTextOfElement(WebElement webElement) {
        String text = null;
        try {
            text = webElement.getText();
            if (text == null || text.isEmpty()) {
                try {
                    text = getAttributeOfElement(webElement, "value");
                } catch (AssertionError e) {
                    text = null;
                }
            }
            LOG.trace(String.format("\nText of [%s] is: %s", webElement, text));
        } catch (Exception e) {
            LOG.fatal("\nElement text could not found.\tError: {}", e.getMessage());
            Assert.fail("\nElement text could not found.\tError: " + e.getMessage());
        }
        return text;
    }

    public String getAttributeOfElement(WebElement webElement, String attribute) {
        String value = "";
        try {
            value = webElement.getAttribute(attribute);
            if (value == null) {
                value = "";
            }
            LOG.trace(String.format("\nAttribute of [%s] is [%s]: [%s]", webElement, attribute, value));
        } catch (Exception e) {
            LOG.fatal("\nElement attribute {} could not found.\tError: {}", attribute, e.getMessage());
            Assert.fail("\nElement attribute " + attribute + " could not found.\tError: " + e.getMessage());
        }
        return value;
    }

    public String getCSSOfElement(WebElement webElement, String css) {
        String value = "";
        try {
            value = webElement.getCssValue(css);
            if (value == null) {
                value = "";
            }
        } catch (Exception e) {
            LOG.fatal("\nElement CSS {} could not found.\tError: {}", css, e.getMessage());
            Assert.fail("\nElement CSS " + css + " could not found.\tError: " + e.getMessage());
        }
        return value;
    }

    public void checkCheckbox(WebElement element, boolean checkStatus) {
        try {
            tapElement(element);
        } catch (Exception e) {
            LOG.fatal("\nElement could not found.\tError: " + e.getMessage());
            Assert.fail("\nElement could not found.\tError: " + e.getMessage());
        }
    }

    public void scrollToLimits(String direction) {
        String script = switch (direction) {
            case "top" -> "window.scrollTo(document.body.scrollHeight, 0)";
            case "bottom" -> "window.scrollTo(0, document.body.scrollHeight)";
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        };
        try {
            ((JavascriptExecutor) getDriverTL()).executeScript(script);
            LOG.info("\nScrolled to {}", direction);
        } catch (Exception e) {
            LOG.fatal("\nCould not scroll to {}\tError: {}", direction, e.getMessage());
            Assert.fail("\nCould not scroll to " + direction + "\tError: " + e.getMessage());
        }
    }
}
