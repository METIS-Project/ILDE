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

package org.restlet.ext.net.internal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Uniform;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.engine.Edition;
import org.restlet.engine.http.ClientCall;
import org.restlet.engine.security.SslContextFactory;
import org.restlet.engine.security.SslUtils;
import org.restlet.engine.util.SystemUtils;
import org.restlet.ext.net.HttpClientHelper;
import org.restlet.representation.Representation;
import org.restlet.util.Series;

/**
 * HTTP client connector call based on JDK's java.net.HttpURLConnection class.
 * 
 * @author Jerome Louvel
 */
public class HttpUrlConnectionCall extends ClientCall {

    /** The wrapped HTTP URL connection. */
    private final HttpURLConnection connection;

    /** Indicates if the response headers were added. */
    private volatile boolean responseHeadersAdded;

    /**
     * Constructor.
     * 
     * @param helper
     *            The parent HTTP client helper.
     * @param method
     *            The method name.
     * @param requestUri
     *            The request URI.
     * @param hasEntity
     *            Indicates if the call will have an entity to send to the
     *            server.
     * @throws IOException
     */
    public HttpUrlConnectionCall(HttpClientHelper helper, String method,
            String requestUri, boolean hasEntity) throws IOException {
        super(helper, method, requestUri);

        if (requestUri.startsWith("http")) {
            URL url = new URL(requestUri);
            this.connection = (HttpURLConnection) url.openConnection();

            // These properties can only be used with Java 1.5 and upper
            // releases
            int majorVersionNumber = SystemUtils.getJavaMajorVersion();
            int minorVersionNumber = SystemUtils.getJavaMinorVersion();
            if ((majorVersionNumber > 1)
                    || ((majorVersionNumber == 1) && (minorVersionNumber >= 5))) {
                this.connection.setConnectTimeout(getHelper()
                        .getConnectTimeout());
                this.connection.setReadTimeout(getHelper().getReadTimeout());
            }

            this.connection.setAllowUserInteraction(getHelper()
                    .isAllowUserInteraction());
            this.connection.setDoOutput(hasEntity);
            this.connection.setInstanceFollowRedirects(getHelper()
                    .isFollowRedirects());
            this.connection.setUseCaches(getHelper().isUseCaches());
            this.responseHeadersAdded = false;

            if (this.connection instanceof HttpsURLConnection) {
                setConfidential(true);
                HttpsURLConnection https = (HttpsURLConnection) this.connection;
                SslContextFactory sslContextFactory = SslUtils
                        .getSslContextFactory(getHelper());
                if (sslContextFactory != null) {
                    try {
                        SSLContext sslContext = sslContextFactory
                                .createSslContext();
                        https
                                .setSSLSocketFactory(sslContext
                                        .getSocketFactory());
                    } catch (Exception e) {
                        throw new RuntimeException(
                                "Unable to create SSLContext.", e);
                    }
                }

                HostnameVerifier verifier = helper.getHostnameVerifier();

                if (verifier != null) {
                    https.setHostnameVerifier(verifier);
                }
            }
        } else {
            throw new IllegalArgumentException(
                    "Only HTTP or HTTPS resource URIs are allowed here");
        }
    }

    /**
     * Returns the connection.
     * 
     * @return The connection.
     */
    public HttpURLConnection getConnection() {
        return this.connection;
    }

    /**
     * Returns the HTTP client helper.
     * 
     * @return The HTTP client helper.
     */
    @Override
    public HttpClientHelper getHelper() {
        return (HttpClientHelper) super.getHelper();
    }

    /**
     * Returns the response reason phrase.
     * 
     * @return The response reason phrase.
     */
    @Override
    public String getReasonPhrase() {
        try {
            return getConnection().getResponseMessage();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected Representation getRepresentation(InputStream stream) {
        Representation r = super.getRepresentation(stream);
        return new ConnectionClosingRepresentation(r, getConnection());
    }

    @Override
    public WritableByteChannel getRequestEntityChannel() {
        return null;
    }

    @Override
    public OutputStream getRequestEntityStream() {
        return getRequestStream();
    }

    @Override
    public OutputStream getRequestHeadStream() {
        return getRequestStream();
    }

    /**
     * Returns the request entity stream if it exists.
     * 
     * @return The request entity stream if it exists.
     */
    public OutputStream getRequestStream() {
        try {
            return getConnection().getOutputStream();
        } catch (IOException ioe) {
            return null;
        }
    }

    @Override
    public ReadableByteChannel getResponseEntityChannel(long size) {
        return null;
    }

    @Override
    public InputStream getResponseEntityStream(long size) {
        InputStream result = null;

        try {
            result = getConnection().getInputStream();
        } catch (IOException ioe) {
            result = getConnection().getErrorStream();
        }

        if (result == null) {
            // Maybe an error stream is available instead
            result = getConnection().getErrorStream();
        }

        return result;
    }

    /**
     * Returns the modifiable list of response headers.
     * 
     * @return The modifiable list of response headers.
     */
    @Override
    public Series<Parameter> getResponseHeaders() {
        Series<Parameter> result = super.getResponseHeaders();

        if (!this.responseHeadersAdded) {
            // Read the response headers
            int i = 1;
            String headerName = getConnection().getHeaderFieldKey(i);
            String headerValue = getConnection().getHeaderField(i);
            while (headerName != null) {
                result.add(headerName, headerValue);
                i++;
                if (Edition.CURRENT != Edition.GAE) {
                    headerName = getConnection().getHeaderFieldKey(i);
                    headerValue = getConnection().getHeaderField(i);
                } else {
                    try {
                        headerName = getConnection().getHeaderFieldKey(i);
                        headerValue = getConnection().getHeaderField(i);
                    } catch (java.util.NoSuchElementException e) {
                        headerName = null;
                    }
                }
            }

            this.responseHeadersAdded = true;
        }

        return result;
    }

    /**
     * Returns the response address.<br>
     * Corresponds to the IP address of the responding server.
     * 
     * @return The response address.
     */
    @Override
    public String getServerAddress() {
        return getConnection().getURL().getHost();
    }

    /**
     * Returns the response status code.
     * 
     * @return The response status code.
     * @throws IOException
     * @throws IOException
     */
    @Override
    public int getStatusCode() throws IOException {
        return getConnection().getResponseCode();
    }

    /**
     * Sends the request to the client. Commits the request line, headers and
     * optional entity and send them over the network.
     * 
     * @param request
     *            The high-level request.
     * @return The result status.
     */
    @Override
    public Status sendRequest(Request request) {
        Status result = null;

        try {
            if (request.isEntityAvailable()) {
                Representation entity = request.getEntity();

                // These properties can only be used with Java 1.5 and upper
                // releases
                int majorVersionNumber = SystemUtils.getJavaMajorVersion();
                int minorVersionNumber = SystemUtils.getJavaMinorVersion();
                if ((majorVersionNumber > 1)
                        || ((majorVersionNumber == 1) && (minorVersionNumber >= 5))) {
                    // Adjust the streaming mode
                    if (entity.getSize() != -1) {
                        // The size of the entity is known in advance
                        getConnection().setFixedLengthStreamingMode(
                                (int) entity.getSize());
                    } else {
                        // The size of the entity is not known in advance
                        if (getHelper().getChunkLength() >= 0) {
                            // Use chunked encoding
                            getConnection().setChunkedStreamingMode(
                                    getHelper().getChunkLength());
                        } else {
                            // Use entity buffering to determine the content
                            // length
                        }
                    }
                }
            }

            // Set the request method
            getConnection().setRequestMethod(getMethod());

            // Set the request headers
            for (Parameter header : getRequestHeaders()) {
                getConnection().addRequestProperty(header.getName(),
                        header.getValue());
            }

            // Ensure that the connection is active
            getConnection().connect();

            // Send the optional entity
            result = super.sendRequest(request);
        } catch (ConnectException ce) {
            getHelper()
                    .getLogger()
                    .log(
                            Level.FINE,
                            "An error occurred during the connection to the remote HTTP server.",
                            ce);
            result = new Status(Status.CONNECTOR_ERROR_CONNECTION, ce);
        } catch (SocketTimeoutException ste) {
            getHelper()
                    .getLogger()
                    .log(
                            Level.FINE,
                            "An timeout error occurred during the communication with the remote HTTP server.",
                            ste);
            result = new Status(Status.CONNECTOR_ERROR_COMMUNICATION, ste);
        } catch (FileNotFoundException fnfe) {
            getHelper()
                    .getLogger()
                    .log(
                            Level.FINE,
                            "An unexpected error occurred during the sending of the HTTP request.",
                            fnfe);
            result = new Status(Status.CONNECTOR_ERROR_INTERNAL, fnfe);
        } catch (IOException ioe) {
            getHelper()
                    .getLogger()
                    .log(
                            Level.FINE,
                            "An error occurred during the communication with the remote HTTP server.",
                            ioe);
            result = new Status(Status.CONNECTOR_ERROR_COMMUNICATION, ioe);
        } catch (Exception e) {
            getHelper()
                    .getLogger()
                    .log(
                            Level.FINE,
                            "An unexpected error occurred during the sending of the HTTP request.",
                            e);
            result = new Status(Status.CONNECTOR_ERROR_INTERNAL, e);
        }

        return result;
    }

    @Override
    public void sendRequest(Request request, Response response, Uniform callback)
            throws Exception {
        // Send the request
        sendRequest(request);
        if(request.getOnSent() != null){
            request.getOnSent().handle(request, response);
        }

        if (callback != null) {
            // Transmit to the callback, if any.
            callback.handle(request, response);
        }
    }

}
