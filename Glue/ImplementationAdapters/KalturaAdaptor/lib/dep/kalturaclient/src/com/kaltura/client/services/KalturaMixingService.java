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

public class KalturaMixingService extends KalturaServiceBase {
    public KalturaMixingService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaMixEntry add(KalturaMixEntry mixEntry) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (mixEntry != null) kparams.add("mixEntry", mixEntry.toParams());
        this.kalturaClient.queueServiceCall("mixing", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMixEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMixEntry get(String entryId) throws KalturaApiException {
        return this.get(entryId, -1);
    }

    public KalturaMixEntry get(String entryId, int version) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addIntIfNotNull("version", version);
        this.kalturaClient.queueServiceCall("mixing", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMixEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMixEntry update(String entryId, KalturaMixEntry mixEntry) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        if (mixEntry != null) kparams.add("mixEntry", mixEntry.toParams());
        this.kalturaClient.queueServiceCall("mixing", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMixEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("mixing", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaMixListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaMixListResponse list(KalturaMixEntryFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaMixListResponse list(KalturaMixEntryFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("mixing", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMixListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public int count() throws KalturaApiException {
        return this.count(null);
    }

    public int count(KalturaMediaEntryFilter filter) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        this.kalturaClient.queueServiceCall("mixing", "count", kparams);
        if (this.kalturaClient.isMultiRequest())
            return 0;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return Integer.parseInt(resultText);
    }

    public KalturaMixEntry clone(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("mixing", "clone", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMixEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMixEntry appendMediaEntry(String mixEntryId, String mediaEntryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("mixEntryId", mixEntryId);
        kparams.addStringIfNotNull("mediaEntryId", mediaEntryId);
        this.kalturaClient.queueServiceCall("mixing", "appendMediaEntry", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMixEntry)KalturaObjectFactory.create(resultXmlElement);
    }

    public int requestFlattening(String entryId, String fileFormat) throws KalturaApiException {
        return this.requestFlattening(entryId, fileFormat, -1);
    }

    public int requestFlattening(String entryId, String fileFormat, int version) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addStringIfNotNull("fileFormat", fileFormat);
        kparams.addIntIfNotNull("version", version);
        this.kalturaClient.queueServiceCall("mixing", "requestFlattening", kparams);
        if (this.kalturaClient.isMultiRequest())
            return 0;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return Integer.parseInt(resultText);
    }

    public List<KalturaMixEntry> getMixesByMediaId(String mediaEntryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("mediaEntryId", mediaEntryId);
        this.kalturaClient.queueServiceCall("mixing", "getMixesByMediaId", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        List<KalturaMixEntry> list = new ArrayList<KalturaMixEntry>();
        for(int i = 0; i < resultXmlElement.getChildNodes().getLength(); i++) {
            Element node = (Element)resultXmlElement.getChildNodes().item(i);
            list.add((KalturaMixEntry)KalturaObjectFactory.create(node));
        }
        return list;
    }

    public List<KalturaMediaEntry> getReadyMediaEntries(String mixId) throws KalturaApiException {
        return this.getReadyMediaEntries(mixId, -1);
    }

    public List<KalturaMediaEntry> getReadyMediaEntries(String mixId, int version) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("mixId", mixId);
        kparams.addIntIfNotNull("version", version);
        this.kalturaClient.queueServiceCall("mixing", "getReadyMediaEntries", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        List<KalturaMediaEntry> list = new ArrayList<KalturaMediaEntry>();
        for(int i = 0; i < resultXmlElement.getChildNodes().getLength(); i++) {
            Element node = (Element)resultXmlElement.getChildNodes().item(i);
            list.add((KalturaMediaEntry)KalturaObjectFactory.create(node));
        }
        return list;
    }

    public void anonymousRank(String entryId, int rank) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addIntIfNotNull("rank", rank);
        this.kalturaClient.queueServiceCall("mixing", "anonymousRank", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }
}
