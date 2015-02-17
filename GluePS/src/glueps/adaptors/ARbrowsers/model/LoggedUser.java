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

import glueps.adaptors.ARbrowsers.adaptors.Constants;
import glueps.adaptors.ARbrowsers.service.AuthService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;



import com.google.gson.Gson;

/**
 * @author juan, GSIC-EMIC research group (see www.gsic.uva.es)
 * User logged in a deploy, using an AR browser and a movile devide (uid)
 * A user can be logged to a positionType, receiving the corresponding POIs
 * (i.e., geopositioned POIs, or POIs positioned with a tag/marker
 *
 */

public class LoggedUser {

	private String uid;
	protected String username;
	private String deployId;
	private Long timestamp;
	private String positionType;

	
	
	public LoggedUser() {
		// TODO Auto-generated constructor stub
	}

	public LoggedUser(String uid, String username, String deployId, String positionType) {
		this.uid = uid;
		this.username = username;
		this.deployId = deployId;
		this.positionType = positionType;
		this.timestamp = new Date().getTime();
		
		
	}

	public String getPositionType() {
		return positionType;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}

	public Long getTimestamp() {
		return timestamp;
	}
	
	public void resetTimestamp(){
		this.timestamp = new Date().getTime();
	}
	

	

}
