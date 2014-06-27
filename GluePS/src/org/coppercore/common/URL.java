/*
 * CopperCore, an IMS-LD level C engine
 * Copyright (C) 2003 Harrie Martens and Hubert Vogten
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program (/license.txt); if not,
 * write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 *     Contact information:
 *     Open University of the Netherlands
 *     Valkenburgerweg 177 Heerlen
 *     PO Box 2960 6401 DL Heerlen
 *     e-mail: hubert.vogten@ou.nl or
 *             harrie.martens@ou.nl
 *
 *
 * Open Universiteit Nederland, hereby disclaims all copyright interest
 * in the program CopperCore written by
 * Harrie Martens and Hubert Vogten
 *
 * prof.dr. Rob Koper,
 * director of learning technologies research and development
 *
 */

package org.coppercore.common;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.coppercore.exceptions.URLSyntaxException;

/**
 * This class implements an URL capable of adding url contexts.
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.10 $, $Date: 2005/01/18 17:58:33 $
 */
public class URL {

  private String url;
  private boolean isAbsolute = false;

  /**
   * Constructs a new URL instance for the specified url.
   * @param anURL String
   * @throws URLSyntaxException
   */
  public URL(String anURL) throws URLSyntaxException {
  this(null, anURL);
  }

  /**
   * Construst a new URL instance for the specified url by combining it to the
   * specified context url.
   *
   * @param context URL defines the context of the new url
   * @param anURL String the new url which has to be combined with the context
   * @throws URLSyntaxException when there is an error constructing a valid url
   */
  public URL(URL context, String anURL) throws URLSyntaxException {
  try {

      if (anURL != null) {

        URI uri = new URI(anURL);

        if (uri.isAbsolute()) {
          //neglect context and URL is absolute

          //check if we are dealing with a valid absolute URL (URI is too forgiving)
          new java.net.URL(uri.toString());

          isAbsolute = true;
          url = anURL;
        }
        else {
          if (context == null) {
            //we are dealing with a relative URL without a context
            url = anURL;
          }
          else {
            //extract URL as String from context
            String contextAsString = context.toString();

            if (context.isAbsolute()) {
              //combine both the absolute context and relative URL into a single absolute URL

              isAbsolute = true;

              //make sure separator exists
              if (!contextAsString.endsWith("/")) {
                contextAsString += "/";
              }

              //create and validate new absolute URL
              java.net.URL myURL = new java.net.URL(new java.net.URL(
                  contextAsString), anURL);
              url = myURL.toString();
            }
            else {

              //combine to relative URLs
              if (anURL.startsWith("/")) {
                //relative path starting from root, ignore context
                url = anURL;
              }
              else {
                if ( (contextAsString != null) && (!contextAsString.equals("")) &&
                    (!contextAsString.endsWith("/"))) {
                  contextAsString += "/";
                }
                url = contextAsString + anURL;
              }
            }
          }
        }
      }
      else {
        url = "";
      }
    }

    catch (URISyntaxException ex) {
      throw new URLSyntaxException(ex.getMessage());
    }
    catch (MalformedURLException ex) {
      throw new URLSyntaxException(ex.getMessage());
    }
  }

  /**
   * Returns true if the url is an absolute url, otherwise return false.
   * @return boolean true if the url is an absolute url, otherwise return false.
   */
  public boolean isAbsolute() {
  return isAbsolute;
  }

  /**
   * Returns a string representation of the url.
   * @return String the string representation of the url
   */
  public String toString() {
  return url;
  }

  /**
   * Checks whether the given object equals this url.
   *
   * <p>The urls are considered equal when they are both of the same type and
   * when their members are equal.
   *
   * @param anObject Object the url to compare this instance to
   * @return boolean true if this URL instance and the given object are equal
   */
  public boolean equals(Object anObject) {

    if (! (anObject instanceof URL)) {
      return false;
    }

    if (this.isAbsolute() && ( (URL) anObject).isAbsolute()) {
      try {
        java.net.URL op1 = new java.net.URL(this.toString());
        java.net.URL op2 = new java.net.URL( ( (URL) anObject).toString());

        return (op1.equals(op2));
      }
      catch (MalformedURLException ex) {
        return false;
      }
    }
    return (unescape().equals( ( (URL) anObject).unescape()));
  }
  
  /**
   * Converts the url containing escaped characters to a normalized url without
   * these escape sequences.<p>
   * Escaped characters are %nn or + for spaces. 
   * @return the converted url
   */
  public String unescape() {
    StringBuffer sbuf = new StringBuffer () ;
    int l  = url.length() ;
    int ch = -1 ;
    int b, sumb = 0;
    for (int i = 0, more = -1 ; i < l ; i++) {
      /* Get next byte b from URL segment s */
      switch (ch = url.charAt(i)) {
	case '%':
	  ch = url.charAt (++i) ;
	  int hb = (Character.isDigit ((char) ch) 
		    ? ch - '0'
		    : 10+Character.toLowerCase((char) ch) - 'a') & 0xF ;
	  ch = url.charAt (++i) ;
	  int lb = (Character.isDigit ((char) ch)
		    ? ch - '0'
		    : 10+Character.toLowerCase ((char) ch)-'a') & 0xF ;
	  b = (hb << 4) | lb ;
	  break ;
	case '+':
	  b = ' ' ;
	  break ;
	default:
	  b = ch ;
      }
      /* Decode byte b as UTF-8, sumb collects incomplete chars */
      if ((b & 0xc0) == 0x80) {			// 10xxxxxx (continuation byte)
	sumb = (sumb << 6) | (b & 0x3f) ;	// Add 6 bits to sumb
	if (--more == 0) sbuf.append((char) sumb) ; // Add char to sbuf
      } else if ((b & 0x80) == 0x00) {		// 0xxxxxxx (yields 7 bits)
	sbuf.append((char) b) ;			// Store in sbuf
      } else if ((b & 0xe0) == 0xc0) {		// 110xxxxx (yields 5 bits)
	sumb = b & 0x1f;
	more = 1;				// Expect 1 more byte
      } else if ((b & 0xf0) == 0xe0) {		// 1110xxxx (yields 4 bits)
	sumb = b & 0x0f;
	more = 2;				// Expect 2 more bytes
      } else if ((b & 0xf8) == 0xf0) {		// 11110xxx (yields 3 bits)
	sumb = b & 0x07;
	more = 3;				// Expect 3 more bytes
      } else if ((b & 0xfc) == 0xf8) {		// 111110xx (yields 2 bits)
	sumb = b & 0x03;
	more = 4;				// Expect 4 more bytes
      } else /*if ((b & 0xfe) == 0xfc)*/ {	// 1111110x (yields 1 bit)
	sumb = b & 0x01;
	more = 5;				// Expect 5 more bytes
      }
      /* We don't test if the UTF-8 encoding is well-formed */
    }
    return sbuf.toString() ;
  }
  

}
