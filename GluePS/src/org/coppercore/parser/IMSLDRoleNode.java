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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.coppercore.common.MessageList;
import org.coppercore.common.XMLTag;
import org.coppercore.component.ActivityTreePropertyDef;
import org.coppercore.exceptions.CopperCoreException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import glueps.core.model.Role;

/**
 * 
 * This class represents an IMS learning design role element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.18 $, $Date: 2005/02/25 14:15:08 $
 */
public class IMSLDRoleNode extends IMSLDNode {
  private Vector roles = new Vector();

  private ActivityTreePropertyDef activityTree = null;

  private String createNewAttr = null;

  private String hrefAttr = null;

  private String matchPersonsAttr = null;

  private String minPersonsAttr = null;

  private String maxPersonsAttr = null;

  /**
   * Constructs a IMSLDRoleNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDRoleNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);

    createNewAttr = getNamedAttribute("create-new");
    hrefAttr = getNamedAttribute("href");
    matchPersonsAttr = getNamedAttribute("match-persons");
    maxPersonsAttr = getNamedAttribute("max-persons");
    minPersonsAttr = getNamedAttribute("min-persons");
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("information")) {
      // add the title as child node
      addElement(new IMSLDInformationNode(childNode, this));
    }

    else if (nodeName.equals("learner") || nodeName.equals("staff")) {
      // we will add the role as child node
      addElement(new IMSLDRoleNode(childNode, this));
    }
  }

  protected void resolveReferences() throws Exception {

    lookupAll(roles);
    super.resolveReferences();

  }

  protected boolean hasParent(IMSLDNode aRole) {
    return (this == aRole ? true : parent.hasParent(aRole));
  }

  /**
   * Gets a collection of the id's of all parent roles for the specified role.<p>
   * this includes the id of roles node. 
   * @param parents the collection collecting the parents of the role.
   */
  protected void getParentIds(Collection parents) {
    // add the id of the parent role of this role to the passed parents collection.
    parents.add(parent.getIdentifier());
    if (parent instanceof IMSLDRoleNode) {
      ((IMSLDRoleNode) parent).getParentIds(parents);
    }
  }

  protected boolean isRelevantFor(IMSLDNode aRole) {
    return hasParent(aRole);
  }

  private String getCreateNew() {
    return createNewAttr;
  }

  private String getHref() {
    return hrefAttr;
  }

  private String getMatchPersons() {
    return matchPersonsAttr;
  }

  private String getMinPersons() {
    return minPersonsAttr;
  }

  private String getMaxPersons() {
    return maxPersonsAttr;
  }

  protected void persist(int uolId) throws CopperCoreException {
    activityTree.persist();
    super.persist(uolId);
  }

  protected boolean buildComponentModel(int uolId, MessageList messages) {
    // build up all conditions
    boolean result = super.buildComponentModel(uolId, messages);
    
    // create the ActivityTree
    activityTree = new ActivityTreePropertyDef(uolId, getIdentifier(), getXMLPlay(this));

    /** @todo: check if we have to create role content as well. For now we don't */

    return result;
  }

  protected void writeXMLRolesTree(PrintWriter output) {
    XMLTag tag = new XMLTag(node.getLocalName());

    // used the roles contextId as identifier
    tag.addAttribute("identifier", getIdentifier());
    tag.addAttribute("org-identifier", getIdentifier());
    tag.addAttribute("create-new", getCreateNew());
    tag.addAttribute("href", getHref());
    tag.addAttribute("match-persons", getMatchPersons());
    tag.addAttribute("min-persons", getMinPersons());
    tag.addAttribute("max-persons", getMaxPersons());

    tag.writeOpenTag(output);

    Iterator myIterator = children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode childNode = (IMSLDNode) myIterator.next();

      childNode.writeXMLRolesTree(output);
    }
    tag.writeCloseTag(output);
  }

  protected void writeXMLContent(PrintWriter output) {
    XMLTag tag = new XMLTag(node.getLocalName());

    // used the roles contextId as identifier
    tag.addAttribute("identifier", getIdentifier());
    tag.addAttribute("create-new", getCreateNew());
    tag.addAttribute("href", getHref());
    tag.addAttribute("match-persons", getMatchPersons());
    tag.addAttribute("min-persons", getMinPersons());
    tag.addAttribute("max-persons", getMaxPersons());

    tag.writeOpenTag(output);

    Iterator myIterator = children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode childNode = (IMSLDNode) myIterator.next();

      if (!(childNode instanceof IMSLDRoleNode)) {
        childNode.writeXMLContent(output);
      }
    }
    tag.writeCloseTag(output);
  }

public String getInformationItem(String unpackedPath) {
	
	for(Iterator it = this.children.iterator();it.hasNext();){
		IMSLDNode child = (IMSLDNode) it.next();
		if(child.node.getLocalName().equalsIgnoreCase("information")){
		//We go through the items, and return the first one as the information
			for(Iterator iter = child.children.iterator(); iter.hasNext();){
				//IMSLDItemNode subchild = (IMSLDItemNode) iter.next();
				
				IMSLDNode subchild = (IMSLDNode) iter.next();
				
				if(subchild instanceof IMSLDItemNode){
					IMSLDItemNode subch = (IMSLDItemNode) subchild;
					
					String path = unpackedPath + subch.getResource().getURL();
					
					File file = new File(path);
					try {
						String content = FileUtils.readFileToString(file, "UTF-8");
						return content;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}
				}else{
					//for now, we ignore all other items (titles, xml files...)
				}
				
	
			}
		}
	}
	
	return null;
}

public ArrayList<Role> addLFRoleToArray(String unpackedPath, ArrayList<Role> rolesArray) {
	
	String roleDescription = this.getInformationItem(unpackedPath);
	Role lfrole = null;
	
	if(this.node.getLocalName().equalsIgnoreCase("learner")){	
		lfrole = new Role(this.getIdentifier(), this.getTitle(), roleDescription, false);
	}else if(this.node.getLocalName().equalsIgnoreCase("staff")){
		lfrole = new Role(this.getIdentifier(), this.getTitle(), roleDescription, true);
	}
	
	if(rolesArray==null) rolesArray = new ArrayList<Role>();
	rolesArray.add(lfrole);
	
	//Go through the children looking for subroles
	for(Iterator it = children.iterator();it.hasNext();){
		IMSLDNode child = (IMSLDNode) it.next();
		if(child.node.getLocalName().equalsIgnoreCase("learner") || child.node.getLocalName().equalsIgnoreCase("staff")){
			//this is a subrole
			IMSLDRoleNode subrole = (IMSLDRoleNode) child;
			rolesArray = subrole.addLFRoleToArray(unpackedPath, rolesArray);
		}
		
	}
	
	
	return rolesArray;
}

}
