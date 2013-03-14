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

<?php extract ($vars) ?>
<div id="one_column">
	<div id="content_area_user_title">
		<h2><?php echo $lds->title ?></h2>
	</div>
	<div id="lds_view_actions">
		<input type="hidden" id="lds_edit_guid" name="guid" value="<?php echo $lds->guid ?>" />
		<input type="hidden" id="lds_base_url" name="guid" value="<?php echo $url ?>pg/lds/" />
		<a class="rightbutton" id="lds_recover_button" href="#">Untrash this LdS</a>
	</div>
	<ul id="lds_view_tabs">
		<?php foreach ($ldsDocs as $doc): ?>
			<?php if ($doc->guid == $currentDocId): ?>
			<li class="activetab"><?php echo $doc->title ?></li>
			<?php else: ?>
			<li><a href="<?php echo lds_viewTools::url_for($lds, 'viewtrashed').'doc/'.$doc->guid.'/'?>"><?php echo $doc->title ?></a></li>
			<?php endif; ?>
		<?php endforeach; ?>
	</ul>
	<div id="the_lds">
		<?php echo $currentDoc->description ?>
	</div>
</div>