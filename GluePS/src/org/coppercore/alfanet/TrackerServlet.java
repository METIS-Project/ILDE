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
 * CopperCore , an IMS-LD level C engine
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

package org.coppercore.alfanet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implements a tracker service for testing purposes.
 *
 * <p> The service is implemented as a servlet which can be called from the
 * Alfanet tracker.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.8 $, $Date: 2005/01/12 14:34:32 $
 */
public class TrackerServlet
    extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final String CONTENT_TYPE = "text/html";

  /**
   * Initialize global variables.

   */
  public void init() {
    // do nothing
  }

  /**
   * Process the HTTP Get request.
   *
   * @param request HttpServletRequest the request object as constructed by the
   *   webserver
   * @param response HttpServletResponse contains the respond to send back to
   *   the client
   * @throws IOException when there is an error writing to response output
   *   stream
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws
      IOException {

    String serviceId = request.getParameter("serviceId");
    String auserId = request.getParameter("userId");
    String courseId = request.getParameter("courseId");
    String activityId = request.getParameter("activityId");
    String itemType = request.getParameter("itemType");
    String action = request.getParameter("cc-action");
    String accessCount = request.getParameter("cc-access-count");
    String value = request.getParameter("cc-value");

    System.out.println("Received track for : " + serviceId + ", " + auserId +
                       ", " + courseId + ", " +
                       activityId + ", " + itemType + ", " + action + ", " +
                       accessCount + ", " + value);

    response.setContentType(CONTENT_TYPE);

    PrintWriter out = response.getWriter();
    out.println("<html>");
    out.println("<head><title>TrackerServlet</title></head>");
    out.println("<body bgcolor=\"#ffffff\">OK");
    out.println("</body></html>");
  }
  /**
   * Clean up resources.
   *
   */
  public void destroy() {
    // do nothing
  }
}
