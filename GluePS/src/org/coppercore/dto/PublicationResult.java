/*
 * CopperCore , an IMS-LD level C engine
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

package org.coppercore.dto;

import java.io.Serializable;

import org.coppercore.common.MessageList;

/**
 * The <code>PublicationResult</code> holds the outcomes of the publication process.<p>
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.4 $, $Date: 2005/01/12 14:34:32 $
 */
public class PublicationResult extends ValidationResult implements Serializable {
  private static final long serialVersionUID = 42L;
  private int uolId = -1;

  /**
   * Creates a new PublicationResult with the given results.
   *
   * @param publicationSucceeded boolean - Indicates wether the publication process completed succesfully
   * @param uolId int                    - the id of the unit of learning.
   * @param messages MessageList         - a list of messages describing the publication result. These include error, warning and informational messages
   */
  public PublicationResult(boolean publicationSucceeded, int uolId, MessageList messages) {
    super(publicationSucceeded, messages);
    this.uolId = uolId;
  }

  /**
   * Returns the id of the published unit of learning.<p>If the publication did not complete successfully the method returns -1.
   *
   * @return int the id of the published unit of learning.<p>If the publication did not complete successfully the method returns -1.
   */
  public int getUolId() {
    return uolId;
  }


}
