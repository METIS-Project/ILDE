/*
 * Expression evaluator , an IMS Learning Design expression evaluator library
 * Copyright (c) 2004 Harrie Martens and Hubert Vogten
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
 * Open University of the Netherlands, hereby disclaims all copyright interest
 * in the program Expression evaluator written by
 * Harrie Martens and Hubert Vogten
 *
 * prof.dr. Rob Koper,
 * director of learning technologies research and development
 *
 */


package org.coppercore.expression;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.coppercore.common.Util;
import org.coppercore.component.ExpressionElement;
import org.coppercore.condition.Condition;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.TypeCastException;
import org.w3c.dom.Element;

public abstract class Expression
    extends Operand
    implements Serializable {

  //list with operands
  protected ArrayList operands = new ArrayList();

  protected int supportedOperationTypes = Util.TYPE_CAST_ERROR;

  public ExpressionElement addElement(Element node, int uolId) throws
      TypeCastException, PropertyException {
    Operand operand = (Operand) getElement(node, uolId);
    addOperand(operand);
    return operand;
  }

  public void addOperand(Operand operand) {
    operands.add(operand);
  }

  public void toXml(PrintWriter out) {
    //get the XML representation of all operands
    Iterator iter = operands.iterator();
    while (iter.hasNext()) {
      Operand op = (Operand) iter.next();
      op.toXml(out);
    }
  }

  public void getTriggers(HashMap propertyTriggers, HashMap timerTriggers, HashMap completionTriggers, HashMap startTriggers, HashMap roleTriggers, Condition condition) {
    Iterator iter = operands.iterator();
    while (iter.hasNext()) {
      Operand op = (Operand) iter.next();
      op.getTriggers(propertyTriggers, timerTriggers, completionTriggers, startTriggers, roleTriggers, condition);
    }
  }
}
