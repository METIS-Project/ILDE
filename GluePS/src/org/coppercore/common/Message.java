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

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Defines a single message as logged by CopperCore.
 *
 * <p>Messages can be of four different types as specified by the level of the
 * message:
 * <ul>
 * <li>INFO - informational message</li>
 * <li>WARNING - warning message</li>
 * <li>ERROR - error message</li>
 * <li>EXCEPTION - exception message</li>
 * </ul>
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.10 $, $Date: 2005/01/12 14:34:32 $
 */
public class Message implements Serializable {
  private static final long serialVersionUID = 42L;
  private int level;
  private long elapsedTime;
  private String message;

  /** Defines a informational message. */
  public final static int INFO = 0;

  /** Defines a warning message. */
  public final static int WARNING = 1;

  /** Defines an error message. */
  public final static int ERROR = 2;

  /** Defines an exception message. */
  public final static int EXCEPTION = 3;

  /**
   * Creates an uninitialized message.
   */
  public Message() {
    // do nothing
  }

  /**
   * Creates an initialized message and sets its members to the specified values.
   *
   * @param aLevel int representing the type of the message. The allowed types are defined in
   * @param aElapsedTime long the time in milliseconds since a starttime
   * @param aMessage String the text of the message to record
   */
  public Message(int aLevel, long aElapsedTime, String aMessage) {
    level = aLevel;
    elapsedTime = aElapsedTime;
    message = aMessage;
  }

  /**
   * Returns the level of the message.
   *
   * <p>The messagelevel represents the type of the message. Supported types are:
   * <ul>
   * <li>INFO - informational message</li>
   * <li>WARNING - warning message</li>
   * <li>ERROR - error message</li>
   * <li>EXCEPTION - exception message</li>
   * </ul>
   *
   * @return int the level of this message
   */
  public int getLevel() {
    return level;
  }

  /**
   * Returns the time elapsed in milliseconds since the start of time recording.
   *
   * <p>The actual starttime is defined out of scope for the message.
   *
   * @return long the elapsed time in miliseconds
   */
  public long getElapsedTime() {
    return elapsedTime;
  }

  /**
   * Returns the logged message.
   *
   * @return String
   */
  public String getMessage() {
    return message;
  }

  /**
   * Adds aditional text to the logged message.
   * @param addition String the extra text to append to the logged message
   */
  public void addToMessage(String addition) {
    message += addition;
  }

  /**
   * Returns a String representation of the logged message.
   *
   * <p>The format of the string is:
   * <br> [&lt;time in seconds&gt;] &lt;type of message&gt; - &lt;the recorded message text&gt;.
   *
   * @return String the representation of the logged message
   */
  public String toString() {
    DecimalFormat df = new DecimalFormat("[####0.000s] ");
    String line;
    line = df.format(elapsedTime / 1000.0);
    switch (level) {
      case INFO:
        line += "INFO - ";
        break;
      case WARNING:
        line += "WARNING - ";
        break;
      case ERROR:
        line += "ERROR - ";
        break;
      case EXCEPTION:
        line += "EXCEPTION - ";
        break;
    }
    line += message;
    return line;

  }

}
