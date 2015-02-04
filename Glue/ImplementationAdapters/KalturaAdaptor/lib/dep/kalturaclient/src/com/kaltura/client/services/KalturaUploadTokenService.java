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

public class KalturaUploadTokenService extends KalturaServiceBase {
    public KalturaUploadTokenService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaUploadToken add() throws KalturaApiException {
        return this.add(null);
    }

    public KalturaUploadToken add(KalturaUploadToken uploadToken) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (uploadToken != null) kparams.add("uploadToken", uploadToken.toParams());
        this.kalturaClient.queueServiceCall("uploadtoken", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUploadToken)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaUploadToken get(String uploadTokenId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("uploadTokenId", uploadTokenId);
        this.kalturaClient.queueServiceCall("uploadtoken", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUploadToken)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaUploadToken upload(String uploadTokenId, File fileData) throws KalturaApiException {
        return this.upload(uploadTokenId, fileData, false);
    }

    public KalturaUploadToken upload(String uploadTokenId, File fileData, boolean resume) throws KalturaApiException {
        return this.upload(uploadTokenId, fileData, resume, true);
    }

    public KalturaUploadToken upload(String uploadTokenId, File fileData, boolean resume, boolean finalChunk) throws KalturaApiException {
        return this.upload(uploadTokenId, fileData, resume, finalChunk, -1);
    }

    public KalturaUploadToken upload(String uploadTokenId, File fileData, boolean resume, boolean finalChunk, int resumeAt) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("uploadTokenId", uploadTokenId);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.put("fileData", fileData);
        kparams.addBoolIfNotNull("resume", resume);
        kparams.addBoolIfNotNull("finalChunk", finalChunk);
        kparams.addIntIfNotNull("resumeAt", resumeAt);
        this.kalturaClient.queueServiceCall("uploadtoken", "upload", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUploadToken)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(String uploadTokenId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("uploadTokenId", uploadTokenId);
        this.kalturaClient.queueServiceCall("uploadtoken", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaUploadTokenListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaUploadTokenListResponse list(KalturaUploadTokenFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaUploadTokenListResponse list(KalturaUploadTokenFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("uploadtoken", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUploadTokenListResponse)KalturaObjectFactory.create(resultXmlElement);
    }
}
