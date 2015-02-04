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

package org.restlet.ext.netty.internal;

import static org.jboss.netty.channel.Channels.pipeline;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.ssl.SslHandler;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;
import org.restlet.ext.netty.NettyServerHelper;

/**
 * HTTPS channel pipeline factory.
 * 
 * @author Gabriel Ciuloaica (gciuloaica@gmail.com)
 */
public class HttpsServerPipelineFactory implements ChannelPipelineFactory {

    /** The server helper. */
    private final NettyServerHelper helper;

    /** The SSL context. */
    private final SSLContext sslContext;

    /**
     * Constructor.
     * 
     * @param serverHelper
     *            The server helper.
     * @param sslContext
     *            The SSL context.
     */
    public HttpsServerPipelineFactory(NettyServerHelper serverHelper,
            SSLContext sslContext) {
        this.helper = serverHelper;
        this.sslContext = sslContext;
    }

    /**
     * Implements the {@link ChannelPipelineFactory#getPipeline()} method.
     * 
     * @return The channel pipeline.
     */
    public ChannelPipeline getPipeline() throws Exception {
        SSLEngine sslEngine = this.sslContext.createSSLEngine();
        sslEngine.setUseClientMode(false);

        ChannelPipeline pipeline = pipeline();
        pipeline.addLast("ssl", new SslHandler(sslEngine));
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("streamer", new ChunkedWriteHandler());
        pipeline.addLast("handler", new HttpRequestHandler(this.helper));
        return pipeline;
    }

}
