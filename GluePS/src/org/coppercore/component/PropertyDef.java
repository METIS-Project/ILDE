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

import java.io.Serializable;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.coppercore.dossier.PropertyDefDto;
import org.coppercore.dossier.PropertyDefFacade;
import org.coppercore.dossier.PropertyLookUpFacade;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.PropertyNotFoundException;

/**
 * This is the root class for all properties definitions both explicit or
 * implicit. The latter is also known as components. Property definitions
 * contain all information of a property or component that is common for all
 * instances.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.24 $, $Date: 2008/05/15 15:32:40 $
 */
public abstract class PropertyDef
    extends PropertyData
    implements Serializable {

  /**
   * defines the ISLOCAL bit indicating that this is a local scope.
   */
  public final static int ISLOCAL = 1;
  /**
   * defines the ISPERSONAL bit indicating that this is a personal scope.
   */
  public final static int ISPERSONAL = 2;
  /**
   * defines the ISROLE bit indicating that this is a role scope.
   */
  public final static int ISROLE = 4;

  /**
   * mask for checking global scope. 
   */
  public final static int GLOBAL = 0;
  /**
   * mask for checking local personal scope.
   */
  public final static int LOCALPERSONAL = 0 | ISLOCAL | ISPERSONAL;
  /**
   * mask for checking the global personal scope.
   */
  public final static int GLOBALPERSONAL = 0 | ISPERSONAL;
  /**
   * mask for checking the local scope.
   */
  public final static int LOCAL = 0 | ISLOCAL;
  /**
   * mask for checking local role scope.
   */
  public final static int LOCALROLE = 0 | ISLOCAL | ISROLE ;

  private PropertyDefFacade pdf = null;

  //this boolean indicates if the property definition was based on an existing PropertyDefinition
  private boolean existingPropertyDef = false;

  /**
   * the data transfer object containing all data for persisting this object.
   */
  protected PropertyDefDto dto = null;
  /**
   * the database id of this PropertyDefintion.
   */
  protected int uolId;
  /**
   * the id of this PropertyDefinition which corresponds with the identifier
   * used in IMS LD.
   */
  protected String propId = null;
  /**
   * the title for this PropertyDefinition as defined in IMS LD. Is null when no
   * title was defined in IMS LD.
   */
  protected String title = null;
  /**
   * the metadata for this PropertyDefinition as defined in IMS LD. Is null when
   * no metadata were defined in IMS LD.
   */
  protected String metadata = null;

  /**
   * Default constructor called by implementing classes.
   */
  protected PropertyDef() {
    //default constructor
  }

  /**
   * This constructor creates a PropertyDef based on the parameters passed. If
   * no corresponding PropertyDef was persisted a FinderException was thrown.
   *
   * @param uolId int
   * @param propId String
   * @throws PropertyException
   */
  PropertyDef(int uolId, String propId) throws PropertyException {

    try {
      //the property defintion should be created/updated when persisiting
      this.existingPropertyDef = false;
      this.uolId = uolId;
      this.propId = propId;

      //try to find find the property definition
      pdf = new PropertyDefFacade(uolId, propId);

      //get the dto for future reference
      dto = pdf.getDto();

      //inform about intitialization
      onInit();

      //create the data container for this definition
      unpack(uolId, dto.getDefaultValue());
    }
    catch (FinderException ex) {
      throw new PropertyNotFoundException(ex);
    }
  }

  /**
   * This constructor is used when the class is constructed on the basis of an
   * global URI. This is the case for an existing property.
   *
   * @param uolId int the uol id for the current UOL
   * @param propId String the prop id for the property alias to be used in the
   *   uol
   * @param dto PropertyDefDto the DTO of the foreign defined PropertyDef
   * @throws PropertyException
   */
  public PropertyDef(int uolId, String propId, PropertyDefDto dto) throws
      PropertyException {

    //the property defintion should be referenced only.
    // Persist should only update the lookup entries
    this.existingPropertyDef = true;
    this.uolId = uolId;
    this.propId = propId;

    //get the dto for future reference
    this.dto = dto;

    //inform about intitialization
    onInit();

    //create the data container for this definition
    unpack(uolId, dto.getDefaultValue());
  }

  /**
   * Called by the constructor just before the unpacking of the
   * XML data. It provides an oppertunity to initialize any properties when
   * needed.
   */
  protected void onInit() {
    //default do nothing
  }

  /**
   * Persists this object to the database by storing its data as XML blobs.
   *
   * @throws PropertyException when the operations fails.
   */
  public void persist() throws PropertyException {
    try {
      //first check if we have to create/update the property defintion or merely
      //have to add an PropertyLookup entry for it.
      if (!existingPropertyDef) {
        //we are dealing with a property definition that was defined in this
        //uol. So create/up the definition and optionally add the property lookup
        try {
          //try to find find the property definition
          pdf = new PropertyDefFacade(uolId, propId);

          //update this definition
          pdf.setDto(dto);
        }
        catch (FinderException ex) {
          //no PropertyDef was found, so create a new PropertyDef
          pdf = new PropertyDefFacade(dto);

          //set the values for this definitions
          dto = pdf.getDto();

          //create an entry for this PropertyDefEntity the PropertyLookUp
          new PropertyLookUpFacade(uolId, propId, dto.getPropDefPK(), dto.getDataType());
        }
      }
      else {
        // The definition was created outside this uol so only the lookup table needs to be altered.
        PropertyLookUpFacade plf;
        // check if the lookup already exists
        try {
          plf = new PropertyLookUpFacade(uolId, propId);
          // found, remove the lookup record
          plf.remove();
        }
        catch (FinderException ex1) {
          //nothing needs to happen
        }

        // not found or removed, create an new entry for this existing PropertyDefEntity
        new PropertyLookUpFacade(uolId, propId, dto.getPropDefPK(), dto.getDataType());
      }
    }
    catch (CreateException ex) {
      throw new PropertyException(ex);
    }
    catch (RemoveException ex) {
      throw new PropertyException(ex);
    }
  }

  /**
   * Returns the title of this Property.
   *
   * @return String the title of this Property if it has one, or null otherwise.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the meta data of this Property.
   *
   * @return String the meta data of this Property if it has meta data, or null
   *   otherwise.
   */
  public String getMetaData() {
    return metadata;
  }

  /**
   * Returns the Property id of this Property. The Property id corresponds with
   * the identifier used in the original IMS LD.
   *
   * @return String the id of this Property.
   */
  public String getPropertyId() {
    return propId;
  }

  /**
   * Return the database id of the Uol defining this Property.
   *
   * @return int the database of this PropertyDefinition
   */
  public int getUolId() {
    return uolId;
  }

  /**
   * Returns the data type of this property. Allowed data types are:
   * activity-structure, activity-tree, act, environment, environment-tree,
   * expression, learning-activity, learning-object, monitor, play, rolepart,
   * roles-tree, send-mail, support-activity, unit-of-learning, integer, real,
   * string, text, datetime, duration, file, boolean and uri.
   *
   * @return String representing the data type of this PropertyDefinition
   */
  public String getDataType() {
    return dto.getDataType();
  }

  /**
   * Returns the scope of this property represented by an Integer value. The
   * following values are bitwise exclusive value representing the different
   * scope aspects: GLOBAL = 0, LOCAL = 1, PERSONAL = 2, ROLE = 4. So for
   * example a local personal property would be represented by value 3.
   *
   * @return int the integer value representing the scope of this Property.
   */
  public int getScope() {
    return dto.getScope();
  }

  /**
   * Returns the primary key of this PropertyDefintion in the database.
   *
   * @return int the primary key in the database of this PropertyDefintion.
   */
  public int getPropDefPK() {
    return pdf.getDto().getPropDefPK();
  }

  /**
   * Returns the XML blob representing the default value in XML format to be
   * used when initialising properties based on this PropertyDefintion. This
   * method is called when a new Property based on this PropertyDefinition is
   * created.
   *
   * @return String the XML default value to used when creating Property
   *   instances.
   */
  protected String getXmlBlobValue() {
    if (dto == null) {
      dto = pdf.getDto();
    }
    return dto.getDefaultValue();
  }

  /**
   * Returns the XML blob representing the default value in XML format to be
   * used when initialising properties based on this PropertyDefintion. This
   * method is called when creating the PropertyDefinition itself. So this is
   * never called when creating an instance of this PropertyDefinition.
   *
   * @return String the XML default value to used when creating Property
   */
  protected abstract String getDefaultBlobValue();
}
