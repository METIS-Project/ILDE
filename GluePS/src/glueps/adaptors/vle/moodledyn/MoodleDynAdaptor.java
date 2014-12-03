package glueps.adaptors.vle.moodledyn;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import glueps.adaptors.vle.IDynamicVLEDeployer;
import glueps.adaptors.vle.moodle.MoodleAdaptor;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Deploy;


import org.openqa.selenium.*;


public class MoodleDynAdaptor extends MoodleAdaptor implements
		IDynamicVLEDeployer {

	
	  private WebDriver driver=null;
	  private String baseUrl=null;
	  private boolean acceptNextAlert = true;
	  private StringBuffer verificationErrors = new StringBuffer();

	
	public MoodleDynAdaptor() {
	}

	public MoodleDynAdaptor(String base, String template, String moodleUrl, String moodleUser, String moodlePassword, Map<String, String> parameters) {
		super(base, template, moodleUrl, moodleUser, moodlePassword, parameters);
	}

	public MoodleDynAdaptor(String base, String template, String modelPackage,
			String backupXmlFilename, String tmpDir, String moodleUrl, String moodleUser, String moodlePassword, Map<String, String> parameters) {
		super(base, template, modelPackage, backupXmlFilename,
				tmpDir, moodleUrl, moodleUser, moodlePassword, parameters);
	}


	  private void doMoodleUpload(String url, File file, Deploy deploy) throws Exception {
		
		try{
			boolean moodleTwo = false;
			//driver = new HtmlUnitDriver(true);//Apparently does not handle javascript right :(
			driver = new FirefoxDriver();
			baseUrl = url;
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	  
			String filename = file.getName();

			driver.get(baseUrl);
		    //driver.findElement(By.linkText("Entrar")).click();
			driver.findElement(By.cssSelector("div.logininfo > a")).click();
		    //we do the user login here
		    driver.findElement(By.cssSelector("div.form-input > input#username")).sendKeys(deploy.getLearningEnvironment().getCreduser());
		    driver.findElement(By.cssSelector("div.form-input > input#password")).sendKeys(deploy.getLearningEnvironment().getCredsecret());
		    driver.findElement(By.cssSelector("div.form-input > input[type=\"submit\"]")).click();
		    //Here we should look for the course name

		    driver.findElement(By.linkText(deploy.getCourse().getName())).click();
		    //Once inside the course, we click on the restore button
		    //driver.findElement(By.linkText("Restore")).click();
		    try{
		    	//Moodle 1.9
		    	driver.findElement(By.xpath("(//a[contains(@href,'backupdata')])")).click();
		    }
		    catch(Exception e){		   
		    	try{
		    		//The administration section could be not visible 
		    		boolean editEnabled = Boolean.valueOf(driver.findElement(By.cssSelector("input[type=\"hidden\"][name=\"edit\"]")).getAttribute("value"));
		    		if (!editEnabled){
		    			driver.findElement(By.cssSelector("input[type=\"hidden\"][name=\"edit\"]~input[type=\"submit\"]")).click();
		    		}
			    	String optionValue = driver.findElement(By.id("add_block_jump")).findElement(By.xpath("//option[contains(@value,'blockid=2')]")).getAttribute("value");
			    	new Select (driver.findElement(By.id("add_block_jump"))).selectByValue(optionValue);
		    	}
		    	catch(Exception e2){
		    		//Try with Moodle 2.2. The sequence of steps is different for this moodle version
			    	moodleTwo = true;
		    	}
		    }
		    
		    if (!moodleTwo){
		    	
			    driver.findElement(By.cssSelector("input[type=\"hidden\"][value=\"upload\"]~input[type=\"submit\"]")).click();	    
			    //driver.findElement(By.xpath("//input[@value='Upload a file']")).click();
			    
			    //driver.findElement(By.name("userfile")).clear();
			    driver.findElement(By.name("userfile")).sendKeys(file.toString());
			    driver.findElement(By.name("save")).click();
			    //Here we should also look for the deploy id 
			    WebElement elem = driver.findElement(By.xpath("(//a[contains(text(),'"+filename+"')]/../..//a[contains(@href,'restore')])"));
			    elem.click();
			    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
			    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
			    if (deploy.getFieldFromDeployData("startingSection")==null)
			    {
				    //We select the current course, deleting it first option
				    new Select(driver.findElement(By.id("menurestore_restoreto"))).selectByValue("0");
			    }else{
			    	//We select the current course, adding it second option
				    new Select(driver.findElement(By.id("menurestore_restoreto"))).selectByValue("1");
			    }
			    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
			    //Here we should look for the course name - if not present, maybe it is not an admin and that screen does not appear!
			    try{
			    	String courseId=deploy.getCourse().getId().substring(deploy.getCourse().getId().lastIndexOf("/")+1);
				    //driver.findElement(By.partialLinkText(deploy.getCourse().getName())).click();
			    	driver.findElement(By.xpath("(//a[contains(@href,'course_id="+courseId+"')])")).click();
			    }catch(Exception e){
			    	//We assume this is because the user is not an admin and thus the list of courses does not appear, we just go on
			    	e.printStackTrace();
			    }
			    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
			    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		
			    //TODO Should we check that something like our deploy's activities appears?
		    }else{
		    	driver.findElement(By.xpath("(//a[contains(@href,'restorefile.php')])")).click();
		    	driver.findElement(By.name("backupfilechoose")).click();
		    	driver.findElement(By.xpath("(//a[img[contains(@src,'component=repository_upload')]])")).click();
		    	driver.findElement(By.name("repo_upload_file")).sendKeys(file.toString());
		    	driver.findElement(By.cssSelector("div.fp-upload-btn > button")).click();
		    	driver.findElement(By.cssSelector("input[type=\"submit\"][name=\"submitbutton\"]")).click();
		    	driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		    	driver.findElement(By.cssSelector("div.bcs-current-course.backup-section input[type=\"radio\"][value=\"0\"]")).click();
		    	driver.findElement(By.cssSelector("div.bcs-current-course.backup-section input[type=\"submit\"]")).click();
		    	driver.findElement(By.name("submitbutton")).click();
		    	driver.findElement(By.name("submitbutton")).click();
		    	driver.findElement(By.name("submitbutton")).click();
		    	driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		    }
		    
		    
		}catch(Exception e){
			e.printStackTrace();
		    throw new Exception("Error while deploying with Selenium webdriver!");

		}finally{
			driver.quit();
		    String verificationErrorString = verificationErrors.toString();
		    if (!"".equals(verificationErrorString)) {
		      throw new Exception("Error while tearing down Selenium webdriver!");
		    }
		}
	  
	  }


	  private boolean isElementPresent(By by) {
	    try {
	      driver.findElement(by);
	      return true;
	    } catch (NoSuchElementException e) {
	      return false;
	    }
	  }

	  private String closeAlertAndGetItsText() {
	    try {
	      Alert alert = driver.switchTo().alert();
	      if (acceptNextAlert) {
	        alert.accept();
	      } else {
	        alert.dismiss();
	      }
	      return alert.getText();
	    } finally {
	      acceptNextAlert = true;
	    }
	  }

	
	
	
	@Override
	public Deploy deploy(String baseUri, Deploy lfdeploy) {
		// First, we generate the Moodle zip file to be uploaded
		File zipfile = null;
		try {
			zipfile = this.generateStaticDeploy(lfdeploy);
		} catch (Exception e) {
			//There was an error generating the zip file
			e.printStackTrace();
			//If something went wrong, we set location=null in case it has already a url
			lfdeploy.setLiveDeployURL(null);
			return lfdeploy;
		}
		
		// We deploy the zip file using Selenium WebDriver
		try {
			doMoodleUpload(baseUri, zipfile, lfdeploy);
		} catch (Exception e1) {
			e1.printStackTrace();
			//If something went wrong, we set location=null in case it has already a url
			lfdeploy.setLiveDeployURL(null);
			//There was an error during the deployment, we return as it is
			return lfdeploy;
		}
		
		// Once it is deployed, we set the liveURL to the course's URL
		String courseId = lfdeploy.getCourse().getId().substring(lfdeploy.getCourse().getId().lastIndexOf("/")+1);
		try {
			lfdeploy.setLiveDeployURL(new URL(baseUri+"course/view.php?id="+courseId));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return lfdeploy;
		}
		
		return lfdeploy;
	}

	@Override
	public Deploy undeploy(String baseUri, Deploy lfdeploy) {
		// TODO Maybe we should clear out the course, but I don't know how... for now, we do nothing!
		return lfdeploy;
	}

	@Override
	public Deploy redeploy(String string, Deploy newDeploy) {
		//In order to re-deploy, we just deploy (the original one will be deleted! sp. internal tools)
		return deploy (string, newDeploy);
	}

	@Override
	public boolean canBeDeployed(String baseUri, Deploy lfdeploy) {
		return true;
	}

}
