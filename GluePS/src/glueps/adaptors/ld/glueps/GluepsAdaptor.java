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
