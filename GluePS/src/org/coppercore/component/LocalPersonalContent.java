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

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.coppercore.alfanet.Tracker;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.Parser;
import org.coppercore.common.URL;
import org.coppercore.events.EventDispatcher;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.URLSyntaxException;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Implementation of this class represent components that contain content that
 * requires personalization. Personalization involves personalized settings for
 * this visibility of this component itself and the visibility of the contained
 * items. Furthermore access to the content contained by the implementing
 * components is tracked and available for querying.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.24 $, $Date: 2007/03/30 08:23:30 $
 */
public abstract class LocalPersonalContent extends Property {

  // NOTE: this property may not be initialized here because class
  // intitialization will overrule any
  // value set during unpacking of the XML blob. Use the onInit() method instead
  // for initializations that
  // are critical for the object creation
  /**
   * contains the visibility state of the implementing components. Valid value
   * are "true" and "false".
   */
  protected String isVisible;

  /**
   * contains the date which this compoment became visible for the first time.
   */
  protected Calendar visibleSince;

  /**
   * contains the date which the content of this component was first accessed.
   */
  protected Calendar firstAccess;

  /**
   * contains the date which the content of this component was last accessed.
   */
  protected Calendar lastAccess;

  /**
   * contains the number of times the content of this component was accessed.
   */
  protected long accessCount;

  private HashMap items;

  /**
   * Default constructor during run time.
   *
   * @param uol int the unit of learning database id
   * @param run int the run database id
   * @param user String the user id
   * @param propId String the property id as defined in IMS LD
   * @throws PropertyException when something goes wrong with the constructor
   */
  public LocalPersonalContent(Uol uol, Run run, User user, String propId) throws PropertyException {
    super(uol, run, user, propId);
  }

  /**
   * This method is called from the constructor when this object is initialized.
   * It provides an oppertunity for additional initialization. Default is does
   * nothing.
   */
  protected void onInit() {
    super.onInit();
    isVisible = Boolean.toString(true);
    // default value for visibleSince lies in the past
    visibleSince = new GregorianCalendar(1970, 1, 1);
    firstAccess = null;
    lastAccess = null;
    accessCount = 0;
    items = new HashMap();
  }

  /**
   * This method is called for each element encountered in the XML <value>
   * </value> part, when loading properties from the database. This way
   * Properties may be instantiated with the corrected value. The mehtod will
   * return true if the children of this element should parsed as well. False is
   * returned otherwise.
   * 
   * @param node
   *          Element the element encountered in the XML data stream
   * @param anUolId
   *          int the database id of the Uol for which the data are retrieved.
   * @throws PropertyException
   *           if the operation fails.
   * @return boolean true indicating that the children should be parsed as well.
   *         False is returned otherwise.
   */
  protected boolean processElement(Element node, int anUolId) throws PropertyException {
    String nodeName = node.getNodeName();

    if ("isvisible".equals(nodeName)) {
      // retrieve the visibility and the timing for the current item
      visibleSince.setTimeInMillis(Long.valueOf(node.getAttribute("visible-since")).longValue());
      isVisible = Parser.getTextValue(node);

      return true;
    } else if ("first-access".equals(nodeName)) {
      long milliSecs = Long.parseLong(Parser.getTextValue(node));
      firstAccess = new GregorianCalendar();
      firstAccess.setTimeInMillis(milliSecs);
      return true;
    } else if ("last-access".equals(nodeName)) {
      long milliSecs = Long.parseLong(Parser.getTextValue(node));
      lastAccess = new GregorianCalendar();
      lastAccess.setTimeInMillis(milliSecs);
      return true;
    } else if ("access-count".equals(nodeName)) {
      accessCount = Long.parseLong(Parser.getTextValue(node));
      return true;
    } else if ("item".equals(nodeName)) {
      items.put(node.getAttribute("identifier"), node.getAttribute("isvisible"));
    }
    return false;
  }

  /**
   * Add specific content for this component to the passed stream in XML format.
   * Children of this component may override this method to add their own
   * specific data to the result.
   * 
   * @param result
   *          PrintWriter to which the result is written
   */
  protected void toXmlBody(PrintWriter result) {
    if (isVisible != null) {
      result.write("<isvisible visible-since=\"" + visibleSince.getTimeInMillis() + "\">" + isVisible()
          + "</isvisible>");
    }
    if (firstAccess != null) {
      result.write("<first-access>" + firstAccess.getTimeInMillis() + "</first-access>");
    }

    if (lastAccess != null) {
      result.write("<last-access>" + lastAccess.getTimeInMillis() + "</last-access>");
    }
    result.write("<access-count>" + accessCount + "</access-count>");

    // write the visibility of the items
    Iterator iter = items.keySet().iterator();
    while (iter.hasNext()) {
      String itemId = (String) iter.next();
      result.write("<item identifier=\"" + itemId + "\" isvisible=\"" + (String) items.get(itemId) + "\"/>");
    }
  }

  /**
   * Adds the XML representation of the data part of this Property to a
   * PrintWriter. StaticProperties do not have any instance data and therefore
   * nothing is added to the stream.
   * 
   * @param result
   *          PrintWriter
   */
  protected void toXml(PrintWriter result) {
    result.write("<value>");
    toXmlBody(result);
    result.write("</value>");
  }

  /**
   * Returns the personalized content of the implementing component as XML.
   * Retrieving the content will influence the access statistics of the
   * component.
   * 
   * @param anUol
   *          Uol context in which the request was done
   * @param aRun
   *          Run context in which the request was done
   * @param aUser
   *          User for which the content if retrieved
   * @param webroot
   *          String offset to the URL containing all the resources stored
   *          during publication stage
   * @return String the personalized content in XML format
   * @throws PropertyException
   *           whenvever the operation fails
   */
  public String getContent(Uol anUol, Run aRun, User aUser, String webroot) throws PropertyException {
    String result = null;

    // check if this object if visible or not.
    if ("true".equals(isVisible)) {
      // ok we are retrieving content, so we consider this object accessed for
      // the first time.
      lastAccess = new GregorianCalendar();
      boolean firstAccessed = false;

      if (firstAccess == null) {
        firstAccessed = true;
        firstAccess = lastAccess;
        // notify the dispatcher of this new event
      }
      accessCount++;

      persist();

      // notify the eventdispatcher about the start of the activity
      if (firstAccessed) {
        EventDispatcher.getEventDispatcher().postMessage(anUol, aRun, aUser, this, EventDispatcher.START_EVENT);
      }

      // get the document root node of the content
      Element root = ((LocalPersonalContentDef) getPropertyDef()).getContent();

      // mix personalised data (Properties) into the content
      personalize(anUol, aRun, aUser, root, webroot);

      // transform dom document to string containing the personalized XML
      result = Parser.documentToString(root);

      // Start of Alfanet Specific Software
      String params[][] = { { "cc-access-count", Long.toString(accessCount) } };
      Tracker.trackEngine(anUol, aUser, getIdentifier(), getPropertyDef().getDataType(), "get-content", params);

    }
    return result;
  }

  private void personalize(Uol anUol, Run anRun, User anUser, Node node, String webroot) {
    node = node.getFirstChild();
    while (node != null) {
      Node sibling = node.getNextSibling();
      // we are only interested in element nodes
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        if (personalizeElement(anUol, anRun, anUser, (Element) node, webroot)) {
          // node is not removed, so process it's children
          personalize(anUol, anRun, anUser, node, webroot);
        }
      }
      node = sibling;
    }
  }

  /**
   * Recursively traverse this passed DOM node to seek any completed attributes.
   * If found, replace its value with the value in the dossier for the current
   * user. Nodes will be ordered according to the IMS LD rules and invisible
   * nodes and incomplete acts are filtered out.
   * 
   * @param anUol
   *          Uol the Uol which defined this activity tree
   * @param aRun
   *          Run the Run for which this activity tree is retrieved
   * @param aUser
   *          User for which this activity is personalized
   * @param element
   *          Element the node of the activity tree to be evaluated
   * @param webroot
   *          String the offset into URL where the content was stored
   * @return LocalPersonalContent representing the personalized version of this
   *         node
   */
  protected boolean personalizeElement(Uol anUol, Run aRun, User aUser, Element element, String webroot) {
    String nodeName = element.getNodeName();

    //deal with the visibility of items and imsldcontent
    if ("imsldcontent".equals(nodeName) || "item".equals(nodeName)) {
      // check if this item is visible in the list of personalized item values
      String itemVisibility = (String) items.get(element.getAttribute("identifier"));
    	Attr isVisibleAttr = element.getAttributeNode("isvisible");
      String defaultVisibility = null;

      if (isVisibleAttr != null) {
      	defaultVisibility = isVisibleAttr.getValue();
      }
      
      //determine if we must show this item at all
      if ("false".equals(itemVisibility) || ((itemVisibility == null) && "false".equals(defaultVisibility))) {
        // this item should not be visible
        element.getParentNode().removeChild(element);
        return false;
      }
    
      //set the value of the visibility to the personalized value, if there was one 
      if ("true".equals(itemVisibility)) {
      	if (isVisibleAttr != null) {
      		isVisibleAttr.setValue("true");
      	}
      }
    }
    
    if ("item".equals(nodeName)) {
       Attr uri = element.getAttributeNode("url");
      // determine the fully qualified url on the basis of the passed web root

      // 2004-11-13: FIXED support for absolute URLs
      boolean absoluteUrl = false;

      //changed 2005-02-25 uri may now be null because item are not required to have a reference to a resource at all
      if (uri != null) {

        // check if we are dealing with an abosulute URL
        try {
          URL url = new URL(uri.getValue());
          if (url.isAbsolute()) {
            absoluteUrl = true;
          }
        } catch (URLSyntaxException ex) {
          // apearently not an absolute URL, so do nothing assuming a relative URI
        }
        
        if (!absoluteUrl) {
          // we must add the localcontext to the URI
          uri.setValue(webroot + uri.getValue());
        }        
      }
    }
    return true;
  }

  /**
   * Returns the visibilty of the implementing component as boolean.
   * @return boolean represention of the visibility of the implementing component
   */
  public boolean isVisible() {
    return "true".equals(isVisible);
  }

  /**
   * Returns the visibilty of the implementing component. Valid values are
   * "true" and "false".
   * 
   * @return Sting the visibility of the implementing component
   * @see #setVisibility(boolean)
   */
  public String getVisibility() {
    return isVisible;
  }

  /**
   * Sets the visibility of the component.
   * 
   * @param isVisible
   *          boolean true if the component is set to be visible, false
   *          otherwise
   * @see #getVisibility() 
   */
  public void setVisibility(boolean isVisible) {
    // check if something changes at all
    if (isVisible() != isVisible) {
      if (isVisible) {
        // appearently the component has become visible, so alter the visibility
        // date
        visibleSince = new GregorianCalendar();
      }
      this.isVisible = Boolean.toString(isVisible);
      persist();
    }
  }

  /**
   * Makes an item which is part of the components content items non visible and
   * there width in effect hidding the item from the content.
   * 
   * @param itemId
   *          the item identifier of the item which should be hidden
   */
  public void hideItem(String itemId) {
    setItemVisibility(itemId, "false");
  }

  /**
   * Makes an item which is part of the components content items visible and
   * there width in effect showing the item in the content.
   * 
   * @param itemId
   *          the item identifier of the item which should be hidden
   */
  public void showItem(String itemId) {
    setItemVisibility(itemId, "true");
  }

  private void setItemVisibility(String itemId, String isVisible) {
    if (items.containsKey(itemId)) {
      String visibility = (String) items.get(itemId);

      // check if something changes at all
      if (visibility != isVisible) {
        items.put(itemId, isVisible);
        persist();
      }
    } else {
      // item was not part of this component. This should not occur
      Logger logger = Logger.getLogger(this.getClass());
      logger.error("Attempting to change the visibility of an item[\"" + itemId
          + "\"] which is not part of this component[id=\"" + propId + "\"]");
      // minor situation, so don't throw an exception
    }
  }

  /**
   * Return the date the content of the component was first accessed.
   * 
   * @return Calendar the first access of the content of the component
   */
  public Calendar getFirstAccess() {
    return firstAccess;
  }

  /**
   * Return the date the component has become visible for the first time.
   * 
   * @return Calendar the date the component has become visible for the first
   *         time
   */
  public Calendar getVisibilitySince() {
    return visibleSince;
  }
}
