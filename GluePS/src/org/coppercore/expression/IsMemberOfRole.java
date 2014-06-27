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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.coppercore.business.RoleParticipation;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.XMLTag;
import org.coppercore.condition.Condition;
import org.coppercore.datatypes.LDBoolean;
import org.coppercore.datatypes.LDDataType;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.PropertyException;

public class IsMemberOfRole extends Operand {
  private static final long serialVersionUID = 42L;
  
  private String roleId = null;

  public IsMemberOfRole(String roleId) {
    this.roleId = roleId;
  }

  public int checkType() throws ExpressionException {
    //return LDConstant.getType();
    return LDDataType.LDBOOLEAN;
  }

  public LDDataType evaluate(Uol uol, Run run, User user) throws
      PropertyException, ExpressionException {
    return new LDBoolean(RoleParticipation.isMemberOfRole(run,user,roleId));
  }

  public void getTriggers(HashMap propertyTriggers, HashMap timerTriggers,
                          HashMap completionTriggers, HashMap startTriggers,
                          HashMap roleTriggers, Condition condition) {
    if (roleTriggers.containsKey(roleId)) {
      //there was already an entry for this role, so we simply add this
      //condition to the list of conditions that need to be evaluated.
      Collection conditions = (Collection) roleTriggers.get(roleId);
      conditions.add(condition);
      roleTriggers.put(roleId, conditions);

    }
    else {
      //there was no entry, so we create a new collection and add this condition
      //as the first one.
      ArrayList conditions = new ArrayList();
      conditions.add(condition);
      roleTriggers.put(roleId, conditions);
    }
  }


  public void toXml(PrintWriter out) {
    XMLTag tag = new XMLTag(Operand.MEMBEROFROLE);
    tag.addAttribute("role-id",roleId);
    tag.writeEmptyTag(out);
  }
}
