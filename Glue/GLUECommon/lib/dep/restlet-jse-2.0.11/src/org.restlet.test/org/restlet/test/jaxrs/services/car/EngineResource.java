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

package org.restlet.test.jaxrs.services.car;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * @author Stephan Koops
 * @see CarResource
 */
public class EngineResource {
    /**
     * @param carId
     * @return
     */
    public static String getPlainRepr(int carId) {
        return "This is the engine of car with id " + carId + ".";
    }

    /** Car, the engine elongs to */
    private final CarResource car;

    public EngineResource(CarResource car) {
        this.car = car;
    }

    @GET
    @Produces("text/plain")
    public String getText() {
        final int carId = this.car.getId();
        return getPlainRepr(carId);
    }
}