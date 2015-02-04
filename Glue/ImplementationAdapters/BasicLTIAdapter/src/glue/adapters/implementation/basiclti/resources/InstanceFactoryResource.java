package glue.adapters.implementation.basiclti.resources;

import java.util.List;
import java.util.Map;

import org.restlet.data.Reference;

/**
 * Resource instance factory, responsible for the creation of Gluelet instances corresponding to a BasicLTI.
 * 
 * @author  	Javier Enrique Hoyos Torio
 * @version 	2012092501
 * @package 	glue.adapters.implementation.basiclti.resources
 */
public class InstanceFactoryResource extends glue.common.resources.InstanceFactoryResource{

	@Override
	protected String checkMissingParameters(String toolName,List<String> users, String callerUser, Map<String, String> specificParams) {
		String result = "";
		if (toolName == null)
			result += "toolName, ";
		if (callerUser == null)
			result += "callerUser, ";
		/*if (users == null || users.size() <= 0)
			result += "users, ";*/
		if (!specificParams.containsKey("key"))
			result += "key, ";
		if (!specificParams.containsKey("pass"))
			result += "pass, ";
		if (result.length() > 0) {
			result = result.substring(0, result.length() - 2);
			return result;
		}
		
		//We need to provide the reference URL to the create method
		Reference ref = this.getReference();
		String refSt = ref.getIdentifier();
		specificParams.put("refURL", refSt.substring(0, refSt.indexOf(ref.getLastSegment())));
		
		return null;
	}

}
