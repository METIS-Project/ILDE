<?php

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
