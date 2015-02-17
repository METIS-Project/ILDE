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

package glueps.adaptors.ld;

import java.io.File;

import glueps.adaptors.ld.edit2.EDIT2Adaptor;
import glueps.adaptors.ld.glueps.GluepsAdaptor;
import glueps.adaptors.ld.imsld.IMSLDAdaptor;
import glueps.adaptors.ld.ppc.PPCAdaptor;
import glueps.adaptors.vle.IVLEAdaptor;
import glueps.adaptors.vle.mediawiki.MediaWikiAdaptor; 
import glueps.adaptors.vle.moodle.MoodleAdaptor;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.LearningEnvironment;

public final class LDAdaptorFactory {

	private GLUEPSManagerApplication app;
	private String UPLOAD_DIRECTORY;
	private String SCHEMA_LOCATION;
	
	public static final String IMSLD_TYPE = "IMS LD";
	public static final String PPC_TYPE = "PPC";
	public static final String T2_TYPE = "T2";
	public static final String GLUEPS_TYPE = "GLUEPS";

	public LDAdaptorFactory(GLUEPSManagerApplication applicationRest){
		
		this.app = applicationRest;
		this.UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";
		this.SCHEMA_LOCATION = app.getAppPath()+"/schemas/";
		
	}
	
	public ILDAdaptor getLDAdaptor(String ldType, String designId){
		
		if(ldType==null) return null;
		
		if(ldType.equals(IMSLD_TYPE)){
			String uploadDir = UPLOAD_DIRECTORY + designId + File.separator;
			IMSLDAdaptor adaptor = new IMSLDAdaptor(SCHEMA_LOCATION, uploadDir);
			return adaptor;
		}else if(ldType.equals(PPC_TYPE)){
			
			PPCAdaptor adaptor = new PPCAdaptor(designId);
			
			return adaptor;
			
		}else if(ldType.equals(T2_TYPE)){
			
			EDIT2Adaptor adaptor = new EDIT2Adaptor(designId);
			
			return adaptor;
			
		}else if(ldType.equals(GLUEPS_TYPE)){
			
			GluepsAdaptor adaptor = new GluepsAdaptor(designId);
			
			return adaptor;
			
		}
		else return null;
		
	}
}
