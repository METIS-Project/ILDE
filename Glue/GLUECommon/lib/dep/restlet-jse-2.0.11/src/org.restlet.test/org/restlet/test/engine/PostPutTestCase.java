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

import org.restlet.Application;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;

/**
 * Unit tests for POST and PUT requests.
 * 
 * @author Jerome Louvel
 */
public class PostPutTestCase extends BaseConnectorsTestCase {

    @Override
    protected void call(String uri) throws Exception {
        Client client = new Client(Protocol.HTTP);
        testCall(client, Method.POST, uri);
        testCall(client, Method.PUT, uri);
        client.stop();
    }

    @Override
    protected Application createApplication(final Component component) {
        Application application = new Application() {
            @Override
            public Restlet createInboundRoot() {
                final Restlet trace = new Restlet(getContext()) {
                    @Override
                    public void handle(Request request, Response response) {
                        Representation entity = request.getEntity();
                        if (entity != null) {
                            Form form = new Form(entity);
                            response.setEntity(form.getWebRepresentation());
                        }
                    }
                };

                return trace;
            }
        };

        return application;
    }

    private void testCall(Client client, Method method, String uri)
            throws Exception {
        Form inputForm = new Form();
        inputForm.add("a", "a");
        inputForm.add("b", "b");

        Request request = new Request(method, uri);
        request.setEntity(inputForm.getWebRepresentation());

        Response response = client.handle(request);
        assertNotNull(response.getEntity());

        Representation entity = response.getEntity();
        Form outputForm = new Form(entity);
        assertEquals(2, outputForm.size());
        assertEquals("a", outputForm.getFirstValue("a"));
        assertEquals("b", outputForm.getFirstValue("b"));
        entity.release();
    }

}
