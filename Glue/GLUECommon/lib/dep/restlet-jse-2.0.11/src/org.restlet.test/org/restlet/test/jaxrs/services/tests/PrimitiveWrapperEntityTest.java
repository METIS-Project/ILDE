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
import org.restlet.data.Status;
import org.restlet.representation.StringRepresentation;
import org.restlet.test.jaxrs.services.providers.BooleanEntityProvider;
import org.restlet.test.jaxrs.services.providers.CharacterEntityProvider;
import org.restlet.test.jaxrs.services.providers.IntegerEntityProvider;
import org.restlet.test.jaxrs.services.resources.PrimitiveWrapperEntityResource;
import org.restlet.test.jaxrs.util.TestUtils;

/**
 * @author Stephan Koops
 * @see PrimitiveWrapperEntityResource
 */
public class PrimitiveWrapperEntityTest extends JaxRsTestCase {

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Application getApplication() {
        final Application appConfig = new Application() {
            @Override
            public Set<Object> getSingletons() {
                return (Set) TestUtils.createSet(new IntegerEntityProvider(),
                        new CharacterEntityProvider(), new BooleanEntityProvider());
            }
    
            @Override
            public Set<Class<?>> getClasses() {
                return (Set) Collections.singleton(PrimitiveWrapperEntityResource.class);
            }
        };
        return appConfig;
    }

    public void test1() throws Exception {
        final Response response = put("intReturnInt", new StringRepresentation(
                "47"));
        sysOutEntityIfError(response);
        assertEquals(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE, response
                .getStatus());
    }

    /**
     * @see PrimitiveWrapperEntityResource#charReturnCharacter(char)
     */
    public void test2() throws Exception {
        final Response response = put("charReturnCharacter",
                new StringRepresentation("x"));
        sysOutEntityIfError(response);
        assertEquals(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE, response
                .getStatus());
    }

    /**
     * @see PrimitiveWrapperEntityResource#BooleanReturnboolean(Boolean)
     */
    public void test3() throws Exception {
        Response response = put("BooleanReturnboolean",
                new StringRepresentation("true"));
        sysOutEntityIfError(response);
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        assertEquals("true", response.getEntity().getText());

        response = put("BooleanReturnboolean", null);
        sysOutEntityIfError(response);
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        if (response.getEntity() != null) {
            assertEquals("false", response.getEntity().getText());
        }
    }

    public void test4() throws Exception {
        Response response = put("integerReturnInteger",
                new StringRepresentation("47"));
        sysOutEntityIfError(response);
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        assertEquals("47", response.getEntity().getText());

        response = put("integerReturnInteger", null);
        sysOutEntityIfError(response);
        assertEquals(Status.SUCCESS_NO_CONTENT, response.getStatus());
        assertEmptyEntity(response);
    }

}