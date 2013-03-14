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
		<h2><?php echo $lds->title ?>: <?php echo T("Revision of %1 (%2) by %3", $document->title, date('j M Y H:i', $revDate), $reviser->name) ?></h2>
	</div>
	<div id="lds_view_actions">
		<?php if (!is_null($prevId)) : ?>
			<a href="<?php echo "{$url}pg/lds/viewrevision/$prevId/" ?>">« <?php echo T("View previous revision") ?></a>
		<?php else: ?>
			<span><?php echo T("You are in the first revision") ?></span> 
		<?php endif; ?>
		|
		<?php if (!is_null($nextId)) : ?>
			<a href="<?php echo "{$url}pg/lds/viewrevision/$nextId/" ?>"><?php echo T("View next revision") ?> »</a>
		<?php else: ?>
			<span><?php echo T("You are in the last revision") ?></span>
		<?php endif; ?>
		<?php if (!is_null($prevId)) : ?>
			|
			<input type="checkbox" id="lds-rev-toggle-diff"> <?php echo T("View changes from previous revision") ?>
		<?php endif; ?>
		<a class="rightbutton" href="<?php echo lds_viewTools::url_for($lds, 'history') ?>"><?php echo T("Back to revision history") ?></a>
	</div>
	<div id="lds_revision_details">
		<a href="<?php echo $reviser->getUrl() ?>"><img src="<?php echo $reviser->getIcon('tiny') ?>" /></a> <?php echo T("Revised by %1 on %2", '<a href="'.$reviser->getUrl().'">'.$reviser->name.'</a>', date('j M Y H:i', $revDate)) ?>
		<?php if (!is_null($prevId)) : ?><span id="previous_revision_details">– <a href="<?php echo $prevReviser->getUrl() ?>"><img src="<?php echo $prevReviser->getIcon('tiny') ?>" /></a> <?php echo T("Comparing with previous revision by %1 on %2", '<a href="'.$prevReviser->getUrl().'">'.$prevReviser->name.'</a>', date('j M Y H:i', $prevRevision->time_created)) ?></span><?php endif; ?>
	</div>
	<div id="the_lds">
		<div id="revision_wrapper"><?php echo $revision->description ?></div>
		<div id="diff_wrapper"><?php echo $diff ?></div>
	</div>
</div>