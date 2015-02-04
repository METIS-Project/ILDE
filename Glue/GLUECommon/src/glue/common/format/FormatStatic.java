/**
 This file is part of GLUECommon.

 GLUECommon is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 GLUECommon is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use GLUECommon for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when GLUECommon is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute GLUECommon and/or modify it under the
   terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>
 ]
*/
package glue.common.format;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.restlet.ext.xml.DomRepresentation;
import org.w3c.dom.Node;

/**
 * Aggregator of static common info and methods
 *
 * @author  David A. Velasco
 * @version 2012092501
 * @package glue.common.format
 */
public class FormatStatic {

	/**
	 * GLUE namespace constant
	 */
	protected static String GLUE_NAMESPACE = "http://gsic.uva.es/glue/1.0";
	
	/**
	 * Atom namespace constant
	 */
	protected static String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";
	
	/**
	 * GLUE namespace prefix constant
	 */
	protected static String GLUE_PREFIX = "glue";

	
	/** Name of GSIC */
	public static final String GSIC_NAME = "Group of Intelligent and Cooperative Systems (GSIC)";

	
	/**
	 * Utility method to dump a DomRepresentation object to standard output
	 * @param node	DomRepresentation object
	 */
	public static void printXML(DomRepresentation node) {
		try {
			node.write(System.out);
			System.out.flush();
		} catch (IOException e) {
			System.out.println("Weird issue shile trying to print XML: " + e.getMessage());
		}
	}
	
	
	/**
	 * Utility method to get the textual representation of a given org.w3c.dom.Node object
	 * @param node	Node	DOM Node to transform
	 */
	public static String xmlToString(Node node) throws TransformerConfigurationException, TransformerException {
        //try {
            Source source = new DOMSource(node);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
        /*} catch (TransformerConfigurationException e) {
        	throw new ResourceException();
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;*/		
	}
	
}
