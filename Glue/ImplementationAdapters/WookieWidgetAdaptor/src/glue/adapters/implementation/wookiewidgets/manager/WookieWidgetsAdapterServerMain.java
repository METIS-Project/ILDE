/**
 This file is part of WookieWidgetAdapter.

 WookieWidgetAdapter is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 WookieWidgetAdapter is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use WookieWidgetAdapter for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when WookieWidgetAdapter is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute WookieWidgetAdapter and/or modify it under the
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
package glue.adapters.implementation.wookiewidgets.manager;

import java.util.logging.Level;
import java.util.logging.Logger;

import glue.adapters.implementation.wookiewidgets.entities.WookieWidgetsFactory;
import glue.common.entities.configuration.BasicConfigurationRepository;
import glue.common.entities.configuration.ConfigurationRepository;
import glue.common.entities.instance.BasicInstanceEntityRepository;
import glue.common.entities.instance.InstanceEntityFactory;
import glue.common.entities.instance.InstanceEntityRepository;
import glue.common.format.FormatStatic;
import glue.common.server.Server;


/**
 * Main class for Wookie Widgets service launch.
 *
 * @author  		David A. Velasco
 * @contributor 	Carlos Alario
 * @contributor		Javier Enrique Hoyos Torio
 * @version 		2012092501
 * @package 		glue.adapters.implementation.wookiewidgets.manager
 */

public class WookieWidgetsAdapterServerMain {

    /// attributes ///
    
	   /** Name of the Wookie Widgets Adapter*/
	public static final String APP_NAME = "GSIC Wookie Widgets Adapter";
	
	/** Name of the implementation adapter author */
	public static final String AUTHOR_NAME = FormatStatic.GSIC_NAME;
	
    /** Filename of the configuration properties file */
    protected final static String CFG_FILENAME = "app";
    
    /** Starting piece of every URL, after host and port */
    protected final static String BASE_URL = "/ToolAdapter/WookieWidgets";

    /** GData implementation namespace */
	public static final String INSTANCE_NAMESPACE = "http://gsic.uva.es/glue/adapters/implementation/wookiewidgets/1.0";
	
	/** Server */
	public static Server server;
	
	/** Tool implementation names */
	protected static String [] toolImplementationNames = 	{	"Chat Widget", 
																"SharedDraw Widget", 
																"Sudoku Widget",
																"Forum Widget",
																"Natter Chat Widget",
																"Bubble Game Widget",
																"Butterfly Paint Widget",
																"You Decide Widget"}; 

	/** Configuration identifiers */
	protected static String [] configurationIds = 			{	"1", "1", "1", "1", "1", "1", "1", "2"	}; 
	
	/** Configuration files names */
	protected static String [] configurationFileNames = 	{	"WookieWidgetConfiguration.xhtml",
																"WookieWidgetConfiguration.xhtml",
																"WookieWidgetConfiguration.xhtml", 
																"WookieWidgetConfiguration.xhtml", 
																"WookieWidgetConfiguration.xhtml", 
																"WookieWidgetConfiguration.xhtml", 
																"WookieWidgetConfiguration.xhtml",
																"YouDecideWidgetConfiguration.xhtml"};
	
	/** Intance entities respotiory - becames member to be reachable by InstancesSave */
	private static InstanceEntityRepository instanceEntityRepository;
	

	/// methods ///
	
	/** 
	 * Main method 
	 */
    public static void main(String[] args) throws Exception {
    	ConfigurationRepository configurationRepository		= new BasicConfigurationRepository(toolImplementationNames, configurationIds, configurationFileNames);
    	InstanceEntityFactory instanceEntityFactory			= new WookieWidgetsFactory(); 
    	//InstanceEntityRepository instanceEntityRepository	= new BasicInstanceEntityRepository(instanceEntityFactory, "instances.txt");
    	instanceEntityRepository	= new BasicInstanceEntityRepository(instanceEntityFactory, "instances.txt");
    	
    	server = Server.initInstance(	APP_NAME, AUTHOR_NAME, 	new WookieWidgetsAdapterApplication(), 
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
