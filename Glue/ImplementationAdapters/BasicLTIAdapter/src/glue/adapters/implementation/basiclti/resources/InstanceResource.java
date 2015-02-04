package glue.adapters.implementation.basiclti.resources;

import java.util.List;
import java.util.Map;

/**
 * Resource adapter instance
 * 
 * @author  	Javier Enrique Hoyos Torio
 * @version 	2012092501
 * @package 	glue.adapters.implementation.basiclti.resources
 */
public class InstanceResource extends glue.common.resources.InstanceResource {

	@Override
	protected String checkMissingParametersInDelete(
			Map<String, String> specificParams) {
		String result = "";
		if (!specificParams.containsKey("instanceid"))
			result += "instanceid, ";
		if (result.length() > 0) {
			result = result.substring(0, result.length() - 2);
			return result;
		}
		return null;
	}

	@Override
	protected String checkMissingParametersInPost(String callerUser,
			List<String> users, Map<String, String> specificParams) {
		// TODO Auto-generated method stub
		return null;
	}

}
