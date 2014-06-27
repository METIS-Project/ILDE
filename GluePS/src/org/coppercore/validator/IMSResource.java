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

package org.coppercore.validator;

import java.util.ArrayList;
import java.util.Iterator;

import org.coppercore.common.Parser;
import org.coppercore.common.URL;
import org.coppercore.exceptions.SemanticException;
import org.coppercore.exceptions.URLSyntaxException;
import org.coppercore.exceptions.ValidationException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * This class implements a ims content package resource element.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2005/01/11 13:15:02 $
 */
public class IMSResource extends IMSObject {

  private ArrayList files = new ArrayList();

  private ArrayList dependencies = new ArrayList();

  private URL baseURL = null;

  private URL fullURL = null;

  private String type = null;

  public IMSResource() {
    // default constructor
  }

  protected void initialize(Node aNode, IMSLDManifest aManifest) {

    super.initialize(aNode, aManifest);

    setKey("identifier");
    type = ((Element) node).getAttribute("type");

    //we have to determine what the base URL is, that should be used
    resolveBaseURL();

    Node currentNode = node.getFirstChild();

    // add all file references and dependencies
    while (currentNode != null) {

      //modified 9-11-2004: fixed name space issue
      if ("file".equals(currentNode.getLocalName()) && Parser.IMSCPNS.equals(currentNode.getNamespaceURI())) {

        files.add(new IMSFile(currentNode, manifest, baseURL));
      }
      else if (currentNode.getNodeName().equals("dependency")) {
        NamedNodeMap attributes = currentNode.getAttributes();
        Node identifierref = attributes.getNamedItem("identifierref");
        dependencies.add(identifierref.getNodeValue());
      }
      currentNode = currentNode.getNextSibling();
    }
  }

  public void resolveReferences() throws ValidationException {
    int size = dependencies.size();

    for (int i = 0; i < size; i++) {
      String idref = (String) dependencies.get(i);
      IMSObject foundObject = manifest.findIMSObject(idref);

      if (foundObject == null) {
        isValid = false;
        getLogger().logError("Could not find a resource named " + idref + "for a dependecy in resource " + this);
      }
      else if (!(foundObject instanceof IMSResource)) {
        isValid = false;
        getLogger()
            .logError("Dependency in resource " + this + " refers to object of wrong type (" + foundObject + ")");
        foundObject = null;
      }
      dependencies.set(i, foundObject);
    }

    if (!isValid) {
      throw new SemanticException("Invalid reference encountered");
    }
  }

  protected void isValid() throws SemanticException {
    //check URI for this resource
    validateURL();

    if (fullURL.isAbsolute()) {
      //no files or dependencies allowed
      if (!files.isEmpty() || !dependencies.isEmpty()) {

        throw new SemanticException("Resource(identifier = " + key
            + ") has a href attribute containing an absolute URL, but also contains files and/or dependencies."
            + position());
      }
    }
    else {
      //we have a relative URI, so we should validate files and dependencies
      //now we should validate all files
      Iterator myIterator = files.iterator();

      while (myIterator.hasNext()) {
        IMSFile currentFile = (IMSFile) myIterator.next();
        try {
          currentFile.isValid();
        }
        catch (SemanticException ex) {
          isValid = false;
          getLogger().logError(ex.getMessage());
        }
      }

      //check if we can solve depedencies
      if (isCircular(new ArrayList())) {
        throw new SemanticException("Circular reference for " + toString());
      }

      //finally, check if we can find a file referred by uri
      if (!hasFileWithURI(fullURL, isGlobalContent())) {
        throw new SemanticException("Expected file with href " + fullURL.toString() + " for " + toString());
      }

      if (!isValid) {
        throw new SemanticException("One or more resource validation errors occured");
      }
    }

  }

  private boolean isCircular(ArrayList originators) {

    if (originators.contains(this)) {
      return true;
    }

    // fork the list by cloning it
    ArrayList myList = (ArrayList) originators.clone();

    myList.add(this);

    Iterator myIterator = dependencies.iterator();

    while (myIterator.hasNext()) {
      IMSResource myResource = (IMSResource) myIterator.next();
      if (myResource != null) {

        if (myResource.isCircular(myList)) {
          return true;
        }
      }
    }
    return false;
  }

  private void validateURL() throws SemanticException {
    String href = getNamedAttribute("href");

    //check if our href attribute is valid
    if (href != null) {

      try {
        fullURL = new URL(baseURL, href);
      }
      catch (URLSyntaxException e) {

        throw new SemanticException("Invalid URL in attribute href for resource(" + key + "). Message was: " + e);
      }
    }
    else {
      throw new SemanticException("Missing href attribute for resource(identifier = " + key + "). " + position());
    }
  }

  /**
   * Returns the base url of this resource.
   * 
   * @return URL the base url of this resource
   */
  public URL getBaseURL() {
    return baseURL;
  }

  private boolean hasFileWithURI(URL referedFile, boolean hasGlobalContent) {
    Iterator fileIterator = files.iterator();

    while (fileIterator.hasNext()) {
      IMSFile currentFile = (IMSFile) fileIterator.next();

      URL fileURL = currentFile.getURL();

      //check if the URI of file equals URI of this resource
      if (referedFile.equals(fileURL)) {
        if (hasGlobalContent) {
          // mark file as having imsldcontent
          currentFile.setGlobalContent();
        }
        return true;
      }
    }

    //we haven't found the file in our file list. Check dependencies
    Iterator dependencyIterator = dependencies.iterator();

    while (dependencyIterator.hasNext()) {
      IMSResource currentResource = (IMSResource) dependencyIterator.next();

      if (currentResource != null) {
        if (currentResource.hasFileWithURI(referedFile, hasGlobalContent)) {
          return true;
        }
      }
    }

    //we haven't found any match file
    return false;
  }

  private void resolveBaseURL() {
    //read xml:base from resources element
    Node parent = node.getParentNode();
    NamedNodeMap parentAttr = parent.getAttributes();
    Node baseAttribute = parentAttr.getNamedItemNS(org.coppercore.common.Parser.XMLNS, "base");
    String base = getNamedAttribute("base", org.coppercore.common.Parser.XMLNS);

    try {
      if (baseAttribute != null) {
        baseURL = new URL(baseAttribute.getNodeValue());
      }
      if (base != null) {
        baseURL = new URL(baseURL, base);
      }
    }
    catch (URLSyntaxException ex) {
      isValid = false;
      baseURL = null;
      getLogger().logError("Failed to create base url with message: " + ex);
    }
  }

  /**
   * Returns true if this resource contains global content.
   * 
   * @return boolean true if this resource contains global content
   */
  protected boolean isGlobalContent() {
    return "imsldcontent".equals(type);
  }
}