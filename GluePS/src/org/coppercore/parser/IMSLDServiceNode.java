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
import java.util.Iterator;

import org.coppercore.common.XMLTag;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design service element.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.10 $, $Date: 2008/04/28 08:00:34 $
 */
public class IMSLDServiceNode extends IMSLDNode {
	private String classDefs = null;
	private IMSLDNode child = null;

	/**
	 * Constructs a IMSLDServiceNode instance from the passed xml dom element.
	 * 
	 * @param aNode
	 *            the xml dom node to parse
	 * @param aParent
	 *            the parsed IMS learning design element that is the parent
	 *            element of this object
	 */
	public IMSLDServiceNode(Node aNode, IMSLDNode aParent) {
		super(aNode, aParent);

		// store the defined classes for this service
		classDefs = this.getNamedAttribute("class");
	}

	protected void parseLDElement(Element childNode, String nodeName) throws Exception {
		super.parseLDElement(childNode, nodeName);

		if (nodeName.equals("send-mail")) {
			child = addElement(new IMSLDSendMailNode(childNode, this));
		} else if (nodeName.equals("conference")) {
			child = addElement(new IMSLDConferenceNode(childNode, this));
		} else if (nodeName.equals("index-search")) {
			child = addElement(new IMSLDIndexSearchNode(childNode, this));
		} else if (nodeName.equals("monitor")) {
			child = addElement(new IMSLDMonitorNode(childNode, this));
		}
	}

	protected void buildClassList(HashMap classReferences) {

		if (classDefs != null) {
			String[] allClasses = classDefs.split(" ");

			for (int i = 0; i < allClasses.length; i++) {
				if (classReferences.containsKey(allClasses[i])) {
					// fetch the collection of objects having a reference to
					// this class
					ArrayList references = (ArrayList) classReferences.get(allClasses[i]);

					// the value contains component id and datatype
					String[] value = { getIdentifier(), child.getComponentDataType() };

					// add this node to the references
					references.add(value);

					// now add this new container to the class references
					classReferences.put(allClasses[i], references);
				} else {
					// create a new container for the references
					ArrayList references = new ArrayList();

					// the value contains component id and datatype
					String[] value = { getIdentifier(), child.getComponentDataType() };

					// add this node to the references
					references.add(value);

					// now add this new container to the class references
					classReferences.put(allClasses[i], references);
				}
			}
		}
	}

	protected void writeXMLEnvironmentTree(PrintWriter output) {
		XMLTag tag = new XMLTag("service");

		tag.writeOpenTag(output);
		tag.addAttribute("parameters", getParameters());

		Iterator myIterator = children.iterator();
		while (myIterator.hasNext()) {
			IMSLDNode childNode = (IMSLDNode) myIterator.next();
			childNode.writeXMLEnvironmentTree(output);
		}
		tag.writeCloseTag(output);
	}

	protected void writeXMLContent(PrintWriter output) {
		Iterator myIterator = children.iterator();
		while (myIterator.hasNext()) {
			IMSLDNode childNode = (IMSLDNode) myIterator.next();
			if (childNode instanceof IMSLDConferenceNode)
				childNode.writeXMLContent(output);
		}
	}
}
