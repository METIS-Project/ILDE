<?php

/**
 * SOme useful functions that may be useful all around the site.
 * @author Pau Moreno
 *
 */
class Utils
{
	/**
	 * Receives an array of objects and a k and v as field names of these objects.
	 * Returns an array where every element has a k as index and v as value.
	 * If k is not defined, the array will be numerically indexed.
	 */
	public static function objExtract ($arr, $v, $k = null)
	{
		$res = array ();
		
		foreach ($arr as $elem)
		{
			if (is_null($k))
				$res[] = $elem->$v;
			else
				$res[$elem->$k] = $elem->$v;
		}
		
		return $res;
	}
	
	/**
	 * SOrts an array of objects by their properties.
	 * Usage:
	 *   osort($items, array("Color" => true, "Size" => false));
	 * or
	 *   osort($items, "Color"); 
	 */
	public static function osort(&$array, $props)
	{
	    if(!is_array($props))
	        $props = array($props => true);
	       
	    usort($array, function($a, $b) use ($props) {
	        foreach($props as $prop => $ascending)
	        {
	            if($a->$prop != $b->$prop)
	            {
	                if($ascending)
	                {
	                	if (is_string($a->$prop))
	                		return (strcasecmp ($a->$prop, $b->$prop) > 0) ? 1 : -1;
	                	else 
	                    	return $a->$prop > $b->$prop ? 1 : -1;
	                }
	                else
	                {
	                	if (is_string($a->$prop))
	                		return (strcasecmp ($b->$prop, $a->$prop) > 0) ? 1 : -1;
	                	else
	  	     	            return $b->$prop > $a->$prop ? 1 : -1;
	                }
	            }
	        }
	        return -1; //if all props equal       
	    });   
	}
	
	/**
	 * Converts from
	 * "  this          text
	 *    string 
	 *    
	 *   example  "
	 * 
	 * to
	 * 
	 * "this text string example"
	 * 
	 * In addition, it removes a hidden DIV that firebug adds to each html.
	 */
	public static function normalizeSpace ($string)
	{
		//Remove the firebug div fisr
		$string = preg_replace('/<div.*_firebugConsole.*>\s*&nbsp;<\/div>/', '', $string);
		return trim(preg_replace('/\s+/', ' ', $string));
	}
	
	/**
	 * Returns an html js reference tag, with an appended last modified timestamp as a querystring
	 * THis avoids the browsers from caching older versions of js scripts.
	 * Enter description here ...
	 * @param unknown_type $module
	 * @param unknown_type $js
	 */
	public static function getJsDeclaration ($module, $js)
	{
		global $CONFIG;
		
		if (file_exists($CONFIG->path.'mod/'.$module.'/javascript/'.$js.'.js'))
			$appendix = "?t=".filectime($CONFIG->path.'mod/'.$module.'/javascript/'.$js.'.js');
		else
			$appendix = '';
			
		return '<script type="text/javascript" src="'.$CONFIG->url.'mod/'.$module.'/javascript/'.$js.'.js'.$appendix.'"></script>';
	}
}