/**
 This file is part of GlueletManager.

 GlueletManager is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 GlueletManager is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use GlueletManager for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when GlueletManager is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute GlueletManager and/or modify it under the
   terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>
 ]
*/
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
