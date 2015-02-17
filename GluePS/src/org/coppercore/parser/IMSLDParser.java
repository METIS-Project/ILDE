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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.coppercore.common.MessageList;
import org.coppercore.common.ParserErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 * This class represents an IMS learning design parser.
 * <p>
 * This parser reads the IMS Learning Design content package and creates on the basis of this
 * learning design the component model. This model is stored in the database for use by the
 * CopperCore runtime environment.
 * <p>
 * The uri of the included unit of learning is (globally) unique identifier used to distinquish the
 * different learning designs in the database. If the unit of learning from the manifest already
 * exists in the database the parser overwrites it.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2005/01/21 14:01:10 $
 */
public class IMSLDParser {

  private MessageList logger = null;

  // private IMSLDManifest manifest = null;
  private IMSCPManifestNode cpManifest = null;

  private DocumentBuilder builder;

  private Document document;

  private byte[] manifest;

  private String webRoot = null;

  /**
   * Creates a new IMSLDParser for parsing the specified IMS Learning Design manifest.
   * 
   * @param aManifest
   *          the IMS Learning Desing manifest to parse
   * @param webRoot
   *          the file location where the resources are stored
   */
  public IMSLDParser(byte[] aManifest, String webRoot) {
    manifest = aManifest;
    logger = new MessageList(true);
    this.webRoot = webRoot;
  }

  /**
   * Creates a new IMSLDParser for parsing the specified IMS Learning Design manifest.
   * 
   * @param aManifest
   *          the IMS Learning Desing manifest to parse
   * @param webRoot
   *          the file location where the resources are stored
   * @param logger
   *          the list that collects all messages generated during the parsing
   */
  public IMSLDParser(byte[] aManifest, String webRoot, MessageList logger) {
    manifest = aManifest;
    this.logger = logger;
    this.webRoot = webRoot;
  }

  /**
   * Returns the logger containing all messages generated during parsing.
   * 
   * @return the logger containing all messages
   */
  public MessageList getLogger() {
    return logger;
  }

  /**
   * Processes the IMS learning design manifest.
   * <p>
   * The manifest is parsed, converted to a component model which then is stored in the database.
   * 
   * @return a boolean status if the processing was successfull
   * @throws Exception
   *           if there is an unhandled error during the parsing of the manifest
   */
  public boolean process() throws Exception {
    boolean result = false;

    logger.logInfo("Processing manifest started");

    Document myDocument = getDocument();

    if (myDocument != null) {

      NodeList nodes = myDocument.getElementsByTagNameNS(org.coppercore.common.Parser.IMSCPNS, "manifest");

      if (nodes != null && nodes.item(0) != null) {
        cpManifest = new IMSCPManifestNode(nodes.item(0));

        result = cpManifest.process(logger, webRoot);
      }
      else {
        logger.logError("Failed to find learning-design root");
      }

      if (result) {
        logger.logInfo("Processing manifest succeeded");
      }
      else {
        logger.logInfo("Processing manifest failed");
      }
    }
    return result;
  }

  /**
   * Validates the IMS learning design manifest.
   * 
   * @return a boolean status indicating whether the manifest is valid
   * @throws Exception
   *           id there is an unhandled exception during the validation of the manifest.
   */
  public boolean validate() throws Exception {
    boolean result = false;

    logger.logInfo("Semantic validation of manifest started");

    Document myDocument = getDocument();

    if (myDocument != null) {

      NodeList nodes = myDocument.getElementsByTagNameNS(org.coppercore.common.Parser.IMSCPNS, "manifest");

      if (nodes != null && nodes.item(0) != null) {
        cpManifest = new IMSCPManifestNode(nodes.item(0));

        result = cpManifest.validate(logger);
      }
      else {
        logger.logError("Failed to find learning-design root");
      }

      if (result) {
        logger.logInfo("Validation manifest succeeded");
      }
      else {
        logger.logInfo("Validation manifest failed");
      }
    }
    return result;
  }

  private boolean initializeParser() {
    boolean result = false;

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    factory.setValidating(false);
    factory.setNamespaceAware(true);

    try {
      builder = factory.newDocumentBuilder();

      builder.setErrorHandler(new ParserErrorHandler());
      result = true;
    }

    catch (IllegalArgumentException x) {
      // Happens if the parser does not support JAXP 1.2
      logger.logException(x);
    }

    catch (ParserConfigurationException pce) {
      // Parser with specified options can't be built
      logger.logException(pce);
    }

    return result;
  }

  private boolean createDomInstance(ByteArrayInputStream manifestXML) {
    document = null;

    try {
      // build a document of the basis of a ByteArrayInputStream, so any encoding issues will be
      // dealt with by the parser
      document = builder.parse(manifestXML);
    }
    catch (SAXParseException spe) {
      // Error generated by the parser
      logger.logError("Parsing error" + ", line " + spe.getLineNumber() + ", uri " + spe.getSystemId()
          + ". Message is: " + spe.getMessage());
    }
    catch (SAXException sxe) {
      // Error generated during parsing
      Exception x = sxe;
      if (sxe.getException() != null) {
        x = sxe.getException();
        logger.logException(x);
      }
    }
    catch (IOException ioe) {
      // I/O error
      logger.logException(ioe);
    }

    return (document != null);
  }

  /**
   * Returns an xml Document object for the manifest.
   * @return an xml Document object for the manifest
   */
  public Document getDocument() {
    if (document == null) {
      if (initializeParser()) {
        // create dom document
        createDomInstance(new ByteArrayInputStream(manifest));
      }
    }
    return document;
  }

  /**
   * Returns the local id of the unit of learning.
   * <p>
   * This is the id used in the CopperCore runtime environment to refer to this
   * unit of learning. It is called local because the ids are only unique per instance
   * of CopperCore. 
   * @return the local id of the unit of learning
   */
  public int getUolId() {
    return cpManifest.getUolId();
  }

}
