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

package org.restlet;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.restlet.data.CacheDirective;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.RecipientInfo;
import org.restlet.data.Warning;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

/**
 * Generic message exchanged between components.
 * 
 * @author Jerome Louvel
 */
public abstract class Message {
    /** The modifiable attributes map. */
    private volatile Map<String, Object> attributes;

    /** The caching directives. */
    private volatile List<CacheDirective> cacheDirectives;

    /** The date and time at which the message was originated. */
    private volatile Date date;

    /** The payload of the message. */
    private volatile Representation entity;

    /** The optional cached Form. */
    private volatile Form entityForm;

    /** The optional cached text. */
    private volatile String entityText;

    /** Callback invoked after sending the response. */
    private volatile Uniform onSent;

    /** The intermediary recipients info. */
    private volatile List<RecipientInfo> recipientsInfo;

    /** The additional warnings information. */
    private volatile List<Warning> warnings;

    /**
     * Constructor.
     */
    public Message() {
        this((Representation) null);
    }

    /**
     * Constructor.
     * 
     * @param entity
     *            The payload of the message.
     */
    public Message(Representation entity) {
        this.attributes = null;
        this.cacheDirectives = null;
        this.date = null;
        this.entity = entity;
        this.entityForm = null;
        this.entityText = null;
        this.onSent = null;
        this.recipientsInfo = null;
        this.warnings = null;
    }

    /**
     * Returns the modifiable map of attributes that can be used by developers
     * to save information relative to the message. Creates a new instance if no
     * one has been set. This is an easier alternative to the creation of a
     * wrapper instance around the whole message.<br>
     * <br>
     * 
     * In addition, this map is a shared space between the developer and the
     * connectors. In this case, it is used to exchange information that is not
     * uniform across all protocols and couldn't therefore be directly included
     * in the API. For this purpose, all attribute names starting with
     * "org.restlet" are reserved. Currently the following attributes are used:
     * <table>
     * <tr>
     * <th>Attribute name</th>
     * <th>Class name</th>
     * <th>Description</th>
     * </tr>
     * <tr>
     * <td>org.restlet.http.headers</td>
     * <td>org.restlet.data.Form</td>
     * <td>Server HTTP connectors must provide all request headers and client
     * HTTP connectors must provide all response headers, exactly as they were
     * received. In addition, developers can also use this attribute to specify
     * <b>non-standard</b> headers that should be added to the request or to the
     * response.</td>
     * </tr>
     * <tr>
     * <td>org.restlet.https.clientCertificates</td>
     * <td>List<java.security.cert.Certificate></td>
     * <td>For requests received via a secure connector, indicates the ordered
     * list of client certificates, if they are available and accessible.</td>
     * </tr>
     * </table>
     * <br>
     * Most of the standard HTTP headers are directly supported via the Restlet
     * API. Thus, adding such HTTP headers is forbidden because it could
     * conflict with the connector's internal behavior, limit portability or
     * prevent future optimizations. The other standard HTTP headers (that are
     * not supported) can be added as attributes via the
     * "org.restlet.http.headers" key.<br>
     * 
     * @return The modifiable attributes map.
     */
    public Map<String, Object> getAttributes() {
        // Lazy initialization with double-check.
        Map<String, Object> r = this.attributes;
        if (r == null) {
            synchronized (this) {
                r = this.attributes;
                if (r == null) {
                    this.attributes = r = new ConcurrentHashMap<String, Object>();
                }
            }
        }

        return this.attributes;
    }

    /**
     * Returns the cache directives.<br>
     * <br>
     * Note that when used with HTTP connectors, this property maps to the
     * "Cache-Control" header.
     * 
     * @return The cache directives.
     */
    public List<CacheDirective> getCacheDirectives() {
        // Lazy initialization with double-check.
        List<CacheDirective> r = this.cacheDirectives;
        if (r == null) {
            synchronized (this) {
                r = this.cacheDirectives;
                if (r == null) {
                    this.cacheDirectives = r = new CopyOnWriteArrayList<CacheDirective>();
                }
            }
        }
        return r;
    }

    /**
     * Returns the date and time at which the message was originated.
     * 
     * @return The date and time at which the message was originated.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the entity representation.
     * 
     * @return The entity representation.
     */
    public Representation getEntity() {
        return this.entity;
    }

    /**
     * Returns the entity as a form. This method can be called several times and
     * will always return the same form instance. Note that if the entity is
     * large this method can result in important memory consumption.
     * 
     * @return The entity as a form.
     * @deprecated Will be removed in future release 2.1.
     */
    @Deprecated
    public Form getEntityAsForm() {
        if (this.entityForm == null) {
            this.entityForm = new Form(getEntity());
        }

        return this.entityForm;
    }

    /**
     * Returns the entity as text. This method can be called several times and
     * will always return the same text. Note that if the entity is large this
     * method can result in important memory consumption.
     * 
     * @return The entity as text.
     */
    public String getEntityAsText() {
        if (this.entityText == null) {
            try {
                this.entityText = getEntity().getText();
            } catch (java.io.IOException e) {
                Context.getCurrentLogger().log(java.util.logging.Level.FINE,
                        "Unable to get the entity text.", e);
            }
        }

        return this.entityText;
    }

    /**
     * Returns the callback invoked after sending the message.
     * 
     * @return The callback invoked after sending the message.
     */
    public Uniform getOnSent() {
        return onSent;
    }

    /**
     * Returns the intermediary recipient information.<br>
     * <br>
     * Note that when used with HTTP connectors, this property maps to the "Via"
     * headers.
     * 
     * @return The intermediary recipient information.
     */
    public List<RecipientInfo> getRecipientsInfo() {
        // Lazy initialization with double-check.
        List<RecipientInfo> r = this.recipientsInfo;
        if (r == null) {
            synchronized (this) {
                r = this.recipientsInfo;
                if (r == null) {
                    this.recipientsInfo = r = new CopyOnWriteArrayList<RecipientInfo>();
                }
            }
        }
        return r;
    }

    /**
     * Returns the additional warnings information.<br>
     * <br>
     * Note that when used with HTTP connectors, this property maps to the
     * "Warning" headers.
     * 
     * @return The additional warnings information.
     */
    public List<Warning> getWarnings() {
        // Lazy initialization with double-check.
        List<Warning> r = this.warnings;
        if (r == null) {
            synchronized (this) {
                r = this.warnings;
                if (r == null) {
                    this.warnings = r = new CopyOnWriteArrayList<Warning>();
                }
            }
        }
        return r;
    }

    /**
     * Indicates if the message was or will be exchanged confidentially, for
     * example via a SSL-secured connection.
     * 
     * @return True if the message is confidential.
     */
    public abstract boolean isConfidential();

    /**
     * Indicates if a content is available and can be sent or received. Several
     * conditions must be met: the content must exists and have some available
     * data.
     * 
     * @return True if a content is available and can be sent.
     */
    public boolean isEntityAvailable() {
        // The declaration of the "result" variable is a workaround for the GWT
        // platform. Please keep it!
        boolean result = (getEntity() != null) && getEntity().isAvailable();
        return result;
    }

    /**
     * Releases the message's entity if present.
     * 
     * @see org.restlet.representation.Representation#release()
     */
    public void release() {
        if (getEntity() != null) {
            getEntity().release();
        }
    }

    /**
     * Sets the modifiable map of attributes. This method clears the current map
     * and puts all entries in the parameter map.
     * 
     * @param attributes
     *            A map of attributes
     */
    public void setAttributes(Map<String, Object> attributes) {
        synchronized (getAttributes()) {
            if (attributes != getAttributes()) {
                getAttributes().clear();

                if (attributes != null) {
                    getAttributes().putAll(attributes);
                }
            }
        }
    }

    /**
     * Sets the cache directives. Note that when used with HTTP connectors, this
     * property maps to the "Cache-Control" header. This method clears the
     * current list and adds all entries in the parameter list.
     * 
     * @param cacheDirectives
     *            The cache directives.
     */
    public void setCacheDirectives(List<CacheDirective> cacheDirectives) {
        synchronized (getCacheDirectives()) {
            if (cacheDirectives != getCacheDirectives()) {
                getCacheDirectives().clear();

                if (cacheDirectives != null) {
                    getCacheDirectives().addAll(cacheDirectives);
                }
            }
        }
    }

    /**
     * Sets the date and time at which the message was originated.
     * 
     * @param date
     *            The date and time at which the message was originated.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Sets the entity representation.
     * 
     * @param entity
     *            The entity representation.
     */
    public void setEntity(Representation entity) {
        this.entity = entity;
    }

    /**
     * Sets a textual entity.
     * 
     * @param value
     *            The represented string.
     * @param mediaType
     *            The representation's media type.
     */
    public void setEntity(String value, MediaType mediaType) {
        setEntity(new StringRepresentation(value, mediaType));
    }

    /**
     * Sets the callback invoked after sending the message.
     * 
     * @param onSentCallback
     *            The callback invoked after sending the message.
     */
    public void setOnSent(Uniform onSentCallback) {
        this.onSent = onSentCallback;
    }

    /**
     * Sets the modifiable list of intermediary recipients. Note that when used
     * with HTTP connectors, this property maps to the "Via" headers. This
     * method clears the current list and adds all entries in the parameter
     * list.
     * 
     * @param recipientsInfo
     *            A list of intermediary recipients.
     */
    public void setRecipientsInfo(List<RecipientInfo> recipientsInfo) {
        synchronized (getRecipientsInfo()) {
            if (recipientsInfo != getRecipientsInfo()) {
                getRecipientsInfo().clear();

                if (recipientsInfo != null) {
                    getRecipientsInfo().addAll(recipientsInfo);
                }
            }
        }
    }

    /**
     * Sets the additional warnings information. Note that when used with HTTP
     * connectors, this property maps to the "Warning" headers. This method
     * clears the current list and adds all entries in the parameter list.
     * 
     * @param warnings
     *            The warnings.
     */
    public void setWarnings(List<Warning> warnings) {
        synchronized (getWarnings()) {
            if (warnings != getWarnings()) {
                getWarnings().clear();

                if (warnings != null) {
                    getWarnings().addAll(warnings);
                }
            }
        }
    }

}
