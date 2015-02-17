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

package org.coppercore.component;

import java.io.PrintWriter;
import java.util.Collection;

import org.coppercore.alfanet.Tracker;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.Parser;
import org.coppercore.events.EventDispatcher;
import org.coppercore.exceptions.PropertyException;
import org.w3c.dom.Element;

/**
 * This abstract class represents the collection of components that can be
 * completed in the IMS LD Completion is set to either true, false or unlimited.
 * The latter represents the situation that the component cannot be completed
 * but should be threated as if it were completed. The completed status may be
 * set and retrieved and personalization of the content deals with any feedback
 * that should be presented.
 * 
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.19 $, $Date: 2007/03/30 08:23:31 $
 */
public abstract class CompletionComponent extends LocalPersonalContent {
  /**
   * reresents the completed state.
   */
  public final static String COMPLETED = "true";

  /**
   * represents the not completed state.
   */ 
  public final static String NOTCOMPLETED = "false";

  /**
   * represents the unlimited state.
   */ 
  public final static String UNLIMITED = "unlimited";

  // NOTE: this property may not be initialized here because class
  // intitialization will overrule any
  // value set during unpacking of the XML blob. Use the onInit() method instead
  // for initializations that
  // are critical for the object creation
  /**
   * contains the completion state of the component.
   */
  protected String completed;

  /**
   * Constructor for creating this component. This will be overloaded by the
   * implementing classes.
   * 
   * @param uol
   *          Uol the Uol in which this component was declared
   * @param run
   *          Run the Run to which this component belongs
   * @param user
   *          User the User to which this component belongs
   * @param propId
   *          String the identifier of this component as defined in IMS LD
   * @throws PropertyException
   *           when the constructor fails
   */
  public CompletionComponent(Uol uol, Run run, User user, String propId) throws PropertyException {
    super(uol, run, user, propId);
  }

  /**
   * This method is called for each element encountered in the XML
   * <value></value> part, when loading properties from the database. This way
   * Properties may be instantiated with the corrected value. The mehtod will
   * return true if the children of this element should parsed as well. False is
   * returned otherwise.
   *
   * @param node Element the element encountered in the XML data stream
   * @param anUolId int the database id of the Uol for which the data are
   *   retrieved.
   * @throws PropertyException if the operation fails.
   * @return boolean true indicating that the children should be parsed as
   *   well. False is returned otherwise.
   */
  protected boolean processElement(Element node, int anUolId) throws PropertyException {
    if (!super.processElement(node, anUolId)) {
      String nodeName = node.getNodeName();

      if ("completed".equals(nodeName)) {
        completed = Parser.getTextValue(node);
        return true;
      }
    }
    return false;
  }

  /**
   * This method is called from the constructor when this object is initialized.
   * It provides an oppertunity for additional initialization. Default is does
   * nothing.
   */
  protected void onInit() {
    super.onInit();
    completed = NOTCOMPLETED;
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
    super.toXmlBody(result);
    result.write("<completed>" + getCompleted() + "</completed>");
  }

  private boolean performComplete() {
    boolean isChanged = false;
    if (completed.equals(NOTCOMPLETED)) {
      completed = COMPLETED;
      persist();
      isChanged = true;
    }
    return isChanged;
  }

  /**
   * Completes implementating component. This method should be called
   * if the completion is established via direct user interaction. Completion as
   * result of event processing should not be handled by this method.
   * 
   * @throws PropertyException
   *           when this operation fails
   */
  public void complete() throws PropertyException {
    if (performComplete()) {
      // Start of Alfanet Specific Software
      Tracker.trackEngine(uol, user, getIdentifier(), getPropertyDef().getDataType(), "complete", null);

      EventDispatcher.getEventDispatcher().postMessage(getUol(), getRun(), getUser(), this,
          EventDispatcher.COMPLETION_EVENT);
    }
  }

  /**
   * Completes implementating component as result of processing an
   * event in the chain of events.
   * 
   * @param firedActions
   *          Collection of actions that have already been performed in the same
   *          event chain
   * @throws PropertyException
   *           if this operation fails
   */
  public void complete(Collection firedActions) throws PropertyException {
    if (performComplete()) {
      // Start of Alfanet Specific Software
      Tracker.trackEngine(uol, user, getIdentifier(), getPropertyDef().getDataType(), "complete", null);

      EventDispatcher.getEventDispatcher().postMessage(getUol(), getRun(), getUser(), this,
          EventDispatcher.COMPLETION_EVENT, firedActions);
    }
  }

  /**
   * Returns the completed state of the implementating component, being
   * either true, false or unlimited.
   * 
   * @return String completed status of the implementation of this component
   */
  public String getCompleted() {
    return completed;
  }

  /**
   * Returns the completed state of the implementating component as boolean. 
   * @return boolean true if the state corresponds with "true" or "unlimited". Return false otherwise.
   */
  public boolean isCompleted() {
    return !NOTCOMPLETED.equals(completed);
  }

  /**
   * Recursively traverse this passed DOM node to seek any completed attributes.
   * If found, replace its value with the value in the dossier for the current
   * user. Nodes will be ordered according to the IMS LD rules and invisible
   * nodes and incomplete acts are filtered out.
   * 
   * @param anUol
   *          Uol the Uol which defined this activity tree
   * @param anRun
   *          Run the Run for which this activity tree is retrieved
   * @param anUser
   *          User for which this activity is personalized
   * @param element
   *          Element the node of the activity tree to be evaluated
   * @param webroot
   *          String the offset into URL where the content was stored
   * @return LocalPersonalContent representing the personalized version of this
   *         node
   */
  protected boolean personalizeElement(Uol anUol, Run anRun, User anUser, Element element, String webroot) {
    boolean result = super.personalizeElement(anUol, anRun, anUser, element, webroot);

    String nodeName = element.getNodeName();

    // feedback will only be shown when the component was completed
    if ((result) && ("feedback-description".equals(nodeName) && (getCompleted().equals(NOTCOMPLETED)))) {
      element.getParentNode().removeChild(element);
      result = false;
    }
    return result;
  }
}
