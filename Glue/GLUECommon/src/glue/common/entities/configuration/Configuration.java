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

import glue.common.entities.configuration.Configuration;

import java.io.InputStream;

/**
 * Entity class for accesing configuration form data in the file system. 
 *
 * Though extremely simple, this class will remain here until every REST call concerning configuration forms has been implemented; more complex interactions are expected. 
 * 
 * @author  David A. Velasco
 * @version 2012092501
 * @package glue.common.entities.configuration
 */

public class Configuration {
	
	/// attributes ///
	
	/** Name of the file where the configuration form is defined */
	protected String filename;
	
	
	/// methods /// 
	
	/** 
	 * Constructor 
	 */
	public Configuration (String filename) {
		this.filename = filename;
	}

	
	/* 
	 * Getter method for obtaining the java.io.File object corresponding to the configuration form.
	 * 
	 * TO DEPRECATE
	 *-/
	public File getAsFile() {
		if (isEmpty())
			return null;
		else 
			return new File(filename);
	}
	*/
	
	/** 
	 * Getter method for obtaining an java.io.InputStream object corresponding to the configuration form.
	 */
	public InputStream getAsStream() {
		if (isEmpty())
			return null;
		else
			return ClassLoader.getSystemClassLoader().getResourceAsStream(filename);
	}
	
	
	/**
	 * Check of configuration is empty - specific data are not needed to create an instance. 
	 * 
	 * @return	'True' when no file name is contained in the object, 'false' in other case.
	 */
	public boolean isEmpty() {
		return (filename == null || filename.length() == 0);
	}
	
}
