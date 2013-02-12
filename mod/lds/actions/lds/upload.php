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


global $CONFIG;

$user = get_loggedin_user();

$funcNum2 = get_Input('CKEditorFuncNum', 'CKEditorFuncNum');

$file = new ElggFile();
$filestorename = strtolower(time().$_FILES['upload']['name']);
$file->setFilename($filestorename);
$file->setMimeType($_FILES['upload']['type']);
$file->owner_guid = $user->guid;
$file->subtype = "lds_uploaded_file";
$file->originalfilename = $filestorename;

$file->access_id = 2;

$file->open("write");
$file->write(get_uploaded_file('upload'));
$file->close();

$result = $file->save();

if ($result)
{
	//903 is the width of al LdS both in the edit form and in the view page.
	$master = get_resized_image_from_existing_file($file->getFilenameOnFilestore(),903,550);

	if ($master !== false)
	{

		$filehandler = new ElggFile();
		$filehandler->setFilename($filestorename);
		$filehandler->setMimeType($_FILES['upload']['type']);
		$filehandler->owner_guid = $user->guid;
		$filehandler->subtype = "lds_uploaded_file";
		$filehandler->originalfilename = $filestorename;
		$filehandler->access_id = 2;
		$filehandler->open("write");
		$filehandler->write($master);
		$filehandler->close();
		$filehandler->save();
		
		$url = $CONFIG->url.'pg/lds/imgdisplay/'.$filehandler->guid.'/';

		echo '<script type="text/javascript">
		window.parent.CKEDITOR.tools.callFunction('.$funcNum2.', "'.$url.'","Success");
		</script>';
		exit();

	}
	else
	{

		$_SESSION['FILE_WRITE_LOG']['file_load']['FAILURE'] = 'True';

		echo '<script type="text/javascript">
		window.parent.CKEDITOR.tools.callFunction('.$funcNum2.', "","");
		</script>';
		exit();
	}
}
?>