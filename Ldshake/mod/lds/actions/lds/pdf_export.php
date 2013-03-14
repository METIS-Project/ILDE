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

/**
 * Exports an LdS document to a PDF file. Uses the wkhtmltopdf tool ( https://github.com/antialize/wkhtmltopdf )
 */

global $CONFIG;

$doc_guid = get_input ('docId');
$doc = get_entity ($doc_guid);

//Wrap the contents into a utf-8 html string. This will allow accents and other alphabets to be displayed correctly
//Also add some basic styling to resemble the view in the web (line height)
$contents = <<<EOD
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<style type="text/css">
		body {
			font-family: Arial, Verdana, sans-serif;
			font-size: 12pt;
			line-height: 1.5em;
		}
		</style>
	</head>
	<body>
		{$doc->description}
	</body>
</html>
EOD;

//Generate temp filenames
$inname = "{$CONFIG->tmppath}HTM_{$doc_guid}.html";
$outname = "{$CONFIG->tmppath}PDF_{$doc_guid}.pdf";

//Save the document data to the file
$handle = fopen($inname, "w");
fwrite($handle, $contents);
fclose($handle);

//Create the PDF (custom margins to respect web layout)
exec("{$CONFIG->pdf_converter_location} -L 11mm -R 12mm $inname $outname");

//Send the PDF to the client
header("Content-disposition: attachment; filename=\"{$doc->title}.pdf\"");
header("Content-type: application/pdf");
readfile($outname);

//Remove the temp files
unlink($inname);
unlink($outname);
