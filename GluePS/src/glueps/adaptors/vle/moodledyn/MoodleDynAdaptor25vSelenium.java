package glueps.adaptors.vle.moodledyn;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import glueps.adaptors.vle.IDynamicVLEDeployer;
import glueps.adaptors.vle.moodle.MoodleAdaptor21vSelenium;
import glueps.adaptors.vle.moodle.model.moodle2.CourseGrouping;
import glueps.adaptors.vle.moodle.utils.Contenedor;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Deploy;
import glueps.core.model.Group;
import glueps.core.model.Participant;


import org.openqa.selenium.*;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;


public class MoodleDynAdaptor25vSelenium extends MoodleAdaptor21vSelenium implements
		IDynamicVLEDeployer {

	
	  private WebDriver driver=null;
	  private String baseUrl=null;
	  private boolean acceptNextAlert = true;
	  private StringBuffer verificationErrors = new StringBuffer();

	
	public MoodleDynAdaptor25vSelenium() {
	}

	public MoodleDynAdaptor25vSelenium(String base, String template,
			GLUEPSManagerApplication applicationRest, String moodleUrl, String moodleUser, String moodlePassword) {
		super(base, template, applicationRest, moodleUrl, moodleUser, moodlePassword);
	}

	public MoodleDynAdaptor25vSelenium(String base, String template,
			GLUEPSManagerApplication applicationRest, String modelPackage,
			String backupXmlFilename, String tmpDir, String moodleUrl, String moodleUser, String moodlePassword) {
		super(base, template, applicationRest, modelPackage, backupXmlFilename,
				tmpDir, moodleUrl, moodleUser, moodlePassword);
	}


	  private void doMoodleUpload(String url, File file, Deploy deploy) throws Exception {
		
		try{
			//driver = new HtmlUnitDriver(true);//Apparently does not handle javascript right :(
			driver = new FirefoxDriver();
			baseUrl = url;
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	  
			String filename = file.getName();

			driver.get(baseUrl);
			driver.findElement(By.cssSelector("div.logininfo > a")).click();
		    //we do the user login here
		    driver.findElement(By.cssSelector("div.form-input > input#username")).sendKeys(deploy.getLearningEnvironment().getCreduser());
		    driver.findElement(By.cssSelector("div.form-input > input#password")).sendKeys(deploy.getLearningEnvironment().getCredsecret());
		    driver.findElement(By.cssSelector("div.form-input > input[type=\"submit\"]")).click();
		    
		    //Here we should look for the course name
		    driver.findElement(By.linkText(deploy.getCourse().getName())).click();
		    //get the edit url of the current modules in the course 
			String lfsection = deploy.getFieldFromDeployData(app.STARTING_SECTION_FIELD);
			List<String> oldHrefModules;
			if(lfsection!=null && lfsection.length()>0){
				WebElement editInput = driver.findElement(By.cssSelector("form[action=\"" + baseUrl + "course/view.php\"] input[type=\"hidden\"][name=\"edit\"]"));
				//Click the edit button if necessary
				if (editInput.getAttribute("value").equals("on")){
					driver.findElement(By.cssSelector("form[action=\"" + baseUrl + "course/view.php\"] input[type=\"submit\"]")).click();
				}
			    List<WebElement> oldAnchorModules = driver.findElements(By.cssSelector("span.commands > a.editing_update"));
				oldHrefModules = new ArrayList<String>(oldAnchorModules.size());
				for (int i = 0; i < oldAnchorModules.size(); i++){
					oldHrefModules.add(oldAnchorModules.get(i).getAttribute("href"));
				}
			}else{
				//The current modules in the course will be deleted, so we empty the list
				oldHrefModules = new ArrayList<String>();
			}

			//restore the course using the backup file
		    driver.findElement(By.xpath("(//a[contains(@href,'restorefile.php')])")).click();
		   	driver.findElement(By.name("backupfilechoose")).click();
		  	driver.findElement(By.xpath("(//a[img[contains(@src,'/standard/repository_upload')]])")).click();
		    driver.findElement(By.name("repo_upload_file")).sendKeys(file.toString());
		    driver.findElement(By.cssSelector("div > button.fp-upload-btn")).click();
		    driver.findElement(By.cssSelector("input[type=\"submit\"][name=\"submitbutton\"]")).click();
		    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
			if(lfsection!=null && lfsection.length()>0){
			    //We choose to keep the course content and restore in the same course
			    driver.findElement(By.cssSelector("div.bcs-current-course.backup-section input[type=\"radio\"][value=\"1\"]")).click();
			}else{
				//We choose to delete the course content and then restore in the same course
				driver.findElement(By.cssSelector("div.bcs-current-course.backup-section input[type=\"radio\"][value=\"0\"]")).click();
			}
		    driver.findElement(By.cssSelector("div.bcs-current-course.backup-section input[type=\"submit\"]")).click();
		    driver.findElement(By.name("submitbutton")).click();		    
		    if(lfsection==null || lfsection.length()==0){
			    //select the option to keep the current roles and enrolments
			    new Select(driver.findElement(By.cssSelector("select[id=\"id_setting_course_keep_roles_and_enrolments\"]"))).selectByValue("1");
			    //unselect the option to keep the current groups and groupings
			    new Select(driver.findElement(By.cssSelector("select[id=\"id_setting_course_keep_groups_and_groupings\"]"))).selectByValue("0");
		    }
		    //unselect the option to override the course configuration
		    new Select(driver.findElement(By.cssSelector("select[id=\"id_setting_course_overwrite_conf\"]"))).selectByValue("0");
		    driver.findElement(By.name("submitbutton")).click();
		    driver.findElement(By.name("submitbutton")).click();
		    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		    
	    	String courseId=deploy.getCourse().getId().substring(deploy.getCourse().getId().lastIndexOf("/")+1);
		    //Add the manual enroll method, if it is not already added
		    try{
			    driver.get(baseUrl + "enrol/instances.php?id=" + courseId);
			    new Select(driver.findElement(By.cssSelector("select[name=\"jump\"]"))).selectByValue("/enrol/manual/edit.php?courseid="+courseId);
			    driver.findElement(By.id("id_submitbutton")).click();
		    }catch(Exception e){
		    }
		    	
		    driver.get(baseUrl + "enrol/users.php?id=" + courseId);
		    String userid;
		    ArrayList<Participant> participants = deploy.getParticipants();
		    ArrayList<Participant> notEnrolled = new ArrayList<Participant>();
		    for (int i = 0; i < participants.size(); i++){
				userid = "";
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
		        try{
			        //check if the user is already enrolled
		        	driver.findElement(By.xpath("(//a[contains(@href,'/user/view.php?id=" + userid + "&course=" + courseId + "')])"));
		        }catch(Exception e){
		        	notEnrolled.add(p);		        	
		        }
		    }
		  //Enroll the users that are not already enrolled in the course
		    if (notEnrolled.size() > 0 ){
			    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
			    //Select to assign the student role
			    new Select(driver.findElement(By.id("id_enrol_manual_assignable_roles"))).selectByValue("5");
				for (int i = 0; i < notEnrolled.size(); i++){
					userid = "";
					Participant p = notEnrolled.get(i);
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
				for (int i = 0; i < notEnrolled.size(); i++){
					userid = "";
					Participant p = notEnrolled.get(i);
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
				        	try{
				        		driver.findElement(By.id("add_assignable_role_3")).click();
				        	}catch(Exception e){
				        		//driver.findElement(By.cssSelector("div.close")).click();
				        	}
				        }
			        }
				}
		    }
		    ArrayList<Group> groups;
		    if(lfsection!=null && lfsection.length()>0){
				//keep the previous groups
				HashMap<String, Group> courseGroups = getCourseGroups(url, deploy.getCourse().getId());
				//Get the new groups from the deploy
				ArrayList<Group> newGroups = new ArrayList<Group>();
				for (int i = 0; i < deploy.getGroups().size(); i++){
					Group deployGroup = deploy.getGroups().get(i);
					boolean exists = false;
					if (courseGroups!=null){
						Iterator it;
						for (it = courseGroups.entrySet().iterator(); it.hasNext();){
							Map.Entry<String, Group> e = (Map.Entry<String, Group>)it.next();
							Group courseGroup = e.getValue();
							//Check if the group already exists in Moodle by the username
							if (courseGroup.getName().toLowerCase().equals(deployGroup.getName().toLowerCase())){
								exists = true;
							}
						}
					}
					if (exists == false){
						newGroups.add(deployGroup);
					}
				}
				groups = newGroups;
		    }else{
				groups = deploy.getGroups();
		    }
			ArrayList<Group> deployGroups = new ArrayList<Group>();
			for (int i = 0; i < groups.size(); i++){
				Group g1 = groups.get(i);
				Boolean repeated = false;
				for (int j = 0; j < i; j++){
					Group g2 = groups.get(j);
					if (g1.getName().toLowerCase().equals(g2.getName().toLowerCase())){
						repeated = true;
					}
				}
				if (!repeated){
					deployGroups.add(g1);
				}
			}
			//Add the groups to the course
			driver.get(baseUrl + "group/index.php?id=" + courseId);
			for (int i = 0; i < deployGroups.size(); i++){
				Group group = deployGroups.get(i);
				driver.findElement(By.cssSelector("input#showcreateorphangroupform")).click();
				driver.findElement(By.cssSelector("input#id_name")).sendKeys(group.getName());
				driver.findElement(By.cssSelector("input#id_submitbutton[type=\"submit\"]")).click();
				if (group.getParticipantIds()!=null){
					//Add the members to the group (it is empty)
					new Select(driver.findElement(By.id("groups"))).selectByVisibleText(group.getName() + " (0)");
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
			for (int i = 0; i < deployGroups.size(); i++){
				Group group = deployGroups.get(i);
				driver.findElement(By.cssSelector("form[action=\"grouping.php\"] input[type=\"submit\"]")).click();
				driver.findElement(By.cssSelector("input#id_name")).sendKeys(group.getName());
				driver.findElement(By.cssSelector("input#id_submitbutton[type=\"submit\"]")).click();
			}
			
			//Get the course grouping info including their ids
			List<WebElement> weEditGroupings = (List<WebElement>) driver.findElements(By.xpath("(//td/a[contains(@href,'grouping.php?id=') and not (contains(@href,'delete='))])"));
			ArrayList<String> hrefEditGroupings = new ArrayList<String>(weEditGroupings.size());
			for (int i = 0; i < weEditGroupings.size(); i++){
				hrefEditGroupings.add(weEditGroupings.get(i).getAttribute("href"));
			}				
			ArrayList<CourseGrouping> courseGroupings = new ArrayList<CourseGrouping>();
			for (int i = 0; i < hrefEditGroupings.size(); i++){
				String href = hrefEditGroupings.get(i);
				String cgId = href.substring(href.indexOf("grouping.php?id=")+"grouping.php?id=".length());
				driver.findElement(By.xpath("(//td/a[contains(@href,'grouping.php?id=" + cgId + "') and not (contains(@ref,'delete='))])")).click();
				String cgName = driver.findElement(By.cssSelector("input#id_name")).getAttribute("value");
				CourseGrouping cg = new CourseGrouping(Integer.parseInt(cgId), Integer.parseInt(courseId), cgName, null);
				courseGroupings.add(cg);
				driver.findElement(By.cssSelector("input#id_cancel[type=\"submit\"]")).click();
			}			
			//Assign the groups to the groupings		    	
			for (int i = 0; i < deployGroups.size(); i++){
				for (int j = 0; j < courseGroupings.size(); j++){
					if (deployGroups.get(i).getName().toLowerCase().equals(courseGroupings.get(j).getName().toLowerCase())){
						driver.findElement(By.xpath("(//td/a[contains(@href,'assign.php?id=" + courseGroupings.get(j).getId() + "')])")).click();
						new Select(driver.findElement(By.id("addselect"))).selectByVisibleText(deployGroups.get(i).getName());
						driver.findElement(By.cssSelector("input#add[type=\"submit\"]")).click();
						driver.findElement(By.cssSelector("td > input[type=\"submit\"][name=\"cancel\"]")).click();
						break;
					}
				}
			}
				
			//Assigning the groupings to the course modules
			driver.get(baseUrl + "course/view.php?id=" + courseId);
			WebElement editInput = driver.findElement(By.cssSelector("form[action=\"" + baseUrl + "course/view.php\"] input[type=\"hidden\"][name=\"edit\"]"));
			//Click the edit button if necessary
			if (editInput.getAttribute("value").equals("on")){
				driver.findElement(By.cssSelector("form[action=\"" + baseUrl + "course/view.php\"] input[type=\"submit\"]")).click();
			}
			ArrayList<String> modulesGroupids = getModulesGroupids(deploy);
			List<WebElement> anchorModules = driver.findElements(By.cssSelector("span.commands > a.editing_update"));
			List<String> hrefModules = new ArrayList<String>();
			for (int i = 0; i < anchorModules.size(); i++){
				boolean existed = false;
				for (int j = 0; j < oldHrefModules.size(); j++){
					if (oldHrefModules.get(j).equals(anchorModules.get(i).getAttribute("href"))){
						existed = true;
						break;
					}
				}
				if (!existed){
					hrefModules.add(anchorModules.get(i).getAttribute("href"));
				}
			}
			for (int i = 0; i < hrefModules.size(); i++){
				driver.findElement(By.xpath("(//a[contains(@class,'editing_update')][contains(@href,'" + hrefModules.get(i).substring(hrefModules.get(i).indexOf("&update=")) + "')])")).click();
				//Check if the grouping select is hidden. If so, we have to click on the link to expand the advanced options
				if (driver.findElement(By.xpath("(//a[contains(@class,'collapseexpand')])")).getAttribute("class").contains("collapse-all")==false){
					driver.findElement(By.xpath("(//a[contains(@class,'collapseexpand')])")).click();
				}
				//select the group mode. Sometimes this select is not available
				/*try{
					new Select(driver.findElement(By.id("id_groupmode"))).selectByIndex(0);
				}catch(Exception e){					
				}*/
				//select the grouping
				String groupid = modulesGroupids.get(i);
				for (int j = 0; j < deploy.getGroups().size(); j++){
					if (deploy.getGroups().get(j).getId().equals(groupid)){
						for (int k = 0; k < courseGroupings.size(); k++){
							if (deploy.getGroups().get(j).getName().toLowerCase().equals(courseGroupings.get(k).getName().toLowerCase())){
								new Select(driver.findElement(By.id("id_groupingid"))).selectByValue(String.valueOf(courseGroupings.get(k).getId()));
							}									
						}
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

}
