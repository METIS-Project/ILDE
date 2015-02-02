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

public class KalturaPartnerService extends KalturaServiceBase {
    public KalturaPartnerService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaPartner register(KalturaPartner partner) throws KalturaApiException {
        return this.register(partner, "");
    }

    public KalturaPartner register(KalturaPartner partner, String cmsPassword) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (partner != null) kparams.add("partner", partner.toParams());
        kparams.addStringIfNotNull("cmsPassword", cmsPassword);
        this.kalturaClient.queueServiceCall("partner", "register", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPartner)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaPartner update(KalturaPartner partner) throws KalturaApiException {
        return this.update(partner, false);
    }

    public KalturaPartner update(KalturaPartner partner, boolean allowEmpty) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (partner != null) kparams.add("partner", partner.toParams());
        kparams.addBoolIfNotNull("allowEmpty", allowEmpty);
        this.kalturaClient.queueServiceCall("partner", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPartner)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaPartner getSecrets(int partnerId, String adminEmail, String cmsPassword) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("partnerId", partnerId);
        kparams.addStringIfNotNull("adminEmail", adminEmail);
        kparams.addStringIfNotNull("cmsPassword", cmsPassword);
        this.kalturaClient.queueServiceCall("partner", "getSecrets", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPartner)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaPartner getInfo() throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        this.kalturaClient.queueServiceCall("partner", "getInfo", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPartner)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaPartnerUsage getUsage() throws KalturaApiException {
        return this.getUsage();
    }

    public KalturaPartnerUsage getUsage(int year) throws KalturaApiException {
        return this.getUsage(year, 1);
    }

    public KalturaPartnerUsage getUsage(int year, int month) throws KalturaApiException {
        return this.getUsage(year, month, "days");
    }

    public KalturaPartnerUsage getUsage(int year, int month, String resolution) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("year", year);
        kparams.addIntIfNotNull("month", month);
        kparams.addStringIfNotNull("resolution", resolution);
        this.kalturaClient.queueServiceCall("partner", "getUsage", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPartnerUsage)KalturaObjectFactory.create(resultXmlElement);
    }
}
