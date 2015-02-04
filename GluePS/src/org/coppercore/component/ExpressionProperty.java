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

import java.util.ArrayList;
import java.util.Collection;

import org.coppercore.business.Event;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.exceptions.PropertyException;

/**
 * This component represents the instance of the collection of expressions
 * stored. Expressions are defined either explicetly by the IMS LD instance via
 * expression or implicitely by the business rules defined by LD. Expressions
 * are either evaluated by an explicit call to the evaluateAll method or trough
 * the event mechanism. Because the ExpressionProperty does not contain state
 * for any specific user it inherits from StaticProperty.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.18 $, $Date: 2005/01/20 13:06:28 $
 */
public class ExpressionProperty extends StaticProperty {
  private static final long serialVersionUID = 42L;

  /**
   * The default constructor for this component.
   *
   * @param uol Uol the Uol defining this component
   * @param propId String id representing this view on the activity tree.
   */
  public ExpressionProperty(Uol uol, String propId) {
    super(uol, propId);
  }

  /**
   * Returns the corresponding PropertyDefinition belonging to this component.
   *
   * @throws PropertyException when this operation fails
   * @return PropertyDef the PropertyDefinition for this component
   */
  protected PropertyDef findPropertyDef() throws PropertyException {
    return new ExpressionPropertyDef(uolId, propId);
  }

  /**
   * Evaluates all the expression contained in the instance of this component
   * using the passed parameters. This method should typically be called when a
   * user is enrolled into a run to make sure all tautologies etc. are evaluated
   * at least once.
   * 
   * @param anUol
   *          Uol the Uol for which the expression needs to be evaluated
   * @param run
   *          Run the Run for which the expression needs to be evaluated
   * @param user
   *          User the User for which the expression needs to be evaluated
   * @throws PropertyException
   *           whenever this operation fails.
   */
  public void evaluateAll(Uol anUol, Run run, User user) throws PropertyException {
    ((ExpressionPropertyDef) getPropertyDef()).evaluateAll(anUol, run, user);
  }

  /**
   * Process an event that has been raised. Because this is a StaticProperty the
   * eventhandling is not dependent on the instance of this component. So the
   * event handling is forwarded to the ExpressionPropertyDef.
   * 
   * @param anUol
   *          Uol the Uol in which context the event was raised
   * @param run
   *          Run the Run in which context the event was raised
   * @param user
   *          User the User who caused the event to be raised
   * @param event
   *          Event the Event that was raised
   * @param sender
   *          StaticProperty the component which was the trigger for the Event
   * @param firedActions
   *          Collection the Collection of other Actions that already were
   *          fired. This collection is passed to avoid trigger loops.
   * @throws PropertyException
   *           whenever this operation fails.
   * 
   */
  public void processEvent(Uol anUol, Run run, User user, Event event, StaticProperty sender, Collection firedActions)
      throws PropertyException {
    ((ExpressionPropertyDef) getPropertyDef()).processEvent(anUol, run, user, event, sender, firedActions);
  }

  /**
   * Returns a collection of users that are in the scope with this Property. In
   * effect this method will return all known users if the scope is global or
   * all users in the run if the scope is local or all users in role if the
   * scope is role or an empty collection if the scope is personal. This is a
   * static properties therefore should never should be called.
   * 
   * @throws PropertyException
   *           whenever the method fails to return the requested users
   * @return Collection of users that are in the same scope
   */
  public Collection /* User */getUsersInScope() throws PropertyException {
    //this is a static properties therefore should never should be called
    return new ArrayList();
  }

}
