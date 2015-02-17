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

package glueps.adaptors.ld.imsld;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.coppercore.common.MessageList;
import org.coppercore.exceptions.ValidationException;
import org.coppercore.parser.IMSCPManifestNode;
import org.coppercore.parser.IMSLDLearningDesignNode;
import org.coppercore.parser.IMSLDNode;
import org.coppercore.parser.IMSLDParser;
import org.coppercore.validator.IMSCPFactory;
import org.coppercore.validator.IMSLDManifest;
import org.coppercore.validator.IMSPackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import glueps.adaptors.ld.ILDAdaptor;
import glueps.adaptors.ld.imsld.icollage.InstanceCollageAdaptor;
import glueps.core.model.Activity;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.model.Participant;

public class IMSLDAdaptor implements ILDAdaptor {

	private final String schemaLocation;
	
	private final String resourcesBaseDir;

	public IMSLDAdaptor(String schemaLocation, String tmpDir) {
		super();
		this.schemaLocation = schemaLocation;
		this.resourcesBaseDir = tmpDir;
	}
	
	
	//This method processes a zip file containing an IMSLS UoL, and generates a Lingua Franca Design
	public Design processUoL(String uolFile){
		
		Design design = null;
		MessageList messageList = new MessageList();
		try {
			IMSPackage pack = IMSCPFactory.getIMSPackage(uolFile, schemaLocation, messageList);
			if(pack.validate()){
				IMSLDManifest manifest = pack.getIMSLDManifest();
				if(manifest.validate(new File(resourcesBaseDir))){
					//Method inspired by from Susana & Vanesa's PFC
					
		        	Document document = manifest.getDocument();
		        	Element elemDoc = document.getDocumentElement();
		        	
		        	IMSCPManifestNode imscpManifestNode = new IMSCPManifestNode(elemDoc);
		        	if (imscpManifestNode.validate(messageList)) {
		        		System.out.println("IMSCP manifest validated");

						System.out.println("Trying to store resources at "+resourcesBaseDir);
						pack.storeResources(resourcesBaseDir);
		        		design = imscpManifestNode.getLFDesign(resourcesBaseDir);
		        		if(design!=null){
							System.out.println("Design after IMSLD parsing:\n"+design.toString());
							return design;
		        		}
					}
				}
			}
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return null;
		
	}
	
	public Deploy processInstantiation(String instFile, Design design, HashMap<String,Participant> vleUsers){
		
		Deploy deploy = null;
		
		if(design!=null){
			deploy = new Deploy(null, design, null, null, null, new Date(), null, null, null, null, null);
		}

		//Use the ICollage and Moodle files to complete the Deploy
		if(instFile!=null){
			InstanceCollageAdaptor icAdaptor = new InstanceCollageAdaptor();
			try {
				icAdaptor.process("glueps.adaptors.ld.imsld.icollage.model", deploy.getId(), instFile, vleUsers);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (JAXBException e) {
				e.printStackTrace();
				return null;
			}
			deploy.setParticipants(icAdaptor.getParticipants());
			deploy.setGroups(icAdaptor.getGroups());
			deploy = icAdaptor.mapRolesGroupsParticipants(deploy);
			deploy = icAdaptor.generateInstancesFromGroups(deploy);
	
		}

		//Make a marshalling of the Deploy to the uploads directory
		//We put the generated deploy into an XML file with the unpacked resources
		File file = new File(resourcesBaseDir + "Deploy.xml");
        FileOutputStream outputFile;
        JAXBContext context;
		try {
			outputFile = new FileOutputStream(file);
			context = JAXBContext.newInstance(Deploy.class);
	        Marshaller m = context.createMarshaller();
	        m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
	        m.marshal(deploy, outputFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return deploy;
	}


	@Override
	public Design fromLDToLF(String filepath) {
		return processUoL(filepath);
	}
	
	
	//This method processes the 3 needed files (from Collage-IMSLD zip file, InstanceCollage XML and Moodle user file, and generates a Lingua Franca deploy
	//THIS METHOD IS NOT USED ANYMORE!
	/*	public Deploy process(String uolFile, String instFile, String mooFile){
		
		Deploy deploy = null;
		MessageList messageList = new MessageList();
		try {
			IMSPackage pack = IMSCPFactory.getIMSPackage(uolFile, schemaLocation, messageList);
			if(pack.validate()){
				IMSLDManifest manifest = pack.getIMSLDManifest();
				if(manifest.validate(new File(resourcesBaseDir))){
					//Method inspired by from Susana & Vanesa's PFC
					
		        	Document document = manifest.getDocument();
		        	Element elemDoc = document.getDocumentElement();
		        	
		        	IMSCPManifestNode imscpManifestNode = new IMSCPManifestNode(elemDoc);
		        	if (imscpManifestNode.validate(messageList)) {
		        		System.out.println("IMSCP manifest validated");

						System.out.println("Trying to store resources at "+resourcesBaseDir);
						pack.storeResources(resourcesBaseDir);
		        		
		        		
						//GenericTree<Activity> activityTree = imscpManifestNode.getLFActivityTree();
		        		Design design = imscpManifestNode.getLFDesign(resourcesBaseDir);
						System.out.println("Design after IMSLD parsing:\n"+design.toString());
		        		if(design!=null){
		        			deploy = new Deploy("1", design, "test", null, "lprisan", new Date(), null, null, null, null, null);
		        		}
	

		        		//Use the ICollage and Moodle files to complete the Deploy
		        		if(instFile!=null && mooFile!=null){
		        			InstanceCollageAdaptor icAdaptor = new InstanceCollageAdaptor();
		        			icAdaptor.process("glueps.adaptors.ld.imsld.icollage.model", deploy.getId(), instFile, mooFile);
		        	
		        			deploy.setParticipants(icAdaptor.getParticipants());
		        			deploy.setGroups(icAdaptor.getGroups());
		        			deploy = icAdaptor.mapRolesGroupsParticipants(deploy);
		        			deploy = icAdaptor.generateInstancesFromGroups(deploy);

		        		}
		        		
		    			//Make a marshalling of the Deploy to the uploads directory
		        		//We put the generated deploy into an XML file with the unpacked resources
		        		File file = new File(resourcesBaseDir + "Deploy.xml");
		    	        FileOutputStream outputFile = new FileOutputStream(file);
		    	        JAXBContext context = JAXBContext.newInstance(Deploy.class);
		    	        Marshaller m = context.createMarshaller();
		    	        m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		    	        m.marshal(deploy, outputFile);
		        		
						return deploy;
		        	}
				}
			}
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	*/
	
	
	
}
