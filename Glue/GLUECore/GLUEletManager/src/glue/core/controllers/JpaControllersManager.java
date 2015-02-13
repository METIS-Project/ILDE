/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of GlueletManager.
 * 
 * GlueletManager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * GlueletManager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.core.controllers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * Manager providing centralized access to JPA controllers for Internal Registry and Instances Repository.
 * 
 * JPA controllers are handled as singletons.
 * 
 * TODO Study how this will affect concurrency; are JPA controllers thread safe? is a controller object per table enough for a complex installation?
 * 
 * @author  David A. Velasco <davivel@gsic.uva.es>
 * @version 2012092501
 * @package glue.core.controllers
 */

public class JpaControllersManager {
	
	/** JPA Controller for Tools table */
	protected static ToolsJpaController toolsController = null;
	
	/** JPA Controller for ToolImplementations table */
	protected static ToolImplementationsJpaController toolImplementationsController = null;
	
	/** JPA Controller for ImplementationModels table */
	protected static ImplementationModelsJpaController implementationModelsController = null;
	
	/** JPA Controller for ToolServices table */
	protected static ToolServicesJpaController toolServicesController = null;
	
	/** JPA Controller for ImplementationAdapters table */
	protected static ImplementationAdaptersJpaController implementationAdaptersController = null;
	
	/** JPA Controller for Gluelets table */
	protected static GlueletsJpaController glueletsController = null;
	
	/** Entity manager to access Internal Registry - ONE for all the tables in the same persistence unit */
	protected static EntityManager InternalRegistryEM;
	
	/** Entity manager to access the Gluelets Repository - ONE for all the tables in the same persistence unit */
	protected static EntityManager GlueletsRepositoryEM;
	
	
	/** Static initializer */
	static {
		/// Initializes the entity manager to access Internal Registry
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("InternalRegistryPersistenceUnit"); 	// parameter must be synchronized with persistence-unit@name at META-INF/persistence.xml
        																										 // TODO this can be avoided by injecting the emf with @PersistenceUnit or @PersistenceContext ?
        String JDBC_driver = (String)emf.getProperties().get("javax.persistence.jdbc.driver");
        try {
        	Class.forName(JDBC_driver);
        } catch (ClassNotFoundException e) {
        	throw new RuntimeException(JDBC_driver + " not found; access to Internal Registry will not be possible"); 
        }
        
        InternalRegistryEM = emf.createEntityManager();
        
        
		/// Initializes the entity manager to access GLUElets Repository
        emf = Persistence.createEntityManagerFactory("GlueletsRepositoryPersistenceUnit"); 	// parameter must be synchronized with persistence-unit@name at META-INF/persistence.xml
        																					// TODO this can be avoided by injecting the emf with @PersistenceUnit or @PersistenceContext ?
        JDBC_driver = (String)emf.getProperties().get("javax.persistence.jdbc.driver");
        try {
        	Class.forName(JDBC_driver);
        } catch (ClassNotFoundException e) {
        	throw new RuntimeException(JDBC_driver + " not found; access to Internal Registry will not be possible"); 
        }

        GlueletsRepositoryEM = emf.createEntityManager();
	}
	
	
	/** Closes the entity managers */
	public static void closeEntityManagers() {
		InternalRegistryEM.close();
		GlueletsRepositoryEM.close();
		
		toolsController = null;
		toolImplementationsController = null;
		implementationModelsController = null;
		toolServicesController = null;
		implementationAdaptersController = null;
		glueletsController = null;
	}

	
	/** Getter for ToolsJpaController */
	public static ToolsJpaController getToolsController () {
		if (toolsController == null)
			toolsController = new ToolsJpaController(InternalRegistryEM);
		return toolsController;
	}

	/** Getter for ToolImplementationsJpaController */
	public static ToolImplementationsJpaController getToolImplementationsController () {
		if (toolImplementationsController == null)
			toolImplementationsController = new ToolImplementationsJpaController(InternalRegistryEM);
		return toolImplementationsController;
	}

	/** Getter for ImplementationModelsJpaController */
	public static ImplementationModelsJpaController getImplementationModelsController () {
		if (implementationModelsController == null)
			implementationModelsController = new ImplementationModelsJpaController(InternalRegistryEM);
		return implementationModelsController;
	}

	/** Getter for ToolServicesJpaController */
	public static ToolServicesJpaController getToolServicesController () {
		if (toolServicesController == null)
			toolServicesController = new ToolServicesJpaController(InternalRegistryEM);
		return toolServicesController;
	}

	/** Getter for ImplementationAdaptersJpaController */
	public static ImplementationAdaptersJpaController getImplementationAdaptersController () {
		if (implementationAdaptersController == null)
			implementationAdaptersController = new ImplementationAdaptersJpaController(InternalRegistryEM);
		return implementationAdaptersController;
	}

	/** Getter for GlueletsController */
	public static GlueletsJpaController getGlueletsController() {
		if (glueletsController == null)
			glueletsController = new GlueletsJpaController(GlueletsRepositoryEM);
		return glueletsController;
	}
	
	
}
