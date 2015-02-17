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

package org.coppercore.common;

import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;

/**
 * The ParserErroHandler class logs errors, warnings and fatal errors occured
 * during the parsing of an XML document. Errors are threaded as fatal and
 * re-throw the exeption.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.5 $, $Date: 2005/01/11 13:15:10 $
 */
public class ParserErrorHandler
    implements org.xml.sax.ErrorHandler {
  /**
   * Logs the parser warning.
   *
   * @param e SAXParseException
   */
  public void warning(SAXParseException e) {
    Logger logger = Logger.getLogger(this.getClass());
    logger.info("Warning: " + e.getMessage() +
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
    Logger logger = Logger.getLogger(this.getClass());
    logger.info("Error: " + e.getMessage() +
                " at line " + e.getLineNumber() +
                ", column " + e.getColumnNumber() +
                " in entity " + e.getSystemId());

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
