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
