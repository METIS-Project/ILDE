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

    $highschool_value = get_input('sdfsddfsdfre2352sgdsgsse78gh5g',null);

    if(isset($CONFIG->community_languages[$highschool_value]))
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
    if(isset($CONFIG->community_languages[$value]))
        $user->language = $value;
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