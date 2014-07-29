package glueps.adaptors.vle.moodle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import glueps.adaptors.vle.IVLEAdaptor;
import glueps.adaptors.vle.VLEAdaptor;
import glueps.adaptors.vle.moodledyn.MoodleDynAdaptor;
import glueps.adaptors.vle.moodledyn.MoodleDynAdaptor21v;
import glueps.adaptors.vle.moodledyn.MoodleDynAdaptor21vSelenium;
import glueps.adaptors.vle.moodledyn.MoodleDynAdaptor23vSelenium;
import glueps.adaptors.vle.moodledyn.MoodleDynAdaptor25vSelenium;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.LearningEnvironment;
import glueps.core.model.LearningEnvironmentInstallation;
import glueps.core.persistence.JpaManager;

public class MoodleVLEAdaptor implements VLEAdaptor{
	
	//selective deployment adaptor, deploying always to one Moodle topic
	private static final String MOODLE_BATCHMODE_ONETOPIC = "onetopic";
	//non-selective mode, it deploys to as many topics as the first multiple branch in the activity tree
	private static final String MOODLE_BATCHMODE_NORMAL = "normal";
	//dynamic mode (like the normal mode, but tries to deploy it live)
	private static final String MOODLE_BATCHMODE_DYNAMIC = "dynamic";
	//dynamic mode using selenium
	private static final String MOODLE_BATCHMODE_SELENIUM = "selenium";
	
	private String UPLOAD_DIRECTORY;
	private String tmpDir;
	private String templateDir;
	
	private static Properties configuration = null;
	
	public MoodleVLEAdaptor(){
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
	public IVLEAdaptor getVLEAdaptor(Map<String, String> parameters) {
		templateDir = parameters.get("appPath") + File.separator + "templates" + File.separator;
		UPLOAD_DIRECTORY = parameters.get("appPath") + "/uploaded/";
		tmpDir = UPLOAD_DIRECTORY + File.separator+"temp" + File.separator+"zips" + File.separator;
		
    	String propertyBatchDeployMode = configuration.getProperty("batchdeploymode", "normal");
    	String creduser = parameters.get("creduser");
    	String credsecret = parameters.get("credsecret");
    	String accessLocation = parameters.get("accessLocation");
		String version = parameters.get("version");
		String batchdeploymode = parameters.get("batchdeploymode");
		if(version!=null && (version.equals("2.5") || version.equals("2.3") || version.equals("2.2") || version.equals("2.1") || version.equals("2.0"))){
			String wstoken = parameters.get("wstoken");
			//We construct the MoodleAdaptor
			MoodleAdaptor mooAdaptor = null;
			if ((propertyBatchDeployMode.equals(MOODLE_BATCHMODE_NORMAL) && batchdeploymode==null) || (batchdeploymode!=null && batchdeploymode.equals(MOODLE_BATCHMODE_NORMAL))){
				mooAdaptor = new MoodleAdaptor21v(UPLOAD_DIRECTORY ,this.templateDir+"moodlebackup.xml", "glueps.adaptors.vle.moodle.model","moodle.xml",this.tmpDir, accessLocation, creduser, credsecret, wstoken, parameters);
			}else if ((propertyBatchDeployMode.equals(MOODLE_BATCHMODE_DYNAMIC )&& batchdeploymode==null) || (batchdeploymode!=null && batchdeploymode.equals(MOODLE_BATCHMODE_DYNAMIC))){
				mooAdaptor = new MoodleDynAdaptor21v(UPLOAD_DIRECTORY ,this.templateDir+"moodlebackup.xml", "glueps.adaptors.vle.moodle.model","moodle.xml",this.tmpDir, accessLocation, creduser, credsecret, wstoken, parameters);
			}else if ((propertyBatchDeployMode.equals(MOODLE_BATCHMODE_SELENIUM)&& batchdeploymode==null) || (batchdeploymode!=null && batchdeploymode.equals(MOODLE_BATCHMODE_SELENIUM))){
				if (version.equals("2.5")){					
					mooAdaptor = new MoodleDynAdaptor25vSelenium(UPLOAD_DIRECTORY ,this.templateDir+"moodlebackup.xml", "glueps.adaptors.vle.moodle.model","moodle.xml",this.tmpDir, accessLocation, creduser, credsecret, parameters);
				}else if (version.equals("2.3")){
					mooAdaptor = new MoodleDynAdaptor23vSelenium(UPLOAD_DIRECTORY ,this.templateDir+"moodlebackup.xml", "glueps.adaptors.vle.moodle.model","moodle.xml",this.tmpDir, accessLocation, creduser, credsecret, parameters);
				}else{
					mooAdaptor = new MoodleDynAdaptor21vSelenium(UPLOAD_DIRECTORY ,this.templateDir+"moodlebackup.xml", "glueps.adaptors.vle.moodle.model","moodle.xml",this.tmpDir, accessLocation, creduser, credsecret, parameters);
				}
			}
			mooAdaptor.setTemplateDir(templateDir);
			return mooAdaptor;
		}else if(propertyBatchDeployMode.equals(MOODLE_BATCHMODE_NORMAL)){
			//We construct the MoodleAdaptor
			MoodleAdaptor mooAdaptor = new MoodleAdaptor(UPLOAD_DIRECTORY ,this.templateDir+"moodlebackup.xml", "glueps.adaptors.vle.moodle.model","moodle.xml",this.tmpDir, accessLocation, creduser, credsecret, parameters);
			mooAdaptor.setTemplateDir(templateDir);
			return mooAdaptor;
		}else if(propertyBatchDeployMode.equals(MOODLE_BATCHMODE_ONETOPIC)){
			//We construct the MoodleAdaptor
			MoodleAdaptorSelectiveOneTopic mooAdaptor = new MoodleAdaptorSelectiveOneTopic(UPLOAD_DIRECTORY ,this.templateDir+"moodlebackup.xml","glueps.adaptors.vle.moodle.model","moodle.xml",this.tmpDir, accessLocation, creduser, credsecret, parameters);
			mooAdaptor.setTemplateDir(templateDir);
			return mooAdaptor;
		}else if(propertyBatchDeployMode.equals(MOODLE_BATCHMODE_DYNAMIC)){
			MoodleDynAdaptor mooAdaptor = new MoodleDynAdaptor(UPLOAD_DIRECTORY ,this.templateDir+"moodlebackup.xml","glueps.adaptors.vle.moodle.model","moodle.xml",this.tmpDir, accessLocation, creduser, credsecret, parameters);
			mooAdaptor.setTemplateDir(templateDir);
			return mooAdaptor;
		}else return null;
	}

}
