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

package org.coppercore.dto;

import java.io.Serializable;

/**
 * Wraps the primary key of the UserEntity in its own class.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.4 $, $Date: 2005/01/12 14:34:31 $
 */
public class UnitOfLearningPK
    implements Serializable {
  private static final long serialVersionUID = 42L;

  /**
   * The primary key of the UserEntity.
   */
  public int id;

  /**
   * Constructs a new uninitialized instance of UnitOfLearningPK.
   */
  public UnitOfLearningPK() {
    //default constructor    
  }

  /**
   * Returns the id of the UserEntity.
   *
   * @param id int the id of the UserEntity
   */
  public UnitOfLearningPK(int id) {
    this.id = id;
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (! (obj instanceof UnitOfLearningPK)) {
      return false;
    }
    UnitOfLearningPK that = (UnitOfLearningPK) obj;
    if (that.id != this.id) {
      return false;
    }
    return true;
  }

  public int hashCode() {
    int result = 17;
    result = 37 * result + this.id;
    return result;
  }

}
