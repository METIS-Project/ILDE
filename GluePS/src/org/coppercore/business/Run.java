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

package org.coppercore.business;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import org.coppercore.common.HomeFactory;
import org.coppercore.common.ServiceLocator;
import org.coppercore.component.ComponentFactory;
import org.coppercore.dossier.PropertyFacade;
import org.coppercore.dto.RunDto;
import org.coppercore.dto.RunEntityPK;
import org.coppercore.entity.RunEntity;
import org.coppercore.entity.RunEntityHome;
import org.coppercore.exceptions.NotFoundException;
import org.coppercore.exceptions.ServiceLocatorException;

/**
 * The Run business object represents the persisted runs of CopperCore.
 *
 * <p> Instances of this class can only be created by calling either the create or finder class
 * methods. The only properties of a run that can be changed are:
 * <ul>
 * <li>the title of the run - use setTitle()</li>
 * <li>the starttime of the run - use setStarttime()</li>
 * </ul>
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.15 $, $Date: 2005/06/17 10:37:52 $
 */
public class Run  implements Serializable {
  private static final long serialVersionUID = 42L;

  private RunDto dto;
  private static RunEntityHome runEntityHome = null;

  /**
   * The contructor for a new Run instance.
   *
   * <p>The constructor is private to enforce the usage of the class factory methods to create a new
   * run. Use Run.create to create a new run in the database, use the Run.find* methods to lookup
   * existing runs from the database.
   *
   * @param dto a RunDto containing the run properties.
   */
  private Run(RunDto dto) {
    this.dto = dto;
  }

  /**
   * Returns the run properties in a RunDto.
   *
   * @return a RunDto containing the properties of the run.
   */
  public RunDto getDto() {
    return this.dto;
  }

  /**
   * Return the run id.
   *
   * @return an int containing the run id.
   */
  public int getId() {
    return dto.getRunId();
  }

  /**
   * Return the run title of this run.
   *
   * @return an String containing the title.
   * @see #setTitle
   */
  public String getTitle() {
    return dto.getTitle();
  }


  /**
   * Returns the unit of learning where this run belongs to.
   *
   * @return the Uol of the run.
   */
  public Uol getUol() {
  try {
      return Uol.findByPrimaryKey(dto.getUolId());
    }
    catch (NotFoundException ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Resets the starttime of the run to the specified value.
   *
   * <p>All time related operations in LD are relative to this starttime.
   *
   * @param starttime a Calendar value specifying the new starttime of the run.
   * @see #getStarttime
   */
  public void setStarttime(Calendar starttime) {
    dto.setStarttime(starttime);
    persist();
  }

  /**
   * Returns the starttime of the run.
   *
   * @return Calendar
   * @see #setStarttime
   */
  public Calendar getStarttime() {

    return dto.getStarttime();
  }

  /**
   * returns the date and time the run has started.
   *
   * @return String containing the date and time the run has started
   */
  public String getTimeUnitOfLearningStarted() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    return (format.format(getStarttime().getTime()));

  }

  /**
   * Resets the title of the run to the specified value.
   *
   * @param title a String specifying the new title of the run.
   * @see #getTitle
   */
  public void setTitle(String title) {
    dto.setTitle(title);
    persist();
  }

  private void persist() {
    RunEntityPK pk = new RunEntityPK(dto.getRunId());
    try {
      // retrieve the entitybean for this run
      RunEntity re = getRunEntityHome().findByPrimaryKey(pk);
      re.setRunDto(dto);
    }
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Removes the run from the system.
   *
   * <p>Removing the run from the system involves removing all runparticipations
   * and all run properties first.
   */
  public void remove() {
    RunEntityPK pk = new RunEntityPK(dto.getRunId());
    try {
      //find all runparticipations associated with this run
      Collection runParticipations = RunParticipation.findByRun(this);

      //remove all runparticipations
      Iterator iter = runParticipations.iterator();
      while (iter.hasNext()) {
        RunParticipation rp = (RunParticipation)iter.next();
        rp.remove();
      }

      //now we must remove all the run properies (run or role)
      PropertyFacade.removeLocalProperties(dto.getRunId());

      // retrieve the entitybean for this run
      RunEntity re = getRunEntityHome().findByPrimaryKey(pk);
      re.remove();
      
      ComponentFactory.getPropertyFactory().clearCache();      
    }
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }


  /**
   * Return true if the passed object equals this object which is true when we
   * are dealing with the same object or if the id is the same.
   *
   * @param obj Object the object to compare
   * @return boolean true when this object equals the passed object
   */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (! (obj instanceof User)) {
      return false;
    }
    Run that = (Run) obj;

    return (that.getId() == this.getId());
  }



  /**
   * Creates a new run for the specified unit of learning.
   *
   * <p>The title is set to the specified value, the starttime of the run is set to time of creation
   * of the run e.g. now.
   *
   * <p>The new run is also persisted in the database.
   *
   * @return a Run representing the newly created run.
   * @param uol Uol, the unit of learning to create a new run for.
   * @param title String, the title of the run.
   */
  public static Run create(Uol uol, String title) {
    return create(uol, title, Calendar.getInstance());
  }

  /**
   * Creates a new run for the specified unit of learning.
   *
   * <p>The title and starttime are set to the specified values.
   *
   * <p>The new run is also persisted in the database.
   *
   * @return a Run representing the newly created run.
   * @param uol Uol, the unit of learning to create a new run for.
   * @param title String, the title of the run.
   * @param starttime Calendar, specifies the starttime of the run.
   */
  public static Run create(Uol uol, String title, Calendar starttime) {
    try {
      RunDto dto = new RunDto( -1, uol.getId(), title, starttime);
      RunEntity runEntity = getRunEntityHome().create(dto);
      return new Run(runEntity.getRunDto());
    }
    // inform ejb container about application exceptions by rethrowing them as an EJBException.
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Returns a <code>Run</code> object representing the run with the specified runid.
   *
   * @param runId int, the id of the run to find.
   * @throws NotFoundException when the run with the specified id cannot be found.
   * @return a Run object representing the run with runId.
   */
  public static Run findByPrimaryKey(int runId) throws NotFoundException {
    try {
      RunEntityPK pk = new RunEntityPK(runId);
      RunEntity re = getRunEntityHome().findByPrimaryKey(pk);
      return new Run(re.getRunDto());
    }
    catch (ServiceLocatorException slex) {
      throw new EJBException(slex);
    }
    catch (FinderException fex) {
      throw new NotFoundException(fex);
    }
  }

  /**
   * Retrieves a collection of all runs belonging to the uol.
   *
   * <p>If no runs are found, the method returns an empty collection.
   *
   * @param uol int, the uol to retrieve all runs for.
   * @return a Collection of Run objects that belong to the specified unit of learning.
   */
  public static Collection findByUol(Uol uol) {
    ArrayList result = new ArrayList();
    try {
      Collection allRuns = getRunEntityHome().findByUolId(uol.getId());
      Iterator i = allRuns.iterator();
      while (i.hasNext()) {
        RunEntity item = (RunEntity) i.next();
        result.add(new Run(item.getRunDto()));
      }
      return result;
    }
    // inform ejb container about application exceptions by rethrowing them as an EJBException.
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Returns the home interface for the RunEntity bean.
   *
   * @throws ServiceLocatorException
   * @return the home interface for the RunEntity bean
   */
  private static RunEntityHome getRunEntityHome() throws
      ServiceLocatorException {
    // Because looking up a home interface is an expensive operation, fetch it only once.
    if (runEntityHome == null) {
      // First time, fetch a new home interface
      ServiceLocator locator = ServiceLocator.getInstance();
      runEntityHome = (RunEntityHome) locator.getEjbLocalHome(HomeFactory.
          RUNENTITY);
    }
    return runEntityHome;
  }


  /**
   * Returns a String representation of this Run instance.
   * @return a String representation of this Run instance
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return "Run[runId=" + getId() + ", title=" + getTitle() + "]";
  }

}
