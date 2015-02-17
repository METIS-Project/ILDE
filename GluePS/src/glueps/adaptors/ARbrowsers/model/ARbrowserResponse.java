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

package glueps.adaptors.ARbrowsers.model;

import org.restlet.data.MediaType;
import org.restlet.data.Status;

public class ARbrowserResponse {

	
	private String answer;
	private Status status;
	private MediaType mediaType;
	
	
	
	public ARbrowserResponse() {
		super();
		// TODO Auto-generated constructor stub
	}



	public ARbrowserResponse(String answer, Status status, MediaType mediaType) {
		super();
		this.answer = answer;
		this.status = status;
		this.mediaType = mediaType;
	}



	public String getAnswer() {
		return answer;
	}



	public void setAnswer(String answer) {
		this.answer = answer;
	}



	public Status getStatus() {
		return status;
	}



	public void setStatus(Status status) {
		this.status = status;
	}



	public MediaType getMediaType() {
		return mediaType;
	}



	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}
	
	
	
	
}
