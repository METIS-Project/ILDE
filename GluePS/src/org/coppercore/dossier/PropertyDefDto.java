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

package org.coppercore.dossier;



/**
 * PropertyDefDto is a data transfer object for the PropertyDefEntity bean.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.5 $, $Date: 2005/01/11 13:15:05 $
 */
public class PropertyDefDto {
  private int pk;
  private int scope = -1;
  private String dataType = null;
  private String defaultValue = null;
  private String href = null;
  private int definedBy;

  /**
   * Default constructor for PropertyDefDto.
   *
   * @param scope int defines the scope of the property
   * @param dataType String the data type of the property
   * @param href String the uri for a gobal property
   * @param defaultValue String the default value of the property
   * @param definedBy int id of the unit of learning this property is defined by
   */
  public PropertyDefDto(int scope, String dataType, String href,
                        String defaultValue, int definedBy) {
    setScope(scope);
    setDataType(dataType);
    setDefaultValue(defaultValue);
    setHref(href);
    setDefinedBy(definedBy);
  }

  /**
   * Constructor for PropertyDefDto which only should be used by
   * PropertyDefEntity to create a DTO containing the primary key.<p> Uses the
   * default constructor.
   *
   * @param scope int defines the scope of the property
   * @param dataType String the data type of the property
   * @param href String the uri for a gobal property
   * @param defaultValue String the default value of the property
   * @param definedBy int id of the unit of learning this property is defined by
   * @param propDefPK int the primary key of the property definition
   */
  public PropertyDefDto(int scope, String dataType, String href,
                        String defaultValue, int definedBy, int propDefPK) {
    this(scope,dataType,href,defaultValue,definedBy);
    setPK(propDefPK);
  }

  private void setScope(int scope) {
    this.scope = scope;
  }

  private void setDataType(String dataType) {
    this.dataType = dataType;
  }

  private void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  private void setHref(String href) {
    this.href = href;
  }

  private void setPK(int pk) {
    this.pk = pk;
  }

  private void setDefinedBy(int definedBy) {
    this.definedBy = definedBy;
  }


  /**
   * Returns the scope of the property.
   *
   * @return int the scope of the property
   */
  public int getScope() {
    return scope;
  }

  /**
   * Returns the datatype of the property.
   *
   * @return String the datatype of the property
   */
  public String getDataType() {
    return dataType;
  }

  /**
   * Returns the default value of the property.
   *
   * @return String the default value of the property
   */
  public String getDefaultValue() {
    return defaultValue;
  }

  /**
   * Returns the Href value. This value is null if the scope of the PropertyDef
   * is not global.
   *
   * @return String the href value
   */
  public String getHref() {
    return href;
  }

  /**
   * Returns the primary key of the PropertyDefEntity.
   *
   * @return int the primary key of the PropertyDefEntity
   */
  public int getPropDefPK() {
    return pk;
  }

  /**
   * Returns the uolId of the uol that defined this PropertyDefEntity.
   *
   * @return int the uolId of the uol that defined this PropertyDefEntity
   */
  public int getDefinedBy() {
    return definedBy;
  }

}
