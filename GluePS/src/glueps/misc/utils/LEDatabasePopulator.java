package glueps.misc.utils;
import glueps.adaptors.vle.moodle.MoodleAdaptor;
import glueps.core.controllers.exist.Exist;
import glueps.core.model.LearningEnvironment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;



// This class creates XMLs with LE objects and populates the DB with them
public class LEDatabasePopulator {

	//DB Parameters
	private static final String dbUrl="xmldb:exist://localhost:8081/exist/xmlrpc/";
	private static final String dbUser="admin";
	private static final String dbPassword="lprisan";
	
	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {

		//TODO: Instantiate the necessary LE objects
		LearningEnvironment le1 = new LearningEnvironment("1", "Moodle de pruebas en la nube", "Moodle", null, new URL("http://157.88.130.126/moodle19/"),null);
		LearningEnvironment le2 = new LearningEnvironment("2", "Moodle para talleres en la nube", "Moodle", null, new URL("http://157.88.130.206/moodle/"),null);
		LearningEnvironment le3 = new LearningEnvironment("3", "Moodle de pruebas de Juan", "Moodle", null, new URL("http://10.0.106.6/moodle19/"),null);
		LearningEnvironment le4 = new LearningEnvironment("4", "Moodle + GLUE en pandora", "Moodle", null, new URL("http://pandora.tel.uva.es/pruebamoodle/"),null);
		
		//Ask the VLE adaptor for the internal tools of the VLE
		//TODO: For now, only Moodle. Someday, we should dynamically load the proper VLE adaptor
		MoodleAdaptor moodle = new MoodleAdaptor();
		le1.setInternalTools(moodle.getInternalTools());
		le2.setInternalTools(moodle.getInternalTools());
		le3.setInternalTools(moodle.getInternalTools());
		le4.setInternalTools(moodle.getInternalTools());
		
		//TODO: Marshall to an XML
		generateXML(le1);
		generateXML(le2);
		generateXML(le3);
		generateXML(le4);
		
		//TODO: Insert the XML into the DB
		insertDB(le1);
		insertDB(le2);
		insertDB(le3);
		insertDB(le4);
		
		System.out.println("Finished the population process");
		
	}

	private static void insertDB(LearningEnvironment le1) {
		//Get the DB parameters from the restlet application
		Exist exist=new Exist(dbUrl, dbUser, dbPassword);
		
		File xmlSource =new File(le1.getId()+".xml");
		
		try {
			exist.setXML("LearningEnvironments",xmlSource,le1.getId()+".xml" );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Elimino el archivo, una vez que lo he subido a la base de datos
		xmlSource.delete();
		
	}

	private static void generateXML(LearningEnvironment le){
		
		//We create the LE XML
		FileOutputStream fichero;
		JAXBContext context;
		try {
			fichero = new FileOutputStream(le.getId()+".xml");
			context = JAXBContext.newInstance(LearningEnvironment.class);
			  Marshaller m = context.createMarshaller();	
			  //Esta Propiedad formatea el código del XML
	          m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
	          m.marshal(le, fichero);
		      fichero.close();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
}
