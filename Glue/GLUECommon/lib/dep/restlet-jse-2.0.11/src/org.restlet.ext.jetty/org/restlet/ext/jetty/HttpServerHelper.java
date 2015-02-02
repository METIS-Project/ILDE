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

package org.restlet.ext.jetty;

import org.eclipse.jetty.server.AbstractConnector;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.server.nio.BlockingChannelConnector;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.restlet.Server;
import org.restlet.data.Protocol;

/**
 * Jetty HTTP server connector. Here is the list of additional parameters that
 * are supported. They should be set in the Server's context before it is
 * started:
 * <table>
 * <tr>
 * <th>Parameter name</th>
 * <th>Value type</th>
 * <th>Default value</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>type</td>
 * <td>int</td>
 * <td>1</td>
 * <td>The type of Jetty connector to use.<br>
 * 1 : Selecting NIO connector (Jetty's SelectChannelConnector class).<br>
 * 2 : Blocking NIO connector (Jetty's BlockingChannelConnector class).<br>
 * 3 : Blocking BIO connector (Jetty's SocketConnector class).</td>
 * </tr>
 * </table>
 * 
 * @see <a href="http://jetty.mortbay.org/jetty6/">Jetty home page</a>
 * @author Jerome Louvel
 */
public class HttpServerHelper extends JettyServerHelper {
    /**
     * Constructor.
     * 
     * @param server
     *            The server to help.
     */
    public HttpServerHelper(Server server) {
        super(server);
        getProtocols().add(Protocol.HTTP);
    }

    /**
     * Creates a new internal Jetty connector.
     * 
     * @return A new internal Jetty connector.
     */
    @Override
    protected AbstractConnector createConnector() {
        AbstractConnector result = null;

        // Create and configure the Jetty HTTP connector
        switch (getType()) {
        case 1:
            // Selecting NIO connector
            result = new SelectChannelConnector();
            break;
        case 2:
            // Blocking NIO connector
            result = new BlockingChannelConnector();
            break;
        case 3:
            // Blocking BIO connector
            result = new SocketConnector();
            break;
        }

        return result;
    }

    /**
     * Returns the type of Jetty connector to use.
     * 
     * @return The type of Jetty connector to use.
     */
    public int getType() {
        return Integer.parseInt(getHelpedParameters()
                .getFirstValue("type", "1"));
    }

}
