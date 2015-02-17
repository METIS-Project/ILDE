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

package org.coppercore.component;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.coppercore.business.RoleInstance;
import org.coppercore.business.RoleParticipation;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.Parser;
import org.coppercore.exceptions.NotFoundException;
import org.coppercore.exceptions.PropertyException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This component reresent the roles tree defined for a particular run. This
 * generic roles tree for a run may be personalized on the basis of the roles
 * for which each user in the run is enrolled.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2007/04/04 09:04:12 $
 */
public class RolesTreeProperty extends Property {
	private static final long serialVersionUID = 42L;
	private Element rolesTree;

	/**
	 * Constructor for creating this component.
	 * 
	 * @param uol
	 *            Uol the Uol in which this component was declared
	 * @param run
	 *            Run the Run to which this component belongs
	 * @param propId
	 *            String the identifier of this component as defined in IMS LD
	 * @throws PropertyException
	 *             when the constructor fails
	 */
	public RolesTreeProperty(Uol uol, Run run, String propId) throws PropertyException {
		// scope is LOCAL so we may omit the user info
		super(uol, run, null, propId);
	}

	/**
	 * Returns the corresponding PropertyDefinition belonging to this component.
	 * 
	 * @throws PropertyException
	 *             when this operation fails
	 * @return PropertyDef the PropertyDefinition for this component
	 */
	protected PropertyDef findPropertyDef() throws PropertyException {
		return new RolesTreePropertyDef(uolId, propId);
	}

	/**
	 * This method is called when a new instance was created in the database. It
	 * is used to build the initial roles tree model during creation of the
	 * instance of this component. nothing.
	 */
	protected void onCreate() {
		// when new rolesTree was created, a run instance is created based on
		// the
		// original
		// rolesTree
		createRoleInstance(rolesTree, new HashMap());

		// persist the result
		persist();
	}

	/**
	 * Returns the identifier of the root role of the rolestree.
	 * 
	 * @return int the identifier of the root role of the rolestree
	 */
	public int getRootRoleId() {
		return Integer.parseInt(rolesTree.getAttribute("identifier"));
	}

	/**
	 * This method is called for each element encountered in the XML <value>
	 * </value> part, when loading properties from the database. This way
	 * Properties may be instantiated with the corrected value. The method will
	 * return true if the children of this element should parsed as well. False
	 * is returned otherwise.
	 * 
	 * @param node
	 *            Element the element encountered in the XML data stream
	 * @param anUolId
	 *            int the database id of the Uol for which the data are
	 *            retrieved
	 * @throws PropertyException
	 *             if the operation fails.
	 * @return boolean true indicating that the children should be parsed as
	 *         well. False is returned otherwise.
	 */
	protected boolean processElement(Element node, int anUolId) throws PropertyException {
		String nodeName = node.getNodeName();

		if ("value".equals(nodeName)) {
			// because we create the document ourselves we may assume that the
			// first
			// child
			// is an instance of Element
			rolesTree = (Element) node.getFirstChild();
			return true;
		}
		return false;
	}

	/**
	 * Adds the XML representation of the data part of this Property to a
	 * PrintWriter. StaticProperties do not have any instance data and therefore
	 * nothing is added to the stream.
	 * 
	 * @param result
	 *            PrintWriter
	 */
	protected void toXml(PrintWriter result) {
		result.write("<value>");
		result.write(getRolesTree());
		result.write("</value>");
	}

	/**
	 * Returns the unpersonalized roles tree. An unpersonalized roles tree
	 * contains all defined roles in a run.
	 * 
	 * @return String the XML representation of the complete unpersonalized
	 *         roles tree
	 */
	public String getRolesTree() {
		return Parser.documentToString(rolesTree);
	}

	/**
	 * Returns the roles tree as XML.
	 * 
	 * @param indent
	 *            boolean true indicates that the roles tree returned should be
	 *            properly indentated for better readability. False will not do
	 *            additional formating of the returned XML.
	 * @return String the (formatted) XML representation of the roles tree
	 */
	public String getRolesTree(final boolean indent) {
		return (indent ? Parser.documentToString2(rolesTree) : getRolesTree());
	}

	/**
	 * Gets the roles tree for the user and run passed as parameters. The roles
	 * tree is expressed in XML. The tree is personalized, meaning that all
	 * properties are filled in with their actual values for that user.
	 * 
	 * @param aRun
	 *            Run the Run for which this activity tree is retrieved
	 * @param aUser
	 *            User for which this activity is personalized
	 * @return String the XML representation for the personalized roles tree.
	 * @throws NotFoundException
	 *             when the user is not assigned to the run
	 */
	public String getPersonalizedTree(Run aRun, User aUser) throws NotFoundException {
		ArrayList userRoles = new ArrayList();
		Collection roles = RoleParticipation.findByUser(aUser, aRun);
		if (roles.isEmpty()) {
			throw new NotFoundException("User is not assigned to the run", null);
		}
		Iterator it = roles.iterator();
		while (it.hasNext()) {
			userRoles.add(String.valueOf(((RoleParticipation) it.next()).getRole().getId()));
		}

		// recursivly filter all roles, starting at the documentroot.
		personalizeXML(rolesTree, userRoles);

		// transform dom document back to string containing XML
		return Parser.documentToString2(rolesTree);
	}

	private void personalizeXML(Element element, Collection userRoles) {

		String identifier = element.getAttribute("identifier");
		if (!userRoles.contains(identifier)) {
			// user is not assigned to this role, so remove this node
			element.getParentNode().removeChild(element);
		} else {
			// Traverse the tree by recursively calling this method for all the
			// children
			Node childNode = element.getFirstChild();
			Node nextNode = null;
			while (childNode != null) {
				// determine the next node now, because childNode may be removed
				// in the
				// process
				nextNode = childNode.getNextSibling();

				if ((childNode.getNodeType() == Node.ELEMENT_NODE)
						&& (("learner".equals(childNode.getNodeName())) || ("staff".equals(childNode.getNodeName())))) {
					personalizeXML((Element) childNode, userRoles);
				}
				// move to the next node
				childNode = nextNode;
			}
		}
	}

	private int createRoleInstance(Element node, HashMap orgSiblingIds) {
		int result = -1;
		String orgId = node.getAttribute("org-identifier");
		if (orgSiblingIds.containsKey(orgId)) {
			node.getParentNode().removeChild(node);
		} else {
			orgSiblingIds.put(orgId, null);
			RoleInstance role = RoleInstance.create(orgId, run);
			result = role.getId();
			node.setAttribute("identifier", String.valueOf(result));

			// Traverse the tree by recursively calling this method for all the
			// children
			Node childNode = node.getFirstChild();
			Node nextNode = null;
			HashMap childrenOrgIds = new HashMap();
			while (childNode != null) {
				// determine the next node now, because childNode may be removed
				// in the
				// process
				nextNode = childNode.getNextSibling();

				if ((childNode.getNodeType() == Node.ELEMENT_NODE)
						&& (("learner".equals(childNode.getNodeName())) || ("staff".equals(childNode.getNodeName())))) {
					createRoleInstance((Element) childNode, childrenOrgIds);
				}
				// move to the next node
				childNode = nextNode;
			}
		}
		return result;
	}

	private int createRoleInstance(Element node) {
		return createRoleInstance(node, new HashMap());
	}

	/**
	 * Creates a new instance of the role who's id is passed.
	 * 
	 * @param roleId
	 *            the id of the role for which an new instance has to be created
	 * @return int the database id of the newly created role
	 */
	public int createNewRole(String roleId) {
		int result = -1;

		Element orgRole = findByRoleId(rolesTree, roleId);
		if (orgRole != null) {
			if (orgRole.getAttribute("create-new").equals("allowed")) {
				Node newRole = orgRole.cloneNode(true);
				orgRole.getParentNode().appendChild(newRole);
				result = createRoleInstance((Element) newRole);

				// everything is ok, so persist
				persist();
			}
		}
		return result;
	}

	/**
	 * Find the node in the dom document where org-identifier equals the given
	 * roleid.
	 * 
	 * <p>
	 * If the roleId is not found in the dom tree, the method returns null.
	 * 
	 * @param node
	 *            is the dom Node from where to search for the org-identifier
	 * @param roleId
	 *            is the id of the role to search for
	 * @return the node if found, or null if roleId was not found
	 */
	private Element findByRoleId(Element element, String roleId) {
		Element result = null;
		if (element.getAttribute("org-identifier").equals(roleId)) {
			result = element;
		} else {
			// org-identifier not found, now loop recursively over the
			// childnodes
			Node childNode = element.getFirstChild();
			while (childNode != null) {
				if ((childNode.getNodeType() == Node.ELEMENT_NODE)
						&& (("learner".equals(childNode.getNodeName())) || ("staff".equals(childNode.getNodeName())))) {
					result = findByRoleId((Element) childNode, roleId);
					if (result != null)
						break;
				}
				childNode = childNode.getNextSibling();
			}
		}
		return result;
	}

	private Element findByRoleinstanceId(Element element, int instanceId) {
		Element result = null;
		int id = Integer.parseInt(element.getAttribute("identifier"));
		if (id == instanceId) {
			result = element;
		} else {
			// identifier not found, now loop recursively over the childnodes
			Node childNode = element.getFirstChild();
			while (childNode != null) {
				if ((childNode.getNodeType() == Node.ELEMENT_NODE)
						&& (("learner".equals(childNode.getNodeName())) || ("staff".equals(childNode.getNodeName())))) {
					result = findByRoleinstanceId((Element) childNode, instanceId);
					if (result != null)
						break;
				}
				childNode = childNode.getNextSibling();
			}
		}
		return result;
	}

	/**
	 * Returns the instance identifier of the parent role of the specified role.
	 * 
	 * @param instanceId
	 *            int the id of role instance
	 * @return int the id of the parent role or -1 if the role has no parent
	 */
	public int getParentId(int instanceId) {
		int result = -1;
		Element instance = findByRoleinstanceId(rolesTree, instanceId);
		if (instance != null) {
			Node parent = instance.getParentNode();
			if (parent.getNodeType() == Node.ELEMENT_NODE) {
				String identifier = ((Element) parent).getAttribute("identifier");
				if (!identifier.equals("")) {
					result = Integer.parseInt(identifier);
				}
			}
		}
		return result;
	}
}
