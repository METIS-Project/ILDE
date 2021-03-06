<?php

/*********************************************************************************
 * LdShake is a platform for the social sharing and co-edition of learning designs
 * Copyright (C) 2009-2012, Universitat Pompeu Fabra, Barcelona.
 *
 * (Contributors, alpha. order) Abenia, P., Carralero, M.A., ChacÃ³n, J., HernÃ¡ndez-Leo, D., Moreno, P.
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

/**
 * Set of helper functions for the views of the LdS module
 */

function ldshake_env_category($lds) {
    if(is_string($lds))
        $subtype = $lds;
    elseif($lds)
        $subtype = $lds->getSubtype();
    else
        return 'LdS';

    switch($subtype) {
        case 'LdS':
            return 'LdS';
        break;
        case 'LdSProject_implementation':
            return 'project';
        break;
        case 'LdSProject':
            return 'workflow';
        break;
    }

    return false;
}

class lds_viewTools
{

    public static function pagination ($count, $elementsPerPage = 10, $filter = null)
    {
        $params = array(
            'baseurl' => 'http://'.$_SERVER['SERVER_NAME'].$_SERVER['REQUEST_URI'],
            'offset' => get_input('offset', 0),
            'count' => $count,
            'limit' => $elementsPerPage,
            'filter' => $filter
        );

        return elgg_view('navigation/pagination', $params);
    }

    public static function tag_display ($tags, $class, $params = null)
    {
        $tags = $tags->$class;

        if (is_string($tags) && strlen($tags))
            $tags = array($tags);

        $extra_params="";
        if(is_array($params)) {
            foreach($params as $k=>$v) {
                $extra_params .= "&{$k}=" . rawurlencode($v);
            }
        }

        $tagstring = '';
        if (is_array($tags) && count($tags))
            foreach($tags as $tag)
                $tagstring .= '<a href="'.self::getUrl().'?tagk='.$class.'&tagv='.urlencode($tag). $extra_params .'" class="lds_tag '.$class.'">'.$tag.'</a> ';

        return $tagstring;
    }

    public static function all_tag_display ($lds)
    {
        $tagtypes = array ('tags', 'discipline', 'pedagogical_approach');

        //We build an array of all the tags of the LdS
        $arr = array();
        foreach ($tagtypes as $type)
        {
            $tags = $lds->$type;

            if (is_string($tags) && strlen($tags)) $tags = array($tags);

            if (is_array($tags))
            {
                foreach ($tags as $tag)
                {
                    $t = new stdClass();
                    $t->type = $type;
                    $t->tag = $tag;
                    $arr[] = $t;
                }
            }

        }

        //Order them by name
        Utils::osort($arr, 'tag');

        //Print them
        $str = '';
        foreach ($arr as $tag)
            $str .= '<span class="lds_small_tag '.$tag->type.'">'.$tag->tag.'</span>';

        return $str;
    }

    public static function url_for ($lds, $type = 'view')
    {
        global $CONFIG;

        $editortype = ($lds->external_editor ? 'editor' : '');

        if($type == 'info')
            $editortype = '';

        $folder = $type.$editortype;

        //$subtype = is_string($lds->subtype) ? $lds->subtype : $lds->getSubtype();
        $subtype = get_subtype_from_id($lds->subtype);
        if($subtype == 'LdSProject_implementation' || $subtype == 'LdSProject') {
            if($type == 'edit') {
                $folder = 'edit_project';
            } elseif($type == 'view') {
                $folder = 'vieweditor';
            } elseif($type == 'list') {
                $folder = 'project_implementation';
            } elseif($type == 'implement') {
                $folder = 'project/implement/';
            }
        }

        if($subtype == 'LdS_implementation') {
            if($type == 'edit') {
                $folder = 'editglueps';
            }
        }

        //Google Docs history
        if($type == 'history' && ($lds->editor_type == 'google_docs' || $lds->editor_type == 'exelearningrest'))
            $folder = 'history';

        return $CONFIG->url . 'pg/lds/'.$folder.'/' . $lds->guid . '/';
    }

    public static function getUrl ($type = 'list')
    {
        global $CONFIG;

        switch ($type)
        {
            case 'mine':
                return $CONFIG->url . 'pg/lds/';
            case 'trashed':
                return $CONFIG->url . 'pg/lds/trashed/';
            case 'created-by-me':
                return $CONFIG->url . 'pg/lds/created-by-me/';
            case 'shared-with-me':
                return $CONFIG->url . 'pg/lds/shared-with-me/';
            case 'list':
                return $CONFIG->url . 'pg/lds/browse/';
            case 'patterns':
                return $CONFIG->url . 'pg/lds/query/';
        }
    }

    public static function iconSupport ($type)
    {
        $supported = array('doc', 'gluepsrest', 'webcollagerest','glueps','openglm','cadmos','project_design','exelearningrest');

        return in_array($type, $supported);
    }

    public static function detailedHistorySupport($lds)
    {
        $supported = array("google_docs", "google_spreadsheet", "google_draw");

        return in_array($lds->editor_type, $supported);
    }
}

/*
  function pagination ($count, $elementsPerPage = 10)
{
    $params = array(
        'baseurl' => 'http://'.$_SERVER['SERVER_NAME'].$_SERVER['REQUEST_URI'],
        'offset' => get_input('offset') ?: 0,
        'count' => $count,
        'limit' => $elementsPerPage
    );

    return elgg_view('navigation/pagination', $params);
}
	
	  function tag_display ($tags, $class)
{
    $tags = $tags->$class;
    //debug_print($tags);
    if (is_string($tags) && strlen($tags)) $tags = array($tags);

    $tagstring = '';
    if (is_array($tags) && count($tags))
        foreach($tags as $tag)
            $tagstring .= '<a href="'.self::getUrl().'?tagk='.$class.'&tagv='.urlencode($tag).'" class="lds_tag '.$class.'">'.$tag.'</a> ';

    return $tagstring;
}

	  function all_tag_display ($lds)
{
    $tagtypes = array ('tags', 'discipline', 'pedagogical_approach');

    //We build an array of all the tags of the LdS
    $arr = array();
    foreach ($tagtypes as $type)
    {
        $tags = $lds->$type;

        if (is_string($tags) && strlen($tags)) $tags = array($tags);

        if (is_array($tags))
        {
            foreach ($tags as $tag)
            {
                $t = new stdClass();
                $t->type = $type;
                $t->tag = $tag;
                $arr[] = $t;
            }
        }

    }

    //Order them by name
    Utils::osort($arr, 'tag');

    //Print them
    $str = '';
    foreach ($arr as $tag)
        $str .= '<span class="lds_small_tag '.$tag->type.'">'.$tag->tag.'</span>';

    return $str;
}

	  function url_for ($lds, $type = 'view')
{
    global $CONFIG;

    $editortype = ($lds->external_editor ? 'editor' : '');

    $folder = $type.$editortype;

    $subtype = is_string($lds->subtype) ? $lds->subtype : $lds->getSubtype();
    if($subtype == 'LdSProject_implementation' || $subtype == 'LdSProject') {
        if($type == 'edit') {
            $folder = 'edit_project';
        } elseif($type == 'view') {
            $folder = 'project_implementation';
        }
    }

    //Google Docs history
    if($type == 'history' && $lds->editor_type == 'google_docs')
        $folder = 'history';

    return $CONFIG->url . 'pg/lds/'.$folder.'/' . $lds->guid . '/';
}
	
	  function getUrl ($type = 'list')
{
    global $CONFIG;

    switch ($type)
    {
        case 'mine':
            return $CONFIG->url . 'pg/lds/';
        case 'trashed':
            return $CONFIG->url . 'pg/lds/trashed/';
        case 'created-by-me':
            return $CONFIG->url . 'pg/lds/created-by-me/';
        case 'shared-with-me':
            return $CONFIG->url . 'pg/lds/shared-with-me/';
        case 'list':
            return $CONFIG->url . 'pg/lds/browse/';
        case 'patterns':
            return $CONFIG->url . 'pg/lds/query/';
    }
}

      function iconSupport ($type)
{
    $supported = array("doc",'webcollagerest','glueps','openglm','cadmos','project_design');

    return in_array($type, $supported);
}

      function detailedHistorySupport($lds)
{
    $supported = array("google_docs");

    return in_array($lds->editor_type, $supported);
}*/