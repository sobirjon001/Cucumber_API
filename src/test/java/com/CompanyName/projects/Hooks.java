package com.CompanyName.projects;

import com.CompanyName.utils.Driver;
import io.cucumber.java.After;

public class Hooks {

    @After
    public void closeBrowser() {
        Driver.close();
    }
}
