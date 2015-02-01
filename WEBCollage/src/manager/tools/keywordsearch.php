<?php

include_once ("../JSON.php");

$result = array();

$petition = $_REQUEST['petition'];
$keywords = $_REQUEST['keywords'];

$url = "http://www.gsic.uva.es/ontoolsearch/search?q=" . urlencode($keywords);

$xml = simplexml_load_file($url);

$result = array();

foreach ($xml->children() as $key => $object) {
	if ($object->getName() == "entry") {
		$result[] = array("url" => (string) $object->link['href'],
						"description" => array("title" => (string)$object->title,
											"summary" => (string)$object->summary));
	}
}


$json = new Services_JSON();
echo $json->encode(array("petition" => $petition, "list" => $result));



?>