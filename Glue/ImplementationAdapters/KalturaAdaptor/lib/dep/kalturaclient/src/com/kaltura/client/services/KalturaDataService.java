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

public class KalturaDataService extends KalturaServiceBase {
    public KalturaDataService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaDataEntry add(KalturaDataEntry dataEntry) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (dataEntry != null) kparams.add("dataEntry", dataEntry.toParams());
        this.kalturaClient.queueServiceCall("data", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDataEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDataEntry get(String entryId) throws KalturaApiException {
        return this.get(entryId, -1);
    }

    public KalturaDataEntry get(String entryId, int version) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addIntIfNotNull("version", version);
        this.kalturaClient.queueServiceCall("data", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDataEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDataEntry update(String entryId, KalturaDataEntry documentEntry) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        if (documentEntry != null) kparams.add("documentEntry", documentEntry.toParams());
        this.kalturaClient.queueServiceCall("data", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDataEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("data", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaDataListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaDataListResponse list(KalturaDataEntryFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaDataListResponse list(KalturaDataEntryFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("data", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDataListResponse)KalturaObjectFactory.create(resultXmlElement);
    }
}
