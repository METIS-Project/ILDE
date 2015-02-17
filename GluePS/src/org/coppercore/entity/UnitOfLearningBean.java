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
import java.sql.Statement;
import java.util.Collection;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.coppercore.dto.UnitOfLearningPK;
import org.coppercore.dto.UolDto;

/**
 * Implements the database access component for the User.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.8 $, $Date: 2007/04/04 09:04:11 $
 */
public class UnitOfLearningBean implements EntityBean {
	private static final long serialVersionUID = 42L;
	private EntityContext entityContext;
	// int id;
	private UolDto dto = null;
	private boolean modified = false;

	/**
	 * Creates a new UnitOfLearning entity in the database, based on the
	 * specified parameters.
	 * 
	 * @param uolDto
	 *            UolDto a data transfer object defining all creation paramters
	 * @throws CreateException
	 *             when the entity could not be created
	 * @return UnitOfLearningPK the primary key of the newly created
	 *         UnitOfLearning
	 */
	public UnitOfLearningPK ejbCreate(UolDto uolDto) throws CreateException {
		dto = uolDto;

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				String sql = "INSERT INTO UnitOfLearning (URI,Title,RolesId, ContentUri) VALUES (?,?,?,?)";
				PreparedStatement statement = connection.prepareStatement(sql);
				try {
					statement.setString(1, getUolDto().getUri());
					statement.setString(2, getUolDto().getTitle());
					statement.setString(3, getUolDto().getRolesId());
					statement.setString(4, getUolDto().getContentUri());

					if (statement.executeUpdate() != 1) {
						throw new CreateException("Could not create UnitOfLearning: " + uolDto);
					}

					int id = Util.getInstance().getIdentity(connection, "UnitOfLearning", "id");
					if (id != -1) {
						dto.setId(id);
						modified = false;
						return new UnitOfLearningPK(id);
					}
					throw new CreateException("Could not create UnitOfLearning: " + uolDto);
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
	 * created. For the UserEntity this method does nothing.
	 * 
	 * @param uolDto
	 *            UolDto a data transfer object defining all creation paramters
	 * @throws CreateException
	 *             is never thrown
	 */
	public void ejbPostCreate(UolDto uolDto) throws CreateException {
		// do nothing
	}

	/**
	 * Removes the UserEntity from the database.
	 * 
	 * @throws RemoveException
	 *             if there is an error deleting the unit of learning from the
	 *             database
	 */
	public void ejbRemove() throws RemoveException {
		UnitOfLearningPK pk = (UnitOfLearningPK) entityContext.getPrimaryKey();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection.prepareStatement("DELETE FROM UnitOfLearning WHERE (Id = ?)");
				try {
					statement.setInt(1, pk.id);

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
	 * @param uolDto
	 *            UolDto the new values for the dto
	 * @see #getUolDto
	 */
	public void setUolDto(UolDto uolDto) {
		this.dto = uolDto;
		modified = true;
	}

	/**
	 * Returns a data transfer object containing the parameters for this unit of
	 * learning.
	 * 
	 * @return UolDto the dto containing the parameters of this unit of learning
	 * @see #setUolDto
	 */
	public UolDto getUolDto() {
		return dto;
	}

	public void ejbLoad() {
		int id = ((UnitOfLearningPK) entityContext.getPrimaryKey()).id;
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				// retreive data from database
				PreparedStatement statement = connection
						.prepareStatement("SELECT URI,Title,RolesId,ContentUri FROM UnitOfLearning WHERE Id = ?");
				try {
					statement.setInt(1, id);
					ResultSet resultSet = statement.executeQuery();

					if (!resultSet.next()) {
						throw new RemoteException("ejbLoad: UnitOfLearning not found: " + id);
					}

					// Store our data
					dto = new UolDto(id, resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
							resultSet.getString(4));
					modified = false;
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
		} catch (Exception e) {
			throw new EJBException(id + " failed to load from database", e);
		}
	}

	public void ejbStore() {
		if (!modified)
			return;
		
		int id = ((UnitOfLearningPK) entityContext.getPrimaryKey()).id;
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("UPDATE UnitOfLearning SET Title=?, RolesId=?, ContentUri=? WHERE Id=?");
				try {
					statement.setString(1, dto.getTitle());
					statement.setString(2, dto.getRolesId());
					statement.setString(3, dto.getContentUri());
					statement.setInt(4, id);
					if (statement.executeUpdate() != 1) {
						throw new EJBException("Could not update UnitOfLearning: " + id);
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
	 * Finds the unit of learning matching the specified uri.
	 * 
	 * <p>
	 * The uri is the unique identifier assigned to the unit of learning in the
	 * learning design manifest. A uri should be world-wide unique. Only one
	 * unit of learning in the system can have this id because all units of
	 * learning with the same id are considered the same and will overwrite each
	 * other during publication.
	 * 
	 * @param uri
	 *            String the uri to search for
	 * @throws FinderException
	 *             if the unit of learning with the specified uri could not be
	 *             locatde in the database
	 * @return UnitOfLearningPK the primary key of the unit of learning found
	 */
	public UnitOfLearningPK ejbFindByURI(String uri) throws FinderException {
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT Id FROM UnitOfLearning WHERE URI = ?");
				try {
					statement.setString(1, uri);
					ResultSet resultSet = statement.executeQuery();

					// check if bean can be loaded from database
					if (!resultSet.next())
						throw new FinderException("Could not find UnitOfLearning: " + uri);

					// Get the id
					int id = resultSet.getInt(1);

					// everything was ok, so return the primary key
					return new UnitOfLearningPK(id);
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
	 * Returns a Collection containing the UnitOfLearningPK's of all
	 * UnitOfLearnings in the database.
	 * 
	 * <p>
	 * These UnitOfLearningPK's are the primary keys that enables the ejb
	 * container to create a collection of UnitOfLearning objects to return to
	 * the caller. <br>
	 * The method returns an empty collection if the are no UnitOfLearnings in
	 * the database.
	 * 
	 * @return a Collection containing the UolId's of all UnitOfLearnings in the
	 *         database or an empty Collection if no unit of learning is found
	 * @throws FinderException
	 *             is never thrown, but required by the spec
	 */
	public java.util.Collection ejbFindAllUnitOfLearnings() throws FinderException {
		Vector result = new Vector();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				Statement statement = connection.createStatement();
				try {
					ResultSet rs = statement.executeQuery("SELECT Id FROM UnitOfLearning");
					while (rs.next()) {
						result.addElement(new UnitOfLearningPK(rs.getInt(1)));
					}
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}

			// everything was ok, so return the collection of primary keys.
			return result;
		} catch (SQLException ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Return a collection of all UnitOfLearning pk's found for the specified
	 * user.
	 * 
	 * <p>
	 * If no unit of learning is found for the user the method returns an empty
	 * collection.
	 * 
	 * @param userId
	 *            is the id of user who's uols will be retrieved
	 * @return a collection of all UnitOfLearning pk's found for the specified
	 *         user.
	 * @throws FinderException
	 *             is never thrown, but required by the spec
	 */
	public Collection ejbFindByUser(String userId) throws FinderException {
		Vector result = new Vector();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				// PreparedStatement statement = connection.prepareStatement(
				// "SELECT DISTINCT UolId FROM RunParticipationView WHERE
				// (UserId = ?)");

				// modified to increase HSQL performance
				PreparedStatement statement = connection
						.prepareStatement("SELECT DISTINCT Run.UolId FROM RunParticipation INNER JOIN Run ON RunParticipation.RunId = Run.RunId WHERE (RunParticipation.UserId = ?)");

				try {
					statement.setString(1, userId);
					ResultSet rs = statement.executeQuery();

					while (rs.next()) {
						result.addElement(new UnitOfLearningPK(rs.getInt(1)));
					}
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}

			// everything was ok, so return the collection of primary keys.
			return result;
		} catch (SQLException ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Finds the unit of learning matching the specified primary key.
	 * 
	 * @param pk
	 *            UnitOfLearningPK the primary key to search for
	 * @throws FinderException
	 *             if the unit of learning with the specified primary key could
	 *             not be located in the database
	 * @return UnitOfLearningPK the primary key of the unit of learning found
	 */
	public UnitOfLearningPK ejbFindByPrimaryKey(UnitOfLearningPK pk) throws FinderException {
		int id = pk.id;

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection.prepareStatement("SELECT Id FROM UnitOfLearning WHERE Id = ?");
				try {
					statement.setInt(1, id);
					ResultSet resultSet = statement.executeQuery();

					// check if bean can be loaded from database
					if (!resultSet.next()) {
						throw new FinderException("Could not find UnitOfLearning: " + id);
					}
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
			return new UnitOfLearningPK(id);
		} catch (SQLException ex) {
			throw new EJBException(ex);
		}
	}

}
