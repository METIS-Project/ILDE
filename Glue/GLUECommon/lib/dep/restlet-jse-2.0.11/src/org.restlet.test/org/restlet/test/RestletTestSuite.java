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

package org.restlet.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.restlet.test.component.ComponentXmlConfigTestCase;
import org.restlet.test.component.ComponentXmlTestCase;
import org.restlet.test.connector.FileClientTestCase;
import org.restlet.test.connector.RestartTestCase;
import org.restlet.test.connector.RiapTestCase;
import org.restlet.test.data.AuthenticationInfoTestCase;
import org.restlet.test.data.ConnegTestCase;
import org.restlet.test.data.CookieTestCase;
import org.restlet.test.data.FileReferenceTestCase;
import org.restlet.test.data.LanguageTestCase;
import org.restlet.test.data.MediaTypeTestCase;
import org.restlet.test.data.ProductTokenTestCase;
import org.restlet.test.data.RangeTestCase;
import org.restlet.test.data.RecipientInfoTestCase;
import org.restlet.test.data.ReferenceTestCase;
import org.restlet.test.data.StatusTestCase;
import org.restlet.test.engine.EngineTestSuite;
import org.restlet.test.ext.atom.AtomTestCase;
import org.restlet.test.ext.freemarker.FreeMarkerTestCase;
import org.restlet.test.ext.gwt.GwtConverterTest;
import org.restlet.test.ext.jaxb.JaxbBasicConverterTest;
import org.restlet.test.ext.jaxb.JaxbIntegrationConverterTestCase;
import org.restlet.test.ext.odata.ODataTestSuite;
import org.restlet.test.ext.spring.AllSpringTests;
import org.restlet.test.ext.velocity.VelocityTestCase;
import org.restlet.test.ext.wadl.WadlTestCase;
import org.restlet.test.ext.xml.ResolvingTransformerTestCase;
import org.restlet.test.ext.xml.RestletXmlTestCase;
import org.restlet.test.ext.xml.TransformerTestCase;
import org.restlet.test.jaxrs.AllJaxRsTests;
import org.restlet.test.representation.AppendableRepresentationTestCase;
import org.restlet.test.representation.DigesterRepresentationTestCase;
import org.restlet.test.representation.RangeRepresentationTestCase;
import org.restlet.test.resource.ResourceTestSuite;
import org.restlet.test.routing.FilterTestCase;
import org.restlet.test.routing.RedirectTestCase;
import org.restlet.test.routing.RouteListTestCase;
import org.restlet.test.routing.ValidatorTestCase;
import org.restlet.test.security.DigestVerifierTestCase;
import org.restlet.test.security.HttpBasicTestCase;
import org.restlet.test.security.HttpDigestTestCase;
import org.restlet.test.security.RoleTestCase;
import org.restlet.test.security.SecurityTestCase;
import org.restlet.test.util.TemplateTestCase;

/**
 * Suite of unit tests for the Restlet RI.
 * 
 * @author Jerome Louvel
 */
public class RestletTestSuite extends TestSuite {

    /**
     * JUnit constructor.
     * 
     * @return The unit test.
     */
    public static Test suite() {
        return new RestletTestSuite();
    }

    /** Constructor. */
    public RestletTestSuite() {
        addTest(ResourceTestSuite.suite());
        addTestSuite(AppendableRepresentationTestCase.class);
        addTestSuite(AtomTestCase.class);
        addTestSuite(AuthenticationInfoTestCase.class);
        addTestSuite(CallTestCase.class);
        addTestSuite(ComponentXmlConfigTestCase.class);
        addTestSuite(CookieTestCase.class);
        addTestSuite(ConnegTestCase.class);
        addTestSuite(FileClientTestCase.class);
        addTestSuite(FileReferenceTestCase.class);
        addTestSuite(FilterTestCase.class);
        addTestSuite(FreeMarkerTestCase.class);
        addTestSuite(GwtConverterTest.class);
        addTestSuite(JaxbBasicConverterTest.class);
        addTestSuite(JaxbIntegrationConverterTestCase.class);
        addTestSuite(LanguageTestCase.class);
        addTestSuite(MediaTypeTestCase.class);
        addTestSuite(ProductTokenTestCase.class);
        addTestSuite(ReferenceTestCase.class);
        addTestSuite(ResolvingTransformerTestCase.class);
        addTestSuite(RestartTestCase.class);
        addTestSuite(RestletXmlTestCase.class);
        addTestSuite(RiapTestCase.class);
        addTestSuite(RouteListTestCase.class);
        addTestSuite(DigestVerifierTestCase.class);
        addTestSuite(RecipientInfoTestCase.class);
        addTestSuite(RoleTestCase.class);
        addTestSuite(StatusTestCase.class);
        addTestSuite(TemplateTestCase.class);
        addTestSuite(TransformerTestCase.class);
        addTestSuite(ValidatorTestCase.class);
        addTestSuite(VelocityTestCase.class);
        addTestSuite(WadlTestCase.class);
        addTest(ODataTestSuite.suite());
        
        // TODO Fix Zip client test case
        // addTestSuite(ZipClientTestCase.class);

        // Tests based on HTTP client connectors are not supported by the GAE
        // edition.
        addTestSuite(ComponentXmlTestCase.class);
        addTestSuite(DigesterRepresentationTestCase.class);
        addTestSuite(HeaderTestCase.class);
        addTestSuite(HttpBasicTestCase.class);
        addTestSuite(HttpDigestTestCase.class);
        addTestSuite(RangeTestCase.class);
        addTestSuite(RangeRepresentationTestCase.class);
        addTestSuite(RedirectTestCase.class);
        addTestSuite(SecurityTestCase.class);
        addTestSuite(TemplateFilterTestCase.class);

        addTest(EngineTestSuite.suite());
        addTest(AllJaxRsTests.suite());
        addTest(AllSpringTests.suite());
    }

}
