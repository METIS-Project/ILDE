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

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * Implements an authenticator that is used by the networking code when
 * accessing the Alfanet tracker module.
 * 
 * <p>
 * The authenticator returns an object containing the userid and password to the
 * network resource requesting authentication.
 *  
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.16 $, $Date: 2005/09/09 07:25:06 $
 */
public class BasicAuthenticator extends Authenticator {
  private String userId = null;

  private String password = null;

  /**
   * Creates a <code>BasicAuthenticator</code> for accessing the Alfanet
   * tracker.
   * 
   * @param userId
   *          String the id of the user to authenticate
   * @param password
   *          String the password of the user
   */
  public BasicAuthenticator(String userId, String password) {
    this.userId = userId;
    this.password = password;
  }


  /**
   * @see java.net.Authenticator#getPasswordAuthentication()
   */
  protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(userId, password.toCharArray());
  }
}
