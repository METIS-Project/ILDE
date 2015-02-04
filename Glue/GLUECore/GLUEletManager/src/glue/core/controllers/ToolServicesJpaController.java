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

import glue.core.controllers.exceptions.NonexistentEntityException;
import glue.core.entities.ToolService;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaQuery;

/**
 * JPA controller class that provides access to glue.core.entities.ToolService objects (registered tool services in the ToolServices table in Internal Registry)
 * 
 * @author		David A. Velasco, 	based upon code by juaase
 * @version 	2012092501
 * @package 	glue.core.controllers
 */

public class ToolServicesJpaController {

	/*-*
	 * (from Javadoc)
	 * 
	 *  Interface used to interact with the entity manager factory for the persistence unit.
	 * 
	 *  When the application has finished using the entity manager factory, and/or at application shutdown, the application should close 
	 *  the entity manager factory. Once an EntityManagerFactory has been closed, all its entity managers are considered to be in the closed state.
	 *-/ 
    protected EntityManagerFactory emf = null;
    */
    
	/**
	 * Entity Manager to access the persistence unit containing the Implementation Models table.  
	 *  
	 * From EntityManager Javadoc:
	 * 
	 * 		Interface used to interact with the persistence context. 
	 *  
	 *  	An EntityManager instance is associated with a persistence context. A persistence context is a set of entity instances in which for any persistent 
	 *  	entity identity there is a unique entity instance. Within the persistence context, the entity instances and their lifecycle are managed. 
	 *  	The EntityManager API is used to create and remove persistent entity instances, to find entities by their primary key, and to query over entities.
	 *  
	 *  	The set of entities that can be managed by a given EntityManager instance is defined by a persistence unit. A persistence unit defines the 
	 *  	set of all classes that are related or grouped by the application, and which must be colocated in their mapping to a single database.
     */
	protected EntityManager em = null;
	
	
    /**
     * Constructor
     * 
     * @param em		Entity manager initialized to access the persistence unit containing the Tool Services table
     */
    public ToolServicesJpaController(EntityManager em) {
    	if (em == null)
    		throw new IllegalArgumentException("em : Entity manager can't be null");
    	this.em = em;
        /*emf = Persistence.createEntityManagerFactory("InternalRegistryPersistenceUnit");	// parameter must be sync with persistence-unit@name at META-INF/persistence.xml
    	String JDBC_driver = (String)emf.getProperties().get("javax.persistence.jdbc.driver");
    	try {
    		Class.forName(JDBC_driver);
    	} catch (ClassNotFoundException e) {
    		throw new RuntimeException(JDBC_driver + " not found; access to Internal Registry will not be possible");
    		//System.err.println(JDBC_driver + " not found; access to Internal Registry will not be possible");
    	}*/
    }

    
    /**
     * Find a toolService in the Internal Registry from its database id. 
     * 
     * @param id	ToolService identifier in the database.
     * @return		ToolService info, if existing.
     */
    public ToolService findToolService(Integer id) {
        //EntityManager em = getEntityManager();
        //try {
            return em.find(ToolService.class, id);
        /*} finally {
            em.close();
        }*/
    }

    /*public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }*/
    

    //********* METHODS NEVER USED BEFORE ********** 
    // 	
    // If you need them, check the code after uncommenting
    
    
    /*-*
     * Insert a ToolService object in the database
     *-/
    public void create(ToolService toolService) {
        EntityManager em = null;
        try {
            em = getEntityManager();		// new EntityManager instance
            em.getTransaction().begin();	// start a resource transaction	
            em.persist(toolService);		// make toolService managed and persistent
            em.getTransaction().commit();	// write toolService to the database; end of transaction 
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    */

    
    /*-*
     * 	Save changes over a previously existing ToolService object into the database
     *  
     * @param  toolService		ToolService object
     * @throws NonexistentEntityException
     * @throws Exception
     *-/
    public void edit(ToolService toolService) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();			// new EntityManager instance ; TODO check if this is necessary every time
            em.getTransaction().begin();		// start a resource transaction
            toolService = em.merge(toolService);				// merge the state of the given entity into the current persistence context
            em.getTransaction().commit();		// write toolService to the database; en of transaction
        //} catch (Exception ex) {
        } catch (RuntimeException ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = toolService.getId();
                if (findToolService(id) == null) {
                    throw new NonexistentEntityException("The ToolService object with id " + id + " no longer exists in the Internal Registry.", ex);
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    */
    

    /*-*
     * Remove a ToolService object from the database
     * 
     * @param 	id	Integer		ToolService identifier in the database.
     * @throws 	NonexistentEntityException
     *-/
    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ToolService toolService;
            try {
                toolService = em.getReference(ToolService.class, id);
                toolService.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ToolService object with id " + id + " no longer exists in the Internal Registry.", enfe);
            }
            em.remove(toolService);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    */

    
    /*-*
     * Find all the registered toolServices in the Internal Registry.
     * 
     * @return	List of all the registered toolServices.
     *-/
    @PersistenceContext
    public List<ToolService> findToolServiceEntities() {
        return findToolServiceEntities(true, -1, -1);
    }
    */

    
    /*-* 
     * Find a list of toolServices in the Internal Registry, with a limited size.
     * 
     * @param 	maxResults	int		Maximum number of objects to be returned.
     * @param 	firstResult	int		Position in the full list of the first ToolService object to return. 	
     * @return	List of ToolService objects.
     *-/
    public List<ToolService> findToolServiceEntities(int maxResults, int firstResult) {
        return findToolServiceEntities(false, maxResults, firstResult);
    }
    */

    
    /*-*
     * Find a list of registered toolServices in the Internal Registry.
     * 
     * Convenience method for supporting the other findToolServiceEntities methods.
     *  
     * @param 	all			boolean		Flag indicating if full list is required.
     * @param 	maxResults	int			Maximum number of objects to be returned.
     * @param 	firstResult	int			Position in the full list of the first ToolService object to return.
     * @return	List of ToolService objects built according to parameter values.
     *-/
    protected List<ToolService> findToolServiceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ToolService.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
	*/
    
    
    /*-*
     * Get the count of registered toolServices in the Internal Registry.
     * 
     * @return	Number of toolServices in the Internal Registry.
     *-/
    public int getToolServiceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ToolService> rt = cq.from(ToolService.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
            
        } finally {
            em.close();
        }
    }
    */

}
