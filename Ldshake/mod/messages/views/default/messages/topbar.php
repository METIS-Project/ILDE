<?php

	/**
	 * Elgg messages topbar extender
	 * 
	 * @package ElggMessages
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd <info@elgg.com>
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.com/
	 */
	 
	 //need to be logged in to send a message
	 gatekeeper();

	//get unread messages
	$num_messages = count_unread_messages();
	if($num_messages){
		$num = $num_messages;
	} else {
		$num = 0;
	}

	if($num == 0){

?>

	<li><a href="<?php echo $vars['url']; ?>pg/messages/<?php echo $_SESSION['user']->username; ?>" class="privatemessages">&nbsp;</a></li>
	
<?php
    }else{
?>

    <li><a href="<?php echo $vars['url']; ?>pg/messages/<?php echo $_SESSION['user']->username; ?>" class="privatemessages_new"><?php echo $num; ?></a></li>
	
<?php
    }
?>