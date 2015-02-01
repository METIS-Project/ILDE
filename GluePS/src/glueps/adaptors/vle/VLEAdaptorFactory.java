package glueps.adaptors.vle;

import java.util.HashMap;

import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.LearningEnvironment;
import glueps.core.model.LearningEnvironmentInstallation;
import glueps.core.persistence.JpaManager;
import glueps.core.persistence.entities.OauthTokenEntity;

public final class VLEAdaptorFactory {

	private GLUEPSManagerApplication app;

	
	public VLEAdaptorFactory(GLUEPSManagerApplication applicationRest){
		this.app = applicationRest;
	}
	
	public IVLEAdaptor getVLEAdaptor(LearningEnvironment le) {
		if (le == null || le.getType() == null)
			return null;
		JpaManager dbmanager = JpaManager.getInstance();
		HashMap<String, String> params = new HashMap<String, String>();
		if (le.getParameters()!=null){//get the parameters from the le. They are usually provided by ldshake
			params.putAll(le.decodeParams());
		}
		if (le.getInstallation()!=null){ //if the request comes from ldshake there is not an installation
			LearningEnvironmentInstallation lei = dbmanager.findLEInstObjectById(le.getInstallation());
			if (lei!=null){
				params.putAll(lei.decodeParams());
				if (lei.getSectype()==1){//user and password
					params.put("creduser", le.getCreduser());
					params.put("credsecret", le.getCredsecret());
				}else if (lei.getSectype()==2){//oauth access token
					OauthTokenEntity oauthToken= dbmanager.findOauthTokenByLeid(le.getId());
					params.put("accessToken", oauthToken.getAccessToken());
				}
			}
		}
		//we assume by now that the sectype is 1 (user and password)
		if (le.getCreduser()!=null){
			params.put("creduser", le.getCreduser());
		}
		if (le.getCredsecret()!=null){
			params.put("credsecret", le.getCredsecret());
		}
		params.put("accessLocation", le.getAccessLocation().toString());
		VLEAdaptor adapter = null;
		try {
			//Load at runtime the VLE adapter class for that type of VLE
			Class c = Class.forName("glueps.adaptors.vle." + le.getType().toLowerCase() + "." + le.getType() + "VLEAdaptor");
			adapter = (VLEAdaptor) c.newInstance();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			return null;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return adapter.getVLEAdaptor(this.app, params);
	}

}
