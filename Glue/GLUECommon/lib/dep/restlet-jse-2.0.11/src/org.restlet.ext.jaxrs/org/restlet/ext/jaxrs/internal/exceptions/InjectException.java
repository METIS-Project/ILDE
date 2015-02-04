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

package org.restlet.ext.jaxrs.internal.exceptions;

import javax.ws.rs.core.Context;

/**
 * This kind of exception is thrown, when an object could not be injected in a
 * field or bean setter annotated with {@link Context} or other annotations.
 * 
 * @author Stephan Koops
 */
public class InjectException extends JaxRsException {
    private static final long serialVersionUID = 6796414811480666857L;

    /**
     * @param mie
     */
    public InjectException(MethodInvokeException mie) {
        super(mie.getMessage(), mie.getCause());
        setStackTrace(mie.getStackTrace());
    }

    /**
     * @param message
     * @param cause
     */
    public InjectException(String message, Throwable cause) {
        super(message, cause);
    }
}