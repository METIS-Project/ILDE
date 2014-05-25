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


class LdS extends ElggObject
{
    protected function initialise_attributes()
    {
        parent::initialise_attributes();
    }

    public function __construct($guid = null)
    {
        parent::__construct($guid);
        $this->access_id = 2;
    }

    public function getURL()
    {
        global $CONFIG;

        return $CONFIG->url;
    }

    private function create_properties() {
        $guid = self::getGUID();

        $tag_id["discipline"] = get_metastring_id("discipline");
        $tag_id["pedagogical_approach"] = get_metastring_id("pedagogical_approach");
        $tag_id["tags"] = get_metastring_id("tags");

        $ms_string = array(
            'editing_by',
            'editing_tstamp',
            'editor_type',
            'all_can_view',
            'yes',
            'generic_comment',
            'revised_docs',
            'revised_docs_editor',
            'viewed_lds',
            'external_editor',
            'editor_subtype',
        );
        $ms_id = array();

        foreach($ms_string as $fms) {
            $ms_id[$fms] = get_metastring_id("$fms");
        }

        $doc_id = get_subtype_id("object", "LdS_document");
        $doceditor_id = get_subtype_id("object", "LdS_document_editor");

        $query = <<<SQL
INSERT INTO objects_property SET
  `guid` = {$guid},
  `subtype` = (SELECT e.subtype FROM entities e WHERE e.guid = {$guid}),
  `subtype_string` = (SELECT es.subtype FROM entities e JOIN entity_subtypes es WHERE es.id = e.subtype AND e.guid = {$guid}),
  `time_created` = (SELECT e.time_created FROM entities e WHERE e.guid = {$guid}),
  `time_updated` = (SELECT time_updated FROM entities e WHERE e.guid = {$guid}),
  `title` = (SELECT o.title FROM objects_entity o WHERE o.guid = {$guid}),
  `owner_guid` = (SELECT e.owner_guid FROM entities e WHERE e.guid = {$guid}),
  `container_guid` = (SELECT e.container_guid FROM entities e WHERE e.guid = {$guid}),
  `external_editor` = (SELECT m.value_id FROM metadata m WHERE m.name_id = {$ms_id["external_editor"]} AND m.entity_guid = {$guid}) IS NOT NULL,
  `creator_name` = (SELECT u.name FROM entities e, users_entity u WHERE e.guid = {$guid} AND u.guid = e.owner_guid),
  `creator_username` = (SELECT u.username FROM entities e, users_entity u WHERE e.guid = {$guid} AND u.guid = e.owner_guid),
  `last_viewed_time` = (SELECT a.time_created FROM annotations a WHERE a.name_id = {$ms_id["viewed_lds"]} AND a.entity_guid = {$guid} ORDER by id DESC LIMIT 1),
  `last_editor_name` = (SELECT u.name FROM annotations ed, users_entity u WHERE ed.name_id IN ({$ms_id["revised_docs"]},{$ms_id["revised_docs_editor"]}) AND ed.entity_guid = {$guid} AND ed.owner_guid = u.guid ORDER BY ed.id DESC LIMIT 1),
  `last_editor_username` = (SELECT u.username FROM annotations ed, users_entity u WHERE ed.name_id IN ({$ms_id["revised_docs"]},{$ms_id["revised_docs_editor"]}) AND ed.entity_guid = {$guid} AND ed.owner_guid = u.guid ORDER BY ed.id DESC LIMIT 1),
  `last_edited_time` = (SELECT IFNULL(ed.time_created, e.time_created) FROM entities e LEFT JOIN (SELECT ed.time_created, ed.entity_guid FROM annotations ed RIGHT JOIN entities e ON e.guid = ed.entity_guid WHERE (ed.name_id IN ({$ms_id["revised_docs"]},{$ms_id["revised_docs_editor"]}) OR ed.name_id IS NULL) AND e.guid = {$guid} ORDER BY id DESC LIMIT 1) AS ed ON e.guid = ed.entity_guid WHERE e.guid = {$guid}),
  `num_contributions` = (SELECT COUNT(ed.entity_guid) FROM annotations ed RIGHT JOIN entities e ON e.guid = ed.entity_guid WHERE (ed.name_id IN ({$ms_id["revised_docs"]},{$ms_id["revised_docs_editor"]}) OR ed.name_id IS NULL) AND e.guid = {$guid}),
  `num_comments` = (SELECT COUNT(DISTINCT a.id) FROM annotations a WHERE a.name_id = {$ms_id["generic_comment"]} AND a.entity_guid = {$guid} GROUP BY a.entity_guid),
  `num_documents` = (SELECT COUNT(DISTINCT e.guid) FROM entities e WHERE e.type = 'object' AND e.subtype IN ({$doc_id},{$doceditor_id}) AND e.container_guid = {$guid}),
  `editor_count` = (SELECT COUNT(ru.guid_one) FROM entity_relationships ru WHERE ru.relationship IN ('lds_editor','lds_editor_group') AND ru.guid_two = {$guid}),
  `viewer_count` = (SELECT COUNT(ru.guid_one) AS viewer_count FROM entity_relationships ru WHERE ru.relationship IN ('lds_viewer','lds_viewer_group')  AND ru.guid_two = {$guid}),
  `all_can_view` = (SELECT entity_guid FROM metadata m WHERE m.name_id = {$ms_id["all_can_view"]} AND m.value_id = {$ms_id["yes"]} AND m.entity_guid = {$guid}) IS NOT NULL,
  `editor_type` = (SELECT string FROM metadata met JOIN metastrings ms ON met.value_id = ms.id WHERE met.name_id = {$ms_id["editor_type"]} AND met.entity_guid = {$guid}),
  `editor_type_id` = (SELECT met.value_id FROM metadata met JOIN metastrings ms ON met.value_id = ms.id WHERE met.name_id = {$ms_id["editor_type"]} AND met.entity_guid = {$guid}),
  `discipline` = (SELECT GROUP_CONCAT(string SEPARATOR ',') AS string FROM metadata m LEFT JOIN metastrings ms ON ms.id = m.value_id WHERE m.name_id = {$tag_id["discipline"]} AND m.entity_guid = {$guid}),
  `pedagogical_approach` = (SELECT GROUP_CONCAT(string SEPARATOR ',') AS string FROM metadata m LEFT JOIN metastrings ms ON ms.id = m.value_id WHERE m.name_id = {$tag_id["pedagogical_approach"]} AND m.entity_guid = {$guid}),
  `tags` = (SELECT GROUP_CONCAT(string SEPARATOR ',') AS string FROM metadata m LEFT JOIN metastrings ms ON ms.id = m.value_id WHERE m.name_id = {$tag_id["tags"]} AND m.entity_guid = {$guid}),
  `editing_tstamp` = CAST(((SELECT string AS editing_tstamp FROM metadata m LEFT JOIN metastrings ms ON ms.id = m.value_id WHERE m.name_id = {$ms_id["editing_tstamp"]} AND m.entity_guid = {$guid})) AS UNSIGNED),
  `editing_by` = (SELECT string AS editing_by FROM metadata m LEFT JOIN metastrings ms ON ms.id = m.value_id WHERE m.name_id = {$ms_id["editing_by"]} AND entity_guid = {$guid} LIMIT 1),
  `editor_subtype` = (SELECT string FROM metadata met JOIN metastrings ms ON met.value_id = ms.id WHERE met.name_id = {$ms_id["editor_subtype"]} AND met.entity_guid = {$guid}),
  `editor_subtype_id` = (SELECT met.value_id FROM metadata met JOIN metastrings ms ON met.value_id = ms.id WHERE met.name_id = {$ms_id["editor_subtype"]} AND met.entity_guid = {$guid})
ON DUPLICATE KEY UPDATE
  `guid` = {$guid},
  `subtype` = (SELECT e.subtype FROM entities e WHERE e.guid = {$guid}),
  `subtype_string` = (SELECT es.subtype FROM entities e JOIN entity_subtypes es WHERE es.id = e.subtype AND e.guid = {$guid}),
  `time_created` = (SELECT e.time_created FROM entities e WHERE e.guid = {$guid}),
  `time_updated` = (SELECT time_updated FROM entities e WHERE e.guid = {$guid}),
  `title` = (SELECT o.title FROM objects_entity o WHERE o.guid = {$guid}),
  `owner_guid` = (SELECT e.owner_guid FROM entities e WHERE e.guid = {$guid}),
  `container_guid` = (SELECT e.container_guid FROM entities e WHERE e.guid = {$guid}),
  `external_editor` = (SELECT m.value_id FROM metadata m WHERE m.name_id = {$ms_id["external_editor"]} AND m.entity_guid = {$guid}) IS NOT NULL,
  `creator_name` = (SELECT u.name FROM entities e, users_entity u WHERE e.guid = {$guid} AND u.guid = e.owner_guid),
  `creator_username` = (SELECT u.username FROM entities e, users_entity u WHERE e.guid = {$guid} AND u.guid = e.owner_guid),
  `last_viewed_time` = (SELECT a.time_created FROM annotations a WHERE a.name_id = {$ms_id["viewed_lds"]} AND a.entity_guid = {$guid} ORDER by id DESC LIMIT 1),
  `last_editor_name` = (SELECT u.name FROM annotations ed, users_entity u WHERE ed.name_id IN ({$ms_id["revised_docs"]},{$ms_id["revised_docs_editor"]}) AND ed.entity_guid = {$guid} AND ed.owner_guid = u.guid ORDER BY ed.id DESC LIMIT 1),
  `last_editor_username` = (SELECT u.username FROM annotations ed, users_entity u WHERE ed.name_id IN ({$ms_id["revised_docs"]},{$ms_id["revised_docs_editor"]}) AND ed.entity_guid = {$guid} AND ed.owner_guid = u.guid ORDER BY ed.id DESC LIMIT 1),
  `last_edited_time` = (SELECT IFNULL(ed.time_created, e.time_created) FROM entities e LEFT JOIN (SELECT ed.time_created, ed.entity_guid FROM annotations ed RIGHT JOIN entities e ON e.guid = ed.entity_guid WHERE (ed.name_id IN ({$ms_id["revised_docs"]},{$ms_id["revised_docs_editor"]}) OR ed.name_id IS NULL) AND e.guid = {$guid} ORDER BY id DESC LIMIT 1) AS ed ON e.guid = ed.entity_guid WHERE e.guid = {$guid}),
  `num_contributions` = (SELECT COUNT(ed.entity_guid) FROM annotations ed RIGHT JOIN entities e ON e.guid = ed.entity_guid WHERE (ed.name_id IN ({$ms_id["revised_docs"]},{$ms_id["revised_docs_editor"]}) OR ed.name_id IS NULL) AND e.guid = {$guid}),
  `num_comments` = (SELECT COUNT(DISTINCT a.id) FROM annotations a WHERE a.name_id = {$ms_id["generic_comment"]} AND a.entity_guid = {$guid} GROUP BY a.entity_guid),
  `num_documents` = (SELECT COUNT(DISTINCT e.guid) FROM entities e WHERE e.type = 'object' AND e.subtype IN ({$doc_id},{$doceditor_id}) AND e.container_guid = {$guid}),
  `editor_count` = (SELECT COUNT(ru.guid_one) FROM entity_relationships ru WHERE ru.relationship IN ('lds_editor','lds_editor_group') AND ru.guid_two = {$guid}),
  `viewer_count` = (SELECT COUNT(ru.guid_one) AS viewer_count FROM entity_relationships ru WHERE ru.relationship IN ('lds_viewer','lds_viewer_group')  AND ru.guid_two = {$guid}),
  `all_can_view` = (SELECT entity_guid FROM metadata m WHERE m.name_id = {$ms_id["all_can_view"]} AND m.value_id = {$ms_id["yes"]} AND m.entity_guid = {$guid}) IS NOT NULL,
  `editor_type` = (SELECT string FROM metadata met JOIN metastrings ms ON met.value_id = ms.id WHERE met.name_id = {$ms_id["editor_type"]} AND met.entity_guid = {$guid}),
  `editor_type_id` = (SELECT met.value_id FROM metadata met JOIN metastrings ms ON met.value_id = ms.id WHERE met.name_id = {$ms_id["editor_type"]} AND met.entity_guid = {$guid}),
  `discipline` = (SELECT GROUP_CONCAT(string SEPARATOR ',') AS string FROM metadata m LEFT JOIN metastrings ms ON ms.id = m.value_id WHERE m.name_id = {$tag_id["discipline"]} AND m.entity_guid = {$guid}),
  `pedagogical_approach` = (SELECT GROUP_CONCAT(string SEPARATOR ',') AS string FROM metadata m LEFT JOIN metastrings ms ON ms.id = m.value_id WHERE m.name_id = {$tag_id["pedagogical_approach"]} AND m.entity_guid = {$guid}),
  `tags` = (SELECT GROUP_CONCAT(string SEPARATOR ',') AS string FROM metadata m LEFT JOIN metastrings ms ON ms.id = m.value_id WHERE m.name_id = {$tag_id["tags"]} AND m.entity_guid = {$guid}),
  `editing_tstamp` = CAST(((SELECT string AS editing_tstamp FROM metadata m LEFT JOIN metastrings ms ON ms.id = m.value_id WHERE m.name_id = {$ms_id["editing_tstamp"]} AND m.entity_guid = {$guid})) AS UNSIGNED),
  `editing_by` = (SELECT string AS editing_by FROM metadata m LEFT JOIN metastrings ms ON ms.id = m.value_id WHERE m.name_id = {$ms_id["editing_by"]} AND entity_guid = {$guid} LIMIT 1),
  `editor_subtype` = (SELECT string FROM metadata met JOIN metastrings ms ON met.value_id = ms.id WHERE met.name_id = {$ms_id["editor_subtype"]} AND met.entity_guid = {$guid}),
  `editor_subtype_id` = (SELECT met.value_id FROM metadata met JOIN metastrings ms ON met.value_id = ms.id WHERE met.name_id = {$ms_id["editor_subtype"]} AND met.entity_guid = {$guid})
SQL;

        insert_data($query);
    }

    public function save() {
        if($guid = parent::save())
            self::create_properties();

        return $guid;
    }

    public function save_ktu() {
        if($guid = parent::save_ktu())
            self::create_properties();

        return $guid;
    }

    function annotate($name, $value, $access_id = 0, $owner_id = 0, $vartype = "") {
        if($result = parent::annotate($name, $value, $access_id, $owner_id, $vartype)) {
            self::create_properties();
        }

        return $result;
    }

    public function enable() {
        if($result = parent::enable())
            self::create_properties();

        return $result;
    }

    public function delete() {
        $guid = self::getGUID();
        if($result = parent::delete()) {
            $query = <<<SQL
    DELETE FROM objects_property WHERE guid = {$guid}
SQL;
            execute_delayed_write_query($query);
        }
        return $result;
    }

    public function disable() {
        $guid = self::getGUID();
        if($result = parent::disable()) {

            $query = <<<SQL
    DELETE FROM objects_property WHERE guid = {$guid}
SQL;

            execute_delayed_write_query($query);
        }

        return $result;
    }

    public function countAnnotations($string){
        if($string == 'generic_comment') {
            $guid = self::getGUID();
            $query = <<<SQL
SELECT op.num_comments
FROM objects_property op
WHERE op.guid = {$guid}
LIMIT 1
SQL;
            if($result = get_data_row($query))
                return (int)$result->num_comments;
        }

        return parent::countAnnotations($string);
    }
}
