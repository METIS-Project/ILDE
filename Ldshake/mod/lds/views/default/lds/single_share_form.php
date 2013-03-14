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

?>

<div id="lds_single_share_popup">
	<a class="close_popup<?php if ($am_i_starter) echo ' edit' ?>" id="lds_single_share_popup_close" href="#"><?php echo T("Done") ?></a>
	<h3><?php echo T("LdS Sharing options") ?></h3>
	<ul id="added_contacts">
		<li>
			<img src="<?php echo $starter->getIcon('small') ?>" />
			<span class="contactinfo" data-guid="<?php echo $starter->guid ?>"><?php echo $starter->name ?></span>
			<span class="static_label"><?php echo T("Starter") ?></span>
		</li>
		<?php if (!$am_i_starter): ?>
		<li>
			<img src="<?php echo get_loggedin_user()->getIcon('small') ?>" />
			<span class="contactinfo" data-guid="<?php echo get_loggedin_userid() ?>"><?php echo get_loggedin_user()->name ?></span>
			<span class="static_label"><?php echo T("Can edit") ?></span>		
		</li>
		<?php endif; ?>
	</ul>
	
	<?php if ($am_i_starter): ?>
	<div class="sharing_field" id="read_all_wrapper">
		<input type="checkbox" name="read_all" id="read_all" checked="checked" />
		<span><?php echo T("Allow all LdShakers to view this LdS") ?></span>
	</div>
	
	<div class="sharing_field">
		<div class="floated-field" style="margin-right:18px;visibility:hidden;" id="add_viewer_wrapper">
			<div><?php echo T("Add viewer (LdShaker or group):") ?></div>
			<input type="text" name="viewers" class="usersuggestbox" id="viewers" autocomplete="off" />
		</div>
		<div class="floated-field">
			<div><?php echo T("Add editor (LdShaker or group):") ?></div>
			<input type="text" name="editors" class="usersuggestbox" id="editors" autocomplete="off" />
		</div>
	</div>
	<?php endif; ?>
	
	<!-- Copys for the JS-generated elements -->
	<input type="hidden" id="copy_editors" value="<?php echo T("Can edit") ?>" />
	<input type="hidden" id="copy_viewers" value="<?php echo T("Can view") ?>" />
</div>