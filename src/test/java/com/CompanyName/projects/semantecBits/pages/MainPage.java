package com.CompanyName.projects.semantecBits.pages;

import com.CompanyName.utils.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class MainPage {

    public MainPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "//a[contains(span, 'Expertise')]")
    public WebElement ExpertiseModule;

    @FindBy(xpath = "//a[contains(span, 'Software Development')]")
    public WebElement SoftwareDevelopmentSelection;

    @FindBy(xpath = "//header[@id='top']")
    public WebElement header;

    @FindBy(xpath = "//div[@id='executive-management']")
    public WebElement executiveManagement;

    public WebElement getModuleByNameInGivenParent(WebElement parent, String moduleName) {
        return parent.findElement(By.xpath(".//a[contains(span, '" + moduleName + "')]"));
    }

    @FindBy(xpath = "//*[contains(text(), 'Zain Hatim')]")
    public WebElement ZainHatim;

    public List<WebElement> getAllElementsByContainedText(String containedText) {
        return Driver.getDriver().findElements(By.xpath(".//*[contains(text(), '" + containedText + "')]"));
    }
}
