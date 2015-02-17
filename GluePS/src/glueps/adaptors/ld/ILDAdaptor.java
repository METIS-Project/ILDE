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

package glueps.adaptors.ld;

import java.util.HashMap;

import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.model.Participant;

/**
 * @author User
 * This is the interface that all LD adaptors have to comply with, in order to translate from the original LD model to the lingua franca (GLUEPS) model
 */
public interface ILDAdaptor {

	
	/**
	 * Gets a design in a foreign LD model (stored in a file in the disk) and translates it to GLUEPS's LF 
	 * @param filepath The path to the disk file containing the original design in a LD language/model
	 * @return A glueps.core.model.Design with the LF design
	 */
	public Design fromLDToLF(String filepath);
	
	/**
	 * Gets an expression of instantiation data in a foreign LD model (stored in a file in the disk) and translates it to a Deploy in GLUEPS's LF 
	 * @param filepath The path to the disk file containing the original design in a LD language/model
	 * @param design A glueps.core.model.Design with the LF design of which the aforementioned instantiation data is a particularization
	 * @param vleUsers A hashmap containing the glueps.core.model.Participant users of the course where the design is going to be deployed
	 * @return A glueps.core.model.Deploy with the particularized LF design (i.e. the deploy)
	 */
	public Deploy processInstantiation(String filepath, Design design, HashMap<String,Participant> vleUsers);
	
}
