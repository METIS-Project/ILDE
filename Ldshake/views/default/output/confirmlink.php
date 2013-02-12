<?php

	/**
	 * Elgg confirmation link
	 * A link that displays a confirmation dialog before it executes
	 * 
	 * @package Elgg
	 * @subpackage Core
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.org/
	 * 
	 * @uses $vars['text'] The text of the link
	 * @uses $vars['href'] The address
	 * @uses $vars['confirm'] The dialog text
	 * 
	 */

?>
<a href="<?php echo $vars['href']; ?>" onclick="return confirm('<?php echo addslashes($vars['confirm']); ?>');"<?php if (isset($vars['class'])) echo ' class="'.$vars['class'].'"'?>><?php echo $vars['text']; ?></a>