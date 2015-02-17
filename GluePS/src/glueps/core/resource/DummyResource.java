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
import org.restlet.resource.ResourceException;

import org.restlet.resource.Get;
import org.restlet.util.Series;
import org.xmldb.api.base.Collection;



import glueps.adaptors.ld.ILDAdaptor;
import glueps.adaptors.ld.LDAdaptorFactory;
import glueps.adaptors.ld.imsld.IMSLDAdaptor;
import glueps.adaptors.vle.VLEAdaptorFactory;
import glueps.core.controllers.exist.Exist;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.persistence.JpaManager;


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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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

public class DummyResource extends GLUEPSResource {

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
    @Get()
    public Representation getDummy() {
    	
   		logger.info("** GET DUMMY received");
    	
		Representation answer = new StringRepresentation("You have logged out. You will be redirected in a few moments...");
		
		try {
			logger.info("** GET DUMMY answer: \n" + (answer != null ? answer.getText() : "NULL"));
		} catch (IOException io) {
			throw new RuntimeException("Extremely weird failure while trying to log", io);
		}
		//return 401 error code
		setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
		return answer;
		
	}
 

}
