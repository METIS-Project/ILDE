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

import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.XMLTag;
import org.coppercore.component.ComponentFactory;
import org.coppercore.component.ExplicitProperty;
import org.coppercore.component.ExplicitPropertyDef;
import org.coppercore.condition.Condition;
import org.coppercore.datatypes.LDBoolean;
import org.coppercore.datatypes.LDDataType;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.PropertyException;

public class NoValue extends Operand {
  private static final long serialVersionUID = 42L;
  private String propId = null;
  private int propType;

  protected NoValue(String propId, int propType) {
    this.propId = propId;
    this.propType = propType;
  }

  public NoValue(ExplicitPropertyDef propertyDef) {
    propId = propertyDef.getPropertyId();
    propType = propertyDef.getType();
  }
  public int checkType() throws ExpressionException {
    return LDDataType.LDBOOLEAN;
  }

  public LDDataType evaluate(Uol uol, Run run, User user) throws PropertyException, ExpressionException {
    ExplicitProperty prop = ComponentFactory.getPropertyFactory().getProperty(uol,run,user,propId,propType);
    return new LDBoolean((prop.getValue() == null));
  }

  public void toXml(PrintWriter out) {
    XMLTag tag = new XMLTag(Operand.NOVALUE);
    tag.addAttribute("ref",propId);
    tag.addAttribute("type",Integer.toString(propType));
    tag.writeEmptyTag(out);
  }

  public void getTriggers(HashMap propertyTriggers, HashMap timerTriggers,
                          HashMap completionTriggers, HashMap startTriggers,
                          HashMap roleTriggers, Condition condition) {
    /** @todo: check if this the way we want to retrieve the trigger id for a property */
    if (propertyTriggers.containsKey(propId)) {
      //there was already an entry for this property, so we simply add this
      //condition to the list of conditions that need to be evaluated.
      Collection conditions = (Collection) propertyTriggers.get(propId);
      conditions.add(condition);
      propertyTriggers.put(propId, conditions);

    }
    else {
      //there was no entry, so we create a new collection and add this condition
      //as the first one.
      ArrayList conditions = new ArrayList();
      conditions.add(condition);
      propertyTriggers.put(propId, conditions);
    }
  }


}
