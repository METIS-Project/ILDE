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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.coppercore.common.Parser;
import org.coppercore.common.XMLTag;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS Learning Design item element.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.13 $, $Date: 2005/05/31 07:40:05 $
 */
public class IMSLDItemNode extends IMSLDNode {
  private IIMSCPResource resource = null;

  /**
   * Constructs a IMSLDItemNode instance from the passed xml dom element.
   * 
   * @param aNode
   *          the xml dom node to parse
   * @param aParent
   *          the parsed IMS learning design element that is the parent element
   *          of this object
   */
  public IMSLDItemNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("item")) {
      addElement(new IMSLDItemNode(childNode, this));
    }
  }

  protected void writeXMLContent(PrintWriter output) {

    IIMSCPResource resource = getResource();

    XMLTag tag;
    if (resource.isIMSLDContent()) {
      tag = new XMLTag("imsldcontent");
    } else {
      tag = new XMLTag("item");
    }
    tag.addAttribute("identifier", getIdentifier());
    tag.addAttribute("isvisible", getIsVisible(), "true");
    tag.addAttribute("url", escapeURL(resource.getURL()));
    tag.addAttribute("type", resource.getType());
    tag.addAttribute("parameters", getParameters());

    // Added in version 2.2.3 to deal with QTI property synchronization.
    // See http://www.imsglobal.org/question/qti_v2p0/imsqti_intgv2p0.html,
    // section 4.1
    tag.addAttribute("resource-identifier", resource.getResourceIdentifier());
    tag.writeOpenTag(output);

    Iterator myIterator = children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode childNode = (IMSLDNode) myIterator.next();

      childNode.writeXMLContent(output);
    }

    tag.writeCloseTag(output);
  }

  protected void buildItemList(final String componentId, HashMap usedItems, HashMap usedIn,
      final HashSet referencedItems, String dataType) {
    HashSet itemsInComponent = null;

    super.buildItemList(componentId, usedItems, usedIn, referencedItems, "");

    // check if this item was ever referenced in a show/hide construct
    if (referencedItems.contains(getIdentifier())) {

      // now build a bidirectional refence (component <--> item) for easy
      // reference
      // during build of components and show/hide conditions

      if (usedItems.containsKey(componentId)) {
        // add this item to the HashSet
        itemsInComponent = (HashSet) usedItems.get(componentId);
      } else {
        // this is the first entry for this component so create a new HashSet
        itemsInComponent = new HashSet();
      }
      // add this item to the HashSet
      String itemTuple[] = { getIdentifier(), getIsVisible() };
      itemsInComponent.add(itemTuple);
      usedItems.put(componentId, itemsInComponent);

      String tuple[] = { componentId, dataType };
      // add the component id belonging to this item
      usedIn.put(getIdentifier(), tuple);
    }
  }

  private String escapeURL(String url) {

    // modified 2005-02-25 the URL can null in case of empty items which are now
    // allowed (Reload fix)
    if (url == null) {
      return null;
    }

    // modified 2004-11-28 the URL should be in XML character escaped format
    // because it will be used in a XML blob
    return Parser.escapeXML(url);
  }

  protected IIMSCPResource getResource() {
    if (resource == null) {
      // lookup corresponding resource
      String idRef = getNamedAttribute("identifierref");

      IMSLDNode element = null;

      if (idRef == null) {
        // there is no item ref, so we assume it refering to a empty resource
        resource = new IMSCPEmptyResourceNode();
      } else {
        try {
          element = lookupReferent(idRef);
        } catch (Exception e) {
          Logger logger = Logger.getLogger(this.getClass());
          logger.error("identifier ref " + idRef + " could not be located", e);
        }

        if (element instanceof IMSCPResourceNode) {
          resource = (IMSCPResourceNode) element;
        } else {
          resource = ((IMSLDItemNode) element).getResource();
        }
      }
    }
    return resource;
  }
}
