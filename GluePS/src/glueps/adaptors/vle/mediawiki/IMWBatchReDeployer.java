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

public interface IMWBatchReDeployer {

	/**
	 * Translates and deploys a (GLUE!-PS lingua franca) deployment data structure, 
	 * that had already been deployed to the LE, to the same LE, updating the activity
	 * structure, groups, etc
	 * @param lfdeploy The GLUEPS deploy structure to be deployed (in lingua franca, Java objects)
	 * @return The GLUEPS deployment structure, with the deployURL parameter correctly set
	 * @throws Exception 
	 */
	public Deploy batchReDeploy(Deploy lfdeploy) throws Exception;

}
