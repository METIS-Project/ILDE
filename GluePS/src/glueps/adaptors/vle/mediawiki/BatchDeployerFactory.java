package glueps.adaptors.vle.mediawiki;

import java.net.URL;
import java.util.Map;

import glueps.core.gluepsManager.GLUEPSManagerApplication;

public class BatchDeployerFactory {

	private GLUEPSManagerApplication app;
	
	// Different modes of deploying
	// simple - this is the simplest mode, just creates the page structure up to activity level
	// open - this another simple mode, a page is created for each activity (with links to instanced activities) and instanced activity (with links and embedded gluelets), and all are linked from the master page
	// onepagepergroupgm - for each activity, this page creates one page per group (instanced activity). That page contains the access to the instances (in GM)
	// onepagepergroupglueps - for each activity, this page creates one page per group (instanced activity). That page contains the access to the instances (in GM)
	// onepageperactivity - for each activity, this page creates a page with the generic resources, the particular instance to show is determined on real time
	public static final String SIMPLE_MODE = "simple";
	public static final String OPEN_MODE = "open";  //This is the variant with all activities pages, open access
	public static final String OPEN2_MODE = "open2"; //This is the variant with only leaf activity pages and 2 indexes, one, per student and other for teacher
	public static final String OPEN3_MODE = "open3"; //This is the like open2, but with protected navigation pages and links to wiki instances with the name of the page (not the 'hueco')
	public static final String OPEN4_MODE = "open4"; //This is like open3, but with the ability to redeploy
	public static final String OPEN5_MODE = "open5"; //This is like open4, but with the gluelets do not appear embedded, but rather as links in the instancedactivity page
	
//	public static final String ONE_PAGE_PER_GROUP_GM = "onepagepergroupgm";
//	public static final String ONE_PAGE_PER_GROUP_GLUEPS = "onepagepergroupglueps";
//	public static final String ONE_PAGE_PER_ACTIVITY = "onepageperactivity";
	private String batchMode;

	public BatchDeployerFactory(){
		
		
	}

	public BatchDeployerFactory(GLUEPSManagerApplication applicationRest){
		
		this.app = applicationRest;
		
	}
	
	public IMWBatchDeployer getDeployer(URL wikiURL, String user, String pwd, String mode, Map<String, String> parameters){
		
		IMWBatchDeployer deployer = null;
		
		if(mode==null || mode.length()==0) return null;
		else if(mode.equals(SIMPLE_MODE)){
			//We construct the simple mode deployer and return it
			deployer = new SimpleMWBatchDeployer(wikiURL, user, pwd);
			return deployer;
		} else if(mode.equals(OPEN_MODE)){
			//We construct the simple mode deployer and return it
			deployer = new OpenMWBatchDeployer(wikiURL, user, pwd);
			return deployer;
		} else if(mode.equals(OPEN2_MODE)){
			//We construct the simple mode deployer and return it
			deployer = new OpenMWBatchDeployer2(wikiURL, user, pwd);
			return deployer;
		} else if(mode.equals(OPEN3_MODE)){
			//We construct the simple mode deployer and return it
			deployer = new OpenMWBatchDeployer3(wikiURL, user, pwd);
			return deployer;
		} else if(mode.equals(OPEN4_MODE)){
			//We construct the simple mode deployer and return it
			deployer = new OpenMWBatchDeployer4(wikiURL, user, pwd, parameters);
			return deployer;
		} else if(mode.equals(OPEN5_MODE)){
			//We construct the simple mode deployer and return it
			deployer = new OpenMWBatchDeployer5(wikiURL, user, pwd, parameters);
			return deployer;
		}
		
		return null;

	}

	public IMWBatchReDeployer getReDeployer(URL wikiURL, String user, String pwd, String mode, Map<String, String> parameters) {
		IMWBatchReDeployer deployer = null;
		
		if(mode==null || mode.length()==0) return null;
		else if(mode.equals(OPEN4_MODE)){
			//We construct the simple mode deployer and return it
			deployer = new OpenMWBatchDeployer4(wikiURL, user, pwd, parameters);
			return deployer;
		} else if(mode.equals(OPEN5_MODE)){
			//We construct the simple mode deployer and return it
			deployer = new OpenMWBatchDeployer5(wikiURL, user, pwd, parameters);
			return deployer;
		}
		
		return null;
	}
	
}
