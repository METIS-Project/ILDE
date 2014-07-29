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
 * The class represent the IMS LD Act. An Act is an instance of a
 * CompletionComponent.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.17 $, $Date: 2005/01/20 16:12:04 $
 */
public class ActProperty extends CompletionComponent {
  private static final long serialVersionUID = 1L;

  /**
   * Constructor for creating this component.
   *
   * @param uol Uol the Uol in which this component was declared
   * @param run Run the Run to which this component belongs
   * @param user User the User to which this component belongs
   * @param propId String the identifier of this component as defined in IMS LD
   * @throws PropertyException when the constructor fails
   */
  public ActProperty(Uol uol, Run run, User user, String propId) throws PropertyException {
    super(uol, run, user, propId);
  }

  /**
   * Returns the corresponding PropertyDefinition belonging to this component.
   *
   * @throws PropertyException when this operation fails
   * @return PropertyDef the PropertyDefinition for this component
   */
  protected PropertyDef findPropertyDef() throws PropertyException {
    return new ActPropertyDef(uolId, propId);
  }

  /**
   * Returns the previous Act component in the sequence acts as defined in the
   * IMS LD instance. If this is the first Act null will be returned.
   *
   * @throws PropertyException when this operation fails
   * @return ActProperty the previous Act in the sequence of Acts or null if
   *   this was the first Act
   */
  protected ActProperty getPreviousAct() throws PropertyException {
    //fetch the id of the previous act
    String previousActId = ((ActPropertyDef) getPropertyDef()).getPreviousActId();
    if (previousActId != null) {
      //return the previous act
      return ComponentFactory.getPropertyFactory().getAct(getUol(), run, user, previousActId);
    }
    //we are dealing with the first act in the chain of acts
    return null;
  }
}