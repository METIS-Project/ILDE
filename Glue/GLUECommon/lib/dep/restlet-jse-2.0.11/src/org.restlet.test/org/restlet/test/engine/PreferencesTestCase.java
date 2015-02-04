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

package org.restlet.test.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.engine.http.header.PreferenceReader;
import org.restlet.engine.http.header.PreferenceWriter;
import org.restlet.test.RestletTestCase;

/**
 * Unit tests for the Preference related classes.
 * 
 * @author Jerome Louvel
 */
public class PreferencesTestCase extends RestletTestCase {
    /**
     * Tests the parsing of a single preference header.
     * 
     * @param headerValue
     *            The preference header.
     */
    private void testMediaType(String headerValue, boolean testEquals)
            throws IOException {
        PreferenceReader<MediaType> pr = new PreferenceReader<MediaType>(
                PreferenceReader.TYPE_MEDIA_TYPE, headerValue);
        List<Preference<MediaType>> prefs = new ArrayList<Preference<MediaType>>();
        pr.addValues(prefs);

        // Rewrite the header
        String newHeaderValue = PreferenceWriter.write(prefs);

        // Reread and rewrite the header (prevent formatting issues)
        pr = new PreferenceReader<MediaType>(PreferenceReader.TYPE_MEDIA_TYPE,
                headerValue);
        prefs = new ArrayList<Preference<MediaType>>();
        pr.addValues(prefs);
        String newHeaderValue2 = PreferenceWriter.write(prefs);

        if (testEquals) {
            // Compare initial and new headers
            assertEquals(newHeaderValue, newHeaderValue2);
        }
    }

    /**
     * Tests the preferences parsing.
     */
    public void testParsing() throws IOException {
        testMediaType(
                "text/*;q=0.3, text/html;q=0.7, text/html;level=1, text/html;LEVEL=2;q=0.4;ext1, */*;q=0.5",
                true);
        testMediaType(
                "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/*,,*/*;q=0.5",
                false);
    }
}
