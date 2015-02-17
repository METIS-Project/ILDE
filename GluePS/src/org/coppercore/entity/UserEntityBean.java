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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

/**
 * The UserEntityBean is the persistence component of the User.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.8 $, $Date: 2007/04/04 09:04:11 $
 */
public class UserEntityBean implements EntityBean {
	private static final long serialVersionUID = 42L;
	private EntityContext entityContext;
	private String userId;
	private boolean modified = true;

	/**
	 * Creates a new UserEntity record in the database.
	 * 
	 * @param aUserId
	 *            a String object identifying the user
	 * @return the primary key of this user, which is the userId
	 * @throws CreateException
	 *             if the new record cannot be created in the database
	 */
	public String ejbCreate(java.lang.String aUserId) throws CreateException {
		setUserId(aUserId);

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection.prepareStatement("INSERT INTO LDUser (UserID) VALUES (?)");
				try {
					statement.setString(1, aUserId);
					if (statement.executeUpdate() != 1) {
						throw new CreateException("Could not create UserEntity: " + aUserId);
					}
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
			modified = false;
			return aUserId;
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
	 * @param aUserId
	 *            String the identifier of the new user
	 * @throws CreateException
	 *             when there would be an error in this method
	 */
	public void ejbPostCreate(String aUserId) throws CreateException {
		// do nothing
	}

	public void ejbRemove() throws RemoveException {
		String pk = (String) entityContext.getPrimaryKey();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection.prepareStatement("DELETE FROM LDUser WHERE (UserId = ?)");
				try {
					statement.setString(1, pk);

					if (statement.executeUpdate() == 0) {
						throw new RemoveException("Could not delete user[" + pk + "]");
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
	 * Sets the id of this User to the specified value.
	 * 
	 * @param userId
	 *            String the new id of the user
	 * @see #getUserId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
		modified = true;
	}

	/**
	 * Returns the id of this user.
	 * 
	 * @return String the id of this user.
	 * @see #setUserId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Finds the record in the database by looking up the specified userId. The
	 * method returns the primary key of the record to indicate the found
	 * record. <br>
	 * If the record cannot be located the method throws a FinderException.
	 * 
	 * @param aUserId
	 *            the primary key of the record to find
	 * @return the primary key of the record, being the userId
	 * @throws FinderException
	 *             if the record cannot be located in the database
	 */
	public String ejbFindByPrimaryKey(String aUserId) throws FinderException {
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT UserId FROM LDUser WHERE (UserId = ?)");
				try {
					statement.setString(1, aUserId);
					ResultSet resultSet = statement.executeQuery();
					// check if bean can be loaded from database
					if (!resultSet.next())
						throw new FinderException("Could not find User: " + aUserId);
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
			// everything was ok, so return the primary key
			return aUserId;
		} catch (SQLException sqle) {
			throw new EJBException(sqle);
		}
	}

	/**
	 * Returns a Collection containing the userId's of all users in the
	 * database.
	 * <p>
	 * These userId's are the primary keys that enables the ejb container to
	 * create a collection of UserEntity objects to return to the caller.<br>
	 * The method returns an empty collection if the are no users in the
	 * database.
	 * 
	 * @return a Collection containing the userId's of all users in the
	 *         database.
	 * @throws FinderException
	 *             if the
	 */
	public java.util.Collection ejbFindAllUsers() throws FinderException {
		Vector result = new Vector();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				Statement statement = connection.createStatement();
				try {
					ResultSet rs = statement.executeQuery("SELECT UserId FROM LDUser");
					while (rs.next()) {
						result.addElement(rs.getString(1));
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
	 * Finds user based on their roleparticipation. The roleid passed is a space
	 * seperated list of LD role ids.
	 * 
	 * @param runId
	 *            int
	 * @param roleId
	 *            String
	 * @throws FinderException
	 * @return Collection
	 */
	public Collection ejbFindByRoleId(int runId, String roleId) throws FinderException {
		ArrayList pks = new ArrayList();
		String[] roleIds = roleId.split(" ");
		int length = roleIds.length;
		// String sqlStatement = "SELECT UserId FROM RoleParticipationView WHERE
		// (RunId=?) AND (";

		// modified to increase HSQL performance
		String sqlStatement = "SELECT RoleParticipation.UserId FROM RoleInstance INNER JOIN RoleParticipation ON RoleInstance.RoleInstanceId = RoleParticipation.RoleInstanceId WHERE (RoleInstance.RunId=?) AND (";

		try {
			Connection connection = Util.getInstance().getConnection();
			try {

				// create a statement with the right number of or conditions
				// (one for each passed roleId)
				for (int i = 0; i < length; i++) {
					if ((i + 1) < length) {
						// sqlStatement += "(RoleId = ?) OR";
						sqlStatement += "(RoleInstance.RoleId = ?) OR";
					} else {
						// sqlStatement += "(RoleId = ?)";
						sqlStatement += "(RoleInstance.RoleId = ?)";
					}
				}
				sqlStatement += ")";

				PreparedStatement statement = connection.prepareStatement(sqlStatement);
				try {
					statement.setInt(1, runId);
					// fill the passed parameters
					for (int i = 0; i < length; i++) {
						statement.setString((i + 2), roleIds[i]);
					}
					ResultSet resultSet = statement.executeQuery();

					while (resultSet.next()) {
						pks.add(resultSet.getString(1));
					}
					// everything was ok, so return the primary key
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

	// FIXED 2006-12-05: added a new finder method to distinct between searches
	// for LD role ids and
	// role instance ids.

	/**
	 * Finds user based on their participation in roles represented by the
	 * passed role instance ids. The ids are passed as a space seperated list of
	 * role instance ids.
	 * 
	 * @param runId
	 *            int
	 * @param roleInstanceIds
	 *            String space separated String of role instance ids
	 * @throws FinderException
	 * @return Collection
	 */
	public Collection ejbFindByRoleInstanceId(int runId, String roleInstanceIds) throws FinderException {
		ArrayList pks = new ArrayList();
		String[] roleIds = roleInstanceIds.split(" ");
		int length = roleIds.length;
		// String sqlStatement = "SELECT UserId FROM RoleParticipationView WHERE
		// (RunId=?) AND (";

		// modified to increase HSQL performance
		String sqlStatement = "SELECT RoleParticipation.UserId FROM RoleInstance INNER JOIN RoleParticipation ON RoleInstance.RoleInstanceId = RoleParticipation.RoleInstanceId WHERE (RoleInstance.RunId=?) AND (";

		try {
			Connection connection = Util.getInstance().getConnection();
			try {

				// create a statement with the right number of or conditions
				// (one for each passed roleId)
				for (int i = 0; i < length; i++) {
					if ((i + 1) < length) {
						// sqlStatement += "(RoleId = ?) OR";
						sqlStatement += "(RoleInstance.RoleInstanceId = ?) OR";
					} else {
						// sqlStatement += "(RoleId = ?)";
						sqlStatement += "(RoleInstance.RoleInstanceId = ?)";
					}
				}
				sqlStatement += ")";

				PreparedStatement statement = connection.prepareStatement(sqlStatement);
				try {
					statement.setInt(1, runId);
					// fill the passed parameters
					for (int i = 0; i < length; i++) {
						statement.setString((i + 2), roleIds[i]);
					}
					ResultSet resultSet = statement.executeQuery();

					while (resultSet.next()) {
						pks.add(resultSet.getString(1));
					}
					// everything was ok, so return the primary key
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
	 * Loads the UserEntity object with data from the database.
	 * <p>
	 * Because the UserEntity for the moment only exists of a primary key, no
	 * direct database access is required.
	 */
	public void ejbLoad() {
		setUserId((String) entityContext.getPrimaryKey());
	}

	/**
	 * Nothing do for now, because the UserEntity exists only of a
	 * <code>userId</code> which is the primary key, so it gets only stored in
	 * the <code>ejbCreate()</code>.
	 */
	public void ejbStore() {
		if (!modified)
			return;
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
}
