/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of GlueletCommon.
 * 
 * GlueletCommon is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * GlueletCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.common.entities.configuration;


import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

public class BasicConfigurationRepository implements ConfigurationRepository {

	/** Configuration form files associated with tool implementations */	
	protected ConcurrentHashMap<String, ConcurrentHashMap<String, Configuration>> associatedConfigurations;
	
	/**
	 * Constructor
	 * 
	 * @param tools  
	 * @param ids 
	 * @param fileNames
	 */
	public BasicConfigurationRepository(String [] tools, String [] ids, String [] fileNames) {
		HashMap<String, Configuration> configurations = new HashMap<String, Configuration>();
		associatedConfigurations = new ConcurrentHashMap<String, ConcurrentHashMap<String, Configuration>>();
		int length =  Math.min( Math.min(tools.length, ids.length), fileNames.length);
		ConcurrentHashMap<String, Configuration> toolConfigs; 
		Configuration cfg;
		for (int i=0; i<length; i++) {
			toolConfigs = associatedConfigurations.get(tools[i]);
			if (toolConfigs == null) {
				toolConfigs = new ConcurrentHashMap<String, Configuration>();
				associatedConfigurations.put(tools[i], toolConfigs);
			}
			cfg = configurations.get(fileNames[i]);
			if (cfg == null) {
				cfg = new Configuration(fileNames[i]);
				configurations.put(tools[i], cfg);
			}
			toolConfigs.put(ids[i], cfg);
		}
		
	}

	/**
	 * Getter for a configuration form file.
	 * 
	 * @param	toolImplementationName
	 * @param	configurationId
	 * @return Configuration object corresponding to the tool and id
	 */
	//@Override
	public Configuration getConfiguration(String toolImplementationName, String configurationId) throws ResourceException {
		Configuration result = null;
		ConcurrentMap<String, Configuration> map = associatedConfigurations.get(toolImplementationName);
		if (map == null)
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Unknown tool '" + toolImplementationName + "'");
		
		if (configurationId == null || configurationId.length() == 0) {
			// default configuration
			String firstKey = map.keySet().iterator().next();	// the first one
			result = map.get(firstKey);
		} else {
			// required configuration
			result = map.get(configurationId); 
		}
		return result;
	}
	

}
