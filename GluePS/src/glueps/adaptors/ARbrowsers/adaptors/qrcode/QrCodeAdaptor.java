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
