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

public class KalturaUserRoleService extends KalturaServiceBase {
    public KalturaUserRoleService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaUserRole add(KalturaUserRole userRole) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (userRole != null) kparams.add("userRole", userRole.toParams());
        this.kalturaClient.queueServiceCall("userrole", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUserRole)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaUserRole get(int userRoleId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("userRoleId", userRoleId);
        this.kalturaClient.queueServiceCall("userrole", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUserRole)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaUserRole update(int userRoleId, KalturaUserRole userRole) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("userRoleId", userRoleId);
        if (userRole != null) kparams.add("userRole", userRole.toParams());
        this.kalturaClient.queueServiceCall("userrole", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUserRole)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaUserRole delete(int userRoleId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("userRoleId", userRoleId);
        this.kalturaClient.queueServiceCall("userrole", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUserRole)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaUserRoleListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaUserRoleListResponse list(KalturaUserRoleFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaUserRoleListResponse list(KalturaUserRoleFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("userrole", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUserRoleListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaUserRole clone(int userRoleId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("userRoleId", userRoleId);
        this.kalturaClient.queueServiceCall("userrole", "clone", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUserRole)KalturaObjectFactory.create(resultXmlElement);
    }
}
