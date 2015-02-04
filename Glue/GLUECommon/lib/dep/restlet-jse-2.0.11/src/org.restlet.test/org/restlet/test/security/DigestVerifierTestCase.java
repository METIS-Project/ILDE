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

package org.restlet.test.security;

import org.restlet.data.Digest;
import org.restlet.ext.crypto.DigestVerifier;
import org.restlet.security.MapVerifier;
import org.restlet.test.RestletTestCase;

/**
 * Restlet unit tests for the DigestVerifierTestCase class.
 * 
 * @author Jerome Louvel
 */
public class DigestVerifierTestCase extends RestletTestCase {

    public void test1() {
        MapVerifier mv = new MapVerifier();
        mv.getLocalSecrets().put("scott", "tiger".toCharArray());

        DigestVerifier<MapVerifier> sdv = new DigestVerifier<MapVerifier>(
                Digest.ALGORITHM_SHA_1, mv, null);

        assertTrue(sdv.verify("scott", "RuPXcqGIjq3/JsetpH/XUC15bgc="
                .toCharArray()));
    }

    public void test2() {
        MapVerifier mv = new MapVerifier();
        mv.getLocalSecrets().put("scott",
                "RuPXcqGIjq3/JsetpH/XUC15bgc=".toCharArray());

        DigestVerifier<MapVerifier> sdv = new DigestVerifier<MapVerifier>(
                Digest.ALGORITHM_SHA_1, mv, Digest.ALGORITHM_SHA_1);

        assertTrue(sdv.verify("scott", "RuPXcqGIjq3/JsetpH/XUC15bgc="
                .toCharArray()));

        assertFalse(sdv.verify("scott", "xxxxx".toCharArray()));

        assertFalse(sdv.verify("tom", "RuPXcqGIjq3/JsetpH/XUC15bgc="
                .toCharArray()));
    }

    public void test3() {
        MapVerifier mv = new MapVerifier();
        mv.getLocalSecrets().put("scott",
                "RuPXcqGIjq3/JsetpH/XUC15bgc=".toCharArray());

        DigestVerifier<MapVerifier> sdv = new DigestVerifier<MapVerifier>(null,
                mv, Digest.ALGORITHM_SHA_1);

        assertTrue(sdv.verify("scott", "tiger".toCharArray()));
    }
}
