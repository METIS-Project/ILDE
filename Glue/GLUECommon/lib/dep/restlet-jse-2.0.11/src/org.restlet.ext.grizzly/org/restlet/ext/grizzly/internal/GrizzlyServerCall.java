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

package org.restlet.ext.grizzly.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.restlet.Response;
import org.restlet.Server;
import org.restlet.engine.http.ServerCall;
import org.restlet.engine.http.io.ChunkedInputStream;
import org.restlet.engine.http.io.ChunkedOutputStream;
import org.restlet.engine.http.io.ReadableEntityChannel;
import org.restlet.engine.io.NioUtils;

import com.sun.grizzly.util.InputReader;
import com.sun.grizzly.util.OutputWriter;
import com.sun.grizzly.util.SSLOutputWriter;

/**
 * HTTP server call specialized for Grizzly.
 * 
 * @author Jerome Louvel
 */
public class GrizzlyServerCall extends ServerCall {

    /** The NIO byte buffer. */
    private final ByteBuffer byteBuffer;

    /** Recycled request stream. */
    private final InputReader requestStream;

    /** The underlying socket channel. */
    private final SocketChannel socketChannel;

    /**
     * Constructor.
     * 
     * @param server
     *            The parent server.
     * @param byteBuffer
     *            The NIO byte buffer.
     * @param key
     *            The NIO selection key.
     * @param confidential
     *            Indicates if the call is confidential.
     */
    public GrizzlyServerCall(Server server, ByteBuffer byteBuffer,
            SelectionKey key, boolean confidential) {
        super(server);
        setConfidential(confidential);

        this.byteBuffer = byteBuffer;
        this.requestStream = new InputReader();
        this.requestStream.setSelectionKey(key);
        this.requestStream.setByteBuffer(byteBuffer);
        this.socketChannel = (SocketChannel) key.channel();

        getRequestHeaders().clear();

        try {
            // Read the request header
            readRequestHead(this.requestStream);
        } catch (IOException ioe) {
            getLogger().log(Level.WARNING, "Unable to parse the HTTP request",
                    ioe);
        }
    }

    @Override
    public void complete() {
        // Exhaust the socket channel before closing in case
        // the client is still writing to it
        // TODO: support NIO exhausting
        // ByteUtils.exhaust(getRequestEntityStream(getContentLength()));
    }

    @Override
    public String getClientAddress() {
        return getSocket().getInetAddress().getHostAddress();
    }

    @Override
    public int getClientPort() {
        return getSocket().getPort();
    }

    @Override
    public ReadableByteChannel getRequestEntityChannel(long size) {
        if (isRequestChunked()) {
            // Leave chunked encoding to the stream mode
            return null;
        }

        return new ReadableEntityChannel(this.byteBuffer, getSocketChannel(),
                size);
    }

    @Override
    public InputStream getRequestEntityStream(long size) {
        if (isRequestChunked()) {
            return new ChunkedInputStream(null, this.requestStream);
        }

        // Leave normal encoding to the channel mode
        return null;
    }

    @Override
    public ReadableByteChannel getRequestHeadChannel() {
        return getSocketChannel();
    }

    @Override
    public InputStream getRequestHeadStream() {
        return null;
    }

    @Override
    public WritableByteChannel getResponseEntityChannel() {
        if (isResponseChunked()) {
            // Leave chunked encoding to the stream mode
            return null;
        }

        return getWritableChannel();
    }

    @Override
    public OutputStream getResponseEntityStream() {
        if (isResponseChunked()) {
            return new ChunkedOutputStream(NioUtils
                    .getStream(getWritableChannel()));
        }
        // Leave normal encoding to the channel mode
        return null;
    }

    /**
     * Returns the socket associated to the socket channel.
     * 
     * @return The socket.
     */
    private Socket getSocket() {
        return getSocketChannel().socket();
    }

    /**
     * Returns the readable socket channel.
     * 
     * @return The readable socket channel.
     */
    private SocketChannel getSocketChannel() {
        return this.socketChannel;
    }

    @Override
    public String getSslCipherSuite() {
        Socket socket = getSocket();

        if (socket instanceof SSLSocket) {
            SSLSocket sslSocket = (SSLSocket) socket;
            SSLSession sslSession = sslSocket.getSession();

            if (sslSession != null) {
                return sslSession.getCipherSuite();
            }
        }

        return null;
    }

    @Override
    public List<Certificate> getSslClientCertificates() {
        Socket socket = getSocket();

        if (socket instanceof SSLSocket) {
            SSLSocket sslSocket = (SSLSocket) socket;
            SSLSession sslSession = sslSocket.getSession();

            if (sslSession != null) {
                try {
                    List<Certificate> clientCertificates = Arrays
                            .asList(sslSession.getPeerCertificates());

                    return clientCertificates;
                } catch (SSLPeerUnverifiedException e) {
                    getLogger().log(Level.FINE,
                            "Can't get the client certificates.", e);
                }
            }
        }
        return null;
    }

    @Override
    protected byte[] getSslSessionIdBytes() {
        Socket socket = getSocket();

        if (socket instanceof SSLSocket) {
            SSLSocket sslSocket = (SSLSocket) socket;
            SSLSession sslSession = sslSocket.getSession();

            if (sslSession != null) {
                return sslSession.getId();
            }
        }

        return null;
    }

    /**
     * Return the underlying socket channel.
     * 
     * @return The underlying socket channel.
     */
    public WritableByteChannel getWritableChannel() {
        if (isConfidential()) {
            return new WritableByteChannel() {
                public void close() throws IOException {

                }

                public boolean isOpen() {
                    return getSocketChannel().isOpen();
                }

                public int write(ByteBuffer src) throws IOException {
                    int nWrite = src.limit();
                    SSLOutputWriter.flushChannel(getSocketChannel(), src);
                    return nWrite;
                }
            };
        }

        return getSocketChannel();
    }

    @Override
    public void writeResponseHead(Response response) throws IOException {
        final ByteArrayOutputStream headStream = new ByteArrayOutputStream(8192);
        writeResponseHead(response, headStream);
        final ByteBuffer buffer = ByteBuffer.wrap(headStream.toByteArray());

        if (isConfidential()) {
            SSLOutputWriter.flushChannel(getSocketChannel(), buffer);
        } else {
            OutputWriter.flushChannel(getSocketChannel(), buffer);
        }

        buffer.clear();
    }

    /**
     * Closes the socket.
     */
    @Override
    public boolean abort() {
        try {
            getSocket().close();
        } catch (IOException e) {
        }

        return true;
    }
}
