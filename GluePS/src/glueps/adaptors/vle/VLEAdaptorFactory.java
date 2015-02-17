/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Glue!PS.
 * 
 * Glue!PS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Glue!PS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
		
		//get the parameters from the le. They are usually provided by ldshake
		if (le.getParameters()!=null){
			params.putAll(le.decodeParams());
		}
		
		//get the parameters from the le installation.
		LearningEnvironmentInstallation lei;
		if (le.getInstallation()!=null){
			lei = dbmanager.findLEInstObjectById(le.getInstallation());
		}else{
			//If it is an old deploy it could not include the installation id
			LearningEnvironment leDB = dbmanager.findLEObjectById(le.getId());
			if (leDB!=null && leDB.getInstallation()!=null){
				lei = dbmanager.findLEInstObjectById(leDB.getInstallation());
			}else{
				//if the request comes from ldshake there is not an installation
				lei = null;
			}
		}
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
		//we assume by now that the sectype is 1 (user and password)
		if (le.getCreduser()!=null){
			params.put("creduser", le.getCreduser());
		}
		if (le.getCredsecret()!=null){
			params.put("credsecret", le.getCredsecret());
		}
		params.put("accessLocation", le.getAccessLocation().toString());
		//Add the additional parameters that are necessary for some vle adaptors
		params.put("startingSection", GLUEPSManagerApplication.STARTING_SECTION_FIELD);
		params.put("appExternalUri", app.getAppExternalUri());
		params.put("ldshakeMode", String.valueOf(app.getLdShakeMode()));
		params.put("gpresGmType", app.getGPresGMType());
		params.put("gpres3GmType", app.getGPres3GMType());
		params.put("webcGmType", app.getWebCGMType());
		params.put("moodleRealTimeGlueps", String.valueOf(app.isMoodleRealTimeGlueps()));
		params.put("appPath", app.getAppPath());
		
		VLEAdaptor adapter = null;
		try {
			//Load at runtime the VLE adapter class for that type of VLE
			Class c = Class.forName("glueps.adaptors.vle." + le.getType().toLowerCase() + "." + le.getType() + "VLEAdaptor");
			adapter = (VLEAdaptor) c.newInstance();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			return null;
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		return adapter.getVLEAdaptor(params);
	}

}
