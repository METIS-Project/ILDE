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

<?php
if (get_context() == 'lds_exec_new' || get_context() == 'lds_exec_edit' || get_context() == 'lds_exec_neweditor' || get_context() == 'lds_exec_editeditor'):
?>
	<script type="text/javascript" src="<?php echo $vars['url'] ?>mod/lds/autoSuggest/jquery.autoSuggest.js"></script>
	<script type="text/javascript" src="<?php echo $vars['url'] ?>mod/lds/ckeditor/ckeditor.js"></script>
	<script type="text/javascript" src="<?php echo $vars['url'] ?>mod/lds/ckeditor/adapters/jquery.js"></script>
	<?php
	echo Utils::getJsDeclaration('lds', 'lds-share');
endif;
if (get_context() == 'lds_exec_new' || get_context() == 'lds_exec_edit'):
	echo Utils::getJsDeclaration('lds', 'lds-form');
elseif (get_context() == 'lds_exec_neweditor' || get_context() == 'lds_exec_editeditor'):
	echo Utils::getJsDeclaration('lds', 'lds-form-editor');
endif;
if (get_context() == 'lds_exec_main' || get_context() == 'lds_exec_browse' || get_context() == 'lds_exec_trashed'):
	echo Utils::getJsDeclaration('lds', 'lds-list');
endif;
if (get_context() == 'lds_exec_history'):
	echo Utils::getJsDeclaration('lds', 'lds-history');
endif;
if (get_context() == 'lds_exec_viewrevision' || get_context() == 'lds_exec_viewrevisioneditor'):
	echo Utils::getJsDeclaration('lds', 'lds-revision');
endif;
if (get_context() == 'lds_exec_view'):
	echo Utils::getJsDeclaration('lds', 'lds-view');
	echo Utils::getJsDeclaration('lds', 'lds-share');
elseif (get_context() == 'lds_exec_vieweditor'):
	echo Utils::getJsDeclaration('lds', 'lds-view_editor');
	echo Utils::getJsDeclaration('lds', 'lds-share');
endif;
if (get_context() == 'lds_exec_info'):
	echo Utils::getJsDeclaration('lds', 'lds-share');
endif;
if (get_context() == 'lds_exec_firststeps'):
	echo Utils::getJsDeclaration('lds', 'lds-firststeps');
endif;
echo Utils::getJsDeclaration('lds', 'lds-common')
?>