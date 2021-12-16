package com.CompanyName.projects.semantecBits.pages;

import com.CompanyName.utils.Driver;
import org.openqa.selenium.support.PageFactory;

public class MainPage {

    public MainPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }
}
