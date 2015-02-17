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

import org.coppercore.dto.RoleInstanceDto;
import org.coppercore.dto.RoleInstanceEntityPK;

// TODO: Auto-generated Javadoc
/**
 * This class implements the persistence component for the RoleInstance.
 * 
 * <p>
 * For all roles in the learning design one RoleInstance is created when a run
 * is created. For roles having the create-new attribute set to allowed more
 * instances can be created.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.8 $, $Date: 2007/04/04 09:04:10 $
 */
public class RoleInstanceEntityBean implements EntityBean {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 42L;

	/** The entity context. */
	private EntityContext entityContext;

	/** The role instance id. */
	private int roleInstanceId;

	/** The _role instance dto. */
	private RoleInstanceDto _roleInstanceDto;
	
	private boolean modified = false;

	/**
	 * Creates a new RoleInstance entity in the database, based on the specified
	 * parameters.
	 * 
	 * @param roleInstanceDto
	 *            RoleInstanceDto a data transfer object defining all creation
	 *            parameters
	 * 
	 * @return RoleInstanceEntityPK the primary key for the new created entity
	 * 
	 * @throws CreateException
	 *             when the entity could not be created
	 */
	public RoleInstanceEntityPK ejbCreate(RoleInstanceDto roleInstanceDto) throws CreateException {

		setRoleInstanceDto(roleInstanceDto);

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				String sql = "INSERT INTO RoleInstance (RoleId, RunId) VALUES (?,?)";
				PreparedStatement statement = connection.prepareStatement(sql);
				try {
					statement.setString(1, getRoleInstanceDto().getRoleId());
					statement.setInt(2, getRoleInstanceDto().getRunId());

					if (statement.executeUpdate() != 1) {
						throw new CreateException("Could not create new RoleInstanceEntity for "
								+ getRoleInstanceDto().getRoleId());
					}

					roleInstanceId = Util.getInstance().getIdentity(connection, "RoleInstance", "RoleInstanceId");
					if (roleInstanceId != -1) {
						getRoleInstanceDto().setRoleInstanceId(roleInstanceId);
						modified = false;
						return new RoleInstanceEntityPK(roleInstanceId);
					}
					throw new CreateException("Could not create new RoleInstanceEntity for "
							+ getRoleInstanceDto().getRoleId());
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
	 * created. For the RoleInstanceEntity this method does nothing.
	 * 
	 * @param roleInstanceDto
	 *            RoleInstanceDto a data transfer object defining all creation
	 *            paramters
	 * 
	 * @throws CreateException
	 *             is never thrown
	 */
	public void ejbPostCreate(RoleInstanceDto roleInstanceDto) throws CreateException {
		// do nothing
	}

	/**
	 * Removes the RoleInstanceEntity from the database.
	 * 
	 * @throws RemoveException
	 *             if there is an error deleting the role instance from the
	 *             database
	 */
	public void ejbRemove() throws RemoveException {
		RoleInstanceEntityPK pk = (RoleInstanceEntityPK) entityContext.getPrimaryKey();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("DELETE FROM RoleInstance WHERE (RoleInstanceId = ?)");
				try {
					statement.setInt(1, pk.roleInstanceId);

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
	 * Sets the data transfer object to the specified value.
	 * 
	 * @param roleInstanceDto
	 *            RoleInstanceDto the new values for the dto
	 * 
	 * @see #getRoleInstanceDto
	 */
	public void setRoleInstanceDto(RoleInstanceDto roleInstanceDto) {
		this._roleInstanceDto = roleInstanceDto;
		modified = true;
	}

	/**
	 * Returns a data transfer object containing all parameters of this role
	 * instance.
	 * 
	 * @return RoleInstanceDto the data transfer object containing all members
	 * 
	 * @see #setRoleInstanceDto
	 */
	public RoleInstanceDto getRoleInstanceDto() {
		return _roleInstanceDto;
	}

	/**
	 * Loads the data of this role instance from the database.
	 */
	public void ejbLoad() {
		RoleInstanceEntityPK pk = ((RoleInstanceEntityPK) entityContext.getPrimaryKey());
		roleInstanceId = pk.roleInstanceId;
		try {
			Connection connection = Util.getInstance().getConnection();
			try {

				// retrieve data from database
				PreparedStatement statement = connection
						.prepareStatement("SELECT RoleId, RunId FROM RoleInstance WHERE RoleInstanceId = ?");
				try {
					statement.setInt(1, roleInstanceId);
					ResultSet resultSet = statement.executeQuery();

					if (!resultSet.next()) {
						throw new RemoteException(pk + " not found");
					}
					// Store our data
					_roleInstanceDto = new RoleInstanceDto(roleInstanceId, resultSet.getString(1), resultSet.getInt(2));
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
	 * Stores all data of this entity instance into the database.
	 */
	public void ejbStore() {
		if (!modified) 
			return;
		
		RoleInstanceEntityPK pk = ((RoleInstanceEntityPK) entityContext.getPrimaryKey());
		roleInstanceId = pk.roleInstanceId;
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("UPDATE RoleInstance SET RoleId=?, RunId=? WHERE RoleInstanceId=?");
				try {
					statement.setString(1, _roleInstanceDto.getRoleId());
					statement.setInt(2, _roleInstanceDto.getRunId());
					statement.setInt(3, roleInstanceId);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.EntityBean#ejbActivate()
	 */
	public void ejbActivate() {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.EntityBean#ejbPassivate()
	 */
	public void ejbPassivate() {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.EntityBean#unsetEntityContext()
	 */
	public void unsetEntityContext() {
		this.entityContext = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.EntityBean#setEntityContext(javax.ejb.EntityContext)
	 */
	public void setEntityContext(EntityContext entityContext) {
		this.entityContext = entityContext;
	}

	/**
	 * Checks if a RoleInstanceEntity with the given primary key exists in the
	 * database.
	 * 
	 * <p>
	 * If the entity is found the method returns its primary key, else it throws
	 * a FinderExceptoin.
	 * 
	 * @param pk
	 *            RoleInstanceEntityPK the key of the RoleInstanceEntity to
	 *            locate
	 * 
	 * @return RoleInstanceEntityPK the primary key of the found entity
	 * 
	 * @throws FinderException
	 *             when the entity could not be located
	 */
	public RoleInstanceEntityPK ejbFindByPrimaryKey(RoleInstanceEntityPK pk) throws FinderException {

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT RoleInstanceId FROM RoleInstance WHERE RoleInstanceId = ?");
				try {
					statement.setInt(1, pk.roleInstanceId);
					ResultSet resultSet = statement.executeQuery();

					// check if bean can be loaded from database
					if (!resultSet.next()) {
						throw new FinderException("Could not find " + pk);
					}
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
			return pk;
		} catch (SQLException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Finds all primary keys of all instances of the specified role in the
	 * specified run.
	 * 
	 * <p>
	 * For all roles in the learning design one RoleInstance is created when a
	 * run is created. For roles having the create-new attribute set to allowed
	 * more instances can be created.
	 * 
	 * <p>
	 * If no instances are found, the method returns an empty Collection.
	 * 
	 * <p>
	 * The application server uses this Collection of primary keys to actually
	 * load the instances from the database.
	 * 
	 * @param runId
	 *            int the id of the run the role instances belong to
	 * @param roleId
	 *            String the learning design role id of the role these instances
	 *            are created for
	 * 
	 * @return Collection of RoleInstanceEntityPK's of the instances of the
	 *         specified role. If no instances are found the method returns an
	 *         empty Collection
	 * 
	 * @throws FinderException
	 *             never thrown, but required by the spec
	 */
	public Collection ejbFindByRoleId(int runId, String roleId) throws FinderException {
		Vector result = new Vector();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT RoleInstanceId FROM RoleInstance WHERE (RoleId = ?) AND (RunId = ?)");
				try {
					statement.setString(1, roleId);
					statement.setInt(2, runId);
					ResultSet rs = statement.executeQuery();

					while (rs.next()) {
						// Construct a primary key from the data, and add it to
						// the result
						result.addElement(new RoleInstanceEntityPK(rs.getInt(1)));
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

}
