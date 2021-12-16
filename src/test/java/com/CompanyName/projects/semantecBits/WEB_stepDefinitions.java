package com.CompanyName.projects.semantecBits;

import com.CompanyName.projects.semantecBits.pages.MainPage;
import com.CompanyName.utils.Browser_Utils;
import com.CompanyName.utils.ConfigurationReader;
import com.CompanyName.utils.Driver;
import com.CompanyName.utils.Util_stuff;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class WEB_stepDefinitions extends Browser_Utils implements Util_stuff {
    MainPage page = new MainPage();

    @Given("I open Semantic Bits website")
    public void I_open_Semantic_Bits_website() {
        Driver.getDriver().get(ConfigurationReader.getProperty("SemanticBitsWebSite"));
        waitForTitleToEqual("SemanticBits – SemanticBits specializes in the design and development of software systems for the health and life sciences industries");
    }

    @When("I select {string} inside {string} module")
    public void I_select_inside_module(String selection, String module) {
        hover(page.getModuleByNameInGivenParent(page.header, module));
        clickButton(page.getModuleByNameInGivenParent(page.header, selection));
    }

    @Then("I verify I'm in {string} page")
    public void I_verify_Im_in_page(String expectedPage) {
        switch (expectedPage) {
            case "Software Development":
                Assert.assertEquals("This is not " + expectedPage + " page! - FAIL!",
                        "Software Development – SemanticBits", getTitle());
                break;
            case "Cloud Services":
                Assert.assertEquals("This is not " + expectedPage + " page! - FAIL!",
                        "Cloud – SemanticBits", getTitle());
                break;
        }
        System.out.println("Current page is as expected, it is " + getTitle());
    }
}
