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

package org.coppercore.dossier;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;
import org.coppercore.component.PropertyDef;
import org.coppercore.entity.Util;

/**
 * The PropertyEntityBean is the persistence class for storing properties in the
 * database.
 * 
 * <p>
 * A property's behaviour is defined by the property definition it is derived
 * from.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.24 $, $Date: 2008/05/15 15:32:37 $
 */
public class PropertyEntityBean implements EntityBean {
	private static final long serialVersionUID = 43L;

	private EntityContext entityContext;

	private int _propPk;
	private int _propDefPK;
	private String _propId;

	private String _propValue;
	private String _dataType;
	private int _scope;

	private boolean modified = false;

	/**
	 * Creates a new property entity in the database.
	 * 
	 * @param propValue
	 *            String the value of the property
	 * @param dataType
	 *            String the datatype of the property
	 * @param runId
	 *            int the id of the run the property belongs to, or null for
	 *            global properties
	 * @param userId
	 *            String the id of the owner of the property, or null for
	 *            non-personal, non-role properties
	 * @param propDefPK
	 *            int the id of the property definition defining the behaviour
	 *            of this property
	 * @param scope
	 *            int the scope of this property
	 * @param propId
	 *            String the learning design id of the property
	 * @throws CreateException
	 *             when there is an error creating the property
	 * @return PropertyEntityPK the primary key of the new created property
	 */
	public PropertyEntityPK ejbCreate(String propValue, String dataType, int runId, String userId, int propDefPK,
			int scope, String propId) throws CreateException {

		_propValue = propValue;
		_dataType = dataType;
		_propDefPK = propDefPK;
		_scope = scope;
		_propId = propId;
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				String sql = "INSERT INTO PropertyValue (UserId,RunId,PropDefForeignPK,PropValue) VALUES (?,?,?,?)";
				PreparedStatement statement = connection.prepareStatement(sql);
				try {
					statement.setString(1, userId);
					if (runId < 0) {
						statement.setNull(2, Types.INTEGER);
					} else {
						statement.setInt(2, runId);
					}
					statement.setInt(3, propDefPK);
					statement.setString(4, propValue);
					if (statement.executeUpdate() != 1) {
						throw new CreateException("Could not create Property " + userId + "," + runId + "," + propValue
								+ "," + propDefPK);

					}

					int id = Util.getInstance().getIdentity(connection, "propertyvalue", "PK");
					if (id != -1) {
						this._propPk = id;
						PropertyEntityPK pk = new PropertyEntityPK(_propPk);
						return pk;
					}
					throw new CreateException("Could not fetch key for Property " + userId + "," + runId + ","
							+ propValue + "," + propDefPK);
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
	 * @param propValue
	 *            String the value of the property
	 * @param dataType
	 *            String the datatype of the property
	 * @param runId
	 *            int the id of the run the property belongs to, or null for
	 *            global properties
	 * @param userId
	 *            String the id of the owner of the property, or null for
	 *            non-personal, non-role properties
	 * @param propDefPK
	 *            int the id of the property definition defining the behaviour
	 *            of this property
	 * @param scope
	 *            int the scope of this property
	 * @param propId
	 *            String the learning design id of the property
	 * @throws CreateException
	 *             is never thrown
	 */
	public void ejbPostCreate(String propValue, String dataType, int runId, String userId, int propDefPK, int scope,
			String propId) throws CreateException {
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
		PropertyEntityPK pk = (PropertyEntityPK) entityContext.getPrimaryKey();

		// this pk may not be used anymore
		PkCache.removeFromCache(pk.propPk);

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection.prepareStatement("DELETE FROM PropertyValue WHERE PK = ?");
				try {
					statement.setInt(1, pk.propPk);

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
	 * Sets the value of this property to the specified value.
	 * <p>
	 * PropertyEntity's are of a generic data type meaning that the value is
	 * always stored as a String, or null.
	 * 
	 * @param value
	 *            String the new value of the property
	 */
	public void setPropValue(String value) {
		if (value != null && !value.equals(_propValue)) {
			modified = true;
			_propValue = value;
		}
	}

	/**
	 * Loads the data of this property instance from the database.
	 */
	public void ejbLoad() {
		Logger logger = Logger.getLogger(this.getClass());
		// get the name from the primary key
		PropertyEntityPK pk = (PropertyEntityPK) entityContext.getPrimaryKey();
		try {

			long start = System.currentTimeMillis();

			Connection connection = Util.getInstance().getConnection();
			try {
				// retrieve data from database
				// PreparedStatement statement = connection
				// .prepareStatement("SELECT DataType,Propvalue, PropId,
				// PropDefForeignPK, Scope FROM PropertyValueView WHERE PK =
				// ?");

				// modified to increase HSQL performance
				PreparedStatement statement = connection
						.prepareStatement("SELECT PropertyValue.PK, PropertyValue.PropValue, PropertyLookup.PropId, PropertyLookup.PropDefForeignPK, PropertyDefinition.Scope FROM PropertyValue INNER JOIN PropertyDefinition ON PropertyDefinition.PK = PropertyValue.PropDefForeignPK INNER JOIN PropertyLookup ON PropertyValue.PropDefForeignPK = PropertyLookup.PropDefForeignPK WHERE PropertyValue.PK = ?");

				try {
					statement.setInt(1, pk.propPk);
					ResultSet resultSet = statement.executeQuery();

					if (!resultSet.next()) {
						throw new RemoteException(pk + "  failed to load from database");
					}

					// Store our data
					this._propPk = pk.propPk;
					_dataType = resultSet.getString(1);
					_propValue = resultSet.getString(2);
					_propId = resultSet.getString(3);
					_propDefPK = resultSet.getInt(4);
					_scope = resultSet.getInt(5);
					modified = false;

					logger.debug("ejbLoad of property " + pk + " took " + (System.currentTimeMillis() - start) + " ms");

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
	 * Stores the data of this property definition instance in the database.
	 */
	public void ejbStore() {
		Logger logger = Logger.getLogger(this.getClass());
		if (!modified) {
			// logger.debug("*** ejbStore of property " + _propPk + " nothing to
			// do");
			return;
		}

		try {
			long start = System.currentTimeMillis();
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("UPDATE PropertyValue SET PropValue = ? WHERE PK=?");
				try {
					statement.setString(1, _propValue);
					statement.setInt(2, _propPk);

					PropertyEntityPK pk = new PropertyEntityPK(_propPk);

					if (statement.executeUpdate() != 1) {
						throw new EJBException("Could not update " + pk);
					}
					modified = false;

				} finally {
					statement.close();
				}
			} finally {
				connection.close();
				logger.debug("ejbStore of property " + _propPk + " took " + (System.currentTimeMillis() - start)
						+ " ms");
			}
		} catch (Exception ex) {
			throw new EJBException(ex.getMessage());
		}
	}

	public void ejbActivate() {
		Logger logger = Logger.getLogger(this.getClass());
		logger.debug("Activating " + entityContext.getPrimaryKey());
	}

	public void ejbPassivate() {
		Logger logger = Logger.getLogger(this.getClass());
		logger.debug("Passivating " + entityContext.getPrimaryKey());
	}

	public void unsetEntityContext() {
		this.entityContext = null;
	}

	public void setEntityContext(EntityContext entityContext) {
		this.entityContext = entityContext;
	}

	/**
	 * Finds the property with the given primary key in the database.
	 * 
	 * <p>
	 * If no property is found, the method throws a FinderException.
	 * 
	 * @param pk
	 *            PropertyEntityPK the primary key of the property to look for
	 * @throws FinderException
	 *             when the property with the given primary key could not be
	 *             located
	 * @return PropertyEntityPK the primary key of the found property
	 */
	public PropertyEntityPK ejbFindByPrimaryKey(PropertyEntityPK pk) throws FinderException {
		try {
			long start = System.currentTimeMillis();
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection.prepareStatement("SELECT PK FROM PropertyValue WHERE PK = ?");
				try {
					statement.setInt(1, pk.propPk);
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
				Logger logger = Logger.getLogger(this.getClass());
				logger.debug("Property find by primary key took: " + (System.currentTimeMillis() - start) + " ms.");
			}
		} catch (SQLException e) {
			throw new FinderException(e.toString());
		}
	}

	/**
	 * Locates the property with the passed parameters in the database. If the
	 * property is found the method returns the primary key of the found
	 * property, otherwise the method throws a FinderException
	 * 
	 * @param uolId
	 *            int the id of the unit of learning this property belongs to
	 * @param propId
	 *            String the learning design of the property to find
	 * @param userId
	 *            String the id of the owner to find the property for
	 * @param runId
	 *            int the id of the run the property belongs to
	 * @throws FinderException
	 *             when the property could not be found
	 * @return PropertyEntityPK the primary key of the found property
	 */
	public PropertyEntityPK ejbFindByLookUp(int uolId, String propId, String userId, int runId) throws FinderException {
		Logger logger = Logger.getLogger(this.getClass());
		long start = System.currentTimeMillis();

		// check if key is already cached to improve performance
		PropertyEntityPK cachedPk = PkCache.getPK(uolId, propId, userId, runId);
		if (cachedPk != null) {
			logger.debug("Found cached key");
			return cachedPk;
		}

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				// modified to increase general database performance. We use two
				// SQL statements now to prevent using an expensive join

				// first fetch the PropertyDefintion data for the requested
				// property
				PreparedStatement statement = connection
						.prepareStatement("SELECT PropertyDefinition.PK, PropertyDefinition.Scope FROM PropertyDefinition INNER JOIN PropertyLookup ON PropertyDefinition.PK = PropertyLookup.propDefForeignPK WHERE (PropertyLookup.Uolid = ?) AND (PropertyLookup.propId = ?)");

				try {
					statement.setInt(1, uolId);
					statement.setString(2, propId);
					ResultSet resultSet = statement.executeQuery();

					// check if bean can be loaded from database
					if (!resultSet.next()) {
						// this should never occur. This means that we are
						// trying to fetch a
						// property that does not have a definition.
						logger.warn("Property definition not found for property[" + propId + "] in unit of learning["
								+ uolId + "]");
						throw new FinderException("Could not find PropertyEntity[" + uolId + "," + runId + "," + userId
								+ "," + propId + "]");
					}

					// store the propertydefinition data needed for next query
					int foundPropDefPK = resultSet.getInt(1);
					int foundScope = resultSet.getInt(2);

					statement.close();

					// now fetch the PK of the requested property using the
					// found values.
					String sql = "SELECT PK FROM PropertyValue WHERE (PropDefForeignPK = " + foundPropDefPK + ")";

					// check if we are dealing with personal or a role scope
					if (((PropertyDef.ISPERSONAL | PropertyDef.ISROLE) & foundScope) != 0) {
						sql += " AND (UserId = '" + userId + "')";
					} else {
						sql += " AND (UserId IS NULL)";
					}

					// check if we are dealing with a local scope
					if ((PropertyDef.ISLOCAL & foundScope) != 0) {
						sql += " AND (RunId = " + runId + ")";
					} else {
						sql += " AND (RunId IS NULL)";
					}

					statement = connection.prepareStatement(sql);
					resultSet = statement.executeQuery();

					// check if bean can be loaded from database
					if (!resultSet.next()) {
						// the property instance does not yet exist. Notify
						// caller.
						throw new FinderException("Could not find PropertyEntity[" + uolId + "," + runId + "," + userId
								+ "," + propId + "]");
					}

					// store the pk on our cache
					int pk = resultSet.getInt(1);
					PkCache.putPK(pk, uolId, propId, userId, runId);
					logger.debug("Lookup for " + pk + " took " + (System.currentTimeMillis() - start) + " ms");
					return new PropertyEntityPK(pk);
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
		} catch (SQLException ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Finds all properties for the specified user in the specified run.
	 * 
	 * <p>
	 * The method returns the primary key for all found properties or an empty
	 * collection if no property matches the search criteria.
	 * 
	 * @param userId
	 *            String the id of the owner of the properties to search for
	 * @param runId
	 *            int the id of the run the properties belong to
	 * @throws FinderException
	 *             when there is an error finding the properties
	 * @return Collection of PropertyEntityPK primary keys of all properties
	 *         that match the search criteria, or an empty Collection if no
	 *         properties are found
	 */
	public Collection /* PropertyEntityPK */ejbFindByUserRun(String userId, int runId) throws FinderException {
		try {
			Vector result = new Vector();

			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT PK FROM PropertyValue WHERE (UserId = ?) AND (RunId = ?)");
				try {
					statement.setString(1, userId);
					statement.setInt(2, runId);
					ResultSet resultSet = statement.executeQuery();

					// create collection of primary keys from each found
					// property
					while (resultSet.next()) {
						result.addElement(new PropertyEntityPK(resultSet.getInt(1)));
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
	 * Find all properties that belong to the specified run.
	 * 
	 * <p>
	 * The method returns the primary key for all found properties or an empty
	 * collection if no property matches the search criteria.
	 * 
	 * @param runId
	 *            int the id of the run to find all properties for.
	 * @throws FinderException
	 *             when there is an error finding the properties
	 * @return Collection of PropertyEntityPK primary keys of all properties
	 *         that match the search criteria, or an empty Collection if no
	 *         properties are found
	 */
	public Collection /* PropertyEntityPK */ejbFindByRun(int runId) throws FinderException {
		try {
			Vector result = new Vector();

			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT PK FROM PropertyValue WHERE (RunId = ?)");
				try {
					statement.setInt(1, runId);
					ResultSet resultSet = statement.executeQuery();

					// create collection of primary keys from each found
					// property
					while (resultSet.next()) {
						result.addElement(new PropertyEntityPK(resultSet.getInt(1)));
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
	 * Finds all properties belonging to the specified unit of learning.
	 * 
	 * <p>
	 * The method returns the primary key for all found properties or an empty
	 * collection if no property matches the search criteria.
	 * 
	 * @param uolId
	 *            int the id of the unit of learning to find all properties for.
	 * @throws FinderException
	 *             when there is an error finding the properties
	 * @return Collection of PropertyEntityPK primary keys of all properties
	 *         that match the search criteria, or an empty Collection if no
	 *         properties are found
	 */
	public Collection /* PropertyEntityPK */ejbFindByUol(int uolId) throws FinderException {
		try {
			Vector result = new Vector();

			Connection connection = Util.getInstance().getConnection();
			try {
				// PreparedStatement statement = connection
				// .prepareStatement("SELECT PK FROM LookUpPropertyValueView
				// WHERE
				// (UolId = ?)");

				// modified to increase HSQL performance
				PreparedStatement statement = connection
						.prepareStatement("SELECT PropertyValue.PK FROM PropertyDefinition INNER JOIN PropertyLookup ON PropertyDefinition.PK = PropertyLookup.PropDefForeignPK INNER JOIN PropertyValue ON PropertyDefinition.PK = PropertyValue.PropDefForeignPK WHERE (PropertyLookup.UolId = ?)");

				try {
					statement.setInt(1, uolId);
					ResultSet resultSet = statement.executeQuery();

					// create collection of primary keys from each found
					// property
					while (resultSet.next()) {
						result.addElement(new PropertyEntityPK(resultSet.getInt(1)));
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
	 * Finds all properties for the given user.
	 * 
	 * <p>
	 * The method returns the primary key for all found properties or an empty
	 * collection if no property matches the search criteria.
	 * 
	 * @param userId
	 *            int the id of the user to find all properties for.
	 * @throws FinderException
	 *             when there is an error finding the properties
	 * @return Collection of PropertyEntityPK primary keys of all properties
	 *         that match the search criteria, or an empty Collection if no
	 *         properties are found
	 */
	public Collection /* PropertyEntityPK */ejbFindByUser(String userId) throws FinderException {
		try {
			Vector result = new Vector();

			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT PK FROM PropertyValue WHERE (UserId = ?)");
				try {
					statement.setString(1, userId);
					ResultSet resultSet = statement.executeQuery();

					// create collection of primary keys from each found
					// property
					while (resultSet.next()) {
						result.addElement(new PropertyEntityPK(resultSet.getInt(1)));
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
	 * Returns a data transfer object containing all data members of this
	 * instance.
	 * 
	 * @return PropertyDto the data transfer object containing all data members
	 *         of this instance
	 */
	public PropertyDto getDto() {
		return new PropertyDto(_propValue, _dataType, _propId, _propDefPK, _scope);
	}

	public String toString() {
		return "PropertyEntityBean " + _propId;
	}

}
