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

package org.coppercore.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import org.apache.log4j.Logger;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.datatypes.LDDataType;
import org.coppercore.dossier.PropertyDefDto;
import org.coppercore.dossier.PropertyDefFacade;
import org.coppercore.dossier.PropertyEntityPK;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.PropertyNotFoundException;
import org.coppercore.exceptions.PropertyTypeException;
import org.coppercore.exceptions.TypeCastException;

/**
 * This class implements the factory for retrieving Ccomponents. Whenever one of
 * the components is retrieved it should be done via this class. A cache is
 * maintained locally to avoid unecessary bean access.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.29 $, $Date: 2008/04/28 08:00:36 $
 */
public class ComponentFactory {
	final static int MAX_ENTRIES = 1024;

	private static ComponentFactory factory = null;

	private static final String CLASSNAME = "org.coppercore.component.ComponentFactory";

	/*
	 * Fixed: 11-10-2006. Removing the cache resulted into multiple loading of the
	 * Expression property. This resulted into multiple java object instances of
	 * the same ThenActions causing the firedActions filters to fail. These
	 * filters make sure that the same actions aren't evaluated over and over
	 * again potentially causing endless loops. Therefore the cache has been
	 * reintroduced. However only ClassProperties and ExpressionProperties are
	 * stored on the cache from now one. Furhtermore the cache has been limited in
	 * size avoiding the memory leak problem connected to the cache earlier.
	 */
	private  Map cache = Collections.synchronizedMap(new LinkedHashMap(MAX_ENTRIES + 1, .75F, true) {
		// This method is called just after a new entry has been added
		public boolean removeEldestEntry(Map.Entry eldest) {
			return size() > MAX_ENTRIES;
		}
	});

	private static long propertyCounter = 0;

	/**
	 * Default method for retrieving a PropertyFactory object. Because a
	 * PropertyFactory maintains a cache, multiple instances of this class should
	 * be avoided. This methods ensure that only one instance at a time is
	 * instantiated (local singleton).
	 * 
	 * @return ComponentFactory the current ComponentFactory to be used for
	 *         retrieving Components
	 */
	public static ComponentFactory getPropertyFactory() {
		if (factory == null) {
			Logger logger = Logger.getLogger(CLASSNAME);
			logger.debug("Creating new cache");

			factory = new ComponentFactory();
		}
		return factory;
	}

	/**
	 * Default constructor.
	 * 
	 */
	private ComponentFactory() {
		// do nothing
	}

	
	/*
	 * Implements a cache for ExpressionProperties. ExpressionProperties must be cached
	 * and may not be reloaded during the evaluation of the expressions!!! This cache makes
	 * sure this will not happen.
	 */
	private synchronized StaticProperty fetchFromCache(String key) {
		StaticProperty prop = (StaticProperty) cache.get(key);
		if (prop == null)
			return null;

		// put key on top of linked list
		cache.remove(key);
		cache.put(key, prop);
		return prop;

		// return (StaticProperty) cache.get(key);
		// TODO enable optional debug statement here
		// System.out.print(" - F:" + ++propertyCounter);
		// return null;
	}

	/**
	 * Clears the cache containing the Components and their definitions.
	 * 
	 */
	public synchronized void clearCache() {
		cache.clear();
	}

	/**
	 * Clears the cache with all none static Components.
	 * 
	 */
	public synchronized  void clearNonStaticCache() {
		/*
		ArrayList propsToRemove = new ArrayList();

		// check which properties should be removed from the cache
		Iterator iter = cache.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			StaticProperty property = (StaticProperty) cache.get(key);

			if (property instanceof Property) {
				// we are dealing with a non static property, so remove it from the
				// cache
				propsToRemove.add(key);
			}
		}

		// remove the properties from the cache
		iter = propsToRemove.iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			cache.remove(key);
		}
		
		*/
	}

	private synchronized StaticProperty addToCache(String key, StaticProperty property) {
		// TODO remove adding the property
		cache.put(key, property);
		return property;
	}

	/**
	 * Returns an ExplicitProperty instance based of the passed parameters.
	 * Explicit properties have to be created via this factory because during
	 * run-time only it's id is known. This factory determines the type of the
	 * property by retrieving the property definition and creates the
	 * corresponding explicit property
	 * 
	 * @param uol
	 *          Uol the uol for which this property was defined
	 * @param run
	 *          Run the run for which this property was instantiated
	 * @param user
	 *          User the user who owns this property
	 * @param propId
	 *          String the id of the property as defined in IMS-LD
	 * @throws PropertyException
	 *           thrown when the operation fails
	 * @return ExplicitProperty the newly created ExplicitProperty
	 */
	public ExplicitProperty getProperty(Uol uol, Run run, User user, String propId) throws PropertyException {

		try {
			String key = getKey(uol, run, user, propId);
			ExplicitProperty property = (ExplicitProperty) fetchFromCache(key);

			if (property == null) {

				// try to find find the property definition
				PropertyDefFacade pdf = new PropertyDefFacade(uol.getId(), propId);

				// determine the type of the property
				String dataType = pdf.getDto().getDataType();

				// now create the property
				property = createProperty(uol, run, user, propId, ExplicitPropertyDef.getPropertyType(dataType));

			}
			return property;
		} catch (FinderException ex) {
			throw new PropertyNotFoundException(ex);
		}

	}

	/**
	 * This method returns the local personal content of a Component. It should be
	 * used in situation where the type of Component is known. This method will
	 * determine the type first and create the appropriate Component accordingly.
	 * Finally the content is returned.
	 * 
	 * @param uol
	 *          Uol the uol for which this component was defined
	 * @param run
	 *          Run the run for which this component was instantiated
	 * @param user
	 *          User the user for which this component was instantiated
	 * @param propId
	 *          String the identifier of the Component as defined in the IMS LD
	 *          instance
	 * @param dataType
	 *          String the data type of the Component to be fetched
	 * @return String the XML fragment representing the local personal content
	 * @throws PropertyException
	 *           wheneve the operation fails
	 * @throws EJBException
	 *           if the data type was unknown
	 */
	public LocalPersonalContent getLocalPersonalContent(Uol uol, Run run, User user, String propId, String dataType)
			throws PropertyException {

		String key = getKey(uol, run, user, propId);
		LocalPersonalContent property = (LocalPersonalContent) fetchFromCache(key);

		if (property == null) {
			if (ActivityStructurePropertyDef.DATATYPE.equals(dataType)) {
				return getActivityStructure(uol, run, user, propId);
			} else if (LearningActivityPropertyDef.DATATYPE.equals(dataType)) {
				return getLearningActivity(uol, run, user, propId);
			} else if (SupportActivityPropertyDef.DATATYPE.equals(dataType)) {
				return getSupportActivity(uol, run, user, propId);
			} else if (RolePartPropertyDef.DATATYPE.equals(dataType)) {
				return getRolePart(uol, run, user, propId);
			} else if (ActPropertyDef.DATATYPE.equals(dataType)) {
				return getAct(uol, run, user, propId);
			} else if (PlayPropertyDef.DATATYPE.equals(dataType)) {
				return getPlay(uol, run, user, propId);
			} else if (LearningObjectPropertyDef.DATATYPE.equals(dataType)) {
				return getLearningObject(uol, run, user, propId);
			} else if (EnvironmentPropertyDef.DATATYPE.equals(dataType)) {
				return getEnvironment(uol, run, user, propId);
			} else if (UnitOfLearningPropertyDef.DATATYPE.equals(dataType)) {
				return getUnitOfLearning(uol, run, user, propId);
			} else if (SendMailPropertyDef.DATATYPE.equals(dataType)) {
				return getSendMail(uol, run, user, propId);
			} else if (MonitorPropertyDef.DATATYPE.equals(dataType)) {
				return getMonitorObject(uol, run, user, propId);
			} else {
				Logger logger = Logger.getLogger(this.getClass());
				logger.error("Unknown datatype encountered: \"" + dataType + "\"");
				throw new EJBException("Unknown datatype encountered: \"" + dataType + "\"");
			}
		}
		return property;
	}

	/**
	 * Return an ExplicitProperty for the passed parameters. The method acts as a
	 * factory method in cases the type of the property is known.
	 * 
	 * @param uol
	 *          Uol the uol for which this component was defined
	 * @param run
	 *          Run the run for which this component was instantiated
	 * @param user
	 *          User the user for which this component was instantiated
	 * @param propId
	 *          String the identifier of the Component as defined in the IMS LD
	 *          instance
	 * @param type
	 *          int the integer value for the data type of the explicit properties
	 * @throws PropertyException
	 *           whenever the operation fails
	 * @return ExplicitProperty the property that was found or null if no property
	 *         was found
	 */
	public ExplicitProperty getProperty(Uol uol, Run run, User user, String propId, int type) throws PropertyException {
		String key = getKey(uol, run, user, propId);
		ExplicitProperty property = (ExplicitProperty) fetchFromCache(key);
		if (property == null) {
			property = createProperty(uol, run, user, propId, type);
		}
		return property;
	}

	/**
	 * Retrieves the ExplicitPropertyDef of a explicit property with global scope.
	 * 
	 * @param uolId
	 *          int the database id of the Uol containing the reference to this
	 *          property
	 * @param propId
	 *          String identifier for local reference to the explicit property as
	 *          defined in the IMS LD instance
	 * @param uri
	 *          Sting the uri action as GUID for this global property.
	 * @return ExplicitPropertyDef the property definition for this global
	 *         property
	 * @throws PropertyException
	 *           whenever no existing defintion for this property could be found
	 */
	public ExplicitPropertyDef getExplicitGlobalPropertyDef(int uolId, String propId, String uri)
			throws PropertyException {
		// try to find find the property definition
		try {
			PropertyDefFacade pdf = new PropertyDefFacade(uri);

			return createExplicitPropertyDef(uolId, propId, pdf.getDto());
		} catch (FinderException ex) {
			throw new PropertyException(ex);
		}
	}

	/**
	 * Returns an ExplicitPropertyDef instance based of the passed parameters.
	 * Explicit properties definition have to be created via this factory because
	 * during run-time only it's id is known. This factory determines the type of
	 * the property by retrieving the property definition and creates the
	 * corresponding explicit property definition.
	 * 
	 * @param uolId
	 *          int the unit of learning id
	 * @param propId
	 *          String the id of the property as defined in IMS-LD
	 * @throws PropertyException
	 *           thrown when a xml error occured when parsing the persisted data
	 * @return ExplicitPropertyDef the newly created ExplicitPropertyDef
	 */
	public ExplicitPropertyDef getExplicitPropertyDef(int uolId, String propId) throws PropertyException {
		// try to find find the property definition
		try {
			PropertyDefFacade pdf = new PropertyDefFacade(uolId, propId);

			return createExplicitPropertyDef(uolId, propId, pdf.getDto());
		} catch (FinderException ex) {
			throw new PropertyException(ex);
		}
	}

	private ExplicitPropertyDef createExplicitPropertyDef(int uolId, String propId, PropertyDefDto dto)
			throws PropertyException {
		ExplicitPropertyDef result = null;

		if (dto.getDataType().equals(StringPropertyDef.DATATYPE)) {
			result = new StringPropertyDef(uolId, propId, dto);
		} else if (dto.getDataType().equals(IntegerPropertyDef.DATATYPE)) {
			result = new IntegerPropertyDef(uolId, propId, dto);
		} else if (dto.getDataType().equals(TextPropertyDef.DATATYPE)) {
			result = new TextPropertyDef(uolId, propId, dto);
		} else if (dto.getDataType().equals(BooleanPropertyDef.DATATYPE)) {
			result = new BooleanPropertyDef(uolId, propId, dto);
		} else if (dto.getDataType().equals(DateTimePropertyDef.DATATYPE)) {
			result = new DateTimePropertyDef(uolId, propId, dto);
		} else if (dto.getDataType().equals(DurationPropertyDef.DATATYPE)) {
			result = new DurationPropertyDef(uolId, propId, dto);
		} else if (dto.getDataType().equals(RealPropertyDef.DATATYPE)) {
			result = new RealPropertyDef(uolId, propId, dto);
		} else if (dto.getDataType().equals(UriPropertyDef.DATATYPE)) {
			result = new UriPropertyDef(uolId, propId, dto);
		} else if (dto.getDataType().equals(FilePropertyDef.DATATYPE)) {
			result = new FilePropertyDef(uolId, propId, dto);
		} else {
			throw new PropertyTypeException("Property Type " + dto.getDataType() + "not known");
		}
		return result;
	}

	/**
	 * Returns the Component containg the class visibilities.
	 * 
	 * @param uol
	 *          Uol which refers to the classes
	 * @param run
	 *          Run which refered to the classes
	 * @param user
	 *          User for which the class visibilties are set
	 * @param propId
	 *          String the id of the component as defined by CopperCore
	 * @return ClassProperty component containg the class visibilities
	 * @throws PropertyException
	 *           whenever the operation fails
	 */
	public ClassProperty getClasses(Uol uol, Run run, User user, String propId) throws PropertyException {
		String key = getKey(uol, run, user, propId);
		ClassProperty property = (ClassProperty) fetchFromCache(key);
		if (property == null) {
			property = new ClassProperty(uol, run, user, propId);
			addToCache(key, property);
		}
		return property;
	}

	/**
	 * Return a LearningActivityProperty based on the passed parameters.
	 * 
	 * @param uol
	 *          Uol which defined this component
	 * @param run
	 *          Run to which this component belongs
	 * @param user
	 *          User the owner of this component
	 * @param propId
	 *          the id of the component as defined in IMS LD
	 * @return LearningActivtyProperty the component which was requested
	 * @throws PropertyException
	 *           whenever the operation fails
	 */
	public LearningActivityProperty getLearningActivity(Uol uol, Run run, User user, String propId)
			throws PropertyException {
		String key = getKey(uol, run, user, propId);
		LearningActivityProperty property = (LearningActivityProperty) fetchFromCache(key);
		if (property == null) {
			property = new LearningActivityProperty(uol, run, user, propId);
			// addToCache(key, property);
		}
		return property;
	}

	/**
	 * Return a ActivityStructureProperty based on the passed parameters.
	 * 
	 * @param uol
	 *          Uol which defined this component
	 * @param run
	 *          Run to which this component belongs
	 * @param user
	 *          User the owner of this component
	 * @param propId
	 *          the id of the component as defined in IMS LD
	 * @return ActivityStructureProperty the component which was requested
	 * @throws PropertyException
	 *           whenever the operation fails
	 */
	public ActivityStructureProperty getActivityStructure(Uol uol, Run run, User user, String propId)
			throws PropertyException {
		String key = getKey(uol, run, user, propId);
		ActivityStructureProperty property = (ActivityStructureProperty) fetchFromCache(key);
		if (property == null) {
			property = new ActivityStructureProperty(uol, run, user, propId);
			// addToCache(key, property);
		}
		return property;
	}

	/**
	 * Return a SupportActivityProperty based on the passed parameters.
	 * 
	 * @param uol
	 *          Uol which defined this component
	 * @param run
	 *          Run to which this component belongs
	 * @param user
	 *          User the owner of this component
	 * @param propId
	 *          the id of the component as defined in IMS LD
	 * @return SupportActivityProperty the component which was requested
	 * @throws PropertyException
	 *           whenever the operation fails
	 */
	public SupportActivityProperty getSupportActivity(Uol uol, Run run, User user, String propId)
			throws PropertyException {
		String key = getKey(uol, run, user, propId);
		SupportActivityProperty property = (SupportActivityProperty) fetchFromCache(key);
		if (property == null) {
			property = new SupportActivityProperty(uol, run, user, propId);
			// addToCache(key, property);
		}
		return property;
	}

	/**
	 * Return a RolePartProperty based on the passed parameters.
	 * 
	 * @param uol
	 *          Uol which defined this component
	 * @param run
	 *          Run to which this component belongs
	 * @param user
	 *          User the owner of this component
	 * @param propId
	 *          the id of the component as defined in IMS LD
	 * @return RolePartProperty the component which was requested
	 * @throws PropertyException
	 *           whenever the operation fails
	 */
	public RolePartProperty getRolePart(Uol uol, Run run, User user, String propId) throws PropertyException {
		String key = getKey(uol, run, user, propId);
		RolePartProperty property = (RolePartProperty) fetchFromCache(key);
		if (property == null) {
			property = new RolePartProperty(uol, run, user, propId);
			// addToCache(key, property);
		}
		return property;
	}

	/**
	 * Return a ActProperty based on the passed parameters.
	 * 
	 * @param uol
	 *          Uol which defined this component
	 * @param run
	 *          Run to which this component belongs
	 * @param user
	 *          User the owner of this component
	 * @param propId
	 *          the id of the component as defined in IMS LD
	 * @return ActProperty the component which was requested
	 * @throws PropertyException
	 *           whenever the operation fails
	 */
	public ActProperty getAct(Uol uol, Run run, User user, String propId) throws PropertyException {
		String key = getKey(uol, run, user, propId);
		ActProperty property = (ActProperty) fetchFromCache(key);
		if (property == null) {
			property = new ActProperty(uol, run, user, propId);
			// addToCache(key, property);
		}
		return property;
	}

	/**
	 * Return a PlayProperty based on the passed parameters.
	 * 
	 * @param uol
	 *          Uol which defined this component
	 * @param run
	 *          Run to which this component belongs
	 * @param user
	 *          User the owner of this component
	 * @param propId
	 *          the id of the component as defined in IMS LD
	 * @return PlayProperty the component which was requested
	 * @throws PropertyException
	 *           whenever the operation fails
	 */
	public PlayProperty getPlay(Uol uol, Run run, User user, String propId) throws PropertyException {
		String key = getKey(uol, run, user, propId);
		PlayProperty property = (PlayProperty) fetchFromCache(key);
		if (property == null) {
			property = new PlayProperty(uol, run, user, propId);
			// addToCache(key, property);
		}
		return property;
	}

	/**
	 * Return a LearningObjectProperty based on the passed parameters.
	 * 
	 * @param uol
	 *          Uol which defined this component
	 * @param run
	 *          Run to which this component belongs
	 * @param user
	 *          User the owner of this component
	 * @param propId
	 *          the id of the component as defined in IMS LD
	 * @return LearningObjectProperty the component which was requested
	 * @throws PropertyException
	 *           whenever the operation fails
	 */
	public LearningObjectProperty getLearningObject(Uol uol, Run run, User user, String propId) throws PropertyException {
		String key = getKey(uol, propId);
		LearningObjectProperty property = (LearningObjectProperty) fetchFromCache(key);
		if (property == null) {
			property = new LearningObjectProperty(uol, run, user, propId);
			// addToCache(key, property);
		}
		return property;
	}

	/**
	 * Return a MonitorProperty based on the passed parameters.
	 * 
	 * @param uol
	 *          Uol which defined this component
	 * @param run
	 *          Run to which this component belongs
	 * @param user
	 *          User the owner of this component
	 * @param propId
	 *          the id of the component as defined in IMS LD
	 * @return MonitorProperty the component which was requested
	 * @throws PropertyException
	 *           whenever the operation fails
	 */
	public MonitorProperty getMonitorObject(Uol uol, Run run, User user, String propId) throws PropertyException {
		String key = getKey(uol, propId);
		MonitorProperty property = (MonitorProperty) fetchFromCache(key);
		if (property == null) {
			property = new MonitorProperty(uol, run, user, propId);
			// addToCache(key, property);
		}
		return property;
	}

	/**
	 * Return a SendMailProperty based on the passed parameters.
	 * 
	 * @param uol
	 *          Uol which defined this component
	 * @param run
	 *          Run to which this component belongs
	 * @param user
	 *          User the owner of this component
	 * @param propId
	 *          the id of the component as defined in IMS LD
	 * @return SendMailProperty the component which was requested
	 * @throws PropertyException
	 *           whenever the operation fails
	 */
	public SendMailProperty getSendMail(Uol uol, Run run, User user, String propId) throws PropertyException {
		String key = getKey(uol, propId);
		SendMailProperty property = (SendMailProperty) fetchFromCache(key);
		if (property == null) {
			property = new SendMailProperty(uol, run, user, propId);
			// addToCache(key, property);
		}
		return property;
	}
	
	
	/**
	 * Return a ConferenceProperty based on the passed parameters.
	 * 
	 * @param uol
	 *          Uol which defined this component
	 * @param run
	 *          Run to which this component belongs
	 * @param user
	 *          User the owner of this component
	 * @param propId
	 *          the id of the component as defined in IMS LD
	 * @return SendMailProperty the component which was requested
	 * @throws PropertyException
	 *           whenever the operation fails
	 */
	public ConferenceProperty getConference(Uol uol, Run run, User user, String propId) throws PropertyException {
		String key = getKey(uol, propId);
		ConferenceProperty property = (ConferenceProperty) fetchFromCache(key);
		if (property == null) {
			property = new ConferenceProperty(uol, run, user, propId);
			// addToCache(key, property);
		}
		return property;
	}	
	

	/**
	 * Return a EnvironmentProperty based on the passed parameters.
	 * 
	 * @param uol
	 *          Uol which defined this component
	 * @param run
	 *          Run to which this component belongs
	 * @param user
	 *          User the owner of this component
	 * @param propId
	 *          the id of the component as defined in IMS LD
	 * @return EnvironmentProperty the component which was requested
	 * @throws PropertyException
	 *           whenever the operation fails
	 */
	public EnvironmentProperty getEnvironment(Uol uol, Run run, User user, String propId) throws PropertyException {
		String key = getKey(uol, run, user, propId);
		EnvironmentProperty property = (EnvironmentProperty) fetchFromCache(key);
		if (property == null) {
			property = new EnvironmentProperty(uol, run, user, propId);
			// addToCache(key, property);
		}
		return property;
	}

	/**
	 * Return a ActivityTreeProperty based on the passed parameters.
	 * 
	 * @param uol
	 *          Uol which defined this component
	 * @param propId
	 *          the id of the component as defined in IMS LD
	 * @return ActivityTreeProperty the component which was requested
	 */
	public ActivityTreeProperty getActivityTree(Uol uol, String propId) {
		String key = getKey(uol, propId);
		ActivityTreeProperty property = (ActivityTreeProperty) fetchFromCache(key);
		if (property == null) {
			property = new ActivityTreeProperty(uol, propId);
			// addToCache(key, property);
		}
		return property;
	}

	/**
	 * Return a EnvironmentTreeProperty based on the passed parameters.
	 * 
	 * @param uol
	 *          Uol which defined this component
	 * @param propId
	 *          the id of the component as defined in IMS LD
	 * @return EnvironmentTreeProperty the component which was requested
	 */
	public EnvironmentTreeProperty getEnvironmentTree(Uol uol, String propId) {
		String key = getKey(uol, propId);
		EnvironmentTreeProperty property = (EnvironmentTreeProperty) fetchFromCache(key);
		if (property == null) {
			property = new EnvironmentTreeProperty(uol, propId);
			// addToCache(key, property);
		}
		return property;
	}

	/**
	 * Return a UnitOfLearningProperty based on the passed parameters.
	 * 
	 * @param uol
	 *          Uol which defined this component
	 * @param run
	 *          Run to which this component belongs
	 * @param user
	 *          User the owner of this component
	 * @param propId
	 *          the id of the component as defined in IMS LD
	 * @return UnitOfLearningProperty the component which was requested
	 * @throws PropertyException
	 *           whenever the operation fails
	 */
	public UnitOfLearningProperty getUnitOfLearning(Uol uol, Run run, User user, String propId) throws PropertyException {
		String key = getKey(uol, run, user, propId);
		UnitOfLearningProperty property = (UnitOfLearningProperty) fetchFromCache(key);
		if (property == null) {
			property = new UnitOfLearningProperty(uol, run, user, propId);
			// addToCache(key, property);
		}
		return property;
	}

	/**
	 * Return a ExpressionProperty based on the passed parameters.
	 * 
	 * @param uol
	 *          Uol which defined this component User the owner of this component
	 * @param propId
	 *          the id of the component as defined in IMS LD
	 * @return ExpressionProperty the component which was requested
	 */
	public ExpressionProperty getExpression(Uol uol, String propId) {
		String key = getKey(uol, propId);
		ExpressionProperty property = (ExpressionProperty) fetchFromCache(key);
		if (property == null) {
			property = new ExpressionProperty(uol, propId);
			addToCache(key, property);
		}
		return property;
	}

	private String getKey(Uol uol, Run run, User user, String propId) {
		return uol.getId() + "." + run.getId() + "." + user.getId() + "." + propId;
	}

	private String getKey(Uol uol, String propId) {
		return uol.getId() + "." + propId;
	}

	private ExplicitProperty createProperty(Uol uol, Run run, User user, String propId, int type)
			throws PropertyException, TypeCastException {
		ExplicitProperty property = null;

		switch (type) {
		case LDDataType.LDSTRING: {
			property = new StringProperty(uol, run, user, propId);
			break;
		}

		case LDDataType.LDINTEGER: {
			property = new IntegerProperty(uol, run, user, propId);
			break;
		}

		case LDDataType.LDTEXT: {
			property = new TextProperty(uol, run, user, propId);
			break;
		}

		case LDDataType.LDBOOLEAN: {
			property = new BooleanProperty(uol, run, user, propId);
			break;
		}

		case LDDataType.LDDATETIME: {
			property = new DateTimeProperty(uol, run, user, propId);
			break;
		}

		case LDDataType.LDDURATION: {
			property = new DurationProperty(uol, run, user, propId);
			break;
		}

		case LDDataType.LDURI: {
			property = new UriProperty(uol, run, user, propId);
			break;
		}

		case LDDataType.LDFILE: {
			property = new FileProperty(uol, run, user, propId);
			break;
		}

		case LDDataType.LDREAL: {
			property = new RealProperty(uol, run, user, propId);
			break;
		}

		default:
			throw new PropertyTypeException("Property Type " + type + "not known");
		}
		// construct the key for the cache
		// String key = getKey(uol, run, user, propId);

		// cache the create property for easy and quick retrieval
		// addToCache(key, property);

		return property;
	}
}
