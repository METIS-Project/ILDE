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

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import org.apache.log4j.Logger;
import org.coppercore.business.Event;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.dossier.PropertyDto;
import org.coppercore.dossier.PropertyFacade;
import org.coppercore.exceptions.PropertyCreationException;
import org.coppercore.exceptions.PropertyException;

/**
 * Abstract class which is the root for all persistent business components and properties.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.33 $, $Date: 2007/03/30 08:23:31 $
 */
public abstract class Property extends StaticProperty {
  /**
   * The database id of the run for which this Property is defined.
   */
  protected int runId;
  /**
   * The owner id of the Property. Depending on the scope of this property this
   * is either the user id or the role id.
   */
  protected String ownerId = null;
  /**
   * The Run for which this Property is defined.
   */
  protected Run run = null;
  /**
   * The User for which this property is defined.
   */
  protected User user = null;
  /**
   * The data transfer object for this Property. Used when persisting or
   * retrieving this Property
   */
  protected PropertyDto dto = null;

  private PropertyFacade pf = null;

  /**
   * Constructor creating Property based on the passed parameters. If this is
   * the first time the Property is accessed it's value are set to default. The
   * property is not stored until the persist is called.
   *
   * @param uol int the unit of learning database id in which this property is used
   * @param run Run the run database id for this property
   * @param user User the user id of the owner of this property
   * @param propId String the property identifier as used in the IMS-LD instance
   * @throws PropertyException when the operation fails
   */
  public Property(Uol uol, Run run, User user, String propId) throws
      PropertyException {
    super(uol, propId);
    long start = System.currentTimeMillis();

    boolean isCreated = false;

    this.run = run;
    this.user = user;
    this.runId = run.getId();
    this.ownerId = getOwnerId();

    try {
      //first try to find find a persisted property
      pf = new PropertyFacade(uolId, runId, ownerId, propId);

    }
    catch (FinderException e) {
      //no Property was found, so create a new Property
      propertyDef = getPropertyDef();

      //create new property based on the PropertyDef
      try {
        pf = new PropertyFacade(runId, ownerId, propId,
                                propertyDef.getScope(),
                                propertyDef.getDefaultBlobValue(),
                                propertyDef.getDataType(),
                                propertyDef.getPropDefPK());
        isCreated = true;
      }
      catch (CreateException ex) {
        throw new PropertyCreationException(ex);
      }
    }
    //get the dto for future reference
    dto = pf.getDto();

    Logger logger = Logger.getLogger(this.getClass());
    logger.debug("property took: " + (System.currentTimeMillis() - start) + "ms.");

    //inform about initialization of this instance
    onInit();

    //create the data container for this definition
    unpack(uolId, dto.getPropValue());

    if (isCreated) {
      //inform property about creation allowing adaption when necessary
      onCreate();
    }
  }

  /**
   * Returns the owner id of this property.
   *
   * <p>In most cases this is the userid of the owner of the property. Only when
   * dealing with role properties, this is the roleinstace id of the role. We
   * assume that no role properties exists at this level. Only explicit
   * properties can be role properties!</p>
   *
   * @return String the owner of this property
   * @throws PropertyException when the operation fails
   */
  protected String getOwnerId() throws PropertyException {
    return (user != null) ? user.getId() : null;
  }

  /**
   * This method is called when a new instance was created in the database. It
   * provides an opertunity to perform additional actions. Default it does
   * nothing.
   */
  protected void onCreate() {
    //default do nothing
  }

  /**
   * This method is called from the constructor when this object is initialized.
   * It provides an oppertunity for additional initialization. Default is does
   * nothing.
   */
  protected void onInit() {
    //default do nothing
  }

  /**
   * Returns the run for which this Property was defined.
   * @return Run of this Property
   */
  protected Run getRun() {
    return run;
  }

  /**
   * Returns the User for which this Property was defined.
   *
   * @return User of this Property
   */
  protected User getUser() {
    return user;
  }

  /**
   * Persists this Property in the database.
   */
  public void persist() {
    pf.setValue(toXml());
  }

  /**
   * Processes event for which this property is the receiver. By default nothing
   * is done with the incomming event.
   *
   * @param raisingUol Uol the Uol raising the event
   * @param raisingRun Run the Run raising the event
   * @param raisingUser User User the User which raised the event
   * @param event Event that has be raised
   * @param sender StaticProperty the component raising the event
   * @param firedActions Collection a collection of events already raised as
   *   reaction to an initial triggering event. The collection prevents ending
   *   up in an endless of events. Each event is only processed once.
   * @throws PropertyException when an event could not be processed
   */
  public void processEvent(Uol raisingUol, Run raisingRun, User raisingUser, Event event,
                           StaticProperty sender, Collection firedActions) throws
      PropertyException {
    //default do nothing
  }

  /**
   * Returns the primary key of the property definition assiociated with this
   * property. This id is fetched from directly fetched from the DTO rather than
   * from the definition to avoid unnecessary access to the propertyDef.
   * 
   * @throws PropertyException
   *           whenver the operation fails
   * @return int the database id of the property defintion associated with this property.
   */
  public int getPropDefPK() throws PropertyException {
    return dto.getPropDefPK();
  }

  /**
   * Returns the scope of this property. This id is fetched from directly from
   * the DTO rather than from the definition, to avoid unnecessary access to the
   * propertyDef.
   *
   * @return int
   * @throws PropertyException
   */
  public int getScope() throws PropertyException {
    return dto.getScope();
  }

  /**
   * Returns a collection of users that are in the scope with this Property. In
   * effect this method will return all known users if the scope is global or
   * all users in the run if the scope is local or all users in role if the
   * scope is role or an empty collection if the scope is personal.
   *
   * @throws PropertyException whenever the method fails to return the
   *   requested users
   * @return Collection of users that are in the same scope
   */
  public Collection /* User */ getUsersInScope() throws PropertyException {
    ArrayList result = new ArrayList();

    PropertyDef def = getPropertyDef();
    if ( (def.getScope() & PropertyDef.ISPERSONAL) == PropertyDef.ISPERSONAL) {
      result.add(user);
    }
    else if ( (def.getScope() & PropertyDef.ISROLE) == PropertyDef.ISROLE) {
    	//FIXED 2006-12-05: get all users based on role instance id, so call new findByRoleInstanceId
    	//instead of findByRoleId
      result.addAll(User.findByRoleInstanceId(run.getId(), ownerId));
    }
    else {
      result.addAll(User.findByRunId(run));
    }

    return result;
  }

}
