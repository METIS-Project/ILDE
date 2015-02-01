package glueps.core.resource;

import glue.common.format.FormatStatic;
import glue.common.format.FormattedEntry;
import glue.common.format.FormattedFeed;
import glue.common.resources.GLUEResource;

import org.apache.commons.io.FileUtils;
import org.exist.examples.soap.GetDocument;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.data.CharacterSet;
import org.restlet.data.Disposition;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;

import org.restlet.data.Status;
import org.restlet.engine.http.connector.ConnectedRequest;
import org.restlet.engine.http.connector.ServerConnection;
import org.restlet.ext.xml.XmlRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

import org.restlet.resource.Get;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;

import glueps.adaptors.ld.imsld.IMSLDAdaptor;
import glueps.core.controllers.exist.Exist;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.model.Participant;
import glueps.core.model.Resource;
import glueps.core.model.ToolInstance;
import glueps.core.persistence.JpaManager;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.mail.internet.NewsAddress;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Resource 'list of tool instances'
 * 
 * List of all the registered tool instances in a deploy
 * 
 * @author	 	David A. Velasco <davivel@gsic.uva.es>
 * @original	juaase, davivel
 * @version 	20110906
 * @package 	glueps.core.resource
 */

public class ToolInstanceListResource extends GLUEPSResource {
	
	private static final String MW_TITLE_SEPARATOR = " - ";

	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");

	private HashMap<String,String> GMConfigCache = null;
	

    /**
	 * POST ToolInstanceListResource - Batch version
	 * 
	 * Creation of a deploy's tool instances in Glueps (and gluelet manager). Variant that receives an xml with the deploy, including tool instance info
	 * 
	 * Dispatch the configuration data to the Gluelet Manager in order to trigger the actual creation.
	 *
	 * 
	 * @return	XML with the deploy, including the updated location of the created tool instances.
	 */
	@Post("xml")
	public Representation createToolInstances(Representation entity) {

		
		try {
			String deployId = trimId((String) this.getRequest().getAttributes().get("deployId"));

			//We get the deploy from db, to check the author
			JpaManager dbmanager = JpaManager.getInstance();
			Deploy deploy = dbmanager.findDeployObjectById(deployId);
			
			//Default authorization: if the caller is not the owner, we do not allow actions on the resource
			String login = this.getRequest().getChallengeResponse().getIdentifier();
			if(!deploy.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the deploy");
	 
			deploy = null;
			
			//Set the upload and schemas paths
			GLUEPSManagerApplication app = (GLUEPSManagerApplication)this.getApplication();
			String UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";
			String uploadDir = UPLOAD_DIRECTORY+"temp/";
			File f = null;
			
			
			//We do not modify the deploy, but rather try to get the info from the toolInstances and create them
			if(deployId!=null){
				//We get the xml that we have received, and write it to a temp file
				try{
					InputStream entrada= entity.getStream();
					//Write the xml to the uploads folder
				   f=new File(uploadDir, deployId+".xml."+(new Date()).getTime());
				   OutputStream salida=new FileOutputStream(f);
				   byte[] buf =new byte[1024];
				   int len;
				   while((len=entrada.read(buf))>0){
				      salida.write(buf,0,len);
				   }
				   salida.close();
				   entrada.close();
				   System.out.println("Upload successful");
				  }catch(IOException e){
				    System.out.println("There was an error : "+e.toString());
					throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unknown error uploading the file", e);  
				  }
				
					//We create the deploy object with the provided deploy XML
					try {
				        JAXBContext jc = JAXBContext.newInstance( glueps.core.model.Deploy.class );
				        Unmarshaller u = jc.createUnmarshaller();
				        deploy = (Deploy)u.unmarshal(new FileInputStream(f.getAbsolutePath()));
				        System.out.println( "\nThe unmarshalled objects are:\n" + deploy.toString());
					} catch (JAXBException e) {
					    System.out.println("There was an error : "+e.toString());
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Unknown error parsing the uploaded file", e);  
					} catch (FileNotFoundException e) {
					    System.out.println("There was an error : "+e.toString());
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Unknown error parsing the uploaded file", e);  
					}
				
				//we delete the temporary file
				f.delete();
				
				//we go through the tool instances, and call Gluelet Manager to create them
				//TODO: what to do if there was an error?? (partial creation of toolInstances) we continue, but we should notify the user
				deploy = this.createGMToolInstances(deploy);
			
				//We de-urlify and insert the updated deploy into the database
				try {
					Deploy deurlifiedDeploy = deURLifyDeploy(deploy);
					dbmanager.insertDeploy(deurlifiedDeploy);
					
					//deploy is supposedly already urlified
					String xmlfile = generateXML(deploy, glueps.core.model.Deploy.class);
					
					//We return the updated xml
					Representation result = null;
					
					result = new StringRepresentation((CharSequence) xmlfile, MediaType.TEXT_XML);
						
					result.setCharacterSet(CharacterSet.UTF_8);

					return result;
				} catch (Exception e2) {
					throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "There was an error while trying to return the updated deploy data");
				}
				

				


			}else{
			    System.out.println("There was an error, no deployId provided ");
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Bad request: no deployId provided");  
			}
		} catch (ResourceException e) {
			throw e;
		} finally {
            discardRepresentation(entity);        // fix to mimick GLUElet Manager corrections for connection stability
		}
		

	}


	
	private Deploy createGMToolInstances(Deploy deploy) {
		
		Deploy updatedDeploy = deploy;
		
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this
		.getApplication();
		
		if(updatedDeploy!=null){
			// TODO We get the tool instance list
			ArrayList<ToolInstance> toolInstances = updatedDeploy.getToolInstances();
			
			if(toolInstances!=null){
				
				ArrayList<ToolInstance> updatedInstances = new ArrayList<ToolInstance>();
				
				for(Iterator<ToolInstance> it = toolInstances.iterator();it.hasNext();){
					// For each element
					ToolInstance instance = it.next();
					
					String toolKind = deploy.getInstanceToolKind(instance.getId());
					//In case we get the whole URL as the tool type, we get only the last part (toolTypeID)
					String toolTypeURL = deploy.getInstanceToolType(instance.getId());
					String toolType = toolTypeURL.substring(toolTypeURL.lastIndexOf("/")+1);
					
					//We check that the instance is referenced in any of the instancedactivities. This check is probably unnecessary now that tool reuse is done by redirecting toolinstances to one another 
					if(deploy.getInstancedActivitiesForToolInstance(instance.getId())==null || deploy.getInstancedActivitiesForToolInstance(instance.getId()).size()==0){
						//If not, we do not add the ToolInstance to the updated array and continue
						System.out.println("The instance "+instance.getId()+" is not used by any instancedActivity. we delete it");
					}else{
						if(toolKind.equals(Resource.TOOL_KIND_EXTERNAL) && instance.getLocation()==null){//We check that it is external, and that it is not already created
							// if it is external, we look through all the instanced activities that reference it and add its users to the list of users for that instance
							String[] participants = deploy.getParticipantUsernamesForToolInstance(instance.getId());
							
							try{
								// We call to the GM asking for tool configuration, and update it with our title (the only modification for now)
								// We implement a cache so that the same tool type is not asked for repeatedly
								if(GMConfigCache==null) GMConfigCache=new HashMap<String, String>();
	
								String configData = GMConfigCache.get(toolType);
								if(configData==null){
									configData = this.getToolInstanceConfiguration(toolType);
									GMConfigCache.put(toolType, configData);
								}
								System.out.println("Trying to set title for toolType "+toolType+" to "+instance.getName());
								configData = this.setTitleInXForms(toolType, configData, instance.getName());
								
								// We do the creation in the GM, with retries in case it timed out
								int counter=0;
								String url=null;
								do{
									if(counter>0) System.out.println("Retrying for the "+counter+" time");
									//url = this.createToolInstanceGM(toolType, configData, deploy.getAuthor(), participants);
									url = this.createToolInstanceGM(toolType, configData, deploy.getStaffUsernames(), participants);
									counter++;
								}while(url==null && counter<=app.getGmConnRetries());
								
								
								// If it was successful, get the URL and update it in the tool instance
								// We take into account that the Moodle may see the GM at a different address than we did
								//if(app.getGmurl().equals(app.getGmurlinternal())){
								if(url.startsWith(app.getGmurl())){
									System.out.println("Successfully created instance. Updating location to "+url); 
									instance.setLocation(new URL(url));
								}else{
									String prefix = url.substring(0, url.lastIndexOf("GLUEletManager/")+15);
									String newURL = url.replace((CharSequence) prefix, (CharSequence) app.getGmurl());
									System.out.println("Successfully created instance. Updating (transformed) location to "+newURL); 
									instance.setLocation(new URL(newURL));
								}
							}catch (Exception e) {
								//There was some kind of error in the instance creation, we leave the location blank
							    System.out.println("There was an error in the creation of instance "+instance.getId()+": "+e.getStackTrace());
							}
								
							// In any case, we add the instance to the array
							updatedInstances.add(instance);
							
						}else if(toolKind.equals(Resource.TOOL_KIND_INTERNAL) || instance.getLocation()!=null){// if it is internal, or already created/reusing, we add it without modifying it
							updatedInstances.add(instance);
						
						}
					}					
				}
				//We update the tool instances in the Deploy
				updatedDeploy.setToolInstances(updatedInstances);
			}else{
				//There were no instances to create, we return the same object we received
				return updatedDeploy;
			}
			// TODO We update the Deploy with the new list of tool instances, and return it
			return updatedDeploy;
		} else return null;
		
	}


	private String createToolInstanceGM(String toolType, String configData,
			String[] teachers, String[] participants) throws Exception {

		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this
				.getApplication();

		
		System.out.println("trying to create the instance of type "+toolType+" at URL "+app.getGmurlinternal() + "instance");
		String response;
		try {
			response = doPostToUrl(app.getGmurlinternal() + "instance", buildStringRepresentation(toolType, configData, teachers, participants), "application/atom+xml");
			FormattedEntry answerEntry = new FormattedEntry(response);
			System.out.println("Created the instance "+ answerEntry.getId());
			System.out.println("The end-user URL is "+ answerEntry.getLinks().get(0));
			return answerEntry.getId();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

		
		
		
////		Component component = new Component();
////		Client client = new Client(Protocol.HTTP);
////		// Timeout 0 means that timeout is infinite
////		client.setConnectTimeout(0);
////		component.getClients().add(client);
////		try {
////			component.start();
////		} catch (Exception e1) {
////			// TODO Auto-generated catch block
////			e1.printStackTrace();
////		}
//
//		//ClientResource instanceFactResource = new ClientResource(component.getContext().createChildContext(), app.getGmurlinternal() + "instance");
//		//New code equivalent mimicking GLUEletManager changes for connection stability
//		ClientResource instanceFactResource = new ClientResource(app.getGmurlinternal() + "instance");
//
//		try {
//			System.out.println("trying to create the instance of type "+toolType+" at URL "+app.getGmurlinternal() + "instance");
//			// Construct the POST representation and send it
//			// The monitor is sent, but it has no real use, since we get the teachers from the participant list
//			instanceFactResource.post(buildRepresentation(toolType, configData,
//					teachers, participants));
//
//			// extract the URL from the response and print it!
//			if (instanceFactResource.getStatus().isSuccess()
//					&& instanceFactResource.getResponseEntity().isAvailable()) {
//				Representation rep = instanceFactResource.getResponseEntity();
//				if (rep != null) {
//					FormattedEntry answerEntry = new FormattedEntry(
//							rep.getText());
//					System.out.println("Created the instance "
//							+ answerEntry.getId());
//					System.out.println("The end-user URL is "
//							+ answerEntry.getLinks().get(0));
//					return answerEntry.getId();
//				}
//			} else {
//				throw new ResourceException(instanceFactResource.getStatus());
//			}
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} finally {
////			Representation entity = instanceFactResource.getResponseEntity();
////			if (entity != null)
////				entity.release();
////			instanceFactResource.release();
//			
//			//New code equivalent mimicking GLUEletManager changes for connection stability
//			discardRepresentation(instanceFactResource.getResponseEntity());    /// **** LA CLAVE: "VOLVER A" EXTRAER LA ENTIDAD RESPUESTA DEL 
//																		///CLIENTRESOURCE; si se llega aquí es desde la ejecución de .get(), 
//																		///por lo que la variable 'response' NO SE HA ASIGNADO
//		}
//
//		return null;
	}


	private Representation buildRepresentation(String toolId, String configurationData, String[] teachersNames, String[] usersNames) {

		//Old code, we do not use the monitor field anymore, we get the teachers from the participants list

		FormattedEntry entry = new FormattedEntry();
		entry.addExtendedTextChild("tool", toolId);
		if (configurationData != null && configurationData.length() > 0)
			entry.addExtendedStructuredElement("configuration", configurationData);
		else
			entry.addExtendedTextChild("configuration", "");
		
		String[] newUsers = null;
		String teacher = null;
		if(teachersNames!=null && teachersNames.length>0){
			teacher = teachersNames[0];
			//If there are teachers, we just add all of them to the users list
			int numTeachers = teachersNames.length;
			
			if(usersNames!=null) newUsers = new String[usersNames.length+numTeachers];
			else newUsers = new String[numTeachers];
			
			if(usersNames!=null){
				for(int i = 0; i<usersNames.length; i++) newUsers[i] = usersNames[i];
			}
			for(int i = 0; i<numTeachers; i++) newUsers[i+usersNames.length]=teachersNames[i];
			
		}else{//No teachers, we just put the first user as callerUser - if there are no users, we put nothing (but it will probably fail!)
			if(usersNames!=null){
				teacher = usersNames[0];
				newUsers = usersNames;
			}else{
				teacher = null;
				newUsers = null;
			}
			
		}
		
		if(newUsers!=null) entry.addExtentedStructuredList("users", "user", newUsers);
		if(teacher!=null) entry.addExtendedTextChild("callerUser", teacher);			
		
		return entry.getRepresentation();
	}

	
	
	
	private String buildStringRepresentation(String toolId, String configurationData, String[] teachersNames, String[] usersNames) throws IOException {
		
		Representation rep = buildRepresentation(toolId, configurationData, teachersNames, usersNames);
		
		try {
			return rep.getText();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
	}
	
	private String setTitleInXForms(String toolType, String configData,
			String name) {
		
		//TODO: In the future, this should be done with plugins (dynamically loaded classes)
		
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		
		if(toolType.equals(app.getGDocsGMType())||
				toolType.equals(app.getGDocs3GMType())||
				toolType.equals(app.getGSprdGMType())||
				toolType.equals(app.getGSprd3GMType())||
				toolType.equals(app.getGPresGMType())||
				toolType.equals(app.getGPres3GMType())){
			//It is a GDocs XForm
			configData = setFieldInXForms(configData, "ins:title", name);

		} else if(toolType.equals(app.getDabbGMType())){
			//Dabbleboard does not support titles, so we do nothing	
		} else if(toolType.equals(app.getGSICMWGMType())){
			//Since MW page titles must be unique, we add a temporal seal to the title
			configData = setFieldInXForms(configData, "title", (new Date()).getTime()+MW_TITLE_SEPARATOR+name);
		} else if(toolType.equals(app.getWebCGMType())){
			//WebContent does not support titles, so we do nothing	
		}
		
		return configData;

	}


	private String setFieldInXForms(String configData, String tag,
			String text) {
		
		
		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true); 
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(configData.getBytes()));

			NodeList nodes = doc.getElementsByTagName(tag);
			System.out.println(doc.toString()+"\nNodos "+tag+" encontrados: "+nodes.getLength());
			
		    for (int idx = 0; idx < nodes.getLength(); idx++) {
		        nodes.item(idx).setTextContent(text);
		        System.out.println(nodes.item(idx).toString());
		     }
		    
		    TransformerFactory transformerFactory =TransformerFactory.newInstance();
		    Transformer transformer = null;
		    transformer = transformerFactory.newTransformer();
		    Source source = new DOMSource(doc);
		    StringWriter writer = new StringWriter();
		    Result result = new StreamResult(writer);
		    transformer.transform(source,result);

		    return writer.toString();

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}


	
	
	private String getToolInstanceConfiguration(String toolType) {
		
     	//Get the GlueletManager URL from app properties
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		String gmUrl = app.getGmurlinternal();
	
		
		String response;
		try {
			response = doGetFromURL(gmUrl+"tools/"+toolType+"/configuration");
			return response;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
////		//Get the list of tool types from the GlueletManager
////    	Component component = new Component();
////    	Client client = new Client(Protocol.HTTP);
////    	//Timeout 0 means that timeout is infinite 
////    	client.setConnectTimeout(0);
////    	component.getClients().add(client);
////    	try {
////			component.start();
////		} catch (Exception e1) {
////			// TODO Auto-generated catch block
////			e1.printStackTrace();
////		}
//
//		//ClientResource configResource = new ClientResource(component.getContext().createChildContext(), gmUrl+"tools/"+toolType+"/configuration");  
//		//New code equivalent mimicking GLUEletManager changes for connection stability
//		ClientResource configResource = new ClientResource(gmUrl+"tools/"+toolType+"/configuration");
//		
//		try{
//			configResource.get();
//			Representation config = null;
//			if (configResource.getStatus().isSuccess() && configResource.getResponseEntity().isAvailable()) config = configResource.getResponseEntity();
//			
//			//config.write(System.out);
//			
//			//Extract the ins:data tag from the Atom
//			String configData = null;
//			FormattedEntry configEntry = new FormattedEntry(config.getText());
//			
//			System.out.println("Received from GM:\n"+configEntry.getXhtmlContentAsText());
//			
//			//configData = findConfigData(configEntry.getXhtmlContent());
//			configData = configEntry.getXhtmlContentAsText();
//			
//			return configData;
//		}catch(Exception e){
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} finally {
////			Representation entity = configResource.getResponseEntity();
////			if (entity != null)
////				entity.release();
////			configResource.release();
//			
//			//New code equivalent mimicking GLUEletManager changes for connection stability
//			discardRepresentation(configResource.getResponseEntity());    /// **** LA CLAVE: "VOLVER A" EXTRAER LA ENTIDAD RESPUESTA DEL 
//																		///CLIENTRESOURCE; si se llega aquí es desde la ejecución de .get(), 
//																		///por lo que la variable 'response' NO SE HA ASIGNADO
//		}
//		

	}



}
