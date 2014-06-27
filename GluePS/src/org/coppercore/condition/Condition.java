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

package org.coppercore.condition;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;

import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.Parser;
import org.coppercore.common.XMLTag;
import org.coppercore.component.ExpressionElement;
import org.coppercore.datatypes.LDBoolean;
import org.coppercore.exceptions.ComponentDataException;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.TypeCastException;
import org.coppercore.expression.IfExpression;
import org.coppercore.expression.Operand;
import org.w3c.dom.Element;

public class Condition extends ConditionRoot {
  private static final long serialVersionUID = 1L;
  private IfExpression ifExpression = null;
  private ThenActionList ifThen = null;
  private Condition elseIf = null;
  private ThenActionList elseThen = null;

  public Condition() {
    //default constructor    
  }

  public Condition(IfExpression ifExpression, ThenActionList ifThen) {
    this.ifExpression = ifExpression;
    this.ifThen = ifThen;
  }

  public Condition(IfExpression ifExpression, ThenActionList ifThen,
                   Condition elseIf) {
    this.ifExpression = ifExpression;
    this.ifThen = ifThen;
    this.elseIf = elseIf;
  }

  public Condition(IfExpression ifExpression, ThenActionList ifThen,
                   ThenActionList elseThen) {
    this.ifExpression = ifExpression;
    this.ifThen = ifThen;
    this.elseThen = elseThen;
  }

  public ExpressionElement addElement(Element node, int uolId) throws
      TypeCastException,
      PropertyException {

    String nodeName = node.getNodeName();

    if (nodeName.equals("if")) {
      ifExpression = (IfExpression) Operand.getElement(node, uolId);
      return ifExpression;
    }
    else if (nodeName.equals("actions")) {
      //if this was the first action encountered ifThen will be null
      if (ifThen == null) {
        ifThen = new ThenActionList();
        return ifThen;
      }

      //so it was the else then which was encountered
      elseThen = new ThenActionList();
      return elseThen;

    }
    else if (nodeName.equals("condition")) {
      elseIf = new Condition();
      return elseIf;
    }

    //a fall trough indicates that an element was not recognized, so throw exception
    throw new ComponentDataException("Invalid expression format encountered: " +
                                     Parser.documentToString(node));
  }

  public void evaluate(Uol uol, Run run, User user, Collection firedActions) throws
      TypeCastException, PropertyException, ExpressionException {
    //evaluate the ifExpression
    LDBoolean result = null;

    result = (LDBoolean) ifExpression.evaluate(uol, run, user);

    if (result.getValue().booleanValue()) {
      //perform the ifThen
      ifThen.performAction(uol, run, user, firedActions);
    }
    else {
      if (elseIf != null) {
        //evaluate the else condition
        elseIf.evaluate(uol, run, user, firedActions);
      }
      if (elseThen != null) {
        //perform the ifThen
        elseThen.performAction(uol, run, user, firedActions);
      }
    }
  }

  /**
   * toXml
   *
   * @param out PrintWriter
   */
  protected void toXml(PrintWriter out) {
    XMLTag root = new XMLTag("condition");

    root.writeOpenTag(out);

    ifExpression.toXml(out);

    ifThen.toXml(out);

    if (elseIf != null) {
      elseIf.toXml(out);
    }
    else if (elseThen != null) {
      elseThen.toXml(out);
    }

    root.writeCloseTag(out);
  }

  
  /* Changed 2005-07-12: 
   * getTriggers() was not properly called for the elseIf causing the condition not to be evaluated under certain conditions
   */
  public void getTriggers(HashMap propertyTriggers, HashMap timerTriggerMap, HashMap completionTriggerMap, HashMap startTriggerMap, HashMap roleTriggerMap) {
    this.getTriggers(propertyTriggers, timerTriggerMap,completionTriggerMap, startTriggerMap, roleTriggerMap, this);
  }
  
  public void getTriggers(HashMap propertyTriggers, HashMap timerTriggerMap, HashMap completionTriggerMap, HashMap startTriggerMap, HashMap roleTriggerMap, Condition condition) {
    ifExpression.getTriggers(propertyTriggers, timerTriggerMap,completionTriggerMap, startTriggerMap, roleTriggerMap, condition);
    if (elseIf != null) {
      elseIf.getTriggers(propertyTriggers, timerTriggerMap,completionTriggerMap, startTriggerMap, roleTriggerMap, condition);
    }
  }    
}
