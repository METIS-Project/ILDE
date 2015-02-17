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

package glueps.adaptors.ARbrowsers.adaptors.junaio;

import glueps.adaptors.ARbrowsers.adaptors.Constants;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.util.Series;

import org.apache.commons.codec.binary.Base64;

/**
 *
 *  @author of the original source: Pierre Levy [Copyright (c) 2010-2012 ARTags project owners (see http://www.artags.org)]
 *  This is a modification of the ARTags Original code, by Juan A. Muñoz, GSIC-EMIC research group (see www.gsic.uva.es)
 */
public class JunaioUtils
{

    private static final String HEADER_AUTH = "Authorization";
    private static final String HEADER_DATE = "Date";

    public static String getHash(Series<Parameter> headers)
    {
        try
        {
            String headerAuthorisation = headers.getFirstValue(HEADER_AUTH);
            String requestSignature = headerAuthorisation.substring("junaio ".length());
            return new String(Base64.decodeBase64(requestSignature));
        } catch (Exception ex)
        {
            Logger.getLogger(JunaioUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static boolean isAuthorized(Series<Parameter> headers )
    {
        String headerAuthorisation = headers.getFirstValue(HEADER_AUTH);
        //Juan: validaci�n de que funciona
//        System.out.println("headerAuthorisation: " + headerAuthorisation);
//        System.out.println("headerAuthorisation.indexOf(\"junaio\"): " + headerAuthorisation.indexOf("junaio"));

        return ((headerAuthorisation != null) && (headerAuthorisation.indexOf("junaio") == 0));

    }

    public static boolean isValidSignature(String uri, Series<Parameter> headers , String key )
    {
        String calculatedHash = calculateHash(uri, headers , key );
        String hash = getHash( headers );
        
        //Juan: validaci�n de que funciona
//        String s = new Boolean(calculatedHash.equals( hash )).toString();
//        System.out.println("La comprobación del hash es: " + s);
        
        return calculatedHash.equals( hash );

    }

    public static String calculateHash( String uri, Series<Parameter> headers , String key )
    {
    	String queryString = null;
    	String path = null;
		try {
			path = ( new URL(uri) ).getPath();
			queryString = ( new URL(uri) ).getQuery();
			//debug
//			System.out.println("queryString: " + queryString);
//			System.out.println("uri ? queristring: " + uri + "?" + queryString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return buildSignature( key , "GET" , path + "?" + queryString , getDate( headers ));
    }
    

    static String buildSignature(String key, String verb, String uri, String date)
    {
    	//Juan: la redirecci�n a mi m�quina est� quitando el "juanmunoz" de la uri y creo que peta por eso
// //       String signatureString = verb + "\n" + uri + "\n" + "Date: " + date + "\n";
    	String signatureString = verb + "\n" + Constants.configuration().getProperty("junaio.server.redirection", "") + uri + "\n" + "Date: " + date + "\n";
//        //Juan: validaci�n de que funciona
//        System.out.println("signature sin hash: " + signatureString);

        return sha1(key + sha1(key + signatureString));

    }



    private static String getDate(Series<Parameter> headers)
    {
    	return headers.getFirstValue("Date");
    }

    public static String sha1(String src)
    {
        MessageDigest md1 = null;
        try
        {
            md1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        try
        {
            md1.update(src.getBytes("UTF-8"));

        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return hex(md1.digest());
    }
    
    private static String hex(byte[] bytes)
    {
    	final String HEX_DIGITS = "0123456789abcdef";
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++)
        {
            int b = bytes[i] & 0xFF;
            sb.append(HEX_DIGITS.charAt(b >>> 4)).append(HEX_DIGITS.charAt(b & 0xF));
        }
        return sb.toString();
    }
    
    

  
    
}
