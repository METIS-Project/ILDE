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

package org.coppercore.exceptions;

/**
 * The class IMSObjectClassNotFoundException denotes errors a reasonable application might want to catch.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.5 $, $Date: 2007/03/30 08:23:26 $
 */
public class IMSObjectClassNotFoundException extends CopperCoreException {
  private static final long serialVersionUID = 42L;
  

  /**
   * Constructs a new exception with the specified detail message.  The
   * cause is not initialized.
   *
   * @param   message   the detail message. The detail message is saved for
   *          later retrieval by the {@link #getMessage()} method.
   */
  public IMSObjectClassNotFoundException(String message) {
    super(message);
  }

  /**
  * Constructs a new exception with the specified cause and a detail
  * message of (cause==null ? null : cause.toString()) (which
  * typically contains the class and detail message of <tt>cause</tt>).
  *
  * @param  cause the cause (which is saved for later retrieval by the
  *         {@link #getCause()} method).  (A <tt>null</tt> value is
  *         permitted, and indicates that the cause is nonexistent or
  *         unknown.)
  */
 public IMSObjectClassNotFoundException(Throwable cause) {
   super(cause);
 }

}
