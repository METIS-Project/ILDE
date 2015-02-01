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

/**
 * This interface should be implemented by all nodes that represent a IMSLD resource. It has been
 * introduced to allow the definition of an empty resource for those cases that item does not contain
 * a reference to a resource.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.1 $, $Date: 2005/05/31 07:40:03 $
 */
public interface IIMSCPResource {

  /**
   * Returns true if the type of this resource is imsldcontent as specified by
   * the type attribute of the resource element in the IMS content package
   * specification.
   * 
   * @return boolean true if the type of this resource is imsldcontent
   */
  public abstract boolean isIMSLDContent();
  
  /**
   * Returns the url of this resource.
   * 
   * @return String the url of this resource
   */
  public abstract String getURL();

  /**
   * Returns the type of this resource.
   * 
   * @return String the type of this resource
   */
  public abstract String getType();
  
  /**
   * Return the identifier of this resource
   * 
   * @return String the identifier of this resource
   */
  public abstract String getResourceIdentifier();
}
