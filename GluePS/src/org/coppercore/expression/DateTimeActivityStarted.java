/*
 * CopperCore, an IMS-LD level C engine Copyright (C) 2003 Harrie Martens and
 * Hubert Vogten
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program (/license.txt); if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * 
 * Contact information: Open University of the Netherlands Valkenburgerweg 177
 * Heerlen PO Box 2960 6401 DL Heerlen e-mail: hubert.vogten@ou.nl or
 * harrie.martens@ou.nl
 * 
 * 
 * Open Universiteit Nederland, hereby disclaims all copyright interest in the
 * program CopperCore written by Harrie Martens and Hubert Vogten
 * 
 * prof.dr. Rob Koper, director of learning technologies research and
 * development
 *  
 */

package org.coppercore.expression;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.XMLTag;
import org.coppercore.component.ComponentFactory;
import org.coppercore.component.LearningActivityProperty;
import org.coppercore.condition.Condition;
import org.coppercore.datatypes.LDDataType;
import org.coppercore.datatypes.LDDateTime;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.NullValueException;
import org.coppercore.exceptions.PropertyException;

public class DateTimeActivityStarted extends Operand {
  private static final long serialVersionUID = 1L;
  private String activityId = null;

  public DateTimeActivityStarted(String activityId) {
    this.activityId = activityId;
  }

  public int checkType() {
    return LDDataType.LDDATETIME;
  }

  public LDDataType evaluate(Uol uol, Run run, User user) throws PropertyException, ExpressionException {
    //Support activities are overloaded from LearningActivities therefore this
    // works for both
    LearningActivityProperty activity = ComponentFactory.getPropertyFactory().getLearningActivity(uol, run, user,
        activityId);

    Calendar activityStarted = activity.getFirstAccess();

    //Throw an exception if result is null
    if (activityStarted != null) {
      return new LDDateTime(activityStarted);
    }

    //the activity started has no value, so throw an exception
    throw new NullValueException("Date/time activity started[" + activityId
        + "] has no value but was used in an expression");

  }

  public void toXml(PrintWriter out) {
    XMLTag tag = new XMLTag(Operand.DATETIMEACTIVITYSTARTED);
    tag.addAttribute("activity-id", activityId);
    tag.writeEmptyTag(out);
  }

  public void getTriggers(HashMap propertyTriggers, HashMap timerTriggers, HashMap completionTriggers,
      HashMap startTriggers, HashMap roleTriggers, Condition condition) {

    if (startTriggers.containsKey(activityId)) {
      //there was already an entry for this activity, so we simply add this
      //condition to the list of conditions that need to be evaluated.
      Collection conditions = (Collection) startTriggers.get(activityId);
      conditions.add(condition);
      startTriggers.put(activityId, conditions);
    } else {
      //there was no entry, so we create a new collection and add this
      // condition
      //as the first one.
      ArrayList conditions = new ArrayList();
      conditions.add(condition);
      startTriggers.put(activityId, conditions);
    }
  }
}