/**
 This file is part of GlueletManager.

 GlueletManager is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 GlueletManager is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use GlueletManager for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when GlueletManager is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute GlueletManager and/or modify it under the
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
package glue.core.glueletManager;


import glue.core.controllers.JpaControllersManager;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.Component;
import org.restlet.data.Protocol;


/**
 * Main class for GLUEletManager service launch.
 *
 * @author  David A. Velasco <davivel@gsic.uva.es>
 * @version 2012092501
 * @package glue.core.glueletManager
 */

public class GLUEletManagerServerMain {
	

    /// attributes ///
    
    /** Name of the Dabbleboard Adapter*/
	public static final String APP_NAME = "GSIC GLUEletManager";

    /** RESTlet component, container of every RESTlet element in the application, responsible for dispacthing of method calls to resources */
    protected static Component component = null;
    
    /** Filename of the configuration properties file */
    protected final static String CFG_FILENAME = "app";
    
    /// methods ///
    
	/** 
	 * Main method; here it all starts 
	 */
    public static void main(String[] args) {
    	
    	// Load configuration properties
    	Properties configuration = new Properties();
    	try {
    		ResourceBundle rb = ResourceBundle.getBundle(CFG_FILENAME);
    		String key = null;
    		for (Enumeration<String> keys = rb.getKeys(); keys.hasMoreElements(); ) {
    			key = (String) keys.nextElement();
    			configuration.put(key , rb.getString(key));
    		}
    	} catch (MissingResourceException me) {
    		System.err.println("Properties file '" + CFG_FILENAME + "' couldn't be found; default properties will be applied");
    	}
    	
    	Runtime.getRuntime().addShutdownHook(new FinalSave());
    	
    	// Run the component
    	int retries = 0;
    	int maxRetries = Integer.parseInt(configuration.getProperty("maxRetries", "2"));
    	boolean failWhileRunning;
    	do {
    		if (retries > 0)
    			System.err.println("Deploying again...");
    		failWhileRunning = false;
   			initComponent(configuration);	// when fails, Exception reaches main() thanks to failWhileRunning
    		try {
    			component.start();
    		
    		} catch (Exception e) {
    			System.err.println("Unexpected termination of " + APP_NAME + " - " + e.getMessage());
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
    protected static void initComponent(Properties configuration) {
    	
        // Create a new Component.
        component = new Component();

        // Add a new HTTP server listening on the port kept in the configuration file
        int port = Integer.parseInt(configuration.getProperty("port", "8185"));
        component.getServers().add(Protocol.HTTP, port);
        component.getDefaultHost().attach("/GLUEletManager", new GLUEletManagerApplication()); // TODO MAYBE change to non-hardcoded path

        // Add a client connection to implementation adapters
        component.getClients().add(Protocol.HTTP);	// to act as a client of implementation adapters
        
        // Set log level
        if (configuration.getProperty("logging", "on").equalsIgnoreCase("on"))
        	component.getLogger().setLevel(Level.ALL);
        else
        	component.getLogger().setLevel(Level.OFF);
        
    }
    
    
    /**
     * Helper class - save instances in persistent file when the adapter is shutdown
     *
     * @author  David A. Velasco <davivel@gsic.uva.es>
     * @version 2012031001
     * @package glue.adapters.implementation.dabbleboard.manager
     */
    private static class FinalSave extends Thread {
    	public void run() {
    		Logger logger = Logger.getLogger("org.restlet");
    		if (logger != null)
    			logger.log(Level.INFO, "*** Closing access to databases...");
    		else
    			System.out.println("*** Closing access to databases...");
    		JpaControllersManager.closeEntityManagers();
    	}
    }
    
}
