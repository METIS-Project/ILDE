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

public class KalturaAnnotationService extends KalturaServiceBase {
    public KalturaAnnotationService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaAnnotationListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaAnnotationListResponse list(KalturaAnnotationFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaAnnotationListResponse list(KalturaAnnotationFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("annotation_annotation", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaAnnotationListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaAnnotation add(KalturaAnnotation annotation) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (annotation != null) kparams.add("annotation", annotation.toParams());
        this.kalturaClient.queueServiceCall("annotation_annotation", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaAnnotation)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaAnnotation get(String id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("annotation_annotation", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaAnnotation)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(String id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("annotation_annotation", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaAnnotation update(String id, KalturaAnnotation annotation) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        if (annotation != null) kparams.add("annotation", annotation.toParams());
        this.kalturaClient.queueServiceCall("annotation_annotation", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaAnnotation)KalturaObjectFactory.create(resultXmlElement);
    }
}
