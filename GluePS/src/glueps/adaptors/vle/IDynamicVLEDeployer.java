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

public interface IDynamicVLEDeployer extends IVLEAdaptor {


	/**
	 * Translates and deploys a (GLUE!-PS lingua franca) deployment data structure, directly to the indicated LE
	 * @param baseUri The base URI of the LE (upon which the deployment calls are calculated) 
	 * @param lfdeploy The GLUEPS deploy structure to be deployed (in lingua franca, Java objects)
	 * @return The GLUEPS deployment structure, with the deployURL parameter correctly set
	 */
	public Deploy deploy(String baseUri, Deploy lfdeploy);
	
	/**
	 * Undeploys a (GLUE!-PS lingua franca) deployment data structure that has already been transferred to the LE
	 * @param baseUri The base URI of the LE (upon which the deployment calls are calculated) 
	 * @param lfdeploy The GLUEPS deploy structure to be undeployed (in lingua franca, Java objects)
	 * @return The GLUEPS deployment structure, with the deployURL parameter correctly set to NULL
	 */
	public Deploy undeploy(String baseUri, Deploy lfdeploy);

	/**
	 * Deploys a (GLUE!-PS lingua franca) deployment data structure that has already been transferred to the LE. 
	 * For now, it just generates a deploy in a similar manner, not caring about existing modifications in the activity pages
	 * @param baseUri The base URI of the LE (upon which the deployment calls are calculated) 
	 * @param lfdeploy The GLUEPS deploy structure to be undeployed (in lingua franca, Java objects)
	 * @return The GLUEPS deployment structure, with the deployURL parameter correctly set to NULL
	 */
	public Deploy redeploy(String string, Deploy newDeploy);
	
	/**
	 * The deploy containing the current learningEnvironment info can be deployed into the VLE
	 * @param baseUri The base URI of the LE (upon which the deployment calls are calculated) 
	 * @param lfdeploy The GLUEPS deploy structure to be deployed (in lingua franca, Java objects)
	 * @return The deploy can be deployed into the course 
	 */
	public boolean canBeDeployed(String baseUri, Deploy lfdeploy);
	
	
}
