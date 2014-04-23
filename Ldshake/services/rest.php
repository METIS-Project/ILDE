<?php
	/**
	 * Rest endpoint.
	 * The API REST endpoint.
	 * 
	 * @package Elgg
	 * @subpackage API
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd <info@elgg.com>
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.org/
	 */

	/**
	 *  Start the Elgg engine
	 */
    global $rest_services;
    $rest_services = true;
	require_once("../engine/start.php");

    require_once __DIR__.'/../mod/lds/rest.php';
    require_once __DIR__ . '/../vendors/httpful/bootstrap.php';

    header_remove("Cookie");
    header_remove("Set-Cookie");

	global $CONFIG;

	// Register the error handler
	error_reporting(E_ALL); 
	set_error_handler('__php_api_error_handler');
	
	// Register a default exception handler
	set_exception_handler('__php_api_exception_handler'); 
	
	// Check to see if the api is available
	if ((isset($CONFIG->disable_api)) && ($CONFIG->disable_api == true))
		throw new SecurityException(elgg_echo('SecurityException:APIAccessDenied'));

	// Register some default PAM methods, plugins can add their own
	register_pam_handler('pam_auth_usertoken', 'required'); // Either token present and valid OR method doesn't require one.

/*
    $path = explode('/', $_SERVER['PATH_INFO']);

    if (!count($path))
        throw new SecurityException(elgg_echo('SecurityException:APIAccessDenied'));

    $query = array();
    foreach($path as $ep)
        if(strlen($ep))
            $query[] = $ep;
*/

    global $API_QUERY;
    $query = ldshake_rest_params();
    $API_QUERY = $query;
	// Get parameter variables
	$method = $query[0];
    set_input('method', $method);
    set_input('view', 'xml');
	$result = null;

    $headers = getallheaders();
    if(isset($headers['Authorization'])) {
        $auth_header = $headers['Authorization'];
        $auth_values = explode(' ', $auth_header);
        $auth_token = $auth_values[1];
        set_input('auth_token', $auth_token);
    }

	// Authenticate session
	if (pam_authenticate())
	{
		// Authenticated somehow, now execute.
		$token = "";		
		$params = get_parameters_for_method($method); // Use $CONFIG->input instead of $_REQUEST since this is called by the pagehandler
		if (isset($params['auth_token'])) $token = $params['auth_token'];

        $params['query'] = $query;

		$result = execute_method($method, $params, $token);
	}
	else
		throw new SecurityException(elgg_echo('SecurityException:NoAuthMethods'));
	
	// Finally output
	if (!($result instanceof GenericResult))
		throw new APIException(elgg_echo('APIException:ApiResultUnknown'));

	// Output the result
	page_draw($method, elgg_view("api/output", array("result" => $result)));
	
?>