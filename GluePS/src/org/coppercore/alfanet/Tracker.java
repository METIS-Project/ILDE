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

package org.coppercore.alfanet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.coppercore.business.Uol;
import org.coppercore.business.User;

/**
 * Implements a tracker for storing CopperCore information in the Alfanet
 * tracker module.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.10 $, $Date: 2005/01/06 16:26:55 $
 */
public class Tracker {
  private static final String SERVICE_ID_PARAM = "serviceId";
  private static final String SERVICE_ID_LDENGINE = "LDENGINE";
  private static final String SERVICE_ID_COURSEMANAGER = "LDCOURSEMANAGER";

  private static final String USER_ID_PARAM = "userId";
  private static final String COURSE_ID_PARAM = "courseId";
  private static final String ACTIVITY_ID_PARAM = "activityId";
  private static final String ITEM_TYPE_PARAM = "itemType";
  private static final String ACTION_PARAM = "cc-action";

  private static Tracker tracker = null;
  private String trackerUrl = null;

  /**
   * Creates a new <code>Tracker</code> for storing the information.
   *
   * @param url String       the url of the Alfanet tracker service
   * @param userId String    the userid for accessing the tracker
   * @param password String  the password for accessing the tracker
   */
  public Tracker(String url, String userId, String password) {
    this.trackerUrl = url;

    if ( (userId != null) && (password != null)) {
      Authenticator.setDefault(new BasicAuthenticator(userId, password));
    }
  }

  /**
   *
   * @param urlString String
   * @return String
   */
  private String fetchURL(String urlString) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    try {
      URL url = new URL(urlString);
      InputStream content = (InputStream) url.getContent();
      BufferedReader in =
          new BufferedReader(new InputStreamReader(content));
      String line;
      while ( (line = in.readLine()) != null) {
        pw.println(line);
      }
    }
    catch (MalformedURLException e) {
      Logger logger = Logger.getLogger(this.getClass());
      logger.error(e);
    }
    catch (IOException e) {
      Logger logger = Logger.getLogger(this.getClass());
      logger.error(e);
    }
    return sw.toString();
  }

  /**
   * Factory method for creating a new Tracker object.
   *
   * <p> Only create a tracker if CopperCore is used for AlfaNet indicated by setting the system
   * property org.coppercore.alfanet to true (use commandline option -Dorg.coppercore.alfanet=true).
   *
   * @param url String the uri to the Alfanet tracker service
   * @param userId String the userid required to access the tracker service
   * @param password String the password of the user
   * @return Tracker with the given parameters
   */
  public static Tracker createTracker(String url, String userId,
                                      String password) {
    String alfanet = System.getProperty("org.coppercore.alfanet", "false");
    if (alfanet.equals("true")) {
      if (url != null) {
        tracker = new Tracker(url, userId, password);
      }
    }
    return tracker;
  }

  /**
   *
   * @param serviceId String
   * @param uol Uol
   * @param user User
   * @param identifier String
   * @param itemType String
   * @param action String
   * @param params String[][]
   */
  private void track(String serviceId, Uol uol, User user, String identifier,
                     String itemType, String action, String[][] params) {
    StringBuffer requestUrl = new StringBuffer(trackerUrl);

    requestUrl.append(SERVICE_ID_PARAM + "=" + serviceId);

    if (user != null) {
      requestUrl.append("&" + USER_ID_PARAM + "=" + user.getId());
    }

    if (uol != null) {
      requestUrl.append("&" + COURSE_ID_PARAM + "=" + uol.getUri());
    }

    if (identifier != null) {
      requestUrl.append("&" + ACTIVITY_ID_PARAM + "=" + identifier);
    }

    if (itemType != null) {
      requestUrl.append("&" + ITEM_TYPE_PARAM + "=" + itemType);
    }

    requestUrl.append("&" + ACTION_PARAM + "=" + action);

    if (params != null) {
      for (int i = 0; i < params.length; i++) {
        String name = params[i][0];
        String value = params[i][1];

        requestUrl.append("&" + name + "=" + value);
      }
    }

    String result = fetchURL(requestUrl.toString());
    Logger logger = Logger.getLogger(this.getClass());
    logger.info(result);
  }

  /**
   * Stores information from the LDEngine in the Alfanet tracker service.
   *
   * <p> The information is only stored if the application is configured to use
   * the tracker. @see #createTracker
   *
   * @param uol Uol - the current unit of learning
   * @param user User - the current user
   * @param identifier String - the id of the component that is tracked
   * @param itemType String - the type of the component that is tracked
   * @param action String - describes the type of action
   * @param params String[][] - an array of name value pair of addition
   *   information to be stored in the tracker or null if no additional
   *   information is to be stored
   */
  public static void trackEngine(Uol uol, User user, String identifier,
                                 String itemType, String action,
                                 String[][] params) {
    if (tracker != null) {
      tracker.track(SERVICE_ID_LDENGINE, uol, user, identifier, itemType,
                    action,
                    params);
    }
  }

  /**
   * Stores information from the CourseManager in the Alfanet tracker service.
   *
   * <p> The information is only stored if the application is configured to use
   * the tracker. @see #createTracker
   *
   * @param uol Uol - the current unit of learning
   * @param user User - the current user
   * @param identifier String - the id of the component that is tracked
   * @param itemType String - the type of the component that is tracked
   * @param action String - describes the type of action
   * @param params String[][] - an array of name value pair of addition
   *   information to be stored in the tracker or null if no additional
   *   information is to be stored
   */
  public static void trackCourseManager(Uol uol, User user, String identifier,
                                        String itemType, String action,
                                        String[][] params) {
    if (tracker != null) {
      tracker.track(SERVICE_ID_COURSEMANAGER, uol, user, identifier, itemType,
                    action, params);
    }
  }

}
