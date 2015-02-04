/*
 * CopperCore, an IMS-LD level C engine
 * Copyright (C) 2003, 2004 Harrie Martens and Hubert Vogten
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

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.Parser;
import org.coppercore.datatypes.LDDataType;
import org.coppercore.exceptions.PropertyException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

/**
 * This component represents the global IMS LD content. This type of content is
 * already preparsed during parsing stage and stored with all other resources at
 * the web root offset. Processing this component involves the personalization
 * of this web content by replacing references to explicit properties by their
 * actual value. This component deviates from all other components by the fact
 * that although the nature of the component is similar to a static property,
 * data storage is not done by this component. This component does not inherit
 * from StaticProperty! Furthermore this component does not support the use of
 * absolute global content. This implies that resource of type IMSLD global
 * content must be provided in the package.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.21 $, $Date: 2005/01/19 16:31:28 $
 */
public class GlobalContent {
  private Uol uol = null;

  private Run run = null;

  private User user = null;

  private String urlString = null;

  /**
   * Default constructor for this component.
   * 
   * @param uol
   *          int the unit of learning database id
   * @param run
   *          int the run database id
   * @param user
   *          String the user id
   * @param urlString
   *          String the relative uri to the resource as specified in the IMSLD
   *          package
   */
  public GlobalContent(Uol uol, Run run, User user, String urlString) {
    this.uol = uol;
    this.run = run;
    this.user = user;

    this.urlString = urlString;
  }

  /**
   * Returns the personalized global content for this GlobalContent component as XML String.
   * @param webroot String the relative URI of the IMSLD global content resource. Note that absolute URI's are not supported!
   * @return String the personalized global content for this component.
   */
  public String getContent(String webroot) {
    return getContent(webroot, null);
  }

  /**
   * Returns the personalized global content for this GlobalContent component as
   * XML String taking account for personalization of the properties of the
   * requesting user as well as the properties of the supported user.
   * 
   * @param webroot
   *          String the relative URI of the IMSLD global content resource. Note
   *          that absolute URI's are not supported!
   * @param supportedUser
   *          String the user id of the supported user. Use null to indicate
   *          that there is no user to support.
   * @return String the personalized global content for this component.
   */
  public String getContent(String webroot, User supportedUser) {
    try {
      java.net.URL url = new java.net.URL(webroot + urlString);
      InputStream content = (InputStream) url.getContent();

      Document document = Parser.getDocumentBuilder(false, true, null, null).parse(new InputSource(content));

      // document contains global elements so add our namespace to it
      document.getDocumentElement().setAttribute("xmlns:cc", Parser.CCNS);

      // fetch the class settings for this user
      ClassProperty classes = ComponentFactory.getPropertyFactory().getClasses(uol, run, user, ClassPropertyDef.ID);

      // mix personalised data into the content
      personalizeContent(document, document, supportedUser, classes);

      // transform dom document to string containing XML
      String result = Parser.documentToString(document);

      return result;
    } catch (Exception ex) {
      Logger logger = Logger.getLogger(this.getClass());
      logger.error(ex);
      throw new EJBException(ex);
    }
  }

  private void personalizeContent(Document document, Node root, User supportedPerson, ClassProperty classes) {

    Node child = root.getFirstChild();

    while (child != null) {
      if (child.getNodeType() == Node.ELEMENT_NODE) {
        // personalize the values for the referenced properties
        personalizeProperty(document, (Element) child, supportedPerson);

        // add the class attributes to enable class visiblity via show/hide
        personalizeClass((Element) child, classes);

        // recursively process each element node
        personalizeContent(document, child, supportedPerson, classes);

      }
      child = child.getNextSibling();
    }
  }

  private void personalizeClass(Element element, ClassProperty classes) {
    if (element.hasAttribute("class")) {
      String[] definedClasses = element.getAttribute("class").split(" ");

      for (int i = 0; i < definedClasses.length; i++) {
        if (classes.hasClass(definedClasses[i])) {
          String title = classes.getTitle(definedClasses[i]);
          if (title != null) {
            element.setAttribute("cc:title", title);
          }

          String withControle = classes.getWithControl(definedClasses[i]);
          if (withControle != null) {
            element.setAttribute("cc:with-control", withControle);
          }

          String isVisible = classes.getIsVisible(definedClasses[i]);
          if (isVisible != null) {
            element.setAttribute("cc:isvisible", isVisible);
          }

          // we stop after we have found the first ld referenced class.
          // although this is an arbitrary choice, it seems to be most logical.
          break;
        }
      }
    }
  }

  private void personalizeProperty(Document document, Element element, User supportedPerson) {
    if (Parser.CCNS.equals(element.getNamespaceURI())
        && ("view-property".equals(element.getLocalName()) || "set-property".equals(element.getLocalName()))) {

      try {
        String ref = element.getAttribute("ref");
        if (!ref.equals("")) {
          element.setAttribute("id", ref);

          ExplicitPropertyDef propDef;
          Element value = document.createElement("cc:value");
          boolean propertyOfSupportedPerson = element.getAttribute("property-of").equals("supported-person");
          if (propertyOfSupportedPerson && (supportedPerson == null)) {
            propDef = getPropertyDef(ref);
            value.setAttribute("data", "no-supported-person");
          } else {
            ExplicitProperty property = getProperty(ref, supportedPerson, propertyOfSupportedPerson);
            propDef = (ExplicitPropertyDef) property.getPropertyDef();
            LDDataType data = property.getValue();
            if (data == null) {
              value.setAttribute("data", "is-null");
            } else {
              value.appendChild(data.asDomNode(document));
            }
            // add info about this property which may be used by the client e.g.
            // for determining
            // an unique filename for storing file properties

            // the uol-id DB id, refering to defining uol of this property. This
            // is not necessarely the same
            // as the current uol id. This is especially the case for global
            // properties
            element.setAttribute("uol-id", Integer.toString(propDef.getPropDefPK()));

            // add the owner id which is either the current user of the
            // supported person
            element.setAttribute("owner-id", propertyOfSupportedPerson ? supportedPerson.getId() : user.getId());

            // add the run DB id if we are dealing with a local property.
            if ((propDef.getScope() & PropertyDef.ISLOCAL) == PropertyDef.ISLOCAL) {
              // we are dealing with a local property, so add run info,
              // otherwise omitted
              element.setAttribute("run-id", Integer.toString(run.getId()));
            }
          }

          element.appendChild(value);
          element.setAttribute("datatype", propDef.getDataType());
          Element newElement = null;
          String attr = element.getAttribute("view");
          if (attr.equals("title-value")) {
            String title = propDef.getTitle();
            if (title != null && !title.equals("")) {
              newElement = document.createElement("cc:title");
              Text text = null;
              text = document.createTextNode(title);
              newElement.appendChild(text);
              element.appendChild(newElement);
            }
          }
          Collection restrictions = null;
          restrictions = propDef.getRestrictions();
          if (restrictions != null) {
            Iterator iter = restrictions.iterator();
            while (iter.hasNext()) {
              String[] tuple = (String[]) iter.next();
              newElement = document.createElement("cc:restriction");
              newElement.setAttribute("restriction-type", tuple[0]);
              Text text = document.createTextNode(tuple[1]);
              newElement.appendChild(text);
              element.appendChild(newElement);
            }
          }
        } else {
          // do nothing
        }
      } catch (PropertyException ex) {
        Logger logger = Logger.getLogger(this.getClass());
        logger.error(ex.getMessage());
      }
    }
  }

  private ExplicitProperty getProperty(String ref, User supportedPerson, boolean propertyOfSupportedPerson)
      throws PropertyException {
    ExplicitProperty property = null;
    if (propertyOfSupportedPerson) {
      property = ComponentFactory.getPropertyFactory().getProperty(uol, run, supportedPerson, ref);
    } else {
      property = ComponentFactory.getPropertyFactory().getProperty(uol, run, user, ref);
    }
    return property;
  }

  private ExplicitPropertyDef getPropertyDef(String ref) throws PropertyException {
    ExplicitPropertyDef propertyDef = null;
    propertyDef = ComponentFactory.getPropertyFactory().getExplicitPropertyDef(uol.getId(), ref);
    return propertyDef;
  }
}
