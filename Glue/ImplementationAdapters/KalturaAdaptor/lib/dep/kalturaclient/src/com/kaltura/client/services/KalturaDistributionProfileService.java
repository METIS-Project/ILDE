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

public class KalturaDistributionProfileService extends KalturaServiceBase {
    public KalturaDistributionProfileService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaDistributionProfile add(KalturaDistributionProfile distributionProfile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (distributionProfile != null) kparams.add("distributionProfile", distributionProfile.toParams());
        this.kalturaClient.queueServiceCall("contentdistribution_distributionprofile", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDistributionProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDistributionProfile get(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("contentdistribution_distributionprofile", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDistributionProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDistributionProfile update(int id, KalturaDistributionProfile distributionProfile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        if (distributionProfile != null) kparams.add("distributionProfile", distributionProfile.toParams());
        this.kalturaClient.queueServiceCall("contentdistribution_distributionprofile", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDistributionProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDistributionProfile updateStatus(int id, KalturaDistributionProfileStatus status) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        kparams.addIntIfNotNull("status", status.getHashCode());
        this.kalturaClient.queueServiceCall("contentdistribution_distributionprofile", "updateStatus", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDistributionProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("contentdistribution_distributionprofile", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaDistributionProfileListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaDistributionProfileListResponse list(KalturaDistributionProfileFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaDistributionProfileListResponse list(KalturaDistributionProfileFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("contentdistribution_distributionprofile", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDistributionProfileListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDistributionProfileListResponse listByPartner() throws KalturaApiException {
        return this.listByPartner(null);
    }

    public KalturaDistributionProfileListResponse listByPartner(KalturaPartnerFilter filter) throws KalturaApiException {
        return this.listByPartner(filter, null);
    }

    public KalturaDistributionProfileListResponse listByPartner(KalturaPartnerFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("contentdistribution_distributionprofile", "listByPartner", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDistributionProfileListResponse)KalturaObjectFactory.create(resultXmlElement);
    }
}
