package ildeRestAPI;


import java.io.InputStreamReader;
import java.io.BufferedReader;
//import java.io.InputStream;
//import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.client.HttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.HttpVersion;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.apache.http.entity.mime.content.FileBody;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.HttpMultipartMode;

public class RestAPI {
	
	
/*	
 * ==================================================================================================================================
 * ================================================================================================================================== 
 	POST /services/rest/login
	description:
		Send the teacher credentials to obtain the session token to perform further operations.
	
	body: XML compliant with login.xsd
		
	response:
		body:		XML compliant with token.xsd
		success: 	status => 200 OK
		
		failure: 	status => 400 Bad Request
					status => 401 Unauthorized
					status => 404 Not Found
					status => 500 Internal Server Error
*/
	
	public String login(String xmlDocument)
	{
		String responseStr="";
		BufferedReader responseInput;
		
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		String url="http://ilde.upf.edu/services/rest/login"; 
		HttpPost postRequest = new HttpPost(url);
		
		try
		{
			StringEntity inputXML = new StringEntity(xmlDocument);
			inputXML.setContentType("text/xml");
			postRequest.setEntity(inputXML);
			
			System.err.println("Sending XML to "+url+"\n");
			System.out.println(xmlDocument);	
			HttpResponse response = httpClient.execute(postRequest);
			System.err.println("Login Request Sent Successfully");

			System.out.print("Response from "+url+" \n"+response.getStatusLine()+"\n");
			
			if(response.getStatusLine().toString().equals("HTTP/1.1 200 OK"))
			{
				String inputLine ;
				responseInput = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				while ((inputLine = responseInput.readLine()) != null) 
				{
					System.out.println(inputLine);
					if(inputLine.startsWith("<token>")&& inputLine.endsWith("</token>"))
						responseStr=inputLine;
				}
				responseInput.close();  
			}
			else
			{
				responseStr=response.getStatusLine().toString();
			}
		}
		catch( Throwable t )
		{
			t.printStackTrace( System.out );
		}
		finally
		{
			httpClient.getConnectionManager().shutdown();
		}
		
		return responseStr;
	}
	
	
/*
 * ==================================================================================================================================
 * ==================================================================================================================================	 
	POST /services/rest/newlds
	description:
		Upload a new design and create the LdS.
	
	body: multipart encoded form
		design => binary file
		design_imsld => ZIP file with the imsld design compatible with GLUEPS (optional parameter)
		properties => XML file compliant with properties.xsd, assign 0 as the lds id
		
	response:
		body:		XML compliant with properties.xsd
			
		success: 	status => 200 OK
		failure: 	status => 400 Bad Request
					status => 401 Unauthorized
					status => 500 Internal Server Error
					status => 507 Insufficient Storage
*/

	public String uploadNewLdS(String token, String binFile, String imsldZip, String propertiesXML, String responceXML)
	{
		String responseStr="";
		BufferedReader responseInput;
		
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		String url="http://ilde.upf.edu/services/rest/newlds"; 
		HttpPost postRequest = new HttpPost(url);
		
		try
		{
			//StringBody comment = new StringBody("Filename: " + fileName);
			
			MultipartEntity requestEntity = new MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );
			FileBody fb_binFile = new FileBody(new File(binFile));
			requestEntity.addPart("design", fb_binFile);
			if(!(imsldZip==null || imsldZip.equals("")))
			{
				FileBody fb_imsldZip = new FileBody(new File(imsldZip));
				requestEntity.addPart("design_imsld", fb_imsldZip);
			}
			FileBody fb_propertiesXSD = new FileBody(new File(propertiesXML));
			requestEntity.addPart("properties", fb_propertiesXSD);
			 
			postRequest.setEntity( requestEntity );
			postRequest.addHeader("Authorization", "ldshake "+token);
			HttpResponse response = httpClient.execute(postRequest);
			
			System.err.println("Sent Successfully");
			System.err.print("\n<<Response from  "+url+">>\nStatus : ");
			System.out.print(""+response.getStatusLine()+"\n"); 
			responseStr=response.getStatusLine().toString();
			
			if(response.getStatusLine().toString().equals("HTTP/1.1 200 OK"))
			{
				System.err.println("Response XML Document");
				File f_propertiesXML = new File(responceXML);
				FileWriter fw_propertiesXML = new FileWriter(f_propertiesXML);
                BufferedWriter bw_propertiesXML = new BufferedWriter(fw_propertiesXML);
                
				String inputLine ;
				
				responseInput = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				while ((inputLine = responseInput.readLine()) != null) 
				{
					bw_propertiesXML.write(inputLine);
					bw_propertiesXML.newLine();
					System.out.println(inputLine);
				}
				responseInput.close();  
				bw_propertiesXML.close();
				fw_propertiesXML.close();
			}
		}
		catch( Throwable t )
		{
			t.printStackTrace( System.out );
		}
		
		return responseStr;
	}
	
	
/*
 * ==================================================================================================================================
 * ==================================================================================================================================
	GET /services/rest/ldseditorlist
	description:
		Download a list with all the designs the user is able to edit.
	
	URL parameters:
		tag => string (optional)
		
	response:
		body: XML compliant with ldslist.xsd
			
		success: 	status => 200 OK
		failure: 	status => 400 Bad Request
					status => 401 Unauthorized
					status => 500 Internal Server Error
*/

	public String getLdSEditorList(String token, String urlArg, String ldslistXSD)
	{
		String responseStr="";
		BufferedReader responseInput;
		
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		String url=""; 
		
		if(urlArg==null || urlArg.equals(""))
			url="http://ilde.upf.edu/services/rest/ldseditorlist";
		else
			url=urlArg;
		
		HttpGet getRequest   = new HttpGet( url );
		
		try
		{
			
			System.out.println("Authorization"+ "ldshake "+token);
			getRequest.addHeader("Authorization", "ldshake "+token);
			HttpResponse response = httpClient.execute(getRequest);
			System.err.println("\nRequest Sent to "+url+" Successfully");

			System.err.print("<<Response from "+url+">>\nStatus : ");
			System.out.print(""+response.getStatusLine()+"\n");
			
			responseStr=response.getStatusLine().toString();
			
			if(response.getStatusLine().toString().equals("HTTP/1.1 200 OK"))
			{
				System.err.println("Response XML Document");
				File f_propertiesXML = new File(ldslistXSD);
				FileWriter fw_propertiesXML = new FileWriter(f_propertiesXML);
                BufferedWriter bw_propertiesXML = new BufferedWriter(fw_propertiesXML);
                
				String inputLine ;
				
				responseInput = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				
				int countLDS=0;
				
				while ((inputLine = responseInput.readLine()) != null) 
				{
						
						
						countLDS+=inputLine.split("<lds><id>").length-1;
					
					System.err.println("Hello");
					//System.err.println(inputLine);
					bw_propertiesXML.write(inputLine);
					bw_propertiesXML.newLine();
					System.out.println(inputLine);
				}
				System.out.println("************** Total LDS : "+countLDS+" ****************");
				responseInput.close();  
				bw_propertiesXML.close();
				fw_propertiesXML.close();
			}
		}
		catch( Throwable t )
		{
			t.printStackTrace( System.out );
		}
		finally
		{
			httpClient.getConnectionManager().shutdown();
		}
		
		return responseStr;
	}
	
/*
 * ==================================================================================================================================
 * ==================================================================================================================================
	GET /services/rest/ldsviewlist
	description:
		Download a list with all the designs the user is able view or edit.
	
	URL parameters:
		tag => string (optional)
		
	response:
		body: ldslist.xsd
			
		success: 	status => 200 OK
		failure: 	status => 400 Bad Request
					status => 401 Unauthorized
					status => 500 Internal Server Error
*/
	
		public String getLdSViewList(String token, String ldslistXSD)
		{
			return getLdSEditorList(token, "http://ilde.upf.edu/services/rest/ldsviewlist", ldslistXSD);
		}
		
/*
 * ==================================================================================================================================
 * ==================================================================================================================================
	POST /services/rest/lds/{lds_id}
	description:
		Upload the updated design.
	
	body: multipart encoded form
		design => binary file
		design_imsld => ZIP file with the imsld design compatible with GLUEPS (optional parameter)
		properties => XML file compliant with properties.xsd
		
	response:
		success: 	status => 200 OK
		failure: 	status => 400 Bad Request
					status => 401 Unauthorized
					status => 500 Internal Server Error
					status => 507 Insufficient Storage
*/
		
		public String uploadDesign(String token, String ldS_ID, String binFile, String imsldZip, String propertiesXSD, String propertiesXML)
		{
		String responseStr="";
		
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		String url="http://ilde.upf.edu/services/rest/lds/"+ldS_ID; 
		HttpPost postRequest = new HttpPost(url);
		
		try
		{
			//StringBody comment = new StringBody("Filename: " + fileName);
			
			MultipartEntity requestEntity = new MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );
			FileBody fb_binFile = new FileBody(new File(binFile));
			requestEntity.addPart("design", fb_binFile);
			if(!(imsldZip==null || imsldZip.equals("")))
			{
				FileBody fb_imsldZip = new FileBody(new File(imsldZip));
				requestEntity.addPart("design_imsld", fb_imsldZip);
			}
			FileBody fb_propertiesXSD = new FileBody(new File(propertiesXSD));
			requestEntity.addPart("properties", fb_propertiesXSD);
			 
			postRequest.setEntity( requestEntity );
			postRequest.addHeader("Authorization", "ldshake "+token);
			HttpResponse response = httpClient.execute(postRequest);
			
			System.err.println("Sent Successfully");
			System.err.print("\n<<Response from  "+url+">>\nStatus : ");
			System.out.print(""+response.getStatusLine()+"\n"); 
			responseStr=response.getStatusLine().toString();
		}
		catch( Throwable t )
		{
			t.printStackTrace( System.out );
		}
		
		return responseStr;
	}
					
/*
 * ==================================================================================================================================
 * ==================================================================================================================================
	GET /services/rest/lds/{lds_id}/data
	description:
		Download the document savefile.
	
	response:
		body:		binary file
		success: 	status => 200 OK
		failure: 	status => 400 Bad Request
					status => 401 Unauthorized
					status => 500 Internal Server Error
					status => 507 Insufficient Storage
*/
		
	
		public String getLdSData(String token, String ldS_ID, String binFile)
		{
			String responseStr="";
			BufferedReader responseInput;
			
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			String url="http://ilde.upf.edu/services/rest/lds/"+ldS_ID+"/data"; 
			HttpGet getRequest = new HttpGet(url);
			
			try
			{
				getRequest.addHeader("Authorization", "ldshake "+token);
				HttpResponse response = httpClient.execute(getRequest);
				
				System.err.println("Sent Successfully");
				System.err.print("\n<<Response from  "+url+">>\nStatus : ");
				System.out.print(""+response.getStatusLine()+"\n"); 
				responseStr=response.getStatusLine().toString();
				
				if(response.getStatusLine().toString().equals("HTTP/1.1 200 OK"))
				{
					System.err.println("Response XML Document");
					File f_propertiesXML = new File(binFile);
					FileWriter fw_propertiesXML = new FileWriter(f_propertiesXML);
	                BufferedWriter bw_propertiesXML = new BufferedWriter(fw_propertiesXML);
	                
					String inputLine ;
					
					responseInput = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					while ((inputLine = responseInput.readLine()) != null) 
					{
						bw_propertiesXML.write(inputLine);
						bw_propertiesXML.newLine();
						System.out.println(inputLine);
					}
					responseInput.close();  
					bw_propertiesXML.close();
					fw_propertiesXML.close();
				}
			}
			catch( Throwable t )
			{
				t.printStackTrace( System.out );
			}
			
			return responseStr;
		}

/*
 * ==================================================================================================================================
 * ==================================================================================================================================
	GET /services/rest/lds/{lds_id}/properties
	description:
		Download the document properties.
	
	response:
		body:		XML compliant with properties.xsd
		success: 	status => 200 OK
		failure: 	status => 400 Bad Request
					status => 401 Unauthorized
					status => 500 Internal Server Error
					status => 507 Insufficient Storage
*/

		public String getLdSProperties(String token, String ldS_ID, String propertiesXSD)
		{
			String responseStr="";
			BufferedReader responseInput;
			
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			String url="/services/rest/lds/"+ldS_ID+"/properties"; 
			HttpGet getRequest = new HttpGet(url);
			
			try
			{
				getRequest.addHeader("Authorization", "ldshake "+token);
				HttpResponse response = httpClient.execute(getRequest);
				
				System.err.println("Sent Successfully");
				System.err.print("\n<<Response from  "+url+">>\nStatus : ");
				System.out.print(""+response.getStatusLine()+"\n"); 
				responseStr=response.getStatusLine().toString();
				
				if(response.getStatusLine().toString().equals("HTTP/1.1 200 OK"))
				{
					System.err.println("Response XML Document");
					File f_propertiesXML = new File(propertiesXSD);
					FileWriter fw_propertiesXML = new FileWriter(f_propertiesXML);
	                BufferedWriter bw_propertiesXML = new BufferedWriter(fw_propertiesXML);
	                
					String inputLine ;
					
					responseInput = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					while ((inputLine = responseInput.readLine()) != null) 
					{
						bw_propertiesXML.write(inputLine);
						bw_propertiesXML.newLine();
						System.out.println(inputLine);
					}
					responseInput.close();  
					bw_propertiesXML.close();
					fw_propertiesXML.close();
				}
			}
			catch( Throwable t )
			{
				t.printStackTrace( System.out );
			}
			
			return responseStr;
		}
		
/*
 * ==================================================================================================================================
 * ==================================================================================================================================
	POST /services/rest/lds/{lds_id}/ping
	description:
		The desktop app must send this call to ldshake every 30 seconds so other users should wait until the app user finishes editing the design.
	
	response:
		body:
		success: status => 200
		failure: status => 400
*/
		
		public String ping(String token, String ldS_ID)
		{
			String responseStr="";
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			String url="http://ilde.upf.edu/services/rest/lds/"+ldS_ID+"/ping"; 
			HttpPost request = new HttpPost(url);
			
			try
			{
				request.addHeader("Authorization", "ldshake "+token);
				HttpResponse response = httpClient.execute(request);
				System.err.println("Ping Rquest Sent Successfully");

				System.err.print("\n<<Response from  "+url+">>\nStatus : ");
				System.out.print(""+response.getStatusLine()+"\n");
				responseStr=response.getStatusLine().toString();
				
			}
			catch( Throwable t )
			{
				t.printStackTrace( System.out );
			}
			finally
			{
				httpClient.getConnectionManager().shutdown();
			}
			
			return responseStr;
		}
		
/*
 * ==================================================================================================================================
 * ==================================================================================================================================
	GET /services/rest/logout
	description:
		Destroy the session token.
	
	response:
		success: 	status => 200 OK
		failure: 	status => 400 Bad Request
					status => 401 Unauthorized
					status => 500 Internal Server Error
*/
	
	public String logout(String token)
	{
		String responseStr="";
		
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		String url="http://ilde.upf.edu/services/rest/logout"; 
		HttpGet getRequest   = new HttpGet( url );
		
		try
		{
			
			getRequest.addHeader("Authorization", "ldshake "+token);
			HttpResponse response = httpClient.execute(getRequest);
			System.err.println("\nLogout Request Sent Successfully");

			System.err.print("<<Response from  "+url+">>\nStatus : ");
			System.out.print(""+response.getStatusLine()+"\n");
			responseStr=response.getStatusLine().toString();
			
		}
		catch( Throwable t )
		{
			t.printStackTrace( System.out );
		}
		finally
		{
			httpClient.getConnectionManager().shutdown();
		}
		
		return responseStr;
	}

	/**
	 * @param args
	 */
	
	
	

}

