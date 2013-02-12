<?php
	/**
	 * Provide a way of setting your password
	 * 
	 * @package Elgg
	 * @subpackage Core
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.org/
	 */

	$user = page_owner_entity();
	
	if ($user) {
?>
	<h3><?php echo T("Account password"); ?></h3>
	<p>
		<?php echo T("Your new password"); ?>: 
		<?php
			echo elgg_view('input/password',array('internalname' => 'password'));
		?></p><p>
		<?php echo T("Your new password again"); ?>: <?php
			echo elgg_view('input/password',array('internalname' => 'password2'));
		?>
	</p>

<?php } ?>