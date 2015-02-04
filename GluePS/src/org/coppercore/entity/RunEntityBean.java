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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.coppercore.dto.RunDto;
import org.coppercore.dto.RunEntityPK;

/**
 * RunEntityBean is the persistence component for a run.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.6 $
 */
public class RunEntityBean implements EntityBean {

	private static final long serialVersionUID = 43L;
	private EntityContext entityContext;
	private RunDto runDto;
	private boolean modified = false;

	/**
	 * Creates a new <code>RunEntity</code> record in the database.
	 * 
	 * <p>
	 * The run will be created for the specified unit-of-learning with the
	 * specified title.
	 * 
	 * @param aRunDto
	 *            the id of the unit-of-learning for this run
	 * @return the primary key of the new RunEntity, being the runId
	 * @throws CreateException
	 *             if the new record cannot be created in the database
	 */
	public RunEntityPK ejbCreate(RunDto aRunDto) throws CreateException {

		setRunDto(aRunDto);
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				String sql = "INSERT INTO Run (UolID, Name, Starttime) VALUES (?,?,?)";
				PreparedStatement statement = connection.prepareStatement(sql);
				try {
					statement.setInt(1, getRunDto().getUolId());
					statement.setString(2, getRunDto().getTitle());
					statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
					if (statement.executeUpdate() != 1) {
						throw new CreateException("Could not create RunEntity for: " + getRunDto().getUolId());
					}

					int id = Util.getInstance().getIdentity(connection, "run", "RunId");
					if (id != -1) {
						setRunDto(new RunDto(id, aRunDto.getUolId(), aRunDto.getTitle(), aRunDto.getStarttime()));
						modified = false;
						return new RunEntityPK(id);
					}
					throw new CreateException("Could not create RunEntity for: " + getRunDto().getUolId());
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
		} catch (SQLException ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	/**
	 * Perform post create actions.
	 * 
	 * <p>
	 * This method is called by the application server after the bean has been
	 * created. For the run this method does nothing.
	 * 
	 * @param aRunDto
	 *            RunDto the parameters of the new run
	 * @throws CreateException
	 *             when there would be an error in this method
	 */
	public void ejbPostCreate(RunDto aRunDto) throws CreateException {
		// do nothing
	}

	/**
	 * Removes the run from the database.
	 * 
	 * @throws RemoveException
	 *             when there is an error removing the run from the database
	 */
	public void ejbRemove() throws RemoveException {
		RunEntityPK pk = (RunEntityPK) entityContext.getPrimaryKey();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection.prepareStatement("DELETE FROM Run WHERE (RunId = ?)");
				try {
					statement.setInt(1, pk.runId);

					if (statement.executeUpdate() == 0) {
						throw new RemoveException("Could not delete " + pk);
					}
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
		} catch (Exception ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Sets the RunDto of this run.
	 * 
	 * <p>
	 * By using a Run Data Transfer Object all members of this instance are set
	 * at once.
	 * 
	 * @param dto
	 *            RunDto the dto containing all parameters of this run
	 * @see #getRunDto
	 */
	public void setRunDto(RunDto dto) {
		this.runDto = dto;
		modified = true;
	}

	/**
	 * Returns all members of this Run in a single RunDto parameter.
	 * 
	 * @return RunDto containing all members of this run instance
	 * @see #setRunDto
	 */
	public RunDto getRunDto() {
		return this.runDto;
	}

	/**
	 * Loads the fields of the RunEntity object with data from the database.
	 */
	public void ejbLoad() {
		RunEntityPK pk = (RunEntityPK) entityContext.getPrimaryKey();
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT UolId, Name, Starttime FROM Run WHERE (RunId = ?)");
				try {
					statement.setInt(1, pk.runId);
					ResultSet resultSet = statement.executeQuery();

					if (!resultSet.next()) {
						throw new EJBException("RunEntity not found: " + pk);
					}
					Calendar timestamp = Calendar.getInstance();
					timestamp.setTimeInMillis(resultSet.getTimestamp(3).getTime());
					runDto = new RunDto(pk.runId, resultSet.getInt(1), resultSet.getString(2), timestamp);
					modified = false;
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
		} catch (Exception e) {
			throw new EJBException(pk + " failed to load", e);
		}
	}

	public void ejbStore() {
		if (!modified) 
			return;
		
		RunEntityPK pk = (RunEntityPK) entityContext.getPrimaryKey();
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("UPDATE Run SET UolId=?, Name=?, Starttime=? WHERE RunId=?");
				try {
					statement.setInt(1, runDto.getUolId());
					statement.setString(2, runDto.getTitle());
					statement.setTimestamp(3, new Timestamp(runDto.getStarttime().getTimeInMillis()));
					statement.setInt(4, pk.runId);
					if (statement.executeUpdate() != 1) {
						throw new EJBException("Could not update " + pk);
					}
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
		} catch (Exception ex) {
			throw new EJBException(ex);
		}
	}

	public void ejbActivate() {
		// do nothin
	}

	public void ejbPassivate() {
		// do nothing
	}

	public void unsetEntityContext() {
		this.entityContext = null;
	}

	public void setEntityContext(EntityContext entityContext) {
		this.entityContext = entityContext;
	}

	/**
	 * Finds the RunEntity record in the database by looking up the specified
	 * runId.
	 * 
	 * <p>
	 * The method returns the primary key of the record to indicate the found
	 * record. <br>
	 * If the record cannot be located the method throws a FinderException.
	 * 
	 * @param pk
	 *            the primary key of the record to find
	 * @return the primary key of the record, being the userId
	 * @throws FinderException
	 *             if the record cannot be located in the database
	 */
	public RunEntityPK ejbFindByPrimaryKey(RunEntityPK pk) throws FinderException {
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection.prepareStatement("SELECT RunId FROM Run WHERE (RunId = ?)");
				try {
					statement.setInt(1, pk.runId);
					ResultSet resultSet = statement.executeQuery();
					// check if bean can be loaded from database
					if (!resultSet.next())
						throw new FinderException("Could not find " + pk);
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
			// everything was ok, so return the primary key
			return pk;
		} catch (SQLException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Returns a Collection containing the RunEntityPK's of all runs for the
	 * specified unit-of-learning.
	 * 
	 * <p>
	 * These objects are the primary keys that enable the ejb container to
	 * create a collection of RunEntity objects to return to the caller. <br>
	 * The method returns an empty collection if there are no runs found in the
	 * database.
	 * 
	 * @param uolId
	 *            the id of the unit-of-learning for which the runs have to be
	 *            retrieved
	 * @return a Collection containing the RunEntityPK's of the specified
	 *         unit-of-learning in the database
	 * @throws FinderException
	 *             if a database error occurs
	 */
	public java.util.Collection ejbFindByUolId(int uolId) throws FinderException {
		Vector result = new Vector();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection.prepareStatement("SELECT RunId FROM Run WHERE (uolId = ?)");
				try {
					statement.setInt(1, uolId);
					ResultSet rs = statement.executeQuery();
					while (rs.next()) {
						result.addElement(new RunEntityPK(rs.getInt(1)));
					}

				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}

			// everything was ok, so return the collection of primary keys.
			return result;
		} catch (SQLException e) {
			throw new EJBException(e);
		}
	}

}
