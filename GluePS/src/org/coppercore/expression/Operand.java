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
import java.util.HashMap;

import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.Parser;
import org.coppercore.component.ExpressionElement;
import org.coppercore.condition.Condition;
import org.coppercore.datatypes.LDDataType;
import org.coppercore.exceptions.ComponentDataException;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.TypeCastException;
import org.w3c.dom.Element;
/**
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.22 $, $Date: 2007/03/30 08:23:33 $
 */
public abstract class Operand
    implements Serializable, ExpressionElement {

  //xml tag names of the expression
  protected final static String SUM = "sum";
  protected final static String IS = "is";
  protected final static String NOT = "not";
  protected final static String IF = "if";
  protected final static String AND = "and";
  protected final static String OR = "or";
  protected final static String SUBTRACT = "subtract";
  protected final static String MULTIPLY = "multiply";
  protected final static String DIVIDE = "divide";
  protected final static String LESSTHAN = "less-than";
  protected final static String GREATERTHAN = "greater-than";
  protected final static String TIMEUOLSTARTED =
      "time-unit-of-learning-started";
  protected final static String DATETIMEACTIVITYSTARTED =
      "date-time-activity-started";
  protected final static String MEMBEROFROLE = "member-of-role";
  protected final static String CURRENTDATETIME = "current-date-time";
  protected final static String NOVALUE = "no-value";
  protected final static String PROPERTYREF = "property-ref";
  protected final static String PROPERTYVALUE = "property-value";
  protected final static String COMPLETE = "complete";
  protected final static String USERSINROLE = "users-in-role";
  protected final static String COUNT = "count";


  //corresponding integer ids
  protected final static int SUM_ID = 1;
  protected final static int IS_ID = 2;
  protected final static int NOT_ID = 3;
  protected final static int IF_ID = 4;
  protected final static int AND_ID = 5;
  protected final static int OR_ID = 6;
  protected final static int SUBTRACT_ID = 7;
  protected final static int MULTIPLY_ID = 8;
  protected final static int DIVIDE_ID = 9;
  protected final static int LESSTHAN_ID = 10;
  protected final static int GREATERTHAN_ID = 11;
  protected final static int TIMEUOLSTARTED_ID = 12;
  protected final static int DATETIMEACTIVITYSTARTED_ID = 13;
  protected final static int MEMBEROFROLE_ID = 14;
  protected final static int CURRENTDATETIME_ID = 15;
  protected final static int NOVALUE_ID = 16;
  protected final static int PROPERTYREF_ID = 17;
  protected final static int PROPERTYVALUE_ID = 18;
  protected final static int COMPLETE_ID = 19;
  protected final static int USERSINROLE_ID = 20;
  protected final static int COUNT_ID = 21;

  public abstract int checkType() throws ExpressionException;

  private static HashMap hashMap = null;

  /**
 * @param uol
 * @param run
 * @param userId
 * @return
 * @throws PropertyException
 * @throws ExpressionException
 */
public abstract LDDataType evaluate(Uol uol, Run run, User userId) throws
      PropertyException, ExpressionException;

  public void getTriggers(HashMap propertyTriggers, HashMap timerTriggers, HashMap completionTriggers, HashMap startTrigger, HashMap roleTriggers, Condition condition) {
    //default do nothing
  }

  private static int findInHashMap(String value) {
    if (hashMap == null) {
      hashMap = new HashMap(25);
      hashMap.put(SUM, new Integer(SUM_ID));
      hashMap.put(IS, new Integer(IS_ID));
      hashMap.put(NOT, new Integer(NOT_ID));
      hashMap.put(IF, new Integer(IF_ID));
      hashMap.put(AND, new Integer(AND_ID));
      hashMap.put(OR, new Integer(OR_ID));
      hashMap.put(SUBTRACT, new Integer(SUBTRACT_ID));
      hashMap.put(MULTIPLY, new Integer(MULTIPLY_ID));
      hashMap.put(DIVIDE, new Integer(DIVIDE_ID));
      hashMap.put(LESSTHAN, new Integer(LESSTHAN_ID));
      hashMap.put(GREATERTHAN, new Integer(GREATERTHAN_ID));
      hashMap.put(TIMEUOLSTARTED, new Integer(TIMEUOLSTARTED_ID));
      hashMap.put(DATETIMEACTIVITYSTARTED,
                  new Integer(DATETIMEACTIVITYSTARTED_ID));
      hashMap.put(MEMBEROFROLE, new Integer(MEMBEROFROLE_ID));
      hashMap.put(CURRENTDATETIME, new Integer(CURRENTDATETIME_ID));
      hashMap.put(NOVALUE, new Integer(NOVALUE_ID));
      hashMap.put(PROPERTYREF, new Integer(PROPERTYREF_ID));
      hashMap.put(PROPERTYVALUE, new Integer(PROPERTYVALUE_ID));
      hashMap.put(COMPLETE, new Integer(COMPLETE_ID));
      hashMap.put(USERSINROLE, new Integer(USERSINROLE_ID));
      hashMap.put(COUNT, new Integer(COUNT_ID));
    }

    Integer result = (Integer) hashMap.get(value);

    if (result != null) {
      return result.intValue();
    }
    return -1;
  }

  /**
   * Write the content of this object as XML to a PrintWriter. This class is
   * abstract and should be implemented by all of the sub classes.
   *
   * @param out PrintWriter
   */
  public abstract void toXml(PrintWriter out);

  public ExpressionElement addElement(Element node, int uolId) throws
      TypeCastException, PropertyException {
    return getElement(node, uolId);
  }

  /**
   * Returns an ExpressionElement based of the current dom node.
   * @param node Node current node of the dom tree being parsed
   * @throws ComponentDataException when no corresponding operand element can be found
   * @return ExpressionElement the corresponding object that was created
   */
  public static ExpressionElement getElement(Element node, int uolId) throws PropertyException {

    switch (findInHashMap(node.getNodeName())) {
      case SUM_ID: {
        return new SumExpression();
      }
      case IS_ID: {
        return new IsExpression();
      }
      case NOT_ID: {
        return new NotExpression();
      }
      case IF_ID: {
        return new IfExpression();
      }
      case AND_ID: {
        return new AndExpression();
      }
      case OR_ID: {
        return new OrExpression();
      }
      case SUBTRACT_ID: {
        return new SubtractExpression();
      }
      case MULTIPLY_ID: {
        return new MultiplyExpression();
      }
      case DIVIDE_ID: {
        return new DivideExpression();
      }
      case GREATERTHAN_ID: {
        return new GreaterThanExpression();
      }
      case LESSTHAN_ID: {
        return new LessThanExpression();
      }
      case TIMEUOLSTARTED_ID: {
        return new TimeUnitOfLearningStarted();
      }
      case DATETIMEACTIVITYSTARTED_ID: {
        String activityId = node.getAttribute("activity-id");
        return new DateTimeActivityStarted(activityId);
      }
      case CURRENTDATETIME_ID: {
        return new CurrentDateTime();
      }
      case MEMBEROFROLE_ID: {
        String roleId = node.getAttribute("role-id");
        return new IsMemberOfRole(roleId);
      }
      case NOVALUE_ID: {
        String propId = node.getAttribute("ref");
        int propType = Integer.parseInt(node.getAttribute("type"));
        return new NoValue(propId, propType);
      }
      case PROPERTYREF_ID: {
        String propId = node.getAttribute("ref");
        int propType = Integer.parseInt(node.getAttribute("type"));
        return new Property(uolId, propId, propType);
      }
      case PROPERTYVALUE_ID: {
        return new PropertyConstant(Parser.getTextValue(node));
      }
      case COMPLETE_ID: {
        return new Complete(node.getAttribute("ref"),
                                               node.getAttribute("type"));
      }
      case USERSINROLE_ID: {
        return new UsersInRole(node.getAttribute("role-ref"));
      }
      case COUNT_ID: {
        return new CountExpression();
      }

      default: {
        //a fall trough indicates that an element was not recognized, so throw exception
        throw new ComponentDataException(
            "Invalid expression format encountered: " +
            Parser.documentToString(node));
      }
    }
  }
}
