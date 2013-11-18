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
$license = "";

$lds=get_entity($doc->lds_guid);
if($lds->license) {
    $license = elgg_view("lds/license_banner", array("lds"=>$lds));
}

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

            /* MSO */
            /* Style Definitions */
        p.MsoNormal, li.MsoNormal, div.MsoNormal
        {margin:0cm;
            margin-bottom:.0001pt;
            font-size:11.0pt;
            font-family:"Arial","sans-serif";}

        p.MsoHeading7, li.MsoHeading7, div.MsoHeading7
        {mso-style-link:"Heading 7 Char";
            margin-top:12.0pt;
            margin-right:0cm;
            margin-bottom:3.0pt;
            margin-left:64.8pt;
            text-indent:-64.8pt;
            font-size:11.0pt;
            font-family:"Arial","sans-serif";}

        p.MsoHeading8, li.MsoHeading8, div.MsoHeading8
        {mso-style-link:"Heading 8 Char";
            margin-top:12.0pt;
            margin-right:0cm;
            margin-bottom:3.0pt;
            margin-left:72.0pt;
            text-indent:-72.0pt;
            font-size:11.0pt;
            font-family:"Arial","sans-serif";
            font-style:italic;}

        p.MsoHeading9, li.MsoHeading9, div.MsoHeading9
        {mso-style-link:"Heading 9 Char";
            margin-top:12.0pt;
            margin-right:0cm;
            margin-bottom:3.0pt;
            margin-left:79.2pt;
            text-indent:-79.2pt;
            font-size:9.0pt;
            font-family:"Arial","sans-serif";
            font-weight:bold;
            font-style:italic;}

        p.MsoToc1, li.MsoToc1, div.MsoToc1
        {margin-top:0cm;
            margin-right:0cm;
            margin-bottom:5.0pt;
            margin-left:0cm;
            font-size:11.0pt;
            font-family:"Arial","sans-serif";}

        p.MsoToc2, li.MsoToc2, div.MsoToc2
        {margin-top:0cm;
            margin-right:0cm;
            margin-bottom:5.0pt;
            margin-left:11.0pt;
            font-size:11.0pt;
            font-family:"Arial","sans-serif";}

        p.MsoCommentText, li.MsoCommentText, div.MsoCommentText
        {mso-style-link:"Comment Text Char";
            margin:0cm;
            margin-bottom:.0001pt;
            font-size:10.0pt;
            font-family:"Arial","sans-serif";}

        p.MsoHeader, li.MsoHeader, div.MsoHeader
        {mso-style-link:"Header Char";
            margin:0cm;
            margin-bottom:.0001pt;
            font-size:11.0pt;
            font-family:"Arial","sans-serif";}

        p.MsoFooter, li.MsoFooter, div.MsoFooter
        {mso-style-link:"Footer Char";
            margin:0cm;
            margin-bottom:.0001pt;
            font-size:11.0pt;
            font-family:"Arial","sans-serif";}

        p.MsoCaption, li.MsoCaption, div.MsoCaption
        {margin-top:0cm;
            margin-right:0cm;
            margin-bottom:10.0pt;
            margin-left:0cm;
            font-size:9.0pt;
            font-family:"Arial","sans-serif";
            color:#4F81BD;
            font-weight:bold;}

        span.MsoCommentReference
        {font-family:"Times New Roman","serif";}

        p.MsoTitle, li.MsoTitle, div.MsoTitle
        {mso-style-link:"Title Char";
            margin-top:12.0pt;
            margin-right:0cm;
            margin-bottom:3.0pt;
            margin-left:0cm;
            text-align:center;
            font-size:16.0pt;
            font-family:"Arial","sans-serif";
            font-weight:bold;}

        p.MsoSubtitle, li.MsoSubtitle, div.MsoSubtitle
        {mso-style-link:"Subtitle Char";
            margin-top:0cm;
            margin-right:0cm;
            margin-bottom:3.0pt;
            margin-left:0cm;
            text-align:center;
            font-size:12.0pt;
            font-family:"Arial","sans-serif";}

        span.MsoHyperlink
        {font-family:"Times New Roman","serif";
            color:blue;
            text-decoration:underline;}

        span.MsoHyperlinkFollowed
        {font-family:"Times New Roman","serif";
            color:purple;
            text-decoration:underline;}

        p.MsoCommentSubject, li.MsoCommentSubject, div.MsoCommentSubject
        {mso-style-link:"Comment Subject Char";
            margin:0cm;
            margin-bottom:.0001pt;
            font-size:10.0pt;
            font-family:"Arial","sans-serif";
            font-weight:bold;}

        p.MsoAcetate, li.MsoAcetate, div.MsoAcetate
        {mso-style-link:"Balloon Text Char";
            margin:0cm;
            margin-bottom:.0001pt;
            font-size:8.0pt;
            font-family:"Tahoma","sans-serif";}

        p.MsoRMPane, li.MsoRMPane, div.MsoRMPane
        {margin:0cm;
            margin-bottom:.0001pt;
            font-size:11.0pt;
            font-family:"Arial","sans-serif";}

        p.MsoListParagraph, li.MsoListParagraph, div.MsoListParagraph
        {margin-top:0cm;
            margin-right:0cm;
            margin-bottom:0cm;
            margin-left:36.0pt;
            margin-bottom:.0001pt;
            font-size:11.0pt;
            font-family:"Arial","sans-serif";}

        p.MsoListParagraphCxSpFirst, li.MsoListParagraphCxSpFirst, div.MsoListParagraphCxSpFirst
        {margin-top:0cm;
            margin-right:0cm;
            margin-bottom:0cm;
            margin-left:36.0pt;
            margin-bottom:.0001pt;
            font-size:11.0pt;
            font-family:"Arial","sans-serif";}

        p.MsoListParagraphCxSpMiddle, li.MsoListParagraphCxSpMiddle, div.MsoListParagraphCxSpMiddle
        {margin-top:0cm;
            margin-right:0cm;
            margin-bottom:0cm;
            margin-left:36.0pt;
            margin-bottom:.0001pt;
            font-size:11.0pt;
            font-family:"Arial","sans-serif";}

        p.MsoListParagraphCxSpLast, li.MsoListParagraphCxSpLast, div.MsoListParagraphCxSpLast
        {margin-top:0cm;
            margin-right:0cm;
            margin-bottom:0cm;
            margin-left:36.0pt;
            margin-bottom:.0001pt;
            font-size:11.0pt;
            font-family:"Arial","sans-serif";}

        p.MsoTocHeading, li.MsoTocHeading, div.MsoTocHeading
        {margin-top:24.0pt;
            margin-right:0cm;
            margin-bottom:0cm;
            margin-left:0cm;
            margin-bottom:.0001pt;
            line-height:115%;
            page-break-after:avoid;
            font-size:14.0pt;
            font-family:"Cambria","serif";
            color:#365F91;
            font-weight:bold;}

        span.Heading1Char
        {mso-style-name:"Heading 1 Char";
            mso-style-link:"Heading 1";
            font-family:"Arial","sans-serif";
            font-weight:bold;}

        span.Heading2Char
        {mso-style-name:"Heading 2 Char";
            mso-style-link:"Heading 2";
            font-family:"Cambria","serif";
            font-weight:bold;
            font-style:italic;}

        span.Heading3Char
        {mso-style-name:"Heading 3 Char";
            mso-style-link:"Heading 3";
            font-family:"Cambria","serif";
            font-weight:bold;}

        span.Heading4Char
        {mso-style-name:"Heading 4 Char";
            mso-style-link:"Heading 4";
            font-family:"Calibri","sans-serif";
            font-weight:bold;}

        span.Heading5Char
        {mso-style-name:"Heading 5 Char";
            mso-style-link:"Heading 5";
            font-family:"Calibri","sans-serif";
            font-weight:bold;
            font-style:italic;}

        span.Heading6Char
        {mso-style-name:"Heading 6 Char";
            mso-style-link:"Heading 6";
            font-family:"Calibri","sans-serif";
            font-weight:bold;}

        span.Heading7Char
        {mso-style-name:"Heading 7 Char";
            mso-style-link:"Heading 7";
            font-family:"Calibri","sans-serif";}

        span.Heading8Char
        {mso-style-name:"Heading 8 Char";
            mso-style-link:"Heading 8";
            font-family:"Calibri","sans-serif";
            font-style:italic;}

        span.Heading9Char
        {mso-style-name:"Heading 9 Char";
            mso-style-link:"Heading 9";
            font-family:"Cambria","serif";}

        span.HeaderChar
        {mso-style-name:"Header Char";
            mso-style-link:Header;
            font-family:"Arial","sans-serif";}

        span.TitleChar
        {mso-style-name:"Title Char";
            mso-style-link:Title;
            font-family:"Cambria","serif";
            font-weight:bold;}

        span.SubtitleChar
        {mso-style-name:"Subtitle Char";
            mso-style-link:Subtitle;
            font-family:"Cambria","serif";}

        span.FooterChar
        {mso-style-name:"Footer Char";
            mso-style-link:Footer;
            font-family:"Arial","sans-serif";}

        span.BalloonTextChar
        {mso-style-name:"Balloon Text Char";
            mso-style-link:"Balloon Text";}

        span.CommentTextChar
        {mso-style-name:"Comment Text Char";
            mso-style-link:"Comment Text";
            font-family:"Arial","sans-serif";}

        span.CommentSubjectChar
        {mso-style-name:"Comment Subject Char";
            mso-style-link:"Comment Subject";
            font-family:"Arial","sans-serif";
            font-weight:bold;}

        span.apple-converted-space
        {mso-style-name:apple-converted-space;}

        .MsoChpDefault
        {font-size:10.0pt;}

            /* Page Definitions */
        @page WordSection1
        {size:595.3pt 841.9pt;
            margin:62.35pt 62.35pt 62.35pt 62.35pt;}
        div.WordSection1
        {page:WordSection1;}
        @page WordSection2
        {size:595.3pt 841.9pt;
            margin:72.0pt 90.0pt 72.0pt 90.0pt;}
        div.WordSection2
        {page:WordSection2;}


        .MsoTitle a,
        .MsoSubtitle a,
        #the_lds h1 a,
        #the_lds h2 a
        {
            color: black!important;
            text-decoration:  none!important;
        }

        p.MsoListParagraphCxSpFirst,
        p.MsoListParagraphCxSpMiddle,
        p.MsoListParagraphCxSpLast,
        p.MsoListParagraph {
            margin-left: 18px!important;
        }

        table {
            width: 100%;
        }

        .license_banner {
            display: none;
        }

		</style>
	</head>
	<body>
		{$doc->description}
        {$license}
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
