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

package org.coppercore.expression;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.XMLTag;
import org.coppercore.component.ActProperty;
import org.coppercore.component.ActivityStructureProperty;
import org.coppercore.component.ComponentFactory;
import org.coppercore.component.LearningActivityProperty;
import org.coppercore.component.PlayProperty;
import org.coppercore.component.RolePartProperty;
import org.coppercore.component.SupportActivityProperty;
import org.coppercore.component.UnitOfLearningProperty;
import org.coppercore.condition.Condition;
import org.coppercore.datatypes.LDBoolean;
import org.coppercore.datatypes.LDDataType;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.IllegalOperationException;
import org.coppercore.exceptions.PropertyException;

/**
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.14 $, $Date: 2007/03/30 08:23:32 $
 */
public class Complete extends Operand {
  private static final long serialVersionUID = 1L;
  private String ref = null;

private String className = null;

  /**
 * @param ref
 * @param className
 */
public Complete(String ref, String className) {
    this.ref = ref;
    this.className = className;
  }

  /* (non-Javadoc)
 * @see org.coppercore.expression.Operand#checkType()
 */
public int checkType() throws ExpressionException {
    //check if we have the correct number of operands
    return LDDataType.LDBOOLEAN;
  }

  /** @todo complete list with complete actions checks */
  /* (non-Javadoc)
 * @see org.coppercore.expression.Operand#evaluate(org.coppercore.business.Uol, org.coppercore.business.Run, org.coppercore.business.User)
 */
public LDDataType evaluate(Uol uol, Run run, User user) throws
      PropertyException, ExpressionException {
    //check if we have the correct number of operands
    if ("learning-activity".equals(className)) {
      LearningActivityProperty component = ComponentFactory.getPropertyFactory().
          getLearningActivity(uol, run, user, ref);
      return new LDBoolean(component.isCompleted());
    }
    else if ("support-activity".equals(className)) {
      SupportActivityProperty component = ComponentFactory.getPropertyFactory().
          getSupportActivity(uol, run, user, ref);
      return new LDBoolean(component.isCompleted());
    }
    else if ("unit-of-learning".equals(className)) {
      UnitOfLearningProperty component = ComponentFactory.getPropertyFactory().
          getUnitOfLearning(uol, run, user, ref);
      return new LDBoolean(component.isCompleted());
    }
    else if ("activity-structure".equals(className)) {
      ActivityStructureProperty component = ComponentFactory.getPropertyFactory().
          getActivityStructure(uol, run, user, ref);
      return new LDBoolean(component.isCompleted());
    }
    else if ("role-part".equals(className)) {
      RolePartProperty component = ComponentFactory.getPropertyFactory().
          getRolePart(uol, run, user, ref);
      return new LDBoolean(component.isCompleted());
    }
    else if ("act".equals(className)) {
      ActProperty component = ComponentFactory.getPropertyFactory().
          getAct(uol, run, user, ref);
      return new LDBoolean(component.isCompleted());
    }
    else if ("play".equals(className)) {
      PlayProperty component = ComponentFactory.getPropertyFactory().
          getPlay(uol, run, user, ref);
      return new LDBoolean(component.isCompleted());

    }
    else {
      throw new IllegalOperationException(
          "Unknown complete object encountered");
    }
  }

  public void toXml(PrintWriter out) {
    XMLTag tag = new XMLTag(Operand.COMPLETE);
    tag.addAttribute("type", className);
    tag.addAttribute("ref", ref);
    tag.writeEmptyTag(out);
  }


  public void getTriggers(HashMap propertyTriggers, HashMap timerTriggers, HashMap completionTriggers, HashMap startTriggers, HashMap roleTriggers, Condition condition) {

    if (completionTriggers.containsKey(ref)) {
      //there was already an entry for this activity, so we simply add this
      //condition to the list of conditions that need to be evaluated.
      Collection conditions = (Collection) completionTriggers.get(ref);
      conditions.add(condition);
      completionTriggers.put(ref, conditions);
    }
    else {
      //there was no entry, so we create a new collection and add this condition
      //as the first one.
      ArrayList conditions = new ArrayList();
      conditions.add(condition);
      completionTriggers.put(ref, conditions);
    }
  }

}
