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

import org.coppercore.common.Message;
import org.coppercore.common.MessageList;

/**
 * Encapsulates the outcome of the validation process.
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.5 $, $Date: 2005/01/12 14:34:31 $
 */
public class ValidationResult implements Serializable {
  private static final long serialVersionUID = 42L;
  private boolean success = false;
  private MessageList messages = null;

  /**
   * Creates a new ValidationResult.
   * @param success boolean      - indicates wether the validation succeeded
   * @param messages MessageList - contains a list of Messages generated during the validation process
   */
  public ValidationResult(boolean success, MessageList messages) {
  this.success = success;
    this.messages = messages;
  }

  /**
   * Returns true if the validation process succeeded.
   *
   * @return boolean - indicates wether the validation process succeeded,
   */
  public boolean getSuccess() {
  return success;
  }

  /**
   * Returns a MessageList containing all messages that were generated during the validation process.
   *
   * <p> These messages include information messages about the process, errors and warnins
   * encountered during the validation if any, and finally if exceptions occurred during the
   * processing these too are returned.
   *
   * @return MessageList - a list with all messages generated during the validation process
   */
  public MessageList getMessages() {
  return messages;
  }

  /**
   * Returns the MessageList as an array of Messages.<p>This method is used for passing the MessageList via the SOAP protocol.
   * @return Message[] - the converted MessageList
   */
  public Message[] getMessageArray() {
  if (messages != null && messages.getLogMessages() != null) {
     return (Message[]) messages.getLogMessages().toArray(new Message[0]);
    }
  return new Message[0];
  }
  
}
