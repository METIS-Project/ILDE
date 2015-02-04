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

public class KalturaSyndicationFeedService extends KalturaServiceBase {
    public KalturaSyndicationFeedService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaBaseSyndicationFeed add(KalturaBaseSyndicationFeed syndicationFeed) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (syndicationFeed != null) kparams.add("syndicationFeed", syndicationFeed.toParams());
        this.kalturaClient.queueServiceCall("syndicationfeed", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBaseSyndicationFeed)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaBaseSyndicationFeed get(String id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("syndicationfeed", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBaseSyndicationFeed)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaBaseSyndicationFeed update(String id, KalturaBaseSyndicationFeed syndicationFeed) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        if (syndicationFeed != null) kparams.add("syndicationFeed", syndicationFeed.toParams());
        this.kalturaClient.queueServiceCall("syndicationfeed", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBaseSyndicationFeed)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(String id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("syndicationfeed", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaBaseSyndicationFeedListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaBaseSyndicationFeedListResponse list(KalturaBaseSyndicationFeedFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaBaseSyndicationFeedListResponse list(KalturaBaseSyndicationFeedFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("syndicationfeed", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBaseSyndicationFeedListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaSyndicationFeedEntryCount getEntryCount(String feedId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("feedId", feedId);
        this.kalturaClient.queueServiceCall("syndicationfeed", "getEntryCount", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaSyndicationFeedEntryCount)KalturaObjectFactory.create(resultXmlElement);
    }

    public String requestConversion(String feedId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("feedId", feedId);
        this.kalturaClient.queueServiceCall("syndicationfeed", "requestConversion", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return resultText;
    }
}
