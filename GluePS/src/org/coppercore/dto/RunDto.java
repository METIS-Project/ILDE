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
import java.text.DateFormat;
import java.util.Calendar;

import org.coppercore.common.Util;

/**
 * This class encapsulates all data of a Run bean.
 *
 * <p> Using a Data Transfer Object improves the performance because it enables
 * the code to set all properties of a bean at once instead of setting them
 * member by member.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.7 $, $Date: 2005/01/26 17:09:09 $
 */
public class RunDto
    implements Serializable {
  private static final long serialVersionUID = 43L;

  private int runId;
  private int uolId;
  private String title;
  private Calendar starttime = null;
  
  /**
   * Creates a new initialized instance of a RunDto.
   *
   * @param runId int the id of the run
   * @param uolId int the id of the unit of learning this run belongs to
   * @param title String the title of the run
   * @param starttime Calendar the starttime of the run, null if the run has no
   *   starttime
   */
  public RunDto(int runId, int uolId, String title, Calendar starttime) {
    this.runId = runId;
    this.uolId = uolId;
    this.title = title;
    this.starttime = starttime;
  }

  /**
   * Returns the id of the run.
   * @return int the id of the run
   */
  public int getRunId() {
  return runId;
  }
  
  /**
   * Sets the id of the run.
   * @param id the id of the run
   */
  public void setRunId(int id) {
    this.runId = id;
  }

  /**
   * Returns the unique id of the unit of learning this run belongs to.
   * @return int the id of the unit of learning
   */
  public int getUolId() {
    return uolId;
  }
  
  /**
   * Sets the uol id of this run.
   * @param uolId the uol id of this run
   */
  public void setUolId(int uolId) {
    this.uolId = uolId;
  }

  /**
   * Returns the title of the run.
   * @return String the title of the run
   * @see #setTitle
   */
  public String getTitle() {
  return title;
  }
  
  /**
   * Returns the starttime of the Run.<p>If the run has no starttime defined, the method returns null
   * @return Calendar the starttime of the run
   * @see #setStarttime
   */
  public Calendar getStarttime() {
  return starttime;
  }

  /**
   * Sets the title of the Run.
   * @param title String the new title of the run
   * @see #getTitle
   */
  public void setTitle(String title) {
  this.title = title;
  }

  /**
   * Sets the starttime of the Run.
   * @param starttime Calendar the starttime of the run
   * @see #getStarttime
   */
  public void setStarttime(Calendar starttime) {
  this.starttime = starttime;
  }

  /**
   * Checks if this instance equals the given object.
   *
   * <p>The objects are considered equal if both instances are of the same type
   * and all members are equal.
   *
   * @param obj Object the object to compare this instance with
   * @return boolean true if both objects are equal, false otherwise
   */
  public boolean equals(Object obj) {
  if (obj != null) {
      if (this.getClass().equals(obj.getClass())) {
        RunDto that = (RunDto) obj;
        return ( (this.getRunId() == that.getRunId()) &&
                 (this.getUolId() == that.getUolId()) &&
                ( ( (this.getTitle() == null) && (that.getTitle() == null)) ||
                 (this.getTitle() != null &&
                  this.getTitle().equals(that.getTitle()))) &&
                  ((this.getStarttime() == null) && (that.getStarttime() == null)) ||
                  (this.getStarttime() != null &&
                   this.getStarttime().equals(that.getStarttime())));
      }
    }
    return false;
  }

  /**
   * Returns a string representation for the Run for presentation purposes.
   * @return String representation of the Run
   */
  public String toString() {
    DateFormat dateFormat = DateFormat.getDateTimeInstance();

    return "Run[runId=" + runId + ",uolId=" + uolId + ",title=" + Util.quotedStr(title) + ",starttime=" +
        (starttime == null ? "[null]" : Util.quotedStr(dateFormat.format(starttime.getTime()))) + "]";
  }

  /**
   * Creates an unitialized instance.
   *
   * <p>This constructor is needed for the Axis soap toolkit
   */
  public RunDto() {
    //default constructor    
  }
}
