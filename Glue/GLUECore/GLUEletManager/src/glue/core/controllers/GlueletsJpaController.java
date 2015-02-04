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
import glue.core.entities.Gluelet;

import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * JPA controller class that provides access to glue.core.entities.Gluelet objects (data stored in the Gluelets table in the Gluelets Repository)
 * 
 * @author		David A. Velasco, 	based upon code by juaase
 * @contributor	Javier Enrique Hoyos Torio
 * @version 	2012092501
 * @package 	glue.core.controllers
 */
public class GlueletsJpaController {
	

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
	 * Entity Manager to access the persistence unit containing the Gluelets table.  
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
     * @param em		Entity manager initialized to access the persistence unit containing the Gluelets table 
     */
    public GlueletsJpaController(EntityManager em) {
    	if (em == null)
    		throw new IllegalArgumentException("em : Entity manager can't be null");
    	this.em = em;
        /*emf = Persistence.createEntityManagerFactory("GlueletsRepositoryPersistenceUnit");	// parameter must be sync with persistence-unit@name at META-INF/persistence.xml
        																					// TODO this can be avoided by injecting the emf with @PersistenceUnit or @PersistenceContext ?
    	String JDBC_driver = (String)emf.getProperties().get("javax.persistence.jdbc.driver");
    	try {
    		Class.forName(JDBC_driver);
    	} catch (ClassNotFoundException e) {
    		throw new RuntimeException(JDBC_driver + " not found; access to Gluelets Repository will not be possible"); 
    	}*/
    }

    
    /**
     * Insert Gluelet object into the database
     */
    public void create(Gluelet gluelet) {
    	synchronized(em) {
        //EntityManager em = null;
        //try {
        	// insertion modified after removing AUTO_INCREMENT from all the tables;
        	//em = getEntityManager();
        	em.getTransaction().begin();
        	int nextId = getMaxId() + 1;	// getMaxId() performs a SELECT operation for every insertion; SLOW, but fully portable
        	gluelet.setId(nextId);
        	em.persist(gluelet);
        	em.getTransaction().commit();
        	
        /*} finally {
            if (em != null) {
                em.close();
            }
        }*/
    	}
    }

    
    /**
     * Remove a Gluelet object from the database
     * 
     * @param 	id	Integer		Gluelet identifier in the database.
     * @throws 	NonexistentEntityException
     */
    public void destroy(Integer id) throws NonexistentEntityException {
    	synchronized(em) {
    	//EntityManager em = null;
        //try {
            //em = getEntityManager();
            em.getTransaction().begin();
            Gluelet gluelet;
            /*try {
                gluelet = em.getReference(Gluelet.class, id);	// gets a row by id, probably LAZYLY
                gluelet.getId();								// to enforce the real access to the row state
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The Gluelet object with id " + id + " no longer exists in Gluelets Repository.", enfe);
            }*/
        	gluelet = em.find(Gluelet.class, id);
        	if (gluelet == null) 
        		throw new NonexistentEntityException("The Gluelet object with id " + id + " no longer exists in Gluelets Repository.");
            em.remove(gluelet);
            em.getTransaction().commit();
            
        /*} finally {
            if (em != null) {
                em.close();
            }
        }*/
    	}
    }

    
    /**
     * Find a gluelet in the Gluelets Repository from its database id. 
     * 
     * @param id	Gluelet identifier in the database.
     * @return		Gluelet, if existing.
     */
    public Gluelet findGluelet(Integer id) {
    	synchronized(em) {
        //EntityManager em = getEntityManager();
        //try {
            return em.find(Gluelet.class, id);
            
        /*} finally {
            em.close();
        }*/
    	}
    }
    
    
    /**
     * Get the maximum value of the column 'id' in the gluelets table
     * 
     * @return 	Maximum value of the column 'id' in the gluelets table.
     */
    protected int getMaxId() {
 	   //EntityManager em = getEntityManager();
 	   //try {
            Query q = em.createNamedQuery("Gluelet.findMaxId");
            Iterator<Integer> it = q.getResultList().iterator();
            Integer result = it.next();
            return (result==null ? 0 : result);
            
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
     * Get the count of gluelet in the Gluelets Repository.
     * 
     * @return	Number of gluelet in the Gluelets Repository.
     *-/
    public int getGlueletCount() {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Gluelet> rt = cq.from(Gluelet.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    */
    
    
    /*-*
     * 	Save changes over a previously existing Gluelet object into the database
     *  
     * @param  gluelet		Gluelet object, gluelet instance data
     * @throws NonexistentEntityException
     * @throws Exception
     *-/
    public void edit(Gluelet gluelet) throws NonexistentEntityException, Exception {
        //EntityManager em = null;
        try {
            //em = getEntityManager();			// new EntityManager instance ; TODO check if this is necessary every time
            em.getTransaction().begin();		// start a resource transaction
            gluelet = em.merge(gluelet);		// merge the state of the given entity into the current persistence context
            em.getTransaction().commit();		// write gluelet to the database; en of transaction
            
        //} catch (Exception ex) {
        } catch (RuntimeException ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = gluelet.getId();
                if (findGluelet(id) == null) {
                    throw new NonexistentEntityException("The Gluelet object with id " + id + " no longer exists in Gluelets Repository.", ex);
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
     * Find all the gluelet in the Gluelets Repository
     * 
     * @return	List of all existing Gluelet objects.
     *-/
    @PersistenceContext
    public List<Gluelet> findGlueletEntities() {
        return findGlueletEntities(true, -1, -1);
    }
    */
    

    /*-* 
     * Find a list of gluelet in the Gluelets Repository, with a limited size.
     * 
     * @param 	maxResults	int		Maximum number of objects to be returned.
     * @param 	firstResult	int		Position in the full list of the first Gluelet object to return. 	
     * @return	List of Gluelet objects.
     *-/
    public List<Gluelet> findGlueletEntities(int maxResults, int firstResult) {
        return findGlueletEntities(false, maxResults, firstResult);
    }
    */
    
    /*-*
     * Find a list of gluelet in the Gluelets Repository.
     * 
     * Convenience method for supporting the other findGlueletEntities methods.
     *  
     * @param 	all			boolean		Flag indicating if full list of gluelet is required.
     * @param 	maxResults	int			Maximum number of gluelet to be returned.
     * @param 	firstResult	int			Position in the full list of the first Gluelet object to return.
     * @return	List of Gluelet objects built according to parameter values.
     *-/
    protected List<Gluelet> findGlueletEntities(boolean all, int maxResults, int firstResult) {
        //EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Gluelet.class));
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

    
}
