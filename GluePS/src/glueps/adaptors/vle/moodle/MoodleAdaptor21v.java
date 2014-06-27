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
import glueps.adaptors.vle.moodle.model.moodle2.Module;
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
import java.util.HashMap;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class MoodleAdaptor21v extends MoodleAdaptor{
	
	private String wstoken;	
	private String moodleUrl;
	private String moodleUser;
	private String moodlePassword;
	 
	public MoodleAdaptor21v() {
		super();
	}


public MoodleAdaptor21v(String base, String template,GLUEPSManagerApplication applicationRest, String moodleUrl, String moodleUser, String moodlePassword, String wstoken) {
	super();
	//This is the pathname to the final zip file. This is no longer set here, since it needs the deployId to construct the path to the file
	//ZIPNAME = zipname;
	//This is the base directory where all uploaded files are
	BASE = base;
	//This the pathname to the moodle xml template
	TEMPLATE = template;
	//This is the Restlet Application of GLUE!-PS, to get the configuration parameters like directory paths, etc
	app=applicationRest;
	
	this.moodleUrl = moodleUrl;
	this.moodleUser = moodleUser;
	this.moodlePassword = moodlePassword;
	this.wstoken = wstoken;
}


public MoodleAdaptor21v(String base, String template,GLUEPSManagerApplication applicationRest, String modelPackage, String backupXmlFilename,
		String tmpDir, String moodleUrl, String moodleUser, String moodlePassword, String wstoken) {
	
	this(base, template, applicationRest, moodleUrl, moodleUser, moodlePassword, wstoken);

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


@Override
public HashMap<String,Participant> getUsers(String moodleBaseUri){
	
	String qapiUrl = moodleBaseUri+"webservice/rest/server.php";
	String wsfunction = "gws_user_get_all_users";

	HashMap<String,Participant> vleUsers = null;
	
	try {
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		String response = doGetFromURL(qapiUrl);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(response));
		Document doc = db.parse(is);
		//optional, but recommended
		doc.getDocumentElement().normalize();
		//Every user is contained in a SINGLE tag
		NodeList userNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
		for (int i = 0; i < userNodeList.getLength(); i++){
			Node userNode = userNodeList.item(i);
			//Each user has a set of nodes with the values
			NodeList keyNodeList = userNode.getChildNodes();
			String id = "";
			String username = "";
			String firstname = "";
			String lastname = "";
			String email = "";
			String firstaccess = "";
			String auth = "";
			for (int j = 0; j < keyNodeList.getLength(); j++){
				Node keyNode = keyNodeList.item(j);
				if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
					Element keyElement = (Element) keyNode;
					String keyName = keyElement.getAttribute("name");
					if (keyName.equals("id")){
						id = keyElement.getChildNodes().item(0).getTextContent();
					}else if(keyName.equals("username")){
						username = keyElement.getChildNodes().item(0).getTextContent();
					}else if(keyName.equals("firstname")){
						firstname = keyElement.getChildNodes().item(0).getTextContent();
					}else if(keyName.equals("lastname")){
						lastname = keyElement.getChildNodes().item(0).getTextContent();
					}else if(keyName.equals("email")){
						email = keyElement.getChildNodes().item(0).getTextContent();
					}else if(keyName.equals("firstaccess")){
						firstaccess = keyElement.getChildNodes().item(0).getTextContent();
					}else if(keyName.equals("auth")){
						auth = keyElement.getChildNodes().item(0).getTextContent();
					}
					
				}
			}
			if (!id.isEmpty() && !username.isEmpty()){
				if (vleUsers == null){
					vleUsers = new HashMap<String, Participant>();
				}//Add the participant to the hash map
				Participant part = new Participant(id,username, null,id + Participant.USER_PARAMETER_SEPARATOR + username
						+ Participant.USER_PARAMETER_SEPARATOR + email + Participant.USER_PARAMETER_SEPARATOR + firstaccess + Participant.USER_PARAMETER_SEPARATOR,false);
				//The iCollage adaptor references users by the username, not the DB id
				vleUsers.put(part.getName(), part);
			}
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	return vleUsers;
}


	@Override
	public HashMap<String, String> getCourses(String moodleBaseUri){
		
		String qapiUrl = moodleBaseUri+"webservice/rest/server.php";
		String wsfunction = "gws_course_get_all_courses";

		HashMap<String,String> vleCourses = null;
		
		try {
			qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//Every course is contained in a SINGLE tag
			NodeList courseNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			for (int i = 0; i < courseNodeList.getLength(); i++){
				Node courseNode = courseNodeList.item(i);
				//Each course has a set of nodes with the values
				NodeList keyNodeList = courseNode.getChildNodes();
				String value = "";
				String fullname = "";
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("id")){
							value = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("fullname")){
							fullname = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
				if (!value.isEmpty() && !fullname.isEmpty()){
					if (vleCourses == null){
						vleCourses = new HashMap<String, String>();
					}//Add the course to the hash map
					vleCourses.put(value, fullname);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return vleCourses;
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
		Boolean auth = moodleAuth(moodleBaseUri, moodleUser, moodlePassword);
		if (auth==false){
			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The user is not allowed to get the courses from the VLE. Please, check your credentials for the VLE");
		}
		String qapiUrl = moodleBaseUri+"webservice/rest/server.php";
		String wsfunction = "gws_course_get_courses_username";
		String roleid = "3"; //the admin role id is 1 and the teacher role id is 3

		HashMap<String,String> vleCourses = null;
		
		try {
			qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction + "&username=" + URLEncoder.encode(username, "UTF-8") + "&roleid=" + roleid;
			String response = doGetFromURL(qapiUrl);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//Every course is contained in a SINGLE tag
			NodeList courseNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			for (int i = 0; i < courseNodeList.getLength(); i++){
				Node courseNode = courseNodeList.item(i);
				//Each course has a set of nodes with the values
				NodeList keyNodeList = courseNode.getChildNodes();
				String id = "";
				String fullname = "";
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("id")){
							id = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("fullname")){
							fullname = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
				if (!id.isEmpty() && !fullname.isEmpty()){
					if (vleCourses == null){
						vleCourses = new HashMap<String, String>();
					}//Add the course to the hash map
					vleCourses.put(id, fullname);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if (vleCourses!=null){
			System.out.println("The courses are: "+vleCourses.toString());
		}

		return vleCourses;
	}

	@Override
	public HashMap<String, Participant> getCourseUsers(String moodleBaseUri, String courseId) {
		
		String qapiUrl = moodleBaseUri+"webservice/rest/server.php";
		String wsfunction = "gws_user_get_users_courseid";
	
		HashMap<String,Participant> courseUsers = null;
		
		try {
			qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction + "&courseid=" + courseId;
			String response = doGetFromURL(qapiUrl);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The MULTIPLE node has the list of SINGLE child nodes
			NodeList userNodeList = doc.getDocumentElement().getChildNodes().item(1).getChildNodes();
			for (int i = 0; i < userNodeList.getLength(); i++){
				Node userNode = userNodeList.item(i);
				if (userNode.getNodeType() == Node.ELEMENT_NODE && userNode.getNodeName().equals("SINGLE")){
					
					String userId = "";
					String username = "";
					String firstname = "";
					String lastname = "";
					String email = "";
					String firstaccess = "";
					String auth = "";
					
					NodeList keyNodeList = userNode.getChildNodes();
					for (int j = 0; j < keyNodeList.getLength(); j++){
						Node keyNode = keyNodeList.item(j);
						if (keyNode.getNodeType() == Node.ELEMENT_NODE && keyNode.getNodeName().equals("KEY")) {						 
							Element keyElement = (Element) keyNode;
							String keyName = keyElement.getAttribute("name");
							
							if (keyName.equals("user")){
								//Each user has a set of nodes with the values
								Node userNodeItem = keyElement.getChildNodes().item(0);
								if (userNodeItem.getNodeType() == Node.ELEMENT_NODE && userNodeItem.getNodeName().equals("SINGLE")){
									NodeList userProperties = userNodeItem.getChildNodes();
									for (int k = 0; k < userProperties.getLength(); k++){
										Node property = userProperties.item(k);
										if (property.getNodeType() == Node.ELEMENT_NODE && property.getNodeName().equals("KEY")) {						 
											Element keyElementProp = (Element) property;
											String keyNameProp = keyElementProp.getAttribute("name");
											if (keyNameProp.equals("id")){
												userId = keyElementProp.getChildNodes().item(0).getTextContent();
											}else if(keyNameProp.equals("username")){
												username = keyElementProp.getChildNodes().item(0).getTextContent();
											}else if(keyNameProp.equals("firstname")){
												firstname = keyElementProp.getChildNodes().item(0).getTextContent();
											}else if(keyNameProp.equals("lastname")){
												lastname = keyElementProp.getChildNodes().item(0).getTextContent();
											}else if(keyNameProp.equals("email")){
												email = keyElementProp.getChildNodes().item(0).getTextContent();
											}else if(keyNameProp.equals("firstaccess")){
												firstaccess = keyElementProp.getChildNodes().item(0).getTextContent();
											}else if(keyNameProp.equals("auth")){
												auth = keyElementProp.getChildNodes().item(0).getTextContent();
											}
											
										}
									}
									
									if (!userId.isEmpty() && !username.isEmpty()){
										if (courseUsers == null){
											courseUsers = new HashMap<String, Participant>();
										}//Add the participant to the hash map
										Participant part = new Participant(userId,username, null,userId + Participant.USER_PARAMETER_SEPARATOR + username
												+ Participant.USER_PARAMETER_SEPARATOR + email + Participant.USER_PARAMETER_SEPARATOR + firstaccess + Participant.USER_PARAMETER_SEPARATOR,false);
										//The iCollage adaptor references users by the username, not the DB id
										courseUsers.put(part.getName(), part);
									}
								}							
							}else if (keyName.equals("role")){
								Node multipleNodeItem = keyElement.getChildNodes().item(0);
								if (multipleNodeItem.getNodeType() == Node.ELEMENT_NODE && multipleNodeItem.getNodeName().equals("MULTIPLE")){
									NodeList roleList = multipleNodeItem.getChildNodes();
									//for each role
									for (int k = 0; k < roleList.getLength(); k++){
										Node roleNodeItem = roleList.item(k);
										if (roleNodeItem.getNodeType() == Node.ELEMENT_NODE && roleNodeItem.getNodeName().equals("SINGLE")){
											//Each role has a set of nodes with the values
											NodeList keyNodeRoleList = roleNodeItem.getChildNodes();
											String roleId = "";
											String name = "";
											String shortname = "";
											String description = "";
											String sortorder = "";
											//for each role property
											for (int l = 0; l < keyNodeRoleList.getLength(); l++){
												Node keyRoleNode = keyNodeRoleList.item(l);
												if (keyRoleNode.getNodeType() == Node.ELEMENT_NODE && keyNode.getNodeName().equals("KEY")) {						 
													Element keyRoleElement = (Element) keyRoleNode;
													String keyNameProp = keyRoleElement.getAttribute("name");
													if (keyNameProp.equals("id")){
														roleId = keyRoleElement.getChildNodes().item(0).getTextContent();
													}else if(keyNameProp.equals("name")){
														name = keyRoleElement.getChildNodes().item(0).getTextContent();
													}else if(keyNameProp.equals("shortname")){
														shortname = keyRoleElement.getChildNodes().item(0).getTextContent();
													}else if(keyNameProp.equals("description")){
														description = keyRoleElement.getChildNodes().item(0).getTextContent();
													}else if(keyNameProp.equals("sortorder")){
														sortorder = keyRoleElement.getChildNodes().item(0).getTextContent();
													}
												}
											}
											//Staff roles in moodle are: teacher, editingteacher, manager, coursecreator
											//Take into account that the admin role has been renamed to manager role in Moodle 2
											boolean staff = (shortname.equals("editingteacher")||shortname.equals("manager")||shortname.equals("teacher")||shortname.equals("coursecreator"));
											
											Participant p = courseUsers.get(username);
											//System.out.println("Retrieved participant: "+p.toString());
											
											//Before putting the participant, we check that we are not downgrading teachers to students
											if(p!=null && !staff);//if we are introducing a student but the user already exists, there is no need to introduce it
											else{
												p.setStaff(staff);
												//courseUsers.put(username, p);
											}
										}
									}
								}	
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return courseUsers;
	
	}

	@Override
	public HashMap<String, Group> getCourseGroups(String moodleBaseUri,String courseId) {
		
		String qapiUrl = moodleBaseUri+"webservice/rest/server.php";
		String wsfunction = "gws_group_get_users_courseid";
	
		HashMap<String,Group> courseGroups = null;
		
		try {
			qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction + "&courseid=" + courseId;
			String response = doGetFromURL(qapiUrl);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The MULTIPLE node has the list of SINGLE child nodes
			NodeList groupNodeList = doc.getDocumentElement().getChildNodes().item(1).getChildNodes();
			for (int i = 0; i < groupNodeList.getLength(); i++){
				Node groupNode = groupNodeList.item(i);
				if (groupNode.getNodeType() == Node.ELEMENT_NODE && groupNode.getNodeName().equals("SINGLE")){
					
					String groupId = "";
					String courseid = "";
					String name = "";
					String description = "";
					
					//Each group has a set of nodes with the values
					NodeList keyNodeList = groupNode.getChildNodes();
					for (int j = 0; j < keyNodeList.getLength(); j++){
						Node keyNode = keyNodeList.item(j);
						if (keyNode.getNodeType() == Node.ELEMENT_NODE && keyNode.getNodeName().equals("KEY")) {						 
							Element keyElement = (Element) keyNode;
							String keyName = keyElement.getAttribute("name");
							if (keyName.equals("group")){
								//Each group has a set of nodes with the values
								Node groupNodeItem = keyElement.getChildNodes().item(0);
								if (groupNodeItem.getNodeType() == Node.ELEMENT_NODE && groupNodeItem.getNodeName().equals("SINGLE")){
									NodeList groupProperties = groupNodeItem.getChildNodes();
									for (int k = 0; k < groupProperties.getLength(); k++){
										Node property = groupProperties.item(k);
										if (property.getNodeType() == Node.ELEMENT_NODE && property.getNodeName().equals("KEY")) {						 
											Element keyElementProp = (Element) property;
											String keyNameProp = keyElementProp.getAttribute("name");
											if (keyNameProp.equals("id")){
												groupId = keyElementProp.getChildNodes().item(0).getTextContent();
											}else if(keyNameProp.equals("courseid")){
												courseid = keyElementProp.getChildNodes().item(0).getTextContent();
											}else if(keyNameProp.equals("name")){
												name = keyElementProp.getChildNodes().item(0).getTextContent();
											}else if(keyNameProp.equals("description")){
												description = keyElementProp.getChildNodes().item(0).getTextContent();
											}											
										}
									}
									
									if (!groupId.isEmpty()){
										if (courseGroups == null){
											courseGroups = new HashMap<String, Group>();
										}
										//Add the group to the hash map	with an empty list of users									
										Group group = new Group(groupId, name, null, new ArrayList<String>());
										courseGroups.put(groupId, group);
									}
								}	
							}else if(keyName.equals("user")){
								Node multipleNodeItem = keyElement.getChildNodes().item(0);
								if (multipleNodeItem.getNodeType() == Node.ELEMENT_NODE && multipleNodeItem.getNodeName().equals("MULTIPLE")){
									NodeList userList = multipleNodeItem.getChildNodes();
									//for each user
									for (int k = 0; k < userList.getLength(); k++){										
										String userId = "";
										String username = "";
										String firstname = "";
										String lastname = "";
										String email = "";
										String firstaccess = "";
										String auth = "";
										
										Node userNodeItem = userList.item(k);
										if (userNodeItem.getNodeType() == Node.ELEMENT_NODE && userNodeItem.getNodeName().equals("SINGLE")){
											NodeList userProperties = userNodeItem.getChildNodes();
											for (int l = 0; l < userProperties.getLength(); l++){
												Node property = userProperties.item(l);
												if (property.getNodeType() == Node.ELEMENT_NODE && property.getNodeName().equals("KEY")) {						 
													Element keyElementProp = (Element) property;
													String keyNameProp = keyElementProp.getAttribute("name");
													if (keyNameProp.equals("id")){
														userId = keyElementProp.getChildNodes().item(0).getTextContent();
													}else if(keyNameProp.equals("username")){
														username = keyElementProp.getChildNodes().item(0).getTextContent();
													}else if(keyNameProp.equals("firstname")){
														firstname = keyElementProp.getChildNodes().item(0).getTextContent();
													}else if(keyNameProp.equals("lastname")){
														lastname = keyElementProp.getChildNodes().item(0).getTextContent();
													}else if(keyNameProp.equals("email")){
														email = keyElementProp.getChildNodes().item(0).getTextContent();
													}else if(keyNameProp.equals("firstaccess")){
														firstaccess = keyElementProp.getChildNodes().item(0).getTextContent();
													}else if(keyNameProp.equals("auth")){
														auth = keyElementProp.getChildNodes().item(0).getTextContent();
													}													
												}
											}
										}
										if (!userId.isEmpty() && !username.isEmpty()){
											Group group = courseGroups.get(groupId);
											//The group should exist!
											if (group != null){
												ArrayList<String> participants = group.getParticipantIds();
												if(!participants.contains(userId)){
													participants.add(userId);
													//group.setParticipantIds(participants);
												}
											}
										}
									}
								}	
							}
						}
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return courseGroups;	
	}
	
	/**
	 * Delete a group from a course
	 * @param groupid The group id
	 */
	public void deleteCourseGroups(ArrayList<Integer> groupids){
		if (groupids.size() == 0){
			return;
		}
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "moodle_group_delete_groups";
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		for (int i = 0; i < groupids.size(); i++){
			qapiUrl += "&groupids[" + i + "]=" + groupids.get(i);
		}
		try {
			String response = doGetFromURL(qapiUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Check if the user's credentials in Moodle are right
	 * @param moodleBaseUri The Moodle URL
	 * @param user The username in moodle
	 * @param pass The password of the user in Moodle
	 * @return The credentials are right or not
	 */
	public boolean moodleAuth(String moodleBaseUri, String user, String pass){
		String qapiUrl = moodleBaseUri+"webservice/rest/server.php";
		String wsfunction = "gws_user_check_user_credentials";
		
		try {
			qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction + "&username=" + URLEncoder.encode(user, "UTF-8") + "&password=" + URLEncoder.encode(pass, "UTF-8");
			String response = doGetFromURL(qapiUrl);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The MULTIPLE node has the list of SINGLE child nodes
			NodeList userNodeList = doc.getDocumentElement().getChildNodes().item(1).getChildNodes();
			for (int i = 0; i < userNodeList.getLength(); i++){
				Node userNode = userNodeList.item(i);
				if (userNode.getNodeType() == Node.ELEMENT_NODE && userNode.getNodeName().equals("SINGLE")){
					//Each user has a set of nodes with the values
					NodeList keyNodeList = userNode.getChildNodes();
					for (int j = 0; j < keyNodeList.getLength(); j++){
						Node keyNode = keyNodeList.item(j);
						if (keyNode.getNodeType() == Node.ELEMENT_NODE && keyNode.getNodeName().equals("KEY")) {						 
							Element keyElement = (Element) keyNode;
							String keyName = keyElement.getAttribute("name");
							if (keyName.equals("username")){
								//The user information has been returned, so the credentials are right
								String username = keyElement.getChildNodes().item(0).getTextContent();
								if (username.equals(user)){
									return true;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	    return false;
	}
	
	
	/**
	 * Add the specified enrol method to Moodle
	 * @param c The course where the enrol method is going to be added
	 * @param enrol The name of the Moodle's enrol method name
	 */
	public Integer addEnrolMethod(Course c, String enrol){
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_enrol_set_enrol"; //The name of the webservice function in Moodle 2.0 and 2.1
		String courseid = c.getId();
		Integer enrolid = null;
		try {
			qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction + "&enrol=" + URLEncoder.encode(enrol, "UTF-8") + "&courseid=" + courseid;
			String response = doGetFromURL(qapiUrl);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			Node userNode = doc.getDocumentElement().getChildNodes().item(1);
			if (userNode.getNodeType() == Node.ELEMENT_NODE && userNode.getNodeName().equals("SINGLE")){
				//Each user has a set of nodes with the values
				NodeList keyNodeList = userNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE && keyNode.getNodeName().equals("KEY")){
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("enrolid")){	
							enrolid = Integer.parseInt(keyElement.getChildNodes().item(0).getTextContent());
							return enrolid;
						}
					}
				}
			}
		} catch (Exception e) {
			return enrolid;
		}
		return enrolid;
	}
	
	public ArrayList<CourseEnrol> getCourseEnrols(Course c){
		ArrayList<CourseEnrol> enrols = new ArrayList<CourseEnrol>();
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_enrol_get_enrols_course";
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction + "&courseid=" + c.getId();
		
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//Every enrol is contained in a SINGLE tag
			NodeList enrolNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			for (int i = 0; i < enrolNodeList.getLength(); i++){
				Node enrolNode = enrolNodeList.item(i);
				//Each Enrol has a set of nodes with the values
				NodeList keyNodeList = enrolNode.getChildNodes();
			    
				String id="", enrol="", status="", courseid="", sortorder="", timecreated="", timemodified="";
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("id")){
							id = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("enrol")){
							enrol = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("status")){
							status = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("courseid")){
							courseid = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("sortorder")){
							sortorder = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("timecreated")){
							timecreated = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("timemodified")){
							timemodified = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
				CourseEnrol courseEnrol = new CourseEnrol(Integer.parseInt(id), enrol, Integer.parseInt(status), Integer.parseInt(courseid));
				courseEnrol.setSortorder(Integer.parseInt(sortorder));
				courseEnrol.setTimecreated(Integer.parseInt(timecreated));
				courseEnrol.setTimemodified(Integer.parseInt(timemodified));
				enrols.add(courseEnrol);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return enrols;
	}
	
	/**
	 * Delete the information of a enrol type in a course
	 * @param enrolid The enrol type id
	 * @param courseid The course id
	 * @return The enrol type has been deleted correctly or not
	 */
	public Boolean deleteCourseEnrol(Integer enrolid, Integer courseid){
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_delete_enrol_course";
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		qapiUrl = qapiUrl + "&enrolid=" + enrolid + "&courseid=" + courseid;
		String success = "";
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The enrol type is contained in a SINGLE tag
			NodeList enrolNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			if (enrolNodeList.getLength()==1){
				Node enrolNode = enrolNodeList.item(0);
				//Each enrol has a set of nodes with the values
				NodeList keyNodeList = enrolNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("success")){
							success = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!success.isEmpty() && Integer.parseInt(success)==1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Enrol manually the users in the course
	 * @param c The course where the users are going to be enrolled
	 * @param participants The users to be enrolled in the course
	 */
	public void enrolUsers(Course c, ArrayList<Participant> participants) {
		if (participants.size() == 0){
			return;
		}
 		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "moodle_enrol_manual_enrol_users"; //The name of the webservice function in Moodle 2.0 and 2.1
		String userid, roleid, courseid;
		courseid = c.getId();
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
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
	        		roleid = "3"; //teacher role in Moodle 2.1		
	        	}
	        	else{
	        		roleid = "5"; //student role in Moodle 2.1
	        	}
        	}
        	try {
        		qapiUrl += "&enrolments[" + i + "][roleid]=" + URLEncoder.encode(roleid, "UTF-8");
        		qapiUrl += "&enrolments[" + i + "][userid]=" + URLEncoder.encode(userid, "UTF-8");
        		qapiUrl += "&enrolments[" + i + "][courseid]=" + URLEncoder.encode(courseid, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		try {
			String response = doGetFromURL(qapiUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Disenroll manually a user from the course
	 * @param c The course from which the user is going to be disenrolled
	 * @param participants The users to be enrolled in the course
	 * @param enrolid The id of the enrol
	 */
	public boolean disenrollUser(Course c, Participant participant, Integer enrolid) {
 		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_enrol_disenroll_course_user"; 
		String courseid;
		courseid = c.getId();
		String success = "";
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction + "&userid=" + participant.getId() + "&courseid=" + courseid + "&enrolid=" + enrolid;
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The user is contained in a SINGLE tag
			NodeList userNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			if (userNodeList.getLength()==1){
				Node userNode =userNodeList.item(0);
				//Each module has a set of nodes with the values
				NodeList keyNodeList = userNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("success")){
							success = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!success.isEmpty() && Integer.parseInt(success)==1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Add the groups to a Moodle's course
	 * @param c The course where the groups are to be added
	 * @param groups The groups to be added
	 * @return the ids in Moodle of the created groups
	 */
	public ArrayList<Integer> createGroups(Course c, ArrayList<Group> groups){
		ArrayList<Integer> createdGroupIds = new ArrayList<Integer>();
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "moodle_group_create_groups"; //The name of the webservice function in Moodle 2.0 and 2.1
		String courseid = c.getId();
		String name, description;
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		for (int i = 0; i < groups.size(); i++){
			Group g = groups.get(i);
			name = g.getName();
			description = "";
			boolean exists = false;
			//Check that there isn't another group with such a name
			for (int j = 0; j < groups.size(); j++){
				if (i!=j && name.equals(groups.get(j).getName())){
					exists = true;
				}
			}
			if (!exists){
				try {
					qapiUrl += "&groups[" + i + "][courseid]=" + URLEncoder.encode(courseid, "UTF-8");
					qapiUrl += "&groups[" + i + "][name]=" + URLEncoder.encode(name, "UTF-8");
					qapiUrl += "&groups[" + i + "][description]=" + URLEncoder.encode(description, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			String response = doGetFromURL(qapiUrl);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The MULTIPLE node has the list of SINGLE child nodes
			NodeList groupNodeList = doc.getDocumentElement().getChildNodes().item(1).getChildNodes();
			int groupNumber = 0;
			for (int i = 0; i < groupNodeList.getLength(); i++){
				Node groupNode = groupNodeList.item(i);
				if (groupNode.getNodeType() == Node.ELEMENT_NODE && groupNode.getNodeName().equals("SINGLE")){
					
					String groupId = "";
					String groupName = "";
					//Each group has a set of nodes with the values
					NodeList keyNodeList = groupNode.getChildNodes();
					for (int j = 0; j < keyNodeList.getLength(); j++){
						Node keyNode = keyNodeList.item(j);
						if (keyNode.getNodeType() == Node.ELEMENT_NODE && keyNode.getNodeName().equals("KEY")) {						 
							Element keyElement = (Element) keyNode;
							String keyName = keyElement.getAttribute("name");
							if (keyName.equals("id")){
								//Take into account that we have created a new! group in Moodle, so it has a different id from the group in Glueps
							    groupId = keyElement.getChildNodes().item(0).getTextContent();
							}else if (keyName.equals("name")){
								groupName = keyElement.getChildNodes().item(0).getTextContent();
								Group group = groups.get(groupNumber);
								groupNumber++;
								//Make sure that the group name is the same
								if (!groupId.equals("")&& group.getName().equals(groupName)){
									createdGroupIds.add(Integer.parseInt(groupId));
								}								
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return createdGroupIds;
	}
	
	/**
	 * Add the users to a group
	 * @param groupId The id of the group in Moodle
	 * @param partIds The id of the users to be added to the group
	 */
	public void addGroupMembers(Integer groupId, ArrayList<String> partIds){
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "moodle_group_add_groupmembers"; //The name of the webservice function in Moodle 2.0 and 2.1
		String userid;
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		for (int i = 0; i < partIds.size(); i++){
			userid = partIds.get(i);
			try {
				qapiUrl += "&members[" + i + "][groupid]=" + groupId;
				qapiUrl += "&members[" + i + "][userid]=" + URLEncoder.encode(userid, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		try {
			String response = doGetFromURL(qapiUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the groupings in a Moodle's course. A grouping is created for each group with the same name
	 * @param groups The groups in the course for which the groupings are going to be created
	 * @return the ids of the created groupings
	 */
	public ArrayList<Integer> createGroupings(Course c, ArrayList<Group> groups){
		ArrayList<Integer> createdGroupingIds = new ArrayList<Integer>();
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_grouping_create_grouping"; //The name of the webservice function in the glue webservice
		String courseid = c.getId();
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		String name, description;
		for (int i = 0; i < groups.size(); i++){
			name = groups.get(i).getName();
			description = "";
			try {
				qapiUrl += "&groupings[" + i + "][courseid]=" + URLEncoder.encode(courseid, "UTF-8");
				qapiUrl += "&groupings[" + i + "][name]=" + URLEncoder.encode(name, "UTF-8");
				qapiUrl += "&groupings[" + i + "][description]=" + URLEncoder.encode(description, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		try {
			String response = doGetFromURL(qapiUrl);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The MULTIPLE node has the list of SINGLE child nodes
			NodeList groupingNodeList = doc.getDocumentElement().getChildNodes().item(1).getChildNodes();
			int groupNumber = 0;
			for (int i = 0; i < groupingNodeList.getLength(); i++){
				Node groupingNode = groupingNodeList.item(i);
				if (groupingNode.getNodeType() == Node.ELEMENT_NODE && groupingNode.getNodeName().equals("SINGLE")){					
					String groupingId = "";
					String groupingName = "";
					//Each grouping has a set of nodes with the values
					NodeList keyNodeList = groupingNode.getChildNodes();
					for (int j = 0; j < keyNodeList.getLength(); j++){
						Node keyNode = keyNodeList.item(j);
						if (keyNode.getNodeType() == Node.ELEMENT_NODE && keyNode.getNodeName().equals("KEY")) {						 
							Element keyElement = (Element) keyNode;
							String keyName = keyElement.getAttribute("name");
							if (keyName.equals("id")){
							    groupingId = keyElement.getChildNodes().item(0).getTextContent();
							}else if (keyName.equals("name")){
								groupingName = keyElement.getChildNodes().item(0).getTextContent();
								Group group = groups.get(groupNumber);
								groupNumber++;
								//Make sure that the group name is the same
								if (!groupingId.equals("") && group.getName().equals(groupingName)){
									createdGroupingIds.add(Integer.parseInt(groupingId));
								}	
							}
						}
					}
				}
			}					    
							    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return createdGroupingIds;
	}
	
	/**
	 * Assign the groups to the groupings in a Moodle's course. Each group is assign to a different grouping
	 * @param groupingId ids of the groupings in the Moodle's course
	 * @param groupId ids of the groups in the Moodle's course
	 */
	public void assignGrouping(ArrayList<Integer> groupingId, ArrayList<Integer> groupId){
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_grouping_assign_grouping"; //The name of the webservice function in the glue webservice
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		if (groupingId.size() == groupId.size()){
			for (int i = 0; i < groupingId.size(); i++){
				qapiUrl += "&groupings_groups[" + i + "][groupingid]=" + groupingId.get(i);
				qapiUrl += "&groupings_groups[" + i + "][groupid]=" + groupId.get(i);
			}
			try {
				String response = doGetFromURL(qapiUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public ArrayList<CourseSection> getCourseSections(Course c){
		ArrayList<CourseSection> sections = new ArrayList<CourseSection>();
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_get_sections_course";
		String courseid = c.getId();
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction + "&courseid=" + courseid;
		
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//Every section is contained in a SINGLE tag
			NodeList sectionNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			for (int i = 0; i < sectionNodeList.getLength(); i++){
				Node sectionNode = sectionNodeList.item(i);
				//Each section has a set of nodes with the values
				NodeList keyNodeList = sectionNode.getChildNodes();
				String id = "";
				String course = "";
				String section = "";
				String name = "";
				String summary = "";
				String summaryformat = "";
				String sequence = "";
				String visible = "";
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("id")){
							id = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("course")){
							course = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("section")){
							section = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("name")){
							name = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("summary")){
							summary = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("summaryformat")){
							summaryformat = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("sequence")){
							sequence = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("visible")){
							visible = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
				CourseSection cs = new CourseSection(Integer.parseInt(id), Integer.parseInt(course), Integer.parseInt(section));
				cs.setName(name);
				cs.setSummary(summary);
				cs.setSummaryformat(Integer.parseInt(summaryformat));
				cs.setSequence(sequence);
				cs.setVisible(Integer.parseInt(visible));
				sections.add(cs);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sections;
	}
	
	public ArrayList<CourseModule> getCourseModules(Course c){
		ArrayList<CourseModule> modules = new ArrayList<CourseModule>();
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_get_modules_course";
		String courseid = c.getId();
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction + "&courseid=" + courseid;
		
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//Every module is contained in a SINGLE tag
			NodeList moduleNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			for (int i = 0; i < moduleNodeList.getLength(); i++){
				Node moduleNode = moduleNodeList.item(i);
				//Each module has a set of nodes with the values
				NodeList keyNodeList = moduleNode.getChildNodes();
				String id = "";
				String course = "";
				String module = "";
				String instance = "";
				String section = "";
				String visible = "";
				String groupmode = "";
				String groupingid = "";
				String groupmembersonly = "";
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("id")){
							id = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("course")){
							course = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("module")){
							module = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("instance")){
							instance = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("section")){
							section = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("visible")){
							visible = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("groupmode")){
							groupmode = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("groupingid")){
							groupingid = keyElement.getChildNodes().item(0).getTextContent();
						}
						else if(keyName.equals("groupmembersonly")){
							groupmembersonly = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
				CourseModule cm = new CourseModule(Integer.parseInt(id), Integer.parseInt(course), Integer.parseInt(module), Integer.parseInt(instance), Integer.parseInt(section));
				cm.setVisible(Integer.parseInt(visible));
				cm.setGroupmode(Integer.parseInt(groupmode));
				cm.setGroupingid(Integer.parseInt(groupingid));
				cm.setGroupmembersonly(Integer.parseInt(groupmembersonly));
				modules.add(cm);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modules;
	}
	
	public ArrayList<CourseUrl> getCourseUrls(Course c){
		ArrayList<CourseUrl> urls = new ArrayList<CourseUrl>();
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_get_urls_course";
		String courseid = c.getId();
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction + "&courseid=" + courseid;
		
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//Every Url is contained in a SINGLE tag
			NodeList UrlNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			for (int i = 0; i < UrlNodeList.getLength(); i++){
				Node urlNode = UrlNodeList.item(i);
				//Each Url has a set of nodes with the values
				NodeList keyNodeList = urlNode.getChildNodes();
				String id = "";
				String course = "";
				String name = "";
				String intro = "";
				String introformat = "";
				String externalurl = "";
				String display = "";
				String displayoptions = "";
				String parameters = "";
				String timemodified = "";
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("id")){
							id = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("course")){
							course = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("name")){
							name = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("intro")){
							intro = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("introformat")){
							introformat = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("externalurl")){
							externalurl = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("display")){
							display = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("display")){
							displayoptions = keyElement.getChildNodes().item(0).getTextContent();
						}
						else if(keyName.equals("parameters")){
							parameters = keyElement.getChildNodes().item(0).getTextContent();
						}
						else if(keyName.equals("timemodified")){
							timemodified = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
				CourseUrl courseUrl = new CourseUrl(Integer.parseInt(id), Integer.parseInt(course), name, intro, Integer.parseInt(introformat), externalurl);
				courseUrl.setDisplay(Integer.parseInt(display));
				courseUrl.setDisplayoptions(displayoptions);
				courseUrl.setParameters(parameters);
				courseUrl.setTimemodified(Integer.parseInt(timemodified));
				urls.add(courseUrl);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return urls;
	}
	
	public CourseModule getCourseModule(Integer moduleid){
		CourseModule cm = null;
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_get_module";
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction + "&moduleid=" + moduleid;
		
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The module is contained in a SINGLE tag
			NodeList moduleNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			if (moduleNodeList.getLength()==1){
				Node moduleNode = moduleNodeList.item(0);
				//Each module has a set of nodes with the values
				NodeList keyNodeList = moduleNode.getChildNodes();
				String id = "";
				String course = "";
				String module = "";
				String instance = "";
				String section = "";
				String visible = "";
				String groupmode = "";
				String groupingid = "";
				String groupmembersonly = "";
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("id")){
							id = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("course")){
							course = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("module")){
							module = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("instance")){
							instance = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("section")){
							section = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("visible")){
							visible = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("groupmode")){
							groupmode = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("groupingid")){
							groupingid = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("groupmembersonly")){
							groupmembersonly = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
				cm = new CourseModule(Integer.parseInt(id), Integer.parseInt(course), Integer.parseInt(module), Integer.parseInt(instance), Integer.parseInt(section));
				cm.setVisible(Integer.parseInt(visible));
				cm.setGroupmode(Integer.parseInt(groupmode));
				cm.setGroupingid(Integer.parseInt(groupingid));
				cm.setGroupmembersonly(Integer.parseInt(groupmembersonly));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cm;
	}
	
	/**
	 * Updates the information of the a module in a course
	 * @param cm The module in the course to be updated
	 * @return The module has been updated correctly or not
	 */
	public Boolean updateCourseModule(CourseModule cm){
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_update_module";
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		qapiUrl = qapiUrl + "&module[id]=" + cm.getId() + "&module[course]=" + cm.getCourse() + "&module[module]=" + cm.getModule() + "&module[instance]=" + cm.getInstance();
		qapiUrl = qapiUrl + "&module[section]=" + cm.getSection() + "&module[visible]=" + cm.getVisible() + "&module[groupmode]=" + cm.getGroupmode() + "&module[groupingid]=" + cm.getGroupingid();
		qapiUrl = qapiUrl + "&module[groupmembersonly]=" + cm.getGroupmembersonly();
		String success = "";
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The module is contained in a SINGLE tag
			NodeList moduleNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			if (moduleNodeList.getLength()==1){
				Node moduleNode = moduleNodeList.item(0);
				//Each module has a set of nodes with the values
				NodeList keyNodeList = moduleNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("success")){
							success = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!success.isEmpty() && Integer.parseInt(success)==1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Updates the information of a section in a course
	 * @param cs The section in the course to be updated
	 * @return The section has been updated correctly or not
	 */
	public Boolean updateCourseSection(CourseSection cs){
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_update_section";
		try{
			qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
			qapiUrl = qapiUrl + "&section[id]=" + cs.getId() + "&section[course]=" + cs.getCourse() + "&section[section]=" + cs.getSection() + "&section[name]=" + URLEncoder.encode(cs.getName(), "UTF-8");
			qapiUrl = qapiUrl + "&section[summary]=" + URLEncoder.encode(cs.getSummary(), "UTF-8") + "&section[summaryformat]=" + cs.getSummaryformat() + "&section[sequence]=" + URLEncoder.encode(cs.getSequence(), "UTF-8") + "&section[visible]=" + cs.getVisible();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String success = "";
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The response is contained in a SINGLE tag
			NodeList sectionNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			if (sectionNodeList.getLength()==1){
				Node sectionNode = sectionNodeList.item(0);
				//Each module has a set of nodes with the values
				NodeList keyNodeList = sectionNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("success")){
							success = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!success.isEmpty() && Integer.parseInt(success)==1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Insert a resource of type URL into a course
	 * @param url The resource of type URL to be inserted
	 * @return The url id
	 */
	public Integer insertCourseUrl(CourseUrl url){
		Integer urlid = null;
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_insert_url_course"; //The name of the webservice function in the glue webservice
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		try {
			qapiUrl = qapiUrl + "&course=" + url.getCourse() + "&name=" + URLEncoder.encode(url.getName(),"UTF-8") + "&intro=" + URLEncoder.encode(url.getIntro(),"UTF-8") + "&introformat=" + url.getIntroformat() + "&externalurl=" + URLEncoder.encode(url.getExternalurl(), "UTF-8");
			qapiUrl = qapiUrl + "&display=" + url.getDisplay() + "&displayoptions=" + URLEncoder.encode(url.getDisplayoptions(),"UTF-8") + "&parameters=" + URLEncoder.encode(url.getParameters(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			Node urlNode = doc.getDocumentElement().getChildNodes().item(1);
			if (urlNode.getNodeType() == Node.ELEMENT_NODE && urlNode.getNodeName().equals("SINGLE")){
				//Each url has a set of nodes with the values
				NodeList keyNodeList = urlNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE && keyNode.getNodeName().equals("KEY")){
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("urlid")){	
							urlid = Integer.parseInt(keyElement.getChildNodes().item(0).getTextContent());
							return urlid;
						}
					}
				}
			}
		} catch (Exception e) {
			return urlid;
		}
		return urlid;
	}
	
	/**
	 * Delete a url list in a course
	 * @param urls The url list to be deleted
	 * @return The url list has been deleted correctly or not
	 */
	public Boolean deleteCourseUrls(ArrayList<CourseUrl> urls){
		if (urls.size() == 0){
			return true;
		}
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_delete_url_course";
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		for (int i = 0; i < urls.size(); i++){
			CourseUrl url = urls.get(i);
			qapiUrl += "&urls[" + i + "][urlid]=" + url.getId() + "&urls[" + i + "][courseid]=" + url.getCourse();
		}
		String success = "";
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The url is contained in a SINGLE tag
			NodeList urlNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			if (urlNodeList.getLength()==1){
				Node urlNode = urlNodeList.item(0);
				//Each module has a set of nodes with the values
				NodeList keyNodeList = urlNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("success")){
							success = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!success.isEmpty() && Integer.parseInt(success)==1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Insert a module into a course
	 * @param cm The module to be inserted
	 * @return The module id
	 */
	public Integer insertCourseModule(CourseModule cm){
		Integer moduleid = null;
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_insert_module"; //The name of the webservice function in the glue webservice
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		qapiUrl = qapiUrl + "&course=" + cm.getCourse() + "&module=" + cm.getModule() + "&instance=" + cm.getInstance() + "&section=" + cm.getSection() + "&visible=" + cm.getVisible();
		qapiUrl = qapiUrl + "&groupmode=" + cm.getGroupmode() + "&groupingid=" + cm.getGroupingid() + "&groupmembersonly=" + cm.getGroupmembersonly();

		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			Node moduleNode = doc.getDocumentElement().getChildNodes().item(1);
			if (moduleNode.getNodeType() == Node.ELEMENT_NODE && moduleNode.getNodeName().equals("SINGLE")){
				//Each module has a set of nodes with the values
				NodeList keyNodeList = moduleNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE && keyNode.getNodeName().equals("KEY")){
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("moduleid")){	
							moduleid = Integer.parseInt(keyElement.getChildNodes().item(0).getTextContent());
							return moduleid;
						}
					}
				}
			}
		} catch (Exception e) {
			return moduleid;
		}
		return moduleid;
	}
	
	/**
	 * Delete the information of a module in a course
	 * @param moduleid The module id in the course to be deleted
	 * @param courseid The course id
	 * @return The module in the course has been deleted correctly or not
	 */
	public Boolean deleteCourseModules(ArrayList<CourseModule> modules){
		if (modules.size() == 0){
			return true;
		}
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_delete_module";
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;

		for (int i = 0; i < modules.size(); i++){
			CourseModule module = modules.get(i);
			qapiUrl += "&modules[" + i + "][moduleid]=" + module.getId() + "&modules[" + i + "][courseid]=" + module.getCourse();
		}
		String success = "";
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The module is contained in a SINGLE tag
			NodeList moduleNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			if (moduleNodeList.getLength()==1){
				Node moduleNode = moduleNodeList.item(0);
				//Each module has a set of nodes with the values
				NodeList keyNodeList = moduleNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("success")){
							success = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!success.isEmpty() && Integer.parseInt(success)==1){
			return true;
		}else{
			return false;
		}
	}
	
	public ArrayList<CourseForum> getCourseForums(Course c){
		ArrayList<CourseForum> forums = new ArrayList<CourseForum>();
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_get_forums_course";
		String courseid = c.getId();
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction + "&courseid=" + courseid;
		
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//Every Forum is contained in a SINGLE tag
			NodeList forumNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			for (int i = 0; i < forumNodeList.getLength(); i++){
				Node forumNode = forumNodeList.item(i);
				//Each Forum has a set of nodes with the values
				NodeList keyNodeList = forumNode.getChildNodes();
				String id="", course="", type="", name="", intro="", introformat="", assessed="", assesstimestart="", assesstimefinish="", maxattachments="", timemodified="";
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("id")){
							id = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("course")){
							course = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("type")){
							type = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("name")){
							name = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("intro")){
							intro = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("introformat")){
							introformat = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("assessed")){
							assessed = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("assesstimestart")){
							assesstimestart = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("assesstimefinish")){
							assesstimefinish = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("maxattachments")){
							maxattachments = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("timemodified")){
							timemodified = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
				CourseForum courseForum = new CourseForum(Integer.parseInt(id),Integer.parseInt(course), type, name, intro);
				courseForum.setIntroformat(Integer.parseInt(introformat));
				courseForum.setAssessed(Integer.parseInt(assessed));
				courseForum.setAssesstimestart(Integer.parseInt(assesstimestart));
				courseForum.setAssesstimefinish(Integer.parseInt(assesstimefinish));
				courseForum.setMaxattachments(Integer.parseInt(maxattachments));
				courseForum.setTimemodified(Integer.parseInt(timemodified));
				forums.add(courseForum);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return forums;
	}
	
	/**
	 * Insert a resource of type Forum into a course
	 * @param forum The resource of type Forum to be inserted
	 * @return The forum id
	 */
	public Integer insertCourseForum(CourseForum forum){
		Integer forumid = null;
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_insert_forum_course"; //The name of the webservice function in the glue webservice
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		try {
			qapiUrl = qapiUrl + "&course=" + forum.getCourse() + "&type=" + URLEncoder.encode(forum.getType(),"UTF-8") + "&name=" + URLEncoder.encode(forum.getName(),"UTF-8") + "&intro=" + URLEncoder.encode(forum.getIntro(),"UTF-8");
			qapiUrl = qapiUrl + "&introformat=" + forum.getIntroformat() + "&assessed=" + forum.getAssessed() + "&assesstimestart=" + forum.getAssesstimestart() + "&assesstimefinish=" + forum.getAssesstimefinish() + "&maxattachments=" + forum.getMaxattachments();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			Node forumNode = doc.getDocumentElement().getChildNodes().item(1);
			if (forumNode.getNodeType() == Node.ELEMENT_NODE && forumNode.getNodeName().equals("SINGLE")){
				//Each forum has a set of nodes with the values
				NodeList keyNodeList = forumNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE && keyNode.getNodeName().equals("KEY")){
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("forumid")){	
							forumid = Integer.parseInt(keyElement.getChildNodes().item(0).getTextContent());
							return forumid;
						}
					}
				}
			}
		} catch (Exception e) {
			return forumid;
		}
		return forumid;
	}
	
	/**
	 * Delete the information of a resource of type Forum in a course
	 * @param forums The forum list to be deleted
	 * @return The forum list has been deleted or not
	 */
	public Boolean deleteCourseForums(ArrayList<CourseForum> forums){
		if (forums.size() == 0){
			return true;
		}
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_delete_forum_course";
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		for (int i = 0; i < forums.size(); i++){
			CourseForum forum = forums.get(i);
			qapiUrl += "&forums[" + i + "][forumid]=" + forum.getId() + "&forums[" + i + "][courseid]=" + forum.getCourse();
		}
		String success = "";
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The forum is contained in a SINGLE tag
			NodeList forumNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			if (forumNodeList.getLength()==1){
				Node urlNode = forumNodeList.item(0);
				//Each module has a set of nodes with the values
				NodeList keyNodeList = urlNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("success")){
							success = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!success.isEmpty() && Integer.parseInt(success)==1){
			return true;
		}else{
			return false;
		}
	}
	
	public ArrayList<CourseChat> getCourseChats(Course c){
		ArrayList<CourseChat> chats = new ArrayList<CourseChat>();
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_get_chats_course";
		String courseid = c.getId();
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction + "&courseid=" + courseid;
		
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//Every chat is contained in a SINGLE tag
			NodeList chatNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			for (int i = 0; i < chatNodeList.getLength(); i++){
				Node chatNode = chatNodeList.item(i);
				//Each Chat has a set of nodes with the values
				NodeList keyNodeList = chatNode.getChildNodes();
				String id="", course="", name="", intro="", introformat="", keepdays="", studentlogs="", chattime="", schedule="", timemodified="";
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("id")){
							id = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("course")){
							course = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("name")){
							name = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("intro")){
							intro = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("introformat")){
							introformat = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("keepdays")){
							keepdays = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("studentlogs")){
							studentlogs = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("chattime")){
							chattime = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("schedule")){
							schedule = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("timemodified")){
							timemodified = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
				CourseChat courseChat = new CourseChat(Integer.parseInt(id), Integer.parseInt(course),name, intro);
				courseChat.setIntroformat(Integer.parseInt(introformat));
				courseChat.setKeepdays(Integer.parseInt(keepdays));
				courseChat.setStudentlogs(Integer.parseInt(studentlogs));
				courseChat.setChattime(Integer.parseInt(chattime));
				courseChat.setSchedule(Integer.parseInt(schedule));
				courseChat.setTimemodified(Integer.parseInt(timemodified));
				chats.add(courseChat);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return chats;
	}
	
	/**
	 * Insert a resource of type chat into a course
	 * @param chat The resource of type Chat to be inserted
	 * @return The chat id
	 */
	public Integer insertCourseChat(CourseChat chat){
		Integer chatid = null;
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_insert_chat_course"; //The name of the webservice function in the glue webservice
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		try {
			qapiUrl = qapiUrl + "&course=" + chat.getCourse() + "&name=" + URLEncoder.encode(chat.getName(),"UTF-8") + "&intro=" + URLEncoder.encode(chat.getIntro(),"UTF-8") + "&introformat=" + chat.getIntroformat();
			qapiUrl = qapiUrl + "&keepdays=" + chat.getKeepdays() + "&studentlogs=" + chat.getStudentlogs() + "&chattime=" + chat.getChattime() + "&schedule=" + chat.getSchedule();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			Node chatNode = doc.getDocumentElement().getChildNodes().item(1);
			if (chatNode.getNodeType() == Node.ELEMENT_NODE && chatNode.getNodeName().equals("SINGLE")){
				//Each chat has a set of nodes with the values
				NodeList keyNodeList = chatNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE && keyNode.getNodeName().equals("KEY")){
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("chatid")){	
							chatid = Integer.parseInt(keyElement.getChildNodes().item(0).getTextContent());
							return chatid;
						}
					}
				}
			}
		} catch (Exception e) {
			return chatid;
		}
		return chatid;
	}
	
	/**
	 * Delete the information of a resource of type Chat in a course
	 * @param chatid The resource id of type Chat to be deleted
	 * @param courseid The course id
	 * @return The resource of type Chat has been deleted correctly or not
	 */
	public Boolean deleteCourseChats(ArrayList<CourseChat> chats){
		if (chats.size() == 0){
			return true;
		}
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_delete_chat_course";
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		for (int i = 0; i < chats.size(); i++){
			CourseChat chat = chats.get(i);
			qapiUrl += "&chats[" + i + "][chatid]=" + chat.getId() + "&chats[" + i + "][courseid]=" + chat.getCourse();
		}
		String success = "";
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The chat is contained in a SINGLE tag
			NodeList chatNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			if (chatNodeList.getLength()==1){
				Node chatNode = chatNodeList.item(0);
				//Each module has a set of nodes with the values
				NodeList keyNodeList = chatNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("success")){
							success = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!success.isEmpty() && Integer.parseInt(success)==1){
			return true;
		}else{
			return false;
		}
	}
	
	
	public ArrayList<CourseQuiz> getCourseQuizzes(Course c){
		ArrayList<CourseQuiz> quizzes = new ArrayList<CourseQuiz>();
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_get_quizzes_course";
		String courseid = c.getId();
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction + "&courseid=" + courseid;
		
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//Every quiz is contained in a SINGLE tag
			NodeList quizNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			for (int i = 0; i < quizNodeList.getLength(); i++){
				Node quizNode = quizNodeList.item(i);
				//Each Quiz has a set of nodes with the values
				NodeList keyNodeList = quizNode.getChildNodes();
				String id="", course="", name="", intro="", introformat="",timeopen="", timeclose="", preferredbehaviour="", attempts="", timemodified="";
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("id")){
							id = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("course")){
							course = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("name")){
							name = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("intro")){
							intro = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("introformat")){
							introformat = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("timeopen")){
							timeopen = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("timeclose")){
							timeclose = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("preferredbehaviour")){
							preferredbehaviour = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("attempts")){
							attempts = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("timemodified")){
							timemodified = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
				CourseQuiz courseQuiz = new CourseQuiz(Integer.parseInt(id), Integer.parseInt(course),name, intro);
				courseQuiz.setIntroformat(Integer.parseInt(introformat));
				courseQuiz.setTimeopen(Integer.parseInt(timeopen));
				courseQuiz.setTimeclose(Integer.parseInt(timeclose));
				courseQuiz.setPreferredbehaviour(preferredbehaviour);
				courseQuiz.setAttempts(Integer.parseInt(attempts));
				courseQuiz.setTimemodified(Integer.parseInt(timemodified));
				quizzes.add(courseQuiz);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return quizzes;
	}
	
	/**
	 * Insert a resource of type quiz into a course
	 * @param quiz The resource of type Quiz to be inserted
	 * @return The quiz id
	 */
	public Integer insertCourseQuiz(CourseQuiz quiz){
		Integer quizid = null;
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_insert_quiz_course"; //The name of the webservice function in the glue webservice
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		try {
			qapiUrl = qapiUrl + "&course=" + quiz.getCourse() + "&name=" + URLEncoder.encode(quiz.getName(),"UTF-8") + "&intro=" + URLEncoder.encode(quiz.getIntro(),"UTF-8") + "&introformat=" + quiz.getIntroformat();
			qapiUrl = qapiUrl + "&timeopen=" + quiz.getTimeopen() + "&timeclose=" + quiz.getTimeclose() + "&preferredbehaviour=" + URLEncoder.encode(quiz.getPreferredbehaviour(),"UTF-8") + "&attempts=" + quiz.getAttempts();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			Node quizNode = doc.getDocumentElement().getChildNodes().item(1);
			if (quizNode.getNodeType() == Node.ELEMENT_NODE && quizNode.getNodeName().equals("SINGLE")){
				//Each chat has a set of nodes with the values
				NodeList keyNodeList = quizNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE && keyNode.getNodeName().equals("KEY")){
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("quizid")){	
							quizid = Integer.parseInt(keyElement.getChildNodes().item(0).getTextContent());
							return quizid;
						}
					}
				}
			}
		} catch (Exception e) {
			return quizid;
		}
		return quizid;
	}
	
	/**
	 * Delete the quiz list in a course
	 * @param quizzes the quiz list to be deleted
	 * @return The quiz list has been deleted correctly or not
	 */
	public Boolean deleteCourseQuizzes(ArrayList<CourseQuiz> quizzes){
		if (quizzes.size() == 0){
			return true;
		}
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_delete_quiz_course";
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		for (int i = 0; i < quizzes.size(); i++){
			CourseQuiz quiz = quizzes.get(i);
			qapiUrl += "&quizzes[" + i + "][quizid]=" + quiz.getId() + "&quizzes[" + i + "][courseid]=" + quiz.getCourse();
		}
		String success = "";
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The quiz is contained in a SINGLE tag
			NodeList quizNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			if (quizNodeList.getLength()==1){
				Node quizNode = quizNodeList.item(0);
				//Each module has a set of nodes with the values
				NodeList keyNodeList = quizNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("success")){
							success = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!success.isEmpty() && Integer.parseInt(success)==1){
			return true;
		}else{
			return false;
		}
	}
	
	public ArrayList<CourseAssignment> getCourseAssignments(Course c){
		ArrayList<CourseAssignment> assignments = new ArrayList<CourseAssignment>();
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_get_assignments_course";
		String courseid = c.getId();
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction + "&courseid=" + courseid;
		
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//Every assignment is contained in a SINGLE tag
			NodeList assignmentNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			for (int i = 0; i < assignmentNodeList.getLength(); i++){
				Node assignmentNode = assignmentNodeList.item(i);
				//Each Assignment has a set of nodes with the values
				NodeList keyNodeList = assignmentNode.getChildNodes();
				String id="", course="", name="", intro="", introformat="",assignmenttype="",resubmit="",preventlate="",emailteachers="",grade="",maxbytes="",timemodified="";
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("id")){
							id = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("course")){
							course = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("name")){
							name = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("intro")){
							intro = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("introformat")){
							introformat = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("assignmentttype")){
							assignmenttype = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("resubmit")){
							resubmit = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("preventlate")){
							preventlate = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("emailteachers")){
							emailteachers = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("grade")){
							grade = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("maxbytes")){
							maxbytes = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("timemodified")){
							timemodified = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
				CourseAssignment courseAssignment = new CourseAssignment(Integer.parseInt(id), Integer.parseInt(course),name, intro);
				courseAssignment.setIntroformat(Integer.parseInt(introformat));
				courseAssignment.setAssignmenttype(assignmenttype);
				courseAssignment.setResubmit(Integer.parseInt(resubmit));
				courseAssignment.setPreventlate(Integer.parseInt(preventlate));
				courseAssignment.setEmailteachers(Integer.parseInt(emailteachers));
				courseAssignment.setGrade(Integer.parseInt(grade));
				courseAssignment.setMaxbytes(Integer.parseInt(maxbytes));
				courseAssignment.setTimemodified(Integer.parseInt(timemodified));
				assignments.add(courseAssignment);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return assignments;
	}
	
	/**
	 * Insert a resource of type assignment into a course
	 * @param assignment The resource of type Assignment to be inserted
	 * @return The quiz id
	 */
	public Integer insertCourseAssignment(CourseAssignment assignment){
		Integer assignmentid = null;
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_insert_assignment_course"; //The name of the webservice function in the glue webservice
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		try {
			qapiUrl = qapiUrl + "&course=" + assignment.getCourse() + "&name=" + URLEncoder.encode(assignment.getName(),"UTF-8") + "&intro=" + URLEncoder.encode(assignment.getIntro(),"UTF-8") + "&introformat=" + assignment.getIntroformat();
			qapiUrl = qapiUrl + "&assignmenttype=" + URLEncoder.encode(assignment.getAssignmenttype(), "UTF-8") + "&resubmit=" + assignment.getResubmit() + "&preventlate=" + assignment.getPreventlate() + "&emailteachers=" + assignment.getEmailteachers() + "&grade=" + assignment.getGrade() + "&maxbytes=" + assignment.getMaxbytes();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			Node assignmentNode = doc.getDocumentElement().getChildNodes().item(1);
			if (assignmentNode.getNodeType() == Node.ELEMENT_NODE && assignmentNode.getNodeName().equals("SINGLE")){
				//Each assignment has a set of nodes with the values
				NodeList keyNodeList = assignmentNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE && keyNode.getNodeName().equals("KEY")){
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("assignmentid")){	
							assignmentid = Integer.parseInt(keyElement.getChildNodes().item(0).getTextContent());
							return assignmentid;
						}
					}
				}
			}
		} catch (Exception e) {
			return assignmentid;
		}
		return assignmentid;
	}
	
	/**
	 * Delete the information of a resource of type Assignment in a course
	 * @param assignments The list of assignments
	 * @return The assignment list has been deleted correctly or not
	 */
	public Boolean deleteCourseAssignments(ArrayList<CourseAssignment> assignments){
		if (assignments.size() == 0){
			return true;
		}
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_delete_assignment_course";
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		for (int i = 0; i < assignments.size(); i++){
			CourseAssignment assignment = assignments.get(i);
			qapiUrl += "&assignments[" + i + "][assignmentid]=" + assignment.getId() + "&assignments[" + i + "][courseid]=" + assignment.getCourse();
		}
		String success = "";
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The assignment is contained in a SINGLE tag
			NodeList assignmentNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			if (assignmentNodeList.getLength()==1){
				Node assignmentNode = assignmentNodeList.item(0);
				//Each module has a set of nodes with the values
				NodeList keyNodeList = assignmentNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("success")){
							success = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!success.isEmpty() && Integer.parseInt(success)==1){
			return true;
		}else{
			return false;
		}
	}
	
	public ArrayList<CoursePage> getCoursePages(Course c){
		ArrayList<CoursePage> pages = new ArrayList<CoursePage>();
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_get_pages_course";
		String courseid = c.getId();
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction + "&courseid=" + courseid;
		
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//Every page is contained in a SINGLE tag
			NodeList pageNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			for (int i = 0; i < pageNodeList.getLength(); i++){
				Node pageNode = pageNodeList.item(i);
				//Each page has a set of nodes with the values
				NodeList keyNodeList = pageNode.getChildNodes();
				String id="", course="", name="", intro="", introformat="",content="",contentformat="",display="",displayoptions="",timemodified="";
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("id")){
							id = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("course")){
							course = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("name")){
							name = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("intro")){
							intro = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("introformat")){
							introformat = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("content")){
							content = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("contentformat")){
							contentformat = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("display")){
							display = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("displayoptions")){
							displayoptions = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("timemodified")){
							timemodified = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
				CoursePage coursePage = new CoursePage(Integer.parseInt(id), Integer.parseInt(course),name, intro);
				coursePage.setIntroformat(Integer.parseInt(introformat));
				coursePage.setContent(content);
				coursePage.setContentformat(Integer.parseInt(contentformat));
				coursePage.setDisplay(Integer.parseInt(display));
				coursePage.setDisplayoptions(displayoptions);
				coursePage.setTimemodified(Integer.parseInt(timemodified));
				pages.add(coursePage);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pages;
	}
	
	/**
	 * Insert a resource of type page into a course
	 * @param assignment The resource of type Page to be inserted
	 * @return The page id
	 */
	public Integer insertCoursePage(CoursePage page){
		Integer pageid = null;
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_insert_page_course"; //The name of the webservice function in the glue webservice
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		try {
			qapiUrl = qapiUrl + "&course=" + page.getCourse() + "&name=" + URLEncoder.encode(page.getName(),"UTF-8") + "&intro=" + URLEncoder.encode(page.getIntro(),"UTF-8") + "&introformat=" + page.getIntroformat();
			qapiUrl = qapiUrl + "&content=" + URLEncoder.encode(page.getContent(), "UTF-8") + "&contentformat=" + page.getContentformat() + "&display=" + page.getDisplay() + "&displayoptions=" + URLEncoder.encode(page.getDisplayoptions(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			Node pageNode = doc.getDocumentElement().getChildNodes().item(1);
			if (pageNode.getNodeType() == Node.ELEMENT_NODE && pageNode.getNodeName().equals("SINGLE")){
				//Each page has a set of nodes with the values
				NodeList keyNodeList = pageNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE && keyNode.getNodeName().equals("KEY")){
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("pageid")){	
							pageid = Integer.parseInt(keyElement.getChildNodes().item(0).getTextContent());
							return pageid;
						}
					}
				}
			}
		} catch (Exception e) {
			return pageid;
		}
		return pageid;
	}

	/**
	 * Delete a page list of the course
	 * @param pages The page list to be deleted
	 * @return The page list has been deleted correctly or not
	 */
	public Boolean deleteCoursePages(ArrayList<CoursePage> pages){
		if (pages.size() == 0){
			return true;
		}
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_delete_page_course";
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		for (int i = 0; i < pages.size(); i++){
			CoursePage page = pages.get(i);
			qapiUrl += "&pages[" + i + "][pageid]=" + page.getId() + "&pages[" + i + "][courseid]=" + page.getCourse();
		}
		String success = "";
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The page is contained in a SINGLE tag
			NodeList pageNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			if (pageNodeList.getLength()==1){
				Node pageNode = pageNodeList.item(0);
				//Each page has a set of nodes with the values
				NodeList keyNodeList = pageNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("success")){
							success = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!success.isEmpty() && Integer.parseInt(success)==1){
			return true;
		}else{
			return false;
		}
	}
	
	public ArrayList<CourseGrouping> getCourseGroupings(Course c){
		ArrayList<CourseGrouping> groupings = new ArrayList<CourseGrouping>();
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_course_get_groupings_course";
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction + "&courseid=" + c.getId();
		
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//Every Grouping is contained in a SINGLE tag
			NodeList groupingNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			for (int i = 0; i < groupingNodeList.getLength(); i++){
				Node groupingNode = groupingNodeList.item(i);
				//Each Grouping has a set of nodes with the values
				NodeList keyNodeList = groupingNode.getChildNodes();
				String id="", courseid="", name="", description="", descriptionformat="";
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("id")){
							id = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("courseid")){
							courseid = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("name")){
							name = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("description")){
							description = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("descriptionformat")){
							descriptionformat = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
				CourseGrouping courseGrouping = new CourseGrouping(Integer.parseInt(id),Integer.parseInt(courseid), name, description);
				courseGrouping.setDescriptionformat(Integer.parseInt(descriptionformat));
				groupings.add(courseGrouping);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return groupings;
	}
	
	
	/**
	 * Delete a list of groupings in a course
	 * @param groupingids The grouping list to be deleted
	 * @param courseid The course id
	 * @return The grouping list has been deleted correctly or not
	 */
	public Boolean deleteCourseGroupings(ArrayList<CourseGrouping> groupings){
		if (groupings.size() == 0){
			return true;
		}
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_grouping_delete_grouping_course";
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		
		for (int i = 0; i < groupings.size(); i++){
			CourseGrouping grouping = groupings.get(i);
			qapiUrl += "&groupings[" + i + "][groupingid]=" + grouping.getId() + "&groupings[" + i + "][courseid]=" + grouping.getCourseid();
		}
		
		String success = "";
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//The result is contained in a SINGLE tag
			NodeList groupingNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			if (groupingNodeList.getLength()==1){
				Node groupingNode = groupingNodeList.item(0);
				//Each module has a set of nodes with the values
				NodeList keyNodeList = groupingNode.getChildNodes();
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("success")){
							success = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!success.isEmpty() && Integer.parseInt(success)==1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Get the list of module types in Moodle
	 * @return The list of modules
	 */
	public ArrayList<Module> getModules(){
		ArrayList<Module> modules = new ArrayList<Module>();
		String qapiUrl = moodleUrl+"webservice/rest/server.php";
		String wsfunction = "gws_module_get_modules";
		qapiUrl = qapiUrl + "?wstoken=" + wstoken + "&wsfunction=" + wsfunction;
		
		try {
			String response = doGetFromURL(qapiUrl);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			Document doc = db.parse(is);
			//optional, but recommended
			doc.getDocumentElement().normalize();
			//Every module is contained in a SINGLE tag
			NodeList moduleNodeList = doc.getDocumentElement().getElementsByTagName("SINGLE");
			for (int i = 0; i < moduleNodeList.getLength(); i++){
				Node moduleNode = moduleNodeList.item(i);
				//Each module has a set of nodes with the values
				NodeList keyNodeList = moduleNode.getChildNodes();
				String id = "";
				String name = "";
				String version = "";
				String cron = "";
				String lastcron = "";
				String search = "";
				String visible= "";
				for (int j = 0; j < keyNodeList.getLength(); j++){
					Node keyNode = keyNodeList.item(j);
					if (keyNode.getNodeType() == Node.ELEMENT_NODE) {						 
						Element keyElement = (Element) keyNode;
						String keyName = keyElement.getAttribute("name");
						if (keyName.equals("id")){
							id = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("name")){
							name = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("version")){
							version = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("cron")){
							cron = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("lastcron")){
							lastcron = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("search")){
							search = keyElement.getChildNodes().item(0).getTextContent();
						}else if(keyName.equals("visible")){
							visible = keyElement.getChildNodes().item(0).getTextContent();
						}
					}
				}
				Module module = new Module(Integer.parseInt(id), name, Integer.parseInt(version), Integer.parseInt(cron));
				module.setLastcron(Integer.parseInt(lastcron));
				module.setSearch(search);
				module.setVisible(Integer.parseInt(visible));
				modules.add(module);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modules;
	}
	

}
