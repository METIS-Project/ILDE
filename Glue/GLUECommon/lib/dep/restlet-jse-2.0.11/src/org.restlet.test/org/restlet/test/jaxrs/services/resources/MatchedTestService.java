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

package org.restlet.test.jaxrs.services.resources;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

/**
 * @author Stephan Koops
 * @see org.restlet.test.jaxrs.services.tests.MatchedTest
 * @see UriInfo#getMatchedResources()
 * @see UriInfo#getMatchedURIs()
 */
@Path("matchedTest")
public class MatchedTestService {

    /**
     * @param uriInfo
     * @param attribute
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    private static Object getAttribute(UriInfo uriInfo, String attribute) {
        final String getterName = "get"
                + attribute.substring(0, 1).toUpperCase()
                + attribute.substring(1);
        Method subMethod;
        try {
            subMethod = uriInfo.getClass().getMethod(getterName);
        } catch (SecurityException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        } catch (NoSuchMethodException e) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        try {
            return subMethod.invoke(uriInfo);
        } catch (IllegalArgumentException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        } catch (IllegalAccessException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        } catch (InvocationTargetException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Context
    private UriInfo mainUriInfo;

    @GET
    @Produces("text/plain")
    public String get(@Context UriInfo uriInfo) {
        final int uriSize = uriInfo.getMatchedURIs().size();
        final int resourcesSize = uriInfo.getMatchedResources().size();
        return uriSize + "\n" + resourcesSize;
    }

    @GET
    @Produces("text/plain")
    @Path("resourceClassNames")
    public String getResources(@Context UriInfo uriInfo) {
        final StringBuilder stb = new StringBuilder();
        final List<Object> resources = uriInfo.getMatchedResources();
        stb.append(resources.size());
        for (final Object resource : resources) {
            stb.append('\n');
            stb.append(resource.getClass().getName());
        }
        return stb.toString();
    }

    @Path("sameSub")
    public MatchedTestService getSameSub() {
        return getSub();
    }

    @Path("sub")
    public MatchedTestService getSub() {
        final MatchedTestService sub = new MatchedTestService();
        sub.mainUriInfo = this.mainUriInfo;
        return sub;
    }

    @GET
    @Produces("text/plain")
    @Path("uriInfo/{attribute}")
    public String getUriInfoAttribute(@Context UriInfo subUriInfo,
            @PathParam("attribute") String attribute) {
        final Object mainAttrValue = getAttribute(this.mainUriInfo, attribute);
        final Object subAttrValue = getAttribute(subUriInfo, attribute);
        return mainAttrValue + "\n" + subAttrValue;
    }

    @GET
    @Produces("text/plain")
    @Path("uris")
    public String getUris(@Context UriInfo uriInfo) {
        final StringBuilder stb = new StringBuilder();
        final List<String> uris = uriInfo.getMatchedURIs();
        stb.append(uris.size());
        for (final String uri : uris) {
            stb.append('\n');
            stb.append(uri);
        }
        return stb.toString();
    }
}