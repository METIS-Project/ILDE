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

import glueps.core.model.Deploy;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

public interface IStaticVLEDeployer extends IVLEAdaptor {

	/**
	 * Gets a file with the static form of a certain glueps.core.model.Deploy, to be deployed "manually" in the LE (e.g. a course backup). This requires that such a file has been previously generated, using the generateStaticDeploy() method
	 * @return The file where the static form of the deployment is located
	 */
	public File getStaticDeploy();
	
	/**
	 * Translates and generates a static, LE-specific representation of the deployment (e.g. a Moodle backup file), storing it in a File somewhere 
	 * @param lfdeploy The GLUEPS deploy structure (in lingua franca, Java objects)
	 * @return The file where the static form of the deployment is located
	 * @throws JAXBException For problems translating from XML to Java, and viceversa
	 * @throws IOException For file manipulation errors
	 */
	public File generateStaticDeploy(Deploy lfdeploy) throws JAXBException, IOException;
	
	
}
