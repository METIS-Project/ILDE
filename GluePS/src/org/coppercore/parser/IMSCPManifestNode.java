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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


import org.apache.commons.io.FileUtils;
import org.coppercore.business.Event;
import org.coppercore.business.Uol;
import org.coppercore.common.MessageList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import glueps.core.model.Activity;
import glueps.core.model.Design;
import glueps.core.model.Resource;
import glueps.core.model.Role;

/**
 * This class represents a parsed IMS Content Package manifest element.
*
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.20 $, $Date: 2005/01/20 16:12:45 $
 */
public class IMSCPManifestNode extends IMSLDNode {

  private HashMap components;
  private boolean errorOccured = false;
  private int uolId = -1;
  private IMSLDLearningDesignNode ld = null;


  /**
   * Constructs a new IMSCPManifestNode by parsing the specified xml dom node.
   * @param aNode Node the xml dom node to parse
   */
  public IMSCPManifestNode(Node aNode) {
  super(aNode, null);
  }

  /**
   * Processes the IMS Content Package.
   *
   * <p>This method result in a published manifest.
   *
   * @param logger MessageList a list containing all messages generated during
   *   processing
   * @param contentUri String the web location of the content resources
   * @throws Exception when there is an exception during the processing of the
   *   manifest
   * @return boolean true if the manifest is published successfully, otherwise
   *   returns false
   */
  public boolean process(MessageList logger, String contentUri) throws Exception {
  /** @todo refactor parse() to throw an exception */
    boolean result = false;
    parse();
    if (!errorOccured) {

      ld = findLearningDesign();
      Uol uol =ld.persistUnitOfLearning(contentUri);
      uolId = uol.getId();

      //remove existing eventhandlers for this unit of learning
      Event.remove(uol);


      //build the item hashmap for future referencing
      ld.buildItemList();

      if (buildComponentModel(uolId, logger)) {

        logger.logInfo("Successfully build component model");

        //check if everything is valid by doing final semantic checks;
        if (isValid(logger)) {

          logger.logInfo("Semantic validation was succcessful");

          try {
            //persist the datastructures
            persist(uolId);
            result = true;
          }
          catch (Exception ex) {
            result = false;
            logger.logException(ex);
          }
        }
      }
   }

    // return positive outcome
    return result;
  }


  /**
   * Validates the manifest.
   * @param logger MessageList a list containing all messages generated during
   *   validation
   * @throws Exception when there is an exception during the validation of the
   *   manifest
   * @return boolean true if the manifest is valid, otherwise
   *   returns false
   */
  public boolean validate(MessageList logger) throws Exception {
  boolean result = false;
   parse();
   if (!errorOccured) {

     ld = findLearningDesign();
     uolId = -1;

     //build the item hashmap for future referencing
     ld.buildItemList();

     if (buildComponentModel(uolId, logger)) {

       logger.logInfo("Successfully build component model");

       //check if everything is valid by doing final semantic checks;
       if (isValid(logger)) {
         result = true;
         logger.logInfo("Semantic validation was succcessful");
       }
     }
  }

   // return positive outcome
   return result;
 }


  public void parse() throws Exception {
    super.parse();

    //check if first stage succeeded
    if (!errorOccured) {
      //now resolve all references
      resolveReferences();
    }
  }


  protected void addComponent(String identifier,IMSLDNode component) {

    //must be placed here, because method will be called from super constructor,
    //so hashmap isn't initialized.
    if (components == null) {
      components = new HashMap();
    }
    components.put(identifier,component);
  }

  protected IMSLDNode lookupReferent(String identifier) throws Exception {
    IMSLDNode result = (IMSLDNode) components.get(identifier);

    if (result == null) {
      throw new Exception("Lookup failed for " + identifier);
    }
    return result;
  }

  protected void error() {
    errorOccured = true;
  }

  /**
   * Returns the database id of the unit of learning present in this content package.
   * @return int the database id of the unit of learning
   */
  protected int getUolId() {
  return uolId;
  }

  /**
   * Returns the parsed IMS Learning Design instance.
   * @return IMSLDLearningDesignNode the parsed IMS Learning Design instance
   */
  public IMSLDLearningDesignNode getLDLearningDesignNode() {
    return ld;
  }

/*  protected void getReferingRoleIds(IMSLDNode aNode, Set roles) {
    //do nothing
  }
*/
  protected void parseCPElement(Element childNode, String nodeName)  throws Exception {
    super.parseCPElement(childNode, nodeName);

    if (nodeName.equals("organizations")) {

      addElement(new IMSCPOrganizationsNode(childNode,this));
    }
    else if (nodeName.equals("resources")) {
      addElement(new IMSCPResourcesNode(childNode,this));
    }
  }

	public Design getLFDesign(String unpackedPath) {
		// Create the object to return
		Design design = new Design();

		IMSLDLearningDesignNode ldnode = this.getLDLearningDesignNode(); 
		
		design.setId(ldnode.getIdentifier());
		design.setName(ldnode.getTitle());
		design.setAuthor("lprisan");
		
		design.setObjectives(getObjectivesAsArray(unpackedPath));
		
		design.setRoles(getRolesAsArray(unpackedPath));

		design.setResources(getResourcesAsArray());

		
		IMSLDMethodNode method = ldnode.getMethod();
		Activity rootNode = method.getActivityAsTreeNode(this, unpackedPath);
		//We fix the "parent" field, which was not set when creating the tree from the IMSLD
		rootNode.fixParentActivities();
		
		design.setRootActivity(rootNode);
		
		design.setOriginalDesignType("IMSLD");
		
		design.setOriginalDesignData(this.getXMLContent());
		
		design.setTimestamp(new Date());
		
		return design;
	}

	private ArrayList<Resource> getResourcesAsArray() {
		ArrayList<Resource> resourcesArray = null;

		// We do not go through the resources node, but rather through all the
		// used environments (if any), which in turn reference learning-object resources or
		// services	
		IMSLDEnvironmentsNode environments = this.findEnvironmentsNode();
		
		if(environments!=null){
			for(Iterator it = environments.children.iterator();it.hasNext();){
				
				IMSLDEnvironmentNode env = (IMSLDEnvironmentNode) it.next();
				
				resourcesArray = env.getEnvironmentAsResources("", resourcesArray);
				
				
			}
		}
		return resourcesArray;
		
	}

	private IMSLDEnvironmentsNode findEnvironmentsNode() {
	    IMSLDEnvironmentsNode result = null;
	    
	    IMSLDLearningDesignNode ldnode = this.findLearningDesign();
	    
	    for(Iterator it1 = ldnode.children.iterator();it1.hasNext();){
	    	IMSLDNode child = (IMSLDNode) it1.next();
	    	if(child.node.getLocalName().equalsIgnoreCase("components")){
	    		for(Iterator it2 = child.children.iterator();it2.hasNext();){
	    	    	IMSLDNode child2 = (IMSLDNode) it2.next();
	    	    	if(child2.node.getLocalName().equalsIgnoreCase("environments")){
	    	    		result = (IMSLDEnvironmentsNode) child2;
	    	    		return result;
	    	    	}
	    		}
	    	}
	    }
	    return null;
	}

	
	private IMSCPResourcesNode findResourcesNode() {
	    IMSCPResourcesNode result = null;
	    
	    for(Iterator it = children.iterator();it.hasNext();){
	    	IMSLDNode child = (IMSLDNode) it.next();
	    	if(child.node.getLocalName().equalsIgnoreCase("resources")){
	    		result = (IMSCPResourcesNode) child;
	    		return result;
	    	}
	    }
	    return null;
	}

	private ArrayList<Role> getRolesAsArray(String unpackedPath) {
		
		IMSLDRolesNode roles = this.findRolesNode();
		
		ArrayList<Role> rolesArray = null;
		
		for(Iterator it = roles.children.iterator();it.hasNext();){
			IMSLDRoleNode role = (IMSLDRoleNode) it.next();
			
			rolesArray = role.addLFRoleToArray(unpackedPath, rolesArray);

		}
		
		return rolesArray;
	}

	protected ArrayList<String> getObjectivesAsArray(String unpackedPath) {
		IMSLDLearningDesignNode ldnode = this.getLDLearningDesignNode(); 
		
		ArrayList<String> objectiveList = null;
		//We go through the children of learning-design, looking for the objectives
		for(Iterator it = ldnode.children.iterator();it.hasNext();){
			IMSLDNode child = (IMSLDNode) it.next();
			if(child.node.getLocalName().equalsIgnoreCase("learning-objectives")){
				//We go through the items, adding each as a learning objective
				//TODO Often, Collage puts all objectives into one item, but we have no easy way of distinguishing this
				for(Iterator iter = child.children.iterator(); iter.hasNext();){
					IMSLDItemNode subchild = (IMSLDItemNode) iter.next();
					
					//TODO We should check resource type, etc... by now we assume it is text
					String path = unpackedPath + subchild.getResource().getURL();
					
					File file = new File(path);
					try {
						String content = FileUtils.readFileToString(file, "UTF-8");
						if(objectiveList==null) objectiveList = new ArrayList<String>();
						objectiveList.add(content);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}
					
					

					
				}
				return objectiveList;
			}
			
		}
		
		return objectiveList;
	}

}
