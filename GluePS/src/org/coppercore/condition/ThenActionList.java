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

package org.coppercore.condition;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.MessageList;
import org.coppercore.common.Parser;
import org.coppercore.common.XMLTag;
import org.coppercore.component.ExpressionElement;
import org.coppercore.exceptions.ComponentDataException;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.TypeCastException;
import org.w3c.dom.Element;

public class ThenActionList
    extends ConditionRoot {
  private static final long serialVersionUID = 42L;
  private ArrayList thenActions = new ArrayList();
  private static HashMap hashMap = null;

  private final static String SHOWHIDECLASS = "show-hide-class";
  private final static String SHOWHIDEACTIVITY = "show-hide-activity";
  private final static String SHOWHIDEACTIVITYSTRUCTURE = "show-hide-activity-structure";
  private final static String SHOWHIDEUNITOFLEARNING = "show-hide-unit-of-learning";
  private final static String SHOWHIDEENVIRONMENT = "show-hide-environment";
  private final static String SHOWHIDEITEM = "show-hide-item";
  private final static String SHOWHIDEPLAY = "show-hide-play";
  private final static String CHANGEPROPERTYVALUE = "change-property-value";
  private final static String COMPLETEFORROLE = "complete-for-role";
  private final static String COMPLETEFORUSER = "complete-for-user";
  private final static String NOTIFICATION = "cc:notification";

  private final static int SHOWHIDECLASS_ID = 1;
  private final static int SHOWHIDEACTIVITY_ID = 2;
  private final static int SHOWHIDEACTIVITYSTRUCTURE_ID = 3;
  private final static int SHOWHIDEUNITOFLEARNING_ID = 4;
  private final static int SHOWHIDEENVIRONMENT_ID = 5;
  private final static int SHOWHIDEITEM_ID = 6;
  private final static int SHOWHIDEPLAY_ID = 7;
  private final static int CHANGEPROPERTYVALUE_ID = 8;
  private final static int COMPLETEFORROLE_ID = 9;
  private final static int COMPLETEFORUSER_ID = 10;
  private final static int NOTIFICATION_ID = 11;


  public ThenActionList() {
    //default constructor
  }

  public void addThenActions(Collection thenActions) {
    this.thenActions.addAll(thenActions);
  }

  public void addThenAction(ThenAction action) {
    thenActions.add(action);
  }
  
  private HashMap getHashMap() {
    if (hashMap == null) {
      hashMap = new HashMap(25);
      hashMap.put(SHOWHIDECLASS, new Integer(SHOWHIDECLASS_ID));
      hashMap.put(SHOWHIDEACTIVITY, new Integer(SHOWHIDEACTIVITY_ID));
      hashMap.put(SHOWHIDEACTIVITYSTRUCTURE, new Integer(SHOWHIDEACTIVITYSTRUCTURE_ID));
      hashMap.put(SHOWHIDEUNITOFLEARNING, new Integer(SHOWHIDEUNITOFLEARNING_ID));
      hashMap.put(SHOWHIDEENVIRONMENT, new Integer(SHOWHIDEENVIRONMENT_ID));
      hashMap.put(SHOWHIDEITEM, new Integer(SHOWHIDEITEM_ID));
      hashMap.put(SHOWHIDEPLAY, new Integer(SHOWHIDEPLAY_ID));
      hashMap.put(CHANGEPROPERTYVALUE, new Integer(CHANGEPROPERTYVALUE_ID));
      hashMap.put(COMPLETEFORROLE, new Integer(COMPLETEFORROLE_ID));
      hashMap.put(COMPLETEFORUSER, new Integer(COMPLETEFORUSER_ID));
      hashMap.put(NOTIFICATION, new Integer(NOTIFICATION_ID));
    }
    return hashMap;
  }

  private int getExpressionType(String nodeName) {
    Integer id = (Integer) getHashMap().get(nodeName);
    int result = -1;

    if (id != null) {
      result = id.intValue();
    }
    return result;
  }


  public ExpressionElement addElement(Element node, int uolId) throws
      TypeCastException, PropertyException {
    switch (getExpressionType(node.getNodeName())) {
      case SHOWHIDECLASS_ID: {
        ShowHideClass action = ShowHideClass.create(node);
        addThenAction(action);
        return action;
      }
      case SHOWHIDEACTIVITY_ID: {
        ShowHideActivity action = ShowHideActivity.create(node);
        addThenAction(action);
        return action;
      }
      case SHOWHIDEACTIVITYSTRUCTURE_ID: {
        ShowHideActivityStructure action = ShowHideActivityStructure.create(
            node);
        addThenAction(action);
        return action;
      }
      case SHOWHIDEUNITOFLEARNING_ID: {
        ShowHideUnitOfLearning action = ShowHideUnitOfLearning.create(node);
        addThenAction(action);
        return action;
      }
      case SHOWHIDEENVIRONMENT_ID: {
        ShowHideEnvironment action = ShowHideEnvironment.create(node);
        addThenAction(action);
        return action;
      }
      case SHOWHIDEITEM_ID: {
        ShowHideItem action = ShowHideItem.create(node);
        addThenAction(action);
        return action;
      }
      case SHOWHIDEPLAY_ID: {
        ShowHidePlay action = ShowHidePlay.create(node);
        addThenAction(action);
        return action;
      }
      case CHANGEPROPERTYVALUE_ID: {
        ChangePropertyValue action = ChangePropertyValue.create(node);
        addThenAction(action);
        return action;
      }
      case COMPLETEFORROLE_ID: {
        CompleteForRole action = CompleteForRole.create(node);
        addThenAction(action);
        return action;
      }
      case COMPLETEFORUSER_ID: {
        CompleteForUser action = CompleteForUser.create(node);
        addThenAction(action);
        return action;
      }
      case NOTIFICATION_ID: {
        Notification action = Notification.create(node);
        addThenAction(action);
        return action;
      }

      default: {

        //a falltrough indicates that an element was not recognized, so throw exception
        throw new ComponentDataException(
            "Invalid expression format encountered: " +
            Parser.documentToString(node));
      }
    }
  }

  protected void performAction(Uol uol, Run run, User user, Collection firedActions) throws
      TypeCastException, PropertyException, ExpressionException {
    Iterator iter = thenActions.iterator();
    while (iter.hasNext()) {
      ThenAction action = (ThenAction) iter.next();
      ThenActionsFired thenActionsFired = new ThenActionsFired(run,user,action);

      //modified 2004-11-20: fixed bug limiting actions to be peformed for individual users. Instead of checking if
      //an condition has fired during an event chain with was to restrictive, now is checked if an action has been
      //performed within the current event chain taking the scope of the action into account. The scope can be either
      //local meaning the action should only be performed once during the event chain or the scope can be localpersonal
      //meaning the action should be performed once for each separate user.
      if (!firedActions.contains(thenActionsFired)) {
        //add this action to the collection of performed actions
        firedActions.add(thenActionsFired);

        //perform the action
        action.performAction(uol, run, user, firedActions);
      }
    }
  }

  protected void toXml(PrintWriter out) {

    XMLTag tag = new XMLTag("actions");

    tag.writeOpenTag(out);

    Iterator iter = thenActions.iterator();
    while (iter.hasNext()) {
      ThenAction thenAction = (ThenAction) iter.next();
      thenAction.toXml(out);
    }

    tag.writeCloseTag(out);
  }

  /**
   * Returns true if all action elements are valid.
   *
   * @param messages MessageList
   * @return boolean
   */
  public boolean isValid(MessageList messages) {
    boolean result = true;

    Iterator iter = thenActions.iterator();
    while (iter.hasNext()) {
      ThenAction thenAction = (ThenAction) iter.next();
      result &= thenAction.isValid(messages);
    }

    return result;
  }

}
