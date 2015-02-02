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

public class KalturaMediaService extends KalturaServiceBase {
    public KalturaMediaService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaMediaEntry add(KalturaMediaEntry entry) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (entry != null) kparams.add("entry", entry.toParams());
        this.kalturaClient.queueServiceCall("media", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMediaEntry addContent(String entryId) throws KalturaApiException {
        return this.addContent(entryId, null);
    }

    public KalturaMediaEntry addContent(String entryId, KalturaResource resource) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        if (resource != null) kparams.add("resource", resource.toParams());
        this.kalturaClient.queueServiceCall("media", "addContent", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMediaEntry addFromUrl(KalturaMediaEntry mediaEntry, String url) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (mediaEntry != null) kparams.add("mediaEntry", mediaEntry.toParams());
        kparams.addStringIfNotNull("url", url);
        this.kalturaClient.queueServiceCall("media", "addFromUrl", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMediaEntry addFromSearchResult() throws KalturaApiException {
        return this.addFromSearchResult(null);
    }

    public KalturaMediaEntry addFromSearchResult(KalturaMediaEntry mediaEntry) throws KalturaApiException {
        return this.addFromSearchResult(mediaEntry, null);
    }

    public KalturaMediaEntry addFromSearchResult(KalturaMediaEntry mediaEntry, KalturaSearchResult searchResult) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (mediaEntry != null) kparams.add("mediaEntry", mediaEntry.toParams());
        if (searchResult != null) kparams.add("searchResult", searchResult.toParams());
        this.kalturaClient.queueServiceCall("media", "addFromSearchResult", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMediaEntry addFromUploadedFile(KalturaMediaEntry mediaEntry, String uploadTokenId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (mediaEntry != null) kparams.add("mediaEntry", mediaEntry.toParams());
        kparams.addStringIfNotNull("uploadTokenId", uploadTokenId);
        this.kalturaClient.queueServiceCall("media", "addFromUploadedFile", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMediaEntry addFromRecordedWebcam(KalturaMediaEntry mediaEntry, String webcamTokenId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (mediaEntry != null) kparams.add("mediaEntry", mediaEntry.toParams());
        kparams.addStringIfNotNull("webcamTokenId", webcamTokenId);
        this.kalturaClient.queueServiceCall("media", "addFromRecordedWebcam", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMediaEntry addFromEntry(String sourceEntryId) throws KalturaApiException {
        return this.addFromEntry(sourceEntryId, null);
    }

    public KalturaMediaEntry addFromEntry(String sourceEntryId, KalturaMediaEntry mediaEntry) throws KalturaApiException {
        return this.addFromEntry(sourceEntryId, mediaEntry, Integer.MIN_VALUE);
    }

    public KalturaMediaEntry addFromEntry(String sourceEntryId, KalturaMediaEntry mediaEntry, int sourceFlavorParamsId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("sourceEntryId", sourceEntryId);
        if (mediaEntry != null) kparams.add("mediaEntry", mediaEntry.toParams());
        kparams.addIntIfNotNull("sourceFlavorParamsId", sourceFlavorParamsId);
        this.kalturaClient.queueServiceCall("media", "addFromEntry", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMediaEntry addFromFlavorAsset(String sourceFlavorAssetId) throws KalturaApiException {
        return this.addFromFlavorAsset(sourceFlavorAssetId, null);
    }

    public KalturaMediaEntry addFromFlavorAsset(String sourceFlavorAssetId, KalturaMediaEntry mediaEntry) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("sourceFlavorAssetId", sourceFlavorAssetId);
        if (mediaEntry != null) kparams.add("mediaEntry", mediaEntry.toParams());
        this.kalturaClient.queueServiceCall("media", "addFromFlavorAsset", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public int convert(String entryId) throws KalturaApiException {
        return this.convert(entryId, Integer.MIN_VALUE);
    }

    public int convert(String entryId, int conversionProfileId) throws KalturaApiException {
        return this.convert(entryId, conversionProfileId, null);
    }

    public int convert(String entryId, int conversionProfileId, ArrayList<KalturaConversionAttribute> dynamicConversionAttributes) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addIntIfNotNull("conversionProfileId", conversionProfileId);
        for(KalturaConversionAttribute obj : dynamicConversionAttributes) {
            kparams.add("dynamicConversionAttributes", obj.toParams());
        }
        this.kalturaClient.queueServiceCall("media", "convert", kparams);
        if (this.kalturaClient.isMultiRequest())
            return 0;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return Integer.parseInt(resultText);
    }

    public KalturaMediaEntry get(String entryId) throws KalturaApiException {
        return this.get(entryId, -1);
    }

    public KalturaMediaEntry get(String entryId, int version) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addIntIfNotNull("version", version);
        this.kalturaClient.queueServiceCall("media", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMediaEntry update(String entryId, KalturaMediaEntry mediaEntry) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        if (mediaEntry != null) kparams.add("mediaEntry", mediaEntry.toParams());
        this.kalturaClient.queueServiceCall("media", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMediaEntry updateContent(String entryId, KalturaResource resource) throws KalturaApiException {
        return this.updateContent(entryId, resource, Integer.MIN_VALUE);
    }

    public KalturaMediaEntry updateContent(String entryId, KalturaResource resource, int conversionProfileId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        if (resource != null) kparams.add("resource", resource.toParams());
        kparams.addIntIfNotNull("conversionProfileId", conversionProfileId);
        this.kalturaClient.queueServiceCall("media", "updateContent", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("media", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaMediaEntry approveReplace(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("media", "approveReplace", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMediaEntry cancelReplace(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("media", "cancelReplace", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMediaListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaMediaListResponse list(KalturaMediaEntryFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaMediaListResponse list(KalturaMediaEntryFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("media", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public int count() throws KalturaApiException {
        return this.count(null);
    }

    public int count(KalturaMediaEntryFilter filter) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        this.kalturaClient.queueServiceCall("media", "count", kparams);
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
        this.kalturaClient.queueServiceCall("media", "upload", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return resultText;
    }

    public KalturaMediaEntry updateThumbnail(String entryId, int timeOffset) throws KalturaApiException {
        return this.updateThumbnail(entryId, timeOffset, Integer.MIN_VALUE);
    }

    public KalturaMediaEntry updateThumbnail(String entryId, int timeOffset, int flavorParamsId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addIntIfNotNull("timeOffset", timeOffset);
        kparams.addIntIfNotNull("flavorParamsId", flavorParamsId);
        this.kalturaClient.queueServiceCall("media", "updateThumbnail", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMediaEntry updateThumbnailFromSourceEntry(String entryId, String sourceEntryId, int timeOffset) throws KalturaApiException {
        return this.updateThumbnailFromSourceEntry(entryId, sourceEntryId, timeOffset, Integer.MIN_VALUE);
    }

    public KalturaMediaEntry updateThumbnailFromSourceEntry(String entryId, String sourceEntryId, int timeOffset, int flavorParamsId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addStringIfNotNull("sourceEntryId", sourceEntryId);
        kparams.addIntIfNotNull("timeOffset", timeOffset);
        kparams.addIntIfNotNull("flavorParamsId", flavorParamsId);
        this.kalturaClient.queueServiceCall("media", "updateThumbnailFromSourceEntry", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMediaEntry updateThumbnailJpeg(String entryId, File fileData) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.put("fileData", fileData);
        this.kalturaClient.queueServiceCall("media", "updateThumbnailJpeg", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMediaEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaBaseEntry updateThumbnailFromUrl(String entryId, String url) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addStringIfNotNull("url", url);
        this.kalturaClient.queueServiceCall("media", "updateThumbnailFromUrl", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBaseEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public int requestConversion(String entryId, String fileFormat) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addStringIfNotNull("fileFormat", fileFormat);
        this.kalturaClient.queueServiceCall("media", "requestConversion", kparams);
        if (this.kalturaClient.isMultiRequest())
            return 0;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return Integer.parseInt(resultText);
    }

    public void flag(KalturaModerationFlag moderationFlag) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (moderationFlag != null) kparams.add("moderationFlag", moderationFlag.toParams());
        this.kalturaClient.queueServiceCall("media", "flag", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public void reject(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("media", "reject", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public void approve(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("media", "approve", kparams);
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
        this.kalturaClient.queueServiceCall("media", "listFlags", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaModerationFlagListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public void anonymousRank(String entryId, int rank) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addIntIfNotNull("rank", rank);
        this.kalturaClient.queueServiceCall("media", "anonymousRank", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }
}
