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

package org.coppercore.component;

import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.exceptions.PropertyException;

/**
 * This class implements a visibility rule that was removed in the final version
 * of the IMS LD specification.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.9 $, $Date: 2009/01/21 07:53:34 $
 */
public abstract class Activities extends CompletionComponent {

  private static final long serialVersionUID = 42L;

/**
   * Default constructor during run time.
   *
   * @param uol int the unit of learning database id
   * @param run int the run database id
   * @param user String the user id
   * @param propId String the property id as defined in IMS LD
   * @throws PropertyException when something goes wrong with the constructor
   */  
  public Activities(Uol uol, Run run, User user, String propId) throws PropertyException {
    super(uol, run, user, propId);
  }

  //NOTE THIS FEATURE WAS REMOVED IN THE LAST VERSION OF THE INFORMATION MODEL
  //THIS CLASS IS LEFT FOR HISTORIC REASONS


  /**
   * Sets the visibility of the Learning and Support activities. This is a
   * special case because the are some specific rules about the visibility of
   * items.
   *
   * @param isVisible boolean indicating if this activity is visible
   */
  /*
     public void setVisibility(boolean isVisible) {
    //check if something changes at all
    if (isVisible() != isVisible) {
      //we may only hide an item if it hasn't been visible for the user
      if (isVisible || (isVisible == false && accessCount == 0)) {
        if (isVisible) {
          // appearently the component has become visible, so alter the visibility date
          visibleSince = new Date(System.currentTimeMillis());
        }
        this.isVisible = Boolean.toString(isVisible);
        persist();
      }
    }
     }
   */
}
