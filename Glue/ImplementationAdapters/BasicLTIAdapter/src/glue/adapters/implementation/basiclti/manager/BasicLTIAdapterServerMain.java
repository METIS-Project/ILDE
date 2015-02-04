package glue.adapters.implementation.basiclti.manager;

import java.util.logging.Level;
import java.util.logging.Logger;

import glue.adapters.implementation.basiclti.entities.BasicLTIFactory;
import glue.common.entities.instance.BasicInstanceEntityRepository;
import glue.common.entities.instance.InstanceEntityFactory;
import glue.common.entities.instance.InstanceEntityRepository;
import glue.common.entities.configuration.BasicConfigurationRepository;
import glue.common.entities.configuration.ConfigurationRepository;
import glue.common.format.FormatStatic;
import glue.common.server.Server;

/**
 * Main class for BasicLTI service launch.
 * 
 * @author  	Javier Enrique Hoyos Torio
 * @version 	2012092501
 * @package 	glue.adapters.implementation.basiclti.manager
 */
public class BasicLTIAdapterServerMain {
	
    /// attributes ///
	
    /** Name of the BasicLTI Adapter */
	public static final String APP_NAME = "Basic LTI Adapter";
	
	/** Name of the implementation adapter author */
	public static final String AUTHOR_NAME = FormatStatic.GSIC_NAME;
	
    /** Filename of the configuration properties file */
	protected final static String CFG_FILENAME = "app";
    
    /** Starting piece of every URL, after host and port */
    protected final static String BASE_URL = "/ToolAdapter/BasicLTI";

    /** GData implementation namespace */
	public static final String INSTANCE_NAMESPACE = "http://gsic.uva.es/glue/adapters/implementation/basiclti/1.0";
	
	/** Server */
	public static Server server;
	
	/** Tool implementation names */
	protected static String [] toolImplementationNames = 	{	"Noteflight", "ToolTest", "Basic LTI PHP Provider"			}; 
	
	/** Configuration identifiers */
	protected static String [] configurationIds = 			{	"1", "2", "3"							}; 
	
	/** Configuration files names */
	protected static String [] configurationFileNames = 	{	"BasicLTIConfiguration.xhtml", "BasicLTIConfiguration.xhtml", "BasicLTIConfiguration.xhtml"	};
	
	/** Intance entities respotory - becames member to be reachable by InstancesSave */
	private static InstanceEntityRepository instanceEntityRepository;
	
	/** 
	 * Main method 
	 */
    public static void main(String[] args) throws Exception {   	
    	ConfigurationRepository configurationRepository = new BasicConfigurationRepository(toolImplementationNames, configurationIds, configurationFileNames);
    	InstanceEntityFactory instanceEntityFactory = new BasicLTIFactory();
    	instanceEntityRepository	= new BasicInstanceEntityRepository(instanceEntityFactory, "instances.txt");
    	
    	server = Server.initInstance(	APP_NAME, AUTHOR_NAME, 	new BasicLTIAdapterApplication(), 
				BASE_URL, CFG_FILENAME, INSTANCE_NAMESPACE, 
				configurationRepository, 
				instanceEntityFactory,	instanceEntityRepository);
    	
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
