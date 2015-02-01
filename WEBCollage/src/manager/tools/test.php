<?php

include_once ("../JSON.php");

$xml = simplexml_load_file("http://www.gsic.uva.es/ontoolsearch/tools/grove");

$name = (string)$xml->author->name;
$summary = (string)$xml->entry->summary;
$title = (string)$xml->entry->title;

$result = array("name" => $name, "title" => $title, "summary" => $summary);

$json = new Services_JSON();
echo $json->encode($result);

?>