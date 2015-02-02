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

package org.restlet.test.jaxrs;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.restlet.test.jaxrs.core.PathSegmentImplTest;
import org.restlet.test.jaxrs.core.UriBuilderImplTest;
import org.restlet.test.jaxrs.core.UriInfoTest;
import org.restlet.test.jaxrs.util.ConverterTests;
import org.restlet.test.jaxrs.util.EncodeOrCheckTests;
import org.restlet.test.jaxrs.util.OrderedMapTest;
import org.restlet.test.jaxrs.util.PathRegExpTests;
import org.restlet.test.jaxrs.util.RemainingPathTests;
import org.restlet.test.jaxrs.util.SortedOrderedBagTest;
import org.restlet.test.jaxrs.util.UtilTests;
import org.restlet.test.jaxrs.wrappers.RootResourceClassTest;
import org.restlet.test.jaxrs.wrappers.WrapperClassesTests;

/**
 * Collect all class tests
 * 
 * @author Stephan Koops
 * @see #suite()
 */
public class AllClassTests extends TestCase {
    public static Test suite() {
        final TestSuite mySuite = new TestSuite();
        mySuite.setName("All class tests");
        // package .
        mySuite.addTestSuite(ExceptionMappersTest.class);
        // logs only: mySuite.addTestSuite(JaxRsApplicationTest.class);
        // package .core.
        mySuite.addTestSuite(PathSegmentImplTest.class);
        mySuite.addTestSuite(UriBuilderImplTest.class);
        mySuite.addTestSuite(UriInfoTest.class);
        // package .util.
        mySuite.addTestSuite(ConverterTests.class);
        mySuite.addTestSuite(EncodeOrCheckTests.class);
        mySuite.addTestSuite(OrderedMapTest.class);
        mySuite.addTestSuite(PathRegExpTests.class);
        mySuite.addTestSuite(RemainingPathTests.class);
        mySuite.addTestSuite(SortedOrderedBagTest.class);
        mySuite.addTestSuite(UtilTests.class);
        // removed temporarily: mySuite.addTestSuite(UtilTests.class);
        // package .wrappers.
        mySuite.addTestSuite(RootResourceClassTest.class);
        mySuite.addTestSuite(WrapperClassesTests.class);
        return mySuite;
    }
}