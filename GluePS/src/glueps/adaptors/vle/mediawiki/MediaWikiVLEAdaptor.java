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
	public IVLEAdaptor getVLEAdaptor(Map<String, String> parameters) {
		MediaWikiAdaptor mwAdaptor;
		//We construct the MediaWikiAdaptor	
    	String creduser = parameters.get("creduser");
    	String credsecret = parameters.get("credsecret");
		if (creduser==null || credsecret==null)
		{
			//In this case, we use the default user and password as defined in the app.properties file
			mwAdaptor = new MediaWikiAdaptor(configuration, parameters);
		}
		else{
			mwAdaptor = new MediaWikiAdaptor(creduser, credsecret, configuration, parameters);
		}
		
		return mwAdaptor;
	}

}
