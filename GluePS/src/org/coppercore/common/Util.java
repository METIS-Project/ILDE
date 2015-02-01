/*
 * CopperCore, an IMS-LD level C engine
 * Copyright (C) 2003, 2004 Harrie Martens and Hubert Vogten
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

import java.io.File;

/**
 * This class contains static utility functions.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.21 $, $Date: 2005/01/11 13:15:09 $
 */
public class Util {

  /** Indicates a type cast error occured. */
  public final static int TYPE_CAST_ERROR = 0;


  /**
   * Return normalized version of path and removes the trailing (back)slash.
   *
   * @param path String path being normalized
   * @return String normalized version of path without trailing (back)slash
   */
  public static String convertPathToPlatform(String path) {
    String result = path.replace('\\', '/').replace('/', File.separatorChar);
    if (result.endsWith(File.separator)) {
      result = result.substring(0, result.length() - 1);
    }
    return result;
  }

  /**
   * Removes trailing slash from the uri.
   * @param uri String uri to be normalized
   * @return String the normailzed uri
   */
  public static String removeTrailingSlash(String uri) {
    if (uri.endsWith("/")) {
      uri = uri.substring(0, uri.length() - 1);
    }
    return uri;
  }

  /**
   * If the passed string is null returns [null] otherwise just returns the original string.
   * @param value String the string to check for a null value
   * @return String the checked string
   */
  public static String nullStr(String value) {
    return (value == null ? "[null]" : value);
  }

  /**
   * If the passed string is null returns [null] otherwise just returns the
   * original string enclosed in quotes.
   *
   * @param value String the string to check for a null value and to enclose in
   *   quotes
   * @return String the checked string
   */
  public static String quotedStr(String value) {
    return (value == null ? "[null]" : '"' + value + '"');

  }

  /**
   * Converts a string value to a boolean.
   *
   * <p>If the string equals "1" or "true" the method returns true, if the
   * passed string equals "0" or "false" the method returns false, otherwise the
   * methos returns null.
   *
   * @param value String the text to convert to a boolean value
   * @return Boolean the value of the converted string or null if the value
   *   could not be converted
   */
  public static Boolean stringToBoolean(String value) {
    Boolean result = null;
    if (value != null) {

      if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1")) {
        result = Boolean.TRUE;

      }
      else if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("0")) {
        result = Boolean.FALSE;
      }
    }
    return result;
  }

  /**
   * Escapes the single quote in the passed string with "&amp;#39;".
   * @param s String the string to convert
   * @return String the converted string
   */
  public static String escapeSingleQuote(String s) {
    StringBuffer result = new StringBuffer(s.length() * 6 / 5);

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c == '\'') {
        result.append("&amp;#39;");
      }
      else {
        result.append(c);
      }
    }

    return result.toString();
  }

  /**
   * Deletes all files and subdirectories under it.
   *
   * <p>Returns true if all deletions were successful. If a deletion fails, the
   * method stops attempting to delete and returns false.</p>
   *
   * @param dir File or Directory
   * @return boolean false if deletion fails, true otherwise
   */
  public static boolean deleteFolder(File dir) {
    if (dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteFolder(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
    }

    // The directory is now empty so delete it
    return dir.delete();

  }
}
