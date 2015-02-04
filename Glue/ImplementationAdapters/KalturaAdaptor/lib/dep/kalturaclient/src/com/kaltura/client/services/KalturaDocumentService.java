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

public class KalturaDocumentService extends KalturaServiceBase {
    public KalturaDocumentService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaDocumentEntry addFromUploadedFile(KalturaDocumentEntry documentEntry, String uploadTokenId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (documentEntry != null) kparams.add("documentEntry", documentEntry.toParams());
        kparams.addStringIfNotNull("uploadTokenId", uploadTokenId);
        this.kalturaClient.queueServiceCall("document", "addFromUploadedFile", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDocumentEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDocumentEntry addFromEntry(String sourceEntryId) throws KalturaApiException {
        return this.addFromEntry(sourceEntryId, null);
    }

    public KalturaDocumentEntry addFromEntry(String sourceEntryId, KalturaDocumentEntry documentEntry) throws KalturaApiException {
        return this.addFromEntry(sourceEntryId, documentEntry, Integer.MIN_VALUE);
    }

    public KalturaDocumentEntry addFromEntry(String sourceEntryId, KalturaDocumentEntry documentEntry, int sourceFlavorParamsId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("sourceEntryId", sourceEntryId);
        if (documentEntry != null) kparams.add("documentEntry", documentEntry.toParams());
        kparams.addIntIfNotNull("sourceFlavorParamsId", sourceFlavorParamsId);
        this.kalturaClient.queueServiceCall("document", "addFromEntry", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDocumentEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDocumentEntry addFromFlavorAsset(String sourceFlavorAssetId) throws KalturaApiException {
        return this.addFromFlavorAsset(sourceFlavorAssetId, null);
    }

    public KalturaDocumentEntry addFromFlavorAsset(String sourceFlavorAssetId, KalturaDocumentEntry documentEntry) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("sourceFlavorAssetId", sourceFlavorAssetId);
        if (documentEntry != null) kparams.add("documentEntry", documentEntry.toParams());
        this.kalturaClient.queueServiceCall("document", "addFromFlavorAsset", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDocumentEntry)KalturaObjectFactory.create(resultXmlElement);
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
        this.kalturaClient.queueServiceCall("document", "convert", kparams);
        if (this.kalturaClient.isMultiRequest())
            return 0;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return Integer.parseInt(resultText);
    }

    public KalturaDocumentEntry get(String entryId) throws KalturaApiException {
        return this.get(entryId, -1);
    }

    public KalturaDocumentEntry get(String entryId, int version) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addIntIfNotNull("version", version);
        this.kalturaClient.queueServiceCall("document", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDocumentEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaDocumentEntry update(String entryId, KalturaDocumentEntry documentEntry) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        if (documentEntry != null) kparams.add("documentEntry", documentEntry.toParams());
        this.kalturaClient.queueServiceCall("document", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDocumentEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("document", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaDocumentListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaDocumentListResponse list(KalturaDocumentEntryFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaDocumentListResponse list(KalturaDocumentEntryFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("document", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaDocumentListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public String upload(File fileData) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.put("fileData", fileData);
        this.kalturaClient.queueServiceCall("document", "upload", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return resultText;
    }

    public String convertPptToSwf(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("document", "convertPptToSwf", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return resultText;
    }
}
