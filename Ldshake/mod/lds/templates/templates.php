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

function ldshake_get_template($template, $format = null) {
    global $CONFIG;

    $templates = array(
        'agile_user_stories' => array('agile_user_stories',
            array('file' => 'agile_user_stories_er',
                'title' => 'evaluation rubric'),
            array('file' => 'agile_user_stories_s',
                'title' => 'Support Document')),
        'practice_narrative' => array('practice_narrative',
            array('file' => 'practice_narrative_er',
                'title' => 'evaluation rubric'),
            array('file' => 'practice_narrative_s',
                'title' => 'Support Document')),
        'practice_pattern' => array('practice_pattern',
            array('file' => 'practice_pattern_s',
                'title' => 'Support Document')),
        'MWD' => array('MWD'),
        'kek_p1' => array('kek_p1'),
        'MDN' => array('mdn'),
        'EP' => array('EP'),
        'coursemap' => array('coursemap'),
        'design_pattern' => array('DP','DPS'),
        'DPS' => array('DPS'),
        'PC' => array('PC','yishay'),
        'FC' => array('FC','yishay'),
        'HE' => array('HE','HEs'),
        'CF' => array('CF'),
        'DB' => array('dreambazaar'),
        'LO' => array('learningobjectives'),
        'scenario' => array('scenario'),

        ///MSF
        'msf_a01' => array('01_ActivityAnalysisTemplate'),
        'msf_d00' => array('00_Learning_Task'),
        'msf_d01c' => array('01_Course_Objectives_Template'),
        'msf_d01d' => array('01_DevelomentTemPlateMain'),
        'msf_d02' => array('02_Course_Estructure_Template_ES-1'),
        'msf_d03' => array('03_Activities_and_Resources_Template'),
        'msf_a02' => array('02_TaskAnalysisTemplate_size'),
        ///MSF

        ///google draw
        'mooc_canvas' => array('mooc_canvas', 'mooc_canvas_support'),
        'google_draw' => array('new_google_draw'),
        ///google draw

        't1e' => array('e_text_integrat'),
        't2e' => array('e_equip'),
        't3e' => array('e_mapa'),
        't4e' => array('e_cronograma'),
        't5e' => array('e_brainstorming'),
    );
    if(isset($templates[$template])) {
        $doc = array();
        foreach($templates[$template] as $file) {
            $title = null;
            if(is_array($file)) {
                $title = T($file['title']);
                $file = T($file['file']);
            }
            $preferred_formats = array('docx','xlsx', 'google_doc_id', null);

            foreach($preferred_formats as $format) {
                $filename = "";
                if(!empty($format)) {
                    if(!empty($format) and file_exists(__DIR__. '/' . $format . '/i18n/'.$CONFIG->language.'/'.$file.'.'.$format)) {
                        $filename = __DIR__. '/' . $format . '/i18n/'.$CONFIG->language.'/'.$file.'.'.$format;
                    }
                    elseif(file_exists(__DIR__. '/' . $format . '/'.$file.'.'.$format)) {
                        $filename = __DIR__. '/' . $format . '/'.$file.'.'.$format;
                    }
                }
                elseif(file_exists(__DIR__.'/i18n/'.$CONFIG->language.'/'.$file.'.txt'))
                    $filename = __DIR__.'/i18n/'.$CONFIG->language.'/'.$file.'.txt';
                elseif(file_exists(__DIR__.'/'.$file.'.txt'))
                    $filename = __DIR__.'/'.$file.'.txt';

                if(!empty($filename)) {
                    if($text = file_get_contents($filename)) {
                        $template_data = array($text, $format);
                        if($title)
                            $template_data[] = $title;
                        $doc[] = $template_data;
                        break;
                    }
                }
            }
        }
        return $doc;
    }
    return null;
}