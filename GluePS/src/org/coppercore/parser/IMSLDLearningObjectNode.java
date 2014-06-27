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
import org.coppercore.component.LearningObjectPropertyDef;
import org.coppercore.exceptions.CopperCoreException;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design learning-object element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2008/04/28 08:00:34 $
 */
public class IMSLDLearningObjectNode
    extends ItemModelNode {
  private LearningObjectPropertyDef learningObject = null;
  private String classDefs = null;

  /**
   * Constructs a IMSLDLearningObjectNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDLearningObjectNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);

    //store the defined classes for this service
    classDefs = this.getNamedAttribute("class");
  }

  /**
   * Returns the value of the type attribute of the learning-object element.
   * <p>
   * The method returns null when the attribute is not specified in the learning design.
   * @return the value of the type attribute of the learning-object element
   */
  protected String getType() {
    return getNamedAttribute("type");
  }

  protected void persist(int uolId) throws CopperCoreException {
    learningObject.persist();

    //make sure all children are persisted as well
    super.persist(uolId);
  }


  protected void buildItemList(final String componentId, HashMap usedItems, HashMap usedIn, final HashSet referencedItems, String dataType) {
    //recursively pick up on items belonging to this component
    super.buildItemList(getIdentifier(), usedItems, usedIn, referencedItems, LearningObjectPropertyDef.DATATYPE);
  }


  protected boolean buildComponentModel(int uolId, MessageList messages) {
    learningObject = new LearningObjectPropertyDef(uolId,
                                                   getIdentifier(),
                                                   getIsVisible(),
                                                   getXMLContent(),
                                                   getItemsForComponent(getIdentifier()));
    return super.buildComponentModel(uolId, messages);
  }

  protected void buildClassList(HashMap classReferences) {

    if (classDefs != null) {
      String[] allClasses = classDefs.split(" ");

      for (int i = 0; i < allClasses.length; i++) {
        if (classReferences.containsKey(allClasses[i])) {
          //fetch the collection of objects having a reference to this class
          ArrayList references = (ArrayList) classReferences.get(allClasses[i]);

          //the value contains component id and datatype
          String[] value = {getIdentifier(), LearningObjectPropertyDef.DATATYPE};

          //add this node to the references
          references.add(value);

          //now add this new container to the class references
          classReferences.put(allClasses[i], references);
        }
        else {
          //create a new container for the references
          ArrayList references = new ArrayList();

          //the value contains component id and datatype
          String[] value = {getIdentifier(), LearningObjectPropertyDef.DATATYPE};

          //add this node to the references
          references.add(value);


          //now add this new container to the class references
          classReferences.put(allClasses[i], references);
        }
      }
    }
  }



  protected void writeXMLEnvironmentTree(PrintWriter output) {
    XMLTag tag = new XMLTag("learning-object");

    tag.addAttribute("identifier", getIdentifier());
    tag.addAttribute("type", getType());
    tag.addAttribute("isvisible", getIsVisible());
    //added to correct bug 1897928
    tag.addAttribute("parameters", getParameters());
    tag.writeOpenTag(output);

    Iterator myIterator = this.children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();
      child.writeXMLEnvironmentTree(output);
    }

    tag.writeCloseTag(output);
  }

  protected void writeXMLContent(PrintWriter output) {
    XMLTag tag = new XMLTag("learning-object");

    //used the activity contextId as identifier
    tag.addAttribute("identifier", getIdentifier());
    tag.addAttribute("isvisible", getIsVisible(), "true");
    //added to correct bug 1897928
    tag.addAttribute("parameters", getParameters());
    tag.addAttribute("type", getType());
    tag.addAttribute("class", getClassAttribute());

    tag.writeOpenTag(output);

    Iterator myIterator = children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode childNode = (IMSLDNode) myIterator.next();
      childNode.writeXMLContent(output);
    }

    tag.writeCloseTag(output);
  }
}
