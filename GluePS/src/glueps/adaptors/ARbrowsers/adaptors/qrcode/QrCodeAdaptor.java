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

package glueps.adaptors.ARbrowsers.adaptors.qrcode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import glueps.adaptors.ARbrowsers.adaptors.Constants;
import glueps.core.model.ToolInstance;

public class QrCodeAdaptor {

	public static String getQrCodeUrl(String location, String callerUserName){
		 String url = null;
		try {
			url = "http://qrickit.com/api/qr?qrsize=200&d=" + location + "?callerUser=" + URLEncoder.encode(callerUserName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 return url;
	}
	
	public static String getQrCodeUrl(String location){
		 String url = "http://qrickit.com/api/qr?qrsize=200&d=" + location;

		 return url;
	}
	 
	public static void setQrCodePosition(ToolInstance toolInst){
		if (toolInst != null){
			if (toolInst.getPositionType() != null) {
				if (toolInst.getLocation() != null && toolInst.getPositionType().equals(Constants.POS_TYPE_QRCODE)){
					String location = toolInst.getLocation().toString();
					String url = "http://qrickit.com/api/qr?qrsize=200&d=" + location;
					toolInst.setPosition(url);
				}
			}

		}

	}
	
	public static void setQrCodePosition(ToolInstance toolInst, String callerUserName){
		if (toolInst != null){
			if (toolInst.getPositionType() != null) {
				if (toolInst.getLocation() != null && toolInst.getPositionType().equals(Constants.POS_TYPE_QRCODE)){
					String location = toolInst.getLocation().toString();
					String url = null;
					try {
						url = "http://qrickit.com/api/qr?qrsize=200&d=" + location + "?callerUser=" + URLEncoder.encode(callerUserName, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					toolInst.setPosition(url);
				}
			}

		}

	}
	
}
