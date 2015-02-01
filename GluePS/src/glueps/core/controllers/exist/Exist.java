package glueps.core.controllers.exist;

import java.io.File;

import java.util.ArrayList;

import javax.xml.transform.OutputKeys;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.modules.XMLResource;

/****
 * <p>
 *   Clase encargada de contener los metodos para crear la conexión,guardar archivos y recuperar de la base 
 *   de datos eXist
 * </p>
 * @author jcalvarez
 *
 */
	public class Exist {
	//Parametros para realizar la conexión	
	//TODO Put these on a config file! make the constructor receive them
	static String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/";
	static String USER= "admin";
	static String PASSWORD="admin";

	private String uri = URI;
	private String user = USER;
	private String password = PASSWORD;
	
	public Exist(){		
	}
	
	public Exist(String uri, String user, String password){
		this.uri = uri;
		this.user = user;
		this.password = password;
	}
	
	/**
	 * Devuelve la conexion al a coleci�n de eXist que se le indique por parametro.
	 * @param collection
	 * @return
	 * @throws Exception
	 */
	public Collection existConection(String collection) throws Exception{
		String driver = "org.exist.xmldb.DatabaseImpl";  
		Class cl = Class.forName(driver);  
		Database database = (Database) cl.newInstance();  
		DatabaseManager.registerDatabase(database); 
		Collection col = DatabaseManager.getCollection(uri+"db/"+collection,user,password);
		return col;
	}
	
	
	
	/**
	 * <p>Función encargada de obtener archivos de la base de datos eXist</p>
	 * @param collection: Nombre de la colección de la cual queremos recuperar un archivo.
	 * @param document : Nombre del documento con extensión .xml que queremos recuperar.
	 * @return
	 * @throws Exception
	 */
	public String getXML(String collection, String document) throws Exception {  
		String driver = "org.exist.xmldb.DatabaseImpl";  
		Class cl = Class.forName(driver);  
		Database database = (Database) cl.newInstance();  
		DatabaseManager.registerDatabase(database);  
		Collection col = DatabaseManager.getCollection(uri+"db/"+collection,user,password);
		col.setProperty(OutputKeys.INDENT, "yes");
		XMLResource res = (XMLResource)col.getResource(document);
		if((res == null)||(res.getContent() ==null)) { 
        System.out.println("documento no encontrado o no se puede parsear");
		return null;
		}
		else{
		return res.getContent().toString();
		}
	}  
	
	/**
	 * <p> Función encarga de almacenar archivos xml en la base de datos eXist</p>
	 * @param collection : Nombre de la coleccion (repositorio)donde se va a almacenar.
	 * @param data: Contenido del fichero a almacenar.
	 * @param xmlName : Nombre con que se va a almacenar en la colección.
	 * @throws Exception
	 */
	public void setXML(String collection,File data, String xmlName) throws Exception{
	        String driver = "org.exist.xmldb.DatabaseImpl";
	        Class cl = Class.forName(driver);
	        Database database = (Database)cl.newInstance();
	        DatabaseManager.registerDatabase(database);
	        Collection col = DatabaseManager.getCollection(uri+"db/"+collection,user,password);	     
	        XMLResource document = (XMLResource)col.createResource(xmlName, "XMLResource");	
	        document.setContent(data);	       
	        col.storeResource(document);	    
	}		
	
	/**
	 * <p> Función encarga de eliminar archivos xml en la base de datos eXist</p>
	 * @param collection : Nombre de la coleccion (repositorio)donde se va a almacenar.
	 * @param data: Contenido del fichero a almacenar.
	 * @param xmlName : Nombre con que se va a almacenar en la colecci�n.
	 * @throws Exception
	 */
	public void deleteXML(String collection, String xmlName) throws Exception{
	        String driver = "org.exist.xmldb.DatabaseImpl";
	        Class cl = Class.forName(driver);
	        Database database = (Database)cl.newInstance();
	        DatabaseManager.registerDatabase(database);
	        Collection col = DatabaseManager.getCollection(uri+"db/"+collection,user,password);	
			
	        //We get the resource from the DB
	        XMLResource res = (XMLResource)col.getResource(xmlName);
			if((res == null)||(res.getContent() ==null)) { 
				System.out.println("documento no encontrado o no se puede parsear");
				throw new Exception("documento no encontrado o no se puede parsear");
			}
			else{
				col.removeResource(res);
				System.out.println("deleted resource "+xmlName);
			}

	}		
	
}