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

public class KalturaEmailIngestionProfileService extends KalturaServiceBase {
    public KalturaEmailIngestionProfileService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaEmailIngestionProfile add(KalturaEmailIngestionProfile EmailIP) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (EmailIP != null) kparams.add("EmailIP", EmailIP.toParams());
        this.kalturaClient.queueServiceCall("emailingestionprofile", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaEmailIngestionProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaEmailIngestionProfile getByEmailAddress(String emailAddress) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("emailAddress", emailAddress);
        this.kalturaClient.queueServiceCall("emailingestionprofile", "getByEmailAddress", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaEmailIngestionProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaEmailIngestionProfile get(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("emailingestionprofile", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaEmailIngestionProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaEmailIngestionProfile update(int id, KalturaEmailIngestionProfile EmailIP) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        if (EmailIP != null) kparams.add("EmailIP", EmailIP.toParams());
        this.kalturaClient.queueServiceCall("emailingestionprofile", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaEmailIngestionProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("emailingestionprofile", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaMediaEntry addMediaEntry(KalturaMediaEntry mediaEntry, String uploadTokenId, int emailProfId, String fromAddress, String emailMsgId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (mediaEntry != null) kparams.add("mediaEntry", mediaEntry.toParams());
        kparams.addStringIfNotNull("uploadTokenId", uploadTokenId);
        kparams.addIntIfNotNull("emailProfId", emailProfId);
        kparams.addStringIfNotNull("fromAddress", fromAddress);
        kparams.addStringIfNotNull("emailMsgId", emailMsgId);
        this.kalturaClient.queueServiceCall("emailingestionprofile", "addMediaEntry", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaEntry)KalturaObjectFactory.create(resultXmlElement);
    }
}
