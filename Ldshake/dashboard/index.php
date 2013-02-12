<?php

	/**
	 * Elgg dashboard
	 * 
	 * @package Elgg
	 * @subpackage Core
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.org/
	 */

	// Get the Elgg engine
		require_once(dirname(dirname(__FILE__)) . "/engine/start.php");

	// Ensure that only logged-in users can see this page
		gatekeeper();
		
	// Set context and title
		set_context('dashboard');
		set_page_owner(get_loggedin_userid());
		$title = elgg_echo('dashboard');
		
		/*
	// wrap intro message in a div
		$intro_message = elgg_view('dashboard/blurb');
		
	// Try and get the user from the username and set the page body accordingly
		$body = elgg_view_layout('widgets',"","",$intro_message);
		*/
		/// LdShake change ///
									
		
		/*
		//columna 1
		
		$content1col=elgg_view_title(elgg_echo('dashboard:brieftools'));
		$menu = get_register('menu');
		if (is_array($menu) && sizeof($menu) > 0) {
			$alphamenu = array();
			foreach($menu as $item) {
				$alphamenu[$item->name] = $item;
			}
			ksort($alphamenu);
		
		foreach($alphamenu as $item) {
    			
    			$content1col.= "<li><a href=\"{$item->value}\">" . $item->name . "</a></li>";
    			
			}
	}
		
			$content1col= elgg_view('canvas_header/submenu_group', array(
												'submenu' =>$content1col,
												'group_name' => 'tyu'
											));
											
											
											////////////////////////////////////
											
											
		$content2col=elgg_view_title(elgg_echo('dashboard'));
		$content3col = elgg_view_title(elgg_echo('content:latest')).list_registered_entities(0,4,true,false,array('object','group'));
		
		$body = elgg_view_layout('three_column_left_sidebar',  $content3col  , $content2col, $content1col);
		*/
		
		//fin miguel
		
		
		
		
		
		/*
		$body = '
		<div id="dashboard_info">

<p>
	<a href="dashboard/latest.php">hola</a>
</p>
<p>
adios

</p>
</div>
<div id="dashboard_info">

<p>
	<a href="dashboard/latest.php">hola</a>
</p>
<p>
adios

</p>
</div>
		
		
		'; */
		////////////////////////////////////////////////////////////////////////////////////////////
		
		
		/// dashboard diseño tablas=
		
		$body="
		<DIV STYLE=\"position:relative; background-color:#D0F5A9;\" ALIGN=CENTER>
<h2> Welcome to <span style=\"color: #709900;\">LdShake</span><br> a site where teachers <span style=\"color: #960001;\">(LdShakers)</span> co-edit and share Learning design Solutions <span style=\"color: #960001;\">(LdS)</span></h2></DIV>
";
/*
		$menu = get_register('menu');
		
		if (is_array($menu) && sizeof($menu) > 0) {
			$alphamenu = array();
			foreach($menu as $item) {
				$alphamenu[$item->name] = $item;
			}
			ksort($alphamenu);
			
$toolbox='
		<ul id="topbardropdownmenu">
		<li><a href="#">'.elgg_echo('tools').'<ul>';

			foreach($alphamenu as $item) {
    			$toolbox.= '<li><a href=\"'. $item->value .'\">' . $item->name . '</a></li>';
    			} 
		
$toolbox.='</ul><!--[if lte IE 6]></td></tr></table></a><![endif]--></li></ul>';

		}
*/


$body.="

<br><table WIDTH=100% HEIGHT=100% VALIGN=MIDDLE>
			<tr><td><IMG SRC=\"http://ldshake.upf.edu/ldshake/_graphics/ld-shake-1.PNG\" > 					</td> 			  <td>
<br><B >Shake hands with other teachers!</B>
<br> Manage de different groups of LdShakers with whom you are sharing LdS
			</td><td rowspan=\"2\" onmouseover=\" \"><img src=\"http://ldshake.upf.edu/ldshake/_graphics/tooboxphoto.PNG\"/></td><td><IMG SRC=\"http://ldshake.upf.edu/ldshake/_graphics/ld-shake-2.PNG\" WIDTH=65% HEIGHT=65% align=center></td><td >
<br><B > Shake different learning design solutions!		</B>
<br> Search for LdS proposed by other LdShakers, or collaborate with them in their co-edition!</td></tr>
			<tr><td><br><IMG SRC=\"http://ldshake.upf.edu/ldshake/_graphics/ld-shake-3.PNG\" WIDTH=90% HEIGHT=90% align=center></td> <td>
<br><B > Shake your students with the learning designs!	</B>
<br> Publish the LdS you created so that they can be publicly available and linked to using a URL!</td>
			<td><br><IMG SRC=\"http://ldshake.upf.edu/ldshake/_graphics/ld-shake-4.PNG\" ></td> <td>
<br><B > Shake up your way of working!</B>
<br>  Create your LdS and share them with other LdShakers so that they can see, comment or co-edit them!</td></tr>
</table>
		";
		
		$body = elgg_view_layout('one_column',$body);
/// LdShake change ///
		page_draw($title, $body);
		
?>