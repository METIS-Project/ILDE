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
 * URIs to the unguarded resources are defined and bound. Currently, only the GETs to toolInstances
 *
 * @author  Javier Enrique Hoyos Torio
 * @version 
 * @package glueps.core.gluepsManager
 */

public class GLUEPSManagerLdshakeUnguardedApplication extends GLUEPSManagerApplication {  

	public GLUEPSManagerLdshakeUnguardedApplication(Properties configuration) {
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
		// Defines a route for the resource DeployData
		router.attach("/deploys/{deployId}",DeployResource.class);
		router.attach("/deploys/{deployId}/undo",DeployResource.class);
		router.attach("/deploys/{deployId}/redo",DeployResource.class);
		router.attach("/deploys/{deployId}/save",DeployResource.class);
		router.attach("/deploys/{deployId}/saveAndExit",DeployResource.class);
		router.attach("/deploys/{deployId}/cancel",DeployResource.class);
		router.attach("/deploys/{deployId}/metadata",DeployResource.class);
		router.attach("/deploys/{deployId}/ldshakedata",DeployResource.class);
		// Route for the deployed versions of the deploy
		router.attach("/deploys/{deployId}/static",DeployResource.class);	
		router.attach("/deploys/{deployId}/live",DeployResource.class);
		// Defines a route for the resource list of ToolInstance
		router.attach("/deploys/{deployId}/toolInstances/{toolInstanceId}",ToolInstanceResource.class);
		router.attach("/deploys/{deployId}/toolInstances/{toolInstanceId}/hexconv",ToolInstanceResource.class);
		router.attach("/deploys/{deployId}/toolInstances/{toolInstanceId}/64conv",ToolInstanceResource.class);
		// Defines a route for the resource Configuration
		router.attach("/learningEnvironments/{LEId}/tools/{toolId}/configuration",ConfigurationResource.class);
		// Defines a route for the resource tool
		router.attach("/tools",ToolResource.class);
		// Defines a route for the resource list of Courses
		router.attach("/courses",CourseListResource.class);	
		
		// Defines a route for the resource Oauth
		router.attach("/oauth",OauthResource.class);
		
		//Please, do not add more routes, unless it is strictly necessary
		return router;  
	}



}  
