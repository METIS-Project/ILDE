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
mb_internal_encoding("UTF-8");
$script_path = explode('/', __DIR__);
$nfolders = count($script_path);
$base_folder = array_slice($script_path, 0, $nfolders - 2);
$base_folder = implode('/', $base_folder);

session_name("LdShake_" . hash('crc32b', $base_folder));
session_start();
session_write_close();

$file = $_GET['file'];
$file = preg_replace("/[^\pL\s\pNd\pP]+/u", "", $file);
$file = preg_replace("/\pP\pP\//u", "", $file);
$content = $_SESSION['editors_content'];
$path = $content . 'content/webcollagerest/' . $file;

if(!file_exists($path)) {
    exit();
}

ini_set('zlib.output_compression', 0);

$file_point = explode('.', $file);
$extension = $file_point[count($file_point)-1];
$extension = strtolower($extension);

switch($extension) {
    case 'html':
    case 'xhtml':
    case 'htm':
        $mime = 'text/html';
        break;
    case 'jpeg':
    case 'jpg':
        $mime = 'image/jpeg';
        break;
    case 'gif':
        $mime = 'image/gif';
        break;
    case 'js':
        $mime = 'text/javascript';
        break;
    case 'css':
        $mime = 'text/css';
        break;
    case 'svg':
        $mime = 'image/svg+xml';
        break;
    case 'png':
        $mime = 'image/png';
        break;
    case 'bmp':
        $mime = 'image/bmp';
        break;
    case 'mp3':
        $mime = 'audio/mpeg';
        break;
    case 'ogg':
    case 'ogv':
        $mime = 'application/ogg';
        break;
    case 'mp4':
        $mime = 'video/mp4';
        break;
    case 'mpeg':
    case 'mpg':
        $mime = 'video/mpeg';
        break;
    case 'mov':
        $mime = 'video/quicktime';
        break;
    case 'flv':
        $mime = 'video/x-flv';
        break;
    case 'zip':
        $mime = 'application/zip';
        break;
    case 'rar':
        $mime = 'application/x-rar-compressed';
        break;
    case 'dmg':
        $mime = 'application/x-apple-diskimage';
        break;
    case 'jar':
        $mime = 'application/java-archive';
        break;
    default:
        $finfo = finfo_open(FILEINFO_MIME_TYPE);
        $mime = finfo_file($finfo, $path);
        finfo_close($finfo);
}

header('Content-Type: '.$mime);
echo file_get_contents($path);

//require_once __DIR__ . '/../../config/config.php';

