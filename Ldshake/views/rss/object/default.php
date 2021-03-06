<?php

	/**
	 * Elgg default object view
	 * 
	 * @package Elgg
	 * @subpackage Core
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.org/
	 */

	$title = $vars['entity']->title;
	if (empty($title)) {
		$title = substr($vars['entity']->description,0,32);
		if (strlen($vars['entity']->description) > 32)
			$title .= " ...";
	}

?>

	<item>
	  <guid isPermaLink='true'><?php echo $vars['entity']->getURL(); ?></guid>
	  <pubDate><?php echo date("r",$vars['entity']->time_created) ?></pubDate>
	  <link><?php echo $vars['entity']->getURL(); ?></link>
	  <title><![CDATA[<?php echo $title; ?>]]></title>
	  <description><![CDATA[<?php echo (autop($vars['entity']->description)); ?>]]></description>
	</item>
