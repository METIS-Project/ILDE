/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Glue!PS.
 * 
 * Glue!PS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Glue!PS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
package org.coppercore.resources;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Localizes messages.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.6 $, $Date: 2005/01/11 13:15:13 $
 */
public class MessageUtil {
   private static ResourceBundle myResources = ResourceBundle.getBundle(
      "org.coppercore.resources.Messages");

  private static String getMessageString(String key) {
    return myResources.getString(key);
  }

  /**
   * Returns the localized and formatted string for message key.
   * @param key String the key of the message to return
   * @return String the localized and formatted message
   */
  public static String formatMessage(String key) {
    MessageFormat mf = new MessageFormat(getMessageString(key));
    return mf.format(new Object[0]);
  }

  /**
   * Returns the localized and formatted string for message key.
   *
   * <p>This overloaded version of the formatMessage method accepts one parameter.
   *
   * @param key String the key of the message to return
   * @param arg0 Object first parameter to the format routine
   * @return String the localized and formatted message
   */
  public static String formatMessage(String key, Object arg0) {
    MessageFormat mf = new MessageFormat(getMessageString(key));
    Object[] args = new Object[1];
    args[0] = arg0;
    return mf.format(args);
  }

  /**
   * Returns the localized and formatted string for message key.
   *
   * <p>This overloaded version of the formatMessage method accepts two parameters.
   *
   * @param key String the key of the message to return
   * @param arg0 Object first parameter to the format routine
   * @param arg1 Object second parameter to the format routine
   * @return String the localized and formatted message
   */
  public static String formatMessage(String key, Object arg0, Object arg1) {
    MessageFormat mf = new MessageFormat(getMessageString(key));
    Object[] args = new Object[2];
    args[0] = arg0;
    args[1] = arg1;
    return mf.format(args);
  }

  /**
   * Returns the localized and formatted string for message key.
   *
   * <p>This overloaded version of the formatMessage method accepts three parameters.
   *
   * @param key String the key of the message to return
   * @param arg0 Object first parameter to the format routine
   * @param arg1 Object second parameter to the format routine
   * @param arg2 Object third parameter to the format routine
   * @return String the localized and formatted message
   */
  public static String formatMessage(String key, Object arg0, Object arg1, Object arg2) {
    MessageFormat mf = new MessageFormat(getMessageString(key));
    Object[] args = new Object[3];
    args[0] = arg0;
    args[1] = arg1;
    args[2] = arg2;
    return mf.format(args);
  }

  /**
   * Returns the localized and formatted string for message key.
   *
   * <p>This overloaded version of the formatMessage method accepts four parameters.
   *
   * @param key String the key of the message to return
   * @param arg0 Object first parameter to the format routine
   * @param arg1 Object second parameter to the format routine
   * @param arg2 Object third parameter to the format routine
   * @param arg3 Object fourth parameter to the format routine
   * @return String the localized and formatted message
   */
  public static String formatMessage(String key, Object arg0, Object arg1, Object arg2, Object arg3) {
    MessageFormat mf = new MessageFormat(getMessageString(key));
    Object[] args = new Object[4];
    args[0] = arg0;
    args[1] = arg1;
    args[2] = arg2;
    args[3] = arg3;
    return mf.format(args);
  }

  /**
   * Returns the localized and formatted string for message key.
   *
   * <p>This overloaded version of the formatMessage method accepts an variable number of parameters.
   *
   * @param key String the key of the message to return
   * @param args Object[] an array with parameters to the format routine
   * @return String the localized and formatted message
   */
  public static String formatMessage(String key, Object[] args) {
    MessageFormat mf = new MessageFormat(getMessageString(key));
    return mf.format(args);
  }
}
