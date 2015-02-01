/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package glueps.adaptors.ARbrowsers.adaptors;

import glueps.adaptors.ARbrowsers.service.AuthService;
import glueps.core.model.Deploy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.restlet.data.Form;

/**
*  @author of the original source: Pierre Levy [Copyright (c) 2010-2012 ARTags project owners (see http://www.artags.org)]
*  This is a modification of the ARTags Original code, by Juan A. Muñoz, GSIC-EMIC research group (see www.gsic.uva.es)
*/
public class Utils
{

    public static double getDouble(Form options, String parameter, double def)
    {
        double value = def;
        String sValue = options.getFirstValue(parameter);
        if (sValue != null)
        {
            try
            {
                value = Double.parseDouble(sValue);
            } catch (NumberFormatException e)
            {
            }
        }
        return value;

    }

    public static int getInt(Form options, String parameter, int def)
    {
        int value = def;
        String sValue = options.getFirstValue(parameter);
        if (sValue != null)
        {
            try
            {
                value = Integer.parseInt(sValue);
            } catch (NumberFormatException e)
            {
            }
        }
        return value;

    }
    
    public static String getDecodeAndEncode(Form options, String parameter, String def)
    {
    	String value = def;
    	
	   	String uidOriginal = options.getFirstValue(parameter);
		String uidDecoded = null;
		try {
			if (uidOriginal != null){
				uidDecoded = URLDecoder.decode(uidOriginal,"UTF-8");
			}
			if (uidDecoded != null){
				value = URLEncoder.encode(uidDecoded, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return value;
    }
    
    public static String toISO(String stringUTF8) {
        byte[] latin1;
        String name = null;
		try {
			latin1 = stringUTF8.getBytes("UTF-8");
			name = new String(latin1, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return name;    	
    }
    
    
    
    //Method to get the author of a deploy, if the channel is a filter, and if logout control should be shown
    //Result: HashMap[author, showLogout]
    public static HashMap getAuthor(String getrequri){

    	HashMap value = new HashMap();
    	String author = null;
    	
		String[] geturlparts;				 
		String delimiter = "/";
		geturlparts = getrequri.split(delimiter);
		int index = 0;
		for (int i=0; i < geturlparts.length; i++)
		{
			if (geturlparts[i].equals("deploy") || geturlparts[i].equals("author")) {
				index = i;
			}
		}
		 
		boolean showLogout = true;
		if (getrequri.contains("/filter/")){
				//The channel is a filter
	
			  if (geturlparts[index].equals("deploy")) {
				  String deployid = geturlparts[index + 1];
				  Deploy deploy = AuthService.getDeploybyID(deployid);
				  if (deploy != null){
						  author = deploy.getAuthor();	
						  showLogout = false;
						  
				  }
			  } else if (geturlparts[index].equals("author")){
				  author = geturlparts[index + 1];
				 //Si se filtra por deployID o por autor y éste sólo tiene un deploy, numdeploys=1 para no mostrar logout
				 List<Deploy> deploys = AuthService.getDeploysfromAuthor(author);
				 if (deploys != null){
					 if (deploys.size() == 1) {
						 showLogout = false; 
					 }
				 }
				  
			  }
		}

		value.put("author", author);
		value.put("showLogout", showLogout);
		
    	return value;
    }
    
    public static String encodeURIComponent(String component)   {     
    	String result = null;      
    	
    	try {       
    		result = URLEncoder.encode(component, "UTF-8")   
    			   .replaceAll("\\%28", "(")                          
    			   .replaceAll("\\%29", ")")   		
    			   .replaceAll("\\+", "%20")                          
    			   .replaceAll("\\%27", "'")   			   
    			   .replaceAll("\\%21", "!")
    			   .replaceAll("\\%7E", "~");     
    	} catch (UnsupportedEncodingException e) {    		
    		result = component;     
    	}      
    	
    	return result;   
    }   
 
    
    
    
}
