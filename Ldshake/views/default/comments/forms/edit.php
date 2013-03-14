<?php

    /**
	 * Elgg comments add form
	 * 
	 * @package Elgg
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd <info@elgg.com>
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.com/
	 * 
	 * @uses $vars['entity']
	 */
	 
	 if (isset($vars['entity']) && isloggedin()) {
    	 
		$form_body .= '<div style="float:left; padding: 30px 20px 0 0;"><a href="'.get_loggedin_user()->getUrl().'"><img src="'.get_loggedin_user()->getIcon('small').'" /></a></div>'; 
		$form_body .= '<div style="padding:0 0 0 60px;"><div style="font-weight:bold; padding-bottom: 10px;">'.T("Add your comment").'</div>'.elgg_view('input/longtext',array('internalname' => 'generic_comment'));
		$form_body .= elgg_view('input/hidden', array('internalname' => 'entity_guid', 'value' => $vars['entity']->getGUID()));
		$form_body .= elgg_view('input/submit', array('value' => T("Add comment"))) . "</div>";
		 
		echo elgg_view('input/form', array('body' => $form_body, 'action' => "{$vars['url']}action/comments/add"));

    }
    
?>