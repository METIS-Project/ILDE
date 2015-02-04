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

public class KalturaPermissionItemService extends KalturaServiceBase {
    public KalturaPermissionItemService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaPermissionItem add(KalturaPermissionItem permissionItem) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (permissionItem != null) kparams.add("permissionItem", permissionItem.toParams());
        this.kalturaClient.queueServiceCall("permissionitem", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPermissionItem)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaPermissionItem get(int permissionItemId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("permissionItemId", permissionItemId);
        this.kalturaClient.queueServiceCall("permissionitem", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPermissionItem)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaPermissionItem update(int permissionItemId, KalturaPermissionItem permissionItem) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("permissionItemId", permissionItemId);
        if (permissionItem != null) kparams.add("permissionItem", permissionItem.toParams());
        this.kalturaClient.queueServiceCall("permissionitem", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPermissionItem)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaPermissionItem delete(int permissionItemId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("permissionItemId", permissionItemId);
        this.kalturaClient.queueServiceCall("permissionitem", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPermissionItem)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaPermissionItemListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaPermissionItemListResponse list(KalturaPermissionItemFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaPermissionItemListResponse list(KalturaPermissionItemFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("permissionitem", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPermissionItemListResponse)KalturaObjectFactory.create(resultXmlElement);
    }
}
