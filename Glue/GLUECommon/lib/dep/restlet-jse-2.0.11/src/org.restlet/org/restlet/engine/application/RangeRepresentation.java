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

package org.restlet.engine.application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.channels.WritableByteChannel;

import org.restlet.data.Range;
import org.restlet.engine.io.BioUtils;
import org.restlet.engine.io.NioUtils;
import org.restlet.representation.Representation;
import org.restlet.util.WrapperRepresentation;

// [excludes gwt]
/**
 * Representation that exposes only a range of the content of a wrapped
 * representation.
 * 
 * @author Jerome Louvel
 */
public class RangeRepresentation extends WrapperRepresentation {

    /** The range specific to this wrapper. */
    private volatile Range range;

    /**
     * Constructor.
     * 
     * @param wrappedRepresentation
     *            The wrapped representation with a complete content.
     */
    public RangeRepresentation(Representation wrappedRepresentation) {
        this(wrappedRepresentation, null);
    }

    /**
     * Constructor.
     * 
     * @param wrappedRepresentation
     *            The wrapped representation with a complete content.
     * @param range
     *            The range to expose.
     */
    public RangeRepresentation(Representation wrappedRepresentation, Range range) {
        super(wrappedRepresentation);
        if (wrappedRepresentation.getRange() != null) {
            throw new IllegalArgumentException(
                    "The wrapped representation must not have a range set.");
        }
        setRange(range);
    }

    @Override
    public long getAvailableSize() {
        return BioUtils.getAvailableSize(this);
    }

    @Override
    public java.nio.channels.ReadableByteChannel getChannel()
            throws IOException {
        return org.restlet.engine.io.NioUtils.getChannel(getStream());
    }

    /**
     * Returns the range specific to this wrapper. The wrapped representation
     * must not have a range set itself.
     * 
     * @return The range specific to this wrapper.
     */
    @Override
    public Range getRange() {
        return this.range;
    }

    @Override
    public Reader getReader() throws IOException {
        return BioUtils.getReader(getStream(), getCharacterSet());
    }

    @Override
    public InputStream getStream() throws IOException {
        return new RangeInputStream(super.getStream(), getSize(), getRange());
    }

    @Override
    public String getText() throws IOException {
        return BioUtils.getText(this);
    }

    /**
     * Sets the range specific to this wrapper. This will not affect the wrapped
     * representation.
     * 
     * @param range
     *            The range specific to this wrapper.
     */
    @Override
    public void setRange(Range range) {
        this.range = range;
    }

    @Override
    public void write(java.io.Writer writer) throws IOException {
        write(BioUtils.getStream(writer));
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        BioUtils.copy(getStream(), outputStream);
    }

    @Override
    public void write(WritableByteChannel writableChannel) throws IOException {
        write(NioUtils.getStream(writableChannel));
    }

}
