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

class ldshake_tools {
    public static function load() {
        global $CONFIG;

        $CONFIG->ldshake_tools = array();

        $CONFIG->ldshake_tools['templates'] = array();
        $CONFIG->ldshake_tools['apps'] = array();

        foreach($template_files as $t_f) {
            $xmlproperties = file_get_contents($_FILES['properties']['tmp_name']);
                //return array();

            $xmldoc = new DOMDocument();
            $xmldoc->loadXML($xmlproperties);

            $xpathvar = new Domxpath($xmldoc);

            $queryResult = $xpathvar->query('//lds/name');
            $name = $queryResult->item(0)->nodeValue;

            $queryResult = $xpathvar->query('//lds/internalname');
            $internalname = $queryResult->item(0)->nodeValue;

            $queryResult = $xpathvar->query('//lds/type');
            $type = $queryResult->item(0)->nodeValue;

            $queryResult = $xpathvar->query('//lds/menu');
            $menu = $queryResult->item(0)->nodeValue;

            $queryResult = $xpathvar->query('//lds/setings');
            $settings = $queryResult->item(0)->nodeValue;

            foreach($settings->childNodes as $setting_node) {
                if($setting_node instanceof DOMElement) {
                    $queryResult = $xpathvar->query('url', $setting_node);
                    $tag_url = $queryResult->item(0)->nodeValue;

                    $queryResult = $xpathvar->query('implementable', $setting_node);
                    $tag_implementable = $queryResult->item(0)->nodeValue;

                    $queryResult = $xpathvar->query('support', $setting_node);
                    $tag_support = $queryResult->item(0)->nodeValue;

                    $queryResult = $xpathvar->query('imsld', $setting_node);
                    $tag_imsld = $queryResult->item(0)->nodeValue;

                    $queryResult = $xpathvar->query('export', $setting_node);
                    $tag_export = $queryResult->item(0)->nodeValue;

                    $queryResult = $xpathvar->query('methods', $setting_node);
                    $tag_methods = $queryResult->item(0)->nodeValue;
                }
            }
        }
    }
}