package glueps.adaptors.vle.moodle;

import glueps.adaptors.vle.IStaticVLEDeployer;
import glueps.adaptors.vle.IVLEAdaptor;
import glueps.adaptors.vle.moodle.model.ASSIGNMENT;
import glueps.adaptors.vle.moodle.model.ASSIGNMENTS;
import glueps.adaptors.vle.moodle.model.COURSE;
import glueps.adaptors.vle.moodle.model.FEEDBACK;
import glueps.adaptors.vle.moodle.model.FEEDBACKS;
import glueps.adaptors.vle.moodle.model.GROUP;
import glueps.adaptors.vle.moodle.model.GROUPING;
import glueps.adaptors.vle.moodle.model.GROUPINGGROUP;
import glueps.adaptors.vle.moodle.model.GROUPINGS;
import glueps.adaptors.vle.moodle.model.GROUPINGSGROUPS;
import glueps.adaptors.vle.moodle.model.HEADER;
import glueps.adaptors.vle.moodle.model.INSTANCE;
import glueps.adaptors.vle.moodle.model.INSTANCES;
import glueps.adaptors.vle.moodle.model.MEMBER;
import glueps.adaptors.vle.moodle.model.MEMBERS;
import glueps.adaptors.vle.moodle.model.MOD;
import glueps.adaptors.vle.moodle.model.MODS;
import glueps.adaptors.vle.moodle.model.MOODLEBACKUP;
import glueps.adaptors.vle.moodle.model.OPTION;
import glueps.adaptors.vle.moodle.model.OPTIONS;
import glueps.adaptors.vle.moodle.model.ROLE;
import glueps.adaptors.vle.moodle.model.SECTION;
import glueps.adaptors.vle.moodle.model.TIMEMODIFIED;
import glueps.adaptors.vle.moodle.model.USER;
import glueps.adaptors.vle.moodle.utils.Contenedor;
import glueps.adaptors.vle.moodle.utils.NZipCompresser;
import glueps.adaptors.vle.moodle.utils.Utilidad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Uniform;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Preference;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.Client;


import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Activity;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.model.Group;
import glueps.core.model.InstancedActivity;
import glueps.core.model.Participant;
import glueps.core.model.Resource;
import glueps.core.model.ToolInstance;





public class MoodleAdaptorSelectiveOneTopic implements IStaticVLEDeployer{
	
	private String CLASSES = "glueps.adaptors.vle.moodle.model";
	private String BACKUPFILENAME = "moodle.xml";
	private String TMP_DIR = null;
	
	
	 private static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"; 
	 private String ZIPNAME="moodle.zip";
	 private String BASE="c:/uploaded/";
	 //This is the template XML backup file that we use as a base
	 //private String TEMPLATE="C:\\Proyectos\\ProtoGLUEPS_GUI\\war\\WEB-INF\\classes\\20110519_moodlebackup-curso_todos_modulos_v6.xml";
	 //private String TEMPLATE="D:\\workspace\\ProtoGLUEPS\\war\\WEB-INF\\classes\\20110519_moodlebackup-curso_todos_modulos_v6.xml";
	 private String TEMPLATE="C:\\PruebaJaxb\\FicherosXML\\moodleJuan\\xmlLeer\\20110519_moodlebackup-curso_todos_modulos_v6.xml";
	 private File moodleZip;
	 
	 
	 private static String DEFAULT_CONFIG_FILENAME="defaultMoodleConfiguration.xforms";
	 private String templateDir="d:\\workspace\\GLUEPSManager\\templates";
	 
	 protected String moodleUrl;
	 protected String moodleUser;
	 protected String moodlePassword;
	 protected Map<String, String> parameters;
	 
	 
public MoodleAdaptorSelectiveOneTopic() {
		super();
	}


public MoodleAdaptorSelectiveOneTopic(String base, String template,String moodleUrl, String moodleUser, String moodlePassword, Map<String, String> parameters) {
	super();
	//This is the pathname to the final zip file. This is no longer set here, since it needs the deployId to construct the path to the file
	//ZIPNAME = zipname;
	//This is the base directory where all uploaded files are
	BASE = base;
	//This the pathname to the moodle xml template
	TEMPLATE = template;
	
	this.moodleUrl = moodleUrl;
	this.moodleUser = moodleUser;
	this.moodlePassword = moodlePassword;
	this.parameters = parameters;
}


public MoodleAdaptorSelectiveOneTopic(String base, String template,String modelPackage, String backupXmlFilename,
		String tmpDir, String moodleUrl, String moodleUser, String moodlePassword, Map<String, String> parameters) {
	
	this(base, template, moodleUrl, moodleUser, moodlePassword, parameters);

	//This is the package that contains the Moodle XML model classes
	CLASSES = modelPackage;
	
	//This is the name of the Moodle backup main xml file
	BACKUPFILENAME = backupXmlFilename; 
	
	//This is a temporary dir for storing files while we make the zipfile
	TMP_DIR = tmpDir; 
	
}


public File getStaticDeploy() {
	return moodleZip;
}


public HashMap<String,Participant> getUsers(String moodleBaseUri){
	
	String qapiUrl = moodleBaseUri+"qapi/rest.php/userlist";

	HashMap<String,Participant> vleUsers = null;
	
	try {
		String response = doGetFromURL(qapiUrl);
		//System.out.println("VLE returned: "+jsonString);
		JSONObject object = new JSONObject(response);

		//JSONObject object = (new JsonRepresentation(rep)).getJsonObject();
		if (object != null) {
			String[] keys = JSONObject.getNames(object);
			for (String key : keys) {
				JSONArray fields = object.getJSONArray(key);
				if (fields != null && fields.length() == 5) {
					if (vleUsers == null)
						vleUsers = new HashMap<String, Participant>();
					Participant part;
					part = new Participant(
							key,
							fields.getString(0),
							null,
							key
									+ Participant.USER_PARAMETER_SEPARATOR
									+ fields.getString(0)
									+ Participant.USER_PARAMETER_SEPARATOR
									+ fields.getString(2)
									+ Participant.USER_PARAMETER_SEPARATOR
									+ fields.getString(3)
									+ Participant.USER_PARAMETER_SEPARATOR,
							false);
					//The iCollage adaptor references users by the username, not the DB id
					vleUsers.put(part.getName(), part);
				}
			}

		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	
	
//// Method 1
//	ClientResource usersResource = new ClientResource(qapiUrl);
//	
//	try{
//		System.out.println("Trying URL: "+qapiUrl);
//		Representation rep = (Representation) usersResource.get();
//		if (usersResource.getStatus().isSuccess()) {
//			
//			
//			
//			try {
//				String jsonString = rep.getText();
//				System.out.println("VLE returned: "+jsonString);
//				JSONObject object = new JSONObject(jsonString);
//
//				//JSONObject object = (new JsonRepresentation(rep)).getJsonObject();
//				if (object != null) {
//					String[] keys = JSONObject.getNames(object);
//					for (String key : keys) {
//						JSONArray fields = object.getJSONArray(key);
//						if (fields != null && fields.length() == 5) {
//							if (vleUsers == null)
//								vleUsers = new HashMap<String, Participant>();
//							Participant part;
//							part = new Participant(
//									key,
//									fields.getString(0),
//									null,
//									key
//											+ Participant.USER_PARAMETER_SEPARATOR
//											+ fields.getString(0)
//											+ Participant.USER_PARAMETER_SEPARATOR
//											+ fields.getString(2)
//											+ Participant.USER_PARAMETER_SEPARATOR
//											+ fields.getString(3)
//											+ Participant.USER_PARAMETER_SEPARATOR,
//									false);
//							//The iCollage adaptor references users by the username, not the DB id
//							vleUsers.put(part.getName(), part);
//						}
//					}
//
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				throw new ResourceException(e);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				throw new ResourceException(e);
//			}
//
//		}
//	
//	} catch (ResourceException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		return null;
//	} finally {
////		Representation entity = usersResource.getResponseEntity(); 
////		  if (entity != null) entity.release(); 
////		usersResource.release();
//		
//		//New code equivalent mimicking GLUEletManager changes for connection stability
//		discardRepresentation(usersResource.getResponseEntity());    /// **** LA CLAVE: "VOLVER A" EXTRAER LA ENTIDAD RESPUESTA DEL 
//																	///CLIENTRESOURCE; si se llega aquí es desde la ejecución de .get(), 
//																	///por lo que la variable 'response' NO SE HA ASIGNADO
//
//	}
		
		

	return vleUsers;
}


/**
 * <p>Creación del hashmap con los usuarios</p> 
 *  
 */
@Override
public HashMap<String, String> getCourses(String moodleBaseUri){
	
	String qapiUrl = moodleBaseUri+"qapi/rest.php/course/all";

	HashMap<String,String> vleCourses = null;
	
	try {
		String response = doGetFromURL(qapiUrl);
		//System.out.println("VLE returned: "+jsonString);
		JSONObject object = new JSONObject(response);

		//JSONObject object = (new JsonRepresentation(rep)).getJsonObject();
		if (object != null) {
			String[] keys = JSONObject.getNames(object);
			for (String key : keys) {
				JSONArray fields = object.getJSONArray(key);
				if (fields != null && fields.length() == 6) {
					if (vleCourses == null)
						vleCourses = new HashMap<String, String>();

					//The iCollage adaptor references users by the course id
					vleCourses.put(key, fields.getString(0));
				}
			}

		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	
	
//	// Method 1: Simple parsing/object
//	ClientResource coursesResource = new ClientResource(qapiUrl);
//	
//	Representation rep = null;
//	try{
//		System.out.println("Trying URL: "+qapiUrl);
//		rep = (Representation) coursesResource.get();
//		if (coursesResource.getStatus().isSuccess()) {
//			
//			try {
//				String jsonString = rep.getText();
//				System.out.println("VLE returned: "+jsonString);
//				JSONObject object = new JSONObject(jsonString);
//
//				//JSONObject object = (new JsonRepresentation(rep)).getJsonObject();
//				if (object != null) {
//					String[] keys = JSONObject.getNames(object);
//					for (String key : keys) {
//						JSONArray fields = object.getJSONArray(key);
//						if (fields != null && fields.length() == 6) {
//							if (vleCourses == null)
//								vleCourses = new HashMap<String, String>();
//
//							//The iCollage adaptor references users by the course id
//							vleCourses.put(key, fields.getString(0));
//						}
//					}
//
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				throw new ResourceException(e);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				throw new ResourceException(e);
//			}
//
//		}
//	
//	} catch (ResourceException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		return null;
//	} finally {
////		Representation entity = coursesResource.getResponseEntity(); 
////		  if (entity != null) entity.release(); 
////		coursesResource.release();
//		
//		//New code equivalent mimicking GLUEletManager changes for connection stability
//		discardRepresentation(coursesResource.getResponseEntity());    /// **** LA CLAVE: "VOLVER A" EXTRAER LA ENTIDAD RESPUESTA DEL 
//																	///CLIENTRESOURCE; si se llega aquí es desde la ejecución de .get(), 
//																	///por lo que la variable 'response' NO SE HA ASIGNADO
//
//	}	
	
	System.out.println("The courses are: "+vleCourses.toString());

	return vleCourses;
}

/**
 * Gets from a Moodle installation the courses in which the user is an administrator user
 * @param moodleBaseUri The Base URI of the Moodle installation
 * @param username The name of the administrator user
 * @return The courses in which the user is the administrator
 *  
 */
@Override
public HashMap<String, String> getCourses(String moodleBaseUri, String username){
	Boolean auth = moodleAuth(moodleBaseUri, moodleUser, moodlePassword);
	if (auth==false){
		throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The user is not allowed to get the courses from the VLE. Please, check your credentials for the VLE");
	}
	String qapiUrl = moodleBaseUri + "qapi/rest.php/course/useradmin/" + username;

	HashMap<String,String> vleCourses = null;
	
	try {
		String response = doGetFromURL(qapiUrl);
		//System.out.println("VLE returned: "+jsonString);
		JSONObject object = new JSONObject(response);

		//JSONObject object = (new JsonRepresentation(rep)).getJsonObject();
		if (object != null) {
			String[] keys = JSONObject.getNames(object);
			for (String key : keys) {
				JSONArray fields = object.getJSONArray(key);
				if (fields != null && fields.length() == 6) {
					if (vleCourses == null)
						vleCourses = new HashMap<String, String>();

					//The iCollage adaptor references users by the course id
					vleCourses.put(key, fields.getString(0));
				}
			}

		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	
	
//	// Method 1: Simple parsing/object
//	ClientResource coursesResource = new ClientResource(qapiUrl);
//	
//	Representation rep = null;
//	try{
//		System.out.println("Trying URL: "+qapiUrl);
//		rep = (Representation) coursesResource.get();
//		if (coursesResource.getStatus().isSuccess()) {
//			
//			try {
//				String jsonString = rep.getText();
//				System.out.println("VLE returned: "+jsonString);
//				JSONObject object = new JSONObject(jsonString);
//
//				//JSONObject object = (new JsonRepresentation(rep)).getJsonObject();
//				if (object != null) {
//					String[] keys = JSONObject.getNames(object);
//					for (String key : keys) {
//						JSONArray fields = object.getJSONArray(key);
//						if (fields != null && fields.length() == 6) {
//							if (vleCourses == null)
//								vleCourses = new HashMap<String, String>();
//
//							//The iCollage adaptor references users by the course id
//							vleCourses.put(key, fields.getString(0));
//						}
//					}
//
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				throw new ResourceException(e);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				throw new ResourceException(e);
//			}
//
//		}
//	
//	} catch (ResourceException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		return null;
//	} finally {
////		Representation entity = coursesResource.getResponseEntity(); 
////		  if (entity != null) entity.release(); 
////		coursesResource.release();
//		
//		//New code equivalent mimicking GLUEletManager changes for connection stability
//		discardRepresentation(coursesResource.getResponseEntity());    /// **** LA CLAVE: "VOLVER A" EXTRAER LA ENTIDAD RESPUESTA DEL 
//																	///CLIENTRESOURCE; si se llega aquí es desde la ejecución de .get(), 
//																	///por lo que la variable 'response' NO SE HA ASIGNADO
//
//	}	
	
	System.out.println("The courses are: "+vleCourses.toString());

	return vleCourses;
}



/****
 * <p>Función que genera el xml de moodle y lo empaqueta en un zip con los archivos de sus recursos</p>
 * @param classes Clases necesarias para poder hacer el paso de xml a objeto y al reves.
 * @param destinationXml : Nombre del fichero XML destino "mooodle.xml"
 * @param dirZip: directorio base donde queremos que se genere el zip. ejemplo: "//zip"
 * @param lfdeploy Deploy: Objeto que contiene la información necesaria para poder generar apartir de ella el xml de moodle
 * @throws JAXBException
 * @throws IOException 
 */
	public File generateStaticDeploy(Deploy lfdeploy) throws JAXBException, IOException{

		//LP: Before generating the Moodle backup, we should check the vleUsers for consistency (or else, the restore will fail)
		String courseId = lfdeploy.getCourse().getId().substring(lfdeploy.getCourse().getId().lastIndexOf("/")+1); //we get the course ID, trimmed from the rest of the URL
		HashMap<String,Participant> vleUsers = this.getCourseUsers(lfdeploy.getLearningEnvironment().getAccessLocation().toString(), courseId);
		
		ArrayList<Participant> checkedPart = new ArrayList<Participant>();
		for(Iterator<Participant> itera = lfdeploy.getParticipants().iterator(); itera.hasNext();){
			Participant p = itera.next();
			Participant currentPart = null;
			if((currentPart = vleUsers.get(p.getName()))!=null){//if this user is already in the vle, we get the leData currently in the vle
				p.setLearningEnvironmentData(currentPart.getLearningEnvironmentData());
			}
			checkedPart.add(p);
		}
		lfdeploy.setParticipants(checkedPart);
		
	
		ZIPNAME = this.BASE+lfdeploy.getShortId()+File.separator+lfdeploy.getShortId()+".zip";
	
		Unmarshaller unmarshaller;
		//Guardamos la información del moodle que hemos tomado como base

		MOODLEBACKUP moodle;
		//Generamos HashMap con la información necesaria para crear los modulos en el xml de mooodle apartir de la lingua Franca
		HashMap prueba=this.generarHashmap(lfdeploy);
		//Obtendo del hashmap el número de secciones
		int numSecciones= (Integer) prueba.get("numSection");
		prueba.remove("numSection");
		//Eliminaria del hashmap la entrad secciones
		//prueba.remove("secciones");
		//Eliminados, auxiliar 
		prueba.remove("auxiliar");
        //Rellenamos las clases java con la información procedente del xml.
        unmarshaller = JAXBContext.newInstance(CLASSES).createUnmarshaller();
        moodle =  (MOODLEBACKUP)unmarshaller.unmarshal( new FileInputStream(TEMPLATE));
        
        /****COMENZAMOS MODIFICACI�N DE LAS CLASES PARA GENERAR XML ****/
        /*--INFO--*/
        	/*--INFO-->Atributo NAME*/
                moodle.getINFO().setNAME(this.getZipName());
            
            /*--INFO.DETAILS-->Atributo MOD*/  
                /*Vamos a seguir el siguiente proceso:
                 * 1)Nos quedamos con el modulo de la posicion 0 de la lista de modulos que hay en MOD
                 * por que como todos tendran los mismos atributos, simplemente lo clonaremos y lo iremos adaptando
                 * 2)En función de la lógica,creamos un mod o no. Debe haber uno por cada tipo
                 * de modulo
                 * 3)Tendremos que crear una instancia por cada modulo de un tipo que haya
                 * 4) A la hora de crear las instancias seguiremos el mismo razonamiento que para lo modulos arriba
                 * mencionados
                 * 
                 * */
                
                //1)Nos quedamos con el modulo de la posicion 0 de la lista de modulos que hay en MOD
                // por que como todos tendran los mismos atributos, simplemente lo clonaremos y lo iremos adaptando
	        	  	//Guardamos modulo de posici�n 0, para tomarlo como base
	        	  	MOD modForoBase=moodle.getINFO().getDETAILS().getMOD().get(0);
	        	  	MOD modForo=null;
	        	  	//Elimino la lista de modulos actuales,para preparala para a�adir los mios
	        	    moodle.getINFO().getDETAILS().setMODList(null);
	        	//2)En funci�n de la l�gica(pendiente de a�adir),creamos un mod o no. Debe haber uno por cada tipo
	            //de modulo   
	        	  
	        	Iterator it = prueba.keySet().iterator(); 
	        	ArrayList <Contenedor> listConten;
	        	int indWhile=0;
	        	
	       		while (it.hasNext()) {
	       	            // Get Clave
	       	            String tipoModulo = (String) it.next();
	       	            listConten = (ArrayList <Contenedor>) ((ArrayList)prueba.get(tipoModulo)).get(1);
	        		//CREAMOS EL MODULO 
	        		//Creamos modulos,para ello hacemos un clone del objeto que hemos tomado como base 
	        		//y modificamos los parametros
	        		 modForo=(MOD) modForoBase.clone();
	        		  Class declaredTypeF=null;
			          QName nameF=null;
			          Class scopeF=null; 
			          ArrayList listF=new ArrayList();
			          //Pongo a null DESCRIPTIONOrFEEDBACKOrFORMAT
			          modForo.setDESCRIPTIONOrFEEDBACKOrFORMAT(null);
	        	  	 for(int g=0; g<modForoBase.getDESCRIPTIONOrFEEDBACKOrFORMAT().size();g++){
		 	                declaredTypeF=  ((JAXBElement)modForoBase.getDESCRIPTIONOrFEEDBACKOrFORMAT().get(g)).getDeclaredType();
		 		        	nameF=((JAXBElement)modForoBase.getDESCRIPTIONOrFEEDBACKOrFORMAT().get(g)).getName();
		 		        	scopeF =((JAXBElement)modForoBase.getDESCRIPTIONOrFEEDBACKOrFORMAT().get(g)).getScope();		
		 		        	listF.add(new JAXBElement(nameF,declaredTypeF,scopeF,((JAXBElement)modForoBase.getDESCRIPTIONOrFEEDBACKOrFORMAT().get(g)).getValue()));
		 	            }
	        		 modForo.setDESCRIPTIONOrFEEDBACKOrFORMAT(listF);
	        		 /*--INFO.DETAILS-->Atributo MOD*/  
	         	  	//Modificamos parametros con las clases de la LinguaFranca.
	         	  	//Para cambiar el nombre del modulo. Se cambia el parametro name de MOD
	         	  	((JAXBElement)modForo.getDESCRIPTIONOrFEEDBACKOrFORMAT().get(0)).setValue(tipoModulo);
	        		
	         	  	//A�ADIMOS LAS INSTANCIAS
	        		 //TODO FALTA A�ADIR LOGICA DE LUIS PABLO
	         	  	 //Me quedo con una instancia como base
	         	  	List<Serializable> listInsForoBase = modForoBase.getINSTANCES().getINSTANCE().get(0).getContent();
	         	  	 ArrayList listInsForo =null;
	         	  	 INSTANCE instanceModForoBase=(INSTANCE)modForoBase.getINSTANCES().getINSTANCE().get(0);
	         	  	 INSTANCE instanceModForo;
	         	  	 //borro el resto de instancias
	         	  	 INSTANCES instances = new INSTANCES();
	         	  	 modForo.setINSTANCES(instances);
	         	  	 //modForo.getINSTANCES().setINSTANCE(null);
	         	  	 
	         	  	 Class declaredTypeID=null;
	         	  	 QName nameID=null;
	         	  	 Class scopeID=null;
	        		 for(int j=0; j<listConten.size();j++){
	        			 //for para crear los elementos de la instacia que son una lista de jaxelement
	        			 instanceModForo=new INSTANCE();
	        			 for(int l=0; l<listInsForoBase.size()-1;l++){
	        				 //Para evitar si vienen algun objeto de tipo String
	        				 if (String.class !=  listInsForoBase.get(l).getClass()){
	        					 //Para evitar que entren objeto que no se han JAXBElement
	        					 if (JAXBElement.class ==  listInsForoBase.get(l).getClass()){
			        				declaredTypeID= ((JAXBElement)listInsForoBase.get(l)).getDeclaredType();
			        		        nameID=((JAXBElement)listInsForoBase.get(l)).getName();
			        		        scopeID=((JAXBElement)listInsForoBase.get(l)).getScope();
	        					 }
		        		         //instanceModForo.getContent().add(new JAXBElement(nameID,declaredTypeID,scopeID,((JAXBElement)listInsForoBase.get(l)).getValue()));
		        		         if (nameID.toString().equals("ID"))
		        		        	 instanceModForo.getContent().add(new JAXBElement(nameID,declaredTypeID,scopeID,new BigInteger(String.valueOf(listConten.get(j).getModId()))));
		        		         else {
		        		        	 if (nameID.toString().equals("NAME")){
		        		        		 instanceModForo.getContent().add(new JAXBElement(nameID,declaredTypeID,scopeID,listConten.get(j).getModName()));
		        		        	 }else{
		        		        		 instanceModForo.getContent().add(new JAXBElement(nameID,declaredTypeID,scopeID,((JAXBElement)listInsForoBase.get(l)).getValue())); 
		        		        	 }        		        	 
		        		         }
	        				 }//Fin del if para evitar,elementos que se han string
	        			 }//fin del for para elementos jaxblement de la instancia	
	        			 //a�ado las instancias al modulo
		        		 modForo.getINSTANCES().getINSTANCE().add(instanceModForo);
	        		 }     
	        		
	        		 //A�adimos el modulo dentro de details una vez hemos hecho todas las modificaciones
	        		 moodle.getINFO().getDETAILS().getMOD().add(modForo); 
	        		 //Incremo el indice
	        		 indWhile++;
	        	 }//fin del for donde se crean los modulos dentro de details 	   
	        	//List <INSTANCE>  listInstance=modForo.getINSTANCES().getINSTANCE();        	  	
        	//ROLES
        	  	//En esta parte no cambiada nada
        	//COURSE  	
	        	COURSE course= moodle.getCOURSE();
	        	//modificamos el paramero header
	        	HEADER header=course.getHEADER();
	        	
	        	//Parametro variables del header
	        	//Como el ID parece que debe ser un numero, cogemos la segunda parte del ID de deploy (iddesign-iddeploy)
	        	//ID
	        		//Para evitar que de error al crear un BigInteger.
	        		if ((lfdeploy.getShortId() !=null) &&
	        			(lfdeploy.getShortId().length() > 0) &&
	        			(lfdeploy.getShortId().lastIndexOf(Deploy.ID_SEPARATOR) != -1)){
	        			header.setID(new BigInteger(lfdeploy.getShortId().substring(lfdeploy.getShortId().lastIndexOf(Deploy.ID_SEPARATOR)+1)));
	        		}
	        	//FULLNAME
	        	 	header.setFULLNAME(lfdeploy.getName());
	        	//SHORTNAME
	        	 	header.setSHORTNAME(lfdeploy.getName());
	        	//IDNUMBER
	        	 	header.setIDNUMBER(lfdeploy.getShortId().substring(lfdeploy.getShortId().lastIndexOf(Deploy.ID_SEPARATOR)+1));
	        	//SUMMARY
	        	 	String sum="Design: "+lfdeploy.getDesign().getName()+" by Author "+ lfdeploy.getDesign().getAuthor()+
	        	 				"<br/>"+ "Deploy: "+lfdeploy.getName()+ " by Author "+lfdeploy.getAuthor() +
	        	 				"<br/>"+ "Type: " +lfdeploy.getDesign().getOriginalDesignType() +
	        	 				"<br/>"+ "Design create: "+lfdeploy.getDesign().getTimestamp() +
	        	 				"<br/>"+ "Objectives: " +lfdeploy.getDesign().getObjectives();
	        	 	header.setSUMMARY(sum);
	        	 //NUMSECTIONS
	        	 //Aqui ira el n�mero de secciones que hay, que calculamos sumando las secciones al valor de tema incremental puesto por el usuario y restando 1 (e.g. 4 secciones a partir del 3, da 6 como �ltimo n�mero)
	        	 int totalSeccionesCurso=0;
	        	 if(lfdeploy.getFieldFromDeployData(getStartingSectionField())!=null) 
	        		 totalSeccionesCurso = (new BigInteger(String.valueOf(numSecciones))).intValue() + (new BigInteger(lfdeploy.getFieldFromDeployData(getStartingSectionField()))).intValue() - 1;
	        	 else 
	        		 totalSeccionesCurso = (new BigInteger(String.valueOf(numSecciones))).intValue();
	        	 
	        	 header.setNUMSECTIONS(new BigInteger(String.valueOf(totalSeccionesCurso)));	
	        	 //Tomo un objeto tipo assignment como base	
	        	 ASSIGNMENT assignmentBase= header.getROLESASSIGNMENTS().getROLE().get(0).getASSIGNMENTS().getASSIGNMENT().get(0);
	        	 ASSIGNMENT assignment;
	        	 //Para el profesor	
	        	 ASSIGNMENTS listaProfesores=header.getROLESASSIGNMENTS().getROLE().get(0).getASSIGNMENTS();
	        	 listaProfesores.setASSIGNMENT(null);
	          	 //Para el profesor	
	        	 ASSIGNMENTS listaAlumnos=header.getROLESASSIGNMENTS().getROLE().get(1).getASSIGNMENTS();
	        	 listaAlumnos.setASSIGNMENT(null);
	        	 assignment=(ASSIGNMENT) assignmentBase.clone();
	        	  	for(int i=0; i<lfdeploy.getParticipants().size(); i++){
	        	  		assignment=new ASSIGNMENT();
	        	  		assignment= (ASSIGNMENT) assignmentBase.clone();
	        	  	    //Obtengo listado con parametros del usuario
			        	String learningEn=lfdeploy.getParticipants().get(i).getLearningEnvironmentData();
			         	
			        	if(learningEn!=null){//if there is LEdata, we parse it and add it to the user parameters
				        	String [] lista=learningEn.split(Participant.USER_PARAMETER_SEPARATOR);
				        	//Modifico parametro userID del assignment
				        	if ((lista !=null) && (lista.length > 0)){
				        		assignment.setUSERID(new BigInteger(lista[0]));
				        	}
			        	}else{
			        		//if no LEdata, we just put the userid=1...x
			        		assignment.setUSERID(new BigInteger(""+(i+1)));
			        	}
			        	if (lfdeploy.getParticipants().get(i).isStaff())
			        		listaProfesores.getASSIGNMENT().add(assignment);			        
			        	else
			        		listaAlumnos.getASSIGNMENT().add(assignment);	        	
		        	}       	 
	        	 
	        	course.setHEADER(header);
	        
	        	
	        //SECTIONS	
	        	List <SECTION>  listSection=moodle.getCOURSE().getSECTIONS().getSECTION();
	           	MOD modBaseSection = listSection.get(0).getMODS().getMOD().get(0);
	           	MOD modSection;
	           	Contenedor contSec;
	           	moodle.getCOURSE().getSECTIONS().setMODList(null);
	        	SECTION section;    	
	        	MODS modsSection;
	        	
	        	/**Creo la secci�n 0, para que moodle no de un warning**/
	        	//Creo la secci�n 0
	        	section = new SECTION();
        		
           		//ID section
        		section.setID(new BigInteger("0"));
        		//NuMBER section
        		section.setNUMBER(new BigInteger("0"));
        		//SUMMARY
        		section.setSUMMARY("");
        		//VISIBLE
        		section.setVISIBLE(new BigInteger("1"));
        		//MODS
        		section.setMODS(new MODS());
	        	//La a�ado al xml
        		moodle.getCOURSE().getSECTIONS().getSECTION().add(section);
        		
        		/**Fin de la secci�n 0**/
        		//LP: Trying to extract how many sections we're creating
        		ArrayList listSecciones = (ArrayList) (getAllActivitys(lfdeploy.getDesign().getRootActivity())).get(1);
        		
	        	//Creamos las secciones... en este caso s�lo hay 1!
        		section = new SECTION();
        		modsSection = new MODS();
        		section.setMODS(modsSection);
        		//ID section
        		section.setID(new BigInteger(String.valueOf(1)));
        		
        		//NuMBER section
        		//Change to set the first section number to a determined number (for incremental deployments)
        		BigInteger startingSection = new BigInteger("1");//Default starting section is 1
        		String lfsection = lfdeploy.getFieldFromDeployData(getStartingSectionField());
        		if(lfsection!=null && lfsection.length()>0){
        			try{
        				int start = Integer.parseInt(lfsection);
        				startingSection = BigInteger.valueOf(start);
        			}catch(Exception e){
        				e.printStackTrace();
        			}
        		}else startingSection = BigInteger.valueOf(1);
        		section.setNUMBER(startingSection);
        		
        		//Ponemos el nombre de la secci�n como el nombre del despliegue, y conectamos con GLUE!-PS
        		String nameSect = "<b>"+lfdeploy.getName()+"</b><br/>";
        		if (getLdShakeMode()==false){
        			nameSect += "... this section was generated with <a href=\""+lfdeploy.getId().replace("/deploys/", "/gui/glueps/deploy.html?deployId=")+"\" target=\"_new\">GLUE!-PS</a><br/>";
        		}
        		section.setSUMMARY(nameSect);
        		//VISIBLE section
        		section.setVISIBLE(new BigInteger("1"));
        		
        		
	        	//for (int i=0; i< listSecciones.size(); i++){
 
	        		 it = prueba.keySet().iterator(); 
	        		//recorremos el hashMap
	        	 	while (it.hasNext()) {
	       	            // Get Clave
	       	            String tipoModulo = (String) it.next();
	       	            listConten = (ArrayList <Contenedor>) ((ArrayList)prueba.get(tipoModulo)).get(1);
	       	            
	       	            //Recorremos objtos cont (representa un modulo) y vamos creando modulos
	       	            for(int j=0; j<listConten.size();j++){
	       	            		contSec=listConten.get(j);
	       	            	//if (contSec.getNumSection()==1){
	       	            	if(true){//we put all mods in this section
		       	               	modSection= new MOD();
		       	            	modSection= (MOD) modBaseSection.clone();
		       	            	//ID
		       	            	modSection.setID(new BigInteger(String.valueOf(contSec.getModId())));
		       	            	//TYPE
		       	            	Class declaredSecType= String.class;
		       		        	QName nameSecType=new QName("TYPE");
		       		        	Class scopeSecType  =String.class;
		       		        	List<Object> listSecType= new  ArrayList();
		       		        	listSecType.add(new JAXBElement(nameSecType,declaredSecType,scopeSecType,contSec.getTipoModulo()));
		       		        	modSection.setDESCRIPTIONOrFEEDBACKOrFORMAT(listSecType);
		       		        			       		        	
		       	            	//INSTANCE
		       		        	INSTANCE instMod1= new INSTANCE();
		       		        	//TODO modSection.getID()
		       		        	//JAXBElement jb=(JAXBElement)modBaseSection.getINSTANCE().getContent().get(0).se;
		       		        	//jb.setValue(modSection.getID().toString());
		       		        	//instMod1.getContent().add(jb);
		       		        	instMod1.getContent().add(modSection.getID().toString());
		       		        	modSection.setINSTANCE(instMod1);
		       		        	if(contSec.getGroupingId() != null){
		       		        		modSection.setGROUPINGID(new BigInteger(String.valueOf(contSec.getGroupingId())));
		       		        	}
		       		        	BigInteger cero= new BigInteger("0");
		       		        	//GROUPMEMBERONLY
		       		          	 if  ((modSection.getGROUPINGID().equals(cero)))
		       		          		modSection.setGROUPMEMBERSONLY(cero);
		       		        	 else
		       		        		 modSection.setGROUPMEMBERSONLY(new BigInteger("1"));
		       		          section.getMODS().getMOD().add(modSection);   
	       	            	}
	       		          	    		        	
	       	            }	       
		        	}         		
	        		
	        		//a�ado una nueva secci�n
	   	         	moodle.getCOURSE().getSECTIONS().getSECTION().add(section);
	        	//}	        
	        	
	        	
	       //USERS
	        	//Guardo usuario base
	        	USER userBase=(USER) course.getUSERS().getContent().get(0);
	        	USER user;
	        	course.getUSERS().setContent(null);  
	        	
	        	//We generate a random (large) number in case we have to generate user ids
	        	long random = (new Date()).getTime();
	        	
	        	// List<Object> listUsers=new ArrayList();
	        	for(int i=0; i<lfdeploy.getParticipants().size(); i++){
		        	//Genero un nuevo usuario
	        		//TODO intentar clonar userbase
	        		user= new USER();
	        		user=(USER) userBase.clone();
		        	
		        	//Parametros a modificar de user
	        		Participant p = lfdeploy.getParticipants().get(i);
		        	String learningEn=p.getLearningEnvironmentData();
		        	
		        	if(learningEn!=null){//if we have LE data
		        	
			        	String [] lista=learningEn.split(Participant.USER_PARAMETER_SEPARATOR);
			        	//Para evitar que se produzca un error por que no existen participantes.
			        	if ((lista !=null)&&(lista.length > 0)){
				        	//ID
				        		user.setID(new BigInteger(lista[0]));
				        	//USERNAME
				        		user.setUSERNAME(lista[1]);
				        	//EMAIL
				        		user.setEMAIL(lista[2]);
				        	//FIRSTACCESS
				        		user.setFIRSTACCESS(new BigInteger(lista[3].trim()));
			        	}

		        	}else{//if we do not have LE data, we do the best we can: just put an username and id to it and pray it works... :$
		        		
		        		user.setID(new BigInteger(""+(i+1))); //we try with a XXXXXXXXX+1..x integer
		        		user.setUSERNAME(p.getName().replaceAll(" ", ""));
		        		user.setEMAIL("username"+random+user.getID()+"@mydomain.com");//The email must be set and be unique? we put a fake one...
		        	}
			        //listUsers.add(user);
		        	course.getUSERS().getContent().add(user);
	        	} 
	        	
	        	GROUP groupBase=course.getGROUPS().getGROUP().get(0);
	        	GROUP group;
	        	MEMBER memberBase=course.getGROUPS().getGROUP().get(0).getMEMBERS().getMEMBER().get(0);
	        	MEMBER member;
	        	course.getGROUPS().setGROUP(null);
	        //GROUPS
	        	String [] listPart=null;
	        	for(int i=0; i<lfdeploy.getGroups().size(); i++){
	        	//creo un nuevo grupo
	        		group= new GROUP();
	        		group= (GROUP) groupBase.clone();
	        		//LP: modify groups so that they coincide with the ones in the Moodle originally, just in case
	        		try{
	        			
	        			BigInteger value = new BigInteger(lfdeploy.getGroups().get(i).getId());
	        			value = new BigInteger(""+(value.longValue()+1));//We need that the ids go from 1...x
	        			group.setID(value);
	        			
	        		}catch (Exception e) {//The ID is not a number, we set it to the string id hashcode (absolute value, first 9 digits to prevent db overflow)
	        			int absolute = Math.abs(lfdeploy.getGroups().get(i).getId().hashCode());
	        			int digits = (absolute%1000000000)+1;//so that the minimum is 1!
	        			group.setID(BigInteger.valueOf(digits));
					}
	        		//BUG: si varios grupos comparten el mismo nombre, se transforman en un solo grupo en Moodle. Soluci�n: a�adir iterador para asegurarnos de que es �nico
	        		//group.setNAME(lfdeploy.getGroups().get(i).getName()+" ("+(i+1)+")");
	        		group.setNAME(lfdeploy.getGroups().get(i).getName());
	        		
	        		//group.setDESCRIPTION(lfdeploy.getGroups().get(i).getName());
	        		group.setDESCRIPTION("");
	        		MEMBERS members=new MEMBERS();
	        		group.setMEMBERS(members);
	        		if (lfdeploy.getGroups().get(i).getParticipantIds()!=null){
		        		for(int j=0; j<lfdeploy.getGroups().get(i).getParticipantIds().size(); j++){
		        			//obtengo el participante
		        			Participant part=lfdeploy.getParticipantById(lfdeploy.getGroups().get(i).getParticipantIds().get(j));
		        			member=new MEMBER();
		        			member= (MEMBER) memberBase.clone();
		        			
		        			if(part.getLearningEnvironmentData()!=null){
			        			listPart = part.getLearningEnvironmentData().split(Participant.USER_PARAMETER_SEPARATOR);
			        			if ((listPart !=null)&&(listPart.length > 0)){
				        			//Member.userId
				        			member.setUSERID(new BigInteger(listPart[0]));
			        			}
		        			}else{
		        				
		        				//if there is no LEdata, we get it from the previous step
		        				BigInteger userid = null;
		        				//we search for the user with the same name as this one, and look at its userid
		        				for(Iterator iter = course.getUSERS().getContent().iterator(); iter.hasNext();){
		        					USER u = (USER) iter.next();
		        					if(part.getName().equals(u.getUSERNAME())){
		        						userid=u.getID();
		        						break;
		        					}
		        				}
								member.setUSERID(userid);

		        			}
		        			//Member.groupId
		        			member.setGROUPID(group.getID());
		        			group.getMEMBERS().getMEMBER().add(member);
		        		}
		        		//A�adimos un grupo a la lista
		        		course.getGROUPS().getGROUP().add(group);
	        		}
	        		else{
	        			ArrayList <MEMBER> emptyMember = new ArrayList <MEMBER> ();
	        			group.getMEMBERS().setMEMBER(emptyMember);
	        			course.getGROUPS().getGROUP().add(group);	        			
	        		}
	        	
	        	}
        
	        //GROUPINGS	
	        GROUPING groupingBase = course.getGROUPINGS().getGROUPING().get(0);
	        GROUPING grouping;
	        course.getGROUPINGS().setGROUPING(null)	;
	        List <GROUP> listGroup = course.getGROUPS().getGROUP();
	        for(int i=0; i<listGroup.size();i++){
	        	grouping=(GROUPING) groupingBase.clone();
	        	grouping.setID(listGroup.get(i).getID());
	        	grouping.setNAME(listGroup.get(i).getNAME());
	        	//grouping.setDESCRIPTION(listGroup.get(i).getNAME());	
	        	grouping.setDESCRIPTION("");
	        	course.getGROUPINGS().getGROUPING().add(grouping);
	        }
	        
	        //GROUPINGSGROUPS
	        GROUPINGGROUP groupingsGroupBase = course.getGROUPINGSGROUPS().getGROUPINGGROUP().get(0);
	        GROUPINGGROUP groupingGroup;
	        course.getGROUPINGSGROUPS().setGROUPINGGROUP(null);
	        for(int i=0; i<listGroup.size();i++){
	        	groupingGroup=(GROUPINGGROUP) groupingsGroupBase.clone();
	        	groupingGroup.setID(listGroup.get(i).getID());
	        	groupingGroup.setGROUPINGID(listGroup.get(i).getID());
	        	groupingGroup.setGROUPID(listGroup.get(i).getID());
	        	course.getGROUPINGSGROUPS().getGROUPINGGROUP().add(groupingGroup);
	        }
	        
	        List <MOD> listModCourse= course.getMODULES().getMOD();
	        course.getMODULES().setMODList(null);
	        MOD mod = null;
	        List<Object> listBase=null;
	        List <Object> listD= new ArrayList();
	        INSTANCE instanceCourse;
	        
	        it = prueba.keySet().iterator(); 
			//recorremos el hashMap
    	 	while (it.hasNext()) {
   	            // Get Clave
   	            String tipoModulo = (String) it.next();
   	            listConten = (ArrayList <Contenedor>) ((ArrayList)prueba.get(tipoModulo)).get(1);
   	            ArrayList <Activity> listActMod=(ArrayList <Activity>) ((ArrayList)prueba.get(tipoModulo)).get(0);
   	        
	        	listBase=new ArrayList();
	         	Class declaredTypeG=null;
		        QName nameG=null;
		        Class scopeG=null; 
	        	//Buscar el tipo de modulo
	               	
		        for(int i=0; i<listConten.size();i++){	
		        	mod=new MOD();
		        	
	            	//FORUM
	            	if (("forum").equals(tipoModulo)){
	            		listD= new ArrayList();
	            	    for(int j=0; j<listModCourse.size(); j++){
	            	      	if ((listModCourse.get(j).getMODTYPE().equals(tipoModulo)) &&
			            		((JAXBElement)(listModCourse.get(j).getDESCRIPTIONOrFEEDBACKOrFORMAT().get(0))).getValue().equals("general")){
			               		mod=(MOD) listModCourse.get(j).clone();
			               		listBase=new ArrayList();
			               		listBase=listModCourse.get(j).getDESCRIPTIONOrFEEDBACKOrFORMAT();
			                  	 for(int g=0; g<listBase.size()-1;g++){
					               	//Para evitar si vienen algun objeto de tipo String
				        				 if (String.class !=  listBase.get(g).getClass()){
					        					 //Para evitar que entren objeto que no se han JAXBElement
					        					 if (JAXBElement.class ==  listBase.get(g).getClass()){
								 	                declaredTypeG=  ((JAXBElement)listBase.get(g)).getDeclaredType();
								 		        	nameG=((JAXBElement)listBase.get(g)).getName();
								 		        	scopeG  =((JAXBElement)listBase.get(g)).getScope();
								 		        	listD.add(new JAXBElement(nameG,declaredTypeG,scopeG,((JAXBElement)listBase.get(g)).getValue()));
					        					 }
				        					 }
					 	            }
			                  	 nameG= new QName("TIMEMODIFIED");
			               	 //TODO buscar como crear un tipo class
			                 declaredTypeG= BigInteger.class;
			               	 listD.add(new JAXBElement(nameG,declaredTypeG,null,new BigInteger("0")));
			            	 break;
			            	}
	            	    }
	            	    //Relleno modulo
	            	    //MODULO TIPO FORUM
	    	            /*
	    	             En DESCRIPTIONOrFEEDBACKOrFORMAT :
	    	                posici�n 0 -->Atributo TYPE
	    	              	posici�n 1 -->Atributo NAME
	    	              	posici�n 2 -->Atributo INTRO
	    	             */
	 
	    	        	//Modifico  los parametros.
	    	            //ID
	            	    mod.setID(new BigInteger(String.valueOf(listConten.get(i).getModId())));
	            	    //NAME
	    	            ((JAXBElement)listD.get(1)).setValue(listConten.get(i).getModName());
	    	            
	    	            //INTRO
	    	            for(int d=0;d<listActMod.size();d++){
	    	            	if(listActMod.get(d).getId()==listConten.get(i).getIdActivity()){
	    	            		 ((JAXBElement)listD.get(2)).setValue(listActMod.get(d).getDescription());
	    	            	}	    	            	
	    	            }	    	           
	    	            //((JAXBElement)listD.get(2)).setValue(listConten.get(i).getModName());
	    	            mod.setDESCRIPTIONOrFEEDBACKOrFORMAT(null);
	    	            mod.setDESCRIPTIONOrFEEDBACKOrFORMAT(listD);          	    
	            	}else{
	            		//CHAT
	            		if (("chat").equals(tipoModulo)){
	            			listD= new ArrayList();
	            		    for(int j=0; j<listModCourse.size(); j++){   
		            	       	if ((listModCourse.get(j).getMODTYPE().equals(tipoModulo))){
		    		               		mod=(MOD) listModCourse.get(j).clone();	
		    		               		listBase=new ArrayList();
		    		               		listBase=listModCourse.get(j).getDESCRIPTIONOrFEEDBACKOrFORMAT();
		    				               	 for(int g=0; g<listBase.size()-1;g++){
		    				               		 //Para evitar si vienen algun objeto de tipo String
						        				 if (String.class !=  listBase.get(g).getClass()){
							        					 //Para evitar que entren objeto que no se han JAXBElement
							        					 if (JAXBElement.class ==  listBase.get(g).getClass()){ 
					    				 	                declaredTypeG=  ((JAXBElement)listBase.get(g)).getDeclaredType();
					    				 		        	nameG=((JAXBElement)listBase.get(g)).getName();
					    				 		        	scopeG  =((JAXBElement)listBase.get(g)).getScope();
					    				 		        	listD.add(new JAXBElement(nameG,declaredTypeG,scopeG,((JAXBElement)listBase.get(g)).getValue()));
							        					 }
						        				 }
		    				 	            }
		    		               	 nameG= new QName("TIMEMODIFIED");
		    		               	 //TODO buscar como crear un tipo class
		    		                 declaredTypeG= BigInteger.class;//((JAXBElement)listBase.get()).getDeclaredType();
		    		               	 listD.add(new JAXBElement(nameG,declaredTypeG,null,new BigInteger("0")));
		    		            	 break;
		    		            	} 
	            		    }
	            		    //modificamos parametros
	            		
		    	        	//Modifico  los parametros.
		    	            //ID
		    	            mod.setID(new BigInteger(String.valueOf(listConten.get(i).getModId())));
		    	            
		    	            //NAME
		    	            ((JAXBElement)listD.get(0)).setValue(listConten.get(i).getModName());
		    	            
		    	            //INTRO
		    	            for(int d=0;d<listActMod.size();d++){
		    	            	if(listActMod.get(d).getId()==listConten.get(i).getIdActivity()){
		    	            		 ((JAXBElement)listD.get(1)).setValue(listActMod.get(d).getDescription());
		    	            	}	    	            	
		    	            }	  
		    	            //((JAXBElement)listD.get(1)).setValue(listConten.get(i).getModName());
		    	            mod.setDESCRIPTIONOrFEEDBACKOrFORMAT(null);
		    	            mod.setDESCRIPTIONOrFEEDBACKOrFORMAT(listD);             		    
	            		}else{
	            			//QUIZ
		            		if (("quiz").equals(tipoModulo)){
		            			listD= new ArrayList();
		            		    for(int j=0; j<listModCourse.size(); j++){   
			            	       	if ((listModCourse.get(j).getMODTYPE().equals(tipoModulo))){
			                           		mod=(MOD) listModCourse.get(j).clone();	  
			    		               		listBase=new ArrayList();
			    		               		listBase=listModCourse.get(j).getDESCRIPTIONOrFEEDBACKOrFORMAT();
			    				               	 for(int g=0; g<listBase.size();g++){
			    				               		//Para evitar si vienen algun objeto de tipo String
							        				 if (String.class !=  listBase.get(g).getClass()){ 
					    				               		//if((g!=17) && (g!=18)&& (g!=20)&& (g!=21)){
							        					//Para evitar que entren objeto que no se han JAXBElement
							        					 if (JAXBElement.class ==  listBase.get(g).getClass()){
					    				               			declaredTypeG=  ((JAXBElement)listBase.get(g)).getDeclaredType();
								    				 		    nameG=((JAXBElement)listBase.get(g)).getName();
								    				 		    scopeG  =((JAXBElement)listBase.get(g)).getScope();
								    				 		    listD.add(new JAXBElement(nameG,declaredTypeG,scopeG,((JAXBElement)listBase.get(g)).getValue()));
									        				
					    				               		}
					    				 		        	else{
					    				 		        		listD.add(listBase.get(g));	
					    				 		        	}
							        				 }
			    				 	            }
			    		               /*	 nameG= new QName("TIMEMODIFIED");
			    		               	 //TODO buscar como crear un tipo class
			    		                 declaredTypeG= ((JAXBElement)listBase.get(10)).getDeclaredType();
			    		               	 listD.add(new JAXBElement(nameG,declaredTypeG,null,new BigInteger("0")));*/
			    		            	 break;
			    		            	}
		            		    }
		            	
			    	        	//Modifico  los parametros.
			    	            //ID
			    	            mod.setID(new BigInteger(String.valueOf(listConten.get(i).getModId())));
			    	            
			    	            //NAME
			    	            ((JAXBElement)listD.get(0)).setValue(listConten.get(i).getModName());
			    	            
			    	            //INTRO
			    	            for(int d=0;d<listActMod.size();d++){
			    	            	if(listActMod.get(d).getId()==listConten.get(i).getIdActivity()){
			    	            		 ((JAXBElement)listD.get(1)).setValue(listActMod.get(d).getDescription());
			    	            	}	    	            	
			    	            }	  
			    	            //((JAXBElement)listD.get(1)).setValue(listConten.get(i).getModName());
			    	            mod.setDESCRIPTIONOrFEEDBACKOrFORMAT(null);
			    	            mod.setDESCRIPTIONOrFEEDBACKOrFORMAT(listD);  
			    	            //FEEDBACK 
			    	            /*
			    	             posici�n 0 ID
			    	             posicion 1 QUIZID
			    	             * */
			    	            listBase=new ArrayList();
			    	            FEEDBACKS feedbs= new FEEDBACKS();
			    	            listBase=mod.getFEEDBACKS().getFEEDBACK().getContent();
			    	            mod.setFEEDBACKS(feedbs);
			    	            //Para cada modulo debo crear un nuevo FEEDBAKCS por que si no me va a poner lo mismo
			    	            // en todos los modulos con el mismo identificador de objeto.
			    	           FEEDBACK feed= new FEEDBACK();
			    	           for(int g=0; g<listBase.size()-1;g++){
			    	        	   if (String.class !=  listBase.get(g).getClass()){ 
	 				               		//if((g!=2)){
			    	        		   if (JAXBElement.class ==  listBase.get(g).getClass()){
		    				 	                declaredTypeG=  ((JAXBElement)listBase.get(g)).getDeclaredType();
		    				 		        	nameG=((JAXBElement)listBase.get(g)).getName();
		    				 		        	scopeG  =((JAXBElement)listBase.get(g)).getScope();
		    				 		        	if ((nameG.getLocalPart().equals("ID")) || (nameG.getLocalPart().equals("QUIZID")))
		 				 		        			feed.getContent().add(new JAXBElement(nameG,declaredTypeG,scopeG,mod.getID()));	
		    				 		        	else
		    				 		        		feed.getContent().add(new JAXBElement(nameG,declaredTypeG,scopeG,((JAXBElement)listBase.get(g)).getValue()));
	 				               		}
	 				 		        	else{
	 				 		          			feed.getContent().add(listBase.get(g));
	 				 		        	}
			    	           		}
 				 	            }
			    	        	 mod.getFEEDBACKS().setFEEDBACK(feed);			    	            
		            		}else{
		            			if (("resource").equals(tipoModulo)){
			            			listD= new ArrayList();
			            			//RESOURCE  	
			            		    for(int j=0; j<listModCourse.size(); j++){   
				            	       	if ((listModCourse.get(j).getMODTYPE().equals(tipoModulo))){
				    		               		mod=(MOD) listModCourse.get(j).clone();	 
				    		               		listBase=new ArrayList();
				    		               		listBase=listModCourse.get(j).getDESCRIPTIONOrFEEDBACKOrFORMAT();
				    				               	 for(int g=0; g<listBase.size();g++){
				    				               		//if ((g!=6) && (g!=7)){
				    				               		if (String.class !=  listBase.get(g).getClass()){  
				    				               			//Para evitar que entren objeto que no se han JAXBElement
				    				               			if (JAXBElement.class ==  listBase.get(g).getClass()){
				    				               				declaredTypeG=  ((JAXBElement)listBase.get(g)).getDeclaredType();
				    				               				nameG=((JAXBElement)listBase.get(g)).getName();
				    				               				scopeG  =((JAXBElement)listBase.get(g)).getScope();
				    				               				listD.add(new JAXBElement(nameG,declaredTypeG,scopeG,((JAXBElement)listBase.get(g)).getValue()));
				    				               			}
				    				 		        	   	else{
				    				 		        	   		listD.add(listBase.get(g));			    				 		        	   		
				    				 		        	   	} 
				    				               		}
				    				 	            }
				    		           /*    	 nameG= new QName("TIMEMODIFIED");
				    		               	 //TODO buscar como crear un tipo class
				    		                 declaredTypeG= ((JAXBElement)listBase.get(listBase.size()-1)).getDeclaredType();
				    		               	 listD.add(new JAXBElement(nameG,declaredTypeG,null,new BigInteger("0")));*/
				    		            	 break;
				    		            	}	
			            		    }	
			            		  //MODULO TIPO RESOURCE
				    	            /*
				    	             En DESCRIPTIONOrFEEDBACKOrFORMAT :
				    	                posici�n 0 -->Atributo NAME
				    	              	posici�n 1 -->Atributo TYPE
				    	              	posici�n 2 -->Atributo REFERENCE
				    	              	posici�n 3 -->Atributo SUMMARY
				    	              	posici�n 4 -->Atributo ALLTEST
				    	              	posici�n 5 -->Atributo POPUP
				    	              	posici�n 6 -->Atributo OPTIONS
				    	              	posici�n 7 -->Atributo TIMEMODIFIED
				    	             */
				         
				    	        	//Modifico  los parametros.
				    	            //ID
				    	            mod.setID(new BigInteger(String.valueOf(listConten.get(i).getModId())));
				    	            
				    	            //NAME
				    	            ((JAXBElement)listD.get(0)).setValue(listConten.get(i).getModName());
				    	            
				    	            //TYPE
				    	            ((JAXBElement)listD.get(1)).setValue(listConten.get(i).getSubTipoModulo());
				    	            
				    	            //SUMMARY
				    	            for(int d=0;d<listActMod.size();d++){
				    	            	if(listActMod.get(d).getId()==listConten.get(i).getIdActivity()){
				    	            		 ((JAXBElement)listD.get(3)).setValue(listActMod.get(d).getDescription());
				    	            	}	    	            	
				    	            }	  
				    	            //((JAXBElement)listD.get(3)).setValue(listConten.get(i).getModName());
				    	            
				    	            //ALLTEST
				    	            if (listConten.get(i).getAllTest() != null)
				    	            	 ((JAXBElement)listD.get(4)).setValue(listConten.get(i).getAllTest());
				    	              else
				    	            	 ((JAXBElement)listD.get(4)).setValue("");
				    	            
				    	            //POPUP
				    	            if (listConten.get(i).getPopUP() != null)
				    	            	 ((JAXBElement)listD.get(5)).setValue(listConten.get(i).getPopUP());
				    	              else
				    	            	 ((JAXBElement)listD.get(5)).setValue("");
				    	            
				    	            //REFERENCES.
				    	            if (listConten.get(i).getLocation() != null)
				    	            	((JAXBElement)listD.get(2)).setValue(listConten.get(i).getLocation());
				    	            else
				    	            	((JAXBElement)listD.get(2)).setValue("");
				    	            
				    	            mod.setDESCRIPTIONOrFEEDBACKOrFORMAT(null);
				    	            mod.setDESCRIPTIONOrFEEDBACKOrFORMAT(listD);  
			            		    
				    	            //OPTIONS
				    	            OPTIONS options= new OPTIONS();
				    	            //options.getContent().add("frame");
				    	            options.getContent().add(listConten.get(i).getOptions());
				    	            listD.set(6, options);
			            		    
			            		}else{
			            			String typeAssignment="";
			            			//Tratamiento de modulo Assignment
			            			if (("assignment").equals(tipoModulo)){
			            				listD= new ArrayList();		            				
			            				for(int j=0; j<listModCourse.size(); j++){
			            					//Obtengo el tipo de assignment buscando en los jaxbelement
			            					for(int z=0; z<listModCourse.get(j).getDESCRIPTIONOrFEEDBACKOrFORMAT().size(); z++){
				            					if ((JAXBElement.class ==  listModCourse.get(j).getDESCRIPTIONOrFEEDBACKOrFORMAT().get(z).getClass()) &&
				            					    (((JAXBElement)(listModCourse.get(j).getDESCRIPTIONOrFEEDBACKOrFORMAT().get(z))).getName().getLocalPart().equals("ASSIGNMENTTYPE"))){
				            						typeAssignment=(String) ((JAXBElement)(listModCourse.get(j).getDESCRIPTIONOrFEEDBACKOrFORMAT().get(z))).getValue();
				            						break;
				            					}			            					
				            				}			            					
			            					if ((listModCourse.get(j).getMODTYPE().equals(tipoModulo)) &&
			        			            		(typeAssignment.toUpperCase().equals("UPLOADSINGLE"))){
			            						mod=(MOD) listModCourse.get(j).clone();
			    			               		listBase=new ArrayList();
			    			               		listBase=listModCourse.get(j).getDESCRIPTIONOrFEEDBACKOrFORMAT();
			    				               	for(int g=0; g<listBase.size();g++){
					    			               	 if (JAXBElement.class ==  listBase.get(g).getClass()){
				    				               			declaredTypeG=  ((JAXBElement)listBase.get(g)).getDeclaredType();
							    				 		    nameG=((JAXBElement)listBase.get(g)).getName();
							    				 		    scopeG  =((JAXBElement)listBase.get(g)).getScope();
							    				 		    listD.add(new JAXBElement(nameG,declaredTypeG,scopeG,((JAXBElement)listBase.get(g)).getValue()));
								        				
				    				               		}
				    				 		        	else{
				    				 		        		listD.add(listBase.get(g));	
				    				 		        	}
			    				               	 }
			    				               	break;
			            					}			            					
			            				}
			            				//MODULO TIPO Assignment
					    	            /*
					    	             En DESCRIPTIONOrFEEDBACKOrFORMAT :
					    	                posici�n 0 -->Atributo NAME
					    	              	posici�n 1 -->Atributo DESCRIPTION	
					    	              	posicion 7 -->Atributo assignmenttype				    	             
					    	             */
			            				 //ID
					    	               mod.setID(new BigInteger(String.valueOf(listConten.get(i).getModId())));
			            				 //NAME
			            				 	((JAXBElement)listD.get(0)).setValue(listConten.get(i).getModName());
			            				 //DESCRIPTION
			            				 	for(int d=0;d<listActMod.size();d++){
			        	    	            	if(listActMod.get(d).getId()==listConten.get(i).getIdActivity()){
			        	    	            		 ((JAXBElement)listD.get(1)).setValue(listActMod.get(d).getDescription());
			        	    	            	}	    	            	
			        	    	            }	  
			            				 	//((JAXBElement)listD.get(1)).setValue(listConten.get(i).getModName());
			            				mod.setDESCRIPTIONOrFEEDBACKOrFORMAT(null);
						    	        mod.setDESCRIPTIONOrFEEDBACKOrFORMAT(listD);  
			            			}else{
			            				if (("text").equals(tipoModulo)){
					            			listD= new ArrayList();
					            			//Modulo resource-text para actividades sin recursos  	
					            		    for(int j=0; j<listModCourse.size(); j++){   
						            	       	if ((listModCourse.get(j).getMODTYPE().equals("resource"))){
						    		               		mod=(MOD) listModCourse.get(j).clone();	 
						    		               		listBase=new ArrayList();
						    		               		listBase=listModCourse.get(j).getDESCRIPTIONOrFEEDBACKOrFORMAT();
						    				               	 for(int g=0; g<listBase.size();g++){
						    				               		//if ((g!=6) && (g!=7)){
						    				               		if (String.class !=  listBase.get(g).getClass()){  
						    				               			//Para evitar que entren objeto que no se han JAXBElement
						    				               			if (JAXBElement.class ==  listBase.get(g).getClass()){
						    				               				declaredTypeG=  ((JAXBElement)listBase.get(g)).getDeclaredType();
						    				               				nameG=((JAXBElement)listBase.get(g)).getName();
						    				               				scopeG  =((JAXBElement)listBase.get(g)).getScope();
						    				               				listD.add(new JAXBElement(nameG,declaredTypeG,scopeG,((JAXBElement)listBase.get(g)).getValue()));
						    				               			}
						    				 		        	   	else{
						    				 		        	   		listD.add(listBase.get(g));			    				 		        	   		
						    				 		        	   	} 
						    				               		}
						    				 	            }
						    		           /*    	 nameG= new QName("TIMEMODIFIED");
						    		               	 //TODO buscar como crear un tipo class
						    		                 declaredTypeG= ((JAXBElement)listBase.get(listBase.size()-1)).getDeclaredType();
						    		               	 listD.add(new JAXBElement(nameG,declaredTypeG,null,new BigInteger("0")));*/
						    		            	 break;
						    		            	}	
					            		    }	
					            		  //MODULO TIPO RESOURCE
						    	            /*
						    	             En DESCRIPTIONOrFEEDBACKOrFORMAT :
						    	                posici�n 0 -->Atributo NAME
						    	              	posici�n 1 -->Atributo TYPE
						    	              	posici�n 2 -->Atributo REFERENCE
						    	              	posici�n 3 -->Atributo SUMMARY
						    	              	posici�n 4 -->Atributo ALLTEST
						    	              	posici�n 5 -->Atributo POPUP
						    	              	posici�n 6 -->Atributo OPTIONS
						    	              	posici�n 7 -->Atributo TIMEMODIFIED
						    	             */
						         
						    	        	//Modifico  los parametros.
						    	            //ID
						    	            mod.setID(new BigInteger(String.valueOf(listConten.get(i).getModId())));
						    	            
						    	            //NAME
						    	            ((JAXBElement)listD.get(0)).setValue(listConten.get(i).getModName());
						    	            
						    	            //TYPE
						    	            //((JAXBElement)listD.get(1)).setValue(listConten.get(i).getSubTipoModulo());
						    	            
						    	            //SUMMARY
						    	            //((JAXBElement)listD.get(3)).setValue(listConten.get(i).getModName());
						    	            
						    	            //ALLTEST
						    	          /*  if (listConten.get(i).getAllTest() != null)
						    	            	 ((JAXBElement)listD.get(4)).setValue(listConten.get(i).getAllTest());
						    	              else
						    	            	 ((JAXBElement)listD.get(4)).setValue("");*/
						    	            
						    	            //POPUP
						    	            /*if (listConten.get(i).getPopUP() != null)
						    	            	 ((JAXBElement)listD.get(5)).setValue(listConten.get(i).getPopUP());
						    	              else
						    	            	 ((JAXBElement)listD.get(5)).setValue("");*/
						    	            
						    	         /*   //REFERENCES.
						    	            if (listConten.get(i).getLocation() != null)
						    	            	((JAXBElement)listD.get(2)).setValue(listConten.get(i).getLocation());
						    	            else
						    	            	((JAXBElement)listD.get(2)).setValue("");
						    	            
						    	            mod.setDESCRIPTIONOrFEEDBACKOrFORMAT(null);
						    	            mod.setDESCRIPTIONOrFEEDBACKOrFORMAT(listD);  
					            		    
						    	            //OPTIONS
						    	            OPTIONS options= new OPTIONS();
						    	            //options.getContent().add("frame");
						    	            options.getContent().add(listConten.get(i).getOptions());
						    	            listD.set(6, options);*/
					            		    
					            		}			            				
			            				
			            			}			            			
			            		}    
		            		}  			            			
	            		}           		
	            	}   
	            	//A�adimos el modulo creado
	            	course.getMODULES().getMOD().add(mod);
    	 	}	//Recorremos todos los modulos
	        	//}   
	            
	   
	
	        }
	      
    	/**GENERO DIRECTORIOS PARA HACER EL ZIP**/ 	
    	 	Date hora= new Date();
			
			//Variable que contiene el directorio temporal con el contenido de hora y donde voy a copiar el contenido
			//de upload
			File dirXML=new File(TMP_DIR+File.separator+String.valueOf(hora.getTime()));
		
			//Va a contener el nombre del directorio
			File dirFilesLF=new File(TMP_DIR+File.separator+String.valueOf(hora.getTime()+File.separator+"course_files"));
			//Genero dentro del directorio temporal, donde se almacenaran los ficheros de uploader
			dirFilesLF.mkdirs();
			
			//Para Generar el directorio donde se deja el zip
			File directoryZip= new File(TMP_DIR);
			
			
			
			/**GENERO EL XML QUES EL BACKUD DE MOODEL**/
    	 	
    	 	
    	 	
    	 	
         //Creamos el xml 
        Marshaller marshaller = JAXBContext.newInstance(CLASSES).createMarshaller();
        //Para eliminar que nos muestre en el nodo raiz del xml standalone="true"
       // marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE); 
        byte[] xmlHeaderBytes = getXMLHeaderByteArray(XML_HEADER);
        FileOutputStream fichero =new FileOutputStream(dirXML.toString()+File.separator+BACKUPFILENAME);
        try {
			fichero.write(xmlHeaderBytes, 0, xmlHeaderBytes.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //Esta Propiedad formatea el c�digo del XML
        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
        marshaller.marshal(moodle, fichero);
        fichero.close();
        System.out.print("Generado xml en "+ dirXML.toString()+File.separator+BACKUPFILENAME +" y se ha leido informaci�n del archivo "+TEMPLATE);		
        
        /************COPIO LOS ARCHIVOS AL DIRECTORIO CREADO Y GENERO EL ZIP*****************/
        Utilidad uti = new Utilidad();
		
		//Copio archivos del directorio base al temporal
		uti.copyDirectory(new File(BASE+lfdeploy.getDesign().getTrimmedId()+File.separator),dirFilesLF);
		
		//Como el zip se tiene que generar en un directorio no deja que se haga en c
		if (!directoryZip.isDirectory())
			directoryZip.mkdir();
		//Creo fichero del directorio temporal donde guardo ficheros de lingua Franca
		NZipCompresser c = new NZipCompresser(dirXML,ZIPNAME);
		try {
			c.compress();
			this.moodleZip=new File(ZIPNAME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Borro Directorio
		while (dirXML.exists()){
			uti.deleteDirectory(dirXML);
		}
       
		return this.moodleZip;
		
}
	  private byte[] getXMLHeaderByteArray(String xmlHeader) {
          char[] chars = xmlHeader.toCharArray();
          byte[] bytes = new byte[chars.length];
          for (int i = 0; i < chars.length; i++) {
      bytes[i] = (byte)chars[i];
          }
          return bytes;
        }
	public String getZipName() {
		return ZIPNAME;
	}
	public void setZipName(String zipName) {
		this.ZIPNAME = ZIPNAME;
	}
	
	/***
	 * <p>Funci�n encargada de rellenar el hashmap que va a contener los modulos a crear en el xml de moodle
	 *    La estructura de este hashmap sera la siguiente:
	 *       - Como indice se usa el nombre de cada modulo.
	 *       - Para cada entrada va a contener un array con dos posiciones:
	 *       	->La primera posici�n: Contiene un array donde se almacenaran las actividades que han dado lugar a modulos del tipo indicado en el indice
	 *       	  del hashmap
	 *          ->La segunda posici�n: Contiene un array donde se almacenan objetos del tipo contenedor, cada uno de ellos dara  lugar a un nuevo modulo
	 *            en el xml de moodle. El objeto contenedor tendra la informaci�n necesaria para construir un modulo de moodle
	 *  </p>
	 * @param lfdeploy
	 * @return
	 */
	public HashMap generarHashmap(Deploy lfdeploy) {
		ArrayList<Activity> hijas;
		Contenedor cont = null;
		ArrayList<Contenedor> listCont;
		// Para indicar el numero de secci�n
		int numSection = 0;
		boolean inSection = false;

		HashMap lfMap = new HashMap();
		// Actividad Raiz
		Activity rootActivity = lfdeploy.getDesign().getRootActivity();

		// Inicializo con los hijos del actividad padre
		ArrayList<Activity> listChildren = new ArrayList<Activity>();
		// Obtengo las instancias de actividad que corresponden a esta actividad
		ArrayList listHashMap;
		ArrayList<String> listAux;
		ArrayList listRecursos = lfdeploy.getDesign().getResources();
		ArrayList listToolInstance = lfdeploy.getToolInstances();
		Resource re;
		ToolInstance tol;
		listChildren.add(rootActivity);
		boolean createSection = false;
		int numModulo = 0;
		int numSecPro;
		boolean construyeModulo = false;
		String nameBaseModule = "";
		int totalSecciones = 0;
		// Para tratar caso en que nodo raiz tenga un tollinstance o recurso
		// asociado
		// entonces se pone a esto secci�n 1
		if ((rootActivity.getResourceIds() != null)
				&& (rootActivity.getResourceIds().size() >= 1)) {
			numSection = 1;
			totalSecciones = 1;
			createSection = true;
		}
		// Empiezo a recorrer los hijos del raiz
		while (listChildren.size() != 0) {
			hijas = new ArrayList();
			for (int j = 0; j < listChildren.size(); j++) {
				/* Zona de secciones */
				// Para localizar la vez que se tienen que crear las secciones
				if ((listChildren.size() > 1) && (!inSection)) {
					numSection++;
					createSection = true;
					totalSecciones++;
				} else {
					// Obtengo el n�mero de seccion de la actividad
					if ((numSection >= 1)
							&& (listChildren.get(j).getParentActivityId() != null)) {
						// para evitar situaci�n cuando una actividad no genera
						// ning�n modulo
						// la funci�n nos devuelve 0, por lo que no debemos
						// cambiar el valor de numSection;
						numSecPro = Integer.valueOf(this.getSectionActivity(
								listChildren.get(j).getParentActivityId(),
								lfMap));
						if (numSecPro > 0)
							numSection = numSecPro;
						createSection = false;
					} else {
						// Para el caso especial en que nodo tenga un solo hijo
						// y la secci�n se cree en el segundo o sucesivo
						if ((numSection == 0)
								&& (listChildren.get(j).getResourceIds() != null)
								&& (listChildren.get(j).getResourceIds().size() >= 1)) {
							numSection = 1;
							createSection = true;
						}
					}
				}
				// Nombre base
				if (listChildren.get(j).getParentActivityId() != null)
					nameBaseModule = getNameModule(rootActivity, listChildren
							.get(j).getParentActivityId());

				// Creo los modulos, si la actividad es para desplegar
				if (((Activity) listChildren.get(j)).isToDeploy()) {

					construyeModulo = false;
					for (int i = 0; i < lfdeploy.getInstancedActivities()
							.size(); i++) {
						if (listChildren
								.get(j)
								.getId()
								.equals(lfdeploy.getInstancedActivities()
										.get(i).getActivityId())) {
							// Resource resource,ToolInstance
							// toolInstance,InstancedActivity
							// instancedActivity,String modeActivity

							// Para crear modulos a partir de los recursos
							listAux = lfdeploy.getInstancedActivities().get(i)
									.getResourceIds();

							if (listAux != null) {
								construyeModulo = true;
								for (int k = 0; k < listAux.size(); k++) {
									re = (Resource) getObjectArray(
											listAux.get(k).toString(),
											listRecursos, 1);
									cont = getTypeMod(re, null, lfdeploy
											.getInstancedActivities().get(i),
											listChildren.get(j).getMode(),
											lfdeploy);
									// Rellenamos el resto de parametros que
									// quedan
									// y no se rellenan en getTypeMod
									cont.setNumSection(numSection);
									cont.setCreateSection(createSection);
									if (createSection)
										cont.setModName(cont.getModName());
									else
										cont.setModName(nameBaseModule
												+ " - "
												+ ((Activity) listChildren
														.get(j)).getName()
												+ " - " + cont.getModName());
									cont.setModId(numModulo++);
									cont.setIdActivity(((Activity) listChildren
											.get(j)).getId());
									lfMap = addHashMap(lfMap, cont,
											((Activity) listChildren.get(j)));
								}
							}
							// Para crear modulos a partir de los toolInstance
							listAux = lfdeploy.getInstancedActivities().get(i)
									.getInstancedToolIds();
							if (listAux != null) {
								construyeModulo = true;
								for (int l = 0; l < listAux.size(); l++) {
									tol = (ToolInstance) getObjectArray(listAux
											.get(l).toString(),
											listToolInstance, 2);
									re = (Resource) getObjectArray(
											tol.getResourceId(), listRecursos,
											1);
									// getTypeMod();
									cont = getTypeMod(re, tol, lfdeploy
											.getInstancedActivities().get(i),
											listChildren.get(j).getMode(),
											lfdeploy);
									// Rellenamos el resto de parametros que
									// quedan
									// y no se rellenan en getTypeMod
									cont.setNumSection(numSection);
									cont.setCreateSection(createSection);
									if (createSection)
										cont.setModName(cont.getModName());
									else
										cont.setModName(nameBaseModule
												+ " - "
												+ ((Activity) listChildren
														.get(j)).getName()
												+ " - " + cont.getModName());
									cont.setModId(numModulo++);
									cont.setIdActivity(((Activity) listChildren
											.get(j)).getId());
									lfMap = addHashMap(lfMap, cont,
											((Activity) listChildren.get(j)));
								}
							}
							/*
							 * if (!construyeModulo){
							 * cont=getTypeMod(null,null,lfdeploy
							 * .getInstancedActivities
							 * ().get(i),listChildren.get
							 * (j).getMode(),lfdeploy);
							 * cont.setNumSection(numSection);
							 * cont.setCreateSection(createSection);
							 * cont.setModId(numModulo++);
							 * lfMap=addHashMap(lfMap,cont
							 * ,((Activity)listChildren.get(j))); }
							 */
						}
					}

					if (!construyeModulo) {
						cont = new Contenedor();
						cont.setNumSection(numSection);
						cont.setIdActivity(((Activity) listChildren.get(j))
								.getId());
						cont.setTipoModulo("auxiliar");
						cont.setSubTipoModulo("text");
						lfMap = addHashMap(lfMap, cont,
								((Activity) listChildren.get(j)));
					}

					/*
					 * A�ado hijos de actividad actual ha un array temporal,
					 * para poteriormente procesarlos
					 */
					if (((Activity) listChildren.get(j))
							.getChildrenActivities() != null) {
						for (int h = 0; h < ((Activity) listChildren.get(j))
								.getChildrenActivities().size(); h++) {
							hijas.add(((Activity) listChildren.get(j))
									.getChildrenActivities().get(h));
						}
					}

				}//fin if todeploy
			}
			// Para evitar que genere nuevas secciones
			if (numSection >= 2)
				inSection = true;
			listChildren.clear();
			listChildren = hijas;
		}
		// lfMap.put("numSection", totalSecciones);
		// En esta implementaci�n del adaptador, siempre hay una y s�lo una
		// secci�n
		lfMap.put("numSection", 1);
		return lfMap;
	}
	
	/***
	 * <p>
	 *   Funci�n encargada de ir rellenando el hash map con la actividad y el objeto contenedor  que dar�n lugar m�s
	 *   tarde a los modulos que luego representaremos en el xml.
	 * 
	 * </p>
	 * @param map HashMap: Contiene el HashMap con la informaci�n que de momento hemos recogido de la LF
	 * @param cont  Contenedor: Objeto contenedor a a�adir al HashMap
	 * @param activity Activity: Mandamos actividad que queramos que a�ada al HashMap
	 * @return HashMap. Devuelve el HashMap actualizado con la actividad y el objeto cont, que le indicamos como parametros
	 */
	
	public HashMap addHashMap(HashMap map,Contenedor cont,Activity activity){
		 	ArrayList listHashMap;
		 	ArrayList listCont; 
		 	ArrayList listAct = null;
		 	Boolean existe=false;
		 	Activity act;
			if (map.get(cont.getTipoModulo()) == null){
   				listHashMap= new ArrayList();
   				listAct=new ArrayList();
   				listAct.add(activity);
   				listHashMap.add(listAct);
   				listCont = new ArrayList();
   				listCont.add(cont);
   				listHashMap.add(listCont);
   				map.put(cont.getTipoModulo(),listHashMap);
   			}else{
   				listAct= ((ArrayList)map.get(cont.getTipoModulo()));
   				//Para evitar meter actividades repetidas
   				for (int i=0; i< ((ArrayList)listAct.get(0)).size();i++){
   					act= ((Activity)((ArrayList)listAct.get(0)).get(i));
   					if (act.getId().equals(activity.getId())){
   						existe=true;
   						break;
   					}  					
   				}
   				if (!existe)
   					((ArrayList)listAct.get(0)).add(activity);
   				listCont= ((ArrayList)map.get(cont.getTipoModulo()));
   				((ArrayList)listCont.get(1)).add(cont);
   				map.put(cont.getTipoModulo(),listCont);       				
   			}
		 
			 return map;
		 }
	
	/***
	 * <p>
	 * 	Funci�n encargada de rellenar el objeto contenedor (que en la pr�ctica se va
	 *  a identificar con un modulo, ya que cada objeto contenedor dara lugar a un modulo) que va a contener la informaci�n necesaria
	 *  para generar el archivo xml de moodle
	 * </p>
	 * @param resource:Resource. Cuando se pase solo este atributo y toolInstance a null, se generar el tipo de modulo a partir de este atributo
	 * @param toolInstance: ToolInstance. Cuando le pase este atributo, le pasare tambien uno del tipo recourse y apartir de los dos se gener� el tipo de modulo.
	 * @param instancedActivity: InstanceActivity
	 * @param modeActivity: 
	 * @return Contenedor: Nos devuelve el objeto contenedor con casi todos los atributos rellenos
	 */
	//TODO preguntar a Juan, para que se pasaba instancedActivity.
	public Contenedor getTypeMod(Resource resource,ToolInstance toolInstance,InstancedActivity instancedActivity,String modeActivity,Deploy lfDeploy){
		 Contenedor cont = new Contenedor();	
		 File directorio;	 
			 // Si viene tollinstace a null se que el modulo se esta generando apartir de un resource
			
		    if (resource!=null){
				//TipoModulo Resource
			
				if ((!resource.isInstantiable()) || 
						//cambiar external por acceso a variable estatica de LFModelBuilder INTERNAL_TOOL_KIND
						((resource.isInstantiable()) && (resource.getToolKind().equals(Resource.TOOL_KIND_EXTERNAL)))){
					//Relleno objeto contenedor
					cont.setTipoModulo("resource");
					directorio=new File(this.getBase()+resource.getLocation());
					
					//Para Obtener el subtipo de un modulo de tipo recurso
					if (directorio.isDirectory())
						cont.setSubTipoModulo("directory");	
					else
						cont.setSubTipoModulo("file");
					
					//Para saber si tengo que rellenar los parametros AllTest y Options
					if ((resource.isInstantiable()) && (resource.getToolKind().equals("external"))){
						cont.setAllTest("userusername=callerUser");
						//TODO PREGUNTAR A LUIS PABLO COMO IDENTIFICA EL google presentations o webcontent (para incompatibilidad con youtube)
						if ((!resource.getToolTypeNumber().equals(getGPresGMType())) && (!resource.getToolTypeNumber().equals(getGPres3GMType())) && (!resource.getToolTypeNumber().equals(getWebCGMType()))){			
							cont.setOptions("frame");	
						}else{
							//Incluimos contenido de la variable POPUP, para tratamiento de google Presentations
							cont.setPopUP("resizable=1,scrollbars=1,directories=1,location=1,menubar=1,toolbar=1,status=1,width=620,height=450");
						}
					}			
					
					//Se pone el nombre del recurso pero falta a�adir el del padre y el de su actividad
					if (toolInstance ==null) {
						cont.setModName(resource.getName());
						cont.setLocation(resource.getLocation().toString());
					}
					else{
						cont.setModName(toolInstance.getName());
						if (toolInstance.getLocation() !=null)
							//Modification to make access to external tools through GM (false) or GLUEPS (true)
							if(!isMoodleRealTimeGlueps()) cont.setLocation(toolInstance.getLocationWithRedirects(lfDeploy).toString());
							else cont.setLocation(toolInstance.getId());
						else
							cont.setLocation("");
					}
									
					//cont.setGroupingId(instancedActivity.getGroupId());	
					ArrayList pru=lfDeploy.getGroups();
					String groupingId = ((Group) this.getObjectArray(instancedActivity.getGroupId(),pru,3)).getId();
					BigInteger gid = null;
					try {//if the group id is a number, we set it to n+1 (min 0). If it is not a number, we set the grouping id to the hashcode of the string id+1
						gid = new BigInteger(groupingId);
						gid = new BigInteger(""+(gid.longValue()+1));
						groupingId = gid.toString();
					} catch (Exception e) {//The ID is not a number, we set it to the string id hashcode (absolute value, first 9 digits to prevent db overflow)
	        			int absolute = Math.abs((""+groupingId).hashCode());
	        			int digits = (absolute%1000000000)+1;
	        			groupingId = ""+digits;
					}
					cont.setGroupingId(groupingId);		
				}
				else
				{
				 //Para el resto de modulos el tipo viene dado por lo que venga en el atributo toolType de resource	
					
					String type = resource.getToolType();
					if(type.startsWith("http://") && type.contains("/tools/")) type = type.substring(type.lastIndexOf("/tools/")+7); //if it is a URL, we get only the type in the last part 
					
					cont.setTipoModulo(type);
					//Para el caso especial de quiz, por que el nombre no coincide con el que pone moodle 
					//cambiar external por acceso a variable estatica de LFModelBuilder INTERNAL_TYPE_FORUM
					if (type.equals("questionnaire"))
						cont.setTipoModulo("quiz");
					
					//Com�n para todos los modulos independientemente del tipo		
					//Se pone el nombre del recurso pero falta a�adir el del padre y el de su actividad
					if (toolInstance ==null) {
						cont.setModName(resource.getName());
						cont.setLocation(resource.getLocation().toString());
					}
					else{
						cont.setModName(toolInstance.getName());
						if (toolInstance.getLocation() !=null){
							//Modification to make access to external tools through GM (false) or GLUEPS (true)
							if(!isMoodleRealTimeGlueps()) cont.setLocation(toolInstance.getLocationWithRedirects(lfDeploy).toString());
							else cont.setLocation(toolInstance.getId());
						}

					}
									
					//cont.setGroupingId(instancedActivity.getGroupId());	
					ArrayList pru=lfDeploy.getGroups();
					//cont.setGroupingId((String) this.getObjectArray(instancedActivity.getGroupId(),pru,3));
					String groupingId = ((Group) this.getObjectArray(instancedActivity.getGroupId(),pru,3)).getId();
					BigInteger gid = null;
					try {//if the group id is not a number, we set the grouping id to the hashcode of the string id
						gid = new BigInteger(groupingId);
					} catch (Exception e) {//The ID is not a number, we set it to the string id hashcode (absolute value, first 9 digits to prevent db overflow)
	        			int absolute = Math.abs((""+groupingId).hashCode());
	        			int digits = absolute%1000000000;
	        			groupingId = ""+digits;
					}
					cont.setGroupingId(groupingId);
					//para el caso especial de la wiki
					//INDIVIDUAL_MODE
					if (modeActivity!=null && modeActivity.equals("individual"))
						cont.setSubTipoModulo("student");
					else
						cont.setSubTipoModulo("group");					
				}
		    }else{
		    	//Parea modulo que no tenga recursos asociados
		    	cont.setTipoModulo("text");	
		    	ArrayList pru=lfDeploy.getGroups();
		    	//cont.setGroupingId((String) this.getObjectArray(instancedActivity.getGroupId(),pru,3));
				String groupingId = ((Group) this.getObjectArray(instancedActivity.getGroupId(),pru,3)).getId();
				BigInteger gid = null;
				try {//if the group id is not a number, we set the grouping id to the hashcode of the string id
					gid = new BigInteger(groupingId);
				} catch (Exception e) {//The ID is not a number, we set it to the string id hashcode (absolute value, first 9 digits to prevent db overflow)
        			int absolute = Math.abs((""+groupingId).hashCode());
        			int digits = absolute%1000000000;
        			groupingId = ""+digits;
				}
				cont.setGroupingId(groupingId);		
		    }
		return cont;
	}
	/**
	 * <p>Funci�n encargada de proprocionar el n�mero de secci�n a la que pertenece un modulo</p>
	 * @param id : Identficador del modulo padre, para saber el n�mero de secci�n del hijo
	 * @param map
	 * @return String: El n�mero de secci�n que le corresponde a la  actividad que se ha enviado en idFather
	 */
	public String getSectionActivity(String idFather, HashMap map){
		ArrayList <Contenedor> list;
		String numSec ="0";
		Iterator it = map.keySet().iterator(); 
		while (it.hasNext()) {
	            // Get Clave
				Activity act;
	            String clave = (String) it.next();
	            list = (ArrayList <Contenedor>) ((ArrayList)map.get(clave)).get(1);
	            for(int i=0; i<list.size();i++ ){	      
	            	//act=((ArrayList<Activity>)((ArrayList) map.get(clave)).get(0)).get(i);
	            	if (idFather.equals(list.get(i).getIdActivity())){
	            		numSec=String.valueOf(list.get(i).getNumSection());  
	            		break;
	            	}
	            }
	            if (!numSec.equals("0"))
            		break;
	        }
		
		
		return numSec;
	}
	/**
	 * <p>Funci�n encarga de generar el nombre para el padre de la actividad actual.</p>
	 * @param id :String Identificador del padre de la actividad actual, para el cual se va formar el nombre
	 * @param activity:Activity. Devuelve un String con el nombre del modulo
	 * @return String: Con el nombre de la actividad que se ha pasado por id.
	 **/
	public String getNameModule (Activity activity, String id){		

		ArrayList lista = getAllActivitys(activity);
		ArrayList <Activity> total= (ArrayList) lista.get(0);
		ArrayList listSecciones = (ArrayList) lista.get(1);
		String numSec="0";
		String nameR="";
		String numSectotal;
		Activity activ;
		numSec=String.valueOf(id);
		while (!listSecciones.contains(numSec) ){
			for (int i=0; i<total.size(); i++){
				if (total.get(i).getId().equals(String.valueOf(numSec))){
					nameR=nameR+total.get(i).getName();
					if (total.get(i).getParentActivityId()!=null)
						numSec=total.get(i).getParentActivityId();
					else
						numSec=listSecciones.get(0).toString();
					break;
				}				
			}
			
		}
		return nameR;
	}
	/**
	 * <p>Funci�n encarga de generar el nombre de la secci�n</p>
	 * @param id :Integer Indica el n�mero de la secci�n de la cual se tiene que generar el nombre
	 * @param activity:Activity. Actividad ra�z del �rbol
	 * @return String: Con el nombre de la secci�n que se le ha pasado por id.
	 **/
	public String getNameSection (Activity activity, int id){		

		ArrayList lista = getAllActivitys(activity);
		ArrayList <Activity> total= (ArrayList) lista.get(0);
		ArrayList listSecciones = (ArrayList) lista.get(1);
		String nameS="";
		String numSectotal;
		String sectionDescription="";
		String style;
		String mensajeFinal;
		Activity fatherActivity=total.get(0);
		String idFather=listSecciones.get(id).toString();
			while (idFather!=null){
				for (int i=0; i<total.size(); i++){
					if (total.get(i).getId().equals(idFather)){
						//Para quedarme con la descripci�n del modulo que crea secci�n
						if ((nameS.equals("")) && (total.get(i).getDescription()!=null))
							sectionDescription=total.get(i).getDescription();
						
						if (!nameS.equals(""))
							nameS=total.get(i).getName()+" - "+nameS;
						else
							nameS=total.get(i).getName();
						
						idFather=total.get(i).getParentActivityId();
					}
					//Para que no continue si ya hemos llegado al padre
					if (idFather==null)
						break;				
				}			
			}
			 style="font-weight: bold;";
			 mensajeFinal="<span style=\""+style+"\">"+nameS+"</span><br /> "+sectionDescription;
		return  mensajeFinal;
	}
	
	/*****
	 * <p>
	 * 		Funci�n encarga de obtener todas las actividades del arbol
	 * </p>
	 * 
	 * @param activity: Es el nodo ra�z del �rbol
	 * @return Array con dos arrays:
	 * 			En la posicion(0): Contiene un array con todas las actividades del �rbol
	 * 			En la posici�n(1): Contiene un array con los id's de las acvidades que crean secci�n
	 */
	public ArrayList getAllActivitys(Activity activity) {

		/*
		 * Array en el que guardamos dos arrays. - El primero contendra todas
		 * las actividades - El segundo los id's de las actividades que generan
		 * secci�n
		 */
		ArrayList activityList = new ArrayList();
		// Para almacenar la actividad raiz
		Activity rootActivity = activity;
		// Array en el que se van almacenando las actividades de cada nivel del
		// �rbol de actividades
		ArrayList<Activity> listChildren = new ArrayList<Activity>();
		/*
		 * Array donde vamos a almacenar todas las actividades de los diferentes
		 * niveles del arbol desde nodo ra�z hasta las actividades hoja.
		 */
		ArrayList<Activity> hijas = new ArrayList<Activity>();

		// ArrayList <String> listAux;
		/*
		 * Array donde vamos a almacenar todas las actividades de los diferentes
		 * niveles del arbol desde nodo ra�z hasta las actividades hoja.
		 */
		ArrayList<Activity> activitys = new ArrayList<Activity>();
		//Almaceno el Id de las actividades que gener�n secci�n.
		ArrayList idSectionsListAct = new ArrayList();
		//Como primer nodo a procesar a�ado el nodo ra�z
		listChildren.add(rootActivity);
		//Para indicar el n�mero de secciones que se crean
		int numSection = 0;
		//Variable que indica cuando esta a false que no se pueden crean m�s secciones y con true lo contrario
		boolean createSection = true;
		//A�ado el nodo raiz al array que va almacenar todas las actividades.
		activitys.add(rootActivity);
		//Compruebo si el nodo raiz tiene asociado un array con recursos 
		if ((rootActivity.getResourceIds() != null)
				&& (rootActivity.getResourceIds().size() >= 1)) {
			//Si es cierto, entonces creo la primera secci�n.
			numSection = 1;
			idSectionsListAct.add(String.valueOf(numSection));
		}

		/*
		 * Recorro el �rbol desde el nodo ra�z hasta los nodos hoja
		 */
		while (listChildren.size() != 0) {
			hijas.clear();
			for (int j = 0; j < listChildren.size(); j++) {

				// Para localizar la vez que se tienen que crear las secciones
				if ((listChildren.size() > 1) && (createSection)) {
					numSection++;
					idSectionsListAct.add(listChildren.get(j).getId());
				} else {
					// Para el caso especial en que nodo tenga un solo hijo y la
					// secci�n se cree en el segundo o sucesivo
					if ((numSection == 0)
							&& (listChildren.get(j).getResourceIds() != null)
							&& (listChildren.get(j).getResourceIds().size() >= 1)) {
						numSection = 1;
						idSectionsListAct.add(String.valueOf(numSection));
					}
				}
				/*
				 * Voy a�adiendo a hijas las hijas de las actividades del nivel anterior del arbol
				 */
				if (((Activity) listChildren.get(j)).getChildrenActivities() != null) {
					for (int h = 0; h < ((Activity) listChildren.get(j))
							.getChildrenActivities().size(); h++) {
						hijas.add(((Activity) listChildren.get(j))
								.getChildrenActivities().get(h));
						//Voy acumulando todas las actividades en el array activitys
						activitys.add(((Activity) listChildren.get(j))
								.getChildrenActivities().get(h));
					}
				}
			}
			// Para evitar que genere nuevas secciones
			if (numSection >= 2)
				createSection = false;
			//Una vez procesado las actividades de un nivel, limpio la lista
			listChildren.clear();
			/*Y le asigno las hijas del las actividades del nivel procesado
			Me toca clonar la lista de las hijas, como luego la borro para preparla para almacenar
			las hijas de las siguientes actividades que voy a procesar
			Si no la clono, me desapareceria la informaci�n de listChildren tambien
			*/
			listChildren = (ArrayList<Activity>) hijas.clone();
		}
		//Asigno los arrays de actividades y de id con actividades que creo secci�n para ser devueltos por la funci�n.
		activityList.add(activitys);
		activityList.add(idSectionsListAct);
		return activityList;
	}
	
	/***
	 * <p>Encarga de buscar objetos dentro de una lista</p>
	 * @param id : De tipo String. Y representa el id del objeto que estamos buscando
	 * @param list<Object>: Contiene un array de objetos
	 * @param ind: Se contemplan dos posibles valores:
	 * 		- Si contiene 1, nos indica que los objetos que contiene el ArrayList, son de tipo Resource
	 * 		- Si contiene 2, nos indica que los objetos que contiene el Arraylist, son de tipo ToolInstance
	 * @return Object: Devuelve un instancia tipo object que podra ser de tipo Resource o ToolInstance y contiene la instancia
	 * 		   que coincide con el id que se ha puesto en id
	 */
	public Object getObjectArray(String id, ArrayList <Object> list,int ind){
		Object ObjectSought = null;
		//		
			for(int i=0;i<list.size();i++){
				if (ind==1){//Para buscar en un array de recurso
					if (id.equals(((Resource)list.get(i)).getId())){
						ObjectSought =list.get(i);
						break;
					}
				}else {
					 if (ind==2){
					//Para buscar en un array de ToolInstance
						if (id.equals(((ToolInstance)list.get(i)).getId())){
							ObjectSought =list.get(i);
							break;
						}
					 }else{
						 if (id.equals(((Group)list.get(i)).getId())){
								//ObjectSought =String.valueOf(i+1);BUGGY! we should return the object, not the index!!
							 	ObjectSought = list.get(i);
								break;
							}
						 
						 
					 }
				}	
			}		
				
		return ObjectSought;		
	}
	
	public String getBase() {
		return BASE;
	}
	public void setBase(String base) {
		this.BASE = base;
	}

	@Override
	public HashMap<String, String> getInternalTools() {
		// TODO Right now the tools are hardwired. Someday, we can ask the installation through an API
		HashMap<String, String> tools = new HashMap<String, String>();
		
		tools.put("chat", "Chat (Moodle)");
		tools.put("forum", "Forum (Moodle)");
		tools.put("questionnaire", "Quiz (Moodle)");
		tools.put("assignment", "Assignment (Moodle)");		
		//tools.put("quiz", "Quiz (Moodle)");
		//tools.put("directory", "Directory (Moodle)");
		//tools.put("file", "File (Moodle)");
		
		return tools;
	}


	public String getToolConfiguration(String localToolId) {
		// TODO Later on, each tool can have different configuration. For now, all use the default xforms
		String configuration;
		
		//We read the configuration file
		File configFile = new File(templateDir+File.separator+DEFAULT_CONFIG_FILENAME);
		
		if(configFile.exists()){
			try {
				configuration = FileUtils.readFileToString(configFile, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			
		}else return null;
		
		return configuration;
	}


	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}


	public HashMap<String, Participant> getCourseUsers(String moodleBaseUri,
			String courseId) {
		String qapiUrl = moodleBaseUri+"qapi/rest.php/course/userlist/"+courseId;

		HashMap<String,Participant> users = null;

		try {
			String response = doGetFromURL(qapiUrl);
			//System.out.println("VLE returned: "+jsonString);
			JSONObject object = new JSONObject(response);

			//JSONObject object = (new JsonRepresentation(rep)).getJsonObject();
			if (object != null) {
				String[] keys = JSONObject.getNames(object);
				for (String key : keys) {
					JSONArray fields = object.getJSONArray(key);
					if (fields != null && fields.length() == 9) {
						if (users == null)
							users = new HashMap<String, Participant>();
						//Staff roles in moodle are: teacher, editingteacher, admin, coursecreator
						boolean staff = (fields.getString(8).equals("editingteacher")||fields.getString(8).equals("admin")||fields.getString(8).equals("teacher")||fields.getString(8).equals("coursecreator"));
						//In Moodle, LEData is <userid>;<username>;<email>;<firstaccess>;
						String leData = fields.getString(0)+Participant.USER_PARAMETER_SEPARATOR+fields.getString(1)+Participant.USER_PARAMETER_SEPARATOR+fields.getString(3)+Participant.USER_PARAMETER_SEPARATOR+fields.getString(4)+Participant.USER_PARAMETER_SEPARATOR;
						Participant p = new Participant(fields.getString(0), fields.getString(1), null, leData, staff);
						System.out.println("Retrieved participant: "+p.toString());
						
						//Before putting the participant, we check that we are not downgrading teachers to students
						if(users.get(fields.getString(0))!=null && !p.isStaff());//if we are introducing a student but the user already exists, there is no need to introduce it
						else users.put(p.getName(), p);
					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
//		// Method 1: Simple parsing/object
//		ClientResource usersResource = new ClientResource(qapiUrl);
//		
//		try{
//			System.out.println("Trying URL: "+qapiUrl);
//			Representation rep = (Representation) usersResource.get();
//			if (usersResource.getStatus().isSuccess()) {
//				
//				try {
//					String jsonString = rep.getText();
//					System.out.println("VLE returned: "+jsonString);
//					JSONObject object = new JSONObject(jsonString);
//
//					//JSONObject object = (new JsonRepresentation(rep)).getJsonObject();
//					if (object != null) {
//						String[] keys = JSONObject.getNames(object);
//						for (String key : keys) {
//							JSONArray fields = object.getJSONArray(key);
//							if (fields != null && fields.length() == 9) {
//								if (users == null)
//									users = new HashMap<String, Participant>();
//								//Staff roles in moodle are: teacher, editingteacher, admin, coursecreator
//								boolean staff = (fields.getString(8).equals("editingteacher")||fields.getString(8).equals("admin")||fields.getString(8).equals("teacher")||fields.getString(8).equals("coursecreator"));
//								//In Moodle, LEData is <userid>;<username>;<email>;<firstaccess>;
//								String leData = fields.getString(0)+Participant.USER_PARAMETER_SEPARATOR+fields.getString(1)+Participant.USER_PARAMETER_SEPARATOR+fields.getString(3)+Participant.USER_PARAMETER_SEPARATOR+fields.getString(4)+Participant.USER_PARAMETER_SEPARATOR;
//								Participant p = new Participant(fields.getString(0), fields.getString(1), null, leData, staff);
//								System.out.println("Retrieved participant: "+p.toString());
//								
//								//Before putting the participant, we check that we are not downgrading teachers to students
//								if(users.get(fields.getString(0))!=null && !p.isStaff());//if we are introducing a student but the user already exists, there is no need to introduce it
//								else users.put(fields.getString(0), p);
//							}
//						}
//
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					throw new ResourceException(e);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					throw new ResourceException(e);
//				}
//
//			}
//		
//		} catch (ResourceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} finally {
////			Representation entity = usersResource.getResponseEntity(); 
////			  if (entity != null) entity.release(); 
////			usersResource.release();
//			
//			//New code equivalent mimicking GLUEletManager changes for connection stability
//			discardRepresentation(usersResource.getResponseEntity());    /// **** LA CLAVE: "VOLVER A" EXTRAER LA ENTIDAD RESPUESTA DEL 
//																		///CLIENTRESOURCE; si se llega aqu� es desde la ejecuci�n de .get(), 
//																		///por lo que la variable 'response' NO SE HA ASIGNADO
//		}	
		
		System.out.println("The users are: "+users.toString());

		return users;

	}


	
	protected void discardRepresentation(Representation rep) {
        if (rep != null) {
            try {
                rep.exhaust();    // this call is explicitly required to avoid problems when using the production-recommended Apache HTTP Client connector
                                            //    http://wiki.restlet.org/docs_2.0/13-restlet/28-restlet/75-restlet.html
                                            //   http://www.restlet.org/documentation/2.0/jse/api/org/restlet/representation/Representation.html#release()
            } catch(IOException io) {
                System.out.println("Secondary exception while exhausting remote response (world sometimes is ugly): "+io.getMessage());
            }
            rep.release();
        }
    }
	
	//TODO HttpClient reuses connections. This way of doing things is wasteful, to be optimized!!
	protected String doGetFromURL(String url) throws Exception{
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = null;
		HttpEntity entity = null;
		try {
			System.out.println((new Date()).toString()+" - Trying to GET "+url);
			
			response = httpclient.execute(httpget);
			
			int rc = response.getStatusLine().getStatusCode();

			entity = response.getEntity();
     
			if (rc != 200) {
				throw new Exception("GET unsuccessful. Returned code "+rc);
			}
				
			if (entity != null) {

				String content = EntityUtils.toString(entity, "UTF-8");
				System.out.println((new Date()).toString()+" - Got response from server: "+content);

				return content;
			} else throw new Exception("GET unsuccessful. Null entity!");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			throw e;
		} finally {
			if(entity!=null){
				try {
					EntityUtils.consume(entity);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(httpget!=null) httpget.abort();
			if(httpclient!=null) httpclient.getConnectionManager().shutdown();
		}
		return null;

		
		
	}


	@Override
	public HashMap<String, Group> getCourseGroups(String baseUri,
			String courseId) {
		
		String qapiUrl = baseUri+"qapi/rest.php/course/grouplist/"+courseId;

		HashMap<String,Group> groups = null;

		try {
			String response = doGetFromURL(qapiUrl);
			//System.out.println("VLE returned: "+jsonString);
			
			//Returned information should be: [groupid, groupname, groupmemberid, userid, username, firstname lastname, email, firstaccess, auth, course name, courseid]
			JSONObject object = new JSONObject(response);

			//JSONObject object = (new JsonRepresentation(rep)).getJsonObject();
			if (object != null) {
				String[] keys = JSONObject.getNames(object);
				for (String key : keys) {
					JSONArray fields = object.getJSONArray(key);
					if (fields != null && fields.length() == 11) {
						if (groups == null)
							groups = new HashMap<String, Group>();
						
						String groupid = fields.getString(0);
						String participantid = fields.getString(3);
						
						Group group = null;
						if((group = groups.get(groupid))==null){//The group does not exist, we create it with this user
							ArrayList<String> participants = new ArrayList<String>();
							participants.add(participantid);
							group = new Group(groupid, fields.getString(1), null, participants);
						}else{//The group already exists, we check that the participant does not already exist, and we add it
							ArrayList<String> participants = group.getParticipantIds();
							if(!participants.contains(participantid)){
								participants.add(participantid);
								group.setParticipantIds(participants);
							}
						}
						groups.put(groupid, group);
						//System.out.println("Retrieved group: "+group.toString());
						
					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		System.out.println("The groups are: "+groups.toString());

		return groups;
		
	}
	
    protected boolean moodleAuth(String baseUri, String user, String pass){	
		String qapiUrl = baseUri+"qapi/rest.php/course/credentials/"+user+"/"+pass;
		Boolean auth = false;

		try {
			String response = doGetFromURL(qapiUrl);
						
			if (!response.equals("[]")) {
				JSONObject object = new JSONObject(response);
				String[] keys = JSONObject.getNames(object);
				for (String key : keys) {
					JSONArray fields = object.getJSONArray(key);
					if (fields != null && fields.length() == 5) {
						auth = true;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    	return auth;
    }
    
    protected String getStartingSectionField(){
 	   return parameters.get("startingSection");
    }
    
    protected String getAppExternalUri(){
 	   return parameters.get("appExternalUri");
    }
    
    protected Boolean getLdShakeMode(){
 	   if (parameters.get("ldshakeMode")!= null && String.valueOf("ldshakeMode").equals("true")){
 		   return true;
 	   }else{
 		   return false;
 	   }
    }
    
    protected String getGPresGMType(){
 	   return parameters.get("gpresGmType");
    }
    
    protected String getGPres3GMType(){
 	   return parameters.get("gpres3GmType");
    }
 	
    protected String getWebCGMType(){
  	   return parameters.get("webcGmType");
     }    
    
    protected Boolean isMoodleRealTimeGlueps(){
 	   if (parameters.get("moodleRealTimeGlueps")!=null && String.valueOf("moodleRealTimeGlueps").equals("true")){
 		   return true;
 	   }
 	   else{
 		   return false;
 	   }
    }
}
