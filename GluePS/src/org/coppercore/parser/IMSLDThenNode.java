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

import java.util.ArrayList;
import java.util.Iterator;

import org.coppercore.common.MessageList;
import org.coppercore.condition.ThenActionList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design then element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.10 $, $Date: 2005/01/21 14:01:30 $
 */
public class IMSLDThenNode extends IMSLDNode{
  private ThenActionList thenActionList = new ThenActionList();
  private ArrayList actionNodes = new ArrayList();
  private ArrayList notifications = new ArrayList();

  /**
   * Constructs a IMSLDThenNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDThenNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("show")) {
      actionNodes.add(addElement(new IMSLDShowNode(childNode, this)));
    }
    else if (nodeName.equals("hide")) {
      actionNodes.add(addElement(new IMSLDHideNode(childNode, this)));
    }
    else if (nodeName.equals("change-property-value")) {
      actionNodes.add(addElement(new IMSLDChangePropertyValueNode(childNode, this)));
    }
    else if (nodeName.equals("notification")) {
      notifications.add(addElement(new IMSLDNotificationNode(childNode,this)));
    }
  }

  /**
   * Returns a list of all actions that occur when this then evaluates true.
   * @return a list of all actions that occur when this then evaluates true
   */
  protected ThenActionList getThenActionList() {
    return thenActionList;
  }

  protected boolean buildComponentModel(int uolId, MessageList messages) {
    boolean result = super.buildComponentModel(uolId, messages);

    Iterator iter = actionNodes.iterator();
    while (iter.hasNext()) {
      IMSLDNode actions = (IMSLDNode)iter.next();
      if (actions instanceof ShowHideModelEntity) {
        thenActionList.addThenActions(((ShowHideModelEntity) actions).getThenActions());
      }
      else {
        thenActionList.addThenActions(((IMSLDChangePropertyValueNode) actions).getThenActions());
      }
    }
    iter = notifications.iterator();
    while (iter.hasNext()) {
      IMSLDNotificationNode notification = (IMSLDNotificationNode) iter.next();
      thenActionList.addThenAction(notification.getThenAction());
    }

    return result;
  }


  protected boolean isValid(MessageList messages) {

    boolean result = super.isValid(messages);

    result &= thenActionList.isValid(messages);

    return result;
  }

}
