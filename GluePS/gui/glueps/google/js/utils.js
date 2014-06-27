/**
 * Some helper functions
 */

/**
 * Gets the parameter value from a URL
 * @param url The URL which contains the parameter
 * @param name The parameter name
 * @return The parameter value
 */
function gup (url, name) {
	name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
	var regexS = "[\\?&]" + name + "=([^&#]*)";
	var regex = new RegExp(regexS);
	var results = regex.exec(url);
	if (results == null)
		return "";
	else
		return results[1];
}

/**
 * Generate a random and unique identifier
 * @return The identifier generated
 */
function generateRandomUid(){
	var max = 1000;
	var min = 1;
	var random = Math.floor(Math.random() * (max-min+1)) + min;
	var time = new Date().getTime();
	return time + "_" + random;
}

/**
 * Function that returns if the navigator is a mobile device such as Iphone, Android and so on
 * @returns {Boolean}
 */
function navigatorIsMobileDevice(){
	var device = navigator.userAgent;
	if (device.match(/Iphone/i)|| device.match(/Ipod/i)|| device.match(/Android/i)|| device.match(/J2ME/i)|| device.match(/BlackBerry/i)|| device.match(/iPhone|iPad|iPod/i)|| device.match(/Opera Mini/i)|| device.match(/IEMobile/i)|| device.match(/Mobile/i)|| device.match(/Windows Phone/i)|| device.match(/windows mobile/i)|| device.match(/windows ce/i)|| device.match(/webOS/i)|| device.match(/palm/i)|| device.match(/bada/i)|| device.match(/series60/i)|| device.match(/nokia/i)|| device.match(/symbian/i)|| device.match(/HTC/i))
	{ 
		return true;
	}
	else
	{
		return false;
	}
}