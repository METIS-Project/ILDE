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

public class KalturaLiveStreamService extends KalturaServiceBase {
    public KalturaLiveStreamService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaLiveStreamAdminEntry add(KalturaLiveStreamAdminEntry liveStreamEntry) throws KalturaApiException {
        return this.add(liveStreamEntry, KalturaSourceType.get(Integer.MIN_VALUE));
    }

    public KalturaLiveStreamAdminEntry add(KalturaLiveStreamAdminEntry liveStreamEntry, KalturaSourceType sourceType) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (liveStreamEntry != null) kparams.add("liveStreamEntry", liveStreamEntry.toParams());
        kparams.addIntIfNotNull("sourceType", sourceType.getHashCode());
        this.kalturaClient.queueServiceCall("livestream", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaLiveStreamAdminEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaLiveStreamEntry get(String entryId) throws KalturaApiException {
        return this.get(entryId, -1);
    }

    public KalturaLiveStreamEntry get(String entryId, int version) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addIntIfNotNull("version", version);
        this.kalturaClient.queueServiceCall("livestream", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaLiveStreamEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaLiveStreamAdminEntry update(String entryId, KalturaLiveStreamAdminEntry liveStreamEntry) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        if (liveStreamEntry != null) kparams.add("liveStreamEntry", liveStreamEntry.toParams());
        this.kalturaClient.queueServiceCall("livestream", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaLiveStreamAdminEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("livestream", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaLiveStreamListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaLiveStreamListResponse list(KalturaLiveStreamEntryFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaLiveStreamListResponse list(KalturaLiveStreamEntryFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("livestream", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaLiveStreamListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaLiveStreamEntry updateOfflineThumbnailJpeg(String entryId, File fileData) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.put("fileData", fileData);
        this.kalturaClient.queueServiceCall("livestream", "updateOfflineThumbnailJpeg", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaLiveStreamEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaLiveStreamEntry updateOfflineThumbnailFromUrl(String entryId, String url) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addStringIfNotNull("url", url);
        this.kalturaClient.queueServiceCall("livestream", "updateOfflineThumbnailFromUrl", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaLiveStreamEntry)KalturaObjectFactory.create(resultXmlElement);
    }
}
