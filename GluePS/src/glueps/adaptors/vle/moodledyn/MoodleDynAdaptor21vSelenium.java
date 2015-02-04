package glueps.adaptors.vle.moodledyn;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import glueps.adaptors.vle.IDynamicVLEDeployer;
import glueps.adaptors.vle.moodle.MoodleAdaptor;
import glueps.adaptors.vle.moodle.MoodleAdaptor21v;
import glueps.adaptors.vle.moodle.MoodleAdaptor21vSelenium;
import glueps.adaptors.vle.moodle.model.moodle2.CourseAssignment;
import glueps.adaptors.vle.moodle.model.moodle2.CourseChat;
import glueps.adaptors.vle.moodle.model.moodle2.CourseEnrol;
import glueps.adaptors.vle.moodle.model.moodle2.CourseForum;
import glueps.adaptors.vle.moodle.model.moodle2.CourseGrouping;
import glueps.adaptors.vle.moodle.model.moodle2.CourseModule;
import glueps.adaptors.vle.moodle.model.moodle2.CoursePage;
import glueps.adaptors.vle.moodle.model.moodle2.CourseQuiz;
import glueps.adaptors.vle.moodle.model.moodle2.CourseSection;
import glueps.adaptors.vle.moodle.model.moodle2.CourseUrl;
import glueps.adaptors.vle.moodle.utils.Contenedor;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Activity;
import glueps.core.model.Course;
import glueps.core.model.Deploy;
import glueps.core.model.Group;
import glueps.core.model.Participant;


import org.openqa.selenium.*;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;


public class MoodleDynAdaptor21vSelenium extends MoodleAdaptor21vSelenium implements
		IDynamicVLEDeployer {

	
	  private WebDriver driver=null;
	  private String baseUrl=null;
	  private boolean acceptNextAlert = true;
	  private StringBuffer verificationErrors = new StringBuffer();

	
	public MoodleDynAdaptor21vSelenium() {
	}

	public MoodleDynAdaptor21vSelenium(String base, String template,String moodleUrl, String moodleUser, String moodlePassword, Map<String, String> parameters) {
		super(base, template, moodleUrl, moodleUser, moodlePassword, parameters);
	}

	public MoodleDynAdaptor21vSelenium(String base, String template, String modelPackage,
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
		    	
		    //Add the manual enroll method
		    String courseId=deploy.getCourse().getId().substring(deploy.getCourse().getId().lastIndexOf("/")+1);
		    driver.get(baseUrl + "enrol/instances.php?id=" + courseId);
		    new Select(driver.findElement(By.cssSelector("select[name=\"jump\"]"))).selectByValue("/enrol/manual/edit.php?courseid="+courseId);
		    driver.findElement(By.id("id_submitbutton")).click();
		    	
		    //Enroll the users in the course
		    driver.get(baseUrl + "enrol/users.php?id=" + courseId);
		    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		    ArrayList<Participant> participants = deploy.getParticipants();
		    String userid, roleid;
			for (int i = 0; i < participants.size(); i++){
				roleid = userid = "";
				Participant p = participants.get(i);
				String learningEn= p.getLearningEnvironmentData();
		        if(learningEn!=null){//if we have LE data	        	
			        String [] lista=learningEn.split(Participant.USER_PARAMETER_SEPARATOR);
			        //Para evitar que se produzca un error por que no existen participantes.
			        if ((lista !=null)&&(lista.length > 0)){
				        	userid = lista[0];
			        }else{
			        	//if no LEdata, we just put the userid=1...x
			        		userid = String.valueOf(i+1);
			        }			        	
		        }
		        //click the button to enroll the user
				driver.findElement(By.cssSelector("div.user[rel=\"" + userid + "\"] input[type=\"button\"]")).click();		        	
			}
			driver.findElement(By.cssSelector("div.close-button input[type=\"button\"]")).click();
			for (int i = 0; i < participants.size(); i++){
				roleid = userid = "";
				Participant p = participants.get(i);
				String learningEn= p.getLearningEnvironmentData();
		        if(learningEn!=null){//if we have LE data	        	
			        String [] lista=learningEn.split(Participant.USER_PARAMETER_SEPARATOR);
			        //Para evitar que se produzca un error por que no existen participantes.
			        if ((lista !=null)&&(lista.length > 0)){
				        	userid = lista[0];
			        }else{
			        	//if no LEdata, we just put the userid=1...x
			        	userid = String.valueOf(i+1);
			        }
			        if (p.isStaff()){
			        	driver.findElement(By.xpath("(//a[contains(@href,'action=assign') and contains(@href, 'user=" + userid + "')])")).click();
			        	//click to add the teacher role
			        	driver.findElement(By.id("add_assignable_role_3")).click();
			        }
		        }
			}
				
			//Add the groups to the course
			ArrayList<Group> groups = deploy.getGroups();
			driver.get(baseUrl + "group/index.php?id=" + courseId);
			for (int i = 0; i < groups.size(); i++){
				Group group = groups.get(i);
				driver.findElement(By.cssSelector("input#showcreateorphangroupform")).click();
				driver.findElement(By.cssSelector("input#id_name")).sendKeys(group.getName());
				driver.findElement(By.cssSelector("input#id_submitbutton[type=\"submit\"]")).click();
				if (group.getParticipantIds()!=null){
					//Add the members to the group
					new Select(driver.findElement(By.id("groups"))).selectByIndex(i);
					driver.findElement(By.cssSelector("input#showaddmembersform[type=\"submit\"]")).click();
					ArrayList<String> participantIds = group.getParticipantIds();
					for (int j = 0; j < participantIds.size(); j++){
						new Select(driver.findElement(By.id("addselect"))).selectByValue(participantIds.get(j));
						driver.findElement(By.cssSelector("input#add[type=\"submit\"]")).click();
					}
					driver.findElement(By.cssSelector("td#backcell input[type=\"submit\"][name=\"cancel\"]")).click();
						
				}
			}
			//Add the groupings to the course
			driver.get(baseUrl + "group/groupings.php?id=" + courseId);
			for (int i = 0; i < groups.size(); i++){
				Group group = groups.get(i);
				driver.findElement(By.cssSelector("form[action=\"grouping.php\"] input[type=\"submit\"]")).click();
				driver.findElement(By.cssSelector("input#id_name")).sendKeys(group.getName());
				driver.findElement(By.cssSelector("input#id_submitbutton[type=\"submit\"]")).click();
			}
			//Assign the groups to the groupings		    	
			List<WebElement> anchorGroupings = (List<WebElement>) driver.findElements(By.cssSelector("td.cell > a"));
			List<String> hrefs = new ArrayList<String>(anchorGroupings.size());
			for (int i = 0; i < anchorGroupings.size(); i++){
				String href = anchorGroupings.get(i).getAttribute("href");
				if (href.contains("assign.php?id=") && !href.substring(href.indexOf("assign.php?id=")+"assign.php?id=".length()).contains("&")){
					hrefs.add(href);
				}
			}
			for (int i = 0; i < hrefs.size(); i++){
				String href = hrefs.get(i);
				String groupingId = href.substring(href.indexOf("assign.php?id=")+ "assign.php?id=".length());
				driver.findElement(By.cssSelector("td.cell > a[href=\"assign.php?id=" + groupingId + "\"]")).click();
				//Each grouping is composed by only one group
				new Select(driver.findElement(By.id("addselect"))).selectByIndex(i);
				driver.findElement(By.cssSelector("input#add[type=\"submit\"]")).click();
				driver.findElement(By.cssSelector("td > input[type=\"submit\"][name=\"cancel\"]")).click();
			}
				
			//Assigning the groupings to the course modules
			driver.get(baseUrl + "/course/view.php?id=" + courseId);
			WebElement editInput = driver.findElement(By.cssSelector("form[action=\"" + baseUrl + "course/view.php\"] input[type=\"hidden\"][name=\"edit\"]"));
			//Click the edit button if necessary
			if (editInput.getAttribute("value").equals("on")){
				driver.findElement(By.cssSelector("form[action=\"" + baseUrl + "course/view.php\"] input[type=\"submit\"]")).click();
			}
			ArrayList<String> modulesGroupids = getModulesGroupids(deploy);
			List<WebElement> anchorModules = driver.findElements(By.cssSelector("span.commands > a.editing_update"));
			List<String> hrefModules = new ArrayList<String>(anchorModules.size());
			for (int i = 0; i < anchorModules.size(); i++){
				hrefModules.add(anchorModules.get(i).getAttribute("href"));
			}
			for (int i = 0; i < hrefModules.size(); i++){
				driver.findElement(By.xpath("(//a[contains(@href,'" + hrefModules.get(i).substring(hrefModules.get(i).indexOf("mod.php?update=")) + "')])")).click();
				//Check if the grouping select is hidden. If so, we have to click on the button to show the advanced options
				if (driver.findElement(By.id("fitem_id_groupingid")).getAttribute("class").contains("hide")){
					driver.findElement(By.cssSelector("div.advancedbutton input.showadvancedbtn")).click();
				}
				//select the group mode
				new Select(driver.findElement(By.id("id_groupmode"))).selectByIndex(0);
				//select the grouping
				String groupingid = modulesGroupids.get(i);
				for (int j = 0; j < groups.size(); j++){
					if (groups.get(j).getId().equals(groupingid)){
						new Select(driver.findElement(By.id("id_groupingid"))).selectByIndex(j+1);//Take into account that the first option is no grouping
						break;
					}
				}
				//check the group members only option
				WebElement gmoCheckbox = driver.findElement(By.id("id_groupmembersonly"));
				if (!gmoCheckbox.isSelected()){
					gmoCheckbox.click();
				}
				//save changes and go back to the course
				driver.findElement(By.id("id_submitbutton2")).click();
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
	
	
	private ArrayList<String> getModulesGroupids(Deploy lfdeploy){
        ArrayList<Group> groups = lfdeploy.getGroups();
		ArrayList<String> modulesGroupids = new ArrayList<String>(); //A grouping is set for each module
		HashMap prueba=generarHashmap(lfdeploy);
		prueba.remove("numSection");
		prueba.remove("auxiliar");
    	for (Iterator it = prueba.keySet().iterator(); it.hasNext();) {
    		// Get the key
	        String tipoModulo = (String) it.next();
	        ArrayList <Contenedor>listConten = (ArrayList <Contenedor>) ((ArrayList)prueba.get(tipoModulo)).get(1);	            
	        //for each module
	        for(int i=0; i<listConten.size();i++){
	        	Contenedor contSec=listConten.get(i);
	        	modulesGroupids.add(contSec.getGroupId());
	        }
    	}
    	return modulesGroupids;
	}

	@Override
	public boolean canBeDeployed(String baseUri, Deploy lfdeploy) {
		return true;
	}

}
