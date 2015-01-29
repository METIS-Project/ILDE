package glueps.core.gluepsManager;  

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
import glueps.core.resource.PoiListResource;
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

public class GLUEPSManagerMainApplication extends GLUEPSManagerApplication {  


	public GLUEPSManagerMainApplication(Properties configuration) {
		super(configuration);

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
		//router.attach("/gui", new Directory(getContext(), "file:///"+this.getAppPath()+"/gui/"));
		// Define the route to serve the static design resources
		//router.attach("/uploaded", new Directory(getContext(), "file:///"+this.getAppPath()+"/uploaded/"));
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
		router.attach("/deploys/{deployId}/live/redeploy",DeployResource.class);
		router.attach("/deploys/{deployId}/live/newdeploy",DeployResource.class);
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



}  
