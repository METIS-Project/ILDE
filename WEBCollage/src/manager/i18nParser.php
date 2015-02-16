<?php

/*Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
Valladolid, Spain. https://www.gsic.uva.es/

This file is part of Web Collage.

Web Collage is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or 
(at your option) any later version.

Web Collage is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

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
        if (isset($_GET['lang'])){
            //admit lang parameter from ldshake
            $l = $_GET['lang'];
        }else if (isset($_GET['l'])){
            $l = $_GET['l'];
        }
	if (isset($l)) {
		if ($l == "es" || $l == "en" || $l == "ca") {
			setcookie("l", $l);
			return $l;
		}
	} else if (isset($_COOKIE['l'])) {
		$l = $_COOKIE['l'];
		if ($l == "es" || $l == "en" || $l == "ca") {
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
