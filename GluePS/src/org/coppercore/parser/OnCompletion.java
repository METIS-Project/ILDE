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

package org.coppercore.parser;

import javax.xml.transform.TransformerException;

import org.coppercore.common.MessageList;
import org.coppercore.common.Parser;
import org.coppercore.component.CompletionComponentDef;
import org.coppercore.component.ExplicitPropertyDef;
import org.coppercore.condition.Condition;
import org.coppercore.condition.Conditions;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.ValidationException;
import org.coppercore.expression.CurrentDateTime;
import org.coppercore.expression.GreaterThanExpression;
import org.coppercore.expression.IfExpression;
import org.coppercore.expression.IsExpression;
import org.coppercore.expression.NoValue;
import org.coppercore.expression.NotExpression;
import org.coppercore.expression.Property;
import org.coppercore.expression.PropertyConstant;
import org.coppercore.expression.SumExpression;
import org.coppercore.expression.TimeUnitOfLearningStarted;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class is the base class for all IMS learning Design elements that complete other elements.
 * <ul>
 * <li>complete-activity</li>
 * <li>complete-act</li>
 * <li>complete-play</li>
 * <li>complete-unit-of-learning</li>
 * </ul>
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2005/01/21 14:01:11 $
 */
public abstract class OnCompletion extends IMSLDNode {
  String timeLimit = null;

  Object timeLimitProperty = null;

  IMSLDWhenPropertyValueIsSetNode whenPropertySet = null;

  Condition condition = null;

  abstract CompletionComponentDef getComponentDef();

  /**
   * Constructs a OnCompletion instance from the passed xml dom element.
   * 
   * @param aNode
   *          the xml dom node to parse
   * @param aParent
   *          the parsed IMS learning design element that is the parent element of this object
   */
  public OnCompletion(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("time-limit")) {
      // store the time limit information
      timeLimit = Parser.getTextValue(childNode);

      // check if there is a property-ref
      if (childNode.hasAttribute("property-ref")) {
        timeLimitProperty = childNode.getAttribute("property-ref");
      }
    }
    else if (nodeName.equals("when-property-value-is-set")) {
      whenPropertySet = (IMSLDWhenPropertyValueIsSetNode) addElement(new IMSLDWhenPropertyValueIsSetNode(childNode,
          this));
    }
  }

  protected void resolveReferences() throws Exception {

    if (timeLimitProperty != null) {
      timeLimitProperty = lookupReferent((String) timeLimitProperty);
    }
    super.resolveReferences();
  }

  protected boolean hasTimeLimit() {
    return ((timeLimit != null) || (timeLimitProperty != null));
  }

  protected boolean isValid(MessageList messages) {
    boolean result = super.isValid(messages);

    // we have to check if the time limit is valid. A time limit may refer to a property
    // that can not contain a time limit
    if (hasTimeLimit()) {
      // ok we are dealing with a time limit expression, so fetch it
      IfExpression ifExpression = buildTimeLimitCondition();

      try {
        // check if all types are convertible, if not an exception is thrown
        ifExpression.checkType();
      }
      catch (ExpressionException ex) {
        // report the error
        messages.logError(ex.getMessage());

        try {
          // try to pinpoint the location in the manifest
          messages.appendToLastLog(" in \"" + toXML() + "\"");
        }
        catch (TransformerException ex1) {
          // do nothing
        }
        result = false;
      }
    }
    return result;
  }

  IfExpression buildWhenPropertySetCondition() throws ValidationException {
    // build the condition
    IMSLDPropertyValueNode valueNode = whenPropertySet.getProperyValueNode();
    PropertyNode propertyNode = whenPropertySet.getPropertyNode();

    IfExpression ifExpression = new IfExpression();

    // changed: 2004-12-22
    // check if we are dealing with a value comparison or whether we have to check
    // if the property was set at all.
    if (valueNode == null) {
      // check if value was set
      NotExpression notExpression = new NotExpression();
      notExpression.addOperand(new NoValue(propertyNode.getPropertyDef()));
      ifExpression.addOperand(notExpression);
    }
    else {
      // check if value equals passed reference value
      IsExpression isExpression = new IsExpression();
      isExpression.addOperand(propertyNode.getExpression());
      isExpression.addOperand(valueNode.getExpression());
      ifExpression.addOperand(isExpression);
    }
    return ifExpression;
  }

  IfExpression buildTimeLimitCondition() {
    // build the condition
    IfExpression ifExpression = new IfExpression();
    GreaterThanExpression greaterThanExpression = new GreaterThanExpression();
    greaterThanExpression.addOperand(new CurrentDateTime());
    SumExpression sumExpression = new SumExpression();
    sumExpression.addOperand(new TimeUnitOfLearningStarted());
    if (timeLimitProperty != null) {
      ExplicitPropertyDef propertyDef = ((PropertyNode) timeLimitProperty).getPropertyDef();
      sumExpression.addOperand(new Property(propertyDef));
    }
    else {
      sumExpression.addOperand(new PropertyConstant(timeLimit));
    }
    greaterThanExpression.addOperand(sumExpression);

    ifExpression.addOperand(greaterThanExpression);

    return ifExpression;
  }

  protected void getSystemConditions(Conditions conditions) {
    super.getSystemConditions(conditions);

    // add the complete conditions to the conditions container
    conditions.addCondition(condition);
  }
}
