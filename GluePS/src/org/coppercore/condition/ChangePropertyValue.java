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

import javax.ejb.EJBException;

import org.apache.log4j.Logger;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.MessageList;
import org.coppercore.common.XMLTag;
import org.coppercore.component.ComponentFactory;
import org.coppercore.component.ExplicitProperty;
import org.coppercore.component.ExplicitPropertyDef;
import org.coppercore.component.ExpressionElement;
import org.coppercore.component.PropertyDef;
import org.coppercore.datatypes.LDDataType;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.TypeCastException;
import org.coppercore.expression.Operand;
import org.coppercore.expression.PropertyConstant;
import org.w3c.dom.Element;

public class ChangePropertyValue extends ThenAction {
  private static final long serialVersionUID = 1L;
  private ExplicitPropertyDef propertyDef = null;
  private String propertyId = null;
  private int scope;
  private int type;
  private Operand operand = null;
  private String ldContext = null;

  public ChangePropertyValue(ExplicitPropertyDef propertyDef, Operand operand) {
    this.propertyDef = propertyDef;
    this.propertyId = propertyDef.getPropertyId();
    this.scope = propertyDef.getScope();
    this.type = propertyDef.getType();
    this.operand = operand;
  }

  public ChangePropertyValue(ExplicitPropertyDef propertyDef, Operand operand,
                             String ldContext) {
    this(propertyDef,operand);
    this.ldContext = ldContext;
  }

  public ChangePropertyValue(String propId, int type, int scope) {
    this.propertyId = propId;
    this.type = type;
    this.scope = scope;
  }

  public static ChangePropertyValue create(Element node) {
    String propId = node.getAttribute("ref");
    int type = Integer.parseInt(node.getAttribute("type"));
    int scope = Integer.parseInt(node.getAttribute("scope"));
    return new ChangePropertyValue(propId,type,scope);
  }

  public ExpressionElement addElement(Element node, int uolId) throws
      TypeCastException, PropertyException {
      operand = (Operand) Operand.getElement(node, uolId);
      return operand;
  }

  protected void performAction(Uol uol, Run run, User user, Collection firedActions) throws
      TypeCastException, PropertyException, ExpressionException {

    ExplicitProperty property = ComponentFactory.getPropertyFactory().getProperty(uol,run, user, propertyId, type);

    // Modified 30-10-2004. Added a type cast to the outcome of the operand because
    // value must be one of the defined types to allow restriction checking.
    LDDataType value = property.typeCast(operand.evaluate(uol, run, user));

    property.setValue(value,firedActions);
  }


  protected void toXml(PrintWriter out) {
    XMLTag tag = new XMLTag("change-property-value");
    tag.addAttribute("ref", propertyId);
    tag.addAttribute("type",String.valueOf(type));
    tag.addAttribute("scope",String.valueOf(scope));
    tag.writeOpenTag(out);
    operand.toXml(out);
    tag.writeCloseTag(out);
  }


  protected boolean hasLocalScope() {
    // scope is considered local, unless the scope was local personal
    return ((scope & PropertyDef.LOCALPERSONAL) != PropertyDef.LOCALPERSONAL);
  }

  protected boolean isValid(MessageList messages) {
    //check if the type of the property corresponds with the type of the expression
    boolean result = true;

    try {
      if (propertyDef != null) {
        if (operand instanceof PropertyConstant) {
          //try the conversion to see if an exception is thrown
          switch (propertyDef.getType()) {
            case LDDataType.LDBOOLEAN: {
              ( (PropertyConstant) operand).getValue().toLDBoolean();
              break;
            }
            case LDDataType.LDDATETIME: {
              ( (PropertyConstant) operand).getValue().toLDDateTime();
              break;

            }
            case LDDataType.LDDURATION: {
              ( (PropertyConstant) operand).getValue().toLDDuration();
              break;
            }
            case LDDataType.LDFILE: {
              //( (PropertyConstant) operand).getValue().toLDFile();
              break;
            }
            case LDDataType.LDINTEGER: {
              ( (PropertyConstant) operand).getValue().toLDInteger();
              break;
            }
            case LDDataType.LDREAL: {
              ( (PropertyConstant) operand).getValue().toLDReal();
              break;
            }
            case LDDataType.LDSTRING: {
              ( (PropertyConstant) operand).getValue().toLDString();
              break;
            }
            case LDDataType.LDTEXT: {
              ( (PropertyConstant) operand).getValue().toLDText();
              break;
            }
            case LDDataType.LDURI: {
              ( (PropertyConstant) operand).getValue().toLDUri();
              break;
            }
          }
        }
        else {
          if (operand.checkType() != propertyDef.getType()) {
            messages.logError("Value assigment to property with id " +
                              propertyId + " failed due to a type cast violation in "
                              + ldContext);
          }
        }
      }
      else {
        //this should never occur, as validation is done after having user the
        //constructor passing the property definition
        Logger logger = Logger.getLogger(this.getClass());
        logger.error("isValid called without a PropertyDef being declared");
        throw new EJBException("isValid called without a PropertyDef being declared");
      }
    }
    catch (ExpressionException ex) {
      //type casting failed, so return false
      messages.logError("Value assigment to property with id " +
                        propertyId + " failed. Because: " +
                        ex.getMessage() + " in " + ldContext);
      result = false;
    }
    return result;
  }
}
