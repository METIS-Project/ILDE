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

//LdShake mode
if (isset($ldshake_mode)) {
    $CONFIG->ldshake_mode = $ldshake_mode;
} else {
    $CONFIG->ldshake_mode = "ilde";
}

if (isset($ldshake_submode)) {
    $CONFIG->ldshake_submode = $ldshake_submode;
}

//ILDE debug mode
if (isset($debug)) {
    $CONFIG->debug = $debug;
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

if(isset($vle_key))
    $CONFIG->vle_key = $vle_key;
$CONFIG->google_drive = $google_drive;
$CONFIG->disable_contextual_help = $disable_contextual_help;
$CONFIG->editor_debug = $editor_debug;
$CONFIG->moodle = $moodle;

if(isset($glueps_url)) {
    $CONFIG->glueps_url = $glueps_url;
    $CONFIG->glueps_password = $glueps_password;
}

if(isset($webcollagerest_url) && isset($webcollagerest_password)) {
    $CONFIG->webcollagerest_url = $webcollagerest_url;
    $CONFIG->webcollagerest_password = $webcollagerest_password;
}

if(isset($exelearningrest_url) && isset($exelearningrest_password)) {
    $CONFIG->exelearningrest_url = $exelearningrest_url;
    $CONFIG->exelearningrest_password = $exelearningrest_password;
}

if(isset($google_analytics))
    $CONFIG->google_analytics = $google_analytics;

$CONFIG->editors_content = $CONFIG->dataroot;
if (!file_exists($CONFIG->editors_content . 'content/webcollagerest')) {
    mkdir($CONFIG->editors_content . 'content/webcollagerest', 0755, true);
}