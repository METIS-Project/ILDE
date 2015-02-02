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

public class KalturaUserService extends KalturaServiceBase {
    public KalturaUserService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaUser add(KalturaUser user) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (user != null) kparams.add("user", user.toParams());
        this.kalturaClient.queueServiceCall("user", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUser)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaUser update(String userId, KalturaUser user) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("userId", userId);
        if (user != null) kparams.add("user", user.toParams());
        this.kalturaClient.queueServiceCall("user", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUser)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaUser get(String userId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("userId", userId);
        this.kalturaClient.queueServiceCall("user", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUser)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaUser getByLoginId(String loginId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("loginId", loginId);
        this.kalturaClient.queueServiceCall("user", "getByLoginId", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUser)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaUser delete(String userId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("userId", userId);
        this.kalturaClient.queueServiceCall("user", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUser)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaUserListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaUserListResponse list(KalturaUserFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaUserListResponse list(KalturaUserFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("user", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUserListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public void notifyBan(String userId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("userId", userId);
        this.kalturaClient.queueServiceCall("user", "notifyBan", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public String login(int partnerId, String userId, String password) throws KalturaApiException {
        return this.login(partnerId, userId, password, 86400);
    }

    public String login(int partnerId, String userId, String password, int expiry) throws KalturaApiException {
        return this.login(partnerId, userId, password, expiry, "*");
    }

    public String login(int partnerId, String userId, String password, int expiry, String privileges) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("partnerId", partnerId);
        kparams.addStringIfNotNull("userId", userId);
        kparams.addStringIfNotNull("password", password);
        kparams.addIntIfNotNull("expiry", expiry);
        kparams.addStringIfNotNull("privileges", privileges);
        this.kalturaClient.queueServiceCall("user", "login", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return resultText;
    }

    public String loginByLoginId(String loginId, String password) throws KalturaApiException {
        return this.loginByLoginId(loginId, password, Integer.MIN_VALUE);
    }

    public String loginByLoginId(String loginId, String password, int partnerId) throws KalturaApiException {
        return this.loginByLoginId(loginId, password, partnerId, 86400);
    }

    public String loginByLoginId(String loginId, String password, int partnerId, int expiry) throws KalturaApiException {
        return this.loginByLoginId(loginId, password, partnerId, expiry, "*");
    }

    public String loginByLoginId(String loginId, String password, int partnerId, int expiry, String privileges) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("loginId", loginId);
        kparams.addStringIfNotNull("password", password);
        kparams.addIntIfNotNull("partnerId", partnerId);
        kparams.addIntIfNotNull("expiry", expiry);
        kparams.addStringIfNotNull("privileges", privileges);
        this.kalturaClient.queueServiceCall("user", "loginByLoginId", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return resultText;
    }

    public void updateLoginData(String oldLoginId, String password) throws KalturaApiException {
        this.updateLoginData(oldLoginId, password, "");
    }

    public void updateLoginData(String oldLoginId, String password, String newLoginId) throws KalturaApiException {
        this.updateLoginData(oldLoginId, password, newLoginId, "");
    }

    public void updateLoginData(String oldLoginId, String password, String newLoginId, String newPassword) throws KalturaApiException {
        this.updateLoginData(oldLoginId, password, newLoginId, newPassword, null);
    }

    public void updateLoginData(String oldLoginId, String password, String newLoginId, String newPassword, String newFirstName) throws KalturaApiException {
        this.updateLoginData(oldLoginId, password, newLoginId, newPassword, newFirstName, null);
    }

    public void updateLoginData(String oldLoginId, String password, String newLoginId, String newPassword, String newFirstName, String newLastName) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("oldLoginId", oldLoginId);
        kparams.addStringIfNotNull("password", password);
        kparams.addStringIfNotNull("newLoginId", newLoginId);
        kparams.addStringIfNotNull("newPassword", newPassword);
        kparams.addStringIfNotNull("newFirstName", newFirstName);
        kparams.addStringIfNotNull("newLastName", newLastName);
        this.kalturaClient.queueServiceCall("user", "updateLoginData", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public void resetPassword(String email) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("email", email);
        this.kalturaClient.queueServiceCall("user", "resetPassword", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public void setInitialPassword(String hashKey, String newPassword) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("hashKey", hashKey);
        kparams.addStringIfNotNull("newPassword", newPassword);
        this.kalturaClient.queueServiceCall("user", "setInitialPassword", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaUser enableLogin(String userId, String loginId) throws KalturaApiException {
        return this.enableLogin(userId, loginId, null);
    }

    public KalturaUser enableLogin(String userId, String loginId, String password) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("userId", userId);
        kparams.addStringIfNotNull("loginId", loginId);
        kparams.addStringIfNotNull("password", password);
        this.kalturaClient.queueServiceCall("user", "enableLogin", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUser)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaUser disableLogin() throws KalturaApiException {
        return this.disableLogin(null);
    }

    public KalturaUser disableLogin(String userId) throws KalturaApiException {
        return this.disableLogin(userId, null);
    }

    public KalturaUser disableLogin(String userId, String loginId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("userId", userId);
        kparams.addStringIfNotNull("loginId", loginId);
        this.kalturaClient.queueServiceCall("user", "disableLogin", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUser)KalturaObjectFactory.create(resultXmlElement);
    }
}
