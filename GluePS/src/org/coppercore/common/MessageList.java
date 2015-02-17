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

package org.coppercore.common;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Implements a list of {@link Message} items.
 *
 * <p>The class supports buffering of the messages. When buffering is enabled
 * all messages are kept in memory and can be retrieved later. When buffering is
 * disabled all messages are directly written to the console via System.out. All
 * messages are time-stamped since the a starttime. This starttime is either
 * explicitly defined during the creation of a new instance or it is set when
 * logging the first message.
 *
 * <p> Each message is of a specific type, four types are allowed:
 * <ul>
 * <li>INFO - informational message</li>
 * <li>WARNING - warning message</li>
 * <li>ERROR - error message</li>
 * <li>EXCEPTION - exception message</li>
 * </ul>
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.9 $, $Date: 2005/01/12 14:34:32 $
 */
public class MessageList implements Serializable {
  private static final long serialVersionUID = 42L;

  private ArrayList logMessages = new ArrayList();
  private long startTime = 0;
  private boolean useBuffering = true;

  /**
   * Creates a new instance of the MessageList with default values.
   */
  public MessageList() {
    // do nothing
  }

  /**
   * Creates a new instance of the MessageList and sets the buffering to the
   * specified value.
   *
   * @param aUseBuffering boolean defines whether buffering is enabled
   */
  public MessageList(boolean aUseBuffering) {
    useBuffering = aUseBuffering;
  }

  /**
   * Creates a new instance of the MessageList and sets the buffering and the starttime to the
   * specified values.
   *
   * @param aUseBuffering boolean defines whether buffering is enabled
   * @param aStartTime long the time used as starttime for all logging
   */
  public MessageList(boolean aUseBuffering, long aStartTime) {
    useBuffering = aUseBuffering;
    startTime = aStartTime;
  }

  /**
   * Returns the starttime of this MessageList.<p>All logged timings are relative to this time.
   * @return long the starttime of this instance
   */
  public long getStartTime() {
    return startTime;
  }

  /**
   * Adds additional text to the last logged message.
   *
   * <p>If no message has been logged no text is added. If buffering is disabled
   * messages are never kept so this method cannot append additional text to the
   * last message.
   *
   * @param addition String the text to add to the last logged message
   */
  public void appendToLastLog(String addition) {
    int index = logMessages.size();

    if (index > 0) {
      Message msg = (Message) logMessages.get(index - 1);

      msg.addToMessage(addition);
    }
  }

  /**
   * Appends another MessageList to this one.
   * @param aLogger MessageList the list to append to this one
   */
  public void appendLog(MessageList aLogger) {
    logMessages.addAll(aLogger.getLogMessages());
  }

  private long elapsedTime() {
//    double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0;
//    DecimalFormat df = new DecimalFormat("####0.000s");
//    return df.format(elapsedTime);
    if (startTime == 0) {
      startTime = System.currentTimeMillis();
    }
    return (System.currentTimeMillis() - startTime);
  }

  private void log(int level, String message) {
    Message msg;
    msg = new Message(level, elapsedTime(), message);
    if (useBuffering) {
      logMessages.add(msg);
    }
    else {
      System.out.println(msg);
    }
  }

  /**
   * Logs the exception and sets the level of the message to EXCEPTION.
   *
   * @param ex Exception the exception to log
   */
  public void logException(Exception ex) {
    log(Message.EXCEPTION, ex.getMessage());
  }

  /**
   * Logs the message and sets the level of the message to ERROR.
   *
   * @param message String the text of the messsage to store
   */
  public void logError(String message) {
    log(Message.ERROR, message);
  }

  /**
   * Logs the message and sets the level of the message to WARNING.
   *
   * @param message String the text of the messsage to store
   */
  public void logWarning(String message) {
    log(Message.WARNING, message);
  }

  /**
   * Logs the message and sets the level of the message to INFO.
   *
   * @param message String the text of the messsage to store
   */
  public void logInfo(String message) {
    log(Message.INFO, message);
  }

  /**
   * Returns the list of logged {@link Message}s.
   *
   * @return ArrayList the list of logged Messages
   */
  public ArrayList getLogMessages() {
    return logMessages;
  }

  /**
   * Writes all logged messages to the specified PrintStream.
   *
   * @param out PrintStream the stream to write the messages to
   */
  public void printMessages(PrintStream out) {
    if (logMessages != null) {
      Iterator iterator = logMessages.iterator();
      while (iterator.hasNext()) {
        out.println( ( (Message) iterator.next()).toString());
      }
    }
  }

  /**
   * Writes all logged messages to the specified PrintWriter.
   *
   * @param out PrintWriter the stream to write the messages to
   */
  public void printMessages(PrintWriter out) {
    if (logMessages != null) {
      Iterator iterator = logMessages.iterator();
      while (iterator.hasNext()) {
        out.println( ( (Message) iterator.next()).toString());
      }
    }
  }

}
