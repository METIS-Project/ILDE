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

public class KalturaDropFolderFileService extends KalturaServiceBase {
    public KalturaDropFolderFileService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaDropFolderFile add(KalturaDropFolderFile dropFolderFile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (dropFolderFile != null) kparams.add("dropFolderFile", dropFolderFile.toParams());
        this.kalturaClient.queueServiceCall("dropfolder_dropfolderfile", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDropFolderFile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDropFolderFile get(int dropFolderFileId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("dropFolderFileId", dropFolderFileId);
        this.kalturaClient.queueServiceCall("dropfolder_dropfolderfile", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDropFolderFile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDropFolderFile update(int dropFolderFileId, KalturaDropFolderFile dropFolderFile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("dropFolderFileId", dropFolderFileId);
        if (dropFolderFile != null) kparams.add("dropFolderFile", dropFolderFile.toParams());
        this.kalturaClient.queueServiceCall("dropfolder_dropfolderfile", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDropFolderFile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDropFolderFile delete(int dropFolderFileId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("dropFolderFileId", dropFolderFileId);
        this.kalturaClient.queueServiceCall("dropfolder_dropfolderfile", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDropFolderFile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDropFolderFileListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaDropFolderFileListResponse list(KalturaDropFolderFileFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaDropFolderFileListResponse list(KalturaDropFolderFileFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("dropfolder_dropfolderfile", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDropFolderFileListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDropFolderFile ignore(int dropFolderFileId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("dropFolderFileId", dropFolderFileId);
        this.kalturaClient.queueServiceCall("dropfolder_dropfolderfile", "ignore", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDropFolderFile)KalturaObjectFactory.create(resultXmlElement);
    }
}
