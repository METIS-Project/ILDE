/**
 * Copyright 2005-2011 Noelios Technologies.
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL 1.0 (the
 * "Licenses"). You can select the license that you prefer but you may not use
 * this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0.html
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1.php
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1.php
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0.php
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.noelios.com/products/restlet-engine
 * 
 * Restlet is a registered trademark of Noelios Technologies.
 */

package org.restlet.test.ext.velocity;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import java.util.TreeMap;

import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.engine.io.BioUtils;
import org.restlet.ext.velocity.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.test.RestletTestCase;

/**
 * Test case for the Velocity extension.
 * 
 * @author Jerome Louvel
 */
public class VelocityTestCase extends RestletTestCase {

    public void testRepresentationTemplate() throws Exception {
        // Create a temporary directory for the tests
        File testDir = new File(System.getProperty("java.io.tmpdir"),
                "VelocityTestCase");
        testDir.mkdir();

        // Create a temporary template file
        File testFile = File.createTempFile("test", ".vm", testDir);
        FileWriter fw = new FileWriter(testFile);
        fw.write("Value=$value");
        fw.close();

        Map<String, Object> map = new TreeMap<String, Object>();
        map.put("value", "myValue");

        // Representation approach
        Reference ref = LocalReference.createFileReference(testFile);
        ClientResource r = new ClientResource(ref);
        Representation templateFile = r.get();
        TemplateRepresentation tr = new TemplateRepresentation(templateFile,
                map, MediaType.TEXT_PLAIN);
        final String result = tr.getText();
        assertEquals("Value=myValue", result);

        // Clean-up
        BioUtils.delete(testFile);
        BioUtils.delete(testDir, true);
    }

    public void testStandardTemplate() throws Exception {
        // Create a temporary directory for the tests
        final File testDir = new File(System.getProperty("java.io.tmpdir"),
                "VelocityTestCase");
        testDir.mkdir();

        // Create a temporary template file
        final File testFile = File.createTempFile("test", ".vm", testDir);
        final FileWriter fw = new FileWriter(testFile);
        fw.write("Value=$value");
        fw.close();

        final Map<String, Object> map = new TreeMap<String, Object>();
        map.put("value", "myValue");

        // Standard approach
        final TemplateRepresentation tr = new TemplateRepresentation(testFile
                .getName(), map, MediaType.TEXT_PLAIN);
        tr.getEngine().setProperty("file.resource.loader.path",
                testDir.getAbsolutePath());
        final String result = tr.getText();
        assertEquals("Value=myValue", result);

        // Clean-up
        BioUtils.delete(testFile);
        BioUtils.delete(testDir, true);
    }
}
