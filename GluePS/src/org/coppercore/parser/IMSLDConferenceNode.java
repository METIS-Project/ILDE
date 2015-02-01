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
import org.coppercore.component.ConferencePropertyDef;
import org.coppercore.component.SendMailPropertyDef;
import org.coppercore.exceptions.CopperCoreException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design conference element.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.5 $, $Date: 2008/04/28 08:00:35 $
 */
public class IMSLDConferenceNode extends ItemModelNode {
	private ArrayList participants = new ArrayList();
	private ArrayList observers = new ArrayList();
	private Object conferenceManager = null;
	private Object moderator = null;
	private ConferencePropertyDef conference = null;

	/**
	 * Constructs a IMSLDConferenceNode instance from the passed xml dom
	 * element.
	 * 
	 * @param aNode
	 *            the xml dom node to parse
	 * @param aParent
	 *            the parsed IMS learning design element that is the parent
	 *            element of this object
	 */
	public IMSLDConferenceNode(Node aNode, IMSLDNode aParent) {
		super(aNode, aParent);
	}

	protected void parseLDElement(Element childNode, String nodeName) throws Exception {
		super.parseLDElement(childNode, nodeName);

		if (nodeName.equals("participant")) {
			participants.add(getNamedAttribute(childNode, "role-ref"));
		} else if (nodeName.equals("observer")) {
			observers.add(getNamedAttribute(childNode, "role-ref"));
		} else if (nodeName.equals("conference-manager")) {
			conferenceManager = getNamedAttribute(childNode, "role-ref");
		} else if (nodeName.equals("moderator")) {
			moderator = getNamedAttribute(childNode, "role-ref");
		}
	}

	protected void resolveReferences() throws Exception {

		lookupAll(participants);
		lookupAll(observers);
		if (conferenceManager != null)
			conferenceManager = lookupReferent((String) conferenceManager);
		if (moderator != null)
			moderator = lookupReferent((String) moderator);

		super.resolveReferences();
	}

	protected void persist(int uolId) throws CopperCoreException {
		conference.persist();

		// make sure all children are persisted as well
		super.persist(uolId);
	}

	protected void buildItemList(final String componentId, HashMap usedItems, HashMap usedIn,
			final HashSet referencedItems, String dataType) {
		// recursively pick up on items belonging to this component
		super.buildItemList(getIdentifier(), usedItems, usedIn, referencedItems, SendMailPropertyDef.DATATYPE);
	}

	protected String getComponentDataType() {
		return ConferencePropertyDef.DATATYPE;
	}

	protected boolean buildComponentModel(int uolId, MessageList messages) {
		boolean result = super.buildComponentModel(uolId, messages);

		conference = new ConferencePropertyDef(uolId, parent.getIdentifier(), parent.getIsVisible(), getXMLContent(),
				getItemsForComponent(getIdentifier()));

		return result;
	}

	protected void writeXMLEnvironmentTree(PrintWriter output) {
		XMLTag tag = new XMLTag("conference");

		// used the parent contextId as identifier
		tag.addAttribute("identifier", parent.getIdentifier());
		tag.addAttribute("class", parent.getClassAttribute());
		tag.addAttribute("parameters", parent.getParameters());
		tag.addAttribute("isvisible", parent.getIsVisible());
		tag.addAttribute("conference-type", getConferenceType());
		tag.writeOpenTag(output);

		Iterator myIterator = this.children.iterator();
		while (myIterator.hasNext()) {
			IMSLDNode child = (IMSLDNode) myIterator.next();
			child.writeXMLEnvironmentTree(output);
		}

		tag.writeCloseTag(output);
	}

	protected void writeXMLContent(PrintWriter output) {
		XMLTag tag = new XMLTag(node.getLocalName());

		// used the parent contextId as identifier
		tag.addAttribute("identifier", parent.getIdentifier());
		tag.addAttribute("class", parent.getClassAttribute());
		tag.addAttribute("parameters", parent.getParameters());
		tag.addAttribute("isvisible", parent.getIsVisible());
		tag.addAttribute("conference-type", getConferenceType());
		tag.writeOpenTag(output);

		writeParticipants(output);
		writeObservers(output);
		writeConferenceManager(output);
		writeModerator(output);

	    Iterator myIterator = children.iterator();
	    while (myIterator.hasNext()) {
	      IMSLDNode childNode = (IMSLDNode) myIterator.next();
	      childNode.writeXMLContent(output);
	    }
	    
		tag.writeCloseTag(output);
	}

	private void writeParticipants(PrintWriter output) {
		Iterator myIterator = participants.iterator();
		while (myIterator.hasNext()) {
			IMSLDNode participant = (IMSLDNode) myIterator.next();
			XMLTag tag = new XMLTag("participant");

			tag.addAttribute("identifier", participant.getIdentifier());
			tag.writeEmptyTag(output);
		}
	}

	private void writeObservers(PrintWriter output) {
		Iterator myIterator = observers.iterator();
		while (myIterator.hasNext()) {
			IMSLDNode observer = (IMSLDNode) myIterator.next();
			XMLTag tag = new XMLTag("observer");

			tag.addAttribute("identifier", observer.getIdentifier());
			tag.writeEmptyTag(output);
		}
	}

	private void writeConferenceManager(PrintWriter output) {

		if (conferenceManager instanceof IMSLDNode) {
			XMLTag tag = new XMLTag("conference-manager");

			tag.addAttribute("identifier", ((IMSLDNode) conferenceManager).getIdentifier());
			tag.writeEmptyTag(output);
		}
	}

	private void writeModerator(PrintWriter output) {

		if (moderator instanceof IMSLDNode) {
			XMLTag tag = new XMLTag("moderator");

			tag.addAttribute("identifier", ((IMSLDNode) moderator).getIdentifier());
			tag.writeEmptyTag(output);
		}
	}

	/**
	 * Returns the value of the conference-type attribute.
	 * <p>
	 * 
	 * @return the value of the conference-type attribute
	 */
	protected String getConferenceType() {
		return getNamedAttribute("conference-type");
	}
}
