package glueps.adaptors.ld.glueps;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;

import glueps.adaptors.ld.ILDAdaptor;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.model.Participant;

public class GluepsAdaptor implements ILDAdaptor {
	
	private String designId = null;
	
	public GluepsAdaptor(String designId)
	{
		this.designId = designId;
	}

	@Override
	public Design fromLDToLF(String filepath) {
		String xmlContent = null;		
		File xmlFile = new File(filepath);
		
		if(!xmlFile.exists()) return null;
		
		try {
			xmlContent = FileUtils.readFileToString(xmlFile, "UTF-8");
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		
		//Create the Design object from the XML  provided 
		try {
	        JAXBContext jc = JAXBContext.newInstance(glueps.core.model.Design.class);
	        Unmarshaller u = jc.createUnmarshaller();
	        Design design = (Design)u.unmarshal(new StringReader(xmlContent));
	        //Set the current timestamp
	        design.setTimestamp(new Date());
			return design;
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public Deploy processInstantiation(String filepath, Design design,
			HashMap<String, Participant> vleUsers) {
		String xmlContent = null;		
		File xmlFile = new File(filepath);
		
		if(!xmlFile.exists()) return null;
		
		try {
			xmlContent = FileUtils.readFileToString(xmlFile, "UTF-8");
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		
		//Create the Deploy object from the XML  provided 
		try {
	        JAXBContext jc = JAXBContext.newInstance(glueps.core.model.Deploy.class);
	        Unmarshaller u = jc.createUnmarshaller();
	        Deploy deploy = (Deploy)u.unmarshal(new StringReader(xmlContent));
	        deploy.setDesign(design);
	        //Set the current timestamp
	        deploy.setTimestamp(new Date());
			return deploy;
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}

}
