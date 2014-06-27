package glueps.adaptors.vle.email;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import glueps.adaptors.vle.IVLEAdaptor;
import glueps.adaptors.vle.VLEAdaptor;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.LearningEnvironment;

public class EmailVLEAdaptor implements VLEAdaptor{
	
	private static Properties configuration = null;
	
	public EmailVLEAdaptor(){
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
		EmailAdaptor emailAdaptor = new EmailAdaptor(applicationRest, configuration);
		return emailAdaptor;
	}

}
