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
package glueps.core.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import glue.common.format.FormatStatic;
import glue.common.format.FormattedEntry;
import glue.common.format.FormattedFeed;
import glue.common.resources.GLUEResource;

import glueps.core.controllers.exist.Exist;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.persistence.JpaManager;


import org.apache.commons.io.FileUtils;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.data.CharacterSet;
import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;




/**
 * Resource tool.
 * 
 * Information about a registered design, available to create Gluelets. 
 * 
 * @author	 	Juan Carlos A. <jcalvarezgsic@gsic.uva.es>
 * @package 	glueps.core.resources
 */

public class DesignResource extends GLUEPSResource {
	
/// attributes ///
	
	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");
 
	
	/** Local id. Integer used as identifier in table of designs, without extension! */
    protected String designId;
    
    protected Design design;
    
    protected String feedReference;
    
    @Override
    protected void doInit() throws ResourceException {
    	
   		// get the "designId" attribute value taken from the URI template /designs/{designId}
   		this.designId = trimId((String) getRequest().getAttributes().get("designId"));

   		logger.info("Initializing design resource "+this.designId);
   		
   		JpaManager dbmanager = JpaManager.getInstance();
   		design = dbmanager.findDesignObjectById(designId);

   		// does it exist?
		setExisting(this.design != null);	// setting 'false' implies that REST methods won't start; server will respond with 404

    }
    
    
    
    
    @Get
    public Representation getDesign()  {
    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
		String login = this.getRequest().getChallengeResponse().getIdentifier();
		if(!design.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the design");
     		
   		logger.info("** GET DESIGN received");
   		Representation answer = null;
   		
   		Design urlifiedDesign = URLifyDesign(design, null, getReference().getParentRef().getParentRef().getIdentifier());
   		
   		String xmlfile = generateXML(urlifiedDesign, glueps.core.model.Design.class);
   		
   		if (xmlfile != null){	   		
				answer = new StringRepresentation((CharSequence) xmlfile, MediaType.TEXT_XML);
				answer.setCharacterSet(CharacterSet.UTF_8);
   		}
    	
   		logger.info("** GET DESIGN answer: \n" + (answer != null ? xmlfile : "NULL"));
   		return answer;
    	
    }


    @Delete
    public Representation deleteDesign() {
    	
    	if (doAuthentication){
	    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
			String login = this.getRequest().getChallengeResponse().getIdentifier();
			if(!design.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the design");
    	}

		logger.info("** DELETE DESIGN received");

		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		String UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";

		//We delete all associated deploys
		JpaManager dbmanager = JpaManager.getInstance();
		List<Deploy> deploys;
		try {
			deploys = dbmanager.listDeployObjectsByDesignId(designId);
		} catch (UnsupportedEncodingException e1) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to obtain the list of deploys associated to the design to be deleted", e1);
		}
		if(deploys!=null){
			for(Deploy deploy : deploys){
		   		logger.info("Trying to delete deploy "+deploy.getId()+".");
				
		   		deleteLocalDeploy(deploy.getId());
			}
		}
		

		//We delete the uploaded files
		File designDir = new File(UPLOAD_DIRECTORY+this.designId);
		if(!designDir.exists() || !designDir.isDirectory()){
	   		logger.info("The design directory does not exist! we do nothing...");			
		}else{
			try {
				FileUtils.deleteDirectory(designDir);
			} catch (IOException e) {
		   		logger.info("Error while deleting the directory. We continue...");			
				e.printStackTrace();
			}
		}
		
		dbmanager.deleteDesign(designId);
		
   		logger.info("** DELETE DESIGN completed");
   		return null;
	}    




//  private Status deleteLocalResource(String path) {
//		
//  	
//  	Status status = null;	
//		Representation response = null;
//		
////		Component component = new Component();
////		Client client = new Client(Protocol.RIAP);
////		// Timeout 0 means that timeout is infinite
////		client.setConnectTimeout(0);
////		
////		component.getClients().add(client);
////		try {
////			component.start();
////		} catch (Exception e1) {
////			// TODO Auto-generated catch block
////			e1.printStackTrace();
////		}
//
//		ClientResource localResource = new ClientResource("riap://host/GLUEPSManager"+path);
//		
//		try {
//			localResource.delete();
//			response = localResource.getResponseEntity();
//			status = localResource.getStatus();
//			if (status.equals(Status.SUCCESS_OK) ||
//				status.equals(Status.SUCCESS_ACCEPTED) ||
//				status.equals(Status.SUCCESS_NO_CONTENT)) {
//					
//				return status;
//			}
//			
//		} catch (ResourceException r) {
//			r.printStackTrace();
//			ResourceException re = convertResourceException(r);
//			
//			throw re;
//		} finally {
////			Representation entity = localResource.getResponseEntity();
////			if (entity != null)
////				entity.release();
////			localResource.release();
//			
//			//New code equivalent mimicking GLUEletManager changes for connection stability
//			discardRepresentation(localResource.getResponseEntity());    /// **** LA CLAVE: "VOLVER A" EXTRAER LA ENTIDAD RESPUESTA DEL 
//																		///CLIENTRESOURCE; si se llega aquí es desde la ejecución de .get(), 
//																		///por lo que la variable 'response' NO SE HA ASIGNADO
//
//		}
//		
//		// HTTP code is a not accepted 2xx
//		throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected answer from GLUEPS, HTTP status " + status);
//
//		
//	}




//	/**
//     * Returns a list of deploys in the DB for the specified designId
//     * @param trimmedDesignId2
//     * @return
//     * @throws IOException 
//     */
//    private ArrayList<String> getDeploysForDesign(String designId) {
//		
//    	GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
//    	
//    	ArrayList<String> deployIds = null;
//    	
//    	//String url = getReference().getParentRef().getParentRef().getIdentifier()+"deploys";
//    	String url = "http://localhost:"+app.getPort()+"/GLUEPSManager/deploys";
//    	
//    	String response = getRemoteResource(url);
//    	
//		FormattedFeed feed = new FormattedFeed(response);
//    	
//		List<FormattedEntry> entries = feed.getEntries();
//		
//		for(Iterator<FormattedEntry> it = entries.iterator();it.hasNext();){
//			
//			FormattedEntry deployEntry = it.next();
//			String deployId = deployEntry.getId().substring(deployEntry.getId().indexOf("/deploys/")+9);
//			String trimmedDeployId;
//			
//			if(deployId.indexOf(".")!=-1) trimmedDeployId = deployId.substring(0, deployId.indexOf("."));
//			else trimmedDeployId = deployId;
//			
//			if(trimmedDeployId.startsWith(designId)){
//				if(deployIds == null) deployIds = new ArrayList<String>();
//				deployIds.add(trimmedDeployId);
//			}
//		}
//		
//		
//    	return deployIds;
//	}
//

    





	private void deleteLocalDeploy(String id) {
		DeployResource depRes = new DeployResource();
		depRes.setDeploy(id, null);
		Representation rep = depRes.deleteDeploy();//if it goes ok, we do nothing (if not, it throws exception)
	}




	private String getRemoteResource(String url) {
		
		String response;
		try {
			response = doGetFromURL(url);
			return response;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
			
//			//Get the list of tool types from the GlueletManager
////	    	Component component = new Component();
////	    	Client client = new Client(Protocol.HTTP);
////	    	//Timeout 0 means that timeout is infinite 
////	    	client.setConnectTimeout(0);
////	    	component.getClients().add(client);
////	    	try {
////				component.start();
////			} catch (Exception e1) {
////				// TODO Auto-generated catch block
////				e1.printStackTrace();
////			}
//
//			//ClientResource configResource = new ClientResource(component.getContext().createChildContext(), url);
//			//New code equivalent mimicking GLUEletManager changes for connection stability
//			ClientResource configResource = new ClientResource(url);
//			
//			try{
//				configResource.get();
//				Representation response = null;
//				if (configResource.getStatus().isSuccess() && configResource.getResponseEntity().isAvailable()) response = configResource.getResponseEntity();
//				
//				//config.write(System.out);
//				
//				//Extract the ins:data tag from the Atom
//				String deploys = response.getText();
//				//FormattedEntry deploysFeed = new FormattedEntry(response.getText());
//				
//				//System.out.println("Received from GLUEPS:\n"+deploysFeed.getXhtmlContentAsText());
//				
//				//configData = findConfigData(configEntry.getXhtmlContent());
//				//deploys = deploysFeed.getXhtmlContentAsText();
//				
//				return deploys;
//			}catch(Exception e){
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return null;
//			} finally {
////				Representation entity = configResource.getResponseEntity();
////				if (entity != null)
////					entity.release();
////				configResource.release();
//				
//				//New code equivalent mimicking GLUEletManager changes for connection stability
//				discardRepresentation(configResource.getResponseEntity());    /// **** LA CLAVE: "VOLVER A" EXTRAER LA ENTIDAD RESPUESTA DEL 
//																			///CLIENTRESOURCE; si se llega aquí es desde la ejecución de .get(), 
//																			///por lo que la variable 'response' NO SE HA ASIGNADO
//			}
//			

    	
	}


	private Status deleteRemoteResource(String url) {
    	try {
			int response = doDeleteFromURL(url);
			return new Status(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Status.SERVER_ERROR_INTERNAL;
		}
    	
	}

//    private Status deleteRemoteResource(String url) {
//		Status status = null;	
//		Representation response = null;
//	
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
//		//ClientResource remoteResource = new ClientResource(component.getContext().createChildContext(), url);
//		//New code equivalent mimicking GLUEletManager changes for connection stability
//		ClientResource remoteResource = new ClientResource(url);
//		
//		try {
//			remoteResource.delete();
//			response = remoteResource.getResponseEntity();
//			status = remoteResource.getStatus();
//			if (status.equals(Status.SUCCESS_OK) ||
//				status.equals(Status.SUCCESS_ACCEPTED) ||
//				status.equals(Status.SUCCESS_NO_CONTENT)) {
//					
//				return status;
//			}
//			
//		} catch (ResourceException r) {
//			ResourceException re = convertResourceException(r);
//
//			throw re;
//		} finally {
////			Representation entity = remoteResource.getResponseEntity();
////			if (entity != null)
////				entity.release();
////			remoteResource.release();
//			
//			//New code equivalent mimicking GLUEletManager changes for connection stability
//			discardRepresentation(remoteResource.getResponseEntity());    /// **** LA CLAVE: "VOLVER A" EXTRAER LA ENTIDAD RESPUESTA DEL 
//																		///CLIENTRESOURCE; si se llega aquí es desde la ejecución de .get(), 
//																		///por lo que la variable 'response' NO SE HA ASIGNADO
//
//		}
//		
//		// HTTP code is a not accepted 2xx
//		throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected answer from GLUEPS, HTTP status " + status);
//
//	}
//

    
    /**
	 * PUT DesignResource
	 * 
	 * Modifies Design in Glueps.
	 * 
	 *
	 * 
	 * @return	URI to new Design resource, or null.
	 */
	@Put("xml")
    public Representation putDesign(Representation entity)  {

   		try {
   	    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
   			String login = this.getRequest().getChallengeResponse().getIdentifier();
   			if(!design.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the design");

   			logger.info("** PUT DESIGN received");

			GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
			String UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";
			String uploadDir = UPLOAD_DIRECTORY+"/temp/";
			
			File f;
			if(designId!=null){
				//We get the xml that we have received, and write it to a temp file
				try{
					InputStream entrada= entity.getStream();
					//Write the xml to the uploads folder
				   f=new File(uploadDir, designId+".xml"+(new Date()).getTime());
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
				
					//We read the file, and generate the Design object
				String xmlfile;
				try {
					xmlfile = FileUtils.readFileToString(f, "UTF-8");
				} catch (IOException e) {
					throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to process the received xml file", e);
				}
				Design urlifiedDesign = (Design) generateObject(xmlfile, glueps.core.model.Design.class);
				
				//We de-urlify the object to be stored in database
				Design deurlifiedDesign = deURLifyDesign(urlifiedDesign);
				//We store the object in the database
				JpaManager dbmanager = JpaManager.getInstance();
				try {
					dbmanager.insertDesign(deurlifiedDesign);
				} catch (Exception e) {
					throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert the design in the DB", e);
				}
				
				//we delete the temporary file
				f.delete();
				
				Representation answer=null;
				//we return the representation, in the same form that it came
		   		
		   		if (xmlfile != null){	   		
						answer = new StringRepresentation((CharSequence) xmlfile, MediaType.TEXT_XML);
						answer.setCharacterSet(CharacterSet.UTF_8);
		   		}
		    	
		   		logger.info("** GET DESIGN answer: \n" + (answer != null ? xmlfile : "NULL"));
		   		return answer;


			}else{
				//We do not known which design to modify! bad request...
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Did not specify a designId");  
				
			}
		} catch (ResourceException e) {
			throw e;
		} finally {
            discardRepresentation(entity);        // fix to mimick GLUElet Manager corrections for connection stability
		}
		
   		
   		
   		
   		
    }
    
    
    
    
    public Design getDesignObject(){
    	return this.design;
    }
    
    
    public void setDesign(String designId, String feedReference)  {
        //this.designLocalId = designId;
    	//we set the designLocalId as the id without extension
    	this.designId = trimId(designId);
    	
    	this.feedReference=feedReference;

    	JpaManager dbmanager = JpaManager.getInstance();
    	this.design = dbmanager.findDesignObjectById(this.designId);
    	
   		//Since we are creating the resource internally, we set authentication to false
   		this.doAuthentication = false;
    	
		setExisting(this.design != null);	// setting 'false' implies that REST methods won't start; server will respond with 404

    }   
    
    public FormattedEntry getDesignEntry() {
    	FormattedEntry entry = null;
    	java.util.Date fecha = new Date();
        if(this.isExisting()) {
            entry = new FormattedEntry();

            /// build URI
            String uri = null;
            Reference ref = getReference();
            if(ref!=null)
            	uri = ref.getIdentifier();
            else
            	uri = feedReference+"/" + this.designId;

            uri = doGluepsUriSubstitution(uri);
            
            /// Atom standard elements
            entry.setId(uri);
            entry.setTitle(this.design.getName());
            entry.setUpdated(fecha);
            entry.setAlternateLink("Description of Design "+this.designId,uri);
           	entry.addAuthor(FormatStatic.GSIC_NAME, null,null);
            entry.setRelatedLink("Provider",uri);
     
           	
            //Glueps specific elements
           	entry.addExtendedTextChild("id",this.designId);
           	entry.addExtendedTextChild("designtype", design.getOriginalDesignType());
           	
         } else {
        	setStatus(Status.CLIENT_ERROR_NOT_FOUND);
        }
        
        return entry;
    } 

    
}
