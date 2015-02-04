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

public class KalturaPermissionService extends KalturaServiceBase {
    public KalturaPermissionService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaPermission add(KalturaPermission permission) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (permission != null) kparams.add("permission", permission.toParams());
        this.kalturaClient.queueServiceCall("permission", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPermission)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaPermission get(String permissionName) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("permissionName", permissionName);
        this.kalturaClient.queueServiceCall("permission", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPermission)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaPermission update(String permissionName, KalturaPermission permission) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("permissionName", permissionName);
        if (permission != null) kparams.add("permission", permission.toParams());
        this.kalturaClient.queueServiceCall("permission", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPermission)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaPermission delete(String permissionName) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("permissionName", permissionName);
        this.kalturaClient.queueServiceCall("permission", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPermission)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaPermissionListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaPermissionListResponse list(KalturaPermissionFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaPermissionListResponse list(KalturaPermissionFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("permission", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPermissionListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public String getCurrentPermissions() throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        this.kalturaClient.queueServiceCall("permission", "getCurrentPermissions", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return resultText;
    }
}
