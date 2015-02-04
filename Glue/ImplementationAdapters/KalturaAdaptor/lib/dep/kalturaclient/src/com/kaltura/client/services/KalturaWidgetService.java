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

public class KalturaWidgetService extends KalturaServiceBase {
    public KalturaWidgetService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaWidget add(KalturaWidget widget) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (widget != null) kparams.add("widget", widget.toParams());
        this.kalturaClient.queueServiceCall("widget", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaWidget)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaWidget update(String id, KalturaWidget widget) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        if (widget != null) kparams.add("widget", widget.toParams());
        this.kalturaClient.queueServiceCall("widget", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaWidget)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaWidget get(String id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("widget", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaWidget)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaWidget clone(KalturaWidget widget) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (widget != null) kparams.add("widget", widget.toParams());
        this.kalturaClient.queueServiceCall("widget", "clone", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaWidget)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaWidgetListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaWidgetListResponse list(KalturaWidgetFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaWidgetListResponse list(KalturaWidgetFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("widget", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaWidgetListResponse)KalturaObjectFactory.create(resultXmlElement);
    }
}
