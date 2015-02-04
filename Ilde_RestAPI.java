package com.prolix.editor.ilde.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
//import java.io.StringReader;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.HttpVersion;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.HttpMultipartMode;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//import org.w3c.dom.bootstrap.DOMImplementationRegistry;
//import org.w3c.dom.ls.DOMImplementationLS;
//import org.w3c.dom.ls.LSSerializer;
//import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Created on 17.05.2013
 * 
 * @author Rizwan Uppal
 */
public class Ilde_RestAPI {
	
	
	private String loginToken="";
		
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
	
	public String login(String usr, String pw)
	{
		String tokenXML="";
		String responseStatus="";
		String xmlDoc="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<login>\n<username>"+usr+"</username>\n<password>"+pw+"</password>\n</login>";
		
		BufferedReader responseInput;
		
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		String url="http://ilde.upf.edu/services/rest/login"; 
		HttpPost postRequest = new HttpPost(url);
		
		try
		{
			StringEntity inputXML = new StringEntity(xmlDoc);
			inputXML.setContentType("text/xml");
			postRequest.setEntity(inputXML);
			
			////System.err.println("Sending XML to "+url+"\n");
			//System.out.println(xmlDocument);	
			HttpResponse response = httpClient.execute(postRequest);
			////System.err.println("Login Request Sent Successfully");
			//System.out.print("Response from "+url+" \n"+response.getStatusLine()+"\n");
			
			responseStatus=response.getStatusLine().toString();
			
			if(responseStatus.equals("HTTP/1.1 200 OK"))
			{
				String inputLine ;
				responseInput = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				while ((inputLine = responseInput.readLine()) != null) 
				{
					//System.out.println(inputLine);
					if(inputLine.startsWith("<token>")&& inputLine.endsWith("</token>"))
						tokenXML=inputLine;
				}
				responseInput.close();  
			}
			else
			{
				//JOptionPane.showMessageDialog(null, responseStatus, "Failed to Login ILDE..", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		catch( Throwable t )
		{
			t.printStackTrace( System.out );
			JOptionPane.showMessageDialog(null, t.getMessage(), "Error Login ILDE..", JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			httpClient.getConnectionManager().shutdown();
		}
		
		if(tokenXML.startsWith("<token>"))
		{
			
			tokenXML = tokenXML.substring(7,tokenXML.indexOf("</token>"));
			
			//System.out.println("Token : "+tokenXML);
			loginToken=tokenXML;
		}
		
		return responseStatus;
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

	public String uploadNewLdS(String binFile, String imsldZip, String propertiesXML, String responceXML)
	{
		String responseStr="";
		String id="";
		
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
				//JOptionPane.showMessageDialog(null, "Attached Zip", "Attached Zip", JOptionPane.ERROR_MESSAGE);
			}
			FileBody fb_propertiesXSD = new FileBody(new File(propertiesXML));
			requestEntity.addPart("properties", fb_propertiesXSD);
			 
			postRequest.setEntity( requestEntity );
			postRequest.addHeader("Authorization", "ldshake "+loginToken);
			HttpResponse response = httpClient.execute(postRequest);
			
			////System.err.println("Sent Successfully");
			////System.err.print("\n<<Response from  "+url+">>\nStatus : ");
			//System.out.print(""+response.getStatusLine()+"\n"); 
			responseStr=response.getStatusLine().toString();
			
			if(responseStr.equals("HTTP/1.1 200 OK"))
			{
				////System.err.println("Response XML Document");
				File f_propertiesXML = new File(responceXML);
			
                FileOutputStream fop = new FileOutputStream(f_propertiesXML);
                OutputStreamWriter bufferedWriter = new OutputStreamWriter( fop, "UTF8");
				
				StringBuffer sb = new StringBuffer();
				String aux = "";
				
				responseInput = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				while ((aux = responseInput.readLine()) != null) 
				{
					sb.append(aux);
					//System.out.println(inputLine);
					//JOptionPane.showMessageDialog(null, inputLine, "New LdS Upload Responce XML", JOptionPane.ERROR_MESSAGE);
				}
				responseInput.close();  
				bufferedWriter.write(encodeXMLEscapeChar(sb));
				bufferedWriter.close();
				fop.close();
				
				id = getIDFromXML(responceXML);
				updateXMLId(propertiesXML, id);
			}
			else
			{
				JOptionPane.showMessageDialog(null, response.getStatusLine().toString(), "Network error, failed to export to ILDE...\nPlease check your internet connection and try again", JOptionPane.ERROR_MESSAGE);
			}
		}
		catch( Throwable t )
		{
			t.printStackTrace( System.out );
			JOptionPane.showMessageDialog(null, t.getMessage(), "Error Connecting ILDE..", JOptionPane.ERROR_MESSAGE);
		}
		
		return id;
	}
	
	
	private String getIDFromXML(String xmlFileName)
	{
		File xmlFile = new File(xmlFileName);
		if(xmlFile.exists()) 
		{ 
			//JOptionPane.showMessageDialog(null, "Geting ID", "Geting ID", JOptionPane.ERROR_MESSAGE);
			try {
				String filepath = xmlFileName;
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(filepath);
		 
				Node rootElement = doc.getElementsByTagName("lds").item(0);
		 
		 
				// loop the rootElement child node
				NodeList list = rootElement.getChildNodes();
		 
				for (int i = 0; i < list.getLength(); i++) 
				{
		                   Node node = list.item(i);
		 
				   if ("id".equals(node.getNodeName())) 
				   {
					   //JOptionPane.showMessageDialog(null, node.getTextContent(), node.getTextContent(), JOptionPane.ERROR_MESSAGE);
					   if(node.getTextContent()!=null || !node.getTextContent().trim().equals("0") ||!node.getTextContent().trim().equals(""))
						   return node.getTextContent().trim();
				   }
				}
		 
			   } catch (ParserConfigurationException pce) 
			   {
				   pce.printStackTrace();
			   } catch (IOException ioe) 
			   {
				   ioe.printStackTrace();
			   } catch (SAXException sae) 
			   {
				   sae.printStackTrace();
			   }
		}
		return null;
	}
	
	private String updateXMLId(String xmlFileName, String id)
	{
		File xmlFile = new File(xmlFileName);
		if(xmlFile.exists()) 
		{ 
			//JOptionPane.showMessageDialog(null, "Updating ID", "Updating ID", JOptionPane.ERROR_MESSAGE);
			try {
				String filepath = xmlFileName;
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(filepath);
		 
				Node staff = doc.getElementsByTagName("lds").item(0);
		 
				NodeList list = staff.getChildNodes();
		 
				for (int i = 0; i < list.getLength(); i++) 
				{
		                   Node node = list.item(i);
		 
				   if ("id".equals(node.getNodeName())) 
				   {
					   
					   if(node.getTextContent().trim().equals("0"))
					   {
						   //JOptionPane.showMessageDialog(null, node.getTextContent(), node.getTextContent(), JOptionPane.ERROR_MESSAGE);
						   node.setTextContent(id);
					   }
				   }
				}
		 
				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(filepath));
				transformer.transform(source, result);
		 
				System.out.println("Done");
		 
			   } catch (ParserConfigurationException pce) 
			   {
				   pce.printStackTrace();
			   } catch (TransformerException tfe) 
			   {
				   tfe.printStackTrace();
			   } catch (IOException ioe) 
			   {
				   ioe.printStackTrace();
			   } catch (SAXException sae) 
			   {
				   sae.printStackTrace();
			   }
		}
		return null;
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

	
	public String getLdSEditorList(String urlArg, String ldslistXSD)
	{
		String responseStr="";
		BufferedReader responseInput;
		
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		String url=""; 
		
		if(urlArg.equals("EditableLdSList"))
			url="http://ilde.upf.edu/services/rest/ldseditorlist";
		else
			url=urlArg;
		
		HttpGet getRequest   = new HttpGet( url );
		
		try
		{
			System.out.println("Setting Authorization : "+ "ldshake "+loginToken);
			getRequest.addHeader("Authorization", "ldshake "+loginToken);
			HttpResponse response = httpClient.execute(getRequest);
			//System.err.println("\nRequest Sent to "+url+" Successfully");

			//System.err.print("<<Response from "+url+">>\nStatus : ");
			System.out.print("Response : "+response.getStatusLine()+"\n");
			
			responseStr=response.getStatusLine().toString();
			
			if(response.getStatusLine().toString().equals("HTTP/1.1 200 OK"))
			{
				//System.err.println("Response XML Document");
				File f_propertiesXML = new File(ldslistXSD);
                
                FileOutputStream fop = new FileOutputStream(f_propertiesXML);
                OutputStreamWriter bufferedWriter = new OutputStreamWriter( fop, "UTF8");
				
				responseInput = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				//System.out.println(response.getEntity().getContent().toString());
				int countLDS=0;
				
				StringBuffer sb = new StringBuffer();
				String aux = "";
				
				while ((aux = responseInput.readLine()) != null) 
				{
					countLDS+=aux.split("<lds><id>").length-1;				
					sb.append(aux);
				}
				responseInput.close(); 
				
				bufferedWriter.write(encodeXMLEscapeChar(sb));
				//System.out.println("************** Total LdS Recived : "+countLDS+" ****************");
				
				bufferedWriter.close();
				fop.close();
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
	
	/*public String formatXML(String xml) {

        try {
            final InputSource src = new InputSource(new StringReader(xml));
            final Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src).getDocumentElement();
            final Boolean keepDeclaration = Boolean.valueOf(xml.startsWith("<?xml"));

            //May need this: System.setProperty(DOMImplementationRegistry.PROPERTY,"com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl");

            final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            final LSSerializer writer = impl.createLSSerializer();

            writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE); // Set this to true if the output needs to be beautified.
            writer.getDomConfig().setParameter("xml-declaration", keepDeclaration); // Set this to true if the declaration is needed to be outputted.

            return writer.writeToString(document);
        } catch (Exception e) 
        {
            throw new RuntimeException(e);
        }
    }*/
	
	public String encodeXMLEscapeChar(StringBuffer content)
	{
		
		for (int i = -1; (i = content.indexOf("&", i + 1)) != -1; ) 
		{
			//System.out.println("Index : "+i);
			//System.out.println("Size Before : "+content.length());
			//System.out.println("String Before : "+content);
			if(i>=content.length()-6)
			{
				content.deleteCharAt(i);
				content.insert(i, "&amp;");
				//content.append();
			}
			else
			if(!content.substring(i, i+5).equals("&amp;") )
				//System.out.println(i+" : "+fileContent.substring(i, i+5));
			if(!content.substring(i, i+4).equals("&gt;"))
				//System.out.println(i+" : "+fileContent.substring(i, i+4));
			if(!content.substring(i, i+4).equals("&lt;"))
				//System.out.println(i+" : "+fileContent.substring(i, i+4));
			if(!content.substring(i, i+6).equals("&quot;"))
				//System.out.println(i+" : "+fileContent.substring(i, i+6));
			if(!content.substring(i, i+5).equals("&apos;"))
			{
				//System.out.println(fileContent.substring(0, i)+"&amp;"+fileContent.substring(i+1, fileContent.length()));
				//System.out.println(i+" : "+content.substring(i, i));
				content.deleteCharAt(i);
				content.insert(i, "&amp;");
				
				//content.replace(i, i+1, "&amp;");//=fileContent.substring(0, i)+"&amp;"+fileContent.substring(i+1, fileContent.length());	
			}
			//System.out.println("String After : "+content);
			//System.out.println("Size After : "+content.length());
			//System.out.println("\n\n");
		}
		return content.toString();
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
	
		public String getLdSViewList(String ldslistXSD)
		{
			return getLdSEditorList("http://ilde.upf.edu/services/rest/ldsviewlist", ldslistXSD);
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
		
		public String uploadUpdatedLds(String binFile, String imsldZip, String propertiesXML, String responceXML)
		{
			String responseStr="";
			//String id="";
			
			BufferedReader responseInput;
			
			String ldS_ID = getIDFromXML(propertiesXML);
			
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			String url="http://ilde.upf.edu/services/rest/lds/"+ldS_ID;
			//JOptionPane.showMessageDialog(null, "http://ilde.upf.edu/services/rest/lds/"+ldS_ID, "http://ilde.upf.edu/services/rest/lds/"+ldS_ID, JOptionPane.ERROR_MESSAGE);
			//System.out.println("http://ilde.upf.edu/services/rest/lds/"+ldS_ID);
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
					//JOptionPane.showMessageDialog(null, "Attached Zip", "Attached Zip", JOptionPane.ERROR_MESSAGE);
				}
				FileBody fb_propertiesXSD = new FileBody(new File(propertiesXML));
				requestEntity.addPart("properties", fb_propertiesXSD);
				 
				postRequest.setEntity( requestEntity );
				postRequest.addHeader("Authorization", "ldshake "+loginToken);
				HttpResponse response = httpClient.execute(postRequest);
				
				////System.err.println("Sent Successfully");
				////System.err.print("\n<<Response from  "+url+">>\nStatus : ");
				//System.out.print(""+response.getStatusLine()+"\n"); 
				responseStr=response.getStatusLine().toString();
				
				if(responseStr.equals("HTTP/1.1 200 OK"))
				{
					////System.err.println("Response XML Document");
					File f_propertiesXML = new File(responceXML);
					FileWriter fw_propertiesXML = new FileWriter(f_propertiesXML);
	                BufferedWriter bw_propertiesXML = new BufferedWriter(fw_propertiesXML);
	                
					String inputLine ;
					
					responseInput = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					while ((inputLine = responseInput.readLine()) != null) 
					{
						bw_propertiesXML.write(inputLine);
						bw_propertiesXML.newLine();
						//System.out.println(inputLine);
						//JOptionPane.showMessageDialog(null, inputLine, "New LdS Upload Responce XML", JOptionPane.ERROR_MESSAGE);
					}
					responseInput.close();  
					bw_propertiesXML.close();
					fw_propertiesXML.close();
					
					//String id = getIDFromXML(responceXML);
					//setIDinXML(propertiesXML, id);
				}
				else
				{
					JOptionPane.showMessageDialog(null, response.getStatusLine().toString(), "Network error, failed to export to ILDE...\nPlease check your internet connection and try again", JOptionPane.ERROR_MESSAGE);
				}
			}
			catch( Throwable t )
			{
				t.printStackTrace( System.out );
				JOptionPane.showMessageDialog(null, t.getMessage(), "Error Connecting ILDE..", JOptionPane.ERROR_MESSAGE);
			}
			finally
			{
				httpClient.getConnectionManager().shutdown();
			}
			
			return getIDFromXML(responceXML);
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
		
	
		public String getLdSData(String ldS_ID, String binFile)
		{
			String responseStr="";
			
			File file = new File(binFile);
			String url="http://ilde.upf.edu/services/rest/lds/"+ldS_ID+"/data"; 
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			get.addHeader("Authorization", "ldshake "+loginToken);
			
			try
			{
				HttpResponse response = httpClient.execute(get);
				HttpEntity entity = response.getEntity();
			
				InputStream ip = entity.getContent();
				
				FileOutputStream fos = new FileOutputStream(file);
				int zeichen =0;
				while((zeichen = ip.read())>=0)
				{
					fos.write(zeichen);
				}
				fos.flush();
				fos.close();
				ip.close();
				
			}catch (ClientProtocolException e)
			{
				JOptionPane.showMessageDialog(null, e.getMessage(), "Erorr 1", JOptionPane.ERROR_MESSAGE);
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, e.getMessage(), "Erorr 2", JOptionPane.ERROR_MESSAGE);
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

		public String getLdSProperties(String ldS_ID, String propertiesXML)
		{
			String responseStr="";
			HttpClient httpClient = new DefaultHttpClient();
			
			try
			{
				httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
				
				String url="http://ilde.upf.edu/services/rest/lds/"+ldS_ID+"/properties";	
				//JOptionPane.showMessageDialog(null, url, "URL", JOptionPane.ERROR_MESSAGE);
				
				HttpGet getRequest = new HttpGet(url);
			 
				getRequest.addHeader("Authorization", "ldshake "+loginToken);
				HttpResponse response = httpClient.execute(getRequest);
			
				////System.err.println("Sent Successfully");
				////System.err.print("\n<<Response from  "+url+">>\nStatus : ");
				//System.out.print(""+response.getStatusLine()+"\n"); 
				responseStr=response.getStatusLine().toString();
			
				if(responseStr.equals("HTTP/1.1 200 OK"))
				{
					File f_propertiesXML = new File(propertiesXML);
                
					FileOutputStream fop = new FileOutputStream(f_propertiesXML);
					OutputStreamWriter bufferedWriter = new OutputStreamWriter( fop, "UTF8");
				
					StringBuffer sb = new StringBuffer();
					String aux = "";
				
					BufferedReader responseInput = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					
					while ((aux = responseInput.readLine()) != null) 
					{
						sb.append(aux);
						//System.out.println(inputLine);
						//JOptionPane.showMessageDialog(null, inputLine, "New LdS Upload Responce XML", JOptionPane.ERROR_MESSAGE);
					}
					responseInput.close();  
				
					//JOptionPane.showMessageDialog(null, sb, "XML String", JOptionPane.ERROR_MESSAGE);
					bufferedWriter.write(encodeXMLEscapeChar(sb));
				
					bufferedWriter.close();
					fop.close();
				}
				else
				{
					JOptionPane.showMessageDialog(null, response.getStatusLine().toString(), "Network error, failed to export to ILDE...\nPlease check your internet connection and try again", JOptionPane.ERROR_MESSAGE);
				}
			}
			catch( IOException e )
			{
				e.printStackTrace( System.out );
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error Connecting ILDE..", JOptionPane.ERROR_MESSAGE);
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
				////System.err.println("Ping Rquest Sent Successfully");

				////System.err.print("\n<<Response from  "+url+">>\nStatus : ");
				//System.out.print(""+response.getStatusLine()+"\n");
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
	
	public String logout()
	{
		String responseStr="";
		
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		String url="http://ilde.upf.edu/services/rest/logout"; 
		HttpGet getRequest = new HttpGet( url );
		
		try
		{
			
			getRequest.addHeader("Authorization", "ldshake "+loginToken);
			HttpResponse response = httpClient.execute(getRequest);
			////System.err.println("\nLogout Request Sent Successfully");
			////System.err.print("<<Response from  "+url+">>\nStatus : ");
			////System.out.print(""+response.getStatusLine()+"\n");
			responseStr=response.getStatusLine().toString();
			loginToken="";
			
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

	public String getToken(){
		return loginToken;
	}
}
