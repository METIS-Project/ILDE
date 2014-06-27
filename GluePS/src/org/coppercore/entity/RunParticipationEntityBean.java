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

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.coppercore.dto.RunParticipationDTO;
import org.coppercore.dto.RunParticipationEntityPK;

/**
 * This class implements the persistence component for the RunParticapation.
 * 
 * <p>
 * This class is implemented as a BMP ejb bean.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.9 $, $Date: 2009/01/08 09:23:04 $
 */
public class RunParticipationEntityBean implements EntityBean {
	private static final long serialVersionUID = 42L;
	private EntityContext entityContext;
	// private String userId;
	// private int runId;
	private RunParticipationDTO dto;
	private boolean modified = false;

	/**
	 * Creates a new <code>RunParticipation</code> record in the database.
	 * 
	 * <p>
	 * The run participation will be created with the data specified in the
	 * RunParticiaptionDTO.
	 * 
	 * @param runParticipationDTO
	 *            defines the intial values for this RunParticipation
	 * @return the primary key of the new RunParticipationEntity
	 * @throws CreateException
	 *             if the new record cannot be created in the database
	 */
	public RunParticipationEntityPK ejbCreate(RunParticipationDTO runParticipationDTO) throws CreateException {
		// userId = runParticipationDTO.getUserId();
		// runId = runParticipationDTO.getRunId();

		setDto(runParticipationDTO);
		RunParticipationEntityPK pk = new RunParticipationEntityPK(dto.getRunId(), dto.getUserId());

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("INSERT INTO RunParticipation (UserID, RunID, ActiveRole) VALUES (?,?,?)");
				try {
					statement.setString(1, pk.userId);
					statement.setInt(2, pk.runId);
					statement.setInt(3, runParticipationDTO.getActiveRole());
					if (statement.executeUpdate() != 1) {
						throw new CreateException("Could not create RunParticipationEntity: " + pk);
					}
					if (dto.getActiveRole() != -1) {
						// lookup the LD RoleId of the activeRole to fill the
						// bean completely
						statement.close();
						statement = connection
								.prepareStatement("SELECT RoleId FROM RoleInstance WHERE (RoleInstanceId = ?) AND (RunId = ?)");
						statement.setInt(1, runParticipationDTO.getActiveRole());
						statement.setInt(2, dto.getRunId());
						ResultSet resultSet = statement.executeQuery();
						if (!resultSet.next()) {
							throw new CreateException("Could not create RunParticipationEntity: " + pk);
						}
						dto.setRoleId(resultSet.getString(1));
						modified = false;
					}
					return pk;
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
	 * created. For the run participation this method does nothing.
	 * 
	 * @param runParticipationDTO
	 *            the parameters of the new run
	 * @throws CreateException
	 *             when there would be an error in this method
	 */
	public void ejbPostCreate(RunParticipationDTO runParticipationDTO) throws CreateException {
		// do nothing
	}

	/**
	 * Removes the run participation from the database.
	 * 
	 * @throws RemoveException
	 *             when there is an error removing the run participation from
	 *             the database
	 */
	public void ejbRemove() throws RemoveException {
		RunParticipationEntityPK pk = (RunParticipationEntityPK) entityContext.getPrimaryKey();
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("DELETE FROM RunParticipation WHERE (UserId = ?) AND (RunId = ?)");
				try {
					statement.setString(1, pk.userId);
					statement.setInt(2, pk.runId);

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
	 * Loads the fields of the RunParticipationEntity object with data from the
	 * database.
	 * 
	 * <p>
	 * This method should not be called by the application directly, but is
	 * called by the application server.
	 */
	public void ejbLoad() {
		RunParticipationEntityPK pk = (RunParticipationEntityPK) entityContext.getPrimaryKey();
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				// PreparedStatement statement = connection.prepareStatement(
				// "SELECT ActiveRole, RoleId FROM RunParticipationView WHERE
				// (UserId = ?) AND (RunId = ?)");

				// modified to increase HSQL performance
				PreparedStatement statement = connection
						.prepareStatement("SELECT RunParticipation.ActiveRole, RoleInstance.RoleId FROM RunParticipation LEFT OUTER JOIN RoleInstance ON RunParticipation.ActiveRole = RoleInstance.RoleInstanceId WHERE (RunParticipation.UserId = ?) AND (RunParticipation.RunId = ?)");

				try {
					statement.setString(1, pk.userId);
					statement.setInt(2, pk.runId);
					ResultSet resultSet = statement.executeQuery();

					if (!resultSet.next()) {
						throw new RemoteException("Could not find " + pk);
					}

					// this.userId = pk.userId;
					// this.runId = pk.runId;

					// create the DTO according to the data loaded
					dto = new RunParticipationDTO(pk.userId, pk.runId, resultSet.getInt(1), resultSet.getString(2));
					modified = false;
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
		} catch (Exception e) {
			throw new EJBException(pk + " failed to load from database", e);
		}
	}

	/**
	 * Stores the fields of the RunParticipationEntity object in the database.
	 * 
	 * <p>
	 * This method should not be called by the application directly, but is
	 * called by the application server.
	 */
	public void ejbStore() {
		if (!modified)
			return;

		RunParticipationEntityPK pk = (RunParticipationEntityPK) entityContext.getPrimaryKey();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("UPDATE RunParticipation SET activeRole=? WHERE (userId=?) AND (runId = ?)");
				try {
					statement.setInt(1, dto.getActiveRole());
					statement.setString(2, pk.userId);
					statement.setInt(3, pk.runId);
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
			throw new EJBException(ex.getMessage());
		}
	}

	public void ejbActivate() {
		// do nothing
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
	 * Returns the primary key of the RunParticipationEntity for the given key.
	 * 
	 * <p>
	 * If the key cannot be located the method throws a FinderException.
	 * <p>
	 * This method is called by the J2EE application server, never call it
	 * directly
	 * 
	 * @param pk
	 *            RunParticipationEntityPK the key of the record to find
	 * @throws FinderException
	 *             if the key cannot be located
	 * @return RunParticipationEntityPK the key of the found
	 *         RunParticipationEntity
	 */
	public RunParticipationEntityPK ejbFindByPrimaryKey(RunParticipationEntityPK pk) throws FinderException {
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT RunId FROM RunParticipation WHERE (UserId = ?) AND (RunId = ?)");
				try {
					statement.setString(1, pk.userId);
					statement.setInt(2, pk.runId);
					ResultSet resultSet = statement.executeQuery();

					// check if bean can be loaded from database
					if (!resultSet.next())
						throw new FinderException("Could not find " + pk);

					// everything was ok, so return the primary key
					return pk;
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
		} catch (SQLException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Find all RunParticipations for the specified run.
	 * 
	 * <p>
	 * If no RunParticipations are found, the method returns an empty
	 * Collection.
	 * <p>
	 * This method is called by the J2EE application server, never call it
	 * directly
	 * 
	 * @param runId
	 *            the id of the run to find the runparticipations for
	 * @return a Collection of RunParticipationEntityPK
	 * @throws FinderException
	 *             required by spec, but is never thrown
	 */
	public Collection ejbFindByRunId(int runId) throws FinderException {
		Vector result = new Vector();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT UserId FROM RunParticipation WHERE RunId = ?");
				try {

					statement.setInt(1, runId);
					ResultSet rs = statement.executeQuery();
					while (rs.next()) {
						result.addElement(new RunParticipationEntityPK(runId, rs.getString(1)));
					}
					return result;

				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
		} catch (SQLException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Find all RunParticipations based on their participation in role
	 * represented by the passed role instance id and the passed run id and return their primary keys
	 * <p>
	 * This method is called by the J2EE application server, never call it
	 * directly
	 * 
	 * @param runId
	 *            int representing the run id
	 * @param roleInstanceId
	 *            int representing the role instance id
	 * @throws FinderException
	 * @return Collection containing the primary keys of all found run participations for the
	 *         specified runId and roleInstanceId, or an empty Collection if none are found.
	 */
	public Collection ejbFindByRoleInstanceId(int runId, int roleInstanceId) throws FinderException {
		ArrayList pks = new ArrayList();

		// modified to increase HSQL performance
		String sqlStatement = "SELECT RunParticipation.UserId FROM RunParticipation INNER JOIN RoleParticipation ON RoleParticipation.UserId = RunParticipation.UserId WHERE (RunParticipation.RunId=?) AND (RoleParticipation.RoleInstanceId=?)";

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection.prepareStatement(sqlStatement);
				try {
					statement.setInt(1, runId);
					statement.setInt(2, roleInstanceId);
					ResultSet rs = statement.executeQuery();

					while (rs.next()) {
						pks.add(new RunParticipationEntityPK(runId, rs.getString(1)));
					}
					// everything was ok, so return the primary keys
					return pks;
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
		} catch (SQLException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Finds all RunParticipations for the given user.
	 * 
	 * <p>
	 * If no RunParticipations are found, the method returns an empty
	 * Collection.
	 * <p>
	 * This method is called by the J2EE application server, never call it
	 * directly
	 * 
	 * @param userId
	 *            String the user for whom all RunParticipations are searched
	 * @throws FinderException
	 *             required by the spec, but is never thrown
	 * @return Collection all RunParticipationsEntityPK's for the specified
	 *         user, or an empty Collection if none are found
	 */
	public Collection ejbFindByUserId(String userId) throws FinderException {
		Vector result = new Vector();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT RunId FROM RunParticipation WHERE UserId = ?");
				try {

					statement.setString(1, userId);
					ResultSet rs = statement.executeQuery();
					while (rs.next()) {
						result.addElement(new RunParticipationEntityPK(rs.getInt(1), userId));
					}
					return result;
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
		} catch (SQLException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Finds all RunParticipations in the given unit of learning for the given
	 * user.
	 * 
	 * <p>
	 * If no RunParticipations are found, the method returns an empty
	 * Collection.
	 * <p>
	 * This method is called by the J2EE application server, never call it
	 * directly
	 * 
	 * @param userId
	 *            String the user for whom all RunParticipations are searched
	 * @param uolId
	 *            int the id of the unit of learning the RunParticipations
	 *            should be a part of
	 * @throws FinderException
	 *             required by the spec, but is never thrown
	 * @return Collection all RunParticipationsEntityPK's for the specified
	 *         user, or an empty Collection if none are found
	 */
	public Collection ejbFindByUserId(String userId, int uolId) throws FinderException {
		Vector result = new Vector();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				// PreparedStatement statement = connection.prepareStatement(
				// "SELECT RunId FROM RunParticipationView WHERE (UserId = ?)
				// AND (UolId = ?)");

				// modified to increase HSQL performance
				PreparedStatement statement = connection
						.prepareStatement("SELECT RunParticipation.RunId FROM RunParticipation INNER JOIN Run ON RunParticipation.RunId = Run.RunId WHERE (RunParticipation.UserId = ?) AND (Run.UolId = ?)");

				try {

					statement.setString(1, userId);
					statement.setInt(2, uolId);
					ResultSet rs = statement.executeQuery();
					while (rs.next()) {
						result.addElement(new RunParticipationEntityPK(rs.getInt(1), userId));
					}
					return result;
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
		} catch (SQLException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Returns the Data Transfer Object.
	 * 
	 * @return RunParticipationDTO the dto of this RunParticipationEntity
	 * @see #setDto
	 */
	public RunParticipationDTO getDto() {
		return dto;
	}

	/**
	 * Sets the dto member.
	 * 
	 * @param dto
	 *            RunParticipationDTO the new values of the dto
	 * @see #getDto
	 */
	public void setDto(RunParticipationDTO dto) {
		this.dto = dto;
		// setting the active role was not persisted anymore because modified
		// flag was never set
		modified = true;
	}

}
