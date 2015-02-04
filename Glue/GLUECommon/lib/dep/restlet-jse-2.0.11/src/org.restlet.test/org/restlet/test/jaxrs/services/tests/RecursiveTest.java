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
import org.restlet.test.jaxrs.services.resources.RecursiveTestService;

/**
 * @author Stephan Koops
 * @see RecursiveTestService
 */
public class RecursiveTest extends JaxRsTestCase {
    @Override
    protected Application getApplication() {
        final Application appConfig = new Application() {
            @Override
            @SuppressWarnings({ "unchecked", "rawtypes" })
            public Set<Class<?>> getClasses() {
                return (Set) Collections.singleton(RecursiveTestService.class);
            }
        };
        return appConfig;
    }

    public void test0() throws Exception {
        final Response response = get();
        sysOutEntityIfError(response);
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        assertEquals("0", response.getEntity().getText());
    }

    public void test1a() throws Exception {
        final Response response = get("a");
        sysOutEntityIfError(response);
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        assertEquals("1", response.getEntity().getText());
    }

    public void test1b() throws Exception {
        final Response response = get("a/");
        sysOutEntityIfError(response);
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        assertEquals("1", response.getEntity().getText());
    }

    public void test2() throws Exception {
        final Response response = get("a/a");
        sysOutEntityIfError(response);
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        assertEquals("2", response.getEntity().getText());
    }

    public void test2b() throws Exception {
        final Response response = get("a/a/");
        sysOutEntityIfError(response);
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        assertEquals("2", response.getEntity().getText());
    }

    public void test3() throws Exception {
        final Response response = get("a/a/a");
        sysOutEntityIfError(response);
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        assertEquals("3", response.getEntity().getText());
    }
}