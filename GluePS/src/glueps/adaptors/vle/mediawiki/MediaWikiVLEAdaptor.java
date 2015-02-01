package glueps.adaptors.vle.mediawiki;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import glueps.adaptors.vle.IVLEAdaptor;
import glueps.adaptors.vle.VLEAdaptor;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.LearningEnvironment;

public class MediaWikiVLEAdaptor implements VLEAdaptor{

	private static Properties configuration = null;
	
	public MediaWikiVLEAdaptor(){
		if (configuration == null){
			String fileName = getClass().getSimpleName().toLowerCase();
			Properties properties = new Properties();
			FileInputStream in = null;
			try {
				in = new FileInputStream("conf/" + fileName + ".properties");
				properties.load(in);
			} catch (FileNotFoundException e) {
				System.err.println("Properties file '" + fileName + ".properties" + "' couldn't be found; default properties will be applied");
			}catch (IOException e) {
				System.err.println("Properties file '" + fileName + ".properties" + "' couldn't be found; default properties will be applied");
			}
	    	try {
	    		in.close();
	    	} catch (IOException io) {
	    		System.err.println("Unexpected fail while releasing properties file '" + fileName + ".properties" + "' ; trying to ignore");
	    	}
	    	configuration = properties;
		}
	}
	
	@Override
	public IVLEAdaptor getVLEAdaptor(GLUEPSManagerApplication applicationRest, Map<String, String> parameters) {
		MediaWikiAdaptor mwAdaptor;
		//We construct the MediaWikiAdaptor	
    	String creduser = parameters.get("creduser");
    	String credsecret = parameters.get("credsecret");
		if (creduser==null || credsecret==null)
		{
			//In this case, we use the default user and password as defined in the app.properties file
			mwAdaptor = new MediaWikiAdaptor(applicationRest, configuration);
		}
		else{
			mwAdaptor = new MediaWikiAdaptor(applicationRest, creduser, credsecret, configuration);
		}
		
		return mwAdaptor;
	}

}
