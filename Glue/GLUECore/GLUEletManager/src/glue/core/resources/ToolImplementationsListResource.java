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

import glue.common.format.FormatStatic;
import glue.common.format.FormattedFeed;
import glue.common.resources.GLUEResource;
import glue.core.controllers.JpaControllersManager;
import glue.core.entities.ToolImplementation;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Resource 'list of tools' (tool implementations, indeed).
 * 
 * List of all the registered tool implementations available to create Gluelets. 
 * 
 * @author	 	David A. Velasco
 * @original	juaase
 * @contributor	Javier Enrique Hoyos Torio
 * @version 	2012092501
 * @package 	glue.core.resources
 */

public class ToolImplementationsListResource extends GLUEResource {
	
	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");


	/**
	 * GET ToolImplementationsListResource
	 * 
	 * Returns a list with all registered tool implementations.
	 * 
	 * Method ::18:: at Informe_041b_01_02_09.pdf
	 * 
	 * @return	'Atomized' list of tool implementations known by GLUEletManager
	 */
	//@Get("atom")
    @Get()
    public Representation getTools() {
    	
   		logger.info("** GET TOOLS received");
    	
   		FormattedFeed feed = new FormattedFeed();
		String uri = getReference().getIdentifier();
		
		// Atom standard elements
		feed.setId(uri);  
		feed.setTitle("List of tools (tool implementations)");
		feed.addAuthor(FormatStatic.GSIC_NAME, null, null);
		feed.setSelfLink(null, uri);
			
		// adding the ToolImplementation entries
		List<ToolImplementation> toolImpsList = JpaControllersManager.getToolImplementationsController().findToolImplementationEntities();
		Iterator<ToolImplementation> toolImpsListIterator = toolImpsList.iterator();
		String feedReference = getReference().toString();
		ToolImplementation toolImp = null;
		Date lastUpdate = null, itemUpdate = null;
		ToolImplementationResource toolImpItem = null;
		while(toolImpsListIterator.hasNext())  {
			toolImpItem = new ToolImplementationResource();
			toolImp = (ToolImplementation)toolImpsListIterator.next();
			itemUpdate = toolImp.getUpdated();
			lastUpdate = (lastUpdate == null || itemUpdate.after(lastUpdate))?itemUpdate:lastUpdate;
			toolImpItem.setToolImplementation(toolImp, feedReference);
			feed.getEntries().add(toolImpItem.getToolEntry());
		}
		
		// last Atom standard ; last update date of any tool in the list (or 'now' if the list is empty)
		feed.setUpdated(lastUpdate!=null?lastUpdate:new Date());
		
		Representation answer = feed.getRepresentation();

		/* to test VLEAdapter timeouts
		try {
			Thread.sleep(15000);
		} catch (InterruptedException ex) {
			System.out.println("He sido interrumpido!!!");
		}
		*/
		
		try {
			logger.info("** GET TOOLS answer: \n" + (answer != null ? answer.getText() : "NULL"));
		} catch (IOException io) {
			throw new RuntimeException("Extremely weird failure while trying to log", io);
		}
		return answer;
		
	}
			
			
	/**
	 * Handle POST requests: create a new item.
	 *
	 * Method ::17:: at Informe_041_01_02_09.pdf
	 * 
	 * @todo real code
	 * @todo Atom
	 */
	/*
	@Post
	public Representation acceptItem(Representation entity) {
		Representation result = null;
		/*
		// Parse the given representation and retrieve pairs of
		// "name=value" tokens.
		Form form = new Form(entity);
		String toolId = form.getFirstValue("id");
		String toolName = form.getFirstValue("name");

		// Register the new item if one is not already registered.
		if (!getTools().containsKey(toolName) && getTools().putIfAbsent(toolName, new Tool(toolId, toolName)) == null) {
			// Set the response's status and entity
			setStatus(Status.SUCCESS_CREATED);
			System.out.println("SUCCESS, TOOL CREATED: id=" + toolId + ", name=" + toolName);
			Representation rep = new StringRepresentation("Item created",
					MediaType.TEXT_PLAIN);
			// Indicates where is located the new resource.
			rep.setIdentifier(getRequest().getResourceRef().getIdentifier() + "/" + toolId);
			result = rep;
		} else { // Item is already registered.
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			result = generateErrorRepresentation("Tool " + toolId + " already exists.", "1");
		}
		*-/
		return result;
	}
	*/

}
