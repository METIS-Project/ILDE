package glueps.core.gluepsManager;  

import java.util.Properties;

import org.restlet.Application;  
import org.restlet.Restlet;  
import org.restlet.data.CharacterSet;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;  

import glueps.core.resource.ConfigurationResource;
import glueps.core.resource.CourseResource;
import glueps.core.resource.DeployListResource;
import glueps.core.resource.DeployResource;
import glueps.core.resource.DesignListResource;
import glueps.core.resource.DesignResource;
import glueps.core.resource.DummyResource;
import glueps.core.resource.LearningEnvironmentListResource;
import glueps.core.resource.LearningEnvironmentResource;
import glueps.core.resource.ToolInstanceListResource;
import glueps.core.resource.ToolInstanceResource;




/**
 * RESTlet application class.
 * 
 * URIs to the unguarded resources are defined and bound. Currently, only the GETs to toolInstances
 *
 * @author  Juan Carlos A. <jcalvarez@gsic.uva.es>
 * @version 
 * @package glueps.core.gluepsManager
 */

public class GLUEPSManagerAlwaysOKApplication extends GLUEPSManagerApplication {  

	public GLUEPSManagerAlwaysOKApplication(Properties configuration) {
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
		
		// Defines a route for the resource ToolInstance
		router.attach("",DummyResource.class);	
		
		return router;  
	}



}  
