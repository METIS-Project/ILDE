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
import java.util.Collection;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;
import org.coppercore.entity.Util;

/**
 * The PropertyDefEntityBean is the persistence class for storing Property
 * definitions in the database.
 * 
 * <p>
 * A property definition defines the behaviour of a property, but has no
 * individual parameters. A property defininition and a property resemble a Java
 * class and a Java object instance of such a class.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.22 $, $Date: 2008/11/14 12:52:52 $
 */
public class PropertyDefEntityBean implements EntityBean {
	private static final long serialVersionUID = 42L;
	private EntityContext entityContext;
	private int _propDefPK;
	private int _scope;
	private String _dataType;
	private String _defaultValue;
	private String _href;
	private int _definedBy;
	
	private boolean modified = false;

	/**
	 * Creates a new PropertyDefinition record in the database and sets its data
	 * to the specified values.
	 * 
	 * @param scope
	 *            int defines the scope of the property
	 * @param dataType
	 *            String the data type of the property
	 * @param defaultValue
	 *            String the default value for the properties derived from this
	 *            definition
	 * @param href
	 *            String the uri of the property if its scop is global
	 * @param definedBy
	 *            int the id of the unit of learning where this property is
	 *            defined
	 * @throws CreateException
	 *             when an error occurs during the creation of the instance
	 * @return PropertyDefEntityPK the primary key of the new created instance
	 */
	public PropertyDefEntityPK ejbCreate(int scope, String dataType, String defaultValue, String href, int definedBy)
			throws CreateException {
		_scope = scope;
		_dataType = dataType;
		_defaultValue = defaultValue;
		_href = href;
		_definedBy = definedBy;

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				String sql = "INSERT INTO PropertyDefinition (Scope,DataType,DefaultValue,Href, DefinedBy) VALUES (?,?,?,?,?)";
				PreparedStatement statement = connection.prepareStatement(sql);
				try {
					statement.setInt(1, scope);
					statement.setString(2, dataType);
					statement.setString(3, defaultValue);
					statement.setString(4, href);
					statement.setInt(5, definedBy);

					if (statement.executeUpdate() != 1) {
						throw new CreateException("Could not create PropertyDefinition");
					}

					int id = Util.getInstance().getIdentity(connection, "PropertyDefinition", "PK");
					if (id != -1) {
						PropertyDefEntityPK pk = new PropertyDefEntityPK(id);
						_propDefPK = pk.propDefPK;
						modified = false;
						return pk;
					}
					throw new CreateException("Could not create PropertyDefinition");
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
	 * @param scope
	 *            int defines the scope of the property
	 * @param dataType
	 *            String the data type of the property
	 * @param defaultValue
	 *            String the default value for the properties derived from this
	 *            definition
	 * @param href
	 *            String the uri of the property if its scop is global
	 * @param definedBy
	 *            int the id of the unit of learning where this property is
	 *            defined
	 * @throws CreateException
	 *             when there would be an error in this method
	 */
	public void ejbPostCreate(int scope, String dataType, String defaultValue, String href, int definedBy)
			throws CreateException {
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
		PropertyDefEntityPK pk = (PropertyDefEntityPK) entityContext.getPrimaryKey();
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection.prepareStatement("DELETE FROM PropertyDefinition WHERE PK=?");
				try {
					statement.setInt(1, pk.propDefPK);

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
	 * Loads the data of this PropertyDefinition instance from the database.
	 */
	public void ejbLoad() {
		// get the name from the primary key
		PropertyDefEntityPK pk = (PropertyDefEntityPK) entityContext.getPrimaryKey();
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				// retreive data from database
				PreparedStatement statement = connection
						.prepareStatement("SELECT Scope, DataType, DefaultValue, Href, DefinedBy FROM PropertyDefinition WHERE PK = ?");
				try {
					statement.setInt(1, pk.propDefPK);
					ResultSet resultSet = statement.executeQuery();

					if (!resultSet.next()) {
						throw new RemoteException(pk + " failed to load from database");
					}

					// Store our data
					_propDefPK = pk.propDefPK;
					_scope = resultSet.getInt(1);
					_dataType = resultSet.getString(2);
					_defaultValue = resultSet.getString(3);
					_href = resultSet.getString(4);
					_definedBy = resultSet.getInt(5);
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
	 * Stores the data of this property definition instance in the database.
	 */
	public void ejbStore() {
//		Logger logger = Logger.getLogger(this.getClass());
		if (!modified) 
			return;

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("UPDATE PropertyDefinition SET Scope=?, DataType=?, DefaultValue=?, Href=?, DefinedBy=? WHERE PK=?");
				try {
					statement.setInt(1, _scope);
					statement.setString(2, _dataType);
					statement.setString(3, _defaultValue);
					statement.setString(4, _href);
					statement.setInt(5, _definedBy);
					statement.setInt(6, _propDefPK);

					PropertyDefEntityPK pk = new PropertyDefEntityPK(_propDefPK);

					if (statement.executeUpdate() != 1) {
						throw new EJBException("Could not update " + pk);
					}
					modified = false;

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
		Logger logger = Logger.getLogger(this.getClass());
		logger.debug("Activate " + this);
	}

	public void ejbPassivate() {
		Logger logger = Logger.getLogger(this.getClass());
		logger.debug("Passivate " + this);
	}

	public void unsetEntityContext() {
		this.entityContext = null;
	}

	public void setEntityContext(EntityContext entityContext) {
		this.entityContext = entityContext;
	}

	/**
	 * Finds the property definition with the given primary key in the database.
	 * 
	 * <p>
	 * If no property definition is found, the method throws a FinderException.
	 * 
	 * @param pk
	 *            PropertyDefEntityPK the primary key of the property definition
	 *            to look for
	 * @throws FinderException
	 *             when the property with the given primary key could not be
	 *             located
	 * @return PropertyDefEntityPK the primary key of the found property
	 *         definition
	 */
	public PropertyDefEntityPK ejbFindByPrimaryKey(PropertyDefEntityPK pk) throws FinderException {
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT PK FROM PropertyDefinition WHERE PK = ?");
				try {
					statement.setInt(1, pk.propDefPK);
					ResultSet resultSet = statement.executeQuery();

					// check if bean can be loaded from database
					if (!resultSet.next())
						throw new ObjectNotFoundException("Could not find " + pk);

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
	 * Finds the property definition with the specified LD property id in the
	 * specified unit of learning.
	 * 
	 * @param uolId
	 *            int the id of the unit of learning this property definition
	 *            belongs to
	 * @param propId
	 *            String the learning design id of the property definition to
	 *            look for
	 * @throws FinderException
	 *             when no property definition could be found
	 * @return PropertyDefEntityPK the primary key of the found property
	 *         definition
	 */
	public PropertyDefEntityPK ejbFindByLookUp(int uolId, String propId) throws FinderException {
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT PropDefForeignPK FROM PropertyLookup WHERE (UolId = ?) AND (PropId = ?)");
				try {
					statement.setInt(1, uolId);
					statement.setString(2, propId);
					ResultSet resultSet = statement.executeQuery();

					// check if bean can be loaded from database
					if (!resultSet.next())
						throw new FinderException("Could not find PropertyDefEntity[" + uolId + "," + propId + "]");

					PropertyDefEntityPK pk = new PropertyDefEntityPK(resultSet.getInt(1));

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
	 * Finds the property definition by searching for the specified uri.
	 * 
	 * <p>
	 * If no property definition is found, the method throws a FinderException.
	 * 
	 * @param uri
	 *            String the uri of the property to look for
	 * @throws FinderException
	 *             when no property definition is found
	 * @return PropertyDefEntityPK the primary key of the found property
	 *         definition
	 */
	public PropertyDefEntityPK ejbFindByUri(String uri) throws FinderException {
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT PK FROM PropertyDefinition WHERE (Href = ?)");
				try {
					statement.setString(1, uri);
					ResultSet resultSet = statement.executeQuery();

					// check if bean can be loaded from database
					if (!resultSet.next())
						throw new FinderException("Could not find PropertyDefEntity with URI[" + uri + "]");

					PropertyDefEntityPK pk = new PropertyDefEntityPK(resultSet.getInt(1));

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
	 * Finds all property definitions for the specified unit of learning.
	 * 
	 * <p>
	 * If no property definitions are found the method returns an empty
	 * collection/
	 * 
	 * @param uolId
	 *            int the id of the unit of learning to locate all property
	 *            definitions for
	 * @throws FinderException
	 *             when there is an error accessing the database.
	 * @return Collection of PropertyDefEntityPK (primary key) of all found
	 *         property definitions, or an empty collection if no property
	 *         definition is found
	 */
	public Collection /* PropertyDefEntityPK */ejbFindByUol(int uolId) throws FinderException {
		try {
			Vector result = new Vector();
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT PropDefForeignPK FROM PropertyLookup WHERE  UolId = ?");
				try {
					statement.setInt(1, uolId);

					ResultSet resultSet = statement.executeQuery();

					// create collection of primary keys from each found
					// property
					while (resultSet.next()) {
						result.addElement(new PropertyDefEntityPK(resultSet.getInt(1)));
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
	 * Returns a PropertyDefDto data transfer object containing all parameters
	 * of this property definition instance.
	 * 
	 * @return PropertyDefDto the data transfer object containing all parameters
	 *         of this instance
	 * @see #setDto
	 */
	public PropertyDefDto getDto() {
		return new PropertyDefDto(_scope, _dataType, _href, _defaultValue, _definedBy, _propDefPK);
	}

	/**
	 * Sets all data members of this property definition instance to the values
	 * from the specified data transfer object.
	 * 
	 * @param dto
	 *            PropertyDefDto the data transfer object containing the new
	 *            parameters for this instance
	 * @see #getDto
	 */
	public void setDto(PropertyDefDto dto) {
		modified = true;
		_dataType = dto.getDataType();
		_defaultValue = dto.getDefaultValue();
		_scope = dto.getScope();
		_href = dto.getHref();
		_definedBy = dto.getDefinedBy();
	}
}
