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

$id = get_input('guid');
$ref_id = get_input('ref_guid');
$vars['lds'] = get_entity($id);

//TODO permission / exist checks

$vars['ldsDocs'] = lds_contTools::getLdsDocuments($id);

$editordocument = get_entities_from_metadata('lds_guid',$vars['lds']->guid,'object','LdS_document_editor', 0, 100);

switch($editordocument[0]->editorType) {
    case 'cld':
    case 'image':
    case 'openglm':
        $vars['upload'] = true;
        $vars['uploadDoc'] = $editordocument[0];
        break;
    default:
        $vars['upload'] = false;

}

$vars['editor'] = $editordocument[0]->editorType;

if($vars['lds']->external_editor) {
    $vars['currentDocId'] = $editordocument[0]->guid;
    $vars['currentDoc'] = $editordocument[0];
} else {
    $vars['currentDocId'] = $params[3] ?: $vars['ldsDocs'][0]->guid;
    $vars['currentDoc'] = get_entity($vars['currentDocId']);
}

$vars['am_i_starter'] = (get_loggedin_userid() == $vars['lds']->owner_guid);
$vars['starter'] = get_user($vars['lds']->owner_guid);
$vars['editor'] = $vars['lds']->editor_type;

//Securuty: we won't show a document unless it's part of this LdS
if ($vars['currentDoc']->lds_guid != $id)
{
    lds_exec_404();
    return;
}

//Check if it's published
$vars['publishedId'] = lds_contTools::getPublishedId($vars['currentDocId']);

$vars['nComments'] = $vars['lds']->countAnnotations('generic_comment');

//These are all my friends
$arrays = lds_contTools::buildObjectsArray($vars['lds']);
$vars['jsonfriends'] = json_encode($arrays['available']);
$vars['viewers'] = json_encode($arrays['viewers']);
$vars['editors'] = json_encode($arrays['editors']);
$vars['groups'] = json_encode(ldshakers_contTools::buildMinimalUserGroups(get_loggedin_userid()));
$vars['starter'] = get_user($vars['lds']->owner_guid);

//$vars['all_can_read'] = $vars['lds']->all_can_read;
$vars['all_can_read'] = ($vars['lds']->all_can_view == 'yes' || ($vars['lds']->all_can_view === null && $vars['lds']->access_id < 3 && $vars['lds']->access_id > 0)) ? 'true' : 'false';

$tree_html = elgg_view('lds/view_internal_tree',$vars);
header('Content-Type: application/json; charset=utf-8');
echo json_encode(array("html" => $tree_html));