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
