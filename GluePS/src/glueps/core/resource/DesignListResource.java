package glueps.core.resource;

import glue.common.format.FormatStatic;
import glue.common.format.FormattedEntry;
import glue.common.format.FormattedFeed;
import glue.common.resources.GLUEResource;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileUtils;
import org.restlet.Request;
import org.restlet.data.CharacterSet;
import org.restlet.data.Disposition;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;

import org.restlet.data.Status;
import org.restlet.engine.http.connector.ConnectedRequest;
import org.restlet.engine.http.connector.ServerConnection;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import org.restlet.resource.Get;
import org.restlet.util.Series;
import org.xmldb.api.base.Collection;

import com.google.gson.Gson;



import glueps.adaptors.ld.ILDAdaptor;
import glueps.adaptors.ld.LDAdaptorFactory;
import glueps.adaptors.ld.imsld.IMSLDAdaptor;
import glueps.adaptors.vle.VLEAdaptorFactory;
import glueps.core.controllers.exist.Exist;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.model.LearningEnvironmentInstallation;
import glueps.core.persistence.JpaManager;
import glueps.core.persistence.entities.DesignEntity;
import glueps.core.persistence.entities.UserEntity;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Resource 'list of tools' (tool implementations, indeed).
 * 
 * List of all the registered tool implementations available to create Gluelets. 
 * 
 * @author	 	Juan Carlos A. <jcalvarezgsic@gsic.uva.es>
 * @version 	2010042000
 * @package 	glue.core.resources
 */

public class DesignListResource extends GLUEPSResource {
	
	private static final Object NEW_DESIGN_FILE_FIELD = "NewDImportDesign";
	private static final Object NEW_DESIGN_TITLE_FIELD = "NewDTitle";
	private static final Object NEW_DESIGN_TYPE_FIELD = "imsldType";
	
	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");


	/**
	 * GET DesignListResource
	 * 
	 * 
	 * @return	'Atomized' list of Designs known by GLUEpsManager
	 */
	//@Get("atom")
	@Get("xml|html")
    public Representation getDesigns() {
    	
   		logger.info("** GET DESIGNS received");
    	
   		FormattedFeed feed = new FormattedFeed();
		String uri = getReference().getIdentifier();
		
		uri = doGluepsUriSubstitution(uri);
		
		// Atom standard elements
		feed.setId(uri);  
		feed.setTitle("List of designs (design)");
		feed.addAuthor(FormatStatic.GSIC_NAME, null, null);
		feed.setSelfLink(null, uri);
		
		String login = this.getRequest().getChallengeResponse().getIdentifier();
		JpaManager dbmanager = JpaManager.getInstance();
		try {
			List<Design> designs = dbmanager.listDesignObjectsByUser(login);
			for(Design design : designs){
				DesignResource designItem = new DesignResource();
				designItem.setDesign(design.getId(), getReference().toString());
				feed.getEntries().add(designItem.getDesignEntry());
			}
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Internal error while trying to retrieve deploys from DB", e);
		}
		
		
		Representation answer = feed.getRepresentation();
		
		try {
			logger.info("** GET DESIGNS answer: \n" + (answer != null ? answer.getText() : "NULL"));
		} catch (IOException io) {
			throw new RuntimeException("Extremely weird failure while trying to log", io);
		}
		return answer;
		
	}
    
	/**
	 * GET DesignListResource
	 * 
	 * 
	 * @return	JSON list of Designs known by GLUEpsManager
	 */
    @Get("json")
    public Representation getJsonDesigns() {
    	
   		logger.info("** GET JSON DESIGNS received");
   		Representation answer = null;
		
		String login = this.getRequest().getChallengeResponse().getIdentifier();		
		JpaManager dbmanager = JpaManager.getInstance();
		ArrayList<HashMap<String,String>> designList = new ArrayList<HashMap<String,String>>();
		UserEntity user = dbmanager.findUserByUsername(login);
		List<DesignEntity> designEntities = dbmanager.listDesignsByUser(user.getId());
		for (DesignEntity designEntity : designEntities) {
			HashMap<String,String> prop = getPropertiesJsonDesign(getReference().toString(), designEntity);
			designList.add(prop);
		}
				
		// convert java object to JSON format,
		// and returned as JSON formatted string
		Gson gson = new Gson();
		String json = gson.toJson(designList);
		
   		if (json != null){
   			answer = new StringRepresentation((CharSequence) json, MediaType.APPLICATION_JSON);
			answer.setCharacterSet(CharacterSet.UTF_8);
   		}
		
		logger.info("** GET JSON DESIGNS answer: \n" + (answer != null ? json : "NULL"));
		return answer;
		
	}    
    
    /**
     * Get the information of the design which is going to be shown in the list of designs in JSON format
     * @param feedReference The reference to the DesignListResource
     * @param designEntity The design whose information is going to be gotten
     * @return The information of the design as a HashMap<String, String> of pairs property,value
     */
	private HashMap<String,String> getPropertiesJsonDesign(String feedReference, DesignEntity designEntity) {
		HashMap<String,String> prop = new HashMap<String,String>();
		// / build URI
		String uri = null;
		uri = feedReference + "/" + designEntity.getId();
		uri = doGluepsUriSubstitution(uri);

		prop.put("id", uri);
		prop.put("name", designEntity.getName());
		prop.put("designtype", designEntity.getType());
		return prop;
	}
	
    
    /**
     * Update the database fields of every DesignEntity with the information contained in its Design xmlfile
     * @throws UnsupportedEncodingException 
     */
	@Put()
    public Representation updateDesignEntities(Representation entity) throws UnsupportedEncodingException{
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		if (app.getLetPutUpdateTables()){
			Representation answer = null;
	    	logger.info("** Starting updating glueps_designs table");
	    	JpaManager dbmanager = JpaManager.getInstance();
	    	List<DesignEntity> designs = dbmanager.listDesigns();
	    	if(designs!=null){
	    		for(DesignEntity de : designs){
	    			Design design = (Design) generateObject(new String(de.getXmlfile(),"UTF-8"), Design.class);
	    			de.setName(design.getName());
	    			de.setType(design.getOriginalDesignType());
	    			//Make the changes permanent
	    			dbmanager.insertDesign(de);
	    		}
	    	}
	    	logger.info("** Finished updating glueps_designs table");
	    	return answer;
	    }else{
	    	throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "updating glueps_designs table is not allowed");
	    }
		
    } 
    
//Apparently no one uses this anymore, we comment it out
//    /**
//	 * POST DesignListResource
//	 * 
//	 * Creation Design in Glueps. Variant that receives a zip with the design
//	 * 
//	 * Dispatch the configuration data to the proper LD adaptor in order to trigger the actual creation.
//	 *
//	 * 
//	 * @return	URI to new Design resource, or null.
//	 */
//	@Post("zip")
//	public Representation createDesign(Representation entity) {
//
//		
//		Design design;
//		String designId;
//		try {
//			// load Atom+GLUE formatted entity		
//			FormattedEntry inputEntry = null;
//			
//			design = null;
//			
//			//Set the upload and schemas paths
//			GLUEPSManagerApplication app = (GLUEPSManagerApplication)this.getApplication();
//			String UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";
//			String SCHEMA_LOCATION = app.getAppPath()+"/schemas/";
//
//			//We create the newly uploaded design folder where the zip and resources will be stored
//			String uploadDir = null;
//			String designUrl;
//			do{
//				designId = ""+(new Random()).hashCode();
//				designUrl = getReference().getIdentifier()+"/"+designId;
//				uploadDir = UPLOAD_DIRECTORY + designId + File.separator;
//			}while((new File(uploadDir)).exists());
//			(new File(uploadDir)).mkdirs();
//			
//			try {
//				//Reader reader= entity.getReader();
//				//BufferedReader br = new BufferedReader (reader);
//				//String texto = br.readLine();
//				InputStream entrada= entity.getStream();
//				try{
//					//Write the zip to the uploads folder
//						   File f=new File(uploadDir, designId+".zip");
//						   OutputStream salida=new FileOutputStream(f);
//						   byte[] buf =new byte[1024];
//						   int len;
//						   while((len=entrada.read(buf))>0){
//						      salida.write(buf,0,len);
//						   }
//						   salida.close();
//						   entrada.close();
//						   System.out.println("Upload successful");
//				  }catch(IOException e){
//				    System.out.println("There was an error: "+e.toString());
//				  }
//				  
//				  //TODO: detect the LD type, either from a parameter or by auto-detect
//				  
//				  //Received zip file is now on disk. We use the IMS-LD adaptor to get the Design
//				  IMSLDAdaptor adaptor = new IMSLDAdaptor(SCHEMA_LOCATION,uploadDir);
//				  design = adaptor.processUoL(uploadDir + designId+".zip");
//				  
//				  //TODO: set the title of the design with the name received from the request, not the one in IMSLD
//				  design.setId(designUrl);
//				  
//				  //We create the Design XML
//				  FileOutputStream fichero =new FileOutputStream(uploadDir + designId+".xml");
//				  JAXBContext context = JAXBContext.newInstance(Design.class);
//				  Marshaller m = context.createMarshaller();	
//				  //Esta Propiedad formatea el código del XML
//			      m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
//			      m.marshal(design, fichero);
//			      fichero.close();
//				  
//				  
//				 String entityText = entity.getText();
//				 inputEntry = new FormattedEntry(entityText);
//				logger.info("** POST DESIGN received: \n" + entityText);        	
//			} catch (IOException e) {
//				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Request couldn't be parsed", e);  
//			} catch (JAXBException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			//Guardo Xml en base de datos eXist poniendole como ID el número generado aleatoriamente		
//			Representation result = null;
//
//			//Get the DB parameters from the restlet application
//			Exist exist=new Exist(app.getDburi(), app.getDbuser(), app.getDbpassword());
//			
//			File xmlSource =new File(designId+".xml");
//			
//			try {
//				exist.setXML("Designs",xmlSource,designId+".xml" );
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			
//			//Elimino el archivo, una vez que lo he subido a la base de datos
//			xmlSource.delete();
//		} catch (ResourceException e) {
//			throw e;
//		} finally {
//            discardRepresentation(entity);        // fix to mimick GLUElet Manager corrections for connection stability
//		}
//		
//		
//		
//		//Creo el atom,donde voy a devolver la respuesta
//		//que en este caso sera la direcci�n del recurso que acabamos de crear.
//		String feedReference = getReference().toString();
//		FormattedFeed feed = new FormattedFeed();
//		String uri = getReference().getIdentifier();
//		
//		// Atom standard elements
//		feed.setId(uri);  
//		feed.setTitle("Created design (design)");
//		feed.addAuthor(FormatStatic.GSIC_NAME, null, null);
//		feed.setSelfLink(null, uri);
//		
//		FormattedEntry entry= new FormattedEntry();
//	
//      /*   Reference ref = getReference();
//         if(ref!=null)
//         	uri = ref.getIdentifier();
//         else*/
//         	uri =feedReference+"/" + designId+".xml";
//         java.util.Date fecha = new Date();
//         /// Atom standard elements
//         entry.setId(uri);
//         entry.setTitle(design.getName());
//         entry.setUpdated(fecha);
//         entry.setAlternateLink("Description of Design "+designId+".xml",uri);
//         entry.addAuthor(FormatStatic.GSIC_NAME, null,null);
//         entry.setRelatedLink("Provider",uri);
//  
//        	
//         //Glueps specific elements
//         entry.addExtendedTextChild("url",uri);
//		
//		feed.getEntries().add(entry);
//		
//		Representation answer = feed.getRepresentation();
//		return answer;			
//	}


    /**
	 * POST DesignListResource
	 * 
	 * Creation Design in Glueps. Variant that receives a multipart form
	 * 
	 * Dispatch the configuration data to the proper LD adaptor in order to trigger the actual creation.
	 *
	 * 
	 * @return	URI to new Design resource, or null.
	 */
	@Post("multipart")
	public Representation createDesignForm(Representation entity) {
		
		String login = this.getRequest().getChallengeResponse().getIdentifier();
 
		
		// load Atom+GLUE formatted entity		
		FormattedEntry inputEntry = null;
		String uploadDir = null;
		String designId = null;
		String designUrl = null;
		
		String designTitle = null;
		String designType = null;
		
		Design design = null;
		
		//This will contain the LF design xml
		String designData = null;
		
		if (entity != null) {

			try {
				logger.info("** POST DESIGN received: \n" + entity.toString());

				if (MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(),
				        true)) {

					//Set the upload and schemas paths
					GLUEPSManagerApplication app = (GLUEPSManagerApplication)this.getApplication();
					String UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";

				    // The Apache FileUpload project parses HTTP requests which
				    // conform to RFC 1867, "Form-based File Upload in HTML". That
				    // is, if an HTTP request is submitted using the POST method,
				    // and with a content type of "multipart/form-data", then
				    // FileUpload can parse that request, and get all uploaded files
				    // as FileItem.

					boolean found = false;
					File file = null;

					try {
						// 1/ Create a factory for disk-based file items
						DiskFileItemFactory factory = new DiskFileItemFactory();
						factory.setSizeThreshold(1000240);

						// 2/ Create a new file upload handler based on the Restlet
						// FileUpload extension that will parse Restlet requests and
						// generates FileItems.
						RestletFileUpload upload = new RestletFileUpload(factory);
						List<FileItem> items;

						// 3/ Request is parsed by the handler which generates a
						// list of FileItems
						items = upload.parseRequest(getRequest());

						
						// Process only the uploaded item called "fileToUpload" and
						// save it on disk
						for (final Iterator<FileItem> it = items.iterator(); it
								.hasNext();) {
							FileItem fi = it.next();
							if (fi.getFieldName().equals(NEW_DESIGN_FILE_FIELD)) {
								found = true;

								// We create the newly uploaded design folder where
								// the design and resources will be stored
								do {
									designId = "" + (new Random()).hashCode();
									designUrl = getReference().getIdentifier()+"/"+designId;
									uploadDir = UPLOAD_DIRECTORY + designId
											+ File.separator;
								} while ((new File(uploadDir)).exists());
								(new File(uploadDir)).mkdirs();

								//File file = new File(uploadDir, designId + ".zip");
								//File with no extension, non-IMSLD-centric
								file = new File(uploadDir, trimPath(fi.getName()));
								fi.write(file);
							}else if(fi.getFieldName().equals(NEW_DESIGN_TITLE_FIELD)){
								designTitle=fi.getString("UTF-8");
							}else if(fi.getFieldName().equals(NEW_DESIGN_TYPE_FIELD)){
								designType=fi.getString("UTF-8");
							}
							
							
						}
					} catch (FileUploadException e1) {

						e1.printStackTrace();
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Request could not be parsed", e1);  

					} catch (Exception e) {
						
						e.printStackTrace();
						throw new ResourceException(Status.SERVER_ERROR_INSUFFICIENT_STORAGE, "Could not write the uploaded files", e);  

					}

				    // Once handled, the content of the uploaded file is sent
				    // back to the client.
					if (found) {
						// Create a new representation based on disk file.
						// The content is arbitrarily sent as plain text.
						// rep = new FileRepresentation(new File(fileName),
						// MediaType.TEXT_PLAIN, 0);

						// We continue with the processing

						// TODO: detect the LD type, either from a parameter or by
						// auto-detect

						
							LDAdaptorFactory factory = new LDAdaptorFactory(app);
							
							ILDAdaptor adaptor = factory.getLDAdaptor(designType, designId);
							
							design = adaptor.fromLDToLF(file.toString());
							//old version
							//design = adaptor.processUoL(uploadDir + designId + ".zip");
							if(design==null) throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Failed to parse the provided design");  

							// set the title of the design with the name received
							// from the request, not the one in IMSLD, and also set the id
							design.setName(designTitle);
							design.setAuthor(login);
							design.setId(designUrl);
							
							Design deurlifiedDesign = deURLifyDesign(design);
							
							//We store the deurlified design in DB
							JpaManager dbmanager = JpaManager.getInstance();
							try {
								dbmanager.insertDesign(deurlifiedDesign);
							} catch (Exception e) {
								throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert the design in DB", e);
							}
							

							Representation result = null;

							designData = generateXML(URLifyDesign(deurlifiedDesign, null, getReference().getParentRef().getIdentifier()), glueps.core.model.Design.class);
							
							// Everything went OK

							Representation answer = new StringRepresentation((CharSequence) designData, MediaType.TEXT_XML);
							answer.setCharacterSet(CharacterSet.UTF_8);
						
							return answer;		

					} else {
				        // Some problem occurred, the request probably was not valid.
				        //rep = new StringRepresentation("no file uploaded",
				        //        MediaType.TEXT_PLAIN);
				        //setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "No file was uploaded");  

				    }
				}else{
				    // POST request with a different content type (this should not happen).
				    //setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
					throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Incorrect content type. Should be multipart/form-data");  

				}
			} catch (ResourceException e) {
				throw e;
			} finally {
	            discardRepresentation(entity);        // fix to mimick GLUElet Manager corrections for connection stability
			}
			
        } else {
            // POST request with no entity.
            //setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Incorrect request");  

        }


		
	}//end of CreateDesignForm


}
