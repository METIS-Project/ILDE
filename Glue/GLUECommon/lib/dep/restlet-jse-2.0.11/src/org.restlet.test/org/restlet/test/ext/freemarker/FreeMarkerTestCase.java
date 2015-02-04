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

package org.restlet.test.ext.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import java.util.TreeMap;

import org.restlet.data.MediaType;
import org.restlet.engine.io.BioUtils;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.test.RestletTestCase;

import freemarker.template.Configuration;

/**
 * Unit test for the FreeMarker extension.
 * 
 * @author Jerome Louvel
 */
public class FreeMarkerTestCase extends RestletTestCase {
    public static void main(String[] args) {
        try {
            new FreeMarkerTestCase().testTemplate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testTemplate() throws Exception {
        // Create a temporary directory for the tests
        final File testDir = new File(System.getProperty("java.io.tmpdir"),
                "FreeMarkerTestCase");
        testDir.mkdir();

        // Create a temporary template file
        final File testFile = File.createTempFile("test", ".ftl", testDir);
        final FileWriter fw = new FileWriter(testFile);
        fw.write("Value=${value}");
        fw.close();

        final Configuration fmc = new Configuration();
        fmc.setDirectoryForTemplateLoading(testDir);
        final Map<String, Object> map = new TreeMap<String, Object>();
        map.put("value", "myValue");

        final String result = new TemplateRepresentation(testFile.getName(),
                fmc, map, MediaType.TEXT_PLAIN).getText();
        assertEquals("Value=myValue", result);

        // Clean-up
        BioUtils.delete(testFile);
        BioUtils.delete(testDir, true);
    }

}
