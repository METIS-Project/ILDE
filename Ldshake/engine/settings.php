<?php

global $CONFIG;

if (!isset($CONFIG))
	$CONFIG = new stdClass;

//Pau: Load all the config from the config file. Also, make it environment-dependent if needed.
require_once __DIR__.'/../config/config.php';

foreach ($envUrls as $env => $url)
{
	if (preg_match("/{$url}/", $_SERVER['SERVER_NAME']))
	{
		$currentEnv = $env;
		break;
	}
}

//There we have the current environment: devel, staging or prod
$CONFIG->currentEnv = $currentEnv;

//However, we can force an environment
if (isset($forcedEnv)) {
	$CONFIG->currentEnv = $forcedEnv;
}

//Let's loop through all the options. We'll check for the environment sentitive ones.
foreach ($confOptions as $k=>$v)
{
	//Is it env-dependent?
	if (is_array($v) &&
	    array_key_exists('devel', $v) && 
	    array_key_exists('staging', $v) && 
	    array_key_exists('prod', $v))
    {
    	$CONFIG->$k = $v[$CONFIG->currentEnv];	
    }
    else
    {
    	$CONFIG->$k = $v;
    }
}

//Paths are parsed separately to safely convert the DIR separator to \ if in Windows.
foreach ($confPaths as $k=>$v)
{
	//Is it env-dependent?
	if (is_array($v) &&
	    array_key_exists('devel', $v) && 
	    array_key_exists('staging', $v) && 
	    array_key_exists('prod', $v))
    {
    	$CONFIG->$k = $v[$CONFIG->currentEnv];	
    }
    else
    {
    	$CONFIG->$k = $v;
    }
	
	//For the windows thingies
	if (DIRECTORY_SEPARATOR == '\\') {
		$CONFIG->$k = str_replace('/', '\\', $CONFIG->$k);
	}
}

//echo "<pre>";
//print_r ($CONFIG);
//echo "</pre>";