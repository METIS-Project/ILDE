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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.ejb.EJBException;

import org.coppercore.business.Uol;
import org.coppercore.common.MessageList;
import org.coppercore.common.XMLTag;
import org.coppercore.component.ClassPropertyDef;
import org.coppercore.component.CompletionComponent;
import org.coppercore.component.CompletionComponentDef;
import org.coppercore.component.UnitOfLearningPropertyDef;
import org.coppercore.exceptions.CopperCoreException;
import org.coppercore.exceptions.NotFoundException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design learning-design element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.23 $, $Date: 2005/01/21 14:01:11 $
 */
public class IMSLDLearningDesignNode extends IMSLDNode {
  private String uri;
  private UnitOfLearningPropertyDef componentDef = null;
  private ClassPropertyDef classesDef = null;
  private IMSLDMethodNode method = null;

  public IMSLDMethodNode getMethod() {
	return method;
}

/** HashMap containing per component all used items. */
  HashMap usedItems = new HashMap();
  /** HashMap containing per item  its parent component. */
  HashMap usedIn = new HashMap();
  /** HashSet of items that are shown or hidden via conditions/ */
  HashSet referencedItems = new HashSet();

  /**
   * Constructs a IMSLDLearningDesignNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDLearningDesignNode(Node aNode, IMSLDNode aParent) {
    super (aNode,aParent);
    uri = this.getNamedAttribute("uri");
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("learning-objectives")) {
      addElement(new IMSLDLearningObjectivesNode(childNode,this));
    }
    else if (nodeName.equals("prerequisites")) {
      addElement(new IMSLDPrerequisitesNode(childNode,this));
    }
    else if (nodeName.equals("components")) {

      addElement(new IMSLDComponentsNode(childNode,this));
    }
    else if (nodeName.equals("method")) {
      method = (IMSLDMethodNode) addElement(new IMSLDMethodNode(childNode,this));
    }
  }

  protected String getNameForCompleteExpression() {
    return "unit-of-learning";
  }

  /**
   * Returns the play structure for a particular role as XML tree.
   * <p>
   * This is achieved by recursively calling the writeXMLPlay method for all children
   * of the learning design node.
   * @param aRole the role to create the play tree for
   * @return the play structure for a particular role as XML tree
   */
  public String getXMLPlay(IMSLDNode aRole) {
    StringWriter outputStream = new StringWriter();
    try {
      PrintWriter output = new PrintWriter(outputStream);
      try {
        writeXMLPlay(output, aRole);
        return outputStream.toString();
      }
      catch (Exception e) {
        throw new EJBException(e);
      }
      finally {
        output.close();
      }
    }
    finally {
      try {
        outputStream.close();
      }
      catch (IOException ex) {
        throw new EJBException(ex);
      }
    }
  }



/*  protected void getReferingRoleIds(IMSLDNode aNode, Set roles) {
    method.getReferingRoleIds(aNode,roles);
  }
*/
  protected void persist(int uolId) throws CopperCoreException {

    classesDef.persist();
    componentDef.persist();

    //make sure all children are persisted as well
    super.persist(uolId);

  }


  /**
   * Persists the unit of learning in a Uol.
   *
   * <p>If the unit of learning with the given uri already exists, only the title and the webroot
   * are changed.
   *
   * @param contentUri a String specifying the location of the web content
   * @return an Uol containing the identifier of the persisted unit of learning
   */
  protected Uol persistUnitOfLearning(String contentUri) {
  Uol uol = null;
    try {
      // try to find the Uol
      uol = Uol.findByURI(uri);
      // uol already exists, change the properties
      uol.setTitle(getTitle());
      uol.setContentUri(contentUri);
    }
    catch (NotFoundException nfex) {
      // uol uri doesn't exist so create a new one
      uol = Uol.create(uri, getTitle(), contentUri);
    }
    return uol;
  }

  protected IMSLDLearningDesignNode findLearningDesign () {
    return this;
  }

  String getUri() {
    return uri;
  }

  private String isTimeLimit() {
    String result = "false";

    if (method.getCompleteUnitOfLearning() != null) {
      result = ((hasTimeLimit())?"true":"false");
    }
    return result;
  }

  CompletionComponentDef getComponentDef() {
    return componentDef;
  }

  String[] getComponentIdForItem(String itemId) {
    return (String[]) usedIn.get(itemId);
  }

  protected Collection getItemsForComponent(String componentId) {
    return (Collection) usedItems.get(componentId);
  }

  void buildItemList() {
    //build a set with referenced (show/hide) items.
     buildItemReferenceList(referencedItems);

    //recursively pick up on items belonging to this component
    super.buildItemList(getIdentifier(), usedItems, usedIn, referencedItems, "");
  }

  protected boolean buildComponentModel(int uolId, MessageList messages) {

    //create a hashmap containing all references to a particular class
    HashMap classReferences = new HashMap();

    buildClassList(classReferences);
    classesDef = new ClassPropertyDef(uolId, ClassPropertyDef.ID,classReferences);



    //create the RolePartPropertyDef now
    componentDef = new UnitOfLearningPropertyDef(uolId, getIdentifier(),
                                      (method.getCompleteUnitOfLearning() == null)?CompletionComponent.UNLIMITED:CompletionComponent.NOTCOMPLETED,
                                      getXMLContent(),
                                      getItemsForComponent(getIdentifier()));

   return super.buildComponentModel(uolId, messages);
  }

  protected void writeXMLPlay(PrintWriter output, IMSLDNode aRole) {
    XMLTag tag = new XMLTag("learning-design");
    tag.addAttribute("identifier", getIdentifier());
    if (method.getCompleteUnitOfLearning() != null) {
      tag.addAttribute("completed", CompletionComponent.NOTCOMPLETED);
    }
    else {
      tag.addAttribute("completed", CompletionComponent.UNLIMITED);
    }

    tag.addAttribute("role", aRole.getIdentifier());
    tag.writeOpenTag(output);

    Iterator myIterator = children.iterator();

    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();

      if (child instanceof IMSLDMethodNode || child instanceof IMSLDTitleNode) {
        child.writeXMLPlay(output, aRole);
      }
    }
    tag.writeCloseTag(output);
  }

  protected void writeXMLContent(PrintWriter output) {
    XMLTag tag = new XMLTag("learning-design");

    //used the activity contextId as identifier
    tag.addAttribute("identifier", getIdentifier());
    tag.addAttribute("time-limit",isTimeLimit());
    tag.addAttribute("uri", getUri());
    tag.writeOpenTag(output);

    Iterator myIterator = children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode childNode = (IMSLDNode) myIterator.next();
      childNode.writeXMLContent(output);
    }
    tag.writeCloseTag(output);
  }


}
