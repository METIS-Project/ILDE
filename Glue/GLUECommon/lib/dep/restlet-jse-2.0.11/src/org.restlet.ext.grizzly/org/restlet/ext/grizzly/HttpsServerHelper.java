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

package org.restlet.ext.grizzly;

import java.io.File;
import java.net.InetAddress;

import javax.net.ssl.SSLContext;

import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.engine.security.SslUtils;
import org.restlet.engine.security.SslContextFactory;
import org.restlet.ext.grizzly.internal.HttpParserFilter;

import com.sun.grizzly.Controller;
import com.sun.grizzly.DefaultProtocolChain;
import com.sun.grizzly.DefaultProtocolChainInstanceHandler;
import com.sun.grizzly.ProtocolChain;
import com.sun.grizzly.TCPSelectorHandler;
import com.sun.grizzly.filter.SSLReadFilter;

/**
 * HTTPS connector based on Grizzly. Here is the list of additional parameters
 * that are supported. They should be set in the Server's context before it is
 * started:
 * <table>
 * <tr>
 * <th>Parameter name</th>
 * <th>Value type</th>
 * <th>Default value</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>sslContextFactory</td>
 * <td>String</td>
 * <td>null</td>
 * <td>Let you specify a {@link SslContextFactory} class name as a parameter, or
 * an instance as an attribute for a more complete and flexible SSL context
 * setting. If set, it takes precedence over the other SSL parameters below.</td>
 * </tr>
 * <tr>
 * <td>keystorePath</td>
 * <td>String</td>
 * <td>${user.home}/.keystore</td>
 * <td>SSL keystore path.</td>
 * </tr>
 * <tr>
 * <td>keystorePassword</td>
 * <td>String</td>
 * <td></td>
 * <td>SSL keystore password.</td>
 * </tr>
 * <tr>
 * <td>keystoreType</td>
 * <td>String</td>
 * <td>JKS</td>
 * <td>SSL keystore type</td>
 * </tr>
 * <tr>
 * <td>keyPassword</td>
 * <td>String</td>
 * <td>${keystorePassword}</td>
 * <td>SSL key password.</td>
 * </tr>
 * <tr>
 * <td>certAlgorithm</td>
 * <td>String</td>
 * <td>SunX509</td>
 * <td>SSL certificate algorithm.</td>
 * </tr>
 * <tr>
 * <td>enabledCipherSuites</td>
 * <td>String</td>
 * <td>null</td>
 * <td>Whitespace-separated list of enabled cipher suites and/or can be
 * specified multiple times.</td>
 * </tr>
 * <tr>
 * <td>needClientAuthentication</td>
 * <td>boolean</td>
 * <td>false</td>
 * <td>Indicates if we require client certificate authentication.</td>
 * </tr>
 * <tr>
 * <td>sslProtocol</td>
 * <td>String</td>
 * <td>TLS</td>
 * <td>SSL protocol.</td>
 * </tr>
 * <tr>
 * <td>wantClientAuthentication</td>
 * <td>boolean</td>
 * <td>false</td>
 * <td>Indicates if we would like client certificate authentication (only for
 * the BIO connector type).</td>
 * </tr>
 * </table>
 * 
 * @author Jerome Louvel
 */
public class HttpsServerHelper extends GrizzlyServerHelper {

    /**
     * Constructor.
     * 
     * @param server
     *            The helped server.
     */
    public HttpsServerHelper(Server server) {
        super(server);
        getProtocols().add(Protocol.HTTPS);
    }

    @Override
    protected void configure(Controller controller) throws Exception {
        // Initialize the SSL context
        SslContextFactory sslContextFactory = SslUtils
                .getSslContextFactory(this);
        SSLContext sslContext = sslContextFactory.createSslContext();

        // Get the TCP select handler of the controller
        final TCPSelectorHandler selectorHandler = getSelectorHandler();
        // Configure it
        selectorHandler.setPort(getHelped().getPort());
        if (getHelped().getAddress() != null) {
            selectorHandler.setInet(InetAddress.getByName(getHelped()
                    .getAddress()));
        }

        // Create the Grizzly filters
        final SSLReadFilter readFilter = new SSLReadFilter();
        readFilter.setSSLContext(sslContext);

        final String[] enabledCipherSuites = SslUtils
                .getEnabledCipherSuites(this);
        if (enabledCipherSuites != null) {
            readFilter.setEnabledCipherSuites(enabledCipherSuites);
        }

        if (isNeedClientAuthentication()) {
            readFilter.setNeedClientAuth(isNeedClientAuthentication());
        } else if (isWantClientAuthentication()) {
            readFilter.setWantClientAuth(isWantClientAuthentication());
        }

        final HttpParserFilter httpParserFilter = new HttpParserFilter(this);

        // Create the Grizzly controller
        controller
                .setProtocolChainInstanceHandler(new DefaultProtocolChainInstanceHandler() {
                    @Override
                    public ProtocolChain poll() {
                        ProtocolChain protocolChain = this.protocolChains
                                .poll();
                        if (protocolChain == null) {
                            protocolChain = new DefaultProtocolChain();
                            protocolChain.addFilter(readFilter);
                            protocolChain.addFilter(httpParserFilter);
                        }
                        return protocolChain;
                    }
                });
    }

    /**
     * Returns the SSL certificate algorithm.
     * 
     * @return The SSL certificate algorithm.
     */
    @Deprecated
    public String getCertAlgorithm() {
        return getHelpedParameters().getFirstValue("certAlgorithm", "SunX509");
    }

    /**
     * Returns the SSL key password.
     * 
     * @return The SSL key password.
     */
    @Deprecated
    public String getKeyPassword() {
        return getHelpedParameters().getFirstValue("keyPassword",
                getKeystorePassword());
    }

    /**
     * Returns the SSL keystore password.
     * 
     * @return The SSL keystore password.
     */
    @Deprecated
    public String getKeystorePassword() {
        return getHelpedParameters().getFirstValue("keystorePassword", "");
    }

    /**
     * Returns the SSL keystore path.
     * 
     * @return The SSL keystore path.
     */
    @Deprecated
    public String getKeystorePath() {
        return getHelpedParameters().getFirstValue("keystorePath",
                System.getProperty("user.home") + File.separator + ".keystore");
    }

    /**
     * Returns the SSL keystore type.
     * 
     * @return The SSL keystore type.
     */
    @Deprecated
    public String getKeystoreType() {
        return getHelpedParameters().getFirstValue("keystoreType", "JKS");
    }

    /**
     * Returns the SSL keystore type.
     * 
     * @return The SSL keystore type.
     */
    @Deprecated
    public String getSslProtocol() {
        return getHelpedParameters().getFirstValue("sslProtocol", "TLS");
    }

    /**
     * Indicates if we require client certificate authentication.
     * 
     * @return True if we require client certificate authentication.
     */
    public boolean isNeedClientAuthentication() {
        return Boolean.parseBoolean(getHelpedParameters().getFirstValue(
                "needClientAuthentication", "false"));
    }

    /**
     * Indicates if we would like client certificate authentication.
     * 
     * @return True if we would like client certificate authentication.
     */
    public boolean isWantClientAuthentication() {
        return Boolean.parseBoolean(getHelpedParameters().getFirstValue(
                "wantClientAuthentication", "false"));
    }
}
