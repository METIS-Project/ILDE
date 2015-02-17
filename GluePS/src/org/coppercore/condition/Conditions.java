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
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.Parser;
import org.coppercore.common.XMLTag;
import org.coppercore.component.ExpressionElement;
import org.coppercore.exceptions.ComponentDataException;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.PropertyException;
import org.w3c.dom.Element;

public class Conditions extends ConditionRoot {
  private static final long serialVersionUID = 1L;
  private Title title = null;
  private MetaData metaData = null;
  private ArrayList conditions = new ArrayList();

  public Conditions() {
    //default constructor    
  }

  public Conditions(String title, String metaData) {
    this.title = new Title(title);
    this.metaData = new MetaData(metaData);
  }

  public void addCondition(Condition condition) {
    if (condition != null) {
      conditions.add(condition);
    }
  }

  public ExpressionElement addElement(Element node, int uolId) throws
      PropertyException {
    String nodeName = node.getNodeName();

    if (nodeName.equals("title")) {
      title = Title.create(node);
      return title;
    }
    else if (nodeName.equals("meta-data")) {
      metaData = MetaData.create(node);
      return metaData;
    }
    else if (nodeName.equals("condition")) {
      Condition cond = new Condition();
      addCondition(cond);
      return cond;
    }

    //a fall trough indicates that an element was not recognized, so throw exception
    throw new ComponentDataException("Invalid expression format encounted: " +
                                     Parser.documentToString(node));
  }

  protected void toXml(PrintWriter out) {
    XMLTag root = new XMLTag("conditions");

    root.writeOpenTag(out);

    Iterator iter = conditions.iterator();
    while (iter.hasNext()) {
      Condition condition = (Condition) iter.next();
      condition.toXml(out);
    }
    root.writeCloseTag(out);
  }

  protected void getTriggers(HashMap triggerMap, HashMap timerTriggerMap, HashMap completionTriggerMap, HashMap startTriggerMap, HashMap roleTriggerMap) {
    Iterator iter = conditions.iterator();
    while (iter.hasNext()) {
      Condition condition = (Condition)iter.next();
      condition.getTriggers(triggerMap, timerTriggerMap, completionTriggerMap,startTriggerMap, roleTriggerMap);
     }
  }

  public void evaluateAll(Uol uol, Run run, User user) throws PropertyException {
    Iterator iter = conditions.iterator();
    while (iter.hasNext()) {
      Condition condition = (Condition) iter.next();
      try {
        condition.evaluate(uol, run, user, new ArrayList());
      }
      catch (ExpressionException ex) {
        //Expression exception can be serious but are not fatal.
        //So we continue with the evaluation
        Logger logger = Logger.getLogger(this.getClass());
        logger.error(ex);
      }
    }
  }
}
