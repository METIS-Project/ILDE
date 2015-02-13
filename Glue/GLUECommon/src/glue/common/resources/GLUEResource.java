/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of GlueletCommon.
 * 
 * GlueletCommon is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * GlueletCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.common.resources;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

/**
 * Common methods for every GLUEletManager resource.
 *
 * @author	 	David A. Velasco
 * @contributor Javier Enrique Hoyos Torio
 * @version 	2012092501
 * @package 	glue.core.resources
 */

public class GLUEResource extends ServerResource {
	
	
	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");
	
	
    /** Message for unhandled exceptions */
    public final static String REALLY_BAD_EXCEPTION_MESSAGE = "Something really wrong occured in GLUEletManager (RuntimeException): ";
    
    /** Message for unhandled errors */
    public final static String REALLY_BAD_ERROR_MESSAGE = "Something really wrong occured in GLUEletManager (Error): ";
	


	/**
     * Invoked when an error or an exception is caught during initialization,
     * handling or releasing. By default, updates the responses's status with
     * the result of
     * {@link org.restlet.service.StatusService#getStatus(Throwable, UniformResource)}
     * .
     * 
     * @param throwable
     *            The caught error or exception.
	 */
	@Override
	protected void doCatch(Throwable throwable) {
        Status status = null;
        String msg = "";

        if (throwable instanceof ResourceException) {
            ResourceException re = (ResourceException) throwable;
            status = re.getStatus();
            if (status.isClientError())
            	msg = re.getMessage() + " - " + re.getStatus().getDescription();
            else
            	msg = re.getMessage();	// davivel - our client doesn't need to know anything else; description will be visible in log file

    	} else if (throwable instanceof RuntimeException) {
    		status = Status.SERVER_ERROR_INTERNAL;
    		//msg = REALLY_BAD_EXCEPTION_MESSAGE + throwable.getMessage();
    		msg = status.getDescription();
    		
    	} else if (throwable instanceof Error) {
    		status = Status.SERVER_ERROR_INTERNAL;
    		//msg = REALLY_BAD_ERROR_MESSAGE + throwable.getMessage();
    		msg = status.getDescription();
    		
        } else {
        	// default - copied inspired from UniformResource code
            status = getStatusService().getStatus(throwable, this);
            msg = status.getDescription();
        }

        logger.log(Level.WARNING, "Exception or error caught in resource", throwable);
		//logger.warning(msg);

        if (getResponse() != null) {
            getResponse().setStatus(status);
            getResponse().setEntity(new StringRepresentation(msg));
        }
        
	}
	
	
	protected ResourceException convertResourceException(ResourceException r) {
		Status st = r.getStatus();
		ResourceException re = null;
		if (st.isClientError())
			re = new ResourceException(Status.SERVER_ERROR_INTERNAL, /*st.getDescription(),*/ r);
		
		else if (st.isServerError())
			re = new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, st.getDescription(), r);
		
		else if (st.isInformational() || st.isRedirection())
			re = new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected answer from implementation adapter, HTTP status " + st, r);
		
		else if (st.equals(Status.CONNECTOR_ERROR_COMMUNICATION))
			re = new ResourceException(Status.SERVER_ERROR_GATEWAY_TIMEOUT, "Communication error while connecting to implementation adapter", r);
		
		else if (st.equals(Status.CONNECTOR_ERROR_CONNECTION))
			re = new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Connection to implementation adapter couldn't be established", r);
		
		else if (st.equals(Status.CONNECTOR_ERROR_INTERNAL))
			re = new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unexpected error while connecting to implementation adapter", r);
		
		return re;
	}
	
	
	/**
	 * Discard representation object with a lot of care.
	 * 
	 * @param rep	Representation object to discard
	 */
	protected void discardRepresentation(Representation rep) {
		if (rep != null) {
			try {
				rep.exhaust();	// this call is explicitly required to avoid problems when using the production-recommended Apache HTTP Client connector
								//	http://wiki.restlet.org/docs_2.0/13-restlet/28-restlet/75-restlet.html
								//   http://www.restlet.org/documentation/2.0/jse/api/org/restlet/representation/Representation.html#release()
			} catch(IOException io) {
				getLogger().log(Level.WARNING, "Secondary exception while exhausting remote response (world sometimes is ugly)", io);
			}
			rep.release();
		}
	}
}
