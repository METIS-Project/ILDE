/*
 * CopperCore, an IMS-LD level C engine Copyright (C) 2003 Harrie Martens and
 * Hubert Vogten
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program (/license.txt); if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * 
 * Contact information: Open University of the Netherlands Valkenburgerweg 177
 * Heerlen PO Box 2960 6401 DL Heerlen e-mail: hubert.vogten@ou.nl or
 * harrie.martens@ou.nl
 * 
 * 
 * Open Universiteit Nederland, hereby disclaims all copyright interest in the
 * program CopperCore written by Harrie Martens and Hubert Vogten
 * 
 * prof.dr. Rob Koper, director of learning technologies research and
 * development
 *  
 */

package org.coppercore.parser;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.coppercore.common.Parser;
import org.coppercore.common.XMLTag;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design metadata element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.8 $, $Date: 2005/01/21 14:01:31 $
 */
public class IMSLDMetadataNode extends IMSLDNode {

  /**
   * Constructs a IMSLDMetadataNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDMetadataNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void writeXMLContent(PrintWriter output) {
    XMLTag tag = new XMLTag("metadata");
    tag.writeOpenTag(output);
    Node child = node.getFirstChild();

    while (child != null) {

      if (child.getNodeType() == Node.ELEMENT_NODE) {
        String nodeName = child.getLocalName();
        String nameSpace = child.getNamespaceURI();

        if (nameSpace.equals(Parser.IMSLDNS)) {

          XMLTag subTag = new XMLTag(nodeName);
          subTag.writeOpenTag(output);

          Node content = child.getFirstChild();

          while (content != null) {
            if (content.getNodeType() == Node.TEXT_NODE) {
              try {
                String[] schemas = {};
                DocumentBuilder db = Parser.getDocumentBuilder(true, true, schemas, null);
                Document doc = db.newDocument();

                doc.importNode(child, true);
                output.print(Parser.documentToString(doc));
              }
              catch (ParserConfigurationException e) {
                Logger logger = Logger.getLogger(this.getClass());
                logger.error(e.getMessage(), e);
              }
            }
            content = content.getNextSibling();
          }
          subTag.writeCloseTag(output);
        }
        else {
          //we must add the necessary name space declaration for this node
          checkNameSpaces(child, null);

          output.print(Parser.documentToString(child));

        }
      }
      child = child.getNextSibling();
    }
    tag.writeCloseTag(output);
  }

  /**
   * This method will recursively add name space declaration attributes to DOM
   * nodes for either declared prefixes or for the default name spaces. This
   * method allows the creation of valid fragments based on children of a root
   * DOM node. Be aware that this method has no implementation for name spaced
   * attributes yet.
   * 
   * @param fragment
   *          Node the DOM node under investigation
   * @param nameSpaces
   *          HashMap HashMap containing ArrayList representing stack of
   *          declared prefixes. For the initial call this parameter should be
   *          null
   */
  private void checkNameSpaces(Node fragment, HashMap nameSpaces) {
    final String DEFAULTNS = "_default";

    String prefixAdded = null;

    if (nameSpaces == null) {
      //initialize the hash map with name space if necessary
      nameSpaces = new HashMap();
    }

    if (fragment.getNodeType() == Node.ELEMENT_NODE) {
      String nsUri = fragment.getNamespaceURI();
      String prefix = fragment.getPrefix();

      // if both a prefix and namespace uri where declared, we should check if
      // we have to add
      // a namespace declaration at this place
      if ((nsUri != null) && (prefix != null)) {
        // check if we need to do something at all
        ArrayList stack = (ArrayList) nameSpaces.get(prefix);

        if ((stack == null) || (stack.size() == 0) || (stack != null && !nsUri.equals(stack.get(0)))) {
          //we have to add a declaration for this URI
          ((Element) fragment).setAttribute("xmlns:" + prefix, nsUri);

          //add this declaration to the stack of declarations
          if (stack == null) {
            stack = new ArrayList();
          }
          stack.add(0, nsUri);
          nameSpaces.put(prefix, stack);
          prefixAdded = prefix;
        }
      }
      else if ((prefix == null) && (nsUri != null)) {
        //we have detected a default name space
        // check if we need to do something at all
        ArrayList stack = (ArrayList) nameSpaces.get(DEFAULTNS);

        if ((stack == null) || (stack.size() == 0) || (stack != null && !nsUri.equals(stack.get(0)))) {
          //we have to add a default name space declaration for this URI
          ((Element) fragment).setAttribute("xmlns", nsUri);

          if (stack == null) {
            stack = new ArrayList();
          }
          stack.add(0, nsUri);
          nameSpaces.put(DEFAULTNS, stack);
          prefixAdded = DEFAULTNS;
        }
      }
    }

    //process all the children recursively
    Node child = fragment.getFirstChild();

    while (child != null) {
      //recursively check the child nodes
      checkNameSpaces(child, nameSpaces);

      //get the following sibling
      child = child.getNextSibling();
    }

    //check if there has been any change in our nameSpace stack. If so restore
    // old state
    //because we have left the scope of this declaration
    if (prefixAdded != null) {
      ArrayList stack = (ArrayList) nameSpaces.get(prefixAdded);
      stack.remove(0);
      nameSpaces.put(prefixAdded, stack);
    }
  }

}