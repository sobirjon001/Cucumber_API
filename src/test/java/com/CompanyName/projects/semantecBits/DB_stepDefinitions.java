package com.CompanyName.projects.semantecBits;

import com.CompanyName.utils.DB_Utils;
import com.CompanyName.utils.Storage;
import com.CompanyName.utils.Useful_Utils;
import com.CompanyName.utils.Util_stuff;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import java.util.Map;

public class DB_stepDefinitions extends DB_Utils implements Util_stuff {

    Useful_Utils useful_utils = new Useful_Utils();

    @Then("I validate the issue by id {string} in database with data")
    public void I_validate_the_issue_by_id_in_database_with_data(String issueIdPath, Map<String, String> data) {
        runQuery("select * from public.issues i where i.id = "
                + useful_utils.defineValue(issueIdPath) + ";");
        for (String key : data.keySet()) {
            switch (key) {
                case "snowids":
                case "provider_types":
                    getAllColumnNamesAsList();
                    Assert.assertEquals(key + " mismatch! - FAIL!",
                            useful_utils.defineValue(data.get(key)).replace("\"", "")
                            .replace("[", "{").replace("]", "}"),
                            getCellValue(1, key));
                    System.out.println(key + " actual DB array value is " + getCellValue(1, key));
                    break;
                default:
                    if (data.get(key).equals("null")) {
                        Assert.assertNull(key + " is not null! - FAIL!",
                                getCellValue(1, key));
                        System.out.println(key + " actual DB value is null");
                    } else {
                        Assert.assertEquals(key + " mismatch! - FAIL!",
                                useful_utils.defineValue(data.get(key)),
                                getCellValue(1, key));
                        System.out.println(key + " actual DB value is " + getCellValue(1, key));
                    }
            }
        }
    }
}
