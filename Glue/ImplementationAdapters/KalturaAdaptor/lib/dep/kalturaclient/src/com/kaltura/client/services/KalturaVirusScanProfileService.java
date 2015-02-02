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

public class KalturaVirusScanProfileService extends KalturaServiceBase {
    public KalturaVirusScanProfileService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaVirusScanProfileListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaVirusScanProfileListResponse list(KalturaVirusScanProfileFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaVirusScanProfileListResponse list(KalturaVirusScanProfileFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("virusscan_virusscanprofile", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaVirusScanProfileListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaVirusScanProfile add(KalturaVirusScanProfile virusScanProfile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (virusScanProfile != null) kparams.add("virusScanProfile", virusScanProfile.toParams());
        this.kalturaClient.queueServiceCall("virusscan_virusscanprofile", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaVirusScanProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaVirusScanProfile get(int virusScanProfileId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("virusScanProfileId", virusScanProfileId);
        this.kalturaClient.queueServiceCall("virusscan_virusscanprofile", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaVirusScanProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaVirusScanProfile update(int virusScanProfileId, KalturaVirusScanProfile virusScanProfile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("virusScanProfileId", virusScanProfileId);
        if (virusScanProfile != null) kparams.add("virusScanProfile", virusScanProfile.toParams());
        this.kalturaClient.queueServiceCall("virusscan_virusscanprofile", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaVirusScanProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaVirusScanProfile delete(int virusScanProfileId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("virusScanProfileId", virusScanProfileId);
        this.kalturaClient.queueServiceCall("virusscan_virusscanprofile", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaVirusScanProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public int scan(String flavorAssetId) throws KalturaApiException {
        return this.scan(flavorAssetId, Integer.MIN_VALUE);
    }

    public int scan(String flavorAssetId, int virusScanProfileId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("flavorAssetId", flavorAssetId);
        kparams.addIntIfNotNull("virusScanProfileId", virusScanProfileId);
        this.kalturaClient.queueServiceCall("virusscan_virusscanprofile", "scan", kparams);
        if (this.kalturaClient.isMultiRequest())
            return 0;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return Integer.parseInt(resultText);
    }
}
