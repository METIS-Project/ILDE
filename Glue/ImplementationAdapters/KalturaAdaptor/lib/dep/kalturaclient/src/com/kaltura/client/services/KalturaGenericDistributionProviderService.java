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

public class KalturaGenericDistributionProviderService extends KalturaServiceBase {
    public KalturaGenericDistributionProviderService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaGenericDistributionProvider add(KalturaGenericDistributionProvider genericDistributionProvider) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (genericDistributionProvider != null) kparams.add("genericDistributionProvider", genericDistributionProvider.toParams());
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovider", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaGenericDistributionProvider)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaGenericDistributionProvider get(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovider", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaGenericDistributionProvider)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaGenericDistributionProvider update(int id, KalturaGenericDistributionProvider genericDistributionProvider) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        if (genericDistributionProvider != null) kparams.add("genericDistributionProvider", genericDistributionProvider.toParams());
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovider", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaGenericDistributionProvider)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovider", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaGenericDistributionProviderListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaGenericDistributionProviderListResponse list(KalturaGenericDistributionProviderFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaGenericDistributionProviderListResponse list(KalturaGenericDistributionProviderFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovider", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaGenericDistributionProviderListResponse)KalturaObjectFactory.create(resultXmlElement);
    }
}
