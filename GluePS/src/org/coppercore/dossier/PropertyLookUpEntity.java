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

package org.coppercore.dossier;

/**
 * Specifies the interface for the PropertyLookUpEntity bean.
 *
 * <p>This interface extends the EJBLocalObject by adding methods for setting
 * and getting members of the PropertyEntity instances.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.6 $, $Date: 2007/04/04 09:04:06 $
 */
public interface PropertyLookUpEntity
    extends javax.ejb.EJBLocalObject {


  /**
   * Returns a data transfer object containing all data for this lookup record
   * instance.
   *
   * @return PropertyLookUpDto the data transfer object containing all data for
   *   this lookup record instance
   */
  public PropertyLookUpDto getDto();

}
