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
package org.coppercore.events;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;
import org.coppercore.entity.Util;

/**
 * The EventEntityBean is the persistence class for storing Events in the
 * database.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.9 $, $Date: 2008/05/15 15:32:39 $
 */
public class EventEntityBean implements EntityBean {
	private static final long serialVersionUID = 42L;
	private EntityContext entityContext;
	private EventDto dto;
	private boolean modified = false;

	// int id;

	/**
	 * Creates a new Event entity in the database and sets in values to the data
	 * specified in the passed data transfer object.
	 * 
	 * @param eventDto
	 *            EventDto the data transfer object containing the data of this
	 *            instance
	 * @throws CreateException
	 *             when there is an error creating the new entity in the
	 *             database
	 * @return EventEntityPK the primary key of the just created event instance
	 */
	public EventEntityPK ejbCreate(EventDto eventDto) throws CreateException {
		dto = eventDto;
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				String sql = "INSERT INTO Event (uolId,TriggerId,Type,ClassName,ComponentId) VALUES (?,?,?,?,?)";
				PreparedStatement statement = connection.prepareStatement(sql);
				try {
					statement.setInt(1, eventDto.getUolId());
					statement.setString(2, eventDto.getTriggerId());
					statement.setString(3, eventDto.getType());
					statement.setString(4, eventDto.getClassName());
					statement.setString(5, eventDto.getComponentId());

					if (statement.executeUpdate() != 1) {
						throw new CreateException("Could not create Event: " + eventDto);
					}

					int id = Util.getInstance().getIdentity(connection, "event", "pk");
					if (id != -1) {
						modified = false;
						return new EventEntityPK(id);
					}
					throw new CreateException("Could not create " + eventDto);
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
	 * @param dto
	 *            EventDto the parameters of the new event
	 * @throws CreateException
	 *             when there would be an error in this method
	 */
	public void ejbPostCreate(EventDto dto) throws CreateException {
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
		EventEntityPK pk = (EventEntityPK) entityContext.getPrimaryKey();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {

				// first delete all EventList entries for this EventHandler
				PreparedStatement statement = connection.prepareStatement("DELETE FROM Event WHERE (pk = ?)");
				try {
					statement.setInt(1, pk.id);

					if (statement.executeUpdate() == 0) {
						throw new RemoveException("Could not remove " + toString());
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
	 * Sets the parameters of this instance to the values specified in the data
	 * transfer object dto.
	 * 
	 * @param eventDto
	 *            EventDto the new parameter values for this EventEntity
	 *            instance
	 * @see #getDto
	 */
	public void setDto(EventDto eventDto) {
		this.dto = eventDto;
		modified = true;
	}

	/**
	 * Returns all the parameters of this EventEntity instance in a data
	 * transfer object.
	 * 
	 * @return EventDto contianing the parameters of this EventEntity
	 * @see #setDto
	 */
	public EventDto getDto() {
		return dto;
	}

	/**
	 * Finds the EventEntity with the specified primary key in the database.
	 * 
	 * <p>
	 * If no entity with the given key can be found the method throws a
	 * FinderException.
	 * 
	 * @param pk
	 *            EventEntityPK the primary key of the EventEntity to find
	 * @throws FinderException
	 *             when no EventEntity with the given primary key can be found
	 * @return EventEntityPK the primary key of the found EventEntity
	 */
	public EventEntityPK ejbFindByPrimaryKey(EventEntityPK pk) throws FinderException {
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection.prepareStatement("SELECT pk FROM Event WHERE pk = ?");
				try {
					statement.setInt(1, pk.id);
					ResultSet resultSet = statement.executeQuery();

					// check if bean can be loaded from database
					if (!resultSet.next()) {
						throw new FinderException("Could not find Event: " + pk.id);
					}
					return new EventEntityPK(pk.id);
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
	 * Finds all EventEntity's belonging to the specified unit of learning.
	 * 
	 * <p>
	 * The method returns a Collection of primary keys for all EventEntity's
	 * found. If no EventEntity is found, the method returns an empty
	 * Collection.
	 * 
	 * @param uolId
	 *            int the unit of learning to find all EventEntity's for
	 * @throws FinderException
	 *             when there is an error accessing the database
	 * @return Collection of all primary keys of all EventEntity's for the given
	 *         unit of learning, or an empty Collection if no EventEntity's are
	 *         found
	 */
	public Collection ejbFindByUol(int uolId) throws FinderException {
		ArrayList result = new ArrayList();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection.prepareStatement("SELECT pk FROM Event WHERE (uolId = ?)");
				try {
					statement.setInt(1, uolId);
					ResultSet rs = statement.executeQuery();
					while (rs.next()) {
						int i = rs.getInt(1);
						result.add(new EventEntityPK(i));
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
			throw new FinderException(e.toString());
		}
	}

	/**
	 * Finds all EventEntity's beloning to the specified property definition of
	 * the specified type.
	 * 
	 * <p>
	 * The method returns a Collection of all primary keys as EventEntityPK of
	 * the EventEntity's found, or an empty Collection if no EventEntity's are
	 * found.
	 * 
	 * @param propDefPK
	 *            int the primary key of the property definition to look for
	 * @param type
	 *            String the type of the property definition to look for
	 * @throws FinderException
	 *             when there is an error accessing the database
	 * @return Collection of EventEntityPK (primary key) of all EventEntity
	 *         found, or an empty Collection if no EventEntity is found
	 */
	public Collection /* EventEntityPK */ejbFindByPropDefPK(int propDefPK, String type) throws FinderException {
		ArrayList result = new ArrayList();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				// PreparedStatement statement = connection
				// .prepareStatement("SELECT pk FROM LookUpEventView WHERE
				// (PropDefForeignPK = ?) AND (Type = ?)");

				// modified to increase HSQL performance
				PreparedStatement statement = connection
						.prepareStatement("SELECT Event.pk FROM Event INNER JOIN PropertyLookup ON Event.uolId = PropertyLookup.UolId AND Event.TriggerId = PropertyLookup.PropId WHERE (PropertyLookup.PropDefForeignPK = ?) AND (Event.Type = ?)");

				try {
					statement.setInt(1, propDefPK);
					statement.setString(2, type);
					ResultSet rs = statement.executeQuery();
					while (rs.next()) {
						result.add(new EventEntityPK(rs.getInt(1)));
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
			throw new FinderException(e.toString());
		}

	}

	/**
	 * Finds all EventEntity's of the specified type in the specified unit of
	 * learning.
	 * 
	 * <p>
	 * The method returns a Collection of EventEntityPK, the primary key, of all
	 * EventEntity's found, or an empty Collection if no EventEntity is found.
	 * 
	 * @param uolId
	 *            int the unit of learning id being the scope of the search
	 * @param type
	 *            String the type of events to find
	 * @throws FinderException
	 *             when there is error accessing the database
	 * @return Collection of EventEntityPK for all found EventEntity's, or an
	 *         empty Collection if no EventEntity is found
	 */
	public Collection /* EventEntityPK */ejbFindByType(int uolId, String type) throws FinderException {
		ArrayList result = new ArrayList();

		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("SELECT pk FROM Event WHERE (uolId = ?) AND (Type = ?)");
				try {
					statement.setInt(1, uolId);
					statement.setString(2, type);
					ResultSet rs = statement.executeQuery();
					while (rs.next()) {
						result.add(new EventEntityPK(rs.getInt(1)));
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
			throw new FinderException(e.toString());
		}
	}

	/**
	 * Loads the data for this instance from the database.
	 * 
	 * <p>
	 * The method uses the primary key stored in the entityContext to locate the
	 * instance.
	 */
	public void ejbLoad() {
		EventEntityPK pk = (EventEntityPK) entityContext.getPrimaryKey();
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				// retreive data from database
				PreparedStatement statement = connection
						.prepareStatement("SELECT uolId,TriggerId,Type,ClassName,ComponentId FROM Event WHERE pk = ?");
				try {
					statement.setInt(1, pk.id);
					ResultSet resultSet = statement.executeQuery();

					if (!resultSet.next()) {
						throw new RemoteException("ejbLoad: Event not found: " + pk.id);
					}

					// Store our data
					int uolId = resultSet.getInt(1);
					String triggerId = resultSet.getString(2);
					String type = resultSet.getString(3);
					String className = resultSet.getString(4);
					String componentId = resultSet.getString(5);

					dto = new EventDto(pk.id, uolId, triggerId, type, className, componentId);
					modified = false;
				} finally {
					statement.close();
				}
			} finally {
				connection.close();
			}
		} catch (Exception e) {
			throw new EJBException(pk.id + " failed to load Event from database", e);
		}
	}

	/**
	 * Persist the data of this instance to the database.
	 * 
	 * <p>
	 * The method uses the primary key stored in the entityContext to locate the
	 * record in the database.
	 */
	public void ejbStore() {
		if (!modified)
			return;
		
		EventEntityPK pk = (EventEntityPK) entityContext.getPrimaryKey();
		try {
			Connection connection = Util.getInstance().getConnection();
			try {
				PreparedStatement statement = connection
						.prepareStatement("UPDATE Event SET uolId=?, TriggerId=?, Type=?, ClassName=?, ComponentId=? WHERE pk=?");
				try {
					statement.setInt(1, dto.getUolId());
					statement.setString(2, dto.getTriggerId());
					statement.setString(3, dto.getType());
					statement.setString(4, dto.getClassName());
					statement.setString(5, dto.getComponentId());
					statement.setInt(6, pk.id);
					if (statement.executeUpdate() != 1) {
						throw new EJBException("Could not update " + toString());
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
	 * Returns a String representation of this EventEntity instance.
	 * 
	 * @return String the representation of this instance
	 */
	public String toString() {
		if (dto == null) {
			return "Event (dto == null)";
		}
		return "Event[" + dto.getUolId() + "," + dto.getTriggerId() + "," + dto.getType() + "," + dto.getClassName()
				+ "," + dto.getComponentId() + "]";
	}
}