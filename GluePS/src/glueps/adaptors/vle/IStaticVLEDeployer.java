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
