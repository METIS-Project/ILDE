/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Glue!PS.
 * 
 * Glue!PS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Glue!PS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package glueps.core.gluepsManager;  

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.restlet.Application;  
import org.restlet.Restlet;  
import org.restlet.data.CharacterSet;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;  

import glueps.core.resource.ConfigurationResource;
import glueps.core.resource.CourseListResource;
import glueps.core.resource.CourseResource;
import glueps.core.resource.DeployListResource;
import glueps.core.resource.DeployResource;
import glueps.core.resource.DesignListResource;
import glueps.core.resource.DesignResource;
import glueps.core.resource.LearningEnvironmentInstallationListResource;
import glueps.core.resource.LearningEnvironmentInstallationResource;
import glueps.core.resource.LearningEnvironmentListResource;
import glueps.core.resource.LearningEnvironmentResource;
import glueps.core.resource.OauthResource;
import glueps.core.resource.ToolInstanceListResource;
import glueps.core.resource.ToolInstanceResource;
import glueps.core.resource.ToolResource;




/**
 * RESTlet application class.
 * 
 * URIs to every resource are defined and bound.
 *
 * @author  Juan Carlos A. <jcalvarez@gsic.uva.es>
 * @version 
 * @package glueps.core.gluepsManager
 */

public class GLUEPSManagerApplication extends Application {  

	public static final String STARTING_SECTION_FIELD = "startingSection";
	private String dburi=null;
	private String dbuser=null;
	private String dbpassword=null;
	
	private String appPath = null;
	private String appExternalUri = null;
	
	private String gmurl=null;
	private String gmurlinternal=null;
	
	private int gmConnTimeout=60000;
	private int gmConnRetries=1;

	private int port = 8287;

	private String GDocsGMType=null;
	private String GSprdGMType=null;
	private String GPresGMType=null;
	private String WebCGMType=null;
	private String DabbGMType=null;
	private String GSICMWGMType=null;
	private String GDocs3GMType=null;
	private String GSprd3GMType=null;
	private String GPres3GMType=null;
	
	private boolean moodleRealTimeGlueps=false;
	
	private boolean ldShakeMode = false;
	private boolean onlyUserCourses = false;
	private boolean letPutUpdateTables = false;
	
	private String oauthGoogleClientid = null;
	private String oauthGoogleClientsecret = null;
	
	public GLUEPSManagerApplication(String uri, String user, String password, String rootpath, String gluelet){
		super();
		dburi=uri;
		dbuser=user;
		dbpassword=password;
		appPath=rootpath;
		gmurl=gluelet;
	}
	
	
	public GLUEPSManagerApplication(Properties configuration) {
		super();
		dburi=configuration.getProperty("db.uri", "");
		dbuser=configuration.getProperty("db.user", "");
		dbpassword=configuration.getProperty("db.password", "");
		appPath=configuration.getProperty("app.path", "");
		gmurl=configuration.getProperty("gluelet.uri", "");
		gmurlinternal=configuration.getProperty("gluelet.uri.internal", "");
		gmConnTimeout=Integer.valueOf(configuration.getProperty("gluelet.conn.timeout", "60000")).intValue();
		gmConnRetries=Integer.valueOf(configuration.getProperty("gluelet.conn.retries", "1")).intValue();
		appExternalUri=configuration.getProperty("app.external.uri");
		
		GDocsGMType=configuration.getProperty("gluelet.types.GDOCS_GM_TYPE","1");
		GSprdGMType=configuration.getProperty("gluelet.types.GSPRD_GM_TYPE","2");
		GPresGMType=configuration.getProperty("gluelet.types.GPRES_GM_TYPE","3");
		WebCGMType=configuration.getProperty("gluelet.types.WEBC_GM_TYPE", "6");
		DabbGMType=configuration.getProperty("gluelet.types.DABB_GM_TYPE","7");
		GSICMWGMType=configuration.getProperty("gluelet.types.GSICMW_GM_TYPE","15");
		GDocs3GMType=configuration.getProperty("gluelet.types.GDOCS3_GM_TYPE","1");
		GSprd3GMType=configuration.getProperty("gluelet.types.GSPRD3_GM_TYPE","2");
		GPres3GMType=configuration.getProperty("gluelet.types.GPRES3_GM_TYPE","3");

		moodleRealTimeGlueps=Boolean.valueOf(configuration.getProperty("moodle.realtimeglueps", "false")).booleanValue();

		ldShakeMode=Boolean.parseBoolean(configuration.getProperty("ldshakeMode","false"));
		onlyUserCourses=Boolean.parseBoolean(configuration.getProperty("onlyUserCourses","false"));
		letPutUpdateTables=Boolean.parseBoolean(configuration.getProperty("letPutUpdateTables","false"));
		
		oauthGoogleClientid=configuration.getProperty("oauth.google.clientid");
		oauthGoogleClientsecret=configuration.getProperty("oauth.google.clientsecret");

		//If it does not exist, we create the /uploaded/temp/zips directory
		if(appPath!=null && appPath.length()>0){
			File folder = new File(appPath+File.separator+"uploaded"+File.separator+"temp"+File.separator+"zips");
			if(!folder.exists()){//if the path does not exist, we create it
				System.out.println("Creating directory "+appPath+File.separator+"uploaded"+File.separator+"temp"+File.separator+"zips ...");
				folder.mkdirs();
			}else if(!folder.isDirectory()){//if it exists but it is a file, we delete it and create the directory
				System.out.println("Deleting file "+appPath+File.separator+"uploaded"+File.separator+"temp"+File.separator+"zips ...");
				folder.delete();
				System.out.println("Creating directory "+appPath+File.separator+"uploaded"+File.separator+"temp"+File.separator+"zips ...");
				folder.mkdirs();
			}//if it exists and is a folder, we do nothing
			
		}
		System.out.println("GLUE!-PS Application constructor complete!");

	}


	public String getDburi() {
		return dburi;
	}


	public String getDbuser() {
		return dbuser;
	}


	public String getDbpassword() {
		return dbpassword;
	}


	public String getAppPath() {
		return appPath;
	}


	public String getGmurl() {
		return gmurl;
	}


	public String getGDocsGMType() {
		return GDocsGMType;
	}


	public String getGSprdGMType() {
		return GSprdGMType;
	}


	public String getGPresGMType() {
		return GPresGMType;
	}


	public String getWebCGMType() {
		return WebCGMType;
	}



	public String getDabbGMType() {
		return DabbGMType;
	}


	public String getGSICMWGMType() {
		return GSICMWGMType;
	}


	public String getGDocs3GMType() {
		return GDocs3GMType;
	}


	public String getGSprd3GMType() {
		return GSprd3GMType;
	}


	public String getGPres3GMType() {
		return GPres3GMType;
	}

	/** 
	 * Creates a root Restlet that will receive all incoming calls. 
	 */  
	@Override  
	public synchronized Restlet createInboundRoot() {  
		// Create a router Restlet that defines routes.  
		Router router = new Router(getContext());
		
		// We set the encoding of files to UTF-8, especially for static files to prevent problems in windows??
		this.getMetadataService().setDefaultCharacterSet(CharacterSet.UTF_8);
		
		
		// Define the route to serve the GUI as static content
		router.attach("/gui", new Directory(getContext(), "file:///"+this.appPath+"/gui/"));
		// Define the route to serve the static design resources
		router.attach("/uploaded", new Directory(getContext(), "file:///"+this.appPath+"/uploaded/"));
		// Defines a route for the resource list of designs  
		router.attach("/designs",DesignListResource.class);				  
		// Defines a route for the resource design  
		router.attach("/designs/{designId}",DesignResource.class);				  
		// Defines a route for the resource list of LearningEnvironment
		router.attach("/learningEnvironments",LearningEnvironmentListResource.class);	
		// Defines a route for the resource LearningEnvironment
		router.attach("/learningEnvironments/{LEId}",LearningEnvironmentResource.class);						
		// Defines a route for the resource Configuration
		router.attach("/learningEnvironments/{LEId}/tools/{toolId}/configuration",ConfigurationResource.class);
		// Defines a route for the resource Course
		router.attach("/learningEnvironments/{LEId}/courses/{courseId}",CourseResource.class);
		// Defines a route for the resource list of LearningEnvironmentInstallations
		router.attach("/learningEnvironmentInstallations",LearningEnvironmentInstallationListResource.class);
		//Defines a route for the resource LearningEnvironmentInstallation
		router.attach("/learningEnvironmentInstallations/{LEInstId}",LearningEnvironmentInstallationResource.class);
		// Defines a route for the resource list of DeployData
		router.attach("/deploys",DeployListResource.class);	
		// Defines a route for the resource DeployData
		router.attach("/deploys/{deployId}",DeployResource.class);
		router.attach("/deploys/{deployId}/undo",DeployResource.class);
		router.attach("/deploys/{deployId}/redo",DeployResource.class);
		router.attach("/deploys/{deployId}/metadata",DeployResource.class);
		router.attach("/deploys/{deployId}/clone",DeployResource.class);
		// Route for the deployed versions of the deploy
		router.attach("/deploys/{deployId}/static",DeployResource.class);	
		router.attach("/deploys/{deployId}/live",DeployResource.class);	
		// Defines a route for the resource list of DeployData - for POSTing purposes
		router.attach("/designs/{designId}/deploys",DeployListResource.class);	
		// Defines a route for the resource list of ToolInstance
		router.attach("/deploys/{deployId}/toolInstances",ToolInstanceListResource.class);	
		// Defines a route for the resource list of ToolInstance
		router.attach("/deploys/{deployId}/toolInstances/{toolInstanceId}",ToolInstanceResource.class);	
		// Defines a route for the resource ToolInstance
		//router.attach("/toolInstances/{instanceId}",ToolInstanceResource.class);	
		// Defines a route for the resource list of Courses
		router.attach("/courses",CourseListResource.class);	
		// Defines a route for the resource tool
		router.attach("/tools",ToolResource.class);	
		
		// Defines a route for the resource Oauth
		router.attach("/oauth",OauthResource.class);

		return router;  
	}



	public String getGmurlinternal() {
		return gmurlinternal;
	}



	public long getGmConnTimeout() {
		return gmConnTimeout;
	}



	public int getGmConnRetries() {
		return gmConnRetries;
	}


	public String getAppExternalUri() {
		return appExternalUri;
	}



	public int getPort() {
		return port;
	}


	public boolean isMoodleRealTimeGlueps() {
		return moodleRealTimeGlueps;
	}


//	public void setMoodleRealTimeGlueps(boolean moodleRealTimeGlueps) {
//		this.moodleRealTimeGlueps = moodleRealTimeGlueps;
//	}


//	public void setMWBatchDeployMode(String mWBatchDeployMode) {
//		this.MWBatchDeployMode = mWBatchDeployMode;
//	}
//
//
//	public void setMoodleBatchDeployMode(String moodleBatchDeployMode) {
//		MoodleBatchDeployMode = moodleBatchDeployMode;
//	}

	public boolean getLetPutUpdateTables(){
		return letPutUpdateTables;
	}

	public boolean getLdShakeMode(){
		return ldShakeMode;
	}
	
	public boolean getOnlyUserCourses(){
		return onlyUserCourses;
	}

	public String getOauthGoogleClientid() {
		return oauthGoogleClientid;
	}


	public void setOauthGoogleClientid(String oauthGoogleClientid) {
		this.oauthGoogleClientid = oauthGoogleClientid;
	}


	public String getOauthGoogleClientsecret() {
		return oauthGoogleClientsecret;
	}


	public void setOauthGoogleClientsecret(String oauthGoogleClientsecret) {
		this.oauthGoogleClientsecret = oauthGoogleClientsecret;
	}



}  
