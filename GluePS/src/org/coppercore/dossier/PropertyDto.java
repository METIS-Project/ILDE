/*
 * CopperCore, an IMS-LD level C engine
 * Copyright (C) 2003 not attributable
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
 * not attributable
 *
 * prof.dr. Rob Koper,
 * director of learning technologies research and development
 *
 */

package org.coppercore.dossier;

import java.io.Serializable;

/**
 * PropertyDto is a data transfer object for the PropertyEntity bean.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.7 $, $Date: 2005/01/12 14:34:26 $
 */
public class PropertyDto implements Serializable {
  private static final long serialVersionUID = 42L;

  private String propValue = null;

  private String dataType = null;

  private String propId = null;

  private int propDefPk;

  private int scope;

  /**
   * Default constructor for PropertyDto.
   *
   * @param propValue String property value
   * @param dataType String data type of the property
   * @param propId String the learning design id of the property
   * @param propDefPk int the is of the property definition for this property
   * @param scope int the scope of this property
   */
  public PropertyDto(String propValue, String dataType, String propId, int propDefPk, int scope) {
    setPropValue(propValue);
    setDataType(dataType);
    setPropId(propId);
    setPropDefPK(propDefPk);
    setScope(scope);
  }

  private void setPropValue(String propValue) {
    this.propValue = propValue;
  }

  private void setDataType(String dataType) {
    this.dataType = dataType;
  }

  private void setPropDefPK(int propDefPk) {
    this.propDefPk = propDefPk;
  }

  private void setScope(int scope) {
    this.scope = scope;
  }

  /**
   * Returns the property value.
   *
   * @return String the value of this property instance
   */
  public String getPropValue() {
    return propValue;
  }

  /**
   * Returns the scope of the property.
   *
   * @return String the scope of this property
   */
  public int getScope() {
    return scope;
  }

  /**
   * Returns the data type of this property.
   *
   * @return String the data type of this property
   */
  public String getDataType() {
    return dataType;
  }

  /**
   * Sets the learning design id of this property.
   *
   * @param propId String  the learning design id of this property
   * @see #getPropId
   */
  public void setPropId(String propId) {
    this.propId = propId;
  }

  /**
   * Returns the property id as defined in the IMS-LD instance.
   *
   * @return String  the property id as defined in the IMS-LD instance
   * @see #setPropId
   */
  public String getPropId() {
    return propId;
  }

  /**
   * Return the key for the definition of this property which acts as foreign key in the property table.
   *
   * @return int the key for the definition of this property
   */
  public int getPropDefPK() {
    return propDefPk;
  }

  /**
   * Returns true if property value equals aValue, false otherwise.
   *
   * @param aValue String value used for comparison
   * @return boolean
   */
  public boolean equalsValue(String aValue) {
    boolean result = false;

    if (propValue == null) {
      result = (aValue == null);
    }
    else {
      result = (propValue.equals(aValue));
    }
    return result;
  }
}
