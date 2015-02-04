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

package org.restlet.engine;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.restlet.Connector;
import org.restlet.Context;
import org.restlet.data.Protocol;

/**
 * Base connector helper.
 * 
 * @author Jerome Louvel
 */
public abstract class ConnectorHelper<T extends Connector> extends
        RestletHelper<T> {

    /**
     * Returns the connector service associated to a request.
     * 
     * @return The connector service associated to a request.
     */
    public static org.restlet.service.ConnectorService getConnectorService() {
        org.restlet.service.ConnectorService result = null;
        org.restlet.Application application = org.restlet.Application
                .getCurrent();

        if (application != null) {
            result = application.getConnectorService();
        } else {
            result = new org.restlet.service.ConnectorService();
        }

        return result;
    }

    /** The protocols simultaneously supported. */
    private final List<Protocol> protocols;

    /**
     * Constructor.
     */
    public ConnectorHelper(T connector) {
        super(connector);
        this.protocols = new CopyOnWriteArrayList<Protocol>();
    }

    /**
     * Returns the helped Restlet context.
     * 
     * @return The helped Restlet context.
     */
    @Override
    public Context getContext() {
        if (Edition.CURRENT == Edition.GWT) {
            return null;
        }

        return super.getContext();
    }

    /**
     * Returns the protocols simultaneously supported.
     * 
     * @return The protocols simultaneously supported.
     */
    public List<Protocol> getProtocols() {
        return this.protocols;
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {
    }

    @Override
    public void update() throws Exception {
    }

}
