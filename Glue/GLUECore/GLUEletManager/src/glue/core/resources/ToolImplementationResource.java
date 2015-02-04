/**
 This file is part of GlueletManager.

 GlueletManager is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 GlueletManager is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use GlueletManager for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when GlueletManager is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute GlueletManager and/or modify it under the
   terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>
 ]
*/
package glue.core.resources;

import java.io.IOException;
import java.util.logging.Logger;

import glue.common.format.FormatStatic;
import glue.common.format.FormattedEntry;
import glue.common.resources.GLUEResource;
import glue.core.controllers.JpaControllersManager;
import glue.core.entities.ImplementationModel;
import glue.core.entities.Tool;
import glue.core.entities.ToolImplementation;
import glue.core.entities.ToolService;

import org.restlet.data.Reference;
import org.restlet.data.Status;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

/**
 * Resource tool.
 * 
 * Information about a registered tool implementation, available to create Gluelets. 
 * 
 * @author	 	David A. Velasco <davivel@gsic.uva.es>
 * @contributor	Javier Enrique Hoyos Torio
 * @original	juaase
 * @version 	2012092501
 * @package 	glue.core.resources
 */

public class ToolImplementationResource extends GLUEResource {
	
	/// attributes ///
	
	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");
 
	
	/** Local id. Integer used as identifier in table of tool implementations, in Internal Registry */
    protected int toolImpLocalId;
    
    /** Tool implementation data in Internal Registry */
    protected ToolImplementation toolImplementation = null;
    
    /** URL to list of tool implementations feed; used when tool implementation info is dumped into a list of tool implementations feed (see ToolImplementationsListResource) */
    protected String toolImpListReference = null;

    /** Tool data in Internal Registry */
    protected Tool tool = null;
    
    /** Tool service data in Internal Registry */
    protected ToolService toolService = null;
    
    /** Implementation model data in Internal Registry */
    protected ImplementationModel implementationModel = null;
    
	
	/// methods ///
    
    /**
     * Resource initialization.
     * 
     * Parse URI searching for the local id and extract info from Internal Registry, if tool exists.
     */
    @Override
    protected void doInit() throws ResourceException {
    	
   		// get the "toolId" attribute value taken from the URI template /tool/{toolId}
   		String toolIdString = (String) getRequest().getAttributes().get("toolId");
   		try {
   			this.toolImpLocalId = Integer.parseInt(toolIdString);
   		} catch (NumberFormatException e) {
   			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Wrong identifier format in the URL", e);
   		}

   		// get a controller for accessing the ToolImplementation entity and find it in the Internal Registry
   		this.toolImplementation = JpaControllersManager.getToolImplementationsController().findToolImplementation(this.toolImpLocalId);
        
   		// get the data associated by toolImplementation
   		if (toolImplementation != null) {
   			this.tool = JpaControllersManager.getToolsController().findTool(toolImplementation.getToolId());
   			this.toolService = JpaControllersManager.getToolServicesController().findToolService(this.toolImplementation.getToolServiceId());
   			this.implementationModel = JpaControllersManager.getImplementationModelsController().findImplementationModel(this.toolImplementation.getModelId());
   		}
        
   		// does it exist?
   		setExisting(this.toolImplementation != null);	// setting 'false' implies that REST methods won't start; server will respond with 404

    }
    
    
    /**
     * Set the URL corresponding to a containing feed.
     * 
     * Used for creating the tool implementation resource from the containing Feed; it is not used by the RESTlet engine when the resource is accessed directly.
     */
    public void setToolImplementation(ToolImplementation toolImp, String feedReference)  {
        this.toolImpListReference = feedReference;
        this.toolImplementation = toolImp;
        this.toolImpLocalId = this.toolImplementation.getId();
        // get the data associated by toolImplementation
        if (toolImplementation != null) {
        	this.tool = JpaControllersManager.getToolsController().findTool(toolImplementation.getToolId());
        	this.toolService = JpaControllersManager.getToolServicesController().findToolService(this.toolImplementation.getToolServiceId());
        	this.implementationModel = JpaControllersManager.getImplementationModelsController().findImplementationModel(this.toolImplementation.getModelId());
        }
        setExisting(this.toolImplementation != null);	// if it can be null, there's a big problem with the previous sentence
    }
    
    
	/**
	 * GET ToolImplementationResource
	 * 
	 * Returns the information about a registered tool implementation.
	 * 
	 * Method ::22:: at Informe_041b_01_02_09.pdf
	 * 
	 * @return	'Atomized' tool info known by GLUEletManager
	 */
    //@Get("atom")
    @Get
    public Representation getTool()  {
    	
   		logger.info("** GET TOOL received");

   		FormattedEntry entry = getToolEntry();
   		Representation answer = null;
    	
   		if (entry != null)
   			answer = entry.getRepresentation();
    		
   		try {
   			logger.info("** GET TOOL answer: \n" + (answer != null ? answer.getText() : "NULL"));
		} catch (IOException io) {
			throw new RuntimeException("Extremely weird failure while trying to log", io);
		}
   		return answer;
    	
    }

    /**
     * Builds the Atom+GLUE formtted entry corresponding to a registeres tool implementation.
     * 
     * For access from ToolImplementationResource
     *  
     * @return	Atom+GLUE formatted info known by GLUEletManager about a tool implementation.
     */
    public FormattedEntry getToolEntry() {
    	FormattedEntry entry = null;
        if(this.isExisting()) {
            entry = new FormattedEntry();

            /// build URI
            String uri = null;
            Reference ref = getReference();
            if(ref!=null)
            	uri = ref.getIdentifier();
            else
            	uri = toolImpListReference + "/" + toolImpLocalId;

            /// Atom standard elements
            entry.setId(uri);
            entry.setTitle(tool.getName());
            entry.setUpdated(tool.getUpdated());
            entry.setAlternateLink("Description of tool "+ tool.getName(), uri);
           	entry.addAuthor(FormatStatic.GSIC_NAME, null, null);
            entry.setRelatedLink("Provider", toolService.getUrl());
            
            /// GLUE specific elements
            entry.addExtendedTextChild("impName", implementationModel.getName());
            entry.addExtendedTextChild("toolProvider", toolService.getUrl());

        } else {
        	setStatus(Status.CLIENT_ERROR_NOT_FOUND);
        }
        
        return entry;
    }

    
	/**
	 * Update the information about a registered tool.
	 * 
	 * Method ::23:: at Informe_041_01_02_09.pdf
	 * 
	 * @return
	 * @todo 	ALL	
	 */
    /*
    @Put
	public Representation modTool(Representation entity) {
    	return entity;
    }
    
	/**
	 * Erase the tool resource.
	 * 
	 * Method ::24:: at Informe_041_01_02_09.pdf
	 * 
	 * @return
	 * @todo 	ALL	
	 *-/
    @Delete
    public void erase(Representation entity) {
    	
    }
    */

    
}
