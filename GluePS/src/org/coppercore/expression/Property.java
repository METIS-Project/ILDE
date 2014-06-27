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
package org.coppercore.expression;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.XMLTag;
import org.coppercore.component.ComponentFactory;
import org.coppercore.component.ExplicitProperty;
import org.coppercore.component.ExplicitPropertyDef;
import org.coppercore.condition.Condition;
import org.coppercore.datatypes.LDDataType;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.NullValueException;
import org.coppercore.exceptions.PropertyException;

/**
 * 
 * Add class comment here.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.30 $, $Date: 2007/03/30 08:23:33 $
 */
/**
 * @author Harrie
 *
 */
public class Property extends Operand {
  private static final long serialVersionUID = 42L;

  private int uolId;

  private int type;

  private String propertyId = null;

  /**
   * Constructor for a Property object which should be called during parse time.
   * 
   */
  /**
 * @param propertyDef
 */
public Property(ExplicitPropertyDef propertyDef) {
    // this.propertyDef = propertyDef;
    this.uolId = propertyDef.getUolId();
    this.type = propertyDef.getType();
    this.propertyId = propertyDef.getPropertyId();
  }

  /**
   * Constructor for a Property object which should be called run time.
   * 
   * @param uolId
   *          int
   * @param runId
   *          int
   * @param userId
   *          String
   * @param propId
   *          String
   * @throws ComponentDataException
   */
  /**
 * @param uolId
 * @param propId
 * @param type
 */
public Property(int uolId, String propId, int type) {
    this.uolId = uolId;
    this.propertyId = propId;
    this.type = type;
  }

  public int checkType() {
    return type;
  }

  public LDDataType evaluate(Uol uol, Run run, User user) throws PropertyException, ExpressionException {
    ExplicitProperty property = null;
    // property = getProperty(uolId, runId,userId,propertyId,type);
    property = ComponentFactory.getPropertyFactory().getProperty(uol, run, user, propertyId, type);
    LDDataType result = property.getValue();
    // Throw an exception if result is null
    if (result != null) {
      return result;
    }
    // the property has no value, so throw an exception
    throw new NullValueException("Property[" + propertyId + "] has no value but was used in an expression");
  }

  public void toXml(PrintWriter out) {
    XMLTag tag = new XMLTag(Operand.PROPERTYREF);
    tag.addAttribute("ref", propertyId);

    try {
      ExplicitPropertyDef propDef = ComponentFactory.getPropertyFactory().getExplicitPropertyDef(uolId, propertyId);
      tag.addAttribute("type", String.valueOf(propDef.getType()));
      tag.writeEmptyTag(out);

    }
    catch (PropertyException ex) {
      /** @todo think about what should be done with this exception */
      Logger logger = Logger.getLogger(this.getClass());
      logger.error(ex);
      throw new EJBException(ex);
    }
  }

  public void getTriggers(HashMap propertyTriggers, HashMap timerTriggers, HashMap completionTriggers,
      HashMap startTriggers, HashMap roleTriggers, Condition condition) {
    /**
     * @todo: check if this the way we want to retrieve the trigger id for a
     *        property
     */
    
    if (propertyTriggers.containsKey(propertyId)) {
      // there was already an entry for this property, so we simply add this
      // condition to the list of conditions that need to be evaluated.
      Collection conditions = (Collection) propertyTriggers.get(propertyId);
      conditions.add(condition);
      propertyTriggers.put(propertyId, conditions);
    }
    else {
      // there was no entry, so we create a new collection and add this
      // condition
      // as the first one.
      ArrayList conditions = new ArrayList();
      conditions.add(condition);
      propertyTriggers.put(propertyId, conditions);
    }
  }
}