package glueps.core.model;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;

import org.restlet.data.Reference;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LearningEnvironment {

	private String id;
	
	private String name;
	
	private String type;
	
	private URL accessLocation;
	
	private String userid;
	
	private String creduser;
	
	private String credsecret;
	
	//private String credentials;
	
	private String author;

	private String installation;
	

	//For now, we do not use this, until we know we need it
	//Deploy modes: static (e.g. Moodle zip) and/or live (e.g. creating the wiki pages through an API)
	//public static final String STATIC_DEPLOY_MODE = "static";
	//public static final String LIVE_DEPLOY_MODE = "live";
	//private ArrayList<String> deployModes;

	private HashMap<String, String> internalTools;
	
	private HashMap<String, String> externalTools;
	
//	private HashMap<String, Course> courses;
	private HashMap<String, String> courses;
	
	//JUAN: introduced to may hide the AR controls
	private boolean showAr;
	
	//Additional parameters for the learning environment
	private String parameters;
	
	public LearningEnvironment() {
		super();
	}
	
	public LearningEnvironment(String id, String name, String type, URL accessLocation, String userid, String creduser, String credsecret, String installation) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.accessLocation = accessLocation;
		this.userid = userid;
		this.creduser = creduser;
		this.credsecret = credsecret;
		this.installation = installation;
	}

	public LearningEnvironment(String id, String name, String type, String author, URL accessLocation, String installation) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.author = author;
		this.accessLocation = accessLocation;
		this.installation = installation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	

	public URL getAccessLocation() {
		return accessLocation;
	}

	public void setAccessLocation(URL accessLocation) {
		this.accessLocation = accessLocation;
	}	

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public String getCreduser() {
		return creduser;
	}

	public void setCreduser(String creduser) {
		this.creduser = creduser;
	}
	
	public String getCredsecret() {
		return credsecret;
	}

	public void setCredsecret(String credsecret) {
		this.credsecret = credsecret;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getInstallation() {
		return installation;
	}

	public void setInstallation(String installation) {
		this.installation = installation;
	}

	public void setExternalTools(HashMap<String, String> externalTools) {
		this.externalTools = externalTools;
	}

	public HashMap<String, String> getExternalTools() {
		return externalTools;
	}

	public void setInternalTools(HashMap<String, String> internalTools) {
		this.internalTools = internalTools;
	}

	public HashMap<String, String> getInternalTools() {
		return internalTools;
	}

	@Override
	public String toString() {
		return "LearningEnvironment [id=" + id + ", name=" + name + ", type=" + type 
				+ ", accessLocation=" + accessLocation + ", userid=" + userid + ", creduser=" + creduser + ", credsecret=" + credsecret +  ", author=" + author
				+ ", internalTools=" + internalTools + ", externalTools=" + externalTools + ", courses=" + courses + "]";
	}

//	public void setCourses(HashMap<String, Course> courses) {
//		this.courses = courses;
//	}
//
//	public HashMap<String, Course> getCourses() {
//		return courses;
//	}

	public void setCourses(HashMap<String, String> courses) {
		this.courses = courses;
	}

	public HashMap<String, String> getCourses() {
		return courses;
	}

	//JUAN: introduced to may hide AR controls
	public boolean isShowAr() {
		return showAr;
	}

	public void setShowAr(boolean showAr) {
		this.showAr = showAr;
	}
	
	public String getParameters(){
		return parameters;
	}
	
	public void setParameters(String parameters){
		this.parameters = parameters;
	}

//	public void setDeployModes(ArrayList<String> deployModes) {
//		this.deployModes = deployModes;
//	}
//
//	public ArrayList<String> getDeployModes() {
//		return deployModes;
//	}

	public String getToolTypeNameFromId(String ToolTypeId) {
		HashMap<String, String> toolTypeList = this.getExternalTools();
		String toolname = null;
	    Iterator it = toolTypeList.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry tooltype = (Map.Entry)it.next();
	        String tooltypeIt = (String) tooltype.getKey();
	        String toolnameIt = (String) tooltype.getValue();
	        if (tooltypeIt.equals(ToolTypeId)){
	        	toolname = toolnameIt;
	        	break;
	        }
	    }
	    return toolname;
	}
	
	/**
	 * Decodes a string with adapter-specific parameters
	 * 
	 * @return	Map<String, String>		Set of name-value pairs with the decoded parameters. 
	 */
	public Map<String, String> decodeParams(){
		HashMap<String, String> result = new HashMap<String, String>();
		if (parameters != null) {
			StringTokenizer tokenizer = new StringTokenizer(parameters, "&");
			while (tokenizer.hasMoreElements()) {
				String token = tokenizer.nextToken();
				int pos = token.indexOf("=");
				if (pos > 0) {
					result.put(token.substring(0, pos), Reference.decode(token.substring(pos+1)));
				}
			}
		}
		return result;
	}
	
}
