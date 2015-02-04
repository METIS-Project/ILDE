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

public class KalturaDropFolderService extends KalturaServiceBase {
    public KalturaDropFolderService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaDropFolder add(KalturaDropFolder dropFolder) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (dropFolder != null) kparams.add("dropFolder", dropFolder.toParams());
        this.kalturaClient.queueServiceCall("dropfolder_dropfolder", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDropFolder)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDropFolder get(int dropFolderId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("dropFolderId", dropFolderId);
        this.kalturaClient.queueServiceCall("dropfolder_dropfolder", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDropFolder)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDropFolder update(int dropFolderId, KalturaDropFolder dropFolder) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("dropFolderId", dropFolderId);
        if (dropFolder != null) kparams.add("dropFolder", dropFolder.toParams());
        this.kalturaClient.queueServiceCall("dropfolder_dropfolder", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDropFolder)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDropFolder delete(int dropFolderId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("dropFolderId", dropFolderId);
        this.kalturaClient.queueServiceCall("dropfolder_dropfolder", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDropFolder)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDropFolderListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaDropFolderListResponse list(KalturaDropFolderFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaDropFolderListResponse list(KalturaDropFolderFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("dropfolder_dropfolder", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDropFolderListResponse)KalturaObjectFactory.create(resultXmlElement);
    }
}
