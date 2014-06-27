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

package org.coppercore.dossier;

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

import org.coppercore.entity.Util;

/**
 * The PropertyLookUpEntityBean is the persistence class for storing properties
 * lookup records in the database.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.18 $, $Date: 2008/05/15 15:32:36 $
 */
public class PropertyLookUpEntityBean implements EntityBean {
	private static final long serialVersionUID = 42L;
	private EntityContext entityContext;

	private int uolId;
	private String propId;
	private int propDefPK;
	private String type;

	private boolean modified = false;

	/**
	 * Creates a new property lookup record in the lookup table in the database.
	 * 
	 * @param aUolId
	 *            int the id of the unit of learning the property belongs to
	 * @param aPropId
	 *            String the learning design id of the property
	 * @param aPropDefPK
	 *            int the primary key of the property definition of a property
	 * @param aType
	 *            String the data type of the property
	 * @throws CreateException
	 *             when there is an error creating the lookup record in tha
	 *             database
	 * @return PropertyLookUpEntityPK the primary key of the just created lookup
	 *         record
	 */
	public PropertyLookUpEntityPK ejbCreate(int aUolId, String aPropId, int aPropDefPK, String aType)
			throws CreateException {
		uolId = aUolId;
		propId = aPropId;

		propDefPK = aPropDefPK;
		type = aType;
		PropertyLookUpEntityPK pk = new PropertyLookUpEntityPK(aUolId, aPropId);

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("INSERT INTO PropertyLookup (UolId, PropId, PropDefForeignPK) VALUES (?,?,?)");
				try {
					statement.setInt(1, aUolId);
					statement.setString(2, aPropId);
					statement.setInt(3, aPropDefPK);

					if (statement.executeUpdate() != 1) {
						throw new CreateException("Could not create " + pk);
					}

					modified = false;
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
	 * created. For this class this method does nothing.
	 * 
	 * @param aUolId
	 *            int the id of the unit of learning the property belongs to
	 * @param aPropId
	 *            String the learning design id of the property
	 * @param aPropDefPK
	 *            int the primary key of the property definition of a property
	 * @param aType
	 *            String the data type of the property
	 * @throws CreateException
	 *             when there is an error creating the lookup record in tha
	 *             database
	 */
	public void ejbPostCreate(int aUolId, String aPropId, int aPropDefPK, String aType) throws CreateException {
		// do nothing
	}

	/**
	 * Removes this instance from the database.
	 * 
	 * @throws RemoveException
	 *             when there is an error removing the instance from the
	 *             database
	 */
	public void ejbRemove() throws RemoveException {
		PropertyLookUpEntityPK pk = (PropertyLookUpEntityPK) entityContext.getPrimaryKey();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("DELETE FROM PropertyLookup WHERE (UolId = ?) AND (PropId=?)");
				try {
					statement.setInt(1, pk.uolId);
					statement.setString(2, pk.propId);

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
	 * Loads the lookup record from the database.
	 */
	public void ejbLoad() {
		PropertyLookUpEntityPK pk = (PropertyLookUpEntityPK) entityContext.getPrimaryKey();
		try {
			Connection connection = Util.getInstance().getConnection();
			try {

				// PreparedStatement statement = connection.prepareStatement(
				// "SELECT PropDefForeignPK, DataType FROM PropertyLookUpView
				// WHERE (UolId = ?) AND (PropId=?)");

				// modified to increase HSQL performance
				// retreive data from database
				PreparedStatement statement = connection
						.prepareStatement("SELECT PropertyLookup.PropDefForeignPK, PropertyDefinition.DataType FROM PropertyLookup INNER JOIN PropertyDefinition ON PropertyLookup.PropDefForeignPK = PropertyDefinition.PK WHERE  (UolId = ?) AND (PropId=?)");
				try {
					statement.setInt(1, pk.uolId);
					statement.setString(2, pk.propId);
					ResultSet resultSet = statement.executeQuery();

					if (!resultSet.next()) {
						throw new RemoteException(pk + " failed to load from database");
					}

					// Store our data
					uolId = pk.uolId;
					propId = pk.propId;
					propDefPK = resultSet.getInt(1);
					type = resultSet.getString(2);
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
	 * Stores the lookup record in the database.
	 */
	public void ejbStore() {
		if (!modified)
			return;
		
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("UPDATE PropertyLookup SET PropDefForeignPK=? WHERE (UolId = ?) AND (PropId=?)");
				try {
					PropertyLookUpEntityPK pk = new PropertyLookUpEntityPK(uolId, propId);
					statement.setInt(1, propDefPK);
					statement.setInt(2, pk.uolId);
					statement.setString(3, pk.propId);

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
	 * Locates the lookup record with the given primary key in the database.
	 * 
	 * @param pk
	 *            PropertyLookUpEntityPK the primary key of the record to find
	 * @throws FinderException
	 *             when no record could be found for the specified primary key
	 * @return PropertyLookUpEntityPK the primary key of the found lookup record
	 */
	public PropertyLookUpEntityPK ejbFindByPrimaryKey(PropertyLookUpEntityPK pk) throws FinderException {
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT UolId,PropId FROM PropertyLookup WHERE  (UolId = ?) AND (PropId=?)");
				try {
					statement.setInt(1, pk.uolId);
					statement.setString(2, pk.propId);

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
			throw new FinderException(e.toString());
		}
	}

	/**
	 * Finds all lookup records for the specified property definition.
	 * 
	 * <p>
	 * The method returns a Collection of PropertyLookUpEntityPK for all found
	 * records, or an empty collection if no records where found.
	 * 
	 * @param propDefForeignPk
	 *            int the id of the property definition to find the records for
	 * @throws FinderException
	 *             when there is an error finding the lookup records
	 * @return Collection of PropertyLookUpEntityPK, the primary keys for all
	 *         found lookup records
	 */
	public Collection /* PropertyLookUpEntityPK */ejbFindByPropDefForeignPk(int propDefForeignPk)
			throws FinderException {
		try {
			Vector result = new Vector();

			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT UolId,PropId FROM PropertyLookup WHERE  (PropDefForeignPK = ?)");
				try {
					statement.setInt(1, propDefForeignPk);

					ResultSet resultSet = statement.executeQuery();

					// create collection of primary keys from each found
					// property
					while (resultSet.next()) {
						result.addElement(new PropertyLookUpEntityPK(resultSet.getInt(1), resultSet.getString(2)));
					}

					return result;

				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
		} catch (SQLException e) {
			throw new FinderException(e.toString());
		}

	}

	/**
	 * Finds all lookup records for the specified unit of learning.
	 * 
	 * <p>
	 * The method returns a Collection of PropertyLookUpEntityPK for all found
	 * records, or an empty collection if no records where found.
	 * 
	 * @param aUolId
	 *            int the id of the unit of learning to find all lookup records
	 *            for
	 * @throws FinderException
	 *             when there is an error locating the lookp records
	 * @return Collection of PropertyLookUpEntityPK, the primary keys of all
	 *         found lookup records, or an empty collection if no records where
	 *         found
	 */
	public Collection /* PropertyLookUpEntityPK */ejbFindByUol(int aUolId) throws FinderException {
		try {
			Vector result = new Vector();
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT propId FROM PropertyLookup WHERE  UolId = ?");
				try {
					statement.setInt(1, aUolId);

					ResultSet resultSet = statement.executeQuery();

					// create collection of primary keys from each found
					// property
					while (resultSet.next()) {
						result.addElement(new PropertyLookUpEntityPK(aUolId, resultSet.getString(1)));
					}

					return result;

				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
		} catch (SQLException e) {
			throw new FinderException(e.toString());
		}

	}

	/**
	 * Returns a data transfer object containing all data for this lookup record
	 * instance.
	 * 
	 * @return PropertyLookUpDto the data transfer object containing all data
	 *         for this lookup record instance
	 */
	public PropertyLookUpDto getDto() {
		// construct a new DTO
		return new PropertyLookUpDto(uolId, propDefPK, type);
	}
}
