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
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;
import org.coppercore.business.Event;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.condition.AllConditions;
import org.coppercore.dossier.PropertyDefDto;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.TypeCastException;
import org.w3c.dom.Element;

/**
 * Constructor for ExpressionPropertyDef. All expressions defined in a IMS LD
 * instance are processed by this component. Each Uol has only one instance of
 * this component at any time.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.26 $, $Date: 2006/10/11 15:28:46 $
 */
public class ExpressionPropertyDef extends PropertyDef {
  private static final long serialVersionUID = 42L;
  
  /**
   * contains the data type for this component.

   */
  public final static String DATATYPE = "expression";
  /**
   * defines the scope of this component.
   */
  public final static int SCOPE = PropertyDef.GLOBAL;

  // NOTE: this property may not be initialized here because class intitialization will overrule any
  // value set during unpacking of the XML blob. Use the onInit() method instead for initializations that
  // are critical for the object creation
  private AllConditions allConditions;
  private ArrayList expressionStack;

  
  /**
   * Default constructor for this component definition during run time.
   *
   * @param uolId int the database id of the Uol defining this component
   * @param propId String the identifier of this component as defined in IMS LD
   * @throws PropertyException whenever the constructor fails
   * @throws TypeCastException
   */  
  public ExpressionPropertyDef(int uolId, String propId) throws PropertyException, TypeCastException {

    super(uolId, propId);

    //build the all conditions and expressions
    allConditions.buildTriggers();
  }

  
  /**
   * Called by the constructor just before the unpacking of the
   * XML data. It provides an oppertunity to initialize any properties when
   * needed.
   */
  protected void onInit() {
    super.onInit();

    //reset the allConditions elemetn and the expression stack
    allConditions = null;
    expressionStack = new ArrayList();
  }

  /**
   * Default constructor for this component definition during publication.
   * 
   * @param uolId
   *          int the database id of the Uol defining this component
   * @param propId
   *          String the identifier of this component as defined in IMS LD
   * @param allConditions
   *          AllConditions the condtions (expressions and corresponding
   *          actions) that are processed by this component
   */
  public ExpressionPropertyDef(int uolId, String propId,
                               AllConditions allConditions) {
    this.uolId = uolId;
    this.propId = propId;
    this.allConditions = allConditions;

    //use dto for better performance
    dto = new PropertyDefDto(SCOPE, DATATYPE, null, toXml(),uolId);
  }
  
  
  /**
   * This method is called for each element encountered in the XML
   * <value></value> part, when loading properties from the database. This way
   * Properties may be instantiated with the corrected value. The method will
   * return true if the children of this element should parsed as well. False is
   * returned otherwise.
   *
   * @param node Element the element encountered in the XML data stream
   * @param anUolId int the database id of the Uol for which the data are
   *   retrieved
   * @throws PropertyException if the operation fails.
   * @return boolean true indicating that the children should be parsed as
   *   well. False is returned otherwise.
   */
  protected boolean processElement(Element node, int anUolId) throws
      PropertyException {
    if (node.getNodeName().equals("all-conditions")) {
      //we have detected the root element, so create is and put it on the stack
      allConditions = new AllConditions();

      //add element to the beginning of the stack
      expressionStack.add(0,allConditions);

      //recursively process children
      processElements(node, anUolId);

      //remove the last element from the stack
      expressionStack.remove(0);
    }
    else {
      ExpressionElement exp = ( (ExpressionElement) expressionStack.get(0)).
          addElement(node, anUolId);

      //add element to th end of the stack
      expressionStack.add(0,exp);

      //recursively process children
      processElements(node, anUolId);

      //remove last element from the stack
      expressionStack.remove(0);

    }
    return true;
  }

  /**
   * Adds the XML representation of the data part of this Property to a
   * PrintWriter. StaticProperties do not have any instance data and therefore
   * nothing is added to the stream.
   *
   * @param result PrintWriter
   */
  protected void toXml(PrintWriter result) {
    allConditions.toXml(result);
  }

  
  /**
   * Returns the XML blob representing the default value in XML format to be
   * used when initialising properties based on this PropertyDefintion. This
   * method is called when creating the PropertyDefinition itself. So this is
   * never called when creating an instance of this PropertyDefinition. Because
   * instances of this component are static, meaning that they are the same for
   * all users this method should never be called by instances of this
   * component. A call of this method will result in an error.
   *
   * @return String nothing because a call will result in an error
   */  
  protected String getDefaultBlobValue() {
    //should never be called as instances of this property should be static
    Logger logger = Logger.getLogger(getClass());
    logger.error("Invalid call, static properties are not instantiated");
    throw new EJBException("Invalid call, static properties are not instantiated");
  }

  /**
   * Evaluates all the expression contained in the instance of this component
   * using the passed parameters. This method should typically be called when a
   * user is enrolled into a run to make sure all tautologies etc. are evaluated
   * at least once.
   * 
   * @param uol
   *          Uol the Uol for which the expression needs to be evaluated
   * @param run
   *          Run the Run for which the expression needs to be evaluated
   * @param user
   *          User the User for which the expression needs to be evaluated
   * @throws PropertyException
   *           whenever this operation fails.
   */
  protected void evaluateAll(Uol uol, Run run, User user) throws PropertyException {
    allConditions.evaluateAll(uol,run,user);
  }

  /**
   * Processes the event that has been raised. 
   * 
   * @param uol
   *          Uol the Uol in which context the event was raised
   * @param run
   *          Run the Run in which context the event was raised
   * @param user
   *          User the User who caused the event to be raised
   * @param event
   *          Event the Event that was raised
   * @param sender
   *          StaticProperty the component which was the trigger for the Event
   * @param firedActions
   *          Collection the Collection of other Actions that already were
   *          fired. This collection is passed to avoid trigger loops.
   * @throws PropertyException
   *           whenever this operation fails.
   * 
   */  
  public void processEvent(Uol uol, Run run, User user, Event event, StaticProperty sender, Collection firedActions)  throws
      PropertyException {
    allConditions.processEvent(uol,run,user,event,sender, firedActions);
  }


}
