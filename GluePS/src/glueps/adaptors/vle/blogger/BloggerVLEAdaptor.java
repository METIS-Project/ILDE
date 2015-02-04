package glueps.adaptors.vle.blogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import glueps.adaptors.vle.IVLEAdaptor;
import glueps.adaptors.vle.VLEAdaptor;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.LearningEnvironment;
import glueps.core.model.LearningEnvironmentInstallation;
import glueps.core.persistence.JpaManager;
import glueps.core.persistence.entities.OauthTokenEntity;

public class BloggerVLEAdaptor implements VLEAdaptor{

	@Override
	public IVLEAdaptor getVLEAdaptor(Map<String, String> parameters) {
		String clientid = parameters.get("clientid");
		String apikey = parameters.get("apikey");
		String accessToken = parameters.get("accessToken");
		BloggerAdaptor bloggerAdaptor;
		if (accessToken!=null){
			bloggerAdaptor = new BloggerAdaptor(accessToken, clientid, apikey, parameters);
		}else{
			bloggerAdaptor = new BloggerAdaptor("", clientid, apikey, parameters);
		}
		return bloggerAdaptor;
	}

}
