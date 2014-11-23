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

function ldshake_mode_browselds_filters($filter) {
    global $CONFIG;

    $filter = rawurlencode($filter);
    $content = "";
    $content .= '<div class="browse-filter-button"><a href="'.$CONFIG->url.'pg/lds/browse/?revised=true&filter='.$filter.'">' . htmlspecialchars(T('Only display project LdS edited by users')) . '</a></div>';

    return $content;
}

function ldshake_mode_selected_language() {
    global $CONFIG;
    return $CONFIG->language;
}

function ldshake_lds_oia_mph_get_dblink() {
    global $CONFIG;
    $host = $CONFIG->dbhost;
    $user = $CONFIG->dbuser;
    $password = $CONFIG->dbpass;
    $database =  "oai-mph-ilde-" . $CONFIG->dbname;

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

function ldshake_lds_oia_mph_get_identifier($guid, $format = 'pdf') {
    global $CONFIG;
    $url = parse_url($CONFIG->url);
    $url['path'] = str_replace('/', '', $url['path']);
    if(empty($url['path']))
        $url['path'] = 'root';

    $identifier = 'oai:'.$url['host'].':'.lds_contTools::encodeId($guid) . '-' . $format;

    return $identifier;
}

function ldshake_lds_oia_mph_get_guid_from_identifier($identifier) {
    global $CONFIG;
    $identifier = explode(':', $identifier);
    if(isset($identifier[2]))
        return base_convert(strtolower($identifier[2]), 36, 10) - $CONFIG->salt;

    return 0;
}

function ldshake_lds_oia_mph_put_document($om_dblink, $document) {

    try {
            $formats = ldshake_get_document_formats($document);
    } catch (Exception $e) {
        //TODO: log something
    }

    try {
        $multiformat = (count($formats) > 1) ? true : false;
        foreach($formats as $format_name => $format)
            $lom_documents[$format_name] = ldshake_lds_export_ods($document, $format, $multiformat);
    } catch(Exception $e) {
        return false;
    }

    $result_identifiers = array();
    foreach($lom_documents as $format => $lom) {
        $lom = mysqli_real_escape_string($om_dblink, $lom);
        $guid = (in_array($document->getSubtype(), array('LdS_document', 'LdS_document_editor'))) ? $document->guid : $document->document_guid;
        $identifier = ldshake_lds_oia_mph_get_identifier($guid, $format);
        $result_identifiers[] = $identifier;
        $identifier = mysqli_real_escape_string($om_dblink, $identifier);

        $published = get_metadata_byname($document->guid, 'published');

        $query = <<<SQL
    INSERT INTO oai_headers SET
  `oai_identifier` = '{$identifier}',
  `oai_metadataprefix` = 'lom',
  `datestamp` = {$published->time_created},
  `deleted` = 0,
  `oai_set` = 'class:handson3',
  `metadata_contents` = '{$lom}'
ON DUPLICATE KEY UPDATE
  `oai_identifier` = '{$identifier}',
  `oai_metadataprefix` = 'lom',
  `datestamp` = {$published->time_created},
  `deleted` = 0,
  `oai_set` = 'class:handson3',
  `metadata_contents` = '{$lom}'
SQL;

        mysqli_query($om_dblink, $query);
    }
    return $result_identifiers;
}

function ldshake_lds_oia_mph_mark_deleted_document($om_dblink, $document) {

    $identifier = mysqli_real_escape_string($om_dblink, $document['oai_identifier']);
    $query = <<<SQL
UPDATE oai_headers SET
`deleted` = 1,
`datestamp` = UNIX_TIMESTAMP()
WHERE
`oai_identifier` = '{$identifier}'
SQL;

    mysqli_query($om_dblink, $query);
}

function ldshake_lds_oia_mph_get_current_elements($om_dblink) {

    $query = <<<SQL
SELECT * FROM oai_headers WHERE `deleted` = 0;
SQL;

    $records = array();
    if($result = mysqli_query($om_dblink, $query)) {
        $record = array();
        while($wrecord = mysqli_fetch_assoc($result))
            $record[] = $wrecord;

        //if($record = mysqli_fetch_all($result, MYSQLI_ASSOC)) {
        if(!empty($record)) {
            foreach($record as $r)
                $records["{$r['oai_identifier']}"] = $r;
        };
    }

    //$published_docs = lds_contTools::getUserEntities('object', 'LdS_document', 0, false, 9999, 0, 'published', '1');
    return $records;
}

function ldshake_lds_oia_mph_update_elements($om_dblink) {
    $current_lom_elements = ldshake_lds_oia_mph_get_current_elements($om_dblink);
    $current_published_docs = get_entities_from_metadata('published', '1', "object", "LdS_document", 0, 9999);
    $current_published_docs_editor = get_entities_from_metadata('published', '1', "object", "LdS_document_editor", 0, 9999);
    $current_published_docs_revisions = get_entities_from_metadata('published', '1', "object", "LdS_document_revision", 0, 9999);

    $current_published_docs = array_merge($current_published_docs, $current_published_docs_editor, $current_published_docs_revisions);
    foreach($current_published_docs as $doc) {
        $guid = (in_array($doc->getSubtype(), array('LdS_document', 'LdS_document_editor'))) ? $doc->guid : $doc->document_guid;
        $sure_doc = get_entity($guid);
        $lds = get_entity($sure_doc->lds_guid);

        if(!in_array($lds->editor_type, array('doc','webcollagerest')))
            continue;

        $identifiers = ldshake_lds_oia_mph_put_document($om_dblink, $doc);
        foreach($identifiers as $identifier) {
            if(isset($current_lom_elements["{$identifier}"])) {
                unset($current_lom_elements["{$identifier}"]);
            }
        }
    }

    foreach($current_lom_elements as $doc) {
        try {
            ldshake_lds_oia_mph_mark_deleted_document($om_dblink, $doc);
        } catch(Exception $e) {
        }
    }
}

function ldshake_lds_export_ods($doc, $format, $multiformat = false) {
    global $CONFIG;

    if(in_array($doc->getSubtype(), array('LdS_document_revision', 'LdS_document_editor_revision'))) {
        $doc = get_entity($doc->document_guid);
    }

    if(!$user = get_user($doc->owner_guid))
        throw new Exception("The owner does not exist");

    if(in_array($doc->getSubtype(), array('LdS_document','LdS_document_editor'))) {
        if(!$lds = get_entity($doc->lds_guid))
            throw new Exception("The LdS does not exist");
    } else {
        throw new Exception("The LdS does not exist");
    }

    $project = null;
    if(isset($lds->project_design)) {
        if(is_numeric($lds->project_design)) {
            $project = get_entity($lds->project_design);
        }
    }


    $lom = <<<XML
<?xml version="1.0" encoding="UTF-8"?>
<lom:lom xmlns:lom="http://ltsc.ieee.org/xsd/LOM"/>
XML;
    $ods = new simpleXMLElement($lom, 0, false, 'lom', true);

    $editor_prefix = 'v/';
    if($doc->editorType == 'webcollagerest')
        $editor_prefix = 've/';

    $taxon = "taxonPath";
    $language = $user->language;
    if(empty($language))
        $language = $CONFIG->language;

    $external = ($doc->getSubtype() == "LdS_document_editor") ? "ve" : "v";

    //keywords 10 maximum
    $tags = array();
    $tags[] = "ILDE";
    $tags[] = "HandsON ICT";
    if(isset($format['tag']))
        $tags[] = $format['tag'];

    if($project) {
        $tags[] = $project->title;
    }

    $tool = null;
    $template = null;
    if($lds->editor_type == 'webcollagerest') {
        $tags[] = "WebCollage";
        $tool = "WebCollage";
    } elseif(isset($lds->editor_subtype)) {
        if(isset($CONFIG->project_templates['full'][$lds->editor_subtype])) {
            $tags[] = $CONFIG->project_templates['full'][$lds->editor_subtype]['title'];
            $template = $CONFIG->project_templates['full'][$lds->editor_subtype]['title'];;
        }
    }

    foreach(array('tags', 'discipline', 'pedagogical_approach') as $tags_name) {
        if(!empty($lds->$tags_name)) {
            $current_tag = $lds->$tags_name;
            if(is_array($current_tag))
                $tags = array_merge($tags, $current_tag);
            else if(is_string($current_tag))
                $tags[] = $current_tag;
        }
    }

    $tags = array_slice($tags, 0, 10);

    //license
    $copyright = "yes";
    if(!empty($lds->license)) {
        if((int)$lds->license) {
            switch($lds->license)  {
                case LDS_LICENSE_CC_BY:
                    $rights = "Creative Commons Attribution 3.0 Unported";
                    break;
                case LDS_LICENSE_CC_BY_ND:
                    $rights = "Creative Commons Attribution-NoDerivs 3.0 Unported";
                    break;
                case LDS_LICENSE_CC_BY_SA:
                    $rights = "Creative Commons Attribution-ShareAlike 3.0 Unported";
                    break;
                case LDS_LICENSE_CC_BY_NC:
                    $rights = "Creative Commons Attribution-NonCommercial 3.0 Unported";
                    break;
                case LDS_LICENSE_CC_BY_NC_ND:
                    $rights = "Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported";
                    break;
                case LDS_LICENSE_CC_BY_NC_NA:
                    $rights = "Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported";
                    break;
                default:
                    $rights = "(C) All rights reserved";
                    break;
            }
        } else {
            $rights = "(C) All rights reserved";
        }
    }
    else {
            $rights = "(C) All rights reserved";
    }

    $description = "";
    $description .= "This learning design created using the ILDE environment.\n";
    if($template)
        $description .= "This design is based on the \"{$template}\" template.\n";
    if($tool)
        $description .= "The tool used to author the design is {$tool}.\n";
    if($project)
        $description .= "This document is part of the project \"{$project->title}\".\n";
    if(!empty($format['description']))
        $description .= $format['description']."\n";
    if(!empty($rights))
        $description .= "This work is licensed under the following terms:\n \"{$rights}\"\n";

    //title
    if(empty($doc->title))
        $title = $lds->title;
    else
        $title = $doc->title;

    if($multiformat) {
        $title .= " ({$format['titlesuffix']})";
    }

    $ods->general->identifier[0]->catalog = "Public URL";
    $ods->general->identifier[0]->entry = $CONFIG->url . $external . '/' . lds_contTools::encodeId($doc->guid);

    $ods->general->identifier[1]->catalog = "Logged in URL";
    $ods->general->identifier[1]->entry = $CONFIG->url . 'pg/lds/view/'. $lds->guid . '/doc/' . $doc->guid. '/';


    $ods->general->title->string['language'] = $language;
    $ods->general->title->string = $title;

    $ods->general->language = $language;

    $ods->general->description->string['language'] = "en";
    $ods->general->description->string = $description;

    if(!empty($tags)) {
        $i=0;
        foreach($tags as $tag) {
            $ods->general->keyword[$i]->string['language'] = $language;
            $ods->general->keyword[$i]->string = $tag;
            $i++;
        }
    }

    $ods->lifeCycle->contribute->role->source = "LOMv1.0";
    $ods->lifeCycle->contribute->role->value = "author";
    $ods->lifeCycle->contribute->entity = $user->name;

    $ods->metaMetadata->identifier[0]->catalog = "Public URL";
    $ods->metaMetadata->identifier[0]->entry = $CONFIG->url . $external . '/' . lds_contTools::encodeId($doc->guid);

    $ods->metaMetadata->identifier[1]->catalog = "Logged in URL";
    $ods->metaMetadata->identifier[1]->entry = $CONFIG->url . 'pg/lds/view/'. $lds->guid . '/doc/' . $doc->guid. '/';

    $ods->metaMetadata->contribute->role->source = "LOMv1.0";
    $ods->metaMetadata->contribute->role->value = "creator";
    $ods->metaMetadata->contribute->entity = $user->name;

    $ods->technical->format = $format['mime'];
    $ods->technical->location = $CONFIG->url.$editor_prefix.lds_contTools::encodeId($doc->guid).'/'.$format['urlsuffix'];

    $ods->rights->copyrightAndOtherRestrictions->source = "LOMv1.0";
    $ods->rights->copyrightAndOtherRestrictions->value = $copyright;
    $ods->rights->description->string['language'] = "en";
    $ods->rights->description->string = $rights;

    /*
     * Metadata.Contribute
     */

    /*
     * 5.5.4	LifeCycle.Status
     *
     */
    /*
    $ods->general->keyword->string['language'] = $language;
    $ods->general->keyword->string = $doc->title;
     */


    $ods->classification->purpose->source = "LOMv1.0";
    $ods->classification->purpose->value = "educational objective";
    $ods->classification->$taxon->source->string['language'] = "en";
    $ods->classification->$taxon->source->string = "learning";
    $ods->classification->$taxon->taxon->id = "";
    $ods->classification->$taxon->taxon->entry->string['language'] = "en";
    $ods->classification->$taxon->taxon->entry->string = "learning design";

    $dom = dom_import_simplexml($ods)->ownerDocument;
    $dom->preserveWhiteSpace = false;
    $dom->formatOutput = true;
    return $dom->saveXML();
}