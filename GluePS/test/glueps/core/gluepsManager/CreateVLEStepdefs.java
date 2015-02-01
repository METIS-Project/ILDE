package glueps.core.gluepsManager;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.assertTrue;


public class CreateVLEStepdefs {
 
    private String baseUrl = "http://localhost:8287";
    
    WebDriver driver = new FirefoxDriver();
    
    @Given("^I am on the GLUEPS initial page$")
    public void I_am_on_the_GLUEPS_initial_page() {
        //GLUEPSManagerServerMain.main(null);
    	driver.get(baseUrl + "/GLUEPSManager/gui/glueps/");
    	assertTrue(driver.findElement(By.id("manageVleButton"))!=null);
    }

    @When("^I provide valid VLE data$")
    public void I_provide_valid_VLE_data() {
    	driver.findElement(By.id("manageVleButton")).click();
    	driver.findElement(By.id("createVleInstallation")).click();
    	new Select(driver.findElement(By.id("dialogNewLeInstallation"))).selectByVisibleText("Moodle + GLUE en pandora");
    	driver.findElement(By.id("dialogNewLeName")).clear();
    	driver.findElement(By.id("dialogNewLeName")).sendKeys("Mi Moodle + GLUE en pandora");
    	driver.findElement(By.id("dialogNewLeUser")).clear();
    	driver.findElement(By.id("dialogNewLeUser")).sendKeys("lprisan");
    	driver.findElement(By.id("dialogNewLePassword")).clear();
    	driver.findElement(By.id("dialogNewLePassword")).sendKeys("lppslpps");
    	driver.findElement(By.id("acceptNewLe")).click();  
    }

    @Then("^I should see my VLE in the list of VLEs$")
    public void I_should_see_my_VLE_in_the_list_of_VLEs() {
    	assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Mi Moodle + GLUE en pandora[\\s\\S]*$"));
    	driver.findElement(By.id("acceptManageVles")).click();
    }
}
