package glueps.adaptors.vle.email;

import java.net.URL;
import java.util.Map;
import java.util.Properties;

import glueps.adaptors.vle.mediawiki.IMWBatchDeployer;
import glueps.adaptors.vle.mediawiki.OpenMWBatchDeployer;
import glueps.adaptors.vle.mediawiki.OpenMWBatchDeployer2;
import glueps.adaptors.vle.mediawiki.OpenMWBatchDeployer3;
import glueps.adaptors.vle.mediawiki.OpenMWBatchDeployer4;
import glueps.adaptors.vle.mediawiki.OpenMWBatchDeployer5;
import glueps.adaptors.vle.mediawiki.SimpleMWBatchDeployer;
import glueps.core.gluepsManager.GLUEPSManagerApplication;

public class EmailerFactory {
	
	// Different modes of deploying
	//single - single email with all the deploy is sent to all participants
	public static final String SINGLE_MODE = "single";
	//dual - one email with all the deploy is sent to staff; another with the student activities only is sent to students
	public static final String DUAL_MODE = "dual";
	//personalized - one email is sent to each participant, including only his activities; a complete one is sent to teachers
	public static final String PERSONAL_MODE = "personalized";
	private Properties properties;
	protected Map<String, String> parameters;
	
	public EmailerFactory(Properties properties, Map<String, String> parameters){
		
		this.properties = properties;
		this.parameters = parameters;
		
	}
	
	public IEmailer getEmailer(String mode){
		
		IEmailer emailer = null;
		
		if(mode==null || mode.length()==0) return null;
		else if(mode.equals(SINGLE_MODE)){
			//We construct the simple mode emailer and return it
			emailer = new SingleEmailer(getEmailTemplate(), getAppExternalUri());
			return emailer;
		}
		
		return null;
	}
	
	private String getEmailTemplate(){
		return properties.getProperty("template");
    }
	
	private String getAppExternalUri(){
		   return parameters.get("appExternalUri");
	}
}
