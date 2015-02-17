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
import org.coppercore.component.CompletionComponent;
import org.coppercore.component.EnvironmentPropertyDef;
import org.coppercore.component.EnvironmentTreePropertyDef;
import org.coppercore.exceptions.CopperCoreException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import glueps.adaptors.ld.imsld.icollage.InstanceCollageAdaptor;
import glueps.core.model.Resource;

/**
 * 
 * This class represents an IMS learning design environment element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.17 $, $Date: 2008/04/28 08:00:34 $
 */
public class IMSLDEnvironmentNode
    extends IMSLDNode {
  private EnvironmentPropertyDef environment = null;
  private EnvironmentTreePropertyDef environmentTree = null;
  private boolean isPartOfRolePart = false;

  /**
   * Constructs a IMSLDEnvironmentNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDEnvironmentNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws
      Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("environment-ref")) {
      //modified 2004-12-08 environments where not correctly nested
      addElement(new IMSLDEnvironmentRefNode(childNode, this));
    }
    else if (nodeName.equals("service")) {
      addElement(new IMSLDServiceNode(childNode, this));
    }
    else if (nodeName.equals("learning-object")) {
      addElement(new IMSLDLearningObjectNode(childNode, this));
    }
  }

  protected String getNameForCompleteExpression() {
    return "environment";
  }

  /**
   * Marks this environment as being a part of a role-part.
   *
   */
  protected void isPartOfRolePart() {
    isPartOfRolePart = true;
  }

  protected void persist(int uolId) throws CopperCoreException {
    environment.persist();

    //check if there was an environmnent tree defined for this environment
    if (environmentTree != null) {
      environmentTree.persist();
    }

    super.persist(uolId);
  }

  protected void buildItemList(final String componentId, HashMap usedItems,
                               HashMap usedIn, final HashSet referencedItems,
                               String dataType) {
    //recursively pick up on items belonging to this component
    super.buildItemList(getIdentifier(), usedItems, usedIn, referencedItems,
                        EnvironmentPropertyDef.DATATYPE);
  }

  protected boolean buildComponentModel(int uolId, MessageList messages) {
    boolean result = super.buildComponentModel(uolId, messages);

    environment = new EnvironmentPropertyDef(uolId,
                                             getIdentifier(),
                                             getXMLContent(),
                                             getItemsForComponent(getIdentifier()));

    //create an environment tree for this environment if it is part of a RolePart
    if (isPartOfRolePart) {
      //build the environment tree component
      environmentTree = new EnvironmentTreePropertyDef(uolId,
          getIdentifier(),
          getXMLEnvironmentTree());
    }

    return result;
  }

  protected void writeXMLPlay(PrintWriter output, IMSLDNode aRole,
                              ArrayList envIds) {
    //when a environment is referenced from a rolepart the environment for this
    //environment is always itself. So we may add the environment to the envIds
    //unconditionally
    envIds.add(getIdentifier());

    XMLTag tag = new XMLTag("environment-activity");
    tag.addAttribute("identifier", getIdentifier());
    tag.addAttribute("completed", CompletionComponent.UNLIMITED);
    tag.addAttribute("environment", envIds);
    tag.writeEmptyTag(output);
  }

  protected void writeXMLEnvironmentTree(PrintWriter output) {
    XMLTag tag = new XMLTag("environment");

    tag.addAttribute("identifier", getIdentifier());
    tag.addAttribute("isvisible", getIsVisible());
    tag.writeOpenTag(output);

    Iterator myIterator = children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();
      child.writeXMLEnvironmentTree(output);
    }
    tag.writeCloseTag(output);
  }

  protected void writeXMLContent(PrintWriter output) {
    XMLTag tag = new XMLTag("environment");

    //used the activity contextId as identifier
    tag.addAttribute("identifier", getIdentifier());
    tag.writeOpenTag(output);

    Iterator myIterator = children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode childNode = (IMSLDNode) myIterator.next();

/*      if (! ( (childNode instanceof IMSLDLearningObjectNode) ||
             (childNode instanceof IMSLDServiceNode))) {
        childNode.writeXMLContent(output);
      }*/
      if (! ( (childNode instanceof IMSLDLearningObjectNode) )) {
         childNode.writeXMLContent(output);
       }
    }
    tag.writeCloseTag(output);
  }

protected ArrayList<Resource> getEnvironmentAsResources(String prefix,
		ArrayList<Resource> resourcesArray) {

		// TODO BUG: in IMSLD it looks like each resource is associated to
		// several roles, maybe indicating that not all roles enacting an
		// activity may have access to all resources, and with different rights. In the future, we may have
		// to split the activity into several activities with different resources associated (one per
		// role?)
		// By now, we assume that everybody using an environment, see all the resources (the groups solve who sees what)
	
		//We get the resources in this environment and add them to the resources array
		//Iterate through the children
		for(Iterator it = children.iterator();it.hasNext();){
			IMSLDNode child = (IMSLDNode) it.next();
			if(child.node.getLocalName().equalsIgnoreCase("learning-object")){
				//get the learning object as a resource
				IMSLDLearningObjectNode object = (IMSLDLearningObjectNode) child;
				
				IMSLDItemNode item = null;
				for(Iterator it2 = object.children.iterator();it2.hasNext();){
					Object grandchild = it2.next();
					
					if(grandchild instanceof IMSLDItemNode){
						//get the item which references the resource, to extract its location, sp for files and URLs
						item = (IMSLDItemNode) grandchild;
					}
					
				}
				
				//TODO Attention! different learning objects in environments can make reference to the same resource. Here, we are creating one Resource for each learning-object, NOT for each resource they reference. We probably want to fix this in the future
				Resource lfresource = null;
				if (item!=null){
					//lfresource = new Resource(item.getResource().getResourceIdentifier(), prefix+TITLE_SEPARATOR+getTitle()+TITLE_SEPARATOR+object.getTitle(), false, item.getResource().getURL(), null, null, null);
					lfresource = new Resource(item.getResource().getResourceIdentifier(), object.getTitle(), false, item.getResource().getURL(), null, null, null);
				}
				
				if(resourcesArray==null) resourcesArray = new ArrayList<Resource>();
				if(lfresource!=null){
					if(item!=null && item.getResource()!=null){
						if(findIdInResourceArray(item.getResource().getResourceIdentifier(), resourcesArray)==null) resourcesArray.add(lfresource);
					}
				}
			} else if(child.node.getLocalName().equalsIgnoreCase("service")){
				//get the learning object as a resource
				IMSLDServiceNode service = (IMSLDServiceNode) child;
				
				for(Iterator it2 = child.children.iterator();it2.hasNext();){
					//check that it is a conference (if it is not, ignore) and get the conference component
					IMSLDNode grandchild = (IMSLDNode) it2.next();
					
					if(grandchild.node.getLocalName().equalsIgnoreCase("conference")){
						IMSLDConferenceNode conference = (IMSLDConferenceNode) grandchild;
						
						// get tool type and kind from the parameters attribute
						String gluepsToolId = null;
						Resource lfresource = null;
						if(service.getNamedAttribute("parameters")!=null && service.getNamedAttribute("parameters").indexOf(InstanceCollageAdaptor.TOOL_ID_VARIABLE+"=")!=-1){
							int endIndex = service.getNamedAttribute("parameters").indexOf(";",service.getNamedAttribute("parameters").indexOf(InstanceCollageAdaptor.TOOL_ID_VARIABLE+"="));
							if(endIndex==-1) gluepsToolId = service.getNamedAttribute("parameters").substring(service.getNamedAttribute("parameters").indexOf(InstanceCollageAdaptor.TOOL_ID_VARIABLE+"=")+(InstanceCollageAdaptor.TOOL_ID_VARIABLE.length()+1), service.getNamedAttribute("parameters").length());
							else gluepsToolId = service.getNamedAttribute("parameters").substring(service.getNamedAttribute("parameters").indexOf(InstanceCollageAdaptor.TOOL_ID_VARIABLE+"=")+(InstanceCollageAdaptor.TOOL_ID_VARIABLE.length()+1), service.getNamedAttribute("parameters").indexOf(";",service.getNamedAttribute("parameters").indexOf(InstanceCollageAdaptor.TOOL_ID_VARIABLE+"=")));
						}
						
						// TODO At this point, we still do not know the VLE! we just store the tool id if it comes, and it will be fixed when creating the deploy
						if(gluepsToolId!=null){
							//lfresource = new Resource(service.getIdentifier(), prefix+TITLE_SEPARATOR+getTitle()+TITLE_SEPARATOR+conference.getTitle(), true, null, "unknown", gluepsToolId, service.getNamedAttribute("parameters"));
							lfresource = new Resource(service.getIdentifier(), conference.getTitle(), true, null, "unknown", gluepsToolId, service.getNamedAttribute("parameters"));
						}else{//it has no tool information
							//lfresource = new Resource(service.getIdentifier(), prefix+TITLE_SEPARATOR+getTitle()+TITLE_SEPARATOR+conference.getTitle(), true, null, "unknown", "unknown", service.getNamedAttribute("parameters"));
							lfresource = new Resource(service.getIdentifier(), conference.getTitle(), true, null, "unknown", "unknown", service.getNamedAttribute("parameters"));
						}
				
						if(resourcesArray==null) resourcesArray = new ArrayList<Resource>();
						//We only add the resource if it does not exist in the array already
						//TODO BUG the resource names in IMSLD may vary with each reference, but with this algorithm we cannot do that (we just use the first reference we encounter)
						if(lfresource!=null && service.getIdentifier()!=null){
							if(findIdInResourceArray(service.getIdentifier(), resourcesArray)==null) resourcesArray.add(lfresource);
						}
					}
				}
			}else if(child.node.getLocalName().equalsIgnoreCase("environment-ref")){
				//call recursively to this method, adding the title as a prefix
				
				IMSLDEnvironmentRefNode envref = (IMSLDEnvironmentRefNode) child;
				//resourcesArray = envref.getEnvironment().getEnvironmentAsResources(prefix+TITLE_SEPARATOR+getTitle(), resourcesArray);
				resourcesArray = envref.getEnvironment().getEnvironmentAsResources(prefix, resourcesArray);
				
			}
		}
		return resourcesArray;
	
}

private Resource findIdInResourceArray(String identifier,
		ArrayList<Resource> resourcesArray) {
	if (identifier==null || resourcesArray == null || resourcesArray.size() == 0) return null;
	for(Iterator it = resourcesArray.iterator(); it.hasNext();){
		Resource res = (Resource) it.next();
		if(res.getId().equalsIgnoreCase(identifier)) return res;
	}
	return null;
}

public ArrayList<String> getEnvironmentAsResourceIdsArray(ArrayList<String> resourceIdsArray) {
	
	ArrayList<Resource> resourceArray = getEnvironmentAsResources("", null);
	ArrayList<String> idsArray = null;
	
	if(resourceArray!=null){
		if (resourceIdsArray==null) idsArray = new ArrayList<String>();
		else idsArray = resourceIdsArray;
		
		for(Iterator it = resourceArray.iterator(); it.hasNext();){
			Resource resource = (Resource) it.next();
			idsArray.add(resource.getId());
		}
		return idsArray;
	}else return null;
}

}
