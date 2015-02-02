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

public class KalturaBulkUploadService extends KalturaServiceBase {
    public KalturaBulkUploadService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaBulkUpload add(int conversionProfileId, File csvFileData) throws KalturaApiException {
        return this.add(conversionProfileId, csvFileData, null);
    }

    public KalturaBulkUpload add(int conversionProfileId, File csvFileData, String bulkUploadType) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("conversionProfileId", conversionProfileId);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.put("csvFileData", csvFileData);
        kparams.addStringIfNotNull("bulkUploadType", bulkUploadType);
        this.kalturaClient.queueServiceCall("bulkupload", "add", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBulkUpload)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaBulkUpload get(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("bulkupload", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBulkUpload)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaBulkUploadListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaBulkUploadListResponse list(KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("bulkupload", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaBulkUploadListResponse)KalturaObjectFactory.create(resultXmlElement);
    }
}
