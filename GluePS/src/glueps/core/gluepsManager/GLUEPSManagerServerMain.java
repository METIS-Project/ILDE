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


import glueps.core.model.Deploy;
import glueps.core.model.InstancedActivity;
import glueps.core.persistence.JpaManager;
import glueps.core.service.inprocess.InProcessService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.ext.crypto.DigestAuthenticator;
import org.restlet.resource.Directory;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;
import org.restlet.util.Series;


/**
 * Main class for GLUEletManager service launch.
 *
 * @author  Juan Carlos A. <jcalvarez@gsic.uva.es>
 * @version 
 * @package glue.core.glueletManager
 */

public class GLUEPSManagerServerMain {
	

    /// atributes ///
    
    /** Name of the Dabbleboard Adaptor*/
	public static final String APP_NAME = "GSIC GLUEletManager";

    /** RESTlet component, container of every RESTlet element in the application, responsible for dispacthing of method calls to resources */
    protected static Component component = null;
    
    /** Filename of the configuration properties file */
    protected final static String CFG_FILENAME = "app.properties";

    /** Thread pool for background live deployments **/
    public static ExecutorService pool = null;
    
    /** Information of the deploys that are being deployed into a VLE **/
    public static InProcessService ips = null;
    
    /// methods ///
    
	/** 
	 * Main method; here it all starts 
	 */
    public static void main(String[] args) {
    	
    	// Load configuration properties
    	Properties configuration = new Properties();
    	FileInputStream in = null;
    	try {
    		
    		in = new FileInputStream("conf/" + CFG_FILENAME);
    		configuration.load(in);
    		
    	} catch (FileNotFoundException fe) {
    		System.err.println("Properties file '" + CFG_FILENAME + "' couldn't be found; default properties will be applied");
    	} catch (IOException io) {
    		System.err.println("Properties file '" + CFG_FILENAME + "' couldn't be loaded; default properties will be applied");
    	}
    	
    	try {
    		in.close();
    	} catch (IOException io) {
    		System.err.println("Unexpected fail while releasing properties file '" + CFG_FILENAME + "' ; trying to ignore");
    	}

    	//Initialize the background thread factory
    	pool = Executors.newFixedThreadPool(Integer.parseInt(configuration.getProperty("backgroundThreads", "10")));
    	
    	ips = new InProcessService();
    	
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
        int port = Integer.parseInt(configuration.getProperty("port", "8287"));

        //HTTP server
        if(configuration.getProperty("maxServerThreads")==null){
            component.getServers().add(Protocol.HTTP, port);
        }else{
        	Server httpServer = new Server(Protocol.HTTP, port);
    		component.getServers().add(httpServer);
    		httpServer.getContext().getParameters().add("maxThreads", configuration.getProperty("maxServerThreads","50"));
    		httpServer.getContext().getParameters().add("minThreads", configuration.getProperty("minServerThreads","25"));
    		httpServer.getContext().getParameters().add("maxTotalConnections", configuration.getProperty("maxServerThreads","50"));
        }

        //This is the unguarded part of the application
        GLUEPSManagerUnguardedApplication unguarded = new GLUEPSManagerUnguardedApplication(configuration);

        //Set the authentication framework (HTTP BASIC) for the dummy logout guard
        ChallengeAuthenticator dummy = new ChallengeAuthenticator(component.getContext().createChildContext(), ChallengeScheme.HTTP_BASIC, "GLUE!-PS Realm");
        GLUEPSManagerDummyVerifier dummyver = new GLUEPSManagerDummyVerifier();
        dummy.setVerifier(dummyver);
        GLUEPSManagerAlwaysOKApplication alwaysOk = new GLUEPSManagerAlwaysOKApplication(configuration);
        dummy.setNext(alwaysOk);
        
//        //Set the authentication framework (HTTP DIGEST) for the rest of the application
//        DigestAuthenticator guard = new DigestAuthenticator(component.getContext().createChildContext(), "GLUE!-PS Realm", "gluepsSecretServerKey");
//        // Instantiates a Verifier of identifier/secret couples (soon to be extracted from DB)
//        GLUEPSManagerVerifier verifier = new GLUEPSManagerVerifier();
//        guard.setWrappedVerifier(verifier);

      //Set the authentication framework (HTTP BASIC) for the rest of the application
      ChallengeAuthenticator guard = new ChallengeAuthenticator(component.getContext().createChildContext(), ChallengeScheme.HTTP_BASIC, "GLUE!-PS Realm");
      // Instantiates a Verifier of identifier/secret couples (soon to be extracted from DB)
      GLUEPSManagerVerifier verifier = new GLUEPSManagerVerifier();
      guard.setVerifier(verifier);

        //This is the main application
        GLUEPSManagerMainApplication app = new GLUEPSManagerMainApplication(configuration);

        // Guard the restlet
        guard.setNext(app);

        
        //component.getDefaultHost().attach("/GLUEPSManager", app); // TODO MAYBE change to non-hardcoded path
        
        //We put the unguarded part in front, and let the rest be guarded
        component.getDefaultHost().attach("/GLUEPSManager/logout",dummy);
        component.getDefaultHost().attach("/GLUEPSManager/deploys/{deployId}/toolInstances/{toolInstanceId}", unguarded);
        component.getDefaultHost().attach("/GLUEPSManager/gui", new Directory(component.getContext().createChildContext(), "file:///"+configuration.getProperty("app.path")+"/gui/"));
        component.getDefaultHost().attach("/GLUEPSManager/uploaded", new Directory(component.getContext().createChildContext(), "file:///"+configuration.getProperty("app.path")+"/uploaded/"));
        component.getDefaultHost().attach("/GLUEPSManager/arbrowser", unguarded);
        component.getDefaultHost().attach("/GLUEPSManager/aas", unguarded);
        
        Boolean ldshakeMode = Boolean.valueOf(configuration.getProperty("ldshakeMode", "false"));
        if (ldshakeMode){
        	GLUEPSManagerLdshakeUnguardedApplication ldshakeUnguarded = new GLUEPSManagerLdshakeUnguardedApplication(configuration);
	        //We set the guard (in front of the main app)
	        component.getDefaultHost().attach("/GLUEPSManager/ldshake", ldshakeUnguarded); // TODO MAYBE change to non-hardcoded path
	        component.getDefaultHost().attach("/GLUEPSManager", guard); // TODO MAYBE change to non-hardcoded path
        }
        else{
	        //We set the guard (in front of the main app)
	        component.getDefaultHost().attach("/GLUEPSManager", guard); // TODO MAYBE change to non-hardcoded path
        }

        
        component.getClients().add(Protocol.FILE);

        
        // Add a client connection to implementation adaptors
        //component.getClients().add(Protocol.HTTP);	// to act as a client of implementation adaptors
//        Client httpClient = new Client(Protocol.HTTP);
//        httpClient.setConnectTimeout(Integer.valueOf(configuration.getProperty("gluelet.conn.timeout", "60000")).intValue());
//		component.getClients().add(httpClient);
//        httpClient.getContext().getParameters().add("socketTimeout", configuration.getProperty("gluelet.conn.timeout", "60000"));
//        httpClient.getContext().getParameters().add("maxConnectionsPerHost", "100");
//        httpClient.getContext().getParameters().add("maxTotalConnections", "200");
        
        // Set log level
        if (configuration.getProperty("logging", "on").equalsIgnoreCase("on"))
        	component.getLogger().setLevel(Level.ALL); 
        
        
//        //TODO Initialize user data ------------------------------------------ this should be removed!!
//        JpaManager dbmanager = JpaManager.getInstance();
//        try {
//			dbmanager.populateInitialData();
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//        //We test the database LEs
//        try {
//			System.out.println("MySQL LEs: "+dbmanager.listLEObjects().toString());
//			System.out.println("MySQL Designs: "+dbmanager.listDesignObjects().toString());
//			System.out.println("MySQL deploys: "+dbmanager.listDeployObjects().toString());
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        //TODO END OF TO REMOVE!!------------------------------------------------------------------------------
//        
        
        
  

    }


}
