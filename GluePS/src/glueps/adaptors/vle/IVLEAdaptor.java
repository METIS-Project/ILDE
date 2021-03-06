package glueps.adaptors.vle;

import glueps.core.model.Deploy;
import glueps.core.model.Group;
import glueps.core.model.Participant;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBException;

public interface IVLEAdaptor {

	
	
	
	
	/**
	 * Gets a course's list of participants, including LE-specific data and role (teacher/student) 
	 * @param baseUri The base URI of the LE (upon which the access to the course information is calculated) 
	 * @param courseId The LE-specific identifier of the course
	 * @return A HashMap containing the glueps.core.model.Participant with participant information, indexed by the LE-specific userid
	 */
	public HashMap<String, Participant> getCourseUsers(String baseUri,
			String courseId);
	
	/**
	 * Gets a course's list of groups, including each group's participants
	 * @param baseUri The base URI of the LE (upon which the access to the course information is calculated) 
	 * @param courseId The LE-specific identifier of the course
	 * @return A HashMap containing the glueps.core.model.Group with group information, indexed by the LE-specific groupid
	 */
	public HashMap<String, Group> getCourseGroups(String baseUri,
			String courseId);
	
	/**
	 * Gets a LE's list of available courses 
	 * @param baseUri The base URI of the LE (upon which the access to the course list is calculated)
	 * @return A HashMap containing the list of course names, indexed by the LE-specific courseid
	 */
	public HashMap<String, String> getCourses(String baseUri);
	
	/**
	 * Gets a LE's list of available courses in which the user is an administrator
	 * @param baseUri The base URI of the LE (upon which the access to the course list is calculated)
	 * @param username The user name in the LE
	 * @return A HashMap containing the list of course names, indexed by the LE-specific courseid
	 */
	public HashMap<String, String> getCourses(String baseUri, String username);
	
	/**
	 * Gets a LE's list of internal tools
	 * @return A HashMap containing the list of internal tool names, indexed by the LE-specific internaltoolid
	 */
	public HashMap<String, String> getInternalTools();

	/**
	 * Gets a LE's list of participants, including LE-specific data and role (teacher/student) 
	 * @param baseUri The base URI of the LE (upon which the access to the participant list is calculated) 
	 * @return A HashMap containing the glueps.core.model.Participant with participant information, indexed by the LE-specific userid
	 */
	public HashMap<String,Participant> getUsers(String baseUri);

	
	/**
	 * Returns the tool configuration form of an internal LE tool, generally in XForms format
	 * @param toolId The LE tool identifier
	 * @return A String with the contents of the (XForms) configuration file
	 */
	public String getToolConfiguration(String toolId);
	
	
}
