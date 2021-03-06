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

import java.util.HashMap;

import org.coppercore.condition.Condition;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.TypeCastException;
import org.w3c.dom.Element;

/**
 * This interface should be implemented by all elements that are part of an expression.
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2005/01/19 16:31:27 $
 */
public interface ExpressionElement {
  /**
   * Abstract method that is called during the construction of an expression.
   * The node represents the DOM representation of the expression element that
   * is added.
   * 
   * @param node
   *          the dom representation of the expression element that is added
   * @param uolId
   *          the uol id of the Uol for that is the owner of this expression
   *          element
   * @return ExpressionElement the constructed expression on the basis of the
   *         passed parameters.
   * @throws PropertyException
   *           whenever the operation fails
   * @throws TypeCastException
   *           whenever a type cast error has occured
   */
  public abstract ExpressionElement addElement(Element node, int uolId) throws
      PropertyException, TypeCastException;

  /**
   * Determine all triggers separated per category for this ExpressionElement.
   * Triggers are used to determine when this ExpressionElement should be
   * evaluated.
   * 
   * @param propertyTriggers
   *          HashMap with triggers fired by property changes
   * @param timerTriggers
   *          HashMap with triggers fired by timer events
   * @param completionTriggers
   *          HashMap with triggers fired by completion of components
   * @param startTriggers
   *          HashMap with triggers fired by the start of a components (e.g.
   *          first access)
   * @param roleTriggers
   *          HashMap with triggers fired by assignments to and changes of roles
   * @param condition
   *          Condition the condition of which this ExpressionElement is part of
   */
  public abstract void getTriggers(HashMap propertyTriggers, HashMap timerTriggers, HashMap completionTriggers, HashMap startTriggers, HashMap roleTriggers, Condition condition);
}
