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

public class KalturaBaseEntryService extends KalturaServiceBase {
    public KalturaBaseEntryService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaBaseEntry add(KalturaBaseEntry entry) throws KalturaApiException {
        return this.add(entry, null);
    }

    public KalturaBaseEntry add(KalturaBaseEntry entry, String type) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (entry != null) kparams.add("entry", entry.toParams());
        kparams.addStringIfNotNull("type", type);
        this.kalturaClient.queueServiceCall("baseentry", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBaseEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaBaseEntry addContent(String entryId, KalturaResource resource) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        if (resource != null) kparams.add("resource", resource.toParams());
        this.kalturaClient.queueServiceCall("baseentry", "addContent", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBaseEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaBaseEntry addFromUploadedFile(KalturaBaseEntry entry, String uploadTokenId) throws KalturaApiException {
        return this.addFromUploadedFile(entry, uploadTokenId, null);
    }

    public KalturaBaseEntry addFromUploadedFile(KalturaBaseEntry entry, String uploadTokenId, String type) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (entry != null) kparams.add("entry", entry.toParams());
        kparams.addStringIfNotNull("uploadTokenId", uploadTokenId);
        kparams.addStringIfNotNull("type", type);
        this.kalturaClient.queueServiceCall("baseentry", "addFromUploadedFile", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBaseEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaBaseEntry get(String entryId) throws KalturaApiException {
        return this.get(entryId, -1);
    }

    public KalturaBaseEntry get(String entryId, int version) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addIntIfNotNull("version", version);
        this.kalturaClient.queueServiceCall("baseentry", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBaseEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaBaseEntry update(String entryId, KalturaBaseEntry baseEntry) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        if (baseEntry != null) kparams.add("baseEntry", baseEntry.toParams());
        this.kalturaClient.queueServiceCall("baseentry", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBaseEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaBaseEntry updateContent(String entryId, KalturaResource resource) throws KalturaApiException {
        return this.updateContent(entryId, resource, Integer.MIN_VALUE);
    }

    public KalturaBaseEntry updateContent(String entryId, KalturaResource resource, int conversionProfileId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        if (resource != null) kparams.add("resource", resource.toParams());
        kparams.addIntIfNotNull("conversionProfileId", conversionProfileId);
        this.kalturaClient.queueServiceCall("baseentry", "updateContent", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBaseEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public List<KalturaBaseEntry> getByIds(String entryIds) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryIds", entryIds);
        this.kalturaClient.queueServiceCall("baseentry", "getByIds", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        List<KalturaBaseEntry> list = new ArrayList<KalturaBaseEntry>();
        for(int i = 0; i < resultXmlElement.getChildNodes().getLength(); i++) {
            Element node = (Element)resultXmlElement.getChildNodes().item(i);
            list.add((KalturaBaseEntry)KalturaObjectFactory.create(node));
        }
        return list;
    }

    public void delete(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("baseentry", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaBaseEntryListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaBaseEntryListResponse list(KalturaBaseEntryFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaBaseEntryListResponse list(KalturaBaseEntryFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("baseentry", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBaseEntryListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public int count() throws KalturaApiException {
        return this.count(null);
    }

    public int count(KalturaBaseEntryFilter filter) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        this.kalturaClient.queueServiceCall("baseentry", "count", kparams);
        if (this.kalturaClient.isMultiRequest())
            return 0;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return Integer.parseInt(resultText);
    }

    public String upload(File fileData) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.put("fileData", fileData);
        this.kalturaClient.queueServiceCall("baseentry", "upload", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return resultText;
    }

    public KalturaBaseEntry updateThumbnailJpeg(String entryId, File fileData) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.put("fileData", fileData);
        this.kalturaClient.queueServiceCall("baseentry", "updateThumbnailJpeg", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBaseEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaBaseEntry updateThumbnailFromUrl(String entryId, String url) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addStringIfNotNull("url", url);
        this.kalturaClient.queueServiceCall("baseentry", "updateThumbnailFromUrl", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBaseEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaBaseEntry updateThumbnailFromSourceEntry(String entryId, String sourceEntryId, int timeOffset) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addStringIfNotNull("sourceEntryId", sourceEntryId);
        kparams.addIntIfNotNull("timeOffset", timeOffset);
        this.kalturaClient.queueServiceCall("baseentry", "updateThumbnailFromSourceEntry", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBaseEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public void flag(KalturaModerationFlag moderationFlag) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (moderationFlag != null) kparams.add("moderationFlag", moderationFlag.toParams());
        this.kalturaClient.queueServiceCall("baseentry", "flag", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public void reject(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("baseentry", "reject", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public void approve(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("baseentry", "approve", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaModerationFlagListResponse listFlags(String entryId) throws KalturaApiException {
        return this.listFlags(entryId, null);
    }

    public KalturaModerationFlagListResponse listFlags(String entryId, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("baseentry", "listFlags", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaModerationFlagListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public void anonymousRank(String entryId, int rank) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addIntIfNotNull("rank", rank);
        this.kalturaClient.queueServiceCall("baseentry", "anonymousRank", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaEntryContextDataResult getContextData(String entryId, KalturaEntryContextDataParams contextDataParams) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        if (contextDataParams != null) kparams.add("contextDataParams", contextDataParams.toParams());
        this.kalturaClient.queueServiceCall("baseentry", "getContextData", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaEntryContextDataResult)KalturaObjectFactory.create(resultXmlElement);
    }
}
