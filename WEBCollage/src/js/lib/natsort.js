////////////////////////////////////////////////////
//                 NATURAL ORDER
////////////////////////////////////////////////////
/**
 * @defgroup   NaturalOrder   natcompare.js -- Perform 'natural order' comparisons of strings in JavaScript.
 * 
 * Copyright (C) 2005 by SCK-CEN (Belgian Nucleair Research Centre)
 * Written by Kristof Coomans <kristof[dot]coomans[at]sckcen[dot]be>
 * 
 * Based on the Java version by Pierre-Luc Paour, of which this is more or less a straight conversion.
 * Copyright (C) 2003 by Pierre-Luc Paour <natorder@paour.com>
 *
 * The Java version was based on the C version by Martin Pool.
 * Copyright (C) 2000 by Martin Pool <mbp@humbug.org.au>
 * 
 * @see  http://sourcefrog.net/projects/natsort/
 *      
 * @{ 
 */
 
/**
 * @brief   Check whether the specified char is whitespace.
 * @public  
 * @param[in]  a  [string] Only the first character is checked.
 * @return  [boolean] True if the first character is whitespace, false otherwise.
 */  
function isWhitespaceChar(a)
{
	var charCode = a.charCodeAt(0);
	return charCode <= 32?true:false;
}

/**
 * @brief   Check whether the specified char is digit.
 * @public  
 * @param[in]  a  [string] Only the first character is checked.
 * @return  [boolean] True if the first character is digit, false otherwise.
 */
function isDigitChar(a)
{
	var charCode = a.charCodeAt(0);
	return (charCode >= 48  && charCode <= 57)?true:false;
}

/**
 * @brief   Compare two strings using special algorithm.
 * Characters are compared one by one from the beginnings of the string. The
 * string containing the first bigger number is taken as bigger value. When
 * the nondigit char is reached comparison ends and the longest run of digits
 * wins. If the runs of the digits are the same (includes strings containing
 * only nondigits chars, 0 is returned). Zeroes on the same positions is
 * terminator. 
 *       
 * @private
 * @param[in]  a  [string]
 * @param[in]  b  [string]
 * @return
 *    - -1  If the 'a' is smaller than 'b'.
 *    - 0   'a' and 'b' are the same corresponding to the rules above.
 *    - 1   If the 'a' is bigger than 'b'.
 * 
 * @see  natcompare()    
 * Examples:
 *    - compareRight("123", "321") == -1
 *    - compareRight("12a", "120") == -1
 *    - compareRight("abc", "ccc") == 0
 *    - compareRight("109", "100") == 0 (because of '0' on the second position)
 *    - compareRight("213", "211") == 1    
 */   
function compareRight(a,b)
{
    var ca,cb,bias = 0,ia = 0,ib = 0;

    // The longest run of digits wins.  That aside, the greatest
    // value wins, but we can't know that it will until we've scanned
    // both numbers to know that they have the same magnitude, so we
    // remember it in BIAS.
    for (;; ia++, ib++) {
		ca = a.charAt(ia);
        cb = b.charAt(ib);

        if (!isDigitChar(ca) && !isDigitChar(cb)) {
        	return bias;
        } else if (!isDigitChar(ca)) {
            return -1;
        } else if (!isDigitChar(cb)) {
            return +1;
        } else if (ca < cb) {
            if (bias == 0) {
                bias = -1;
            }
        } else if (ca > cb) {
            if (bias == 0)
                bias = +1;
        } else if (ca == 0 && cb == 0) {
            return bias;
        }
    }
}

/**
 * @brief   Natural Order String Comparison.
 * Computer string sorting algorithms generally don't order strings containing numbers in the same way that a human would do.
 * This library provides natural order string comparison.
 *    
 * Strings are sorted as usual, except that decimal integer substrings are compared on their numeric value. For example,
 *
 *  a < a0 < a1 < a1a < a1b < a2 < a10 < a20 
 *
 * Strings can contain several number parts:
 *
 *  x2-g8 < x2-y7 < x2-y08 < x8-y8 
 *
 * in which case numeric fields are separated by nonnumeric characters. Leading spaces are ignored. This works very well for IP addresses from log files, for example.
 *
 * Leading zeros are not ignored, which tends to give more reasonable results on decimal fractions.
 *
 *  1.001 < 1.002 < 1.010 < 1.02 < 1.1 < 1.3 
 *
 * Some applications may wish to change this by modifying the test that calls isspace.
 *
 * Performance is linear: each character of the string is scanned at most once, and only as many characters as necessary to decide are considered.
 *   
 * @public 
 * @param[in]  a  [string]
 * @param[in]  b  [string]
 * @return
 *    - -1  If the 'a' is smaller than 'b'.
 *    - 0   'a' and 'b' are the same corresponding to the rules above.
 *    - 1   If the 'a' is bigger than 'b'.
 *    
 * @see  http://sourcefrog.net/projects/natsort/  
 */ 
function natcompare(a,b) {
    /*Modificado para obtener el nombre
     *
     *
     **/
    var a = a.name;
    var b = b.name;
    /*
     * 
     */
    var ia = 0, ib = 0;
	var nza = 0, nzb = 0;
	var ca, cb;
	var result;

    while (true)
    {
        // only count the number of zeroes leading the last number compared
        nza = nzb = 0;

        ca = a.charAt(ia);
        cb = b.charAt(ib);

        // skip over leading spaces or zeros
        while ( isWhitespaceChar( ca ) || ca =='0' ) {
            if (ca == '0') {
            	nza++;
            } else {
                // only count consecutive zeroes
                nza = 0;
            }

            ca = a.charAt(++ia);
        }

        while ( isWhitespaceChar( cb ) || cb == '0') {
            if (cb == '0') {
                nzb++;
            } else {
                // only count consecutive zeroes
                nzb = 0;
            }

            cb = b.charAt(++ib);
        }

        // process run of digits
        if (isDigitChar(ca) && isDigitChar(cb)) {
            if ((result = compareRight(a.substring(ia), b.substring(ib))) != 0) {
                return result;
            }
        }

        if (ca == 0 && cb == 0) {
            // The strings compare the same.  Perhaps the caller
            // will want to call strcmp to break the tie.
            return nza - nzb;
        }

        if (ca < cb)
            return -1;
        else
        if (ca > cb)
            return +1;

        ++ia; ++ib;
    }
}


