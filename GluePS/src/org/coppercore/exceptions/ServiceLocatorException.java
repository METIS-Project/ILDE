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
 * The class ServiceLocatorException denotes errors a reasonable applications might want to catch.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.6 $, $Date: 2007/03/30 08:23:25 $
 */
public class ServiceLocatorException
    extends CopperCoreException {
  private static final long serialVersionUID = 42L;

  /**
   * Constructs a new exception with the specified detail message.
   *
   * <p> The cause is not initialized.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *   {@link #getMessage()} method.
   */
  public ServiceLocatorException(String message) {
    super(message);
  }

  /**
   * Constructs a new exception with the specified detail message and cause.
   *
   * <p>Note that the detail message associated with <code>cause</code> is <i>not</i> automatically
   * incorporated in this exception's detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link
   *   #getMessage()} method).
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *   (A <tt>null</tt> value is permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public ServiceLocatorException(String message, Throwable cause) {
    super(message,cause);
  }

  /**
   * Constructs a new exception with the specified cause and a detail message of
   * (cause==null ? null : cause.toString()) (which typically contains the class and
   * detail message of <tt>cause</tt>). This constructor is useful for exceptions that are
   * little more than wrappers for other throwables (for example, {@link java.security.PrivilegedActionException}).
   *
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *   (A <tt>null</tt> value is permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public ServiceLocatorException(Throwable cause) {
      super(cause);
  }

}
