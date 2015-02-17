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

package org.coppercore.dto;

import java.io.Serializable;

import org.coppercore.common.Util;

/**
 * This class encapsulates all data of a UnitOfLearning bean.
 *
 * <p> Using a Data Transfer Object improves the performance because it enables the code to set all
 * properties of a bean at once instead of setting them member by member.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.5 $, $Date: 2005/01/12 14:34:31 $
 */
public class UolDto implements Serializable {
  private static final long serialVersionUID = 42L;
  private int id;
  private String uri = null;
  private String title = null;
  private String rolesId = null;
  private String contentUri = null;

  /**
   * Creates a new UolDto and sets its members according to the specified parameters.
   *
   * @param id int - the unique id of the unit of learning
   * @param uri String - the uri of the unit of learning as specified in the learning design
   * @param title String - the title of the nuit of learning as specified in the learning design
   * @param rolesId String - the id of the rolestree property
   * @param contentUri String - the offset of the uri of the webcontent
   */
  public UolDto(int id, String uri, String title, String rolesId, String contentUri) {
    setId(id);
    setUri(uri);
    setTitle(title);
    setRolesId(rolesId);
    setContentUri(contentUri);
  }

  /**
   * Creates a new UolDto and sets its members according to the specified parameters except for the
   * id of the unit of learning.
   *
   * <p> This constructor is used when creating a new unit of learning in the system. At that point
   * the database generated id of the uol is not known. The initial value of the id is -1.
   *
   * @param uri String - the uri of the unit of learning as specified in the learning design
   * @param title String - the title of the nuit of learning as specified in the learning design
   * @param rolesId String - the id of the rolestree property
   * @param contentUri String - the offset of the uri of the webcontent
   */
  public UolDto(String uri, String title, String rolesId, String contentUri) {
    this( -1, uri, title, rolesId, contentUri);
  }

  /**
   * Returns the unique id of the unit of learning.<p>
   * If this id is -1 the unit of learning is not persisted in the database.
   * @return int - the unique id of the unit of learning
   * @see #setId
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the uri of the UnitOfLearning.<p>
   * The uri is defined in the Learning Design manifest and is set during publication.
   * @return String - the uri of the UnitOfLearning
   * @see #setUri
   */
  public String getUri() {
    return uri;
  }

  /**
   * Returns the title of the UnitOfLearning.<p>
   * The title is defined in the Learning Design manifest and is set during publication.
   * @return String - the title of the UnitOfLearning
   * @see #setTitle
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the id of the root role of the UnitOfLearning.<p>
   * @return String - the id of the root role of the UnitOfLearning
   * @see #setRolesId
   */
  public String getRolesId() {
    return rolesId;
  }

  /**
   * Returns the content uri of the UnitOfLearning.<p>The content uri is the offset of the uri of the webcontent
   * @return String - the content uri of the UnitOfLearning
   * @see #setContentUri
   */
  public String getContentUri() {
    return contentUri;
  }

  /**
   * Sets the unique id of the unit of learning.
   *
   * <p> If this id is -1 the unit of learning is not persisted in the database.
   *
   * @see #getId
   * @param id int the id of the UnitOfLearning
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Sets the uri of the UnitOfLearning.
   *
   * <p> The uri is defined in the Learning Design manifest and is set during publication.
   *
   * @see #getUri
   * @param uri String the uri of the UnitOfLearning
   */
  public void setUri(String uri) {
    this.uri = uri;
  }

  /**
   * Sets the title of the UnitOfLearning.
   *
   * <p> The title is defined in the Learning Design manifest and is set during publication.
   *
   * @see #getTitle
   * @param title String the title of the UnitOfLearning
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Sets the id of the root role of the UnitOfLearning.
   *
   * <p>
   *
   * @see #getRolesId
   * @param rolesId String the id of the root of the roles of the UnitOfLearning
   */
  public void setRolesId(String rolesId) {
    this.rolesId = rolesId;
  }

  /**
   * Sets the content uri of the UnitOfLearning.
   *
   * <p>The content uri is the offset of the uri of the webcontent
   *
   * @see #getContentUri
   * @param contentUri String the content uri of the UnitOfLearning
   */
  public void setContentUri(String contentUri) {
    this.contentUri = contentUri;
  }

  /**
   * Returns a string representation of the UnitOfLearning for presentation purposes.
   *
   * @return String representation of the UnitOfLearning
   */
  public String toString() {
    return ("Uol[id=" + id + ",title=" + Util.quotedStr(title) + ",uri=" + Util.quotedStr(uri) + ",contentUri=" +
            Util.quotedStr(contentUri) + "]");
  }

  /**
   *  Creates an uninitialized instance of a UnitOfLearning Data Transfer Object.
   */
  public UolDto() {
    //default constructor    
  }

}
