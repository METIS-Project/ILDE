package com.kaltura.client.services;

import org.w3c.dom.Element;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaServiceBase;
import com.kaltura.client.utils.XmlUtils;
import com.kaltura.client.enums.*;
import com.kaltura.client.types.*;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import com.kaltura.client.KalturaFiles;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaShortLinkService extends KalturaServiceBase {
    public KalturaShortLinkService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaShortLinkListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaShortLinkListResponse list(KalturaShortLinkFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaShortLinkListResponse list(KalturaShortLinkFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("shortlink_shortlink", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaShortLinkListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaShortLink add(KalturaShortLink shortLink) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (shortLink != null) kparams.add("shortLink", shortLink.toParams());
        this.kalturaClient.queueServiceCall("shortlink_shortlink", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaShortLink)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaShortLink get(String id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("shortlink_shortlink", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaShortLink)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaShortLink update(String id, KalturaShortLink shortLink) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        if (shortLink != null) kparams.add("shortLink", shortLink.toParams());
        this.kalturaClient.queueServiceCall("shortlink_shortlink", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaShortLink)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaShortLink delete(String id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("shortlink_shortlink", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaShortLink)KalturaObjectFactory.create(resultXmlElement);
    }
}
