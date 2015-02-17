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

package org.coppercore.validator;

import org.apache.log4j.Logger;
import org.coppercore.exceptions.IMSObjectClassNotFoundException;
import org.w3c.dom.Node;

/**
 * factory to create instances of an IMSObject subclass.<p>The actual subclass is defined by the name of
* the xml node that is passed to the factory createObject method.
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.13 $, $Date: 2008/11/18 16:52:38 $
 */
public class IMSObjectFactory {

  private static IMSObjectFactory factory = null;

  /** Array with IMS constructs that can be validated without any context. */
  protected static final String NODETABLE[][] = {
      {
      org.coppercore.common.Parser.IMSLDNS, "existing",
      "org.coppercore.validator.IMSExisting"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "global-definition",
      "org.coppercore.validator.IMSGlobalDefinition"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "environment-ref",
      "org.coppercore.validator.IMSEnvironmentRef"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "environment",
      "org.coppercore.validator.IMSEnvironment"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "item",
      "org.coppercore.validator.IMSItem"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "participant",
      "org.coppercore.validator.IMSConferenceRole"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "observer",
      "org.coppercore.validator.IMSConferenceRole"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "moderator",
      "org.coppercore.validator.IMSConferenceRole"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "conference-manager",
      "org.coppercore.validator.IMSConferenceRole"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "role-ref",
      "org.coppercore.validator.IMSRoleRef"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "activity-structure",
      "org.coppercore.validator.IMSActivityStructure"}
      //, {
      //org.coppercore.common.Parser.IMSLDNS, "activity-ref",
      //"org.coppercore.validator.IMSLearningActivityRef"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "support-activity-ref",
      "org.coppercore.validator.IMSSupportActivityRef"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "activity-structure-ref",
      "org.coppercore.validator.IMSActivityStructureRef"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "learning-activity-ref",
      "org.coppercore.validator.IMSLearningActivityRef"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "when-role-part-completed",
      "org.coppercore.validator.IMSWhenRolePartCompleted"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "when-play-completed",
      "org.coppercore.validator.IMSWhenPlayCompleted"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "time-limit",
      "org.coppercore.validator.IMSTimeLimit"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "property-group-ref",
      "org.coppercore.validator.IMSPropertyGroupRef"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "property-group",
      "org.coppercore.validator.IMSPropertyGroup"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "property-ref",
      "org.coppercore.validator.IMSPropertyRef"}
      , {
      org.coppercore.common.Parser.IMSCPNS, "resource",
      "org.coppercore.validator.IMSResource"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "is-member-of-role",
      "org.coppercore.validator.IMSIsMemberOfRole"}
      , {
      org.coppercore.common.Parser.IMSLDNS, "datetime-activity-started",
      "org.coppercore.validator.IMSDateTimeActivityStarted"}
      //{org.coppercore.common.Parser.IMSCPNS,"File","IMSFile"}
  };

  /**
   * Returns the singleton factory for creating IMSObjects.<p>Only one instance of the factory
   * is actually created.
   * @return IMSObjectFactory the factory for creating IMSObjects
   */
  protected static IMSObjectFactory getIMSElementFactory() {
    if (factory == null) {
      factory = new IMSObjectFactory();
    }

    return factory;
  }

  /**
   * Creates an IMSObject of the right subclass for the passed xml node.
   *
   * @param node Node the xml node to create a IMSObject subclass instance for
   * @param manifest IMSLDManifest the manifest the new instance belongs to,
   *   passed to the new instance
   * @throws IMSObjectClassNotFoundException when there is an error creating a
   *   new IMSObject subclass instance
   * @return IMSObject the created subclass for the passed xml node
   */
  protected IMSObject createObject(Node node, IMSLDManifest manifest) throws
  IMSObjectClassNotFoundException {
    Class myClass = null;
    IMSObject myObject = null;

    try {
      // find corresponding classname
      String myClassName = findClass(node.getNamespaceURI(),
                                     node.getLocalName());

      // create a new instance of the found classname
      myClass = Class.forName(myClassName);
      myObject = (IMSObject) myClass.newInstance();

      // initialize the IMSObject
      myObject.initialize(node, manifest);

      return myObject;
    }
    catch (IllegalAccessException ex) {
      Logger logger = Logger.getLogger(this.getClass());
      logger.error(ex);
      throw new IMSObjectClassNotFoundException(ex);
    }
    catch (InstantiationException ex) {
      Logger logger = Logger.getLogger(this.getClass());
      logger.error(ex);
      throw new IMSObjectClassNotFoundException(ex);
    }
    catch (ClassNotFoundException ex) {
      Logger logger = Logger.getLogger(this.getClass());
      logger.error(ex);
      throw new IMSObjectClassNotFoundException(ex);
    }
  }

  private String findClass(String aNS, String elementName) throws
      IMSObjectClassNotFoundException {
    int size = NODETABLE.length;
    int count = 0;

    if (aNS == null) {
      //there is no prefix
      while (count < size) {
        if (NODETABLE[count][1].equals(elementName)) {
          return NODETABLE[count][2];
        }
        count++;
      }
    }
    else {
      //there is a prefix
      while (count < size) {
        if ( (NODETABLE[count][0].equals(aNS)) &&
            (NODETABLE[count][1].equals(elementName))) {
          return NODETABLE[count][2];
        }
        count++;
      }
    }

    //we didn't find a corresponding file
    throw new IMSObjectClassNotFoundException("Class for " + elementName +
                                              " is not known");

  }
}
