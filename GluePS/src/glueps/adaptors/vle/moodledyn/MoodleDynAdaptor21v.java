package glueps.adaptors.vle.moodledyn;

import java.io.File;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import glueps.adaptors.vle.IDynamicVLEDeployer;
import glueps.adaptors.vle.IVLEAdaptor;
import glueps.adaptors.vle.VLEAdaptorFactory;
import glueps.adaptors.vle.moodle.MoodleAdaptor;
import glueps.adaptors.vle.moodle.MoodleAdaptor21v;
import glueps.adaptors.vle.moodle.model.MODS;
import glueps.adaptors.vle.moodle.model.SECTION;
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
import glueps.adaptors.vle.moodle.model.moodle2.Module;
import glueps.adaptors.vle.moodle.utils.Contenedor;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Activity;
import glueps.core.model.Course;
import glueps.core.model.Deploy;
import glueps.core.model.Group;
import glueps.core.model.InstancedActivity;
import glueps.core.model.Participant;
import glueps.core.model.ToolInstance;


import org.openqa.selenium.*;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;


public class MoodleDynAdaptor21v extends MoodleAdaptor21v implements
		IDynamicVLEDeployer {

	
	  private WebDriver driver=null;
	  private String baseUrl=null;
	  private boolean acceptNextAlert = true;

	
	public MoodleDynAdaptor21v() {
	}

	public MoodleDynAdaptor21v(String base, String template,String moodleUrl, String moodleUser, String moodlePassword, String wstoken, Map<String, String> parameters) {
		super(base, template, moodleUrl, moodleUser, moodlePassword, wstoken, parameters);
	}

	public MoodleDynAdaptor21v(String base, String template, String modelPackage,
			String backupXmlFilename, String tmpDir, String moodleUrl, String moodleUser, String moodlePassword, String wstoken, Map<String, String> parameters) {
		super(base, template, modelPackage, backupXmlFilename,
				tmpDir, moodleUrl, moodleUser, moodlePassword,wstoken, parameters);
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
		/*HashMap<String, Participant> users = getCourseUsers(baseUri, lfdeploy.getCourse().getId());
		ArrayList<CourseEnrol> courseEnrols = getCourseEnrols(lfdeploy.getCourse());
		for (int i = 0; i < courseEnrols.size(); i++){
			CourseEnrol enrol = courseEnrols.get(i);
			if (users!=null){
				//for each user we delete his enrol of that type in the course, if this exists
				Iterator it = users.entrySet().iterator();
				while (it.hasNext()){
					Map.Entry<String, Participant> e = (Map.Entry<String,Participant>)it.next();
					disenrollUser(lfdeploy.getCourse(),e.getValue(), enrol.getId());
				}
			}
			//Once all the users have been disenrolled we delete the enrol type 
			deleteCourseEnrol(enrol.getId(), Integer.parseInt(lfdeploy.getCourse().getId()));
		}
		//It is necessary to add the manual enrolment method for that course
		Integer enrolid = addEnrolMethod(lfdeploy.getCourse(), "manual");
		if (users!=null){
			ArrayList<Participant> courseParts = new ArrayList<Participant>();
			Iterator it = users.entrySet().iterator();
			while (it.hasNext()){
				Map.Entry<String, Participant> e = (Map.Entry<String, Participant>)it.next();
				Participant p = e.getValue();
				courseParts.add(p);
			}
			//Enrol the previous users in the course
			enrolUsers(lfdeploy.getCourse(), courseParts);
		}
		*/
		ArrayList<CourseEnrol> courseEnrols = getCourseEnrols(lfdeploy.getCourse());
		boolean manualEnrol = false;
		for (int i = 0; i < courseEnrols.size(); i++){
			CourseEnrol enrol = courseEnrols.get(i);
			if (enrol.getEnrol().equals("manual")){
				manualEnrol = true;
				break;
			}
		}
		if (manualEnrol == false){
			//add the manual enrol method
			Integer enrolid = addEnrolMethod(lfdeploy.getCourse(), "manual");
		}		
		
		//Enrol the users in the course
		enrolUsers(lfdeploy.getCourse(), lfdeploy.getParticipants());
		
		Integer startingSection;
		boolean addingContent;
		String lfsection = lfdeploy.getFieldFromDeployData(this.getStartingSectionField());
		if(lfsection!=null && lfsection.length()>0){
			startingSection = Integer.parseInt(lfsection);
			addingContent = true;
			
			//keep the previous groups
			HashMap<String, Group> courseGroups = getCourseGroups(baseUri, lfdeploy.getCourse().getId());
			//Get the new groups from the deploy
			ArrayList<Group> newGroups = new ArrayList<Group>();
			for (int i = 0; i < lfdeploy.getGroups().size(); i++){
				Group deployGroup = lfdeploy.getGroups().get(i);
				boolean exists = false;
				if (courseGroups!=null){
					for (Iterator it = courseGroups.entrySet().iterator(); it.hasNext();){
						Map.Entry<String, Group> e = (Map.Entry<String, Group>)it.next();
						Group courseGroup = e.getValue();
						//Check if the group already exists in Moodle by the username. Maybe, it should better be made by the ID
						if (courseGroup.getName().equals(deployGroup.getName())){
							exists = true;
						}
					}
				}
				if (exists == false){
					newGroups.add(deployGroup);
				}
			}
			if (newGroups.size() >0){
				//Add the new groups to the course
				ArrayList<Integer> newGroupids = createGroups(lfdeploy.getCourse(), newGroups);
				for (int i = 0; i < newGroups.size(); i++){
					Group group = newGroups.get(i);
					if (group.getParticipantIds()!=null){
						//Add the members to the group
						addGroupMembers(newGroupids.get(i), group.getParticipantIds());
					}
				}
			
				//keep the previous groupings
				//ArrayList<CourseGrouping> cg = getCourseGroupings(lfdeploy.getCourse());
				//Add the new groupings to the course
				ArrayList<Integer> newGroupingids = createGroupings(lfdeploy.getCourse(), newGroups);
				assignGrouping(newGroupingids, newGroupids);
			}
			
		}else{
			//by default the starting section is 1
			startingSection = 1;
			addingContent = false;
			
			//delete the previous groups
			HashMap<String, Group> groups = getCourseGroups(baseUri, lfdeploy.getCourse().getId());
			if (groups!=null){
				ArrayList<Integer> oldGroupids = new ArrayList<Integer>();
				Iterator it = groups.entrySet().iterator();
				while (it.hasNext()){
					Map.Entry<String, Group> e = (Map.Entry<String, Group>)it.next();
					oldGroupids.add(Integer.parseInt(e.getValue().getId()));
				}
				this.deleteCourseGroups(oldGroupids);
			}		
			//Add the different groups to the course
			ArrayList<Group> deployGroups = new ArrayList<Group>();
			//Check that there isn't another group with such a name in the deploy. Moodle doesn't accept two groups with the same name
			for (int i = 0; i < lfdeploy.getGroups().size(); i++){
				Group g1 = lfdeploy.getGroups().get(i);
				boolean repeated = false;
				for (int j = 0; j < i; j++){
					Group g2 = lfdeploy.getGroups().get(j);
					if (g1.getName().equals(g2.getName()))
					{
						repeated = true;
					}
				}
				if (!repeated){
					deployGroups.add(g1);
				}
			}
			ArrayList<Integer> newGroupids = createGroups(lfdeploy.getCourse(), deployGroups);
			for (int i = 0; i < deployGroups.size(); i++){
				Group group = deployGroups.get(i);
				if (group.getParticipantIds()!=null){
					//Add the members to the group
					addGroupMembers(newGroupids.get(i), group.getParticipantIds());
				}
			}
			
			//delete the previous groupings
			ArrayList<CourseGrouping> cg = getCourseGroupings(lfdeploy.getCourse());
			deleteCourseGroupings(cg);	
			//Add the groupings to the course
			ArrayList<Integer> newGroupingids = createGroupings(lfdeploy.getCourse(), deployGroups);
			assignGrouping(newGroupingids, newGroupids);
			
			//delete the previous course content
			deleteCourseModulesContent(lfdeploy.getCourse());
		}		
		//Get the updated info about the course groupings
		ArrayList<CourseGrouping> cg = getCourseGroupings(lfdeploy.getCourse());
		insertCourseModulesContent(lfdeploy, cg);
		
		//Update the sequence attribute of every section
		ArrayList<CourseModule> modules = getCourseModules(lfdeploy.getCourse());
		ArrayList<CourseSection> cs = getCourseSections(lfdeploy.getCourse());
		ArrayList gluepsSections = (ArrayList) (getAllActivitys(lfdeploy.getDesign().getRootActivity())).get(1); //The sections according to the design info
		for (int i = 0; i < cs.size(); i++){
			CourseSection section = cs.get(i);
			ArrayList<Integer> moduleids = new ArrayList<Integer>();
			for (int j = 0; j < modules.size(); j++){
				CourseModule module = modules.get(j);
				if (section.getId().equals(module.getSection())){
					moduleids.add(module.getId());
				}
			}
			StringBuilder s = new StringBuilder();
			for (int j = 0; j < moduleids.size(); j++){
				s.append(moduleids.get(j).intValue()).append(",");
			}
			if (s.length()>0){
				String sequence = s.toString().substring(0, s.toString().length()-1);
				section.setSequence(sequence);
			}else{
				section.setSequence("");
			}
			int sectionNumber = section.getSection().intValue();
			if ( (startingSection <= sectionNumber) && (sectionNumber < (startingSection+gluepsSections.size())) ){
				//Update the summary field of the section
	    		String nameSect = getNameSection(lfdeploy.getDesign().getRootActivity(),sectionNumber - startingSection);
	    		//we erase the Root/Method part, which is useless, and add a link back to the GLUE!-PS, in case changes have to be made to the deploy
	    		nameSect = nameSect.replace("Root/Method - ", "");
	    		String lfdeployUrl = getAppExternalUri() + "gui/glueps/deploy.html?deployId=" + lfdeploy.getId();
	    		if (getLdShakeMode()==false){
	    			nameSect += "... this section was generated with <a href=\""+ lfdeployUrl + "\" target=\"_new\">GLUE!-PS</a><br></br>";
	    		}
	    		section.setSummary(nameSect);
			}else{
				if (!addingContent){
					section.setSummary("");
				}
			}
			//Set the visible value to true
			section.setVisible(1);
			updateCourseSection(section);
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
	
	/**
	 * Delete the existing content in the course in order to be able to deploy the new content
	 * @param course
	 */
	private void deleteCourseModulesContent(Course course){
		//Get the modules created of each type
		ArrayList<CourseAssignment> assignments = getCourseAssignments(course);
		ArrayList<CourseChat> chats = getCourseChats(course);
		ArrayList<CourseForum> forums = getCourseForums(course);
		ArrayList<CoursePage> pages = getCoursePages(course);
		ArrayList<CourseQuiz> quizzes = getCourseQuizzes(course);
		ArrayList<CourseUrl> urls = getCourseUrls(course);
		//delete these modules
		deleteCourseAssignments(assignments);
		deleteCourseChats(chats);
		deleteCourseForums(forums);
		deleteCoursePages(pages);
		deleteCourseQuizzes(quizzes);
		deleteCourseUrls(urls);
		//Get the course modules
		ArrayList<CourseModule> modules = getCourseModules(course);
		//delete all the course modules
		deleteCourseModules(modules);
	}
	
	private void insertCourseModulesContent(Deploy lfdeploy, ArrayList<CourseGrouping> groupings){
		HashMap info=generarHashmap(lfdeploy);
		info.remove("numSection");
		info.remove("auxiliar");
		ArrayList<Integer> assignments = insertCourseAssignments(lfdeploy, info);
		ArrayList<Integer> chats = insertCourseChats(lfdeploy, info);
		ArrayList<Integer> forums = insertCourseForums(lfdeploy, info);
		ArrayList<Integer> pages = insertCoursePages(lfdeploy, info);
		ArrayList<Integer> quizzes = insertCourseQuizzes(lfdeploy, info);
		ArrayList<Integer> urls = insertCourseUrls(lfdeploy, info);
		ArrayList<Integer> modulesGroupingids = getModulesGroupingids(lfdeploy, groupings);
		insertCourseModules(lfdeploy, urls, forums, chats, quizzes, assignments, pages, modulesGroupingids);
	}
	
	private ArrayList<Integer> insertCourseUrls(Deploy lfdeploy, HashMap prueba){
		boolean displayGlueletInFrame = true;
		boolean requiresCallerUser;
		
		/**This if is just to skip a bug in the Moodle version used by that Moodle installation when displaying the content in a frame . Please, remember to delete these code lines when they are no longer necessary*/
		if (lfdeploy.getLearningEnvironment().getAccessLocation().toString().startsWith("http://www.edaverneda.org/moodle/")){
			displayGlueletInFrame =false;
		}
		
		ArrayList<Integer> urlids = new ArrayList<Integer>();
    	for (Iterator it = prueba.keySet().iterator(); it.hasNext();) {
    		// Get the key
	        String tipoModulo = (String) it.next();
	        if (tipoModulo.equals("resource")){
		        ArrayList <Activity> listActMod = (ArrayList <Activity>) ((ArrayList)prueba.get(tipoModulo)).get(0);
		        ArrayList <Contenedor>listConten = (ArrayList <Contenedor>) ((ArrayList)prueba.get(tipoModulo)).get(1);	            
		        //for each module
		        for(int i=0; i<listConten.size();i++){
		        	Contenedor contSec=listConten.get(i);
		        	
	        		String description = "No description has been provided.";
		            for (Activity activity: listActMod){
		            	if(activity.getId().equals(contSec.getIdActivity())){
		            		if (!activity.getDescription().trim().isEmpty()){
		            			description = activity.getDescription();
		            		}
		            		break;
		            	}
		            }
		            String location = "";
		            if (contSec.getLocation()!=null){
		            	location = contSec.getLocation();
		            }
		    		if (!location.contains("GLUEletManager") || (location.contains("GLUEletManager") && location.contains("callerUser")) ){
		    			requiresCallerUser = false;
		    		}else{
		    			requiresCallerUser = true;
		    		}
	        		if (!requiresCallerUser){
	        			description += "<p>If you can not see the resource in the frame below, click on the link to open it: <a href=\"" + location + "\" target=\"_blank\">" + contSec.getModName() + "</a></p>";
	        		}else if(displayGlueletInFrame){
	        			description += "<p>If you can not see the resource in the frame below, click on the link to open it: <a href=\"" + location + "?callerUser=" + "\" target=\"_blank\">" + contSec.getModName() + "</a></p>";
		            }
	        		
	        		String groupId ="";
	        		ToolInstance toolInstReused = lfdeploy.getToolInstanceByLocation(contSec.getLocation());
	        		if (toolInstReused!=null){
	        			toolInstReused = lfdeploy.getOriginalToolInstance(toolInstReused);
	        			HashMap<String, InstancedActivity> ia = lfdeploy.getInstancedActivitiesForToolInstance(toolInstReused.getId());
	        			if (ia!=null){
	        				Iterator<Map.Entry<String,InstancedActivity>> itInstAct = ia.entrySet().iterator();
	        				while(itInstAct.hasNext()){
	        					Map.Entry<String, InstancedActivity> entry = itInstAct.next();
	        					InstancedActivity instAct = entry.getValue();
	        					groupId = instAct.getGroupId();
	        				}
	        			}
	        		}else{
	        			groupId = contSec.getGroupId();
	        		}
	        		String groupName = "";
	        		ArrayList<Group> groups = lfdeploy.getGroups();
	        		for (int g = 0; g < groups.size(); g++){
	        			if (groups.get(g).getId().equals(groupId)){
	        				groupName = " (" + groups.get(g).getName() + ")";
	        				break;
	        			}
	        		}
	        		
		    		CourseUrl url = new CourseUrl(null, Integer.parseInt(lfdeploy.getCourse().getId()), contSec.getModName() + groupName, description, 1, location);
		    		if (!requiresCallerUser || displayGlueletInFrame){
			    		url.setDisplay(2);
		    		}else{
			    		url.setDisplay(1);
		    		}
		    		url.setDisplayoptions("a:2:{s:12:\"printheading\";i:0;s:10:\"printintro\";i:1;}");
		    		if (requiresCallerUser){
		    			url.setParameters("a:1:{s:10:\"callerUser\";s:12:\"userusername\";}");
		    		}else{
		    			url.setParameters("a:0:{}");
		    		}
		    		urlids.add(insertCourseUrl(url));
		        }
	        }
    	}
    	return urlids;
	}
	
	private ArrayList<Integer> insertCourseForums(Deploy lfdeploy, HashMap prueba){
		ArrayList<Integer> forumids = new ArrayList<Integer>();
    	for (Iterator it = prueba.keySet().iterator(); it.hasNext();) {
    		// Get the key
	        String tipoModulo = (String) it.next();
	        if (tipoModulo.equals("forum")){
		        ArrayList <Activity> listActMod = (ArrayList <Activity>) ((ArrayList)prueba.get(tipoModulo)).get(0);
		        ArrayList <Contenedor>listConten = (ArrayList <Contenedor>) ((ArrayList)prueba.get(tipoModulo)).get(1);	            
		        //for each module
		        for(int i=0; i<listConten.size();i++){
		        	Contenedor contSec=listConten.get(i);
		        	
	        		String description = "No description has been provided.";
		            for (Activity activity: listActMod){
		            	if(activity.getId().equals(contSec.getIdActivity())){
		            		if (!activity.getDescription().trim().isEmpty()){
		            			description = activity.getDescription();
		            		}
		            		break;
		            	}
		            }
		            
		            CourseForum forum = new CourseForum(null, Integer.parseInt(lfdeploy.getCourse().getId()), "general", contSec.getModName(), description);
		            forum.setIntroformat(0);
		            forum.setAssessed(0);
		            forum.setAssesstimestart(0);
		            forum.setAssesstimefinish(0);
		            forum.setMaxattachments(1);
		    		forumids.add(insertCourseForum(forum));
		        }
	        }
    	}
    	return forumids;
	}
	
	private ArrayList<Integer> insertCourseChats(Deploy lfdeploy, HashMap prueba){
		ArrayList<Integer> chatids = new ArrayList<Integer>();
    	for (Iterator it = prueba.keySet().iterator(); it.hasNext();) {
    		// Get the key
	        String tipoModulo = (String) it.next();
	        if (tipoModulo.equals("chat")){
		        ArrayList <Activity> listActMod = (ArrayList <Activity>) ((ArrayList)prueba.get(tipoModulo)).get(0);
		        ArrayList <Contenedor>listConten = (ArrayList <Contenedor>) ((ArrayList)prueba.get(tipoModulo)).get(1);	            
		        //for each module
		        for(int i=0; i<listConten.size();i++){
		        	Contenedor contSec=listConten.get(i);
		        	
	        		String description = "No description has been provided.";
		            for (Activity activity: listActMod){
		            	if(activity.getId().equals(contSec.getIdActivity())){
		            		if (!activity.getDescription().trim().isEmpty()){
		            			description = activity.getDescription();
		            		}
		            		break;
		            	}
		            }
		            CourseChat chat = new CourseChat(null, Integer.parseInt(lfdeploy.getCourse().getId()), contSec.getModName(), description);
		            chat.setIntroformat(1);
		            chat.setKeepdays(0);
		            chat.setStudentlogs(0);
		            chat.setChattime(0);
		            chat.setSchedule(0);
		            chat.setTimemodified(0);
		    		chatids.add(insertCourseChat(chat));
		        }
	        }
    	}
    	return chatids;
	}
	
	private ArrayList<Integer> insertCourseQuizzes(Deploy lfdeploy, HashMap prueba){
		ArrayList<Integer> quizids = new ArrayList<Integer>();
    	for (Iterator it = prueba.keySet().iterator(); it.hasNext();) {
    		// Get the key
	        String tipoModulo = (String) it.next();
	        if (tipoModulo.equals("quiz")){
		        ArrayList <Activity> listActMod = (ArrayList <Activity>) ((ArrayList)prueba.get(tipoModulo)).get(0);
		        ArrayList <Contenedor>listConten = (ArrayList <Contenedor>) ((ArrayList)prueba.get(tipoModulo)).get(1);	            
		        //for each module
		        for(int i=0; i<listConten.size();i++){
		        	Contenedor contSec=listConten.get(i);
		        	
	        		String description = "No description has been provided.";
		            for (Activity activity: listActMod){
		            	if(activity.getId().equals(contSec.getIdActivity())){
		            		if (!activity.getDescription().trim().isEmpty()){
		            			description = activity.getDescription();
		            		}
		            		break;
		            	}
		            }
		            CourseQuiz quiz = new CourseQuiz(null, Integer.parseInt(lfdeploy.getCourse().getId()), contSec.getModName(), description);
		            quiz.setIntroformat(0);
		            quiz.setTimeopen(0);
		            quiz.setTimeclose(0);
		            quiz.setPreferredbehaviour("");
		            quiz.setAttempts(0);
		            quizids.add(insertCourseQuiz(quiz));
		        }
	        }
    	}
    	return quizids;
	}
	
	private ArrayList<Integer> insertCourseAssignments(Deploy lfdeploy, HashMap prueba){
		ArrayList<Integer> assignmentids = new ArrayList<Integer>();
    	for (Iterator it = prueba.keySet().iterator(); it.hasNext();) {
    		// Get the key
	        String tipoModulo = (String) it.next();
	        if (tipoModulo.equals("assignment")){
		        ArrayList <Activity> listActMod = (ArrayList <Activity>) ((ArrayList)prueba.get(tipoModulo)).get(0);
		        ArrayList <Contenedor>listConten = (ArrayList <Contenedor>) ((ArrayList)prueba.get(tipoModulo)).get(1);	            
		        //for each module
		        for(int i=0; i<listConten.size();i++){
		        	Contenedor contSec=listConten.get(i);
		        	
	        		String description = "No description has been provided.";
		            for (Activity activity: listActMod){
		            	if(activity.getId().equals(contSec.getIdActivity())){
		            		if (!activity.getDescription().trim().isEmpty()){
		            			description = activity.getDescription();
		            		}
		            		break;
		            	}
		            }
		            CourseAssignment assignment = new CourseAssignment(null, Integer.parseInt(lfdeploy.getCourse().getId()), contSec.getModName(), description);
	                assignment.setIntroformat(0);
	                assignment.setAssignmenttype("uploadsingle");
	                assignment.setResubmit(0);
	                assignment.setPreventlate(0);
	                assignment.setEmailteachers(0);
	                assignment.setGrade(100);
	                assignment.setMaxbytes(1048576);
	                assignmentids.add(insertCourseAssignment(assignment));
		        }
	        }
    	}
    	return assignmentids;
	}
	
	private ArrayList<Integer> insertCoursePages(Deploy lfdeploy, HashMap prueba){
		ArrayList<Integer> pageids = new ArrayList<Integer>();
    	for (Iterator it = prueba.keySet().iterator(); it.hasNext();) {
    		// Get the key
	        String tipoModulo = (String) it.next();
	        if (tipoModulo.equals("page")){
		        ArrayList <Activity> listActMod = (ArrayList <Activity>) ((ArrayList)prueba.get(tipoModulo)).get(0);
		        ArrayList <Contenedor>listConten = (ArrayList <Contenedor>) ((ArrayList)prueba.get(tipoModulo)).get(1);	            
		        //for each module
		        for(int i=0; i<listConten.size();i++){
		        	Contenedor contSec=listConten.get(i);
		        	
	        		String description = "No description has been provided.";
		            for (Activity activity: listActMod){
		            	if(activity.getId().equals(contSec.getIdActivity())){
		            		if (!activity.getDescription().trim().isEmpty()){
		            			description = activity.getDescription();
		            		}
		            		break;
		            	}
		            }
		            CoursePage page = new CoursePage(null, Integer.parseInt(lfdeploy.getCourse().getId()), contSec.getModName(), description);
					page.setIntroformat(1);
					page.setContent("");
					page.setContentformat(1);
					page.setDisplay(5);
					page.setDisplayoptions("a:2:{s:12:\"printheading\";s:1:\"1\";s:10:\"printintro\";s:1:\"0\";}");
					pageids.add(insertCoursePage(page));
		        }
	        }
    	}
    	return pageids;
	}
	
	private ArrayList<Integer> insertCourseModules(Deploy lfdeploy, ArrayList<Integer> urlids, ArrayList<Integer> forumids, ArrayList<Integer> chatids, ArrayList<Integer> quizids, ArrayList<Integer> assignmentids, ArrayList<Integer> pageids, ArrayList<Integer> modulesGroupingids){
		ArrayList<Integer> moduleids = new ArrayList<Integer>();
		int numberUrl = 0;
		int numberForum = 0;
		int numberChat = 0;
		int numberQuiz = 0;
		int numberAssignment = 0;
		int numberPage = 0;
		int modIndex = 0;
		HashMap prueba=generarHashmap(lfdeploy);
		prueba.remove("numSection");
		prueba.remove("auxiliar");
		ArrayList<CourseSection> cs = getCourseSections(lfdeploy.getCourse());
		
		Integer startingSection = 1;//Default starting section is 1
		String lfsection = lfdeploy.getFieldFromDeployData(getStartingSectionField());
		if(lfsection!=null && lfsection.length()>0){
			startingSection = Integer.parseInt(lfsection);
		}
		ArrayList<Module> modules = getModules();
    	for (Iterator it = prueba.keySet().iterator(); it.hasNext();) {
    		// Get the key
	        String tipoModulo = (String) it.next();
	        if (tipoModulo.equals("resource")){
		        ArrayList <Activity> listActMod = (ArrayList <Activity>) ((ArrayList)prueba.get(tipoModulo)).get(0);
		        ArrayList <Contenedor>listConten = (ArrayList <Contenedor>) ((ArrayList)prueba.get(tipoModulo)).get(1);	            
		        //for each module
		        for(int i=0; i<listConten.size();i++){
		        	Contenedor contSec=listConten.get(i);
		        	int sectionid = 0;
		        	for (int j=0; j<cs.size(); j++){
		        		if (cs.get(j).getSection().intValue()==(contSec.getNumSection() + (startingSection-1))){
		        			sectionid = cs.get(j).getId();
		        			break;
		        		}
		        	}		        	
		        	Module mod = findModuleByName(modules, "url"); //do not change the value from url to resource. It is OK
		        	CourseModule cm = new CourseModule(null, Integer.parseInt(lfdeploy.getCourse().getId()), mod.getId(), urlids.get(numberUrl), sectionid);
		        	cm.setVisible(1);
		        	cm.setGroupmode(0);
		        	cm.setGroupingid(modulesGroupingids.get(modIndex));//change it
		        	cm.setGroupmembersonly(1);
		        	numberUrl++;
		        	modIndex++;
		        	moduleids.add(insertCourseModule(cm));
		        }
	        }else if (tipoModulo.equals("forum")){		        
	        	ArrayList <Activity> listActMod = (ArrayList <Activity>) ((ArrayList)prueba.get(tipoModulo)).get(0);
		        ArrayList <Contenedor>listConten = (ArrayList <Contenedor>) ((ArrayList)prueba.get(tipoModulo)).get(1);	            
		        //for each module
		        for(int i=0; i<listConten.size();i++){
		        	Contenedor contSec=listConten.get(i);
		        	int sectionid = 0;
		        	for (int j=0; j<cs.size(); j++){
		        		if (cs.get(j).getSection().intValue()==(contSec.getNumSection() + (startingSection-1))){
		        			sectionid = cs.get(j).getId();
		        			break;
		        		}
		        	}
		        	Module mod = findModuleByName(modules, "forum");
		        	CourseModule cm = new CourseModule(null, Integer.parseInt(lfdeploy.getCourse().getId()), mod.getId(), forumids.get(numberForum), sectionid);
		        	cm.setVisible(1);
		        	cm.setGroupmode(0);
		        	cm.setGroupingid(modulesGroupingids.get(modIndex));//change it
		        	cm.setGroupmembersonly(1);
		        	numberForum++;
		        	modIndex++;
		        	moduleids.add(insertCourseModule(cm));
		        }
	        }else if (tipoModulo.equals("chat")){		        
	        	ArrayList <Activity> listActMod = (ArrayList <Activity>) ((ArrayList)prueba.get(tipoModulo)).get(0);
		        ArrayList <Contenedor>listConten = (ArrayList <Contenedor>) ((ArrayList)prueba.get(tipoModulo)).get(1);	            
		        //for each module
		        for(int i=0; i<listConten.size();i++){
		        	Contenedor contSec=listConten.get(i);
		        	int sectionid = 0;
		        	for (int j=0; j<cs.size(); j++){
		        		if (cs.get(j).getSection().intValue()==(contSec.getNumSection() + (startingSection-1))){
		        			sectionid = cs.get(j).getId();
		        			break;
		        		}
		        	}
		        	Module mod = findModuleByName(modules, "chat");
		        	CourseModule cm = new CourseModule(null, Integer.parseInt(lfdeploy.getCourse().getId()), mod.getId(), chatids.get(numberChat), sectionid);
		        	cm.setVisible(1);
		        	cm.setGroupmode(0);
		        	cm.setGroupingid(modulesGroupingids.get(modIndex));//change it
		        	cm.setGroupmembersonly(1);
		        	numberChat++;
		        	modIndex++;
		        	moduleids.add(insertCourseModule(cm));
		        }
	        }else if (tipoModulo.equals("quiz")){		        
	        	ArrayList <Activity> listActMod = (ArrayList <Activity>) ((ArrayList)prueba.get(tipoModulo)).get(0);
		        ArrayList <Contenedor>listConten = (ArrayList <Contenedor>) ((ArrayList)prueba.get(tipoModulo)).get(1);	            
		        //for each module
		        for(int i=0; i<listConten.size();i++){
		        	Contenedor contSec=listConten.get(i);
		        	int sectionid = 0;
		        	for (int j=0; j<cs.size(); j++){
		        		if (cs.get(j).getSection().intValue()==(contSec.getNumSection() + (startingSection-1))){
		        			sectionid = cs.get(j).getId();
		        			break;
		        		}
		        	}
		        	Module mod = findModuleByName(modules, "quiz");
		        	CourseModule cm = new CourseModule(null, Integer.parseInt(lfdeploy.getCourse().getId()), mod.getId(), quizids.get(numberQuiz), sectionid);
		        	cm.setVisible(1);
		        	cm.setGroupmode(0);
		        	cm.setGroupingid(modulesGroupingids.get(modIndex));//change it
		        	cm.setGroupmembersonly(1);
		        	numberQuiz++;
		        	modIndex++;
		        	moduleids.add(insertCourseModule(cm));
		        }
	        }
	        else if (tipoModulo.equals("assignment")){		        
	        	ArrayList <Activity> listActMod = (ArrayList <Activity>) ((ArrayList)prueba.get(tipoModulo)).get(0);
		        ArrayList <Contenedor>listConten = (ArrayList <Contenedor>) ((ArrayList)prueba.get(tipoModulo)).get(1);	            
		        //for each module
		        for(int i=0; i<listConten.size();i++){
		        	Contenedor contSec=listConten.get(i);
		        	int sectionid = 0;
		        	for (int j=0; j<cs.size(); j++){
		        		if (cs.get(j).getSection().intValue()==(contSec.getNumSection() + (startingSection-1))){
		        			sectionid = cs.get(j).getId();
		        			break;
		        		}
		        	}
		        	Module mod = findModuleByName(modules, "assignment");
		        	CourseModule cm = new CourseModule(null, Integer.parseInt(lfdeploy.getCourse().getId()), mod.getId(), assignmentids.get(numberAssignment), sectionid);
		        	cm.setVisible(1);
		        	cm.setGroupmode(0);
		        	cm.setGroupingid(modulesGroupingids.get(modIndex));//change it
		        	cm.setGroupmembersonly(1);
		        	numberAssignment++;
		        	modIndex++;
		        	moduleids.add(insertCourseModule(cm));
		        }
	        }
	        else if (tipoModulo.equals("page")){		        
	        	ArrayList <Activity> listActMod = (ArrayList <Activity>) ((ArrayList)prueba.get(tipoModulo)).get(0);
		        ArrayList <Contenedor>listConten = (ArrayList <Contenedor>) ((ArrayList)prueba.get(tipoModulo)).get(1);	            
		        //for each module
		        for(int i=0; i<listConten.size();i++){
		        	Contenedor contSec=listConten.get(i);
		        	int sectionid = 0;
		        	for (int j=0; j<cs.size(); j++){
		        		if (cs.get(j).getSection().intValue()==(contSec.getNumSection() + (startingSection-1))){
		        			sectionid = cs.get(j).getId();
		        			break;
		        		}
		        	}
		        	Module mod = findModuleByName(modules, "page");
		        	CourseModule cm = new CourseModule(null, Integer.parseInt(lfdeploy.getCourse().getId()), mod.getId(), pageids.get(numberPage), sectionid);
		        	cm.setVisible(1);
		        	cm.setGroupmode(0);
		        	cm.setGroupingid(modulesGroupingids.get(modIndex));//change it
		        	cm.setGroupmembersonly(1);
		        	numberPage++;
		        	modIndex++;
		        	moduleids.add(insertCourseModule(cm));
		        }
	        }
	        else{
	        	ArrayList <Contenedor>listConten = (ArrayList <Contenedor>) ((ArrayList)prueba.get(tipoModulo)).get(1);
	        	modIndex = modIndex + listConten.size();
	        }
    	}
    	return moduleids;
	}
	
	private ArrayList<Integer> getModulesGroupingids(Deploy lfdeploy, ArrayList<CourseGrouping> groupings){
        ArrayList<Group> groups = lfdeploy.getGroups();
		ArrayList<Integer> modulesGroupingids = new ArrayList<Integer>(); //A grouping is set for each module
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
	            //Get the group id and then the grouping id for the module
	            for (int j=0; j< groups.size(); j++){
	            	if (groups.get(j).getId().equals(contSec.getGroupId())){
	            		for (int k=0; k < groupings.size(); k++){
	            			//Check if the grouping contains that group by the username. Maybe, it should be done in a different way
	            			if (groups.get(j).getName().equals(groupings.get(k).getName())){
	    	            		modulesGroupingids.add(groupings.get(k).getId());
			            		break;
	            			}
	            		}
	            	}
	            }
	        }
    	}
    	return modulesGroupingids;
	}
	
	private Module findModuleByName(ArrayList<Module> modules, String name){
		for (Module m: modules){
			if (m.getName().equals(name)){
				return m;
			}
		}
		return null;		
	}

	@Override
	public boolean canBeDeployed(String baseUri, Deploy lfdeploy){
		HashMap<String,String> courses = null;
		Boolean auth = moodleAuth(baseUri, moodleUser, moodlePassword);
		if (auth == false){
			return false;
		}
		courses = getCourses(baseUri, lfdeploy.getLearningEnvironment().getCreduser());
		if (courses!=null && courses.get(lfdeploy.getCourse().getId())!=null){
			return true;
		}
		return false;
	}
	
	//This returns the toolInstanceId
	private String getTrimmedId(String id){
		if(!id.startsWith("http://") || id.indexOf("/instance/")==-1){
			//This is not an expected URL!
			return id;
		}else{
			//This is an expected URL
			return id.substring(id.indexOf("/instance/")+10);
		}
		
	}

}
