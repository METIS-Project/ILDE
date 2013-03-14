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


/**
 * Set of helper functions for the views of the LdS module 
 */

class lds_viewTools
{	
	public static function pagination ($count, $elementsPerPage = 10)
	{
		$params = array(
			'baseurl' => 'http://'.$_SERVER['SERVER_NAME'].$_SERVER['REQUEST_URI'],
			'offset' => get_input('offset') ?: 0,
			'count' => $count,
			'limit' => $elementsPerPage
		);
		
		return elgg_view('navigation/pagination', $params);
	}
	
	public static function tag_display ($tags, $class)
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
		
		return $CONFIG->url . 'pg/lds/'.$type.$editortype.'/' . $lds->guid . '/';
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
		}
	}
}