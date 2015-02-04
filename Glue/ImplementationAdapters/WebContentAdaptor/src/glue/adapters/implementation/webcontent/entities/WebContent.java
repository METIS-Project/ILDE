/**
 This file is part of WebContentAdapter.

 WebContentAdapter is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 WebContentAdapter is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use WebContentAdapter for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when WebContentAdapter is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute WebContentAdapter and/or modify it under the
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
package glue.adapters.implementation.webcontent.entities;


import glue.common.entities.instance.InstanceEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.restlet.resource.ResourceException;


/**
 * Entity representing a Web Content.
 * 
 * @author  		David A. Velasco
 * @contributor 	Carlos Alario
 * @version 		2012092501
 * @package 		glue.adapters.implementation.webcontent.entities
 */
public class WebContent implements InstanceEntity {

	/// attributes ///
	
	/** Web Content title */
	protected String title = null;
	
	/** URL to the Web Content */
	protected String entryURL = null;
		
	/** Local identifier */
	protected int index = -1;
	
	/** Date of the instance creation */ 
	protected Date updated;
	
	
	/// methods ///
	
	/**
	 * Constructor for a Web Content.
	 * 
	 * @param 	url		String		URL identifying the Web Content.
	 */
	public WebContent(String docURL) {
		entryURL = docURL;
		title = "No title for this instance";
		updated = new Date();
	}

	
	public WebContent(int index, String title, Date updated) {
		this.index = index;
		this.title = title;
		this.updated = updated;
	}


	@Override
	public void create(String callerUser, Map<String, String> specificParams) throws ResourceException {
		// fake creation
	}


	@Override
	public String delete(Map<String, String> specificParams) throws ResourceException {
		// fake deletion
		return null;
	}

	
	@Override
	public String getAccessParams(String callerUser, Map<String, String> specificParams) {
		return null;
	}


	/**
	 * Getter for browser-friendly URL 

	 * @param 	callerUser	String					Name of the user asking for the URL
	 * @param	params		Map<String, String>		List of specific parameters
	 * @return 										Browser-friendly URL 
	 */
	@Override
	public String getHtmlURL(String callerUser, Map<String, String> specificParams) {
		return entryURL;
	}


	@Override
	public void loadSpecificState(BufferedReader in) throws IOException {
		entryURL = in.readLine();
	}


	@Override
	public void saveSpecificState(PrintStream out) {
		out.println(entryURL);
	}


	@Override
	public void setIndex(int index) {
		this.index = index;
	}
	

	/** 
	 * Getter for title 
	 * 
	 * @return title.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Getter for last update date
	 * 
	 * @return Last update date
	 */
	public Date getUpdated() {
		return updated;
	}


	@Override
	public int getIndex() {
		return index;
	}


	@Override
	public void setUsers(List<String> users, String callerUser, Map<String, String> specificParams) {
		// nothing to do
	}
	
}
