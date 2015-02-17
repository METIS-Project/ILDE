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

import glueps.core.model.Deploy;

public interface IMWBatchDeployer {

	/**
	 * Translates and deploys a (GLUE!-PS lingua franca) deployment data structure, directly to the indicated LE
	 * @param lfdeploy The GLUEPS deploy structure to be deployed (in lingua franca, Java objects)
	 * @return The GLUEPS deployment structure, with the deployURL parameter correctly set
	 * @throws Exception 
	 */
	public Deploy batchDeploy(Deploy lfdeploy) throws Exception;
	
	/**
	 * Undeploys a (GLUE!-PS lingua franca) deployment data structure, deleting it from the indicated LE
	 * @param lfdeploy The GLUEPS deploy structure to be undeployed (in lingua franca, Java objects)
	 * @return The GLUEPS deployment structure, with the deployURL parameter correctly set to NULL
	 * @throws Exception 
	 */
	public Deploy batchUndeploy(Deploy lfdeploy) throws Exception;

}
