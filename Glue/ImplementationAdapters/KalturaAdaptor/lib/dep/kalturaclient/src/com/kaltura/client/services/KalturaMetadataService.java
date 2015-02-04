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

public class KalturaMetadataService extends KalturaServiceBase {
    public KalturaMetadataService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaMetadataListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaMetadataListResponse list(KalturaMetadataFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaMetadataListResponse list(KalturaMetadataFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("metadata_metadata", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMetadataListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMetadata add(int metadataProfileId, KalturaMetadataObjectType objectType, String objectId, String xmlData) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("metadataProfileId", metadataProfileId);
        kparams.addIntIfNotNull("objectType", objectType.getHashCode());
        kparams.addStringIfNotNull("objectId", objectId);
        kparams.addStringIfNotNull("xmlData", xmlData);
        this.kalturaClient.queueServiceCall("metadata_metadata", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMetadata)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMetadata addFromFile(int metadataProfileId, KalturaMetadataObjectType objectType, String objectId, File xmlFile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("metadataProfileId", metadataProfileId);
        kparams.addIntIfNotNull("objectType", objectType.getHashCode());
        kparams.addStringIfNotNull("objectId", objectId);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.put("xmlFile", xmlFile);
        this.kalturaClient.queueServiceCall("metadata_metadata", "addFromFile", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMetadata)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMetadata addFromUrl(int metadataProfileId, KalturaMetadataObjectType objectType, String objectId, String url) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("metadataProfileId", metadataProfileId);
        kparams.addIntIfNotNull("objectType", objectType.getHashCode());
        kparams.addStringIfNotNull("objectId", objectId);
        kparams.addStringIfNotNull("url", url);
        this.kalturaClient.queueServiceCall("metadata_metadata", "addFromUrl", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMetadata)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMetadata addFromBulk(int metadataProfileId, KalturaMetadataObjectType objectType, String objectId, String url) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("metadataProfileId", metadataProfileId);
        kparams.addIntIfNotNull("objectType", objectType.getHashCode());
        kparams.addStringIfNotNull("objectId", objectId);
        kparams.addStringIfNotNull("url", url);
        this.kalturaClient.queueServiceCall("metadata_metadata", "addFromBulk", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMetadata)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("metadata_metadata", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public void invalidate(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("metadata_metadata", "invalidate", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaMetadata get(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("metadata_metadata", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMetadata)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMetadata update(int id) throws KalturaApiException {
        return this.update(id, null);
    }

    public KalturaMetadata update(int id, String xmlData) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        kparams.addStringIfNotNull("xmlData", xmlData);
        this.kalturaClient.queueServiceCall("metadata_metadata", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMetadata)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMetadata updateFromFile(int id) throws KalturaApiException {
        return this.updateFromFile(id, null);
    }

    public KalturaMetadata updateFromFile(int id, File xmlFile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.put("xmlFile", xmlFile);
        this.kalturaClient.queueServiceCall("metadata_metadata", "updateFromFile", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMetadata)KalturaObjectFactory.create(resultXmlElement);
    }
}
