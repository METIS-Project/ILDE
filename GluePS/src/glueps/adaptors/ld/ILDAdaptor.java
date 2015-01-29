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
