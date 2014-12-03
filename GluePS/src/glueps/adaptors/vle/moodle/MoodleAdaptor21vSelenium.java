package glueps.adaptors.vle.moodle;

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
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Course;
import glueps.core.model.Deploy;
import glueps.core.model.Group;
import glueps.core.model.Participant;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class MoodleAdaptor21vSelenium extends MoodleAdaptor{
	
	private WebDriver driver=null;
	 
	public MoodleAdaptor21vSelenium() {
		super();
	}


public MoodleAdaptor21vSelenium(String base, String template, String moodleUrl, String moodleUser, String moodlePassword, Map<String, String> parameters) {
	super(base, template,moodleUrl, moodleUser, moodlePassword, parameters);
}


public MoodleAdaptor21vSelenium(String base, String template,String modelPackage, String backupXmlFilename,
		String tmpDir, String moodleUrl, String moodleUser, String moodlePassword, Map<String, String> parameters) {
	
	this(base, template, moodleUrl, moodleUser, moodlePassword, parameters);

	//This is the package that contains the Moodle XML model classes
	CLASSES = modelPackage;
	
	//This is the name of the Moodle backup main xml file
	BACKUPFILENAME = backupXmlFilename; 
	
	//This is a temporary dir for storing files while we make the zipfile
	TMP_DIR = tmpDir; 
}

	@Override
	public HashMap<String, String> getInternalTools() {
		HashMap<String, String> tools = super.getInternalTools();
		tools.put("page", "Page (Moodle)");
		return tools;
	}
	
	/**
	 * Gets from a Moodle installation the courses in which the user is an administrator user
	 * @param moodleBaseUri The Base URI of the Moodle installation
	 * @param username The name of the administrator user
	 * @return The courses in which the user is the administrator
	 *  
	 */
	@Override
	public HashMap<String, String> getCourses(String moodleBaseUri, String username){
		HashMap<String,String> vleCourses = null;
		try{
			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			if (!moodleBaseUri.equals("http://campusvirtual.uva.es/")){
				driver.get(moodleBaseUri);
				driver.findElement(By.cssSelector("div.logininfo > a")).click();
			}else{
				driver.get(moodleBaseUri + "login/index.php");
			}				
		    //we do the user login here
		    driver.findElement(By.cssSelector("div.form-input > input#username")).sendKeys(moodleUser);
		    driver.findElement(By.cssSelector("div.form-input > input#password")).sendKeys(moodlePassword);
		    //driver.findElement(By.cssSelector("div.form-input > input[type=\"submit\"]")).click();
		    driver.findElement(By.cssSelector("input#loginbtn[type=\"submit\"]")).click();
		    //Here we should look for the courses
		    List<WebElement> archorCourses;
		    if (!moodleBaseUri.equals("http://campusvirtual.uva.es/")){
		    	archorCourses = (List<WebElement>) driver.findElements(By.cssSelector("div.coursebox > div.info a"));
		    }else{
		    	archorCourses = (List<WebElement>) driver.findElements(By.cssSelector("div.coursebox > div.course_title a"));
		    }
		    for (int i = 0; i < archorCourses.size(); i++){
		    	String href = archorCourses.get(i).getAttribute("href");
		    	if (href.contains("/course/view.php?id=")){
		    		String id = href.substring(href.indexOf("course/view.php?id=") + "course/view.php?id=".length());
		    		String fullname = archorCourses.get(i).getText();
					if (!id.isEmpty() && !fullname.isEmpty()){
						if (vleCourses == null){
							vleCourses = new HashMap<String, String>();
						}//Add the course to the hash map
						vleCourses.put(id, fullname);
					}
		    	}
		    }
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			driver.quit();
		}
		if (vleCourses!=null){
			System.out.println("The courses are: "+vleCourses.toString());
		}

		return vleCourses;
	}

	@Override
	public HashMap<String, Participant> getCourseUsers(String moodleBaseUri, String courseId) {
		
		/////////////////
		Boolean auth = moodleAuth(moodleBaseUri, moodleUser, moodleUrl);
		///////////////////
		
		HashMap<String, Participant> courseUsers = null;
		try{
			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.get(moodleBaseUri);
			if (!moodleBaseUri.equals("http://campusvirtual.uva.es/")){
				driver.get(moodleBaseUri);
				driver.findElement(By.cssSelector("div.logininfo > a")).click();
			}else{
				driver.get(moodleBaseUri + "login/index.php");
			}	
			
		    //we do the user login here
		    driver.findElement(By.cssSelector("div.form-input > input#username")).sendKeys(moodleUser);
		    driver.findElement(By.cssSelector("div.form-input > input#password")).sendKeys(moodlePassword);
		    driver.findElement(By.cssSelector("input#loginbtn[type=\"submit\"]")).click();
		    //Here we should look for the link containing the courseId
		    driver.findElement(By.xpath("(//a[contains(@href,'/course/view.php?id=" + courseId + "')])")).click();
		    //Here we go to the user page
		    driver.get(moodleBaseUri + "enrol/users.php?id=" + courseId);
		    
		    //Click to see the enrolled users
		    driver.findElement(By.xpath("(//a[contains(@href,'/enrol/users.php?id=" + courseId + "')])")).click();
		    //Here we should look for the users
		    List<WebElement> archorUsers = (List<WebElement>) driver.findElements(By.cssSelector("div.subfield_picture > a"));
		    List<String> hrefs = new ArrayList<String>(archorUsers.size());
		    for (int i = 0; i < archorUsers.size(); i++){
		    	hrefs.add(archorUsers.get(i).getAttribute("href"));
		    }
		    for (int i = 0; i < hrefs.size(); i++){
		    	String href = hrefs.get(i);
		    	if (href.contains("/user/view.php?id=") && href.contains("&course=")){
		    		String userId = href.substring(href.indexOf("/user/view.php?id=") + "/user/view.php?id=".length(), href.indexOf("&course="));
		    		String username = driver.findElement(By.cssSelector("tr#user_"+userId + " div.subfield_firstname")).getText();
		    		
		    		String email = driver.findElement(By.cssSelector("tr#user_"+userId + " div.subfield_email")).getText();
		    		String firstaccess = String.valueOf(new Date().getTime());
		    		
		    		Participant part = null;
					if (!userId.isEmpty() && !username.isEmpty()){
						if (courseUsers == null){
							courseUsers = new HashMap<String, Participant>();
						}//Add the participant to the hash map
						part = new Participant(userId,username, null,userId + Participant.USER_PARAMETER_SEPARATOR + username
								+ Participant.USER_PARAMETER_SEPARATOR + email + Participant.USER_PARAMETER_SEPARATOR + firstaccess + Participant.USER_PARAMETER_SEPARATOR,false);
						//The iCollage adaptor references users by the username, not the DB id
						courseUsers.put(part.getName(), part);
					}
					boolean staff = false;
					//driver.get(moodleBaseUri + "enrol/users.php?id=" + courseId);
					driver.get(moodleBaseUri + "user/view.php?id=" + userId + "&course=" + courseId);
					List<WebElement> anchorRoles = driver.findElements(By.xpath("(//a[contains(@href,'&roleid=')])"));
					for (int j=0; j < anchorRoles.size();j++){
						String hrefValue = anchorRoles.get(j).getAttribute("href");
						String roleId = hrefValue.substring(hrefValue.indexOf("&roleid=") + "&roleid=".length());
						if (roleId.equals("1") || roleId.equals("2") || roleId.equals("3") || roleId.equals("4")){
							staff = true;
							break;
						}
					}
					if (part!=null){
						part.setStaff(staff);
					}
					//go back to the users page
					driver.get(moodleBaseUri + "enrol/users.php?id=" + courseId);
		    	}
		    }
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			driver.quit();
		}
		return courseUsers;
	}
	
	@Override
	public HashMap<String,Participant> getUsers(String moodleBaseUri){
		
		HashMap<String, Participant> users = null;
		try{
			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.get(moodleBaseUri);
			driver.findElement(By.cssSelector("div.logininfo > a")).click();
		    //we do the user login here
		    driver.findElement(By.cssSelector("div.form-input > input#username")).sendKeys(moodleUser);
		    driver.findElement(By.cssSelector("div.form-input > input#password")).sendKeys(moodlePassword);
		    driver.findElement(By.cssSelector("div.form-input > input[type=\"submit\"]")).click();
		    //move to the user list page
		    driver.get(moodleUrl + "admin/user.php");
		    //Here we should look for the links to the users' profile
		    List<WebElement> anchorUsers = (List<WebElement>) driver.findElements(By.cssSelector("table.generaltable a"));
		    List<String> hrefs = new ArrayList<String>();
		    for (int i = 0; i < anchorUsers.size(); i++){
		    	String href = anchorUsers.get(i).getAttribute("href");
		    	if (href.contains("user/editadvanced.php?id=") && href.contains("&course=")){
		    		hrefs.add(href);
		    	}
		    }
		    for (int i = 0; i < hrefs.size(); i++){
		    	String href = hrefs.get(i);
		    	String userId = href.substring(href.indexOf("user/editadvanced.php?id=") + "user/editadvanced.php?id=".length(), href.indexOf("&course="));
		    	String courseId = href.substring(href.indexOf("&course=") + "&course=".length());
		    	driver.findElement(By.xpath("(//a[contains(@href,'user/editadvanced.php?id=" + userId + "&course=" + courseId +"')])")).click();
		    	String username = driver.findElement(By.cssSelector("input#id_username")).getAttribute("value");
		    	String email = driver.findElement(By.cssSelector("input#id_email")).getAttribute("value");
		    	String firstaccess = String.valueOf(new Date().getTime());
		    		
		    	Participant part = null;
				if (!userId.isEmpty() && !username.isEmpty()){
					if (users == null){
						users = new HashMap<String, Participant>();
					}//Add the participant to the hash map
					part = new Participant(userId,username, null,userId + Participant.USER_PARAMETER_SEPARATOR + username
								+ Participant.USER_PARAMETER_SEPARATOR + email + Participant.USER_PARAMETER_SEPARATOR + firstaccess + Participant.USER_PARAMETER_SEPARATOR,false);
					//the role can vary from course to course, so we set the staff value to false
					part.setStaff(false);
					//The iCollage adaptor references users by the username, not the DB id
					users.put(part.getName(), part);
				}
				//go back to the previous page
		    	driver.get(moodleUrl + "admin/user.php");
		    }
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			driver.quit();
		}
		return users;
	}
	

	@Override
	public HashMap<String, String> getCourses(String moodleBaseUri){

		HashMap<String,String> vleCourses = null;
		try{
			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.get(moodleBaseUri);
			driver.findElement(By.cssSelector("div.logininfo > a")).click();
		    //we do the user login here
		    driver.findElement(By.cssSelector("div.form-input > input#username")).sendKeys(moodleUser);
		    driver.findElement(By.cssSelector("div.form-input > input#password")).sendKeys(moodlePassword);
		    driver.findElement(By.cssSelector("div.form-input > input#loginbtn[type=\"submit\"]")).click();
		    //move to the course list page
		    driver.get(moodleUrl + "course/index.php?categoryedit=on");
		    //Here we should look for the links to the course categories
		    List<WebElement> anchorCategories = (List<WebElement>) driver.findElements(By.cssSelector("table.generalbox a"));
		    List<String> hrefs = new ArrayList<String>();
		    for (int i = 0; i < anchorCategories.size(); i++){
		    	String href = anchorCategories.get(i).getAttribute("href");
		    	if (href.contains("category.php?id=") && href.contains("&categoryedit=on")){
		    		hrefs.add(href);
		    	}
		    }
		    for (int i = 0; i < hrefs.size(); i++){
		    	String href = hrefs.get(i);
		    	String categoryId = href.substring(href.indexOf("category.php?id=") + "category.php?id=".length(), href.indexOf("&categoryedit=on"));
		    	//Check if there are courses in that category
		    	Integer courseCategory = Integer.parseInt(driver.findElements(By.cssSelector("table.generalbox td.count")).get(i).getText());
		    	if (courseCategory > 0){
			    	driver.findElement(By.xpath("(//a[contains(@href,'category.php?id=" + categoryId + "&categoryedit=on')])")).click();
			    	//Here we should look for the links to the courses in that category
			    	List<WebElement> anchorCourses = (List<WebElement>) driver.findElements(By.cssSelector("form#movecourses a"));
				    for (int j = 0; j < anchorCourses.size(); j++){
				    	String courseHref = anchorCourses.get(j).getAttribute("href");
				    	if (courseHref.contains("course/view.php?id=")){
				    		String id = courseHref.substring(courseHref.indexOf("course/view.php?id=") + "course/view.php?id=".length());
				    		String fullname = anchorCourses.get(j).getText();
							if (!id.isEmpty() && !fullname.isEmpty()){
								if (vleCourses == null){
									vleCourses = new HashMap<String, String>();
								}//Add the course to the hash map
								vleCourses.put(id, fullname);
							}
				    	}
				    }
				    driver.get(moodleUrl + "course/index.php?categoryedit=on");
		    	}
		    }
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			driver.quit();
		}
		System.out.println("The courses are: "+vleCourses.toString());
		return vleCourses;
	}
	
	@Override
	public HashMap<String, Group> getCourseGroups(String baseUri, String courseId) {
		
		HashMap<String, Group> courseGroups = null;
		try{
			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.get(moodleUrl);
			if (!moodleUrl.equals("http://campusvirtual.uva.es/")){
				driver.get(moodleUrl);
				driver.findElement(By.cssSelector("div.logininfo > a")).click();
			}else{
				driver.get(moodleUrl + "login/index.php");
			}	
		    //we do the user login here
		    driver.findElement(By.cssSelector("div.form-input > input#username")).sendKeys(moodleUser);
		    driver.findElement(By.cssSelector("div.form-input > input#password")).sendKeys(moodlePassword);
		    driver.findElement(By.cssSelector("input#loginbtn[type=\"submit\"]")).click();
		    //Here we should look for the link containing the courseId
		    driver.findElement(By.xpath("(//a[contains(@href,'/course/view.php?id=" + courseId + "')])")).click();
		    //Here we go to the group page
		    driver.get(moodleUrl + "group/index.php?id=" + courseId);
		    
			ArrayList<String> groupIds = new ArrayList<String>();
			Select selectGroups = new Select(driver.findElement(By.id("groups")));
			for (int i = 0; i < selectGroups.getOptions().size(); i++){				
				String empty = " ";
				String groupId = selectGroups.getOptions().get(i).getAttribute("value");
				if (!empty.equals(groupId)){
					groupIds.add(groupId);
				}
			}
			for (int i = 0; i < groupIds.size(); i++){
				String groupId = groupIds.get(i);
				//select the option
				selectGroups = new Select(driver.findElement(By.id("groups")));
				//deselect the previous one
				selectGroups.deselectAll();
				selectGroups.selectByValue(groupIds.get(i));
				driver.findElement(By.cssSelector("input#showeditgroupsettingsform[type=\"submit\"]")).click();
				String groupName = driver.findElement(By.cssSelector("input#id_name")).getAttribute("value");
				if (courseGroups == null){
					courseGroups = new HashMap<String, Group>();
				}
				//Add the group to the hash map	with an empty list of users									
				Group group = new Group(groupId, groupName, null, new ArrayList<String>());
				courseGroups.put(groupId, group);
				//Go back to the previous page
				driver.findElement(By.cssSelector("input#id_cancel[type=\"submit\"]")).click();
				
				selectGroups = new Select(driver.findElement(By.id("groups")));
				//deselect the previous one
				selectGroups.deselectAll();
				selectGroups.selectByValue(groupIds.get(i));
				//Get the id of every member of the group
				Select members = new Select(driver.findElement(By.id("members")));
				for (int j = 0; j < members.getOptions().size(); j++){
					String userId = members.getOptions().get(j).getAttribute("value");
					String username = members.getOptions().get(j).getAttribute("title");		    		
					if (!userId.isEmpty() && !username.isEmpty()){
						ArrayList<String> participants = group.getParticipantIds();
						if(!participants.contains(userId)){
							participants.add(userId);
						}
					}					
				}
				driver.get(moodleUrl + "group/index.php?id=" + courseId);
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			driver.quit();
		}
		if (courseGroups!=null){
			System.out.println("The groups are: "+ courseGroups);
		}else{
			System.out.println("There are no groups in the course");
		}

		return courseGroups;
		
	}
	   @Override
	   public boolean moodleAuth(String baseUri, String user, String pass){	
			Boolean auth = false;
			try{
				driver = new FirefoxDriver();
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				driver.get(moodleUrl);
				if (!moodleUrl.equals("http://campusvirtual.uva.es/")){
					driver.get(moodleUrl);
					driver.findElement(By.cssSelector("div.logininfo > a")).click();
				}else{
					driver.get(moodleUrl + "login/index.php");
				}	
			    //we do the user login here
			    driver.findElement(By.cssSelector("div.form-input > input#username")).sendKeys(moodleUser);
			    driver.findElement(By.cssSelector("div.form-input > input#password")).sendKeys(moodlePassword);
			    driver.findElement(By.cssSelector("input#loginbtn[type=\"submit\"]")).click();
			    //If the login failed we would see the login form again
			    List<WebElement> login = driver.findElements(By.cssSelector("div.logininfo > a"));
			    for (int i = 0; i < login.size(); i++){
				    if (login.get(i).getAttribute("href").contains("login/logout.php?")){
				    	auth = true;
				    	break;
				    }
			    }

			}catch(Exception e){
				e.printStackTrace();
				return false;
			}finally{
				driver.quit();
			}
			return auth;
	    }

}
