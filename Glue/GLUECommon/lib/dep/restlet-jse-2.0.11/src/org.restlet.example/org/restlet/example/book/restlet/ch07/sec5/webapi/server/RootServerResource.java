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

package org.restlet.example.book.restlet.ch07.sec5.webapi.server;

import org.restlet.data.MediaType;
import org.restlet.example.book.restlet.ch02.sect5.sub5.common.RootResource;
import org.restlet.ext.wadl.DocumentationInfo;
import org.restlet.ext.wadl.MethodInfo;
import org.restlet.ext.wadl.RepresentationInfo;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

/**
 * Root resource implementation.
 */
public class RootServerResource extends WadlServerResource implements
        RootResource {

    @Override
    protected RepresentationInfo describe(MethodInfo methodInfo,
            Class<?> representationClass, Variant variant) {
        RepresentationInfo result = new RepresentationInfo(MediaType.TEXT_PLAIN);
        result.setIdentifier("root");

        DocumentationInfo doc = new DocumentationInfo();
        doc.setTitle("Mail application");
        doc
                .setTextContent("Simple string welcoming the user to the mail application");
        result.getDocumentations().add(doc);
        return result;
    }

    @Override
    protected void doInit() throws ResourceException {
        setAutoDescribing(false);
        setName("Root resource");
        setDescription("The root resource of the mail server application");
    }

    public String represent() {
        return "Welcome to the " + getApplication().getName() + " !";
    }

}
