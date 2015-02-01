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
