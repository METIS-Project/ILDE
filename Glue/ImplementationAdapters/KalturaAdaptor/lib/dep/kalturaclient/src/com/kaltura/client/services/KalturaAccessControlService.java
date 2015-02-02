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

public class KalturaAccessControlService extends KalturaServiceBase {
    public KalturaAccessControlService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaAccessControl add(KalturaAccessControl accessControl) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (accessControl != null) kparams.add("accessControl", accessControl.toParams());
        this.kalturaClient.queueServiceCall("accesscontrol", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaAccessControl)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaAccessControl get(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("accesscontrol", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaAccessControl)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaAccessControl update(int id, KalturaAccessControl accessControl) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        if (accessControl != null) kparams.add("accessControl", accessControl.toParams());
        this.kalturaClient.queueServiceCall("accesscontrol", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaAccessControl)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("accesscontrol", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaAccessControlListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaAccessControlListResponse list(KalturaAccessControlFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaAccessControlListResponse list(KalturaAccessControlFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("accesscontrol", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaAccessControlListResponse)KalturaObjectFactory.create(resultXmlElement);
    }
}
