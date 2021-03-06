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

import org.apache.log4j.Logger;
import org.coppercore.common.MessageList;
import org.xml.sax.SAXParseException;

/**
 * The ParserErroHandler class logs errors, warnings and fatal errors occured
 * during the parsing of an XML document. Errors are threaded as fatal and
 * re-throw the exeption.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.4 $, $Date: 2005/01/11 13:15:01 $
 */
public class ParserErrorHandler
    implements org.xml.sax.ErrorHandler {
  MessageList messageList = null;

  /**
   * Default constructor creating a ParserHandler capable of writing the
   * warnings to a MessageList.
   *
   * @param messageList MessageList
   */
  public ParserErrorHandler(MessageList messageList) {
    this.messageList = messageList;
  }

  /**
   * Logs the parser warning.
   *
   * @param e SAXParseException
   */
  public void warning(SAXParseException e) {
    messageList.logWarning("Warning: " + e.getMessage() +
                " at line " + e.getLineNumber() +
                ", column " + e.getColumnNumber() +
                " in entity " + e.getSystemId());
  }

  /**
   * Logs the parser error and re-throws the exception indicating that an error
   * is fatal.
   *
   * @param e SAXParseException
   * @throws SAXParseException
   */
  public void error(SAXParseException e) throws SAXParseException {
     // treat validation errors as fatal
    throw (e);
  }

  /**
   * Logs a fatal parser error.
   *
   * @param exception SAXParseException
   */
  public void fatalError(SAXParseException exception) {
    Logger logger = Logger.getLogger(this.getClass());
    logger.info("Fatal Error: " + exception.getMessage() +
                " at line " + exception.getLineNumber() +
                ", column " + exception.getColumnNumber() +
                " in entity " + exception.getSystemId());
  }
}
