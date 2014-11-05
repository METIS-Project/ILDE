<?php
/*********************************************************************************
 * LdShake is a platform for the social sharing and co-edition of learning designs
 * Copyright (C) 2009-2012, Universitat Pompeu Fabra, Barcelona.
 *
 * (Contributors, alpha. order) Abenia, P., Carralero, M.A., Chacón, J., Hernández-Leo, D., Moreno, P.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by the
 * Free Software Foundation with the addition of the following permission added
 * to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK
 * IN WHICH THE COPYRIGHT IS OWNED BY Universitat Pompeu Fabra (UPF), Barcelona,
 * UPF DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with
 * this program; if not, see http://www.gnu.org/licenses.
 *
 * You can contact the Interactive Technologies Group (GTI), Universitat Pompeu Fabra, Barcelona.
 * headquarters at c/Roc Boronat 138, Barcelona, or at email address davinia.hernandez@upf.edu
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License version 3,
 * these Appropriate Legal Notices must retain the display of the "Powered by
 * LdShake" logo with a link to the website http://ldshake.upf.edu.
 * If the display of the logo is not reasonably feasible for
 * technical reasons, the Appropriate Legal Notices must display the words
 * "Powered by LdShake" with the link to the website http://ldshake.upf.edu.
 ********************************************************************************/

global $CONFIG;

$CONFIG->community_languages = array(
    "bg" => "Bulgarian",
    "ca" => "Catalan",
    "en" => "English",
    "fr" => "French",
    "el" => "Greek",
    "sl" => "Slovenian",
    "es" => "Spanish",
);

function ldshake_mode_get_translations() {
    global $CONFIG;
    return  $CONFIG->community_languages;
}

function ldshake_mode_open_register_validation() {
    global $CONFIG;

    $value = get_input('sdfsddfsdfre2352sgdsgsse78gh5g',null);

    if(isset($CONFIG->community_languages[$value]))
        return true;
    else {
        throw new Exception(T('Please, select you preferred language.'));
        return false;
    }
}

function ldshake_mode_open_register(&$user) {
    $value = get_input('sdfsddfsdfre2352sgdsgsse78gh5g',null);
    ldshake_handson3_register($user, $value);
}

function ldshake_handson3_register(&$user, $value) {
    global $CONFIG;
    if(isset($CONFIG->community_languages[$value])) {
        $user->language = $value;
        $user->save();
    }
    else
        throw new Exception("Invalid language code");
}

function ldshake_mode_ldsnew($params) {
    global $CONFIG;

    $initLdS = json_decode($params['data']['initLdS']);
    $user = get_loggedin_user();
    if(isset($CONFIG->community_languages[$user->language]))
        $initLdS->tags = $CONFIG->community_languages[$user->language];

    $params['data']['initLdS'] = json_encode($initLdS);

    return $params['data'];
}

function ldshake_mode_ldsnew_project(&$initLdS) {
    global $CONFIG;

    $user = get_loggedin_user();
    if(isset($CONFIG->community_languages[$user->language]))
        $initLdS->tags = array($CONFIG->community_languages[$user->language]);
}

function ldshake_lds_oia_mph_get_dblink() {
    global $CONFIG;
    $host = $CONFIG->dbhost;
    $user = $CONFIG->dbuser;
    $password = $CONFIG->dbpass;
    $database = "oai-mph-ilde";

    if (!$dblink = mysqli_init())
        throw new DatabaseException("Error configuring database link");

    if(!mysqli_options($dblink, MYSQLI_OPT_CONNECT_TIMEOUT, 30))
        throw new DatabaseException("Error configuring database link");

    // Connect to database
    if (!mysqli_real_connect($dblink, $host, $user, $password, $database))
        throw new DatabaseException(sprintf(elgg_echo('DatabaseException:WrongCredentials'), $user, $host, "****"));

    if(!mysqli_set_charset($dblink, "utf8"))
        throw new DatabaseException("Error configuring database link");

    return $dblink;
}


function ldshake_lds_oia_mph_put_document($om_dblink, $document) {

    $lom = ldshake_lds_export_ods($document);
    $lom = mysqli_real_escape_string($om_dblink, $lom);

    $query = <<<SQL
    INSERT INTO oai_headers SET
  `oai_identifier` = {$document->guid},
  `oai_metadataprefix` = 'lom',
  `datestamp` = {$document->time_updated},
  `deleted` = 0,
  `oai_set` = '',
  `metadata_contents` = '{$lom}'
ON DUPLICATE KEY UPDATE
  `oai_identifier` = {$document->guid},
  `oai_metadataprefix` = 'lom',
  `datestamp` = {$document->time_updated},
  `deleted` = 0,
  `oai_set` = '',
  `metadata_contents` = '{$lom}'
SQL;

    mysqli_query($om_dblink, $query);
}

function ldshake_lds_oia_mph_mark_deleted_document($om_dblink, $document) {

    $query = <<<SQL
UPDATE oai_headers SET
`deleted` = 1
WHERE
`oai_identifier` = {$document['oai_identifier']}
SQL;

    mysqli_query($om_dblink, $query);
}

function ldshake_lds_oia_mph_get_current_elements($om_dblink) {

    $query = <<<SQL
SELECT * FROM oai_headers;
SQL;

    $records = array();
    if($result = mysqli_query($om_dblink, $query)) {
        if($record = mysqli_fetch_all($result, MYSQLI_ASSOC)) {
            foreach($record as $r)
                $records["{$r['oai_identifier']}"] = $r;
        };
    }

    //$published_docs = lds_contTools::getUserEntities('object', 'LdS_document', 0, false, 9999, 0, 'published', '1');
    return $records;
}

function ldshake_lds_oia_mph_update_elements($om_dblink) {
    $current_lom_elements = ldshake_lds_oia_mph_get_current_elements($om_dblink);
    $current_published_docs = get_entities_from_metadata('published', '1', "", "", 0, 9999);

    foreach($current_published_docs as $doc) {
        ldshake_lds_oia_mph_put_document($om_dblink, $doc);
        if(isset($current_lom_elements["{$doc->guid}"])) {
            unset($current_lom_elements["{$doc->guid}"]);
        }
    }

    foreach($current_lom_elements as $doc) {
        ldshake_lds_oia_mph_mark_deleted_document($om_dblink, $doc);
    }
}

function ldshake_lds_export_ods($lds) {
    global $CONFIG;
    $lom = '<?xml version="1.0" encoding="UTF-8"?><lom:lom xmlns:lom="http://ltsc.ieee.org/xsd/LOM"/>';
    $ods = new simpleXMLElement($lom, 0, false, 'lom', true);
    /*
    $ods = (object)array(
        "general" => new sdtClass(),
        "technical" => new sdtClass(),
        "classification" => new sdtClass(),
    );
    */

    $taxon = "taxonPath";
    $ods->general->title->string['language'] = "en";
    $ods->general->title->string = $lds->title;
    $ods->technical->location = $CONFIG->url.'pg/lds/view/'.$lds->guid;
    $ods->classification->purpose->source = "LOMv1.0";
    $ods->classification->purpose->value = "educational objective";
    $ods->classification->$taxon->source->string['language'] = "en";
    $ods->classification->$taxon->source->string = "science";
    $ods->classification->$taxon->taxon->id = "";
    $ods->classification->$taxon->taxon->entry->string['language'] = "en";
    $ods->classification->$taxon->taxon->entry->string = "astronomy";

    return $ods->asXML();
}