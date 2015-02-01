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
	
	
}
