package glueps.core.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.model.InstancedActivity;
import glueps.core.model.LearningEnvironment;
import glueps.core.model.LearningEnvironmentInstallation;
import glueps.core.model.Participant;
import glueps.core.model.Resource;
import glueps.core.model.ToolInstance;
import glueps.core.persistence.entities.DeployEntity;
import glueps.core.persistence.entities.DeployVersionEntity;
import glueps.core.persistence.entities.DesignEntity;
import glueps.core.persistence.entities.LearningEnvironmentEntity;
import glueps.core.persistence.entities.LearningEnvironmentInstallationEntity;
import glueps.core.persistence.entities.OauthTokenEntity;
import glueps.core.persistence.entities.SectokenEntity;
import glueps.core.persistence.entities.UserEntity;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;


/**
 * Important note: all ids going in and out of the DB are in trimmed form! we perform preventive trims before inserting anything into the DB
 * @author lpps
 *
 */

public class JpaManager {
    // variables.
    public static JpaManager manager = null;
    //public static EntityManager em = null;

    public static EntityManager userem = null;
    public static EntityManager leem = null;
    public static EntityManager leinstallationsem = null;
    public static EntityManager designem = null;
    public static EntityManager deployem = null;
    public static EntityManager deployversionem = null;
    public static EntityManager sectem = null;
    public static EntityManager oauthtem = null;

    private static  JAXBContext jcDeploy = null;//created to avoid delays loading the deploy class
    
    /**
     * Constructor. Crea la unidad de persistencia.
     */
    public JpaManager() {
        String PERSISTENCEUNIT = "GLUEPSAR201401_PU";
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCEUNIT, System.getProperties());

        userem = factory.createEntityManager();
        leem = factory.createEntityManager();
        leinstallationsem = factory.createEntityManager();
        designem = factory.createEntityManager();
        deployem = factory.createEntityManager();
        deployversionem = factory.createEntityManager();
        sectem = factory.createEntityManager();
        oauthtem = factory.createEntityManager();
    }

    /**
     * Recupera una instancia de la clase.
     * @return Manejador.
     */
    public static JpaManager getInstance() {
        if (manager == null) {
            System.out.println("Creating JPA Manager");
            manager = new JpaManager();
            return manager;
        } else {
            return manager;
        }
    }
    
    
    
    //------------------------------------------------- USERS


    /**
     * Get a user from the DB
     */
    public UserEntity findUserByUsername(String login){
    	synchronized(userem){
	        try {
	            System.out.println("Retrieving user from database: [" + login + "]");
	            Query q = userem.createNamedQuery("UserEntity.findByLogin");
	            q.setParameter("login",login);
	            UserEntity u = (UserEntity) q.getSingleResult();
	
	            return u;
	        } catch (Exception e) {
	            return null;
	        }
    	}
    	
    }
    
    /**
     * Get a user from the DB
     */
    public UserEntity findUserById(long id){
    	synchronized(userem){

        try {
            System.out.println("Retrieving user from database: [" + id + "]");
            Query q = userem.createNamedQuery("UserEntity.findById");
            q.setParameter("id",id);
            UserEntity u = (UserEntity) q.getSingleResult();

            return u;
        } catch (Exception e) {
            return null;
        }
    	}
    	
    }
    
    /**
     * Insert a user in the DB... if the username exists, the record is updated
     */
    public void insertUser(UserEntity u) {
    	synchronized(userem) {
	    	UserEntity existingUser = findUserByUsername(u.getLogin());
	    	
	    	//We set the ID so that a new user is NOT generated, rather update the existing one
	    	if(existingUser!=null){
	            System.out.println("user "+existingUser.getLogin()+" already exists in DB: ["+existingUser.toString()+"] -> ["+u.toString()+"]");
	    		u.setId(existingUser.getId());
	    	}
	    	
	        EntityTransaction tx = userem.getTransaction();
	        tx.begin();
	        System.out.println("Inserting/updating user in DB: ["+u.toString()+"]");
	
	        //em.persist(u);
	        userem.merge(u);
	        userem.flush();
	
	        System.out.println("User inserted/updated in DB");
	
	        tx.commit();
    	}
    }
    

    
    //------------------------------------------------- LEARNING ENVIRONMENTS

    
    public LearningEnvironmentEntity findLEbyId(long id){
    	synchronized(leem){

        try {
            System.out.println("Retrieving LE from database: [" + id + "]");
            Query q = leem.createNamedQuery("LearningEnvironmentEntity.findById");
            q.setParameter("id",id);
            LearningEnvironmentEntity u = (LearningEnvironmentEntity) q.getSingleResult();

            return u;
        } catch (Exception e) {
            return null;
        }
    	
    	}
    }
    
    public List<LearningEnvironmentEntity> listLEs(){
    	
        try {
            System.out.println("Retrieving LEs from database");
            Query q = leem.createNamedQuery("LearningEnvironmentEntity.listAll");
            List<LearningEnvironmentEntity> les = q.getResultList();
            
            return les;
        } catch (Exception e) {
            return null;
        }
    	
    }
    
    


    
    /**
     * Convenience method that gets a LE from DB by passing it the (possibly url) ID
     */
    public LearningEnvironment findLEObjectById(String id){
    	
    	String trimmedId = trimId(id);
    	
    	long dbId=0;
    	try {
			dbId = Long.parseLong(trimmedId);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return null;
		}
    	
    	LearningEnvironmentEntity leEnt = findLEbyId(dbId);
    	if (leEnt == null){
    		return null;
    	}
    	LearningEnvironmentInstallationEntity leInstEnt = findLEInstallationById(leEnt.getInstallation());
    	if (leInstEnt == null){ 
    		return null;
    	}
    	
		LearningEnvironment le = new LearningEnvironment();
		le.setId(String.valueOf(leEnt.getId()));//we set the object id to the trimmed/db one, just in case the xml has the url
		
		le.setName(leEnt.getName());
		le.setType(leInstEnt.getType());
		try {
			le.setAccessLocation(new URL(leInstEnt.getAccessLocation()));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		le.setUserid(leEnt.getUserid());
		le.setCreduser(leEnt.getCreduser());
		le.setCredsecret(leEnt.getCredsecret());
		le.setInstallation(String.valueOf(leEnt.getInstallation()));
		le.setShowAR(leEnt.isShowAR());
		le.setShowVG(leEnt.isShowVG());
		
    	//le.setId(trimmedId);//we set the LE id to just the trimmed id (just in case the xml is erroneous)
    	
		System.out.println("Retrieved LE from database: "+le.toString());
		return le;    	
    }
    


	/**
     * Convenience method that gets LEs from DB 
     * @throws UnsupportedEncodingException 
     */
    public List<LearningEnvironment> listLEObjects() throws UnsupportedEncodingException{
    	
    	List<LearningEnvironmentEntity> les = listLEs();
    	
    	List<LearningEnvironment> leObjects = null;
    	if(les!=null){
    		leObjects = new ArrayList<LearningEnvironment>();
    		
    		for(LearningEnvironmentEntity lee : les){   			
    			LearningEnvironmentInstallationEntity leInstEnt = findLEInstallationById(lee.getInstallation());
    			if (leInstEnt!=null){
	    			LearningEnvironment le = new LearningEnvironment();
	    			le.setId(String.valueOf(lee.getId()));//we set the object id to the trimmed/db one, just in case the xml has the url
	    			
	    			le.setName(lee.getName());
	    			le.setType(leInstEnt.getType());
	    			try {
						le.setAccessLocation(new URL(leInstEnt.getAccessLocation()));
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					le.setUserid(lee.getUserid());
					le.setCreduser(lee.getCreduser());
					le.setCredsecret(lee.getCredsecret());
					le.setInstallation(String.valueOf(lee.getInstallation()));
					le.setShowAR(lee.isShowAR());
					le.setShowVG(lee.isShowVG());
	    			leObjects.add(le); 
    			}
    		}
    	}
    	return leObjects;
    }
    
    
    
    

	/**
     * Insert a LE in the DB, and return the id... if the id already exists, we update the record
     */
    public long insertLearningEnvironment(LearningEnvironmentEntity u) {
    	synchronized(leem) {
	    	LearningEnvironmentEntity existingLE = findLEbyId(u.getId());
	    	
	    	//We set the ID so that a new user is NOT generated, rather update the existing one
	    	if(existingLE!=null){
	            System.out.println("LE "+existingLE.getId()+" already exists in DB: ["+existingLE.toString()+"] -> ["+u.toString()+"]");
	    		u.setId(existingLE.getId());
	    	}
	    	
	        leem.getTransaction().begin();
	
	        System.out.println("Inserting/updating LE in DB: ["+u.toString()+"]");
	
	        //em.persist(u);
	        leem.merge(u);
	        leem.flush();
	
	        System.out.println("LE inserted/updated in DB");
	
	        leem.getTransaction().commit();
	        return u.getId();
    	}
    }
    
    
    public long insertLearningEnvironment(LearningEnvironment le) throws Exception{
    	long id;
    	if(le.getId()==null){
    		id = 0;
    	}else{
    		id = Long.valueOf(le.getId());
    	}

    	LearningEnvironmentEntity lee = new LearningEnvironmentEntity(id, le.getName(), le.getUserid(), le.getCreduser(), le.getCredsecret(), Long.valueOf(le.getInstallation()), le.isShowAR(), le.isShowVG());
    	return insertLearningEnvironment(lee);   	
    }
    
    public void deleteLearningEnvironment(String id) {
       	String trimmedId = trimId(id);   	
    	long dbId= Long.parseLong(trimmedId);
    	OauthTokenEntity oauthToken = findOauthTokenByLeid(id);
    	if (oauthToken!=null){
    		deleteOauthToken(String.valueOf(oauthToken.getId()));
    	}
    	synchronized(leem) {
    		leem.getTransaction().begin();
	        System.out.println("Deleting Learning Environment from DB with id: [" + trimId(id) + "]");
	        Query q = leem.createNamedQuery("LearningEnvironmentEntity.deleteById");
	        q.setParameter("id", dbId);
	        int num = q.executeUpdate();
	        System.out.println(num + " deleted entries.");
	        leem.getTransaction().commit();
    	}
        
    }
    
    
  //------------------------------------------------- SECURITY TOKENS FOR LDSHAKE
    
    public SectokenEntity findSectokenById(String id){
    	synchronized(sectem){

        try {
            System.out.println("Retrieving security token from database: [" + id + "]");
            Query q = sectem.createNamedQuery("SectokenEntity.findById");
            q.setParameter("id",id);
            SectokenEntity u = (SectokenEntity) q.getSingleResult();

            return u;
        } catch (Exception e) {
            return null;
        }
    	
    	}
    }
    
    public SectokenEntity findSectokenByToken(String sectoken){
    	synchronized(sectem){

        try {
            System.out.println("Retrievinkg security token from database by token: [" + sectoken + "]");
            Query q = sectem.createNamedQuery("SectokenEntity.findBySectoken");
            q.setParameter("sectoken",sectoken);
            SectokenEntity u = (SectokenEntity) q.getSingleResult();

            return u;
        } catch (Exception e) {
            return null;
        }
    	
    	}
    }
    
    public List<SectokenEntity> listSectokens(){
    	
        try {
            System.out.println("Retrieving security token list from database");
            Query q = sectem.createNamedQuery("SectokenEntity.listAll");
            List<SectokenEntity> sectokens = q.getResultList();
            
            return sectokens;
        } catch (Exception e) {
            return null;
        }    	
    }
    
    /**
     * Insert a user sectoken in the DB... if the sectoken exists, the record is updated
     */
    public void insertSectoken(SectokenEntity u) {
    	synchronized(sectem) {
    		SectokenEntity existingSectoken = this.findSectokenById(u.getId());
	    	
	    	//We set the ID so that a new sectoken is NOT generated, rather update the existing one
	    	if(existingSectoken!=null){
	            System.out.println("sectoken "+u.getId()+" already exists in DB: ["+existingSectoken.toString()+"] -> ["+u.toString()+"]");
	    		u.setId(existingSectoken.getId());
	    	}
	    	
	        EntityTransaction tx = sectem.getTransaction();
	        tx.begin();
	        System.out.println("Inserting/updating sectoken in DB: ["+u.toString()+"]");
	
	        //sectem.persist(u);
	        sectem.merge(u);
	        sectem.flush();
	
	        System.out.println("Sectoken inserted/updated in DB");
	
	        tx.commit();
    	}
    }
    
    public void deleteSectoken(String id) {
    	synchronized(sectem) {
    		sectem.getTransaction().begin();
	        System.out.println("Deleting sectoken from DB with id: [" + trimId(id) + "]");
	        Query q = sectem.createNamedQuery("SectokenEntity.deleteById");
	        q.setParameter("id", trimId(id));
	        int num = q.executeUpdate();
	        System.out.println(num + " deleted entries.");
	        sectem.getTransaction().commit();
    	}
        
    }
    
    
  //------------------------------------------------- OAUTH TOKENS FOR LDSHAKE
    
    public OauthTokenEntity findOauthTokenById(String l){
    	
    	String trimmedId = trimId(l);    	
    	long dbId = Long.parseLong(trimmedId);

    	synchronized(oauthtem){

	        try {
	            System.out.println("Retrieving oauth token from database: [" + l + "]");
	            Query q = oauthtem.createNamedQuery("OauthTokenEntity.findById");
	            q.setParameter("id",dbId);
	            OauthTokenEntity u = (OauthTokenEntity) q.getSingleResult();
	
	            return u;
	        } catch (Exception e) {
	            return null;
	        }
    	
    	}
    }
    
    public OauthTokenEntity findOauthTokenByLeid(String leid){
    	String trimmedId = trimId(leid);    	
    	long dbId = Long.parseLong(trimmedId);
    	synchronized(oauthtem){

        try {
            System.out.println("Retrievinkg oauth token from database by leid: [" + leid + "]");
            Query q = oauthtem.createNamedQuery("OauthTokenEntity.findByLeid");
            q.setParameter("leid",dbId);
            OauthTokenEntity u = (OauthTokenEntity) q.getSingleResult();

            return u;
        } catch (Exception e) {
            return null;
        }
    	
    	}
    }
    
    public List<OauthTokenEntity> listOauthTokens(){
    	
        try {
            System.out.println("Retrieving oauth token list from database");
            Query q = oauthtem.createNamedQuery("OauthTokenEntity.listAll");
            List<OauthTokenEntity> oauthTokens = q.getResultList();
            
            return oauthTokens;
        } catch (Exception e) {
            return null;
        }    	
    }
    
    /**
     * Insert a oauth token in the DB... if the oauth token exists, the record is updated
     */
    public void insertOauthToken(OauthTokenEntity u) {
    	synchronized(oauthtem) {
    		OauthTokenEntity existingOauthToken = this.findOauthTokenByLeid(String.valueOf(u.getLeid()));
	    	
	    	//We set the ID so that a new oauth token is NOT generated, rather update the existing one
	    	if(existingOauthToken!=null){
	            System.out.println("oauth token "+u.getId()+" already exists in DB: ["+existingOauthToken.toString()+"] -> ["+u.toString()+"]");
	    		u.setId(existingOauthToken.getId());
	    	}
	    	
	        EntityTransaction tx = oauthtem.getTransaction();
	        tx.begin();
	        System.out.println("Inserting/updating oauth token in DB: ["+u.toString()+"]");
	
	        //sectem.persist(u);
	        oauthtem.merge(u);
	        oauthtem.flush();
	
	        System.out.println("Oauth token inserted/updated in DB");
	
	        tx.commit();
    	}
    }
    
    public void deleteOauthToken(String id) {
    	String trimmedId = trimId(id);    	
    	long dbId = Long.parseLong(trimmedId);
    	synchronized(oauthtem) {
    		oauthtem.getTransaction().begin();
	        System.out.println("Deleting oauth token from DB with id: [" + id + "]");
	        Query q = oauthtem.createNamedQuery("OauthTokenEntity.deleteById");
	        q.setParameter("id", dbId);
	        int num = q.executeUpdate();
	        System.out.println(num + " deleted entries.");
	        oauthtem.getTransaction().commit();
    	}
        
    }
    


    /**
     * Convenience method that gets a LEInst from DB by passing it the (possibly url) ID
     */
    public LearningEnvironmentInstallation findLEInstObjectById(String id){
    	
    	String trimmedId = trimId(id);
    	
    	long dbId=0;
    	try {
			dbId = Long.parseLong(trimmedId);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
    	
    	LearningEnvironmentInstallationEntity leInstEnt = findLEInstallationById(dbId);
    	if (leInstEnt == null)
    	{
    		return null;
    	}
    	
    	LearningEnvironmentInstallation leInst = null;
		try {
			leInst = new LearningEnvironmentInstallation(String.valueOf(leInstEnt.getId()), leInstEnt.getName(), leInstEnt.getType(), new URL(leInstEnt.getAccessLocation()), leInstEnt.getParameters(), leInstEnt.getSectype());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	leInst.setId(trimmedId);//we set the LE id to just the trimmed id (just in case the xml is erroneous)
    	
		System.out.println("Retrieved LE from database: "+leInst.toString());
		return leInst;
    }
    
    /**
     * Convenience method that gets a LEInst from DB by passing it the accessLocation
     */
    public LearningEnvironmentInstallation findLEInstObjectByAccessLocation(String accessLocation){
    	
    	LearningEnvironmentInstallationEntity leInstEnt = findLEInstallationByAccessLocation(accessLocation);
    	if (leInstEnt == null)
    	{
    		return null;
    	}
    	
    	LearningEnvironmentInstallation leInst = null;
		try {
			leInst = new LearningEnvironmentInstallation(String.valueOf(leInstEnt.getId()), leInstEnt.getName(), leInstEnt.getType(), new URL(leInstEnt.getAccessLocation()), leInstEnt.getParameters(), leInstEnt.getSectype());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		System.out.println("Retrieved LE from database: "+leInst.toString());
		return leInst;
    }
    
	/**
     * Convenience method that gets LEs from DB 
     * @throws UnsupportedEncodingException 
     */
    public List<LearningEnvironmentInstallation> listLEInstObjects() throws UnsupportedEncodingException{
    	
    	List<LearningEnvironmentInstallationEntity> leInsts = listLEInstallations();
    	
    	List<LearningEnvironmentInstallation> leInstObjects = null;
    	if(leInsts!=null){
    		leInstObjects = new ArrayList<LearningEnvironmentInstallation>();
    		
    		for(LearningEnvironmentInstallationEntity leInstEnt : leInsts){
    			
    			try {
    				LearningEnvironmentInstallation leInst = new LearningEnvironmentInstallation(String.valueOf(leInstEnt.getId()), leInstEnt.getName(), leInstEnt.getType(), new URL(leInstEnt.getAccessLocation()), leInstEnt.getParameters(), leInstEnt.getSectype());
    				leInstObjects.add(leInst);
    			} catch (MalformedURLException e) {
    				e.printStackTrace();
    			}
    			
    		}
    	}
    	return leInstObjects;
    }
    
  //------------------------------------------------- LEARNING ENVIRONMENTS INSTALLATIONS
    
    public LearningEnvironmentInstallationEntity findLEInstallationById(long leInstallation){
    	synchronized(leinstallationsem){

        try {
            System.out.println("Retrieving LE Installation from database: [" + leInstallation + "]");
            Query q = leinstallationsem.createNamedQuery("LearningEnvironmentInstallationEntity.findById");
            q.setParameter("id",leInstallation);
            LearningEnvironmentInstallationEntity u = (LearningEnvironmentInstallationEntity) q.getSingleResult();

            return u;
        } catch (Exception e) {
            return null;
        }
    	
    	}
    }
    
    public LearningEnvironmentInstallationEntity findLEInstallationByAccessLocation(String acccessLocation){
    	synchronized(leinstallationsem){

        try {
            System.out.println("Retrieving LE Installation from database: [" + acccessLocation + "]");
            Query q = leinstallationsem.createNamedQuery("LearningEnvironmentInstallationEntity.findByAccessLocation");
            q.setParameter("accessLocation",acccessLocation);
            LearningEnvironmentInstallationEntity u = (LearningEnvironmentInstallationEntity) q.getSingleResult();

            return u;
        } catch (Exception e) {
            return null;
        }
    	
    	}
    }
    
    public List<LearningEnvironmentInstallationEntity> listLEInstallations(){
    	
        try {
            System.out.println("Retrieving LE Installations from database");
            Query q = leinstallationsem.createNamedQuery("LearningEnvironmentInstallationEntity.listAll");
            List<LearningEnvironmentInstallationEntity> leInstallations = q.getResultList();
            
            return leInstallations;
        } catch (Exception e) {
            return null;
        }    	
    }
    
    
    //------------------------------------------------- DESIGNS
    
    
    public DesignEntity findDesignById(String id){
    	synchronized(designem){

        try {
            System.out.println("Retrieving Design from database: [" + trimId(id) + "]");
            Query q = designem.createNamedQuery("DesignEntity.findById");
            q.setParameter("id",trimId(id));
            DesignEntity u = (DesignEntity) q.getSingleResult();

            return u;
        } catch (Exception e) {
            return null;
        }
    	}
    	
    }
    
    public List<DesignEntity> listDesigns(){
    	synchronized(designem){

        try {
            System.out.println("Retrieving Designs from database");
            Query q = designem.createNamedQuery("DesignEntity.listAll");
            List<DesignEntity> les = q.getResultList();
            
            return les;
        } catch (Exception e) {
            return null;
        }
    	}
    }
    
    
    public List<DesignEntity> listDesignsByUser(long userid){
    	synchronized(designem){

        try {
            System.out.println("Retrieving Designs from database for user ["+userid+"]");
            Query q = designem.createNamedQuery("DesignEntity.listByUser");
            q.setParameter("userid",userid);
            List<DesignEntity> des = q.getResultList();
            
            return des;
        } catch (Exception e) {
            return null;
        }
    	}
    }
    
    /**
     * Convenience method that gets a Design from DB by passing it the (possibly url) ID
     */
    public Design findDesignObjectById(String id){
    	
    	String trimmedId = trimId(id);
    	
    	
    	DesignEntity desEnt = findDesignById(trimmedId);
    	
    	String xmlfile=null;
    	try {
			xmlfile = new String(desEnt.getXmlfile(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
    	
    	Design design=(Design) generateObject(xmlfile, glueps.core.model.Design.class);
    	design.setId(trimmedId);//we set the design id to the trimmed one, just in case the xml was stored with url ids
    	
    	//JUAN: comento la línea de abajo para no cargar los logs
	//	System.out.println("Retrieved Design from database: "+design.toString());
		return design;
        	
    	
    }
    
    
    /**
     * Convenience method that gets Designs from DB 
     * @throws UnsupportedEncodingException 
     */
    public List<Design> listDesignObjectsByUser(String author) throws UnsupportedEncodingException{
    	
    	UserEntity u = findUserByUsername(author);
    	if(u==null) return null;//If the user is not in the DB, we return no records
    	
    	List<DesignEntity> designs = listDesignsByUser(u.getId());
    	
    	List<Design> designObjects = null;
    	if(designs!=null){
    		designObjects = new ArrayList<Design>();
    		
    		for(DesignEntity de : designs){
    			Design design = (Design) generateObject(new String(de.getXmlfile(),"UTF-8"), Design.class);
    			design.setId(de.getId());//We set the design id to the trimmed version, just in case the xml contained the url one
    			designObjects.add(design);
    			
    		}
    	}
    	return designObjects;
    }
    
    
    
    /**
     * Convenience method that gets Designs from DB 
     * @throws UnsupportedEncodingException 
     */
    public List<Design> listDesignObjects() throws UnsupportedEncodingException{
    	
    	List<DesignEntity> designs = listDesigns();
    	
    	List<Design> designObjects = null;
    	if(designs!=null){
    		designObjects = new ArrayList<Design>();
    		
    		for(DesignEntity de : designs){
    			Design design = (Design) generateObject(new String(de.getXmlfile(),"UTF-8"), Design.class);
    			design.setId(de.getId());//We set the design id to the trimmed version, just in case the xml contained the url one
    			designObjects.add(design);
    			
    		}
    	}
    	return designObjects;
    }
    

	/**
     * Insert a design in the DB, and return the id... if the design already exists, it updates the record
     */
    public String insertDesign(DesignEntity u) {
    	synchronized(designem) {
	    	DesignEntity existingDesign = findDesignById(u.getId());
	    	
	    	//We set the ID so that a new design is NOT generated, rather update the existing one
	    	if(existingDesign!=null){
	            System.out.println("Design "+existingDesign.getId()+" already exists in DB: ["+existingDesign.toString()+"] -> ["+u.toString()+"]");
	    		u.setId(existingDesign.getId());
	    	}
	    	
	    	EntityTransaction tx = designem.getTransaction();
	        tx.begin();
	        
	        System.out.println("Inserting/updating Design in DB: ["+u.toString()+"]");
	
	        //em.persist(u);
	        designem.merge(u);
	
	        designem.flush();
	
	        System.out.println("Design inserted/updated in DB");
	
	        tx.commit();
	        return u.getId();
    	}
    }
    
    
    public String insertDesign(String id, long userid, String xmlfile, String name, String type) throws Exception{
    	
    	DesignEntity de = new DesignEntity();
    	
    	if(userid==0) throw new Exception("A valid design author must be specified!!");
    	else de.setUserid(userid);
    	
    	if(xmlfile==null) throw new Exception("A design xml file must be specified!!");
    	de.setXmlfile(xmlfile.getBytes("UTF-8"));
    	if(id!=null) de.setId(trimId(id));//preventive id trim
    	if(name==null || name.equals("")) throw new Exception("A name must be specified!!");
    	de.setName(name);
    	if(type==null || type.equals("")) throw new Exception("A type must be specified!!");
    	de.setType(type);

    	return insertDesign(de);
    	
    }
    
    
    public String insertDesign(Design d) throws Exception{
    	
    	long authorid=0;
    	if(d.getAuthor()==null) throw new Exception("A design author must be specified!!");
    	else{
    		authorid = findUserByUsername(d.getAuthor()).getId();
    		if(authorid==0) throw new Exception("A valid design author must be specified!!");
    	}
    	
    	if(d.getId()==null){//it is a new Design, we create the new record, then fill it with the right data
    		String id=null;
    		do{
    			id = "" + (new Random()).hashCode();
   			//we check that the random number does not already exist (unlikely!)
    		}while(findDesignById(id)!=null);
    		
    		d.setId(id);
    	}
    	
    	String xmlfile = generateXML(d, Design.class);
    	
    	if(xmlfile!=null) return insertDesign(trimId(d.getId()), authorid, xmlfile, d.getName(), d.getOriginalDesignType());
    	else throw new Exception("Error when converting the Design to XML format");
    	
    }
    

    public void deleteDesign(String id) {
    	synchronized(designem) {
	        designem.getTransaction().begin();
	        System.out.println("Deleting design from DB with id: [" + trimId(id) + "]");
	        Query q = designem.createNamedQuery("DesignEntity.deleteById");
	        q.setParameter("id", trimId(id));
	        int num = q.executeUpdate();
	        System.out.println(num + " deleted entries.");
	        designem.getTransaction().commit();
    	}
        
    }
    
    
    
    //------------------------------------------------- DEPLOYS

    
	public DeployEntity findDeployById(String id) {
		synchronized (deployem) {

			try {
				System.out.println("Retrieving Deploy from database: ["
						+ trimId(id) + "]");
				Query q = deployem.createNamedQuery("DeployEntity.findById");
				q.setParameter("id", trimId(id));
				DeployEntity u = (DeployEntity) q.getSingleResult();

				return u;
			} catch (Exception e) {
				return null;
			}
		}
	}
    
    public List<DeployEntity> listDeploys(){
    	synchronized(deployem){

        try {
            System.out.println("Retrieving Deploys from database");
            Query q = deployem.createNamedQuery("DeployEntity.listAll");
            List<DeployEntity> des = q.getResultList();
            
            return des;
        } catch (Exception e) {
            return null;
        }
    	}
    }
    
    
    public List<DeployEntity> listDeploysByUser(long userid){
    	synchronized(deployem){

        try {
            System.out.println("Retrieving Deploys from database for user ["+userid+"]");
            Query q = deployem.createNamedQuery("DeployEntity.listByUser");
            q.setParameter("userid",userid);
            List<DeployEntity> des = q.getResultList();
            
            return des;
        } catch (Exception e) {
            return null;
        }
    	}
    }
    
    public List<DeployEntity> listDeploysByDesignId(String designid){
    	synchronized(deployem){

        try {
            System.out.println("Retrieving Deploys from database for design ["+trimId(designid)+"]");
            Query q = deployem.createNamedQuery("DeployEntity.listByDesign");
            q.setParameter("design",trimId(designid));
            List<DeployEntity> des = q.getResultList();
            
            return des;
        } catch (Exception e) {
            return null;
        }
    	}
    }
    
    /**
     * Convenience method that gets a Deploy from DB by passing it the (possibly url) ID
     */
    public Deploy findDeployObjectById(String id){
    	
    	String trimmedId = trimId(id);   	
    	DeployVersionEntity depVerEnt = findLastValidDeployVersion(trimmedId);
    	String xmlfile;
    	try {
    		if (depVerEnt!=null){
    			xmlfile = new String(depVerEnt.getXmlfile(), "UTF-8");
    		}
    		else{    			
    	    	DeployEntity depEnt = findDeployById(trimmedId);
    			DeployVersionEntity dve = new DeployVersionEntity(trimmedId,1);
    			dve.setXmlfile(depEnt.getXmlfile());
    			insertDeployVersion(dve);
    			xmlfile = new String(depEnt.getXmlfile(), "UTF-8");
    		}
		//Juan: Changed UnsupportedEncodingException for Exception to avoid a null pointer exception in case deploy does not exist
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Deploy deploy = (Deploy) generateObject(xmlfile, glueps.core.model.Deploy.class);
				
    	deploy.setId(trimmedId);
    	//TODO we might want to retrieve also the other entities, if we want to be sure about the updatedness of included data
    	//For now we do not do it - we allow design variations for a specific deploy
    	//deploy.setAuthor(findUserById(depEnt.getUserid()).getLogin());
    	//deploy.setDesign(findDesignObjectById(depEnt.getDesignid()));
    	
    	//JUAN: comento la línea de abajo para no cargar los logs
    	//	System.out.println("Retrieved Deploy from database: "+deploy.toString());
		return deploy;
        	
    	
    }
    
    
    /**
     * Convenience method that gets Designs from DB 
     * @throws UnsupportedEncodingException 
     */
    public List<Deploy> listDeployObjectsByUser(String author) throws UnsupportedEncodingException{
    	
    	UserEntity user = findUserByUsername(author);
    	if(user==null) return null;//If the author is not in the database, we return no records
    	
    	long userid = user.getId();
    	
    	//We get the deploys from database
    	List<DeployEntity> deploys = listDeploysByUser(userid);
    	
    	//We convert them to model objects
    	List<Deploy> deployObjects = null;
    	if(deploys!=null){
    		deployObjects = new ArrayList<Deploy>();
    		
    		for (DeployEntity de : deploys){
        		Deploy deploy = findDeployObjectById(de.getId());
        		deployObjects.add(deploy);
    		}
    	}
    	return deployObjects;
    }
    
    /**
     * Convenience method that gets Designs from DB 
     * @throws UnsupportedEncodingException 
     */
    public List<Deploy> listDeployObjectsByDesignId(String designid) throws UnsupportedEncodingException{
    	
    	//We get the deploys from the database
    	List<DeployEntity> deploys = listDeploysByDesignId(designid);
    	
    	//We convert the entities to model objects
    	List<Deploy> deployObjects = null;
    	if(deploys!=null){
    		deployObjects = new ArrayList<Deploy>();
    		
    		for (DeployEntity de: deploys){
        		Deploy deploy = findDeployObjectById(de.getId());
        		deployObjects.add(deploy);
    		}

    	}
    	return deployObjects;
    }
    
    /**
     * Convenience method that gets Deploys from DB 
     * @throws UnsupportedEncodingException 
     */
    public List<Deploy> listDeployObjects() throws UnsupportedEncodingException{
    	
    	List<DeployEntity> deploys = listDeploys();
    	
    	List<Deploy> deployObjects = null;
    	if(deploys!=null){
    		deployObjects = new ArrayList<Deploy>();
    		
    		for (DeployEntity de: deploys){
        		Deploy deploy = findDeployObjectById(de.getId());
        		deployObjects.add(deploy);
    		}
    	}
    	return deployObjects;
    }
    
    
    public String insertDeploy(String id, long userid, String designid, String xmlfile, String name, boolean complete, String staticDeploy, String liveDeploy) throws Exception{
    	
    	DeployEntity de = new DeployEntity();
    	
    	if(userid==0) throw new Exception("A valid deploy author must be specified!!");
    	else de.setUserid(userid);
    	
    	if(designid==null) throw new Exception("A valid deploy design must be specified!!");
    	else de.setDesignid(trimId(designid));
    	
    	if(xmlfile==null) throw new Exception("A deploy xml file must be specified!!");
    	de.setXmlfile(xmlfile.getBytes("UTF-8"));
    	if(id!=null) de.setId(trimId(id));
    	if(name==null || name.equals("")) throw new Exception("A name xml file must be specified!!");
    	de.setName(name);
    	de.setComplete(complete);
    	de.setStaticDeploy(staticDeploy);
    	de.setLiveDeploy(liveDeploy);

    	//Insert the deploy version
    	String deployid = insertDeploy(de);
    	long nextVersion = getNextVersionDeploy(de.getId());
    	DeployVersionEntity dve = new DeployVersionEntity(de.getId(), nextVersion);
    	dve.setXmlfile(de.getXmlfile());
    	insertDeployVersion(dve);
    	return deployid;
    	
    	
    	
    	//Insert deploy version
    	
    }
    
    
    public String insertDeploy(Deploy d) throws Exception{
    	
    	long authorid=0;
    	if(d.getAuthor()==null) throw new Exception("A deploy author must be specified!!");
    	else{
    		authorid = findUserByUsername(d.getAuthor()).getId();
    		if(authorid==0) throw new Exception("A valid deploy author must be specified!!");
    	}
    	
    	DesignEntity design=null;
    	if(d.getDesign()==null || d.getDesign().getId()==null) throw new Exception("A deploy design must be specified!!");
    	else{
    		design = findDesignById(trimId(d.getDesign().getId()));
    		if(design==null) throw new Exception("A valid deploy design must be specified!!");
    	}
    	
    	
    	if(d.getId()==null){//it is a new Deploy, we create the new record, then fill it with the right data
    		String id=null;
    		do{
    			id = design.getId()+ "-" + (new Random()).hashCode();
   			//we check that the random number does not already exist (unlikely!)
    		}while(findDeployById(id)!=null);
    		
    		d.setId(id);
    	}
    	//set the timestamp value of the deploy for this version
    	d.setTimestamp(new Date());
    	
    	String xmlfile = generateXML(d, Deploy.class);
		String staticDeploy = null; 
    	if (d.getStaticDeployURL()!=null){
    		staticDeploy = d.getStaticDeployURL().toString();
    	}
		String liveDeploy = null; 
    	if (d.getLiveDeployURL()!=null){
    		liveDeploy = d.getLiveDeployURL().toString();
    	}
    	if(xmlfile!=null) return insertDeploy(trimId(d.getId()), authorid, design.getId(), xmlfile, d.getName(), d.isComplete(), staticDeploy, liveDeploy);
    	else throw new Exception("Error when converting the Deploy to XML format");
    	
    }
    

    public void deleteDeploy(String id) {
    	synchronized(deployem) {
	        deployem.getTransaction().begin();
	        System.out.println("Deleting deploy from DB with id: [" + trimId(id) + "]");
	        Query q = deployem.createNamedQuery("DeployEntity.deleteById");
	        q.setParameter("id", trimId(id));
	        int num = q.executeUpdate();
	        System.out.println(num + " deleted entries.");
	        deployem.getTransaction().commit();
    	}
        
    }
    
    
    
	/**
     * Insert a deploy in the DB, and return the id... if the deploy already exists, it updates the record
     */
    public String insertDeploy(DeployEntity u) {
    	synchronized(deployem) {
	    	DeployEntity existingDeploy = findDeployById(u.getId());
	    	
	    	//We set the ID so that a new design is NOT generated, rather update the existing one
	    	if(existingDeploy!=null){
	            System.out.println("Deploy "+existingDeploy.getId()+" already exists in DB: ["+existingDeploy.toString()+"] -> ["+u.toString()+"]");
	    		u.setId(existingDeploy.getId());
	    	}
	    	
	    	EntityTransaction tx = deployem.getTransaction();
	        tx.begin();
	
	        System.out.println("Inserting/updating Deploy in DB: ["+u.toString()+"]");
	
	        //em.persist(u);
	        deployem.merge(u);
	
	        deployem.flush();
	
	        System.out.println("Deploy inserted/updated in DB");
	
	        tx.commit();
	        return u.getId();
    	}
    }
    
    
    //------------------------------------------------- DEPLOY VERSIONS
    
    public long getLastValidVersionDeploy(String deployid){
    	synchronized(deployversionem){

	        try {
	            System.out.println("Getting the last valid version of the deploy querying the DB: [" + deployid + "]");
	            Query q = deployversionem.createNamedQuery("DeployVersionEntity.findLastValidVersionDeploy");
	          
	            q.setParameter("deployid",deployid);
	
	            long u = (Long)q.getSingleResult();
	            return u;
	        } catch (Exception e) {
	            return 0;
	        }
    	
    	}

    }
    
    public DeployVersionEntity findUndoVersionDeploy(String deployid){
    	synchronized(deployversionem){
    	
        try {
            System.out.println("Retrieving Undo version from database: [" + trimId(deployid) + "]");
            Query q = deployversionem.createNamedQuery("DeployVersionEntity.findUndoVersionDeploy");
          
            q.setParameter("deployid",trimId(deployid));

            DeployVersionEntity u = (DeployVersionEntity) q.getSingleResult();
            deployversionem.refresh(u);
            return u;
        } catch (Exception e) {
            return null;
        }
    	
    	}
    }
    
    public DeployVersionEntity findLastValidDeployVersion(String deployid){
    	synchronized(deployversionem){
    	
    	long lastValidVersion = getLastValidVersionDeploy(deployid);
        try {
            System.out.println("Retrieving Deploy version from database: [" + trimId(deployid) + ", " + lastValidVersion + "]");
            Query q = deployversionem.createNamedQuery("DeployVersionEntity.findById");
          
            q.setParameter("deployid",trimId(deployid));
            q.setParameter("version", lastValidVersion);

            DeployVersionEntity u = (DeployVersionEntity) q.getSingleResult();
            deployversionem.refresh(u);
            return u;
        } catch (Exception e) {
            return null;
        }
    	
    	}
    }
    
    public DeployVersionEntity findDeployVersionById(String deployid, long version){
    	synchronized(deployversionem){

        try {
            System.out.println("Retrieving Deploy version from database: [" + trimId(deployid) + ", " + version + "]");
            Query q = deployversionem.createNamedQuery("DeployVersionEntity.findById");
          
            q.setParameter("deployid",trimId(deployid));
            q.setParameter("version", version);

            DeployVersionEntity u = (DeployVersionEntity) q.getSingleResult();
            deployversionem.refresh(u);
            return u;
        } catch (Exception e) {
            return null;
        }
    	
    	}
    }
    
    public DeployVersionEntity findDeployVersionByIdNext(String deployid, long next){
    	synchronized(deployversionem){

        try {
            Query q = deployversionem.createNamedQuery("DeployVersionEntity.findByDeployidNext");
          
            q.setParameter("deployid",trimId(deployid));
            q.setParameter("next", next);

            DeployVersionEntity u = (DeployVersionEntity) q.getSingleResult();
            deployversionem.refresh(u);
            return u;
        } catch (Exception e) {
            return null;
        }
    	
    	}
    }
    
    public DeployVersionEntity undoDeployVersion(String deployid){
    	synchronized(deployversionem){
        long version = getLastValidVersionDeploy(deployid);
        try {
        	Query q;
        	int num;
	        deployversionem.getTransaction().begin();
	        q = deployversionem.createNamedQuery("DeployVersionEntity.updateValidDeploy");          
	        q.setParameter("deployid",trimId(deployid));
	        q.setParameter("version", version);
	        q.setParameter("valid", false);
	        num = q.executeUpdate();
	        deployversionem.getTransaction().commit();
            
            long newVersion = getLastValidVersionDeploy(deployid);
            deployversionem.getTransaction().begin();
            q = deployversionem.createNamedQuery("DeployVersionEntity.updateNextDeploy");
            q.setParameter("deployid",trimId(deployid));
            q.setParameter("version", newVersion);
            q.setParameter("next", version);
            num = q.executeUpdate();
            deployversionem.getTransaction().commit();
            DeployVersionEntity dve = findDeployVersionById(deployid, newVersion);
            
            return dve;
        } catch (Exception e) {
            return null;
        }
    	
    	}
    }
    
    public DeployVersionEntity redoDeployVersion(String deployid){
    	synchronized(deployversionem){
        long version = getLastValidVersionDeploy(deployid);
        try {
        	DeployVersionEntity dve = findDeployVersionById(deployid, version);
        	deployversionem.getTransaction().begin();
            Query q = deployversionem.createNamedQuery("DeployVersionEntity.updateValidDeploy");          
            q.setParameter("deployid",trimId(deployid));
            q.setParameter("version", dve.getNext());
            q.setParameter("valid", true);
            q.executeUpdate();
            deployversionem.getTransaction().commit();
            
            dve = findDeployVersionById(deployid, dve.getNext());
            
            return dve;
        } catch (Exception e) {
            return null;
        }
    	
    	}
    }
    
	/**
     * Insert a deploy in the DB, and return the id... if the deploy already exists, it updates the record
     */
    public long insertDeployVersion(DeployVersionEntity u) {
    	synchronized(deployversionem) {
	    	DeployVersionEntity existingDeployVersion = findDeployVersionById(u.getDeployid(), u.getVersion());
	    	
	    	//We set the ID so that a new design is NOT generated, rather update the existing one
	    	if(existingDeployVersion!=null){
	            System.out.println("Deploy "+existingDeployVersion.getDeployid()+ "version " + existingDeployVersion.getVersion() + " already exists in DB: ["+existingDeployVersion.toString()+"] -> ["+u.toString()+"]");
	    	}
	    	
	    	EntityTransaction tx = deployversionem.getTransaction();
	        tx.begin();
	
	        System.out.println("Inserting/updating Deploy version in DB: ["+u.toString()+"]");
	
	        //deployversionem.persist(u);
	        deployversionem.merge(u);
	
	        deployversionem.flush();
	
	        System.out.println("Deploy version inserted/updated in DB");
	
	        tx.commit();
	        return u.getVersion();
    	}
    }
    
    public long updateDeployVersion(DeployVersionEntity u) {
    	synchronized(deployversionem) {
	    	EntityTransaction tx = deployversionem.getTransaction();
	        tx.begin();
	
	        System.out.println("Updating Deploy version in DB: ["+u.toString()+"]");
	        deployversionem.persist(u);
	
	        System.out.println("Deploy version updated in DB");
	
	        tx.commit();
	        return u.getVersion();
    	}
    }
    
   
    public long getNextVersionDeploy(String deployid){
    	synchronized(deployversionem){

	        try {
	            System.out.println("Getting the next version of the deploy querying the DB: [" + deployid + "]");
	            Query q = deployversionem.createNamedQuery("DeployVersionEntity.findLastVersionDeploy");
	          
	            q.setParameter("deployid",deployid);
	
	            long u = (Long)q.getSingleResult();
	            return u+1;
	        } catch (Exception e) {
	            return 1;
	        }
    	
    	}

    }
    
    public void deleteDeployVersion(String deployid, long version) {
    	synchronized(deployversionem) {
    		deployversionem.getTransaction().begin();
	        System.out.println("Deleting deploy version from DB with id: : [" + trimId(deployid) + ", " + version + "]");
	        Query q = deployversionem.createNamedQuery("DeployVersionEntity.deleteById");
	        q.setParameter("deployid", trimId(deployid));
	        q.setParameter("version", version);
	        int num = q.executeUpdate();
	        System.out.println(num + " deleted entries.");
	        deployversionem.getTransaction().commit();
    	}        
    }
    
    public void deleteDeployVersions(String deployid) {
    	synchronized(deployversionem) {
    		deployversionem.getTransaction().begin();
	        System.out.println("Deleting deploy versions from DB with id: : [" + trimId(deployid) + "]");
	        Query q = deployversionem.createNamedQuery("DeployVersionEntity.deleteByDeployid");
	        q.setParameter("deployid", trimId(deployid));
	        int num = q.executeUpdate();
	        System.out.println(num + " deleted entries.");
	        deployversionem.getTransaction().commit();
    	}        
    }
    
    
    //--------------------- Utility methods
    
 
	private String generateXML(Object o, Class classname){
		
		//We create the LE XML
		String xml = null;
		JAXBContext context;
		try {
			StringWriter writer = new StringWriter();
			context = JAXBContext.newInstance(classname);
			  Marshaller m = context.createMarshaller();	
			  //Esta Propiedad formatea el c�digo del XML
	          m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
	          m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	          m.marshal(o, writer);
		      xml = writer.toString();
	          return xml;
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
    
	private Object generateObject(String xmlfile,Class classname) {
        
	      Object o = null;
	      JAXBContext jc = null;
	            //We create the learning environment with the provided XML
	            try {
	                  if (classname.equals(Deploy.class)){
	                        if (jcDeploy==null){
	                             jcDeploy = JAXBContext.newInstance( classname );
	 
	                        }
	                        jc = jcDeploy;
	                       
	                  }else{
	                        jc = JAXBContext.newInstance( classname );
	                     
	                  }
	                  Unmarshaller u = jc.createUnmarshaller();
	              o = u.unmarshal(new StringReader(xmlfile));
	            //JUAN: comento la línea de abajo para no cargar los logs
	          //    System.out.println( "\nThe unmarshalled objects are:\n" + o.toString());
	                  return o;
	            } catch (JAXBException e) {
	                  e.printStackTrace();
	                  return null;
	            }
	     
	     
	     
	      }

    
    
    private String trimId(String id) {
    	
    	if(id==null) return null;
    	String trimmedId=null;
    	if(id.indexOf("/")!=-1){
        	trimmedId = id.substring(id.lastIndexOf("/")+1);
        	//if it has a extension, we remove it
        	if(trimmedId.lastIndexOf(".")!=-1) trimmedId = trimmedId.substring(0, trimmedId.lastIndexOf("."));
    	}else trimmedId = id;//if it is not url-like, lets hope it is directly the (numeric) id
    	return trimmedId;
	}
    
    

   	protected Deploy deURLifyDeploy(Deploy d) throws Exception {
    	Deploy updatedDeploy = null;
    	if(d==null) return null;

    	//We start with the urlified deploy
    	updatedDeploy = d;
    	//deurlify deploy id
    	updatedDeploy.setId(trimId(d.getId()));
    	
    	//deurlify design
    	updatedDeploy.setDesign(deURLifyDesign(d.getDesign()));
    	
    	//deurlify learningEnvironment
    	updatedDeploy.setLearningEnvironment(deURLifyLearningEnvironment(d.getLearningEnvironment()));
    	
    	//deurlify toolinstances
    	if(updatedDeploy.getToolInstances()!=null){
    		for(ToolInstance inst : updatedDeploy.getToolInstances()){
    			inst.setId(trimId(inst.getId()));

    			if(inst.getLocation()!=null){
	    			//deurlify internal toolinstance references in toolinstance locations
	    	    	if(inst.getLocation().toString().contains("/GLUEPSManager/") && inst.getLocation().toString().contains("/toolInstances/")){
	    	    		inst.setInternalReference(trimId(inst.getLocation().toString()));
	    	    		inst.setLocation(null);
	    	    	}
    			}    	    	
    	    	inst.setDeployId(trimId(inst.getDeployId()));
    		}	
    	}
    	
    	//deurlify toolinstance references in instancedactivities
    	if(updatedDeploy.getInstancedActivities()!=null){
    		for(InstancedActivity instAct : updatedDeploy.getInstancedActivities()){
    			if(instAct.getInstancedToolIds()!=null){
    				ArrayList<String> newInsts = new ArrayList<String>();
    				for(String ref : instAct.getInstancedToolIds()) newInsts.add(trimId(ref));
    				instAct.setInstancedToolIds(newInsts);
    			}
    			
    			instAct.setDeployId(trimId(instAct.getDeployId()));
    		}
    	}
    	
    	//deurlify participant deployIds
    	if(updatedDeploy.getParticipants()!=null){
    		for(Participant part : updatedDeploy.getParticipants()){
    			part.setDeployId(trimId(part.getDeployId()));
    		}
    	}
    	
    	//we check that the deploy is still valid, just in case
    	if(!updatedDeploy.isValid()) throw new Exception("The deploy is not valid anymore!");
    	
    	return updatedDeploy;
   	}

    

    protected LearningEnvironment deURLifyLearningEnvironment(
			LearningEnvironment le) {
		LearningEnvironment updatedLE = null;
		if(le==null) return null;
		
		updatedLE = le;
		
    	//deurlify LE id
    	updatedLE.setId(trimId(le.getId()));
    	//deurlify course ids
    	if(le.getCourses()!=null){
    		HashMap<String, String> newCourses = new HashMap<String, String>();
    		for(Entry<String,String> courseentry : le.getCourses().entrySet()){
    			newCourses.put(trimId(courseentry.getKey()), courseentry.getValue());
    		}
    		updatedLE.setCourses(newCourses);
    	}
    	
    	//deurlify internal tool ids
    	if(le.getInternalTools()!=null){
    		HashMap<String, String> newIntTools = new HashMap<String, String>();
    		for(Entry<String,String> toolentry : le.getInternalTools().entrySet()){
    			newIntTools.put(trimId(toolentry.getKey()), toolentry.getValue());
    		}
    		updatedLE.setInternalTools(newIntTools);
    	}
    	
    	//urlify external tool ids
    	if(le.getExternalTools()!=null){
    		HashMap<String, String> newExtTools = new HashMap<String, String>();
    		for(Entry<String,String> toolentry : le.getExternalTools().entrySet()){
    			newExtTools.put(trimId(toolentry.getKey()), toolentry.getValue());
    		}
    		updatedLE.setExternalTools(newExtTools);
    	}
    	return updatedLE;
	}

	protected Design deURLifyDesign(Design d) {
    	Design updatedDesign = null;
    	if(d==null) return null;

    	//We start with the urlified design
    	updatedDesign = d;
    	//deurlify deploy id
    	updatedDesign.setId(trimId(d.getId()));
    	
    	//deurlify tooltypes in resources
    	if(updatedDesign.getResources()!=null){
    		for(Resource res : updatedDesign.getResources()){
    			res.setToolType(trimId(res.getToolType()));
    		}
    	}
    	
    	return updatedDesign;		
	}

//	//------------------------ Test methods
//    
//    
//    public void populateInitialData() throws Exception{
//    	
//    	UserEntity u1 = null;
//    	if(findUserByUsername("lprisan")==null){
//    	
//	    	u1 = new UserEntity();
//	    	u1.setLogin("lprisan");
//	    	u1.setPassword("lprisan");
//	    	
//	    	insertUser(u1);
//    	}
//    	
//    	String xmlfile=null;
//    	try {
//    		for(int i=1;i<10;i++){
//				xmlfile=FileUtils.readFileToString(new File("le/" + i+".xml"), "UTF-8");
//		    	if(xmlfile==null) throw new Exception("A le xml file must be specified!!");
//		    	LearningEnvironment le = (LearningEnvironment) generateObject(xmlfile, glueps.core.model.LearningEnvironment.class);
//				insertLearningEnvironment(deURLifyLearningEnvironment(le));
//    		}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
////    	String designId = null;
////    	Design des = null;
////    	try {
////			xmlfile=FileUtils.readFileToString(new File("design.xml"), "UTF-8");
////	    	if(xmlfile==null) throw new Exception("A design xml file must be specified!!");
////			des = (Design) generateObject(xmlfile, Design.class);
////			des.setAuthor(u1.getLogin());
////			designId = insertDesign(deURLifyDesign(des));
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////    	
////    	try {
////			xmlfile=FileUtils.readFileToString(new File("deploy.xml"), "UTF-8");
////	    	if(xmlfile==null) throw new Exception("A deploy xml file must be specified!!");
////	    	Deploy dep = (Deploy) generateObject(xmlfile, Deploy.class);
////	    	dep.setDesign(deURLifyDesign(des));
////	    	dep.setAuthor(u1.getLogin());
////			insertDeploy(deURLifyDeploy(dep));
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//    }
    
    
	

}
