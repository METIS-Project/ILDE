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

package org.coppercore.events;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.coppercore.business.Uol;

/**
 * This class represents a timed event in CopperCore.
 * <p>
 * The TimerMessageBean is implemented as an EJB Message Bean to make sure that
 * each event is handled by the CopperCore engine.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.6 $, $Date: 2006/09/22 14:57:18 $
 */
public class TimerMessageBean implements MessageDrivenBean, MessageListener {
  private static final long serialVersionUID = 42L;
  MessageDrivenContext messageDrivenContext;

  /**
   * Creates a new TimerMessageBean.
   */
  public void ejbCreate() {
    //do nothing
  }

  public void ejbRemove() {
    //do nothing
  }

  public void onMessage(Message msg) {
    try {
      Logger logger = Logger.getLogger(this.getClass());
      logger.debug("check timer events for uol[" + ((TextMessage) msg).getText() + "]");

      int uolId = Integer.parseInt(((TextMessage) msg).getText());

      Uol uol = Uol.findByPrimaryKey(uolId);

      //notify the event dispatcher about a new timer event
      EventDispatcher.getEventDispatcher().postMessage(uol, null, null, null, EventDispatcher.TIMER_EVENT);

    } catch (Exception ex) {
      Logger logger = Logger.getLogger(this.getClass());
      logger.error(ex);
    	//TODO enable optional debug statement here
    	//System.out.println(ex);
    }
  }

  public void setMessageDrivenContext(MessageDrivenContext messageDrivenContext) {
    this.messageDrivenContext = messageDrivenContext;
  }
}