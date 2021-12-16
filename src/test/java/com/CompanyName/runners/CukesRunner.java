package com.CompanyName.runners;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "html:target/cucumber-report.html",
                "rerun:target/rerun.txt",
                "json:target/cucumber-report.json"
        },
        features = "src/test/resources/features",
        glue = "com/CompanyName/projects",
        dryRun = false
//        ,tags = "@3002"
)
public class CukesRunner {
}
