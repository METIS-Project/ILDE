<?php
//Pau: Google Analytics asynchronous tracking code
//If we are not running on a local machine
global $CONFIG;
if (!empty($CONFIG->google_analytics) and !preg_match('/localhost/', $_SERVER['HTTP_HOST'])):
	//And we are not in a UPF computer
	$excludedIPs = array (
		'^127\.0\.0', //Localhost
        '^192\.168', //Localhost
	);
	$regexp = implode('|', $excludedIPs);
	if (!preg_match("/($regexp)/", $_SERVER['REMOTE_ADDR']))
        echo $CONFIG->google_analytics;
endif;