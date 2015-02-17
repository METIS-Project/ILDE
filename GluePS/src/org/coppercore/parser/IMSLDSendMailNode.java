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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.coppercore.common.MessageList;
import org.coppercore.common.XMLTag;
import org.coppercore.component.SendMailPropertyDef;
import org.coppercore.exceptions.CopperCoreException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design send-mail element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.13 $, $Date: 2005/01/21 14:01:29 $
 */
public class IMSLDSendMailNode extends IMSLDNode {

  private String select = null;
  private SendMailPropertyDef sendMail = null;
  private ArrayList emailData = new ArrayList();

  /**
   * Constructs a IMSLDSendMailNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDSendMailNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);

    select = getNamedAttribute("select");
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("email-data")) {
      emailData.add(addElement(new IMSLDEmailDataNode(childNode,this)));
    }
  }

  private String getSelect() {
    return select;
  }

  protected void persist(int uolId) throws CopperCoreException {
    sendMail.persist();

    //make sure all children are persisted as well
    super.persist(uolId);
  }


  protected void buildItemList(final String componentId, HashMap usedItems, HashMap usedIn, final HashSet referencedItems, String dataType) {
    //recursively pick up on items belonging to this component
    super.buildItemList(getIdentifier(), usedItems, usedIn, referencedItems, SendMailPropertyDef.DATATYPE);
  }

  protected String getComponentDataType() {
    return SendMailPropertyDef.DATATYPE;
  }


  protected boolean buildComponentModel(int uolId, MessageList messages) {
    boolean result = super.buildComponentModel(uolId, messages);

    sendMail = new SendMailPropertyDef(uolId,
                                       parent.getIdentifier(),
                                       parent.getIsVisible(),
                                       getXMLContent(),
                                       getItemsForComponent(getIdentifier()));

   return result;
   }


  protected void writeXMLEnvironmentTree(PrintWriter output) {
    XMLTag tag = new XMLTag("send-mail");

    tag.addAttribute("identifier",parent.getIdentifier());
    tag.addAttribute("class",parent.getClassAttribute());
    tag.addAttribute("parameters",parent.getParameters());
    tag.addAttribute("isvisible",parent.getIsVisible());
    tag.addAttribute("select",getSelect());
    tag.writeOpenTag(output);

    Iterator myIterator = this.children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();
      child.writeXMLEnvironmentTree(output);
    }

    tag.writeCloseTag(output);
  }

  protected void writeXMLContent(PrintWriter output) {
    XMLTag tag = new XMLTag("send-mail");

    tag.addAttribute("identifier",parent.getIdentifier());
    tag.addAttribute("class",parent.getClassAttribute());
    tag.addAttribute("parameters",parent.getParameters());
    tag.addAttribute("isvisible",parent.getIsVisible());
    tag.addAttribute("select",getSelect());
    tag.writeOpenTag(output);

    Iterator myIterator = this.children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode)myIterator.next();
      child.writeXMLContent(output);
    }
    tag.writeCloseTag(output);
  }
}
