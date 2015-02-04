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

package org.restlet.test.jaxrs.services.tests;

import java.util.Collections;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.test.jaxrs.services.providers.IllegalArgExcMapper;
import org.restlet.test.jaxrs.services.resources.ExcMapperTestResource;

/**
 * @author Stephan Koops
 * @see ExcMapperTestResource
 * @see IllegalArgExcMapper
 */
public class ExcMapperTest extends JaxRsTestCase {

    /**
     * @param accMediaType
     * @param expMediaType
     */
    private void check(MediaType accMediaType, MediaType expMediaType) {
        final Response response = get(accMediaType);
        sysOutEntityIfError(response);
        assertEquals(IllegalArgExcMapper.STATUS, response.getStatus().getCode());
        assertEqualMediaType(expMediaType, response);
    }

    @Override
    protected Application getApplication() {
        return new Application() {
            @Override
            @SuppressWarnings({ "unchecked", "rawtypes" })
            public Set<Class<?>> getClasses() {
                return (Set) Collections.singleton(ExcMapperTestResource.class);
            }
    
            @Override
            @SuppressWarnings({ "unchecked", "rawtypes" })
            public Set<Object> getSingletons() {
                return (Set) Collections.singleton(new IllegalArgExcMapper());
            }
        };
    }

    public void testHtml() {
        check(MediaType.TEXT_HTML, MediaType.TEXT_HTML);
    }

    public void testImage() {
        check(MediaType.IMAGE_BMP, MediaType.TEXT_PLAIN);
    }

    public void testPlain() {
        check(MediaType.TEXT_PLAIN, MediaType.TEXT_PLAIN);
    }

    public void testXml() {
        check(MediaType.TEXT_XML, MediaType.TEXT_PLAIN);
    }
}