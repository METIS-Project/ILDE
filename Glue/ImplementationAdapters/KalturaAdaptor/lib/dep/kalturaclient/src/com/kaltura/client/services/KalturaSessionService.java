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

public class KalturaSessionService extends KalturaServiceBase {
    public KalturaSessionService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public String start(String secret) throws KalturaApiException {
        return this.start(secret, "");
    }

    public String start(String secret, String userId) throws KalturaApiException {
        return this.start(secret, userId, KalturaSessionType.get(0));
    }

    public String start(String secret, String userId, KalturaSessionType type) throws KalturaApiException {
        return this.start(secret, userId, type, Integer.MIN_VALUE);
    }

    public String start(String secret, String userId, KalturaSessionType type, int partnerId) throws KalturaApiException {
        return this.start(secret, userId, type, partnerId, 86400);
    }

    public String start(String secret, String userId, KalturaSessionType type, int partnerId, int expiry) throws KalturaApiException {
        return this.start(secret, userId, type, partnerId, expiry, null);
    }

    public String start(String secret, String userId, KalturaSessionType type, int partnerId, int expiry, String privileges) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("secret", secret);
        kparams.addStringIfNotNull("userId", userId);
        kparams.addIntIfNotNull("type", type.getHashCode());
        kparams.addIntIfNotNull("partnerId", partnerId);
        kparams.addIntIfNotNull("expiry", expiry);
        kparams.addStringIfNotNull("privileges", privileges);
        this.kalturaClient.queueServiceCall("session", "start", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return resultText;
    }

    public void end() throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        this.kalturaClient.queueServiceCall("session", "end", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public String impersonate(String secret, int impersonatedPartnerId) throws KalturaApiException {
        return this.impersonate(secret, impersonatedPartnerId, "");
    }

    public String impersonate(String secret, int impersonatedPartnerId, String userId) throws KalturaApiException {
        return this.impersonate(secret, impersonatedPartnerId, userId, KalturaSessionType.get(0));
    }

    public String impersonate(String secret, int impersonatedPartnerId, String userId, KalturaSessionType type) throws KalturaApiException {
        return this.impersonate(secret, impersonatedPartnerId, userId, type, Integer.MIN_VALUE);
    }

    public String impersonate(String secret, int impersonatedPartnerId, String userId, KalturaSessionType type, int partnerId) throws KalturaApiException {
        return this.impersonate(secret, impersonatedPartnerId, userId, type, partnerId, 86400);
    }

    public String impersonate(String secret, int impersonatedPartnerId, String userId, KalturaSessionType type, int partnerId, int expiry) throws KalturaApiException {
        return this.impersonate(secret, impersonatedPartnerId, userId, type, partnerId, expiry, null);
    }

    public String impersonate(String secret, int impersonatedPartnerId, String userId, KalturaSessionType type, int partnerId, int expiry, String privileges) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("secret", secret);
        kparams.addIntIfNotNull("impersonatedPartnerId", impersonatedPartnerId);
        kparams.addStringIfNotNull("userId", userId);
        kparams.addIntIfNotNull("type", type.getHashCode());
        kparams.addIntIfNotNull("partnerId", partnerId);
        kparams.addIntIfNotNull("expiry", expiry);
        kparams.addStringIfNotNull("privileges", privileges);
        this.kalturaClient.queueServiceCall("session", "impersonate", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return resultText;
    }

    public KalturaStartWidgetSessionResponse startWidgetSession(String widgetId) throws KalturaApiException {
        return this.startWidgetSession(widgetId, 86400);
    }

    public KalturaStartWidgetSessionResponse startWidgetSession(String widgetId, int expiry) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("widgetId", widgetId);
        kparams.addIntIfNotNull("expiry", expiry);
        this.kalturaClient.queueServiceCall("session", "startWidgetSession", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaStartWidgetSessionResponse)KalturaObjectFactory.create(resultXmlElement);
    }
}
