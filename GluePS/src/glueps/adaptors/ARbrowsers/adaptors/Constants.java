
package glueps.adaptors.ARbrowsers.adaptors; 


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public final class Constants
{
    public static final String CONTENT_TYPE_TEXT = "text/plain";
    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String CONTENT_TYPE_JPEG = "image/jpeg"; 
    public static final String CONTENT_TYPE_JSON = "application/javascript";
    public static final String CONTENT_TYPE_KML = "application/vnd.google-earth.kml+xml";
    public static final String CONTENT_TYPE_HTML = "text/html";

 //   public static final String URL_SERVER = "http://pandora.tel.uva.es/juanmunoz/GLUE_GLUEPSManager";
    public static final String URL_LOGIN = "/pois/login";
    public static final String URL_LOGOUT = "/pois/logout";
    public static final String loggedUsersPersistenceFile = "loggedUsers.txt";

    public static final String SERVER_URL = Constants.configuration().getProperty("app.junaio.external.uri", "http://localhost:8287/GLUEPSManager/");
    
    public static final String POS_TYPE_GEOPOSITION = "geoposition";
    public static final String POS_TYPE_JUNAIOMARKER = "junaiomarker";
    public static final String POS_TYPE_QRCODE = "qrcode";

    public static final String JUNAIOMARKER_URLBASE = Constants.configuration().getProperty("junaio.urlbase.marker", "markers/ID_Marker1-21/MetaioMarker");
    
    public static final String JUNAIO_ICON_GLUE = Constants.configuration().getProperty("junaio.icon.glue", "http://localhost:8287/GLUEPSManager/gui/glueps/arbrowsers/glue.zip");
    public static final String JUNAIO_ICON_LOGIN = Constants.configuration().getProperty("junaio.icon.login", "http://localhost:8287/GLUEPSManager/gui/glueps/arbrowsers/login.zip");
    public static final String JUNAIO_ICON_LOGOUT = Constants.configuration().getProperty("junaio.icon.logout", "http://localhost:8287/GLUEPSManager/gui/glueps/arbrowsers/logout.zip");
    public static final String JUNAIO_ICON_GEO = Constants.configuration().getProperty("junaio.icon.geo", "http://localhost:8287/GLUEPSManager/gui/glueps/arbrowsers/geo.zip");
    public static final String JUNAIO_ICON_MARKER = Constants.configuration().getProperty("junaio.icon.marker", "http://localhost:8287/GLUEPSManager/gui/glueps/arbrowsers/marker.zip");
    
    public static final String JUNAIO_3DMODEL_DEFAULT_SCALE = "0.1";
    public static final String JUNAIO_ARIMAGE_DEFAULT_SCALE = "1";
    
    public static final String JUNAIO_3DMODEL_DEFAULT_ORIENTATION = "1.5708,0,0";
    public static final String JUNAIO_ARIMAGE_JUNMARKER_DEFAULT_ORIENTATION = "0,1.5708,1.5708";
    public static final String JUNAIO_ARIMAGE_GEO_DEFAULT_ORIENTATION = "0,0,1.5708";
    
    public static String JUNAIO_LOGFILE = configuration().getProperty("junaio.logfile", null);
    
    public static final String LAYAR_ICON_GEO = Constants.configuration().getProperty("layar.icon.geo", "http://localhost:8287/GLUEPSManager/gui/glueps/arbrowsers/geolayer.png");
    public static final String LAYAR_ICON_GLUE = Constants.configuration().getProperty("layar.icon.glue", "http://localhost:8287/GLUEPSManager/gui/glueps/arbrowsers/logoGlue110.png");
    public static final String LAYAR_ICON_LOGIN = Constants.configuration().getProperty("layar.icon.login", "http://localhost:8287/GLUEPSManager/gui/glueps/arbrowsers/login.png");
    public static final String LAYAR_ICON_LOGOUT = Constants.configuration().getProperty("layar.icon.logout", "http://localhost:8287/GLUEPSManager/gui/glueps/arbrowsers/logout.png");

    public static final int LAYAR_DEF_MAX_POIS = 50;
    public static String ACCESS_LOGFILE = configuration().getProperty("arbrowsers.logfile.access", null);
    public static String GOOGLE_LOGFILE = configuration().getProperty("google.logfile", null);
    public static String ARBROWSERS_PASSWORD = configuration().getProperty("arbrowsers.password", "glueps");
    public static String ARBROWSERS_DEF_MAXDISTANCE = configuration().getProperty("arbrowsers.default.maxdistance", "50000");
    public static boolean ARBROWSERS_SHOWUSERS = Boolean.parseBoolean(configuration().getProperty("arbrowsers.showusers", "false"));

    //Security Constants
    public static final String JUNAIO_ADMIN_KEY = configuration().getProperty("junaio.adminkey", "gsicpruebas");
    public static final String JUNAIO_KEY = configuration().getProperty("junaio.key", "979abe582f20d3ec5d01ebed3e7363cd");
    public static final String JUNAIO_CHANNEL_ID = configuration().getProperty("junaio.channelid.geo", null);
    public static final String JUNAIO_GLUE_CHANNEL_ID = configuration().getProperty("junaio.channelid.glue", null);
    public static final String LAYAR_CHANNEL_ID = configuration().getProperty("layar.channelid.geo", null);
    public static final String LAYAR_VISION_CHANNEL_ID = configuration().getProperty("layar.channelid.vision", null);

    //GUI constants
    public static final boolean GUI_SHOWAR = Boolean.parseBoolean(configuration().getProperty("gui.showAr", "false"));


    //We use the singleton pattern for this attribute
    private static Properties config = null;
    
	// Load configuration properties
    /* Filename of the configuration properties file */
    
    private final static String CFG_FILENAME = "app.properties";
    
    public static Properties configuration() {
    	if (config == null)
    	{
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
				if (in != null) {
					in.close();
				}
		
			} catch (IOException io) {
				System.err.println("Unexpected fail while releasing properties file '" + CFG_FILENAME + "' ; trying to ignore");
			}
			
			config = configuration;
	    }
	    return config;
    }
}
