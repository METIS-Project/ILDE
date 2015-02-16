<?php

/*Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
Valladolid, Spain. https://www.gsic.uva.es/

This file is part of Web Collage.

Web Collage is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or 
(at your option) any later version.

Web Collage is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

/**
 *  Database functions related to the sectokens table
 *  @author Javier E. Hoyos Torio 
 */


/**
 * Insert a sectoken into the database
 * @param resource $link Identifier of the mysql link
 * @param string $sectoken The security token associated to the design
 * @param string $docid The identifier of the design
 * @return mixed String containing the sectoken or false if it can not be inserted
 */
function createSectokenDB($link, $sectoken, $docid) {
    $sql = "INSERT INTO sectokens (sectoken, docid) values('" . mysql_real_escape_string($sectoken) . "', " . $docid . ")";
    if (mysql_query($sql, $link)) {
        return $sectoken;
    }
    return false;
}

/**
 * Load a sectoken from the database
 * @param resource $link Identifier of the mysql link
 * @param string $sectoken The security token associated to the design
 * @return mixed Array containing the sectoken table entry or false if it can not be loaded
 */
function loadSectokenDB($link, $sectoken) {
    $sql = "select * from sectokens where sectoken='" . mysql_real_escape_string($sectoken) . "'";
    $result = mysql_query($sql, $link);
    if (mysql_num_rows($result) > 0){
        //Return as a associative array
        return mysql_fetch_assoc($result);
    }
    return false;
}

/**
 * Delete a sectoken entry from the database
 * @param resource $link Identifier of the mysql link
 * @param string $sectoken The security token associated to the design
 * @return boolean The sectoken entry has been deleted or not
 */
function deleteSectokenDB($link, $sectoken) {
    $sql = "delete from sectokens where sectoken='" . mysql_real_escape_string($sectoken) . "'";
    if (mysql_query($sql, $link)) {
        return true;
    }
    return false;
}

/**
 * Delete a sectoken from the database
 * @param resource $link Identifier of the mysql link
 * @param int $docid The document identifier
 * @return boolean The sectoken entry has been deleted or not
 */
function deleteSectokenDocumentDB($link, $docid) {
    $sql = "delete from sectokens where docid='" . mysql_real_escape_string($docid) . "'";
    if (mysql_query($sql, $link)) {
        return true;
    }
    return false;
}

?>
