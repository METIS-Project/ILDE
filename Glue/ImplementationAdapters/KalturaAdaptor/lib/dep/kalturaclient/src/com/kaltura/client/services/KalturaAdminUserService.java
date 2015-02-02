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

public class KalturaAdminUserService extends KalturaServiceBase {
    public KalturaAdminUserService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaAdminUser updatePassword(String email, String password) throws KalturaApiException {
        return this.updatePassword(email, password, "");
    }

    public KalturaAdminUser updatePassword(String email, String password, String newEmail) throws KalturaApiException {
        return this.updatePassword(email, password, newEmail, "");
    }

    public KalturaAdminUser updatePassword(String email, String password, String newEmail, String newPassword) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("email", email);
        kparams.addStringIfNotNull("password", password);
        kparams.addStringIfNotNull("newEmail", newEmail);
        kparams.addStringIfNotNull("newPassword", newPassword);
        this.kalturaClient.queueServiceCall("adminuser", "updatePassword", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaAdminUser)KalturaObjectFactory.create(resultXmlElement);
    }

    public void resetPassword(String email) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("email", email);
        this.kalturaClient.queueServiceCall("adminuser", "resetPassword", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public String login(String email, String password) throws KalturaApiException {
        return this.login(email, password, Integer.MIN_VALUE);
    }

    public String login(String email, String password, int partnerId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("email", email);
        kparams.addStringIfNotNull("password", password);
        kparams.addIntIfNotNull("partnerId", partnerId);
        this.kalturaClient.queueServiceCall("adminuser", "login", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return resultText;
    }

    public void setInitialPassword(String hashKey, String newPassword) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("hashKey", hashKey);
        kparams.addStringIfNotNull("newPassword", newPassword);
        this.kalturaClient.queueServiceCall("adminuser", "setInitialPassword", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }
}
