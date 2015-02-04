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

public class KalturaEntryDistributionService extends KalturaServiceBase {
    public KalturaEntryDistributionService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaEntryDistribution add(KalturaEntryDistribution entryDistribution) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (entryDistribution != null) kparams.add("entryDistribution", entryDistribution.toParams());
        this.kalturaClient.queueServiceCall("contentdistribution_entrydistribution", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaEntryDistribution)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaEntryDistribution get(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("contentdistribution_entrydistribution", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaEntryDistribution)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaEntryDistribution validate(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("contentdistribution_entrydistribution", "validate", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaEntryDistribution)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaEntryDistribution update(int id, KalturaEntryDistribution entryDistribution) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        if (entryDistribution != null) kparams.add("entryDistribution", entryDistribution.toParams());
        this.kalturaClient.queueServiceCall("contentdistribution_entrydistribution", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaEntryDistribution)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("contentdistribution_entrydistribution", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaEntryDistributionListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaEntryDistributionListResponse list(KalturaEntryDistributionFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaEntryDistributionListResponse list(KalturaEntryDistributionFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("contentdistribution_entrydistribution", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaEntryDistributionListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaEntryDistribution submitAdd(int id) throws KalturaApiException {
        return this.submitAdd(id, false);
    }

    public KalturaEntryDistribution submitAdd(int id, boolean submitWhenReady) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        kparams.addBoolIfNotNull("submitWhenReady", submitWhenReady);
        this.kalturaClient.queueServiceCall("contentdistribution_entrydistribution", "submitAdd", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaEntryDistribution)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaEntryDistribution submitUpdate(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("contentdistribution_entrydistribution", "submitUpdate", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaEntryDistribution)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaEntryDistribution submitFetchReport(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("contentdistribution_entrydistribution", "submitFetchReport", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaEntryDistribution)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaEntryDistribution submitDelete(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("contentdistribution_entrydistribution", "submitDelete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaEntryDistribution)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaEntryDistribution retrySubmit(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("contentdistribution_entrydistribution", "retrySubmit", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaEntryDistribution)KalturaObjectFactory.create(resultXmlElement);
    }
}
