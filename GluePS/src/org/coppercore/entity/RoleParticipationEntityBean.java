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

package org.coppercore.entity;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;
import org.coppercore.dto.RoleParticipationDto;
import org.coppercore.dto.RoleParticipationEntityPK;

/**
 * This class implements a persistence component for the
 * RoleParticipationEntity.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.8 $, $Date: 2007/04/04 09:04:11 $
 */
public class RoleParticipationEntityBean implements EntityBean {
	private static final long serialVersionUID = 42L;

	private EntityContext entityContext;
	// private String userId;
	// private int roleInstanceId;
	private RoleParticipationDto dto;

	/**
	 * Creates a new RoleParticipation entity in the database, based on the
	 * specified parameters.
	 * 
	 * <p>
	 * A RoleParticipation couples a user to a role instance. The role instance
	 * is either the ld role itself or a new created instance of this role. New
	 * instances can be created for roles that have the create-new attribute set
	 * to allowed
	 * 
	 * @param aDto
	 *            RoleParticipationDto a data transfer object defining all
	 *            creation parameters
	 * @throws CreateException
	 *             when the entity could not be created
	 * @return RoleParticipationEntityPK the primary key for the new created
	 *         entity
	 */
	public RoleParticipationEntityPK ejbCreate(RoleParticipationDto aDto) throws CreateException {
		// userId = aDto.getUserId();
		// roleInstanceId = aDto.getRoleInstanceId();
		setDto(aDto);
		RoleParticipationEntityPK pk = new RoleParticipationEntityPK(aDto.getUserId(), aDto.getRoleInstanceId());
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("INSERT INTO RoleParticipation (UserID, RoleInstanceId) VALUES (?,?)");
				try {
					statement.setString(1, pk.userId);
					statement.setInt(2, pk.roleInstanceId);
					if (statement.executeUpdate() != 1) {
						throw new CreateException("Could not create " + pk);
					}
					// statement = connection.prepareStatement(
					// "SELECT RoleId, RunId FROM RoleParticipationView WHERE
					// (UserId = ?) AND (RoleInstanceId=?)");

					// modified to increase HSQL performance
					statement = connection
							.prepareStatement("SELECT RoleInstance.RoleId, RoleInstance.RunId FROM RoleInstance INNER JOIN RoleParticipation ON RoleInstance.RoleInstanceId = RoleParticipation.RoleInstanceId WHERE (RoleParticipation.UserId = ?) AND (RoleParticipation.RoleInstanceId=?)");

					statement.setString(1, dto.getUserId());
					statement.setInt(2, dto.getRoleInstanceId());
					ResultSet resultSet = statement.executeQuery();
					if (!resultSet.next()) {
						throw new CreateException("Could not create " + pk);
					}
					this.dto.setRoleId(resultSet.getString(1));
					this.dto.setRunId(resultSet.getInt(2));
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
	 * created. For the RoleParticipationEntity this method does nothing.
	 * 
	 * @param dto
	 *            RoleParticipationDto a data transfer object defining all
	 *            creation paramters
	 * @throws CreateException
	 *             is never thrown
	 */
	public void ejbPostCreate(RoleParticipationDto aDto) throws CreateException {
		// do nothing
	}

	/**
	 * Removes the RoleParticipationEntity from the database.
	 * 
	 * @throws RemoveException
	 *             if there is an error deleting the entity from the database
	 */
	public void ejbRemove() throws RemoveException {
		RoleParticipationEntityPK pk = (RoleParticipationEntityPK) entityContext.getPrimaryKey();
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("DELETE FROM RoleParticipation WHERE (UserId = ?) AND (RoleInstanceId = ?)");
				try {
					statement.setString(1, pk.userId);
					statement.setInt(2, pk.roleInstanceId);

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
	 * Uses a data transfer object to set all members of this instance to the
	 * specified values.
	 * 
	 * @param dto
	 *            RoleParticipationDto the data transfer object specifiying the
	 *            new values
	 * @see #getDto
	 */
	public void setDto(RoleParticipationDto dto) {
		this.dto = dto;
	}

	/**
	 * Returns a RoleParticipationDto data transfer object containing all
	 * relevant parameters of this instance.
	 * 
	 * @return RoleParticipationDto the data transfer object describing this
	 *         instance
	 * @see #setDto
	 */
	public RoleParticipationDto getDto() {
		return dto;
	}

	/**
	 * Loads the data for this RoleParticipationEntity from the database.
	 */
	public void ejbLoad() {
		RoleParticipationEntityPK pk = (RoleParticipationEntityPK) entityContext.getPrimaryKey();
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				// PreparedStatement statement = connection.prepareStatement(
				// "SELECT RoleId, RunId FROM RoleParticipationView WHERE
				// (UserId = ?) AND (RoleInstanceId=?)");

				// modified to increase HSQL performance
				PreparedStatement statement = connection
						.prepareStatement("SELECT RoleId, RunId FROM RoleInstance WHERE (RoleInstanceId=?)");

				try {
					statement.setInt(1, pk.roleInstanceId);
					ResultSet resultSet = statement.executeQuery();
					if (!resultSet.next()) {
						throw new RemoteException("Could not find " + pk);
					}
					// setUserId(pk.userId);
					// setRoleInstanceId(pk.roleInstanceId);
					dto = new RoleParticipationDto(pk.userId, pk.roleInstanceId, resultSet.getString(1), resultSet
							.getInt(2));
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

	public void ejbStore() {
		// Nothing to do, userId and roleInstanceId are already stored as
		// primary key in the
		// ejbCreate and they cannot be changed. The other members, roleId and
		// runId are
		// derived directly from the database and cannot be changed either.
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
	 * Returns a Collection of RoleParticipationEntityPK's for all
	 * RoleParticipations of the specified user in the specified run.
	 * 
	 * <p>
	 * This list defines all roles the user is assigned to in the run. If the
	 * user is not assigned to any run, this methos returns an empty Collection.
	 * 
	 * @param anUserId
	 *            String the id of the user to look the RoleParticipation up for
	 * @param aRunId
	 *            int the id of the run where to look.
	 * @throws FinderException
	 *             is nver thrown
	 * @return Collection of RoleParticipationEntityPK's for all role
	 *         participations this user is assigned to, or an empty Collection
	 *         if the user is not assigned to any role in this run.
	 */
	public Collection ejbFindByUserId(String anUserId, int aRunId) throws FinderException {
		Vector result = new Vector();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				// PreparedStatement statement = connection.prepareStatement(
				// "SELECT RoleInstanceId FROM RoleParticipationView WHERE
				// (UserId = ?) AND (RunId = ?)");

				// modified to increase HSQL performance
				PreparedStatement statement = connection
						.prepareStatement("SELECT RoleInstance.RoleInstanceId FROM RoleInstance INNER JOIN RoleParticipation ON RoleInstance.RoleInstanceId = RoleParticipation.RoleInstanceId WHERE (RoleParticipation.UserId = ?) AND (RoleInstance.RunId=?)");

				try {
					statement.setString(1, anUserId);
					statement.setInt(2, aRunId);
					ResultSet rs = statement.executeQuery();

					while (rs.next()) {
						// Construct a primary key from the data, and add it to
						// the result
						result.addElement(new RoleParticipationEntityPK(anUserId, rs.getInt(1)));
					}

					// everything was ok, so return the collection of primary
					// keys.
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
	 * Checks if an entity with the given key actually exists in the database.
	 * If the entity is found, the method returns the primary key of the found
	 * entity, else the method throws a FinderException.
	 * 
	 * @param pk
	 *            RoleParticipationEntityPK the primary key of the
	 *            RoleParticipationEntity to look for.
	 * @throws FinderException
	 *             when no entity with the given key is found
	 * @return RoleParticipationEntityPK the primary key of the found entity.
	 */
	public RoleParticipationEntityPK ejbFindByPrimaryKey(RoleParticipationEntityPK pk) throws FinderException {
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT RoleInstanceId FROM RoleParticipation WHERE (UserId = ?) AND (RoleInstanceId = ?)");
				try {
					statement.setString(1, pk.userId);
					statement.setInt(2, pk.roleInstanceId);
					ResultSet resultSet = statement.executeQuery();

					// check if bean can be loaded from database
					if (!resultSet.next())
						throw new FinderException("Could not find RoleParticipation: " + pk);

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
	 * Finds the primary key for the participation where the user is assigned to
	 * an instance of the specified learning design role in the given run.
	 * 
	 * <p>
	 * If no participation is found a FinderException is thrown.
	 * 
	 * <p>
	 * A user might be assigned to more than one instance of an ld role but only
	 * one is returned.
	 * 
	 * @param aRunId
	 *            int the id of the run this role participatiomn is bound to
	 * @param aUserId
	 *            String the id of the user to look the participation for
	 * @param aRoleId
	 *            String the learning design role id of the instance to look for
	 * @throws FinderException
	 *             when no participation can be found for the given user, role
	 *             in the specified run
	 * @return RoleParticipationEntityPK the primary key of the
	 *         RoleParticaptionEntity that meets the search criteria
	 */
	public RoleParticipationEntityPK ejbFindByUserRole(int aRunId, String aUserId, String aRoleId)
			throws FinderException {
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				// PreparedStatement statement = connection.prepareStatement(
				// "SELECT RoleInstanceId FROM RoleParticipationView WHERE
				// (UserId = ?) AND (RoleId = ?) AND (RunId= ?)");

				// modified to increase HSQL performance
				PreparedStatement statement = connection
						.prepareStatement("SELECT RoleInstance.RoleInstanceId FROM RoleInstance INNER JOIN RoleParticipation ON RoleInstance.RoleInstanceId = RoleParticipation.RoleInstanceId WHERE (RoleParticipation.UserId = ?) AND (RoleInstance.RoleId = ?) AND (RoleInstance.RunId=?)");

				try {
					statement.setString(1, aUserId);
					statement.setString(2, aRoleId);
					statement.setInt(3, aRunId);
					ResultSet resultSet = statement.executeQuery();

					// check if bean can be loaded from database
					if (!resultSet.next())
						throw new FinderException("Could not find RoleParticipation: " + aUserId + "," + aRoleId + ","
								+ aRunId);

					// read the key
					int id = resultSet.getInt(1);

					// everything was ok, so return the primary key
					return new RoleParticipationEntityPK(aUserId, id);

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
}
