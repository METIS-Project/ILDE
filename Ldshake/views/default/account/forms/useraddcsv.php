<?php
/**
 * Elgg add user form.
 *
 * @package Elgg
 * @subpackage Core
 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
 * @author Curverider Ltd
 * @copyright Curverider Ltd 2008
 * @link http://elgg.org/
 */

$admin_option = false;
if (($_SESSION['user']->admin) && ($vars['show_admin']))
    $admin_option = true;

$form_body = "<p><label>" . T("CSV file") . "<br />" . elgg_view('input/file' , array('internalname' => 'filedata')) . "</label></p>";
$form_body .= elgg_view('input/submit', array('internalname' => 'submit', 'value' => elgg_echo('register'))) . "</p>";
?>


<div id="add-box">
    <h2><?php echo elgg_echo('adduser'); ?></h2>
    <?php echo elgg_view('input/form', array('action' => "{$vars['url']}action/registercsv", 'enctype' => "multipart/form-data", 'body' => $form_body)) ?>
</div>