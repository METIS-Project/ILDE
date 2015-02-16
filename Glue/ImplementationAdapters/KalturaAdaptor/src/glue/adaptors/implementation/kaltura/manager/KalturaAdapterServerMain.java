/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Kaltura Adapter.
 * 
 * Kaltura Adapter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Kaltura Adapter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.adaptors.implementation.kaltura.manager;


import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import glue.adaptors.implementation.kaltura.entities.KalturaFactory;
import glue.adaptors.implementation.kaltura.manager.KalturaAdapterApplication;
import glue.common.entities.configuration.BasicConfigurationRepository;
import glue.common.entities.configuration.ConfigurationRepository;
import glue.common.entities.instance.BasicInstanceEntityRepository;
import glue.common.entities.instance.InstanceEntityFactory;
import glue.common.entities.instance.InstanceEntityRepository;
import glue.common.format.FormatStatic;
import glue.common.server.Server;


/**
 * Main class for KalturaAdaptor service launch.
 *
 * @author  David A. Velasco <davivel@gsic.uva.es>
 * @version 2012092501
 * @package glue.adaptors.implementation.gdata3.manager
 */

public class KalturaAdapterServerMain {

	
    /// atributes ///
    
    /** Name of the GDataAdaptor to be shown to Kaltura service */
	public static final String APP_NAME = "Kaltura";
	
	/** Name of the implementation adaptor author */
	public static final String AUTHOR_NAME = FormatStatic.GSIC_NAME;
	
    /** Filename of the configuration properties file */
    protected final static String CFG_FILENAME = "app";
    
    /** Starting piece of every URL, after host and port */
    protected final static String BASE_URL = "/ToolAdapter/Kaltura";

    /** GData implementation namespace */
	public static final String INSTANCE_NAMESPACE = "http://gsic.uva.es/glue/adaptors/implementation/kaltura/1.0";
	
	/** Server */
	public static Server server;
	
	/** Tool implementation names */
	protected static String [] toolImplementationNames = 	{	"Kaltura" 	};	// accepts both suffixed and non-suffixed names for each tool  
	
	/** Configuration identifiers */
	protected static String [] configurationIds = 			{	"1"}; 
	
	/** Configuration files names */
	protected static String [] configurationFileNames = 	{	"KalturaConfiguration.xhtml"};

	/** Intance entities respotiory - becames member to be reachable by InstancesSave */
	private static InstanceEntityRepository instanceEntityRepository;
	

	/// methods ///
	
	/** 
	 * Main method 
	 */
    public static void main(String[] args) throws Exception {
    	
    	ConfigurationRepository configurationRepository		= new BasicConfigurationRepository(toolImplementationNames, configurationIds, configurationFileNames);
    	InstanceEntityFactory instanceEntityFactory			= new KalturaFactory(); 
    	//InstanceEntityRepository instanceEntityRepository	= new BasicInstanceEntityRepository(instanceEntityFactory, "instances.txt");
    	instanceEntityRepository	= new BasicInstanceEntityRepository(instanceEntityFactory, "instances.txt");
    	
    	server = Server.initInstance(	APP_NAME, AUTHOR_NAME, 	new KalturaAdapterApplication(), 
    									BASE_URL, CFG_FILENAME, INSTANCE_NAMESPACE, 
    									configurationRepository, 
    									instanceEntityFactory, 	instanceEntityRepository);

    	Runtime.getRuntime().addShutdownHook(new FinalSave());
    	
    	server.start();
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
    			logger.log(Level.INFO, "*** Final save of instances...");
    		else
    			System.out.println("*** Final save of instances...");
    		instanceEntityRepository.saveEntities();
    	}
    }

}
