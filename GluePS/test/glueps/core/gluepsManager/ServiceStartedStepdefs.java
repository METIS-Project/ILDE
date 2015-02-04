package glueps.core.gluepsManager;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.assertNotEquals;;


public class ServiceStartedStepdefs {
    private int code=0;
 
    WebDriver driver = new FirefoxDriver();
    
    @Given("^I run the GLUEPS Manager$")
    public void I_run_the_GLUEPS_Manager() {
        GLUEPSManagerServerMain.main(null);
    }

    @When("^I probe the service$")
    public void I_probe_the_service() {
    	//TODO: get the parameters from the configuration file
//		HttpClient httpclient = new DefaultHttpClient();
//		HttpGet httpget = new HttpGet("http://localhost:8287/GLUEPSManager/");
//		HttpResponse response = null;
//		try {
//			response = httpclient.execute(httpget);
//			code = response.getStatusLine().getStatusCode();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			if(httpget!=null) httpget.abort();
//			if(httpclient!=null) httpclient.getConnectionManager().shutdown();
//		}
    	driver.get("http://localhost:8287/GLUEPSManager/gui/glueps/");
    	//If nothing bad happened, we set the code to 1
        code=1;
    }

    @Then("^it should answer something valid$")
    public void it_should_answer_something_valid() {
        assertNotEquals(code, 0);
    }
}
