package glueps.adaptors.vle.email;

import glueps.core.model.Deploy;

public interface IEmailer {

	/**
	 * Translates and deploys a (GLUE!-PS lingua franca) deployment data structure, by sending a batch of emails to the specified email server
	 * @param lfdeploy The GLUEPS deploy structure to be deployed (in lingua franca, Java objects)
	 * @return The GLUEPS deployment structure, with the deployURL parameter correctly set (since we do not have a proper LE, it is set to the GLUEPS own UI)
	 * @throws Exception 
	 */
	public Deploy batchEmail(Deploy lfdeploy) throws Exception;
	
	
}
