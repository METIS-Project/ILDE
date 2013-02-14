<?php

/**
 * Funciones para la internacionalización de la herramienta
 */

function getJavascriptMessages() {
	$output = '<script language = "Javascript">';
	$output .= 'i18n.messages = {';
	$messages = getMessages();
	$comma = false;
	foreach ($messages as $key => $value) {
		if ($comma) {
			$output .= ",";
		} else {
			$comma = true;
		}
		$output .= "\"$key\": \"$value\"";
	}

	$output .= "};</script>";

	return $output;
}

function i18nGetLocale() {
	if (isset($_GET['l'])) {
		$l = $_GET['l'];
		if ($l == "es" || $l == "en") {
			setcookie("l", $l);
			return $l;
		}
	} else if (isset($_COOKIE['l'])) {
		$l = $_COOKIE['l'];
		if ($l == "es" || $l == "en") {
			return $l;
		}
	}

	return "en";
}

function getMessages() {
	global $locale;
	static $messages = null;
	$language = $locale;

	if ($messages == null) {
		$messages = array();
		$messagesFolder = "i18n/{$language}/";

		$dh = opendir($messagesFolder);
		while (false !== ($filename = readdir($dh))) {
			if (strlen($filename) > 0 && $filename[0] != '.') {
				$fullpath = $messagesFolder . $filename;
				$handle = fopen($fullpath, "r");
				if ($handle) {
					while (!feof($handle)) {
						$line = fgets($handle);
						$pos = strpos($line, "=");
						if ($pos > 0) {
							$key = substr($line, 0, $pos);
							$value = trim(substr($line, $pos + 1), "\r\n");
							$messages[$key] = $value;
						}
					}
					fclose($handle);
				}
			}
		}
	}

	return $messages;
}

function parseFunction($call) {
	eval('$returnValue = ' . $call . ';');
	return parseText($returnValue);
}

function parseText($text) {
	$values = getMessages();
	while (($pos = strpos($text, '${')) !== FALSE) {
		$keypos = $pos + 2;
		$endpos = strpos($text, '}', $keypos);
		if ($endpos > $keypos) {
			$key = substr($text, $keypos, $endpos - $keypos);
			if (strpos($key, "(") === FALSE) {
				$value = $values[$key];
			} else {
				$value = parseFunction($key);
			}
			$text = substr_replace($text, $value, $pos, $endpos - $pos + 1);
		} else {
			break;
		}
	}

	return $text;
}

function parseContentFile($file) {
	$result = "";
	$handle = fopen($file, "r");

	if ($handle) {
		while (!feof($handle)) {
			$line = fgets($handle);
			$result .= parseText($line);
		}
		fclose($handle);
	}
	return $result;
}

$locale = i18nGetLocale();
?>
