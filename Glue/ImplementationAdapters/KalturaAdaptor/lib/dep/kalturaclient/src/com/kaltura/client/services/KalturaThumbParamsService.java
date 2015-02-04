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

public class KalturaThumbParamsService extends KalturaServiceBase {
    public KalturaThumbParamsService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaThumbParams add(KalturaThumbParams thumbParams) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (thumbParams != null) kparams.add("thumbParams", thumbParams.toParams());
        this.kalturaClient.queueServiceCall("thumbparams", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaThumbParams)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaThumbParams get(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("thumbparams", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaThumbParams)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaThumbParams update(int id, KalturaThumbParams thumbParams) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        if (thumbParams != null) kparams.add("thumbParams", thumbParams.toParams());
        this.kalturaClient.queueServiceCall("thumbparams", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaThumbParams)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("thumbparams", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaThumbParamsListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaThumbParamsListResponse list(KalturaThumbParamsFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaThumbParamsListResponse list(KalturaThumbParamsFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("thumbparams", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaThumbParamsListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public List<KalturaThumbParams> getByConversionProfileId(int conversionProfileId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("conversionProfileId", conversionProfileId);
        this.kalturaClient.queueServiceCall("thumbparams", "getByConversionProfileId", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        List<KalturaThumbParams> list = new ArrayList<KalturaThumbParams>();
        for(int i = 0; i < resultXmlElement.getChildNodes().getLength(); i++) {
            Element node = (Element)resultXmlElement.getChildNodes().item(i);
            list.add((KalturaThumbParams)KalturaObjectFactory.create(node));
        }
        return list;
    }
}
