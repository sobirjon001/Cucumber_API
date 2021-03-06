package com.CompanyName.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class Browser_Utils {

    WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
    Actions actions = new Actions(Driver.getDriver());
    JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();

    public String getURL() {
        return Driver.getDriver().getCurrentUrl();
    }

    public String getTitle() { return Driver.getDriver().getTitle(); }

    public void waitForVisibilityOf(WebElement webElement) {
        wait.until(ExpectedConditions.visibilityOf(webElement));
    }

    public void waitForInVisibilityOf(WebElement webElement) {
        wait.until(ExpectedConditions.invisibilityOf(webElement));
    }

    public void waitForClickAbilityOf(WebElement webElement) {
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    public void waitForTitleToEqual(String expectedTitle) {
        wait.until(ExpectedConditions.titleIs(expectedTitle));
    }

    public String getElementsText(WebElement webElement) {
        waitForInVisibilityOf(webElement);
        try {
            return webElement.getText();
        } catch (Exception e) {
            return webElement.getAttribute("value");
        }
    }

    public List<String> getElementsTexts(List<WebElement> webElements) {
        try {
            return webElements.stream().map(WebElement::getText).collect(Collectors.toList());
        } catch (Exception e) {
            return webElements.stream().map(p -> p.getAttribute("value")).collect(Collectors.toList());
        }
    }

    public void clickButton(WebElement webElement) {
        waitForClickAbilityOf(webElement);
        webElement.click();
    }

    public void selectValueFromDropdown(WebElement webElement, String value) {
        waitForInVisibilityOf(webElement);
        Select select = new Select(webElement);
        select.selectByValue(value);
    }

    public void fillInput(WebElement webElement, String value) {
        waitForInVisibilityOf(webElement);
        webElement.click();
        webElement.clear();
        webElement.sendKeys(Keys.chord(Keys.CONTROL, "A"));
        webElement.sendKeys(value);
    }

    public void hover(WebElement webElement) {
        waitForVisibilityOf(webElement);
        actions.moveToElement(webElement).perform();
    }

    public void scrollDown() {
        js.executeScript("window.scrollBy(0,250)", "");
    }

    public void waitFor(int seconds) {
        try {
            Thread.sleep(1000L * seconds);
        } catch (Exception e) {}
    }
}
