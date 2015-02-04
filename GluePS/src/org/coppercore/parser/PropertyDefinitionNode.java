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

import java.util.ArrayList;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;
import org.coppercore.common.MessageList;
import org.coppercore.component.BooleanPropertyDef;
import org.coppercore.component.DateTimePropertyDef;
import org.coppercore.component.DurationPropertyDef;
import org.coppercore.component.FilePropertyDef;
import org.coppercore.component.IntegerPropertyDef;
import org.coppercore.component.RealPropertyDef;
import org.coppercore.component.StringPropertyDef;
import org.coppercore.component.TextPropertyDef;
import org.coppercore.component.UriPropertyDef;
import org.coppercore.exceptions.CopperCoreException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class is the base class for all IMS learning Design elements that define properties.
 * <ul>
 * <li>loc-property</li>
 * <li>locpers-property</li>
 * <li>locrole-property</li>
 * <li>global-definition</li>
 * </ul>
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.7 $, $Date: 2005/01/21 14:01:11 $
 */
public abstract class PropertyDefinitionNode
    extends PropertyNode {

  private IMSLDDataTypeNode datatypeNode = null;
  private IMSLDInitialValueNode initialValueNode = null;
  private ArrayList restrictions = new ArrayList();
  private String roleId = null;

  /**
   * Constructs a PropertyDefinitionNode instance from the passed xml dom element.
   * 
   * @param aNode
   *          the xml dom node to parse
   * @param aParent
   *          the parsed IMS learning design element that is the parent element of this object
   */
  public PropertyDefinitionNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws
      Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("datatype")) {
      datatypeNode = (IMSLDDataTypeNode) addElement(new
          IMSLDDataTypeNode(childNode, this));
    }
    else if (nodeName.equals("initial-value")) {
      initialValueNode = (IMSLDInitialValueNode) addElement(new
          IMSLDInitialValueNode(childNode, this));
    }
    else if (nodeName.equals("restriction")) {

      IMSLDRestrictionNode res = (IMSLDRestrictionNode) addElement(new
          IMSLDRestrictionNode(childNode, this));
      restrictions.add(res.getData());
    }
  }

  private String getDatatype() {
    return datatypeNode.getDatatype();
  }

  protected boolean isValid(MessageList messages) {

    boolean result = super.isValid(messages);

    //validate the property definition
    result &= getPropertyDef().isValid(messages);

    return result;
  }

  protected void persist(int uolId) throws CopperCoreException {
    getPropertyDef().persist();

    //make sure all children are persisted as well
    super.persist(uolId);
  }


  protected boolean buildComponentModel(int uolId, MessageList messages) {
    boolean result = super.buildComponentModel(uolId, messages);

    //create explicit property components so they may be validated and persisted
    String type = getDatatype().toLowerCase();
    String initialValue = initialValueNode == null ? null :
        initialValueNode.getContent();

    try {
      if ("string".equals(type) || ("other".equals(type))) {
        setPropertyDef(new StringPropertyDef(uolId, getIdentifier(), getScope(),
                                             type, getHref(),
                                             initialValue,
                                             restrictions,
                                             getMetadataXml(),
                                             getTitle(), roleId
                                             ));
      }
      else if ("boolean".equals(type)) {
        setPropertyDef(new BooleanPropertyDef(uolId, getIdentifier(), getScope(),
                                              type, getHref(),
                                              initialValue,
                                              restrictions,
                                              getMetadataXml(),
                                              getTitle(),roleId
                                              ));
      }
      else if ("real".equals(type)) {
        setPropertyDef(new RealPropertyDef(uolId, getIdentifier(), getScope(),
                                           type, getHref(),
                                           initialValue,
                                           restrictions,
                                           getMetadataXml(),
                                           getTitle(), roleId
                                           ));
      }
      else if ("integer".equals(type)) {
        setPropertyDef(new IntegerPropertyDef(uolId, getIdentifier(), getScope(),
                                              type, getHref(),
                                              initialValue,
                                              restrictions,
                                              getMetadataXml(),
                                              getTitle(), roleId
                                              ));

      }
      else if ("text".equals(type)) {
        setPropertyDef(new TextPropertyDef(uolId, getIdentifier(), getScope(),
                                           type, getHref(),
                                           initialValue,
                                           restrictions,
                                           getMetadataXml(),
                                           getTitle(), roleId
                                           ));
      }
      else if ("duration".equals(type)) {
        setPropertyDef(new DurationPropertyDef(uolId, getIdentifier(), getScope(),
                                               type, getHref(),
                                               initialValue,
                                               restrictions,
                                               getMetadataXml(),
                                               getTitle(), roleId
                                               ));
      }
      else if ("datetime".equals(type)) {
        setPropertyDef(new DateTimePropertyDef(uolId, getIdentifier(), getScope(),
                                               type, getHref(),
                                               initialValue,
                                               restrictions,
                                               getMetadataXml(),
                                               getTitle(), roleId
                                               ));
      }
      else if ("uri".equals(type)) {
        setPropertyDef(new UriPropertyDef(uolId, getIdentifier(), getScope(),
                                          type, getHref(),
                                          initialValue,
                                          restrictions,
                                          getMetadataXml(),
                                          getTitle(), roleId
                                          ));
      }
      else if ("file".equals(type)) {
        if (initialValue == null) {
          setPropertyDef(new FilePropertyDef(uolId, getIdentifier(), getScope(),
                                             type, getHref(),
                                             restrictions,
                                             getMetadataXml(),
                                             getTitle(), roleId
                                             ));
        }
        else {
          //initial values don't make sense with file properties, so report error
          result = false;
          messages.logError("Initial value is not allowed in combination with a property of type file");
        }
      }
      else {
        Logger logger = Logger.getLogger(this.getClass());
        logger.error("Unknown property type \"" + type + "\" encountered.");
        throw new EJBException("Unknown property type \"" + type +
                               "\" encountered.");
      }
    }
    catch (Exception ex) {
      //something went wrong with type conversion so end of the line here
      result = false;
      messages.logError("Failed to create PropertyDefinition: " + ex.getMessage());
    }

    return result;
  }

  void setRoleId (String roleId) {
    this.roleId = roleId;
  }
}
