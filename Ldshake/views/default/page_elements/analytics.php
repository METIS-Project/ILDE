<?php
//Pau: Google Analytics asynchronous tracking code
//If we are not running on a local machine
global $CONFIG;
if (!preg_match('/localhost/', $_SERVER['HTTP_HOST'])):
	//And we are not in a UPF computer
	$excludedIPs = array (
		'^193\.145\.56\.241$',
		'^193\.145\.56\.242$',
		'^193\.145\.56\.243$',
		'^193\.145\.56\.244$',
		'^193\.145\.39', // Last part can be anything, WiFi connections from the UPF
		'^127\.0\.0', //Localhost
        '^192\.168', //Localhost
	);
	$regexp = implode('|', $excludedIPs);
	if (!preg_match("/($regexp)/", $_SERVER['REMOTE_ADDR']))
        echo $CONFIG->google_analytics;
endif;