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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import javax.ejb.EJBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.coppercore.common.Parser;
import org.coppercore.common.ParserErrorHandler;
import org.coppercore.exceptions.ComponentDataException;
import org.coppercore.exceptions.PropertyException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * The class PropertyData is the root class for all property data containers. An
 * instance will convert the XML data representations of both Properties and
 * PropertiesDefinitions to native Java objects and visa versa. The actual
 * marshalling of the elements is left to the implementing classes.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.23 $, $Date: 2005/02/25 14:15:08 $
 */
public abstract class PropertyData
    implements Serializable {

  private static DocumentBuilder builder = null;

  /**
   * Unpacks the passed xmlBlob, meaning that the data is parsed and the appropriate object is
   * constructed from it.
   *
   * @param uolId int the unit of learning id the XML belongs to
   * @param xmlBlob String the XML instance to be parsed
   * @throws PropertyException thrown when an invalid data type was encountered
   */
  protected void unpack(int uolId, String xmlBlob) throws
      PropertyException {

    try {

      Node root = null;
      
      //changed 2004-02-24: synchronized the parser in order to 
      //avoid "parse may not be called while parsing" error that occasionally occured
      synchronized("unpack_property") {
        //note that we use the inputSourceFromString to avoid problems with Xerces and UTF-8 BOMs
        root = getBuilder().parse(Parser.inputSourceFromString(xmlBlob));
      }  

      //now starting parsing the XML blob
      this.processElements(root, uolId);

    }
    catch (IOException ex) {
      //fatal we can not recover
      Logger logger = Logger.getLogger(this.getClass());
      logger.error(ex);
      throw new EJBException(ex);
    }

    catch (SAXException ex) {
      //a parsing error occured
      //fatal we can not recover
      Logger logger = Logger.getLogger(this.getClass());
      logger.error(ex.getMessage(), ex);
      throw new ComponentDataException(ex);
    }
  }

  /**
   * This method is called during the construction of the class from a stored
   * XML representation for every XML element encountered. This method is
   * abstract and should be implemented by all the sub classes.
   *
   * @param node Node the dom node in the XML instance being currently processed
   * @param uolId int the unit of learning id of the UOL this document belongs
   *   to
   * @throws PropertyException
   * @return boolean
   */
  protected abstract boolean processElement(Element node, int uolId) throws
      PropertyException;

  /**
   * Processes the XML document which root was passed as parameter by calling
   * processElement for each element encountered.
   *
   * @param node Node the root node of the document to process
   * @param uolId int the unit of learning id to which this node belongs
   * @throws PropertyException
   */
  protected void processElements(Node node, int uolId) throws
      PropertyException {
    Node child = node.getFirstChild();

    while (child != null) {

      if ( (child.getNodeType() == Node.ELEMENT_NODE) &&
          (!processElement((Element) child, uolId))) {
        //if the child node was not processed already, process it now.
        processElements(child, uolId);

      }
      child = child.getNextSibling();
    }
  }

  /**
   * Writes the XML representation of this object to the passed PrintWriter.
   *
   * @param result PrintWriter to which the XML representation will be added.
   */
  protected abstract void toXml(PrintWriter result);

  /**
   * Returns the XML representation of the data encapsulated by this instance as
   * String.
   *
   * @return String representing the XML format of the encapsulated data.
   */
  public String toXml() {
    StringWriter outputStream = new StringWriter();
    try {
      PrintWriter output = new PrintWriter(outputStream);
      try {
        output.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        toXml(output);
        return outputStream.toString();
      }
      catch (Exception ex) {
        Logger logger = Logger.getLogger(this.getClass());
        logger.error(ex);
        throw new EJBException(ex);
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
        Logger logger = Logger.getLogger(this.getClass());
        logger.error(ex);
        throw new EJBException(ex);
      }
    }
  }

  /**
   * Returns a non-validating DOM document builder.
   *
   * @return DocumentBuilder a none validating document builder instance.
   */
  protected DocumentBuilder getBuilder() {
    /** @todo check for rentrency problems when reusing the parser **/
    if (builder == null) {
      try {
        builder = Parser.getDocumentBuilder(new ParserErrorHandler());
      }
      catch (ParserConfigurationException ex) {
        Logger logger = Logger.getLogger(this.getClass());
        logger.error(ex);
        throw new EJBException(ex);
      }
    }
    return builder;
  }
}
