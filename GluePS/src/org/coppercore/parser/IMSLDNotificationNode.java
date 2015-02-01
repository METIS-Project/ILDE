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

import org.coppercore.condition.Notification;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design notification element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.8 $, $Date: 2005/06/16 22:09:57 $
 */
public class IMSLDNotificationNode
    extends IMSLDNode {

  private Object activity = null;
  private ArrayList recipients = new ArrayList();
  private IMSLDNode subject = null;


  /**
   * Constructs a IMSLDNotificationNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDNotificationNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("learning-activity-ref")) {
      activity = getNamedAttribute(childNode,"ref");
    }
    else if (nodeName.equals("support-activity-ref")) {
      activity = getNamedAttribute(childNode,"ref");
    }
    else if (nodeName.equals("email-data")) {
     recipients.add(addElement(new IMSLDEmailDataNode(childNode, this)));
    }
    else if (nodeName.equals("subject")) {
     subject = addElement(new IMSLDSubjectNode(childNode, this));
    }
  }

  protected void resolveReferences() throws Exception {
    if (activity != null) {
      activity = this.lookupReferent( (String) activity);
    }

    super.resolveReferences();
  }

  /**
   * Returns all recipients of this notification. 
   * @return all recipients of this notification
   */
  ArrayList getEmailDataNodes() {
    return recipients;
  }

  String getSubject() {
    return (subject == null)?null: subject.getContent();
  }

  String getActivityId() {
   return (activity == null)?null:((IMSLDNode) activity).getIdentifier();
  }

  String getActivityTag() {
    return (activity == null)?null:((IMSLDNode) activity).node.getLocalName();
  }

  Notification getThenAction() {
    return new Notification(recipients, getSubject(),getActivityId(),getActivityTag());
  }
}
