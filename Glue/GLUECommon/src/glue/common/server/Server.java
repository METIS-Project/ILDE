/**
 This file is part of GLUECommon.

 GLUECommon is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 GLUECommon is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use GLUECommon for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when GLUECommon is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute GLUECommon and/or modify it under the
   terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>
 ]
*/
package glue.common.server;

import glue.common.entities.configuration.ConfigurationRepository;
import glue.common.entities.instance.InstanceEntityFactory;
import glue.common.entities.instance.InstanceEntityRepository;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.restlet.Component;
import org.restlet.data.Protocol;


/**
 * Helper class that simplifies the initialization of an implementation adapter based in Java and RESTlet    
 *
 * @author  David A. Velasco
 * @version 2011012500
 * @package glue.common.server
 */

public class Server {
	
	/// attributes ///
	
	/** Application name */
	protected String appName;
	
	/** Author application (implementation adapter) name */
	protected String authorName;

	/** RESTlet application object */
	protected Application application;
	
	/** Starting piece of every URL, after host and port */
	protected String urlStart;
	
	/** Configuration properties */
	protected Properties configuration;
	
	/** Format instance namespace */
	protected String instanceNamespace;		// TODO maybe implementation-specific

	/** Configuration forms manager */
	protected ConfigurationRepository configurationRepository = null;

	/** Instance entities factory */
	protected InstanceEntityFactory instanceEntityFactory = null;
	
	/** Instance entities repository */
	protected InstanceEntityRepository instanceEntityRepository = null;
	
	/** Singleton instance */
	protected static Server theInstance = null;
	
	
	/// methods ///
	
	/**
	 * 
	 */
	public static Server initInstance(	String appName, 	String authorName,			Application application,	
										String urlStart,	String cfgFileName,			String instanceNamespace,
										ConfigurationRepository configurationManager, 	
										InstanceEntityFactory instanceEntityFactory,	InstanceEntityRepository	instanceEntityManager) {
		
		theInstance = new Server(	appName, 				authorName, 		application, 
									urlStart, 				cfgFileName, 		instanceNamespace, 
									configurationManager, 
									instanceEntityFactory, instanceEntityManager	);
		return theInstance;
	}


	public static Server getInstance() {
		return theInstance;
	}
	
	
	/**
	 * Constructor
	 * 
	 * @param appName
	 * @param application
	 * @param urlStart
	 * @param cfgFileName
	 * @param instanceNamespace
	 */
	protected Server(	String appName, 	String authorName,				Application application,	
						String urlStart,	String cfgFileName,				String instanceNamespace,	
						ConfigurationRepository configurationRepository,	
						InstanceEntityFactory instanceEntityFactory,		InstanceEntityRepository instanceEntityRepository) {
		
		this.appName 					= appName;
		this.authorName 				= authorName;
		this.application 				= application;
		this.urlStart 					= urlStart;
		this.configuration 				= loadConfigurationProperties(cfgFileName);
		this.instanceNamespace 			= instanceNamespace;
		this.configurationRepository 	= configurationRepository;
		this.instanceEntityFactory 		= instanceEntityFactory;
		this.instanceEntityRepository 	= instanceEntityRepository;
	}

	
    protected Properties loadConfigurationProperties(String fileName) {
    	// Load configuration properties
    	Properties configuration = new Properties();
    	try {
    		ResourceBundle rb = ResourceBundle.getBundle(fileName);
    		String key = null;
    		for (Enumeration<String> keys = rb.getKeys(); keys.hasMoreElements(); ) {
    			key = (String) keys.nextElement();
    			configuration.put(key , rb.getString(key));
    		}
    	} catch (MissingResourceException me) {
    		//System.err.println("Properties file '" + fileName + "' couldn't be found");
    		throw new RuntimeException("Properties file '" + fileName + "' couldn't be found");
    	}
    	return configuration;
    }


    public void start() {
    	// Run the component
    	int retries = 0;
    	int maxRetries = Integer.parseInt(configuration.getProperty("maxRetries", "2"));
    	boolean failWhileRunning;
    	do {
    		if (retries > 0)
    			System.err.println("Deploying again...");
    		failWhileRunning = false;
    		Component component = initComponent(configuration, urlStart, application);	// when fails, Exception reaches main() thanks to failWhileRunning
    		try {
    			component.start();
    		
    		} catch (Exception e) {
    			System.err.println("Unexpected termination of " + appName + " - " + e.getMessage());
    			failWhileRunning = true;
    			retries++;
    		}
    		
    	} while (failWhileRunning && retries < maxRetries);
    	
    }

    
    /**
     * Initialization of server
     *  
     * @param configuration		Configuration parameters loaded from CFG_FILENAME 
     */
    protected Component initComponent(Properties configuration, String urlBase, Application application) {
    	
        // Create a new Component.
        Component component = new Component();

        // Add a new HTTP server listening on the port kept in the configuration file
        int port = Integer.parseInt(configuration.getProperty("port"));
        component.getServers().add(Protocol.HTTP, port);
        component.getDefaultHost().attach(urlBase, application);	// TODO application should be reinstanced wirh each initComponent call

        // Set log level
        if (configuration.getProperty("logging").equalsIgnoreCase("on"))
        	component.getLogger().setLevel(Level.ALL);
       
        return component;
        
    }
    
    
    public String getAppName() {
    	return appName;
    }
    
    public String getInstanceNamespace() {
    	return instanceNamespace;
    }
    
    public ConfigurationRepository getConfigurationRepository() {
    	return configurationRepository;
    }


	public String getAuthorName() {
		return authorName;
	}


	public InstanceEntityFactory getInstanceEntityFactory() {
		return instanceEntityFactory;
	}

	public InstanceEntityRepository getInstanceEntityRepository() {
		return instanceEntityRepository;
	}

}
