package glueps.adaptors.vle.mediawiki;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import glueps.adaptors.vle.IDynamicVLEDeployer;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Deploy;
import glueps.core.model.Group;
import glueps.core.model.Participant;

public class MediaWikiAdaptor extends MWOperator implements IDynamicVLEDeployer {

	private static final String DEFAULT_CONFIG_FILENAME = "defaultMediaWikiConfiguration.xforms";
	private static final Object TEACHER_WIKI_GROUP = "sysop";
	private String templateDir;
	
	//This is the wiki base URL
	//private URL wikiURL = null;
	//protected String wikiCookie = "";
	
	//TODO These two should be set somehow in the constructor. For now, we hardwire them for delfos
	private String wikiUser;
	private String wikiPassword;
	private Properties properties;
	private Map<String, String> parameters;
	
	
	public MediaWikiAdaptor() {
		super();
	}
	
	public MediaWikiAdaptor(Properties properties, Map<String, String> parameters){
		
		super();
		this.wikiUser=getMWGluepsUser();
		this.wikiPassword=getMWGluepsPwd();
		this.properties = properties;
		this.parameters = parameters;
	}
	
	public MediaWikiAdaptor(String wikiUser, String wikiPassword, Properties properties, Map<String, String> parameters){
		
		super();
		this.wikiUser=wikiUser;
		this.wikiPassword=wikiPassword;
		this.properties = properties;
		this.parameters = parameters;
	}	
	
	
	/*public String getWikiUser(){
		return wikiUser;
	}
	
	public void setWikiUser(String wikiUser){
		this.wikiUser = wikiUser;
	}
	
	public String getWikiPassword(){
		return wikiPassword;
	}
	
	public void setWikiPassword(String wikiPassword){
		this.wikiPassword = wikiPassword;
	}*/
	
	@Override
	public HashMap<String, Participant> getCourseUsers(String baseUri,
			String courseId) {
		//Currently, in MediaWiki we do not have courses, and so we return the whole list of wiki users
		return getUsers(baseUri);
	}

	
	@Override
	public HashMap<String, String> getCourses(String baseUri) {
		
		//We do not have courses in MediaWiki, so we just return a single entry
		//TODO in the future, we could have a category of pages for the courses, but we have to discuss this further
		
		HashMap<String, String> courses = new HashMap<String, String>();
		
		courses.put("0", "-- Default course --");
		
		return courses;
	}
	
	@Override
	public HashMap<String, String> getCourses(String baseUri, String username) {
		
		//We do not have courses in MediaWiki, so we just return a single entry
		//TODO in the future, we could have a category of pages for the courses, but we have to discuss this further
		
		HashMap<String, String> courses = new HashMap<String, String>();
		
		courses.put("0", "-- Default course --");
		
		return courses;
	}
	
	@Override
	public HashMap<String, String> getInternalTools() {
		// TODO Right now the tools are hardwired. Someday, we can ask the installation through an API
		HashMap<String, String> tools = new HashMap<String, String>();
		
		tools.put("wiki", "Wiki Page (MediaWiki)");
		
		return tools;
	}
	

	@Override
	public HashMap<String, Participant> getUsers(String baseUri) {

		HashMap<String, Participant> users = null;
		
		try {
			this.wikiURL = new URL(baseUri);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		if (this.wikiURL != null){
			
			URLConnection conn = null;
			try {
				conn = this.wikiURL.openConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
			

			if (conn!= null){
				// We did connect with the wiki

				// Authentication with MediaWiki
				if (mediaWikiAuth(this.wikiUser, this.wikiPassword)){
					
					users = this.getWikiUsers(this.wikiCookie);
					return users;
				}
			}		
		}
		return null;
	}

	private HashMap<String, Participant> getWikiUsers(String cookie) {
		String listURL = new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "api.php?action=query&list=allusers&auprop=groups&aulimit=5000&format=xml");
		String listResponse = postToMediaWikiApi(listURL, cookie);
		if (listResponse == null){
			return null;
		}

		HashMap<String, Participant> users = null;
		
		//JUAN: NAPA SARA USUARIOS. PARA QUITAR
//		ArrayList<String> sarausers = new ArrayList<String>(Arrays.asList("AARAVEL", "ABarLop", "ACUECAR", "ACanYep", 
//				"ADASNOT", "AGARCOL", "AGILPAS", "AGONGAR", "AGamIgl", "Leticia muñoz", "E12420190y", "Henry Díaz"));
		
		File f = new File("wikiTICusers.txt");
		ArrayList<String> sarausers = new ArrayList<String>();
		if(f.exists()) {
            try {
            	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            	String line;

				while((line = br.readLine()) != null) {
					sarausers.add(line);
					 System.out.println("line: " + line);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		//JUAN: FIN ÑAPA
		
		try{
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        InputSource is = new InputSource();
	        is.setCharacterStream(new StringReader(listResponse));
	        Document doc = db.parse(is);
	        NodeList nodeUsers = doc.getElementsByTagName("u");
	        
	        for(int i=0; i<nodeUsers.getLength();i++){
	        	Element userElement = (Element) nodeUsers.item(i);
	        	Participant part = null;
	        	NodeList groups = null;
	        	if((groups = userElement.getElementsByTagName("g"))==null || userElement.getElementsByTagName("g").getLength()==0){ //It does not have groups, we assume it is a student
		        	part = new Participant(userElement.getAttribute("name"), userElement.getAttribute("name"), null, null, false);
	        	}else{
	        		for(int j=0; j<groups.getLength(); j++){
	        			if(groups.item(j).getTextContent().equals(TEACHER_WIKI_GROUP)){
	        				part = new Participant(userElement.getAttribute("name"), userElement.getAttribute("name"), null, null, true);
	        				break;
	        			}
	        			
	        		}
	        		//if no group matched the teacher one, it is a student
	        		if(part==null) part = new Participant(userElement.getAttribute("name"), userElement.getAttribute("name"), null, null, false);
	        	}
	        	
	        	//JUAN: COMENTADO PARA METER ÑAPA DE USUARIOS PARA EXPERIENCIA DE SARA
//	        	if(users == null) users = new HashMap<String, Participant>();	        	
//        		users.put(userElement.getAttribute("name"),part);
        		
        		//JUAN: ÑAPA DE USUARIOS EXPERIENCIA SARA 
	        	if (f.exists() && this.wikiURL.toString().equals("http://www.gsic.uva.es/TIC/")){
	        		
	        		if (part.isStaff() || sarausers.contains(part.getId())){
	    	        	if(users == null) users = new HashMap<String, Participant>();	        	
	            		users.put(userElement.getAttribute("name"),part);
	        		}
	        	} else {
    	        	if(users == null) users = new HashMap<String, Participant>();	        	
            		users.put(userElement.getAttribute("name"),part);
	        	}
        		//JUAN: FIN DE ÑAPA
	        	
	        }
	        
	        return users;
	        
		}catch(Exception e){
			System.err.println("Error parsing response.");
			return null;
		}

	}

	@Override
	public Deploy deploy(String baseUri, Deploy lfdeploy) {
		
		try {
			this.wikiURL = new URL(baseUri);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return lfdeploy;
		}

		Deploy updatedDeploy=null;
		try {
			BatchDeployerFactory factory;
			factory = new BatchDeployerFactory();
			
			IMWBatchDeployer deployer;
			if(getMWBatchDeployMode()!=null) deployer = factory.getDeployer(this.wikiURL, this.wikiUser, this.wikiPassword, getMWBatchDeployMode(), parameters);
			else deployer = factory.getDeployer(this.wikiURL, this.wikiUser, this.wikiPassword, BatchDeployerFactory.OPEN4_MODE, parameters);
			updatedDeploy = deployer.batchDeploy(lfdeploy);
		} catch (Exception e) {
			e.printStackTrace();
			//If something went wrong, we set location=null in case it has already a url
			lfdeploy.setLiveDeployURL(null);
			return lfdeploy;
		}
		
		return updatedDeploy;
	}


	
	@Override
	public Deploy undeploy(String baseUri, Deploy lfdeploy) {
		
		try {
			this.wikiURL = new URL(baseUri);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		Deploy updatedDeploy=null;
		try {
			BatchDeployerFactory factory;
			factory = new BatchDeployerFactory();
			
			IMWBatchDeployer deployer;
			if(getMWBatchDeployMode()!=null) deployer = factory.getDeployer(this.wikiURL, this.wikiUser, this.wikiPassword, getMWBatchDeployMode(), parameters);
			else deployer = factory.getDeployer(this.wikiURL, this.wikiUser, this.wikiPassword, BatchDeployerFactory.OPEN4_MODE, parameters);
			updatedDeploy = deployer.batchUndeploy(lfdeploy);
		} catch (Exception e) {
			e.printStackTrace();
			//If something went wrong, we set location=null in case it has already a url
			lfdeploy.setLiveDeployURL(null);
			return lfdeploy;
		}
		
		return updatedDeploy;
	}
	
	
	@Override
	public String getToolConfiguration(String toolId) {
		String configuration;
		
		//We read the configuration file
		File configFile = new File(templateDir+File.separator+DEFAULT_CONFIG_FILENAME);
		
		if(configFile.exists()){
			try {
				configuration = FileUtils.readFileToString(configFile, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			
		}else return null;
		
		return configuration;
	}

	@Override
	public Deploy redeploy(String baseUri, Deploy lfdeploy) {
		try {
			this.wikiURL = new URL(baseUri);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}

		Deploy updatedDeploy=null;
		try {
			BatchDeployerFactory factory;
			factory = new BatchDeployerFactory();
			
			IMWBatchReDeployer deployer;
			if(getMWBatchDeployMode()!=null) deployer = factory.getReDeployer(this.wikiURL, this.wikiUser, this.wikiPassword, getMWBatchDeployMode(), parameters);
			else deployer = factory.getReDeployer(this.wikiURL, this.wikiUser, this.wikiPassword, BatchDeployerFactory.OPEN4_MODE, parameters);
			updatedDeploy = deployer.batchReDeploy(lfdeploy);
		} catch (Exception e) {
			e.printStackTrace();
			//If something went wrong, we set location=null in case it has already a url
			lfdeploy.setLiveDeployURL(null);
			return lfdeploy;
		}
		
		return updatedDeploy;
	}

	@Override
	public HashMap<String, Group> getCourseGroups(String baseUri,
			String courseId) {
		//There are no groups in wikis! we just return null
		return null;
	}
	
	
	private String getMWGluepsUser(){
		return properties.getProperty("gluepsuser", "gluepsuser");
	}
	
	private String getMWGluepsPwd(){
		return properties.getProperty("gluepspwd", "gluepspwd");
	}
	
	private String getMWBatchDeployMode(){
		return properties.getProperty("batchdeploymode", "simple");
	}

	@Override
	public boolean canBeDeployed(String baseUri, Deploy lfdeploy) {
		return true;
	}
}
