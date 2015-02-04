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

public class KalturaFlavorParamsService extends KalturaServiceBase {
    public KalturaFlavorParamsService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaFlavorParams add(KalturaFlavorParams flavorParams) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (flavorParams != null) kparams.add("flavorParams", flavorParams.toParams());
        this.kalturaClient.queueServiceCall("flavorparams", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaFlavorParams)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaFlavorParams get(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("flavorparams", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaFlavorParams)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaFlavorParams update(int id, KalturaFlavorParams flavorParams) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        if (flavorParams != null) kparams.add("flavorParams", flavorParams.toParams());
        this.kalturaClient.queueServiceCall("flavorparams", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaFlavorParams)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("flavorparams", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaFlavorParamsListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaFlavorParamsListResponse list(KalturaFlavorParamsFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaFlavorParamsListResponse list(KalturaFlavorParamsFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("flavorparams", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaFlavorParamsListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public List<KalturaFlavorParams> getByConversionProfileId(int conversionProfileId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("conversionProfileId", conversionProfileId);
        this.kalturaClient.queueServiceCall("flavorparams", "getByConversionProfileId", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        List<KalturaFlavorParams> list = new ArrayList<KalturaFlavorParams>();
        for(int i = 0; i < resultXmlElement.getChildNodes().getLength(); i++) {
            Element node = (Element)resultXmlElement.getChildNodes().item(i);
            list.add((KalturaFlavorParams)KalturaObjectFactory.create(node));
        }
        return list;
    }
}
