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

package org.coppercore.entity;

import org.coppercore.dto.RunDto;

/**
 * Specifies the interface for the Run bean.
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.3 $, $Date: 2005/01/11 13:15:12 $
 */
public interface RunEntity extends javax.ejb.EJBLocalObject {

  /**
   * Returns all members of this Run in a single RunDto parameter.
   * @return RunDto containing all members of this run instance
   * @see #setRunDto
   */
  public RunDto getRunDto();

  /**
   * Sets the RunDto of this run.
   *
   * <p>By using a Run Data Transfer Object all members of this instance are set
   * at once.
   *
   * @param runDto RunDto the dto containing all parameters of this run
   * @see #getRunDto
   */
  public void setRunDto(RunDto runDto);
}
