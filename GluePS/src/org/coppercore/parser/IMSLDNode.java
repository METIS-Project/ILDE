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
import java.io.StringWriter;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.coppercore.common.MessageList;
import org.coppercore.common.Parser;
import org.coppercore.condition.Conditions;
import org.coppercore.exceptions.CopperCoreException;
import org.coppercore.exceptions.ValidationException;
import org.coppercore.expression.Operand;
import org.doomdark.uuid.UUIDGenerator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import glueps.core.model.Activity;

/**
 * Implements a base node for all IMS LD element nodes.
 * <p>
 * This class implements the default behaviour for all IMS LD element nodes and so forms the root
 * class of the CopperCore parsing object model.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.38 $, $Date: 2005/09/28 09:15:15 $
 */
public abstract class IMSLDNode {
  /** The xml dom node containing the actual ld xml for this element. */
  protected Node node;

  /** The parent node of this element, or null if this instance is the root. */
  protected IMSLDNode parent;

  /** The metadata node of this element, or null if there is no metadata. */
  protected IMSLDMetadataNode metadata = null;

  /** The title of this element, or null if this element has no title. */
  protected IMSLDTitleNode title = null;

  /**
   * Contains all child elements of this ld element. The children are instances of classes from the
   * parser object model.
   */
  protected Vector children = new Vector();

  // some commonly used attributes
  private String isVisibleAttr;

  private String identifierAttr = null;

  private String classAttr = null;

  private String parametersAttr = null;

  protected static final String TITLE_SEPARATOR = " - ";
  
  
  /**
   * Creates a new instance of IMSLDNode.
   * <p>
   * During creation the following attributes are set by the defining Node:
   * <ul>
   * <li>identifier, if no identifier is present, a unique identifier is created.</li>
   * <li>isvisible</li>
   * <li>class</li>
   * <li>parameters</li>
   * </ul>
   * 
   * @param aNode
   *          the xml element from the manifest defining this instance
   * @param aParent
   *          the parent element of this instance
   */
  public IMSLDNode(Node aNode, IMSLDNode aParent) {
    node = aNode;
    parent = aParent;

    identifierAttr = this.getNamedAttribute("identifier");
    if (identifierAttr != null) {
      // add element to hashtable for resolving referents later on
      addComponent(identifierAttr, this);
    }
    else {
    	if (aParent instanceof IMSLDServiceNode) {
    		//because the identifier of services is not stored in the service elements themselves, but
    		//rather in the Service wrapper we must retrieve this identifier if possible
    		identifierAttr = aParent.getIdentifier();
    	}
    	else {
        // we must create a value for the identifier as we might need it later on
        // when processing a role tree
        identifierAttr = UUIDGenerator.getInstance().generateTimeBasedUUID().toString();
    	}  
    }

    isVisibleAttr = this.getNamedAttribute("isvisible");
    classAttr = this.getNamedAttribute("class");
    parametersAttr = this.getNamedAttribute("parameters");
  }

  /**
   * Creates parser object model instances for all recognized child elements.
   * <p>
   * The method does not actually create any instances but delegates this to namespace specific
   * instances of parse.
   * 
   * @throws Exception
   *           when there is an error creating a new instance
   */
  protected void parse() throws Exception {
    Node childNode = node.getFirstChild();
    // loop over all children
    while (childNode != null) {
      // only interested in xml elements
      if (childNode.getNodeType() == Node.ELEMENT_NODE) {
        // determine the namespace of this node
        String ns = childNode.getNamespaceURI();
        String localName = childNode.getLocalName();
        if (Parser.IMSLDNS.equals(ns)) {
          parseLDElement((Element) childNode, localName);
        }
        else if (Parser.IMSCPNS.equals(ns)) {
          parseCPElement((Element) childNode, localName);
        }
        else if (Parser.IMSMDNS.equals(ns)) {
          parseMDElement((Element) childNode, localName);
        }
      }
      childNode = childNode.getNextSibling();
    }
  }

  /**
   * Parses the passed xml dom LD element node with the given name.
   * <p>
   * If the method recognizes a name as an element it can handle, it parses that element.
   * 
   * @param childNode
   *          Element the xml dom element node to be parsed
   * @param nodeName
   *          String the name of the node to parse
   * @throws Exception
   *           when an error occurs during parsing
   */
  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    if ("title".equals(nodeName)) {
      if (title == null) {
        // only save the title of the first item encountered during parsing
        title = (IMSLDTitleNode) addElement(new IMSLDTitleNode(childNode, this));
      }
      else {
        Logger logger = Logger.getLogger(this.getClass());
        logger.debug("title found in " + node.getLocalName());
      }
    }
    else if ("metadata".equals(nodeName)) {
      metadata = (IMSLDMetadataNode) addElement(new IMSLDMetadataNode(childNode, this));
    }
  }

  /**
   * Parses the passed xml dom CP element node with the given name.
   * <p>
   * If the method recognizes a name as an element it can handle, it parses that element.
   * 
   * @param childNode
   *          Element the xml dom element node to be parsed
   * @param nodeName
   *          String the name of the node to parse
   * @throws Exception
   *           when an error occurs during parsing
   */
  protected void parseCPElement(Element childNode, String nodeName) throws Exception {
    // default do nothing
  }

  /**
   * Parses the passed xml dom MD element node with the given name.
   * <p>
   * If the method recognizes a name as an element it can handle, it parses that element.
   * 
   * @param childNode
   *          the xml dom element node to be parsed
   * @param nodeName
   *          the name of the node to parse
   * @throws Exception
   *           when an error occurs during parsing
   */
  protected void parseMDElement(Element childNode, String nodeName) throws Exception {
    // default do nothing
  }

  /**
   * Adds the passed element to the list of children.
   * <p>
   * After adding the element to the list all its children are parsed.
   * 
   * @param element
   *          the element to add to the list
   * @return the added element
   * @throws Exception
   *           when there is an error parsing the child elements of the added element
   */
  protected IMSLDNode addElement(IMSLDNode element) throws Exception {
    children.add(element);
    element.parse();
    return element;
  }

  /**
   * Adds the passed component with the specified identifier to the global list of all components.
   * <p>
   * This list is used to resolve all references to LD elements. This method bubbles to the root of
   * the parser object model.
   * 
   * @param identifier
   *          the id of the passed component
   * @param component
   *          the component to store on the list
   */
  protected void addComponent(String identifier, IMSLDNode component) {
    parent.addComponent(identifier, component);
  }

  /**
   * Returns the value of the specified attribute from the specified namespace.
   * <p>
   * The value is read from the xml node defining this element.
   * 
   * @param attributeName
   *          the name of the attribute to lookup
   * @param NS
   *          the namespace the attribute belongs to
   * @return the value of the attribute or null if the attribute was not found
   */
  protected String getNamedAttribute(String attributeName, String NS) {
    return Parser.getNamedAttribute(node, attributeName, NS);
  }

  /**
   * Returns the value of the specified attribute.
   * <p>
   * The value is read from the xml node defining this element.
   * 
   * @param attributeName
   *          the name of the attribute to lookup
   * @return the value of the attribute or null if the attribute was not found
   */
  protected String getNamedAttribute(String attributeName) {
    return Parser.getNamedAttribute(node, attributeName);
  }

  /**
   * Returns the value of the specified attribute from the passed node.
   * <p>
   * The value is read from the xml node passed to the method.
   * 
   * @param xmlNode
   *          the xml node to fetch the attribute for
   * @param attributeName
   *          the name of the attribute to lookup
   * @return the value of the attribute or null if the attribute was not found
   */
  protected String getNamedAttribute(Node xmlNode, String attributeName) {
    return Parser.getNamedAttribute(xmlNode, attributeName);
  }


  /**
   * Resolves all references by looping over all children.
   * <p>
   * Subclasses need to override this implementation to actually resolve the references. The
   * subclass needs to call the super implementation to make sure all elements are processed.
   * 
   * @throws Exception
   *           when there is an error resolving a reference. If the LD was validated this should not
   *           occur.
   */
  protected void resolveReferences() throws Exception {
    Iterator myIterator = children.iterator();

    while (myIterator.hasNext()) {
      IMSLDNode currentElement = (IMSLDNode) myIterator.next();

      currentElement.resolveReferences();
    }
  }

  /**
   * Returns a String representation of this instance.
   * <p>
   * This representation contains the name of the class and the title.
   * 
   * @return the string representation of this instance
   */
  public String toString() {
    String value = getTitle();
    return (this.getClass().getName() + "[" + (!"".equals(value) ? value : "no-title") + "]");
  }

  /**
   * Returns the title of this element as a String.
   * 
   * @return the title of this element.
   */
  public String getTitle() {
    String result = "";
    if (title != null) {
      result = title.getContent();
    }
    return result;
  }

  /**
   * 
   * @param aNode
   * @param roles
   */
  /*
   * protected void getReferingRoleIds(IMSLDNode aNode, Set roles) {
   * parent.getReferingRoleIds(aNode, roles); }
   */

  /**
   * Retrieves the IMSLDNode with the specified identifier from the parser object model.
   * <p>
   * This method may only be called after the entire parser object model is created.
   * 
   * @param identifier
   *          the id of the node to lookup
   * @return the node with the specified identifier
   * @throws Exception
   *           when the node could not be retrieved, this should occur after the LD is validated.
   */
  protected IMSLDNode lookupReferent(String identifier) throws Exception {
    return getManifest().lookupReferent(identifier);
  }

  /**
   * Returns the xml representation of the metadata.
   * <p>
   * The method return null if no metadata is available.
   * 
   * @return the xml representation of the metadata
   */
  protected String getMetadataXml() {
    String result = null;
    if (metadata != null) {
      result = metadata.getXMLContent();
    }
    return result;
  }

  /**
   * Replaces all identifiers in the referents list by the IMSLDnodes they point to.
   * 
   * @param referents
   *          the list of referents to lookup
   * @throws Exception
   *           when a referent could not be resolved. This should not occur in a validated LD
   *           package
   */
  protected void lookupAll(AbstractList referents) throws Exception {
    // check if there are any referents to resolve
    if (referents == null) {
      return;
    }
    Iterator myIterator;
    int index;

    // resolve referents to the environments
    myIterator = referents.iterator();
    index = 0;

    while (myIterator.hasNext()) {
      String ref = (String) myIterator.next();
      IMSLDNode referee = lookupReferent(ref);
      if (referee == null) {
        throw new Exception("Failed to resolve referent " + ref);
      }
      // swap the identifier with the found referee node
      referents.set(index++, referee);
    }
  }

  /**
   * Checks if the passed role is one of the parent roles of this node.
   * 
   * @param aRole
   *          the role to check for
   * @return true if the role is found amongst the parents
   */
  protected boolean hasParent(IMSLDNode aRole) {
    return false;
  }

  /**
   * Checks if this node has a time-limit for completing the element.
   * 
   * @return tre if this node has a time-limit, false otherwise
   */
  protected boolean hasTimeLimit() {
    return false;
  }

  /*
   * protected void getParentIds(Collection parents) { //default do nothing }
   */
  /**
   * Returns the expression for this node.
   * 
   * @return the expression
   * @throws ValidationException
   *           when the expression is not valid
   */
  protected Operand getExpression() throws ValidationException {
    // default do nothing
    Logger logger = Logger.getLogger(this.getClass());
    logger.error("IMSLDNode.getExpression() called, this should not happen.");
    return null;
  }

  /**
   * Checks if this node is relevant for the specified role.
   * 
   * @param aRole
   *          the role this node should be relevant for
   * @return true if this node is relevant, otherwise the method returns false
   */
  protected boolean isRelevantFor(IMSLDNode aRole) {
    return false;
  }

  /**
   * Return the component data type of the component defined in a service. Returns null if not
   * applicable.
   * 
   * @return String the data type of the component or null if not applicable
   */
  protected String getComponentDataType() {
    return null;
  }

  /**
   * Writes this node as xml to the specified output stream as part of the activity tree.
   * <p>
   * The node is only written when it is relevant for the specified role.
   * 
   * @param output
   *          the stream for the activity tree
   * @param aRole
   *          acts as a filter to decide if this node is relevant
   */
  protected void writeXMLPlay(PrintWriter output, IMSLDNode aRole) {
    // default do nothing
  }

  /**
   * Writes this node as xml to the specified output stream as part of the activity tree.
   * <p>
   * The node is only written when it is relevant for the specified role.
   * 
   * @param output
   *          the stream for the activity tree
   * @param aRole
   *          acts as a filter to decide if this node is relevant
   * @param envIds
   *          lists all parent environments to be added to the environments of this node
   */
  protected void writeXMLPlay(PrintWriter output, IMSLDNode aRole, ArrayList envIds) {
    // default do nothing
  }

  /**
   * Writes an xml representation of this node to the role tree.
   * 
   * @param output
   *          the stream to write the xml to
   */
  protected void writeXMLRolesTree(PrintWriter output) {
    // default do nothing
  }

  /**
   * Writes this node as xml to the content stream.
   * 
   * @param output
   *          the stream to write the xml to
   */
  protected void writeXMLContent(PrintWriter output) {
    // default do nothing
  }

  /**
   * writes this node as xml to the environment tree.
   * 
   * @param output
   *          the stream to write the xml to
   */
  protected void writeXMLEnvironmentTree(PrintWriter output) {
    // default do nothing
  }

  /**
   * Persists the node and all its children.
   * <p>
   * This default implementation makes sure all childeren are persisted. It is the responsibility of
   * the subclass to store the node itself and to call the super implementation in order to store
   * the children.
   * 
   * @param uolId
   *          the id of the unit of learning this node is part of
   * @throws CopperCoreException
   *           when there is an error persisting the node
   */
  protected void persist(int uolId) throws CopperCoreException {
    // persist all children
    Iterator myIterator = children.iterator();

    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();

      child.persist(uolId);
    }
  }

  /**
   * Checks if this node and its children are semantically.
   * <p>
   * This default implementation makes sure all childeren are validated. It is the responsibility of
   * the subclass to validate the node itself if applicable and to call the super implementation in
   * order to validate the children.
   * 
   * @param messages
   *          the list to hold all validation messages
   * @return the outcome of the validation, true if valid, otherwise false
   */
  protected boolean isValid(MessageList messages) {
    boolean result = true;

    // validate all children
    Iterator myIterator = children.iterator();

    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();

      result &= child.isValid(messages);
    }
    return result;
  }

  /**
   * Builds a list of all referenced show/hide items belonging to the specified component.<p>
   * This method loops over all child nodes, so a subclass overriding this method should
   * call the super method to ensure the children are processed too. The build lists are
   * members of the IMSLDLearningDesignNode.
   * 
   * @param componentId the id of the component to build the lists for
   * @param usedItems the list of items used by this component
   * @param usedIn the list of items using this component
   * @param referencedItems the items referenced by this component
   * @param dataType the datatype of the component
   */
  protected void buildItemList(final String componentId, HashMap usedItems, HashMap usedIn,
      final HashSet referencedItems, String dataType) {

    Iterator myIterator = children.iterator();

    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();
      child.buildItemList(componentId, usedItems, usedIn, referencedItems, dataType);
    }
  }

  /**
   * Returns all show/hide items belonging to the specified component.<p>
   * @param componentId id of the component to gather the show/hide items for
   * @return a Collection of all show/hide items for the specified component
   */
  protected Collection getItemsForComponent(String componentId) {
    return getManifest().getLDLearningDesignNode().getItemsForComponent(componentId);
  }

  /**
   * Creates a global list of all classes refered from components of the learning design.
   * @param classReferences the list where all classes are added to 
   */
  protected void buildClassList(HashMap classReferences) {

    Iterator myIterator = children.iterator();

    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();
      child.buildClassList(classReferences);
    }
  }

  /**
   * Creates a global list of all items being refered from a show hide construct.
   * 
   * @param referencedItems the list to add the found items to
   */
  protected void buildItemReferenceList(HashSet referencedItems) {

    Iterator myIterator = children.iterator();

    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();
      child.buildItemReferenceList(referencedItems);
    }
  }

  /**
   * Constucts the component model from the parser object model.<p>
   * The component model is the model actually used by the CopperCore runtime to
   * model the learning design. The specific subclasses implement the actual constructing
   * code and they need to call the super implementation to make sure the entire model
   * is constructed.  
   * @param uolId id of the current unit of learning
   * @param messages container for all messages generated during the construction if the
   * component model.
   * @return true when the component model is successfully created, else false
   */
  protected boolean buildComponentModel(int uolId, MessageList messages) {
    boolean result = true;

    // buildComponentModel for all children
    Iterator myIterator = children.iterator();

    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();

      result &= child.buildComponentModel(uolId, messages);
    }
    return result;
  }

  /**
   * Collects all implicit conditions.<p>
   * Implicit conditions are conditions not explicitly defined in the conditions element
   * of unit of learning. 
   * @param conditions the list where the conditions are added to 
   */
  protected void getSystemConditions(Conditions conditions) {
    Iterator myIterator = children.iterator();

    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();

      child.getSystemConditions(conditions);
    }
  }

  /**
   * Returns the play structure for a particular role as XML tree by recursively calling its parent.
   * <p>
   * The returned play is a subset of the complete play as defined in the learning design. Only those
   * parts that are relevant for the specified role are included.  
   * 
   * @param aRole the role to retrieve the play for
   * @return the xml representation of the play
   */
  public String getXMLPlay(IMSLDNode aRole) {
    return parent.getXMLPlay(aRole);
  }

  /**
   * Returns the identifier of this object.
   * @return the identifier of this object
   */
  protected String getIdentifier() {
    return identifierAttr;
  }

  /**
   * Returns the default visibility status of this ld element.<p>
   * If the visibility is not specified explicitly in the learning design
   * the status defaults to true.
   * @return "true" when the element is visible, "false" otherwise
   */
  protected String getIsVisible() {
    return isVisibleAttr == null ? "true" : isVisibleAttr;
  }

  /**
   * Returns the value of the class attribute.
   * @return the value of the class attribute
   */
  protected String getClassAttribute() {
    return classAttr;
  }

  /**
   * Returns the optional additional parameters defined in the learning design for
   * this ld element.
   * @return the parameters of this ld element or null if the attribute was not defined
   */
  protected String getParameters() {
    
    return (parametersAttr == null)? parametersAttr : Parser.escapeXML(parametersAttr);
  }

  /**
   * Notifies the manifest an error has occurred.
   */
  protected void error() {
    parent.error();
  }

  /**
   * Returns true if the specified role-part is part of a complete-act condition.<p>
   * Default behaviour is to always return false.
   * @param rolePart the element to check if it is a part of a complete-act
   * @return true if this node is part of a complete-act condition.
   */
  protected boolean isPartOfCompletion(IMSLDRolePartNode rolePart) {
    return false;
  }

  /**
   * Converts the dom node of this element to an XML representation.
   * 
   * @return the XML for the current dom node
   * @throws TransformerException when there is an error converting the dom node
   */
  protected String toXML() throws TransformerException {

    // Serialisation through Transform.
    StringWriter out = new StringWriter();
    DOMSource domSource = new DOMSource(node);
    StreamResult streamResult = new StreamResult(out);
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer serializer = null;
    serializer = tf.newTransformer();
    serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    serializer.transform(domSource, streamResult);

    return out.toString();
  }

  /**
   * Returns the <code>IMSLDLearningDesign</code> node in the content package tree.
   * @return the IMSLDLearningDesign node from the parser object model
   */
  protected IMSLDLearningDesignNode findLearningDesign() {

    IMSLDLearningDesignNode result = null;

    Iterator myIterator = children.iterator();

    while (myIterator.hasNext() && (result == null)) {
      IMSLDNode child = (IMSLDNode) myIterator.next();
      result = child.findLearningDesign();
    }
    return result;
  }

  /**
   * Returns the manifest node of this object model.
   * 
   * @return the manifest node of this object model
   */
  protected IMSCPManifestNode getManifest() {
    return (parent != null ? parent.getManifest() : (IMSCPManifestNode) this);
  }

  /**
   * Finds the RolesNode by looping over all children.<p>
   * The RolesNode is the root node for the roles tree.
   * 
   * @return the found RolesNode
   */
  protected IMSLDRolesNode findRolesNode() {
    IMSLDRolesNode result = null;
    Iterator it = children.iterator();
    while (it.hasNext() && (result == null)) {
      IMSLDNode child = (IMSLDNode) it.next();
      result = child.findRolesNode();
    }
    return result;
  }

//  public IMSLDNode findMethodNode() {
//	    IMSLDNode result = null;
//	    if(node.getLocalName().equals("method")) return this;
//	    else{
//		    Iterator it = children.iterator();
//		    while (it.hasNext() && result==null) {
//		      IMSLDNode child = (IMSLDNode) it.next();
//		      result = child.findMethodNode();
//		    }
//	    }
//	    return result;
//	  }

  
  /**
   * Returns the prefix for the expression defining the complete condition of this
   * element.
   * @return the prefix for the name of the complete expression property
   */
  protected String getNameForCompleteExpression() {
    return null;
  }

  /**
   * Returns a String containing an EnvironmentTree wrapped by a environments tag.
   * @return a String containing an EnvironmentTree wrapped by a environments tag
   */
  protected String getXMLEnvironmentTree() {

    StringWriter outputStream = new StringWriter();
    try {
      PrintWriter output = new PrintWriter(outputStream);
      try {
        writeXMLEnvironmentTree(output);
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

  /**
   * Returns the XML representation of the content for the current node including all child nodes.
   * 
   * @return XML representation of the content
   */
  public String getXMLContent() {
    StringWriter outputStream = new StringWriter();
    try {
      PrintWriter output = new PrintWriter(outputStream);

      try {
        writeXMLContent(output);
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

  /**
   * Gets the text content from the xml node of this instance.
   * 
   * @return the text content of this node
   */
  protected String getContent() {
    StringBuffer result = new StringBuffer();

    Node child = node.getFirstChild();
    // loop over all children as the text may be interrupted by comment nodes etc.
    while (child != null) {
      // add all nodes of type TEXT NODE to the output
      if (child.getNodeType() == Node.TEXT_NODE) {
        // call documentToString to escape entities
        result.append(Parser.documentToString(child));
      }
      child = child.getNextSibling();
    }
    return result.toString();
  }

  
  
  /**
   * Gets the learning design title of the UoL
   * 
   * @return String the learning design title of the UoL
   * 
   * @author Susana Carretero Galiano
   * @author Vanesa Martín de Cima
   */
//   public String getLDTitle() throws Exception {
//	   String result = "";
//	  IMSLDLearningDesignNode imsldLDNode = this.findLearningDesign();
//	  result = imsldLDNode.getTitle();
//	  return (result);
//   }
   
  /**
   * Gets the code for printing the teacher's (staff) complete activity tree,
   * extracting the method node from the current IMSLDNode and recursively its
   * needed children (the ones that contain information about the teacher's
   * activity tree).
   * 
   * @param parentName String representing the name for the parent node of the current node
   * 		that is being analyzed
   * @param childName String representing the name for the node that is being analyzed
   * @param imscpManifestNode IMSCPManifestNode that contains the complete manifest
   * @return String the code that prints the teacher's complete activity tree
   * 
   * @author Susana Carretero Galiano
   * @author Vanesa Martín de Cima
   */
//   public String getCompleteActivityTreeTeacher(String parentName, String childName, IMSCPManifestNode imscpManifestNode) throws Exception {
// 	   String result = "";
// 	   String nodeName = node.getLocalName();
// 	   
// 	   try {
// 		   
//           //these are the nodes we need to analyze from the manifest because they contain 
//           //information related to the teacher's activity tree.
//           //if the node is another we don't need it so we don't do nothing with it 
//           if ((nodeName.equals("manifest")) || (nodeName.equals("organizations")) || (nodeName.equals("learning-design")) || (nodeName.equals("method")) 
//        		   || (nodeName.equals("play")) || (nodeName.equals("act")) || (nodeName.equals("role-part")) || (nodeName.equals("title"))){
//        	 if ((nodeName.equals("play"))||(nodeName.equals("act"))) {
// 			  	if (nodeName.equals("play")) {
// 			  		//we create the code for printing a play node and continue analyzing its children
// 			  		result = "var "+childName+" = new WebFXTreeItem('"+getTitle()+"');"+childName+".icon = webFXTreeConfig.play;"+childName+".openIcon = webFXTreeConfig.playopen;"+parentName+".add("+childName+");";
// 			  		Iterator it = children.iterator();
// 		 	 		int i = 1;
// 		 	 	   	while (it.hasNext()) {
// 		 	 	   		IMSLDNode child = (IMSLDNode) it.next();
// 		 	 	   		String newChild = childName+i;
// 		 	 	   		result = result+child.getCompleteActivityTreeTeacher(childName,newChild,imscpManifestNode);
// 		 	 	   		i++;
// 		 	 	   	}
// 			  	}
// 			  	
// 			  	if (nodeName.equals("act")) {
// 			  		//we create the code for printing an act node in the tree
// 			  		result = "var "+childName+" = new WebFXTreeItem('"+getTitle()+"');"+childName+".icon = webFXTreeConfig.act;"+childName+".openIcon = webFXTreeConfig.actopen;"+parentName+".add("+childName+");";
// 			  		Iterator it = children.iterator();
// 		 	 		int i = 1;
// 		 	 	   	while (it.hasNext()) {
// 		 	 	   		IMSLDNode child = (IMSLDNode) it.next();
// 		 	 	   		String newChild = childName+i;
// 		 	 	   		result = result+child.getCompleteActivityTreeTeacher(childName,newChild,imscpManifestNode);
// 		 	 	   		i++;
// 		 	 	   	}
// 			  	}
// 			   	 
//        	 } else if (nodeName.equals("role-part")) {
//	 		  //the nodes that are role-part ones, need a special treatment
//	 		  	NodeList nodeChilds = node.getChildNodes();
//			    int length = nodeChilds.getLength();
//			    for (int i=0;i<length;i++) {
//			    	  Node child = nodeChilds.item(i);
//			    	  if (child instanceof Element) {
//			    		  String childNodeName = child.getLocalName();
//			    		  if ((childNodeName.equals("support-activity-ref"))){
//			    			  //a role-part node can contain support-activity-ref nodes, that are
//			    			  //the ones which have the teachers' activities (support activities)
//			    			  //so we analyze the role-part childs that are support-activity-ref
//			    			  //extract the ref for the support activity and look for it in the
//			    			  //manifest structure
//		 	   	   			  String ref = getNamedAttribute((Element) child,"ref");
//		 	   	   			  result = result + imscpManifestNode.getSupportActivity (parentName,childName+i,ref);
//		 	   	   		   } //if
//			    		  
//			    	  }//if
//			    	
//			    } //for
//			    
//			 } else {
//				 //with other nodes needed we must analyze their children
//				 Iterator it = children.iterator();
//				 int i = 1;
//				 while (it.hasNext()) {
//	 	   		  	IMSLDNode child = (IMSLDNode) it.next();
//	 	   		  	result = result+child.getCompleteActivityTreeTeacher(parentName,childName,imscpManifestNode);
//	 	   		  	i++;
//	 	   	  	}
//			 }
//           } else {
//        	   //no needed nodes, so do nothing
//           }
//  	   	   
// 	   } /*try*/ catch (Exception e) {
// 		   
// 	   }
// 	   return result;
//    }
//   
   /**
    * Gets the code for printing a support activity into the teacher's (staff) 
    * complete activity tree, extracting the method node from the current IMSLDNode
    * and recursively its needed children (the ones that contain the support activities,
    * until we find the support activity with the ref passed by parameter).
    * 
    * @param parentName String representing the name for the parent node of the current node
    * 		that is being analyzed
    * @param childName String representing the name for the node that is being analyzed
    * @param ref String that is the reference of the support activity we are looking for
    * @return String the code that prints the support activity
    * 
    * @author Susana Carretero Galiano
    * @author Vanesa Mart�n de Cima
    */
//    public String getSupportActivity(String parentName, String childName, String ref) throws Exception {
// 	   String result = "";
// 	   String nodeName = node.getLocalName();
// 	   try {
//        
//       //these are the nodes we need to analyze from the manifest because they contain 
//       //information related to the support activity we are looking for.
//       //if the node is another we don't need it so we don't do nothing with it 
//       if ((nodeName.equals("manifest")) || (nodeName.equals("organizations")) || (nodeName.equals("learning-design")) 
// 			   || (nodeName.equals("components")) || (nodeName.equals("activities")) || (nodeName.equals("support-activity"))){
//    	   if ((nodeName.equals("support-activity"))) {
// 			  if (getNamedAttribute("identifier").equals(ref)) {
// 				  //we have found the support activity we were looking for
// 			  	if (getNamedAttribute("isvisible").equals("true")) {
// 			  		//we add to result the code for printing the node of the support activity into the teacher's activity tree
// 			  		result = "var "+childName+" = new WebFXTreeItem('"+getTitle()+"');"+childName+".icon = webFXTreeConfig.suppact;"+childName+".openIcon = webFXTreeConfig.suppactopen;"+parentName+".add("+childName+");";
// 			  	} //if
//	 			 
// 			  }//if
//	 	 	   	
//	 	   } else {
//	 		   //if the node is another one needed, but not the one that has the info about
//	 		   //the support activity we call recursively this function to analyze its children
//	 		   Iterator it = children.iterator();
//	 		   int i = 1;
//	 	   	   while (it.hasNext()) {
//	 	   		   IMSLDNode child = (IMSLDNode) it.next();
//	 	   		   result = result+child.getSupportActivity(parentName,childName,ref);
//	 	   		   i++;
//	 	   	   }
//		   }
// 	   } else {
// 		   //no needed node, so do nothing
// 	   }
//    	   
// 	   } /*try*/ catch (Exception e) {
// 		   
// 	   }
// 	   
// 	   return result;
//    }
    
    
    /**
     * Gets the code for printing the students' complete activity tree,
     * extracting the method node from the current IMSLDNode and recursively its
     * needed children (the ones that contain information about the students'
     * activity tree).
     * 
     * @param parentName String representing the name for the parent node of the current node
     * 		that is being analyzed
     * @param childName String representing the name for the node that is being analyzed
     * @param imscpManifestNode IMSCPManifestNode that contains the complete manifest
     * @return String the code that prints the teacher's complete activity tree
     * 
     * @author Susana Carretero Galiano
     * @author Vanesa Mart�n de Cima
     */
//    public String getCompleteActivityTree(String parentName, String childName, IMSCPManifestNode imscpManifestNode) throws Exception {
//  	   String result = "";
//  	   String nodeName = node.getLocalName();
//  	   try {
//         
//            //these are the nodes we need to analyze from the manifest because they contain 
//            //information related to the teacher's activity tree.
//            //if the node is another we don't need it so we don't do nothing with it 
//            if ((nodeName.equals("manifest")) || (nodeName.equals("organizations")) || (nodeName.equals("learning-design")) || (nodeName.equals("method")) 
//            		|| (nodeName.equals("play")) || (nodeName.equals("act")) || (nodeName.equals("role-part")) || (nodeName.equals("title"))){
//  		   
//            	if ((nodeName.equals("play"))||(nodeName.equals("act"))) {
//            		//we create the code for printing a play node and continue analyzing its children
// 			  		if (nodeName.equals("play")) {
// 			  			result = "var "+childName+" = new WebFXTreeItem('"+getTitle()+"');"+childName+".icon = webFXTreeConfig.play;"+childName+".openIcon = webFXTreeConfig.playopen;"+parentName+".add("+childName+");";
// 			  			Iterator it = children.iterator();
// 			  			int i = 1;
//  		 	 	   		while (it.hasNext()) {
//  		 	 	   			IMSLDNode child = (IMSLDNode) it.next();
//  		 	 	   			String newChild = childName+i;
//  		 	 	   			result = result+child.getCompleteActivityTree(childName,newChild,imscpManifestNode);
//  		 	 	   			i++;
//  		 	 	   		}
// 			  		}
//  			  	
// 			  		if (nodeName.equals("act")) {
//  			  		//we obtain the title of the role corresponding to this act
// 			  			String ldRoleTitle = getLDRoleTitle(imscpManifestNode);
// 			  			String role = " ("+ldRoleTitle+")";
//  			  		
// 			  			//we create the code for printing an act node with its corresponding role
// 			  			//and continue analyzing its children
// 			  			result = "var "+childName+" = new WebFXTreeItem('"+getTitle()+"*"+role+"');"+childName+".icon = webFXTreeConfig.act;"+childName+".openIcon = webFXTreeConfig.actopen;"+parentName+".add("+childName+");";
// 			  			Iterator it = children.iterator();
// 			  			int i = 1;
//  		 	 	   		while (it.hasNext()) {
//  		 	 	   			IMSLDNode child = (IMSLDNode) it.next();
//  		 	 	   			String newChild = childName+i;
//  		 	 	   			result = result+child.getCompleteActivityTree(childName,newChild,imscpManifestNode);
//  		 	 	   			i++;
//  		 	 	   		}
// 			  		}
//  			  	
//            	} else if (nodeName.equals("role-part")) {
//            	//the nodes that are role-part ones, need a special treatment
//            		NodeList nodeChilds = node.getChildNodes();
//            		int length = nodeChilds.getLength();
//            		for (int i=0;i<length;i++) {
//            			Node child = nodeChilds.item(i);
//            			if (child instanceof Element) {
//            				String childNodeName = child.getLocalName();
// 			    		
//            				//a role-part node can contain learning-activity-ref nodes or 
//            				//activity-structure-ref nodes (a set of learning activities,
//            				//so they are composed of learning-activity-ref nodes), that 
//            				//are the ones which have the students' activities (learning 
//            				//activities) so we analyze the role-part childs that have this 
//            				//kind of nodes extract the ref for the learning activities and 
//            				//look for them in the manifest structure
//            				if ((childNodeName.equals("learning-activity-ref")) || (childNodeName.equals("activity-structure-ref"))){
//            					String ref = getNamedAttribute((Element) child,"ref");
// 		 	   	   				if (childNodeName.equals("learning-activity-ref")) {
// 		 	   	   					result = result + imscpManifestNode.getLearningActivities (parentName,childName+i,ref);
// 		 	   	   				} //if
// 		 	   	   				if (childNodeName.equals("activity-structure-ref")) {
// 		 	   	   					result = result + imscpManifestNode.getActivityStructure (parentName,childName+i,ref,imscpManifestNode);
// 		 	   	   				} //if
// 		 	 		   		} //if
//            			}//if
//            		} //for
//            		
//            	} else {
// 	 	  			Iterator it = children.iterator();
// 	 	  			int i = 1;
// 	 	  			while (it.hasNext()) {
// 	 	  				IMSLDNode child = (IMSLDNode) it.next();
// 	 	  				result = result+child.getCompleteActivityTree(parentName,childName,imscpManifestNode);
// 	 	  				i++;
// 	 	  			}	
// 	 	  		}
//            	
//            } else {
//            }
//  	   	   
//  	   } catch (Exception e) {
//  		   
//  	   }
//  	   return result;
//    }
    
    /**
     * Gets the title of the role whose IMSLDRoleNode is in current IMSLDNode.
     * 
     * @param imscpManifestNode IMSCPManifestNode that contains the complete manifest
     * @return String the title of the role that is represented in the IMSLDRoleNode 
     * 			contained in current IMSLDNode.
     * 
     * @author Susana Carretero Galiano
     * @author Vanesa Mart�n de Cima
     */
//    public String getLDRoleTitle(IMSCPManifestNode imscpManifestNode) throws Exception {
//   	   String result = "";
//   	   String nodeName = node.getLocalName();
//   	   try {
// 
//   		   Iterator it = children.iterator();
//           while (it.hasNext()) {
//        	   IMSLDNode child = (IMSLDNode) it.next();
//        	   if (child instanceof IMSLDRolePartNode) {
//        		   IMSLDNode role = ((IMSLDRolePartNode)child).getRole();
//        		   if (role instanceof IMSLDRoleNode) {
//        			   String ref = role.getNamedAttribute("identifier");
//        			   String roleTitle = imscpManifestNode.getLDRoleTitle(ref);
//        			   if (!roleTitle.equals("")) {
//        				   result=roleTitle;
//        			   }
//        		   }
//        	   } //if
//           } //while
//   	   	   
//   	   } /*try*/ catch (Exception e) {
//   		   
//   	   }
//   	   
//   	   return result;
//    }
      
      
    /**
     * Gets the title of the role with the identifier passed by parameter, looking
     * for the node with that role identifier inside the current IMSLDNode, calling
     * this function recursively for every children of the parent node.
     * 
     * @param ref String the identifier of the role
     * @return String the title of the role
     * 
     * @author Susana Carretero Galiano
     * @author Vanesa Mart�n de Cima
     */
//    public String getLDRoleTitle(String ref) throws Exception {
//    	String result = "";
//    	String nodeName = node.getLocalName();
//    	try {
//    		   
//            if ((nodeName.equals("manifest")) || (nodeName.equals("organizations")) || (nodeName.equals("learning-design")) 
//            		|| (nodeName.equals("components")) || (nodeName.equals("roles"))  || (nodeName.equals("learner"))){
//            	
//            	if ((nodeName.equals("learner"))) {
// 	   				if (getNamedAttribute("identifier").equals(ref)) {
// 	   					result = getTitle();
// 	   				} else {
// 	   					Iterator it = children.iterator();
// 		   				while (it.hasNext()) {
// 		 	 	   	   		IMSLDNode child = (IMSLDNode) it.next();
// 		 	 	   	   	  	result = result+child.getLDRoleTitle(ref);
// 		 	 	   	   	}
// 	   				}
// 	   			} else {
// 	   				Iterator it = children.iterator();
// 	   				while (it.hasNext()) {
// 	 	 	   	   		IMSLDNode child = (IMSLDNode) it.next();
// 	 	 	   	   	  	result = result+child.getLDRoleTitle(ref);
// 	 	 	   	   	}
// 	   			}
//            } else {
//            	//other nodes not needed
// 	   		}
//    	   	   
//    	} /*try*/ catch (Exception e) {
//    		   
//    	}
//    	   
//    	return result;
//    }
    
    /**
     * Gets the code for printing a learning activity into the students' 
     * complete activity tree, extracting the method node from the current IMSLDNode
     * and recursively its needed children (the ones that contain the learning activities,
     * until we find the learning activity with the ref passed by parameter).
     * 
     * @param parentName String representing the name for the parent node of the current node
     * 		that is being analyzed
     * @param childName String representing the name for the node that is being analyzed
     * @param ref String that is the reference of the learning activity we are looking for
     * @return String the code that prints the learning activity
     * 
     * @author Susana Carretero Galiano
     * @author Vanesa Mart�n de Cima
     */
//    public String getLearningActivities(String parentName, String childName, String ref) throws Exception {
//   	   String result = "";
//   	   String nodeName = node.getLocalName();
//   	   try {
//   		   
//           if ((nodeName.equals("manifest")) || (nodeName.equals("organizations")) || (nodeName.equals("learning-design")) 
//        		   || (nodeName.equals("components")) || (nodeName.equals("activities")) || (nodeName.equals("learning-activity"))){
//        	   
//        	   if ((nodeName.equals("learning-activity"))) {
//        		   if (getNamedAttribute("identifier").equals(ref)) {
//        			   if (getNamedAttribute("isvisible").equals("true")) {
//        				   result = "var "+childName+" = new WebFXTreeItem('"+getTitle()+"');"+childName+".icon = webFXTreeConfig.learnact;"+childName+".openIcon = webFXTreeConfig.learnactopen;"+parentName+".add("+childName+");";
//        			   } //if
//        		   }//if
//        	   } else {
//        		   Iterator it = children.iterator();
//        		   int i = 1;
//        		   while (it.hasNext()) {
//        			   IMSLDNode child = (IMSLDNode) it.next();
//        			   result = result+child.getLearningActivities(parentName,childName,ref);
//        			   i++;
//        		   }
//        	   }
//           } else {
//        	   //nodes not needed
//   		   }
//   	   	   
//   	   } catch (Exception e) {
//   	   }
//   	   
//   	   return result;
//   	   
//    }
      
      
    /**
     * Gets the code for printing a learning activity structure into the students' 
     * complete activity tree, extracting the method node from the current IMSLDNode
     * and recursively its needed children (the ones that contain the activity structure,
     * until we find the activity structure with the ref passed by parameter).
     * An activity structure is a set of learning activities, so inside the node activity
     * structure there are learning activity nodes, that must be shown in the activity tree
     * so, once we have found the activity structure, we call the function getLearningActivities
     * for every child node (from the activity structure node) that is a learning activity.
     * 
     * @param parentName String representing the name for the parent node of the current node
     * 		that is being analyzed
     * @param childName String representing the name for the node that is being analyzed
     * @param ref String that is the reference of the activity structure we are looking for
     * @param imscpManifestNode IMSCPManifestNode that contains the complete manifest
     * @return String the code that prints the activity structure
     * 
     * @author Susana Carretero Galiano
     * @author Vanesa Mart�n de Cima
     * @param imscpManifestNode 
     */
//    public String getActivityStructure(String parentName, String childName, String ref, IMSCPManifestNode imscpManifestNode) throws Exception {
//    	String result = "";
//    	String nodeName = node.getLocalName();
//    	try {
//    		   
//            if ((nodeName.equals("manifest")) || (nodeName.equals("organizations")) || (nodeName.equals("learning-design")) 
//    			   || (nodeName.equals("components")) || (nodeName.equals("activities")) || (nodeName.equals("activity-structure"))){
//            	
//            	if ((nodeName.equals("activity-structure"))) {
//            		if (getNamedAttribute("identifier").equals(ref)) {
//            			result = "var "+childName+" = new WebFXTreeItem('"+getTitle()+"');"+childName+".icon = webFXTreeConfig.actstruct;"+childName+".openIcon = webFXTreeConfig.actstructopen;"+parentName+".add("+childName+");";
//            			NodeList nodeChilds = node.getChildNodes();
//   	  		   			int length = nodeChilds.getLength();
//   	  		   			for (int i=0;i<length;i++) {
//   	  		   				Node child = nodeChilds.item(i);
//   	  		   				if (child instanceof Element) {
//   	  		   					String childNodeName = child.getLocalName();
//   	  		   					if ((childNodeName.equals("learning-activity-ref")) ){
//   	  		   						String learningActivityRef = getNamedAttribute((Element) child,"ref");
//   	  		   						result = result + imscpManifestNode.getLearningActivities (childName,childName+i,learningActivityRef);
//   	  		   					} //if
//   	  		   				}//if
//   	  		   			} //for
//   	  		   		}//if
//            	} else {
//            		Iterator it = children.iterator();
//   		 		  	int i = 1;
//   	 	   	   	  	while (it.hasNext()) {
//   	 	   	   	  		IMSLDNode child = (IMSLDNode) it.next();
//   	 	   	   	  		result = result+child.getActivityStructure(parentName,childName,ref,imscpManifestNode);
//   	 	   	   	  		i++;
//   	 	   	   	  	}
//            	}
//            	
//            } else {
//    		}
//     	   	   
//    	} catch (Exception e) {
//    	}
//    	
//    	return result;
//    }
 
//	public GenericTree<Activity> getLFActivityTree(IMSCPManifestNode imscpManifestNode) {
//		// Create the object to return
//		GenericTree<Activity> activityTree = new GenericTree<Activity>();
//
//		// We extract this node's information as LF's activities
//		GenericTreeNode<Activity> rootNode = imscpManifestNode.getActivityAsTreeNode();
//		activityTree.setRoot(rootNode);
//		return activityTree;
//	}

	
	protected Activity getActivityAsTreeNode(IMSCPManifestNode imscpManifestNode, String unpackedPath) {
		String nodeName = node.getLocalName();
		Activity activity = new Activity();

		// See if this element is one of the ones that we want to analyze
		if ((nodeName.equals("method")) || (nodeName.equals("play"))
				|| (nodeName.equals("act")) || (nodeName.equals("role-part"))
				|| (nodeName.equals("title"))) {

			if (nodeName.equals("method")){
				activity.setName("Root/Method");
				activity.setId("0");
				activity.setChildrenSequenceMode(Activity.PARALLEL);
				activity.setMode(Activity.CLASS);
				
				// Iterate through the play's children
				Iterator it = children.iterator();
				
				while (it.hasNext()) {
					IMSLDNode child = (IMSLDNode) it.next();
					
					Activity childActivity = child.getActivityAsTreeNode(imscpManifestNode, unpackedPath);
					if (childActivity!=null) activity.addChild(childActivity);
				}

				return activity;

			} else if (nodeName.equals("play")){
				activity.setName(getTitle());
				activity.setId(getIdentifier());
				
				// Specially, acts are sequential, so the play sequence-mode is sequential
				activity.setChildrenSequenceMode(Activity.SEQUENCE);
				// Normally, plays are performed by all the class
				activity.setMode(Activity.CLASS);
				
				// Iterate through the play's children
				Iterator it = children.iterator();
				
				while (it.hasNext()) {
					IMSLDNode child = (IMSLDNode) it.next();
					
					Activity childActivity = child.getActivityAsTreeNode(imscpManifestNode, unpackedPath);
					if (childActivity!=null) activity.addChild(childActivity);
				}

				return activity;	
			} else if (nodeName.equals("act")) {

					activity.setName(getTitle());
					activity.setId(getIdentifier());
					
					//role-parts within an act are run concurrently
					activity.setChildrenSequenceMode(Activity.PARALLEL);
					// Normally, acts are performed by all the class
					activity.setMode(Activity.CLASS);
					
					// Iterate through the play's children
					Iterator it = children.iterator();
					
					while (it.hasNext()) {
						IMSLDNode child = (IMSLDNode) it.next();
						
						Activity childActivity = child.getActivityAsTreeNode(imscpManifestNode, unpackedPath);
						if (childActivity!=null) activity.addChild(childActivity);
					}

					return activity;
			} else if (nodeName.equals("role-part")) {
				// the nodes that are role-part ones, need a special treatment
				// for now, we ignore role-part title, and do not translate it
				// as an additional level of activities, since it only relates
				// one activity (or structure) with one role or environment
				// TODO Still not sure about how to implement the association of
				// role with environment

				IMSLDRolePartNode rolepartnode = (IMSLDRolePartNode) this;
				
				//First, we find out the relevant role
				String roleref = rolepartnode.getRoleRef();

				NodeList nodeChilds = node.getChildNodes();
				
				int length = nodeChilds.getLength();
				for (int i = 0; i < length; i++) {
					Node child = nodeChilds.item(i);
					if (child instanceof Element) {
						String childNodeName = child.getLocalName();

						// a role-part node can contain learning-activity-ref
						// nodes or
						// activity-structure-ref nodes (a set of learning
						// activities,
						// so they are composed of learning-activity-ref nodes),
						// that
						// are the ones which have the students' activities
						// (learning
						// activities) so we analyze the role-part childs that
						// have this
						// kind of nodes extract the ref for the learning
						// activities and
						// look for them in the manifest structure
						if ((childNodeName.equals("learning-activity-ref"))	|| (childNodeName.equals("activity-structure-ref")) || (childNodeName.equals("support-activity-ref"))) {
							String ref = getNamedAttribute((Element) child,	"ref");
							if (childNodeName.equals("learning-activity-ref") || childNodeName.equals("support-activity-ref")) {
							
								Activity activityNode = getActivityAsTreeNode(ref, imscpManifestNode, unpackedPath);

								if(activityNode!=null){
									activityNode.setRoleIds(new ArrayList<String>(Arrays.asList(roleref)));
									return activityNode;
								}

							} else if (childNodeName.equals("activity-structure-ref")) {
								
								Activity activityStructureNode = getActivityStructureAsTreeNode(ref, imscpManifestNode, unpackedPath);
								if(activityStructureNode!=null){
									activityStructureNode.setRoleIds(new ArrayList<String>(Arrays.asList(roleref)));
									activityStructureNode.setChildrenRoleIds(new ArrayList<String>(Arrays.asList(roleref)));
									return activityStructureNode;
								}

							}
						} // if
					}// if
				} // for
				return null;

			
			} 

			return null;
			
		}

		return null;

	}

	
	private Activity getActivityStructureAsTreeNode(
			String ref, IMSCPManifestNode imscpManifestNode, String unpackedPath) {
		IMSLDActivityStructureNode structure = (IMSLDActivityStructureNode) imscpManifestNode.findActivityNodeByIdentifier(ref);
		
		if(structure!=null){
			
			return structure.getActivityStructureAsTreeNode(imscpManifestNode, unpackedPath);

		} else return null;
	}

	
	
//	protected ArrayList<String> convertChildrenIdsToArrayList(
//			ArrayList<GenericTreeNode<Activity>> childrenActivities) {
//		if (!childrenActivities.isEmpty()){
//			ArrayList<String> result = new ArrayList<String>();
//			Iterator it = childrenActivities.iterator();
//			while(it.hasNext()){
//				GenericTreeNode<Activity> child = (GenericTreeNode<Activity>) it.next();
//				result.add(child.getData().getId());
//			}
//			return result;
//		} else return null;
//	}

	protected Activity getActivityAsTreeNode(String ref,
			IMSCPManifestNode imscpManifestNode, String unpackedPath) {

		IMSLDLearningActivityNode ldactivity = (IMSLDLearningActivityNode) imscpManifestNode
				.findActivityNodeByIdentifier(ref);

		
		
		if (ldactivity != null) {

			Activity activityNode = new Activity();
			String description = null;
			ArrayList<String> resources = null;
			
			if (ldactivity.node.getLocalName().equalsIgnoreCase(
					"learning-activity")
					|| ldactivity.node.getLocalName().equalsIgnoreCase(
							"support-activity")) {

				for (Object child : ldactivity.children) {
					IMSLDNode childNode = (IMSLDNode) child;
					if (childNode.node.getLocalName().equalsIgnoreCase(
							"activity-description")) {
						IMSLDActivityDescriptionNode descrnode = (IMSLDActivityDescriptionNode) childNode;

						for (Iterator iter = descrnode.children.iterator(); iter
								.hasNext();) {
							
							//We look for an item with a txt file, and ignore the rest of elements (title, xmls...)
							//TODO This can be implemented better!
							Object obj=iter.next();

							if (obj instanceof IMSLDItemNode) {
								IMSLDItemNode subchild = (IMSLDItemNode) obj;
								
								String path = unpackedPath
								+ subchild.getResource().getURL();
								
								if(path.endsWith("txt")){
									File file = new File(path);
									try {
										String descrItem = FileUtils
												.readFileToString(file, "UTF-8");
										if (description == null)
											description = descrItem;
										else
											description = description + "\n"
													+ descrItem;
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										return null;
									}
								}
							} else if (obj instanceof IMSLDTitleNode){
								//We ignore the title (it normally is "activity description of XXXXX")
								IMSLDTitleNode subchild = (IMSLDTitleNode) obj;
								//For now, we do nothing
							}
							

						}

						
					}

				}

				for(Iterator it = ldactivity.getEnvironments().iterator();it.hasNext();){
					IMSLDEnvironmentNode env = (IMSLDEnvironmentNode) it.next();
					
					resources = env.getEnvironmentAsResourceIdsArray(resources);
					
				}
				
				// Extracting the parent is best done later, once the
				// activity tree is complete
				// The mode is quite difficult to extract from IMS-LD,
				// leave it empty for now

				activityNode = new Activity(ref, ldactivity.getTitle(),
						description, null, null, null, resources, null);
				return activityNode;

			} else
				return null;
		} else
			return null;
	}


	protected IMSLDNode findActivityNodeByIdentifier(String ref) {
		// TODO This recursive method is not very efficient, we could go though the parsed structure instead... implement that when we have time
	    IMSLDNode result = null;
	    if(this.getIdentifier().equals(ref)) return this;
	    else{
		    Iterator it = children.iterator();
		    while (it.hasNext() && result==null) {
		      IMSLDNode child = (IMSLDNode) it.next();
		      result = child.findActivityNodeByIdentifier(ref);
		    }
	    }
	    return result;

	}

	
}
