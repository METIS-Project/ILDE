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

public class KalturaConversionProfileService extends KalturaServiceBase {
    public KalturaConversionProfileService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaConversionProfile setAsDefault(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("conversionprofile", "setAsDefault", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaConversionProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaConversionProfile getDefault() throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        this.kalturaClient.queueServiceCall("conversionprofile", "getDefault", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaConversionProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaConversionProfile add(KalturaConversionProfile conversionProfile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (conversionProfile != null) kparams.add("conversionProfile", conversionProfile.toParams());
        this.kalturaClient.queueServiceCall("conversionprofile", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaConversionProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaConversionProfile get(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("conversionprofile", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaConversionProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaConversionProfile update(int id, KalturaConversionProfile conversionProfile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        if (conversionProfile != null) kparams.add("conversionProfile", conversionProfile.toParams());
        this.kalturaClient.queueServiceCall("conversionprofile", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaConversionProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("conversionprofile", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaConversionProfileListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaConversionProfileListResponse list(KalturaConversionProfileFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaConversionProfileListResponse list(KalturaConversionProfileFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("conversionprofile", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaConversionProfileListResponse)KalturaObjectFactory.create(resultXmlElement);
    }
}
