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

package org.restlet.test.resource;

import java.io.IOException;

import org.restlet.Application;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.routing.Router;
import org.restlet.test.RestletTestCase;

/**
 * Test annotated resource that reimplements of one the annotated method from
 * its abstract super class that implements several annotated interfaces.
 * 
 * @author Thierry Boileau
 */
public class AnnotatedResource11TestCase extends RestletTestCase {

    private static class TestApplication extends Application {

        @Override
        public Restlet createInboundRoot() {
            Router router = new Router(getContext());
            router.attach("/test", MyResource11.class);
            return router;
        }

    }

    private Component c;

    private Client client;

    protected void setUp() throws Exception {
        super.setUp();
        Engine.getInstance().getRegisteredConverters().clear();
        Engine.getInstance().registerDefaultConverters();
        c = new Component();
        c.getServers().add(Protocol.HTTP, 8111);
        c.getDefaultHost().attach(new TestApplication());
        c.start();

        client = new Client(Protocol.HTTP);
    }

    @Override
    protected void tearDown() throws Exception {
        c.stop();
        c = null;
        client.stop();
        client = null;
        super.tearDown();
    }

    /**
     * Test annotated methods.
     * 
     * @throws IOException
     * @throws ResourceException
     */
    public void test() throws IOException, ResourceException {

        client = new Client(Protocol.HTTP);
        Request request = new Request(Method.GET, "http://localhost:8111/test");
        Response response = client.handle(request);
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        assertEquals("asText-txt", response.getEntity().getText());
        response.getEntity().release();

        request = new Request(Method.POST, "http://localhost:8111/test");
        response = client.handle(request);
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        assertEquals("accept", response.getEntity().getText());
        response.getEntity().release();
    }

}
