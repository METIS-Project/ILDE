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

public class KalturaPlaylistService extends KalturaServiceBase {
    public KalturaPlaylistService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaPlaylist add(KalturaPlaylist playlist) throws KalturaApiException {
        return this.add(playlist, false);
    }

    public KalturaPlaylist add(KalturaPlaylist playlist, boolean updateStats) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (playlist != null) kparams.add("playlist", playlist.toParams());
        kparams.addBoolIfNotNull("updateStats", updateStats);
        this.kalturaClient.queueServiceCall("playlist", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPlaylist)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaPlaylist get(String id) throws KalturaApiException {
        return this.get(id, -1);
    }

    public KalturaPlaylist get(String id, int version) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        kparams.addIntIfNotNull("version", version);
        this.kalturaClient.queueServiceCall("playlist", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPlaylist)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaPlaylist update(String id, KalturaPlaylist playlist) throws KalturaApiException {
        return this.update(id, playlist, false);
    }

    public KalturaPlaylist update(String id, KalturaPlaylist playlist, boolean updateStats) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        if (playlist != null) kparams.add("playlist", playlist.toParams());
        kparams.addBoolIfNotNull("updateStats", updateStats);
        this.kalturaClient.queueServiceCall("playlist", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPlaylist)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(String id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("playlist", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaPlaylist clone(String id) throws KalturaApiException {
        return this.clone(id, null);
    }

    public KalturaPlaylist clone(String id, KalturaPlaylist newPlaylist) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        if (newPlaylist != null) kparams.add("newPlaylist", newPlaylist.toParams());
        this.kalturaClient.queueServiceCall("playlist", "clone", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPlaylist)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaPlaylistListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaPlaylistListResponse list(KalturaPlaylistFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaPlaylistListResponse list(KalturaPlaylistFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("playlist", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPlaylistListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public List<KalturaBaseEntry> execute(String id) throws KalturaApiException {
        return this.execute(id, "");
    }

    public List<KalturaBaseEntry> execute(String id, String detailed) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        kparams.addStringIfNotNull("detailed", detailed);
        this.kalturaClient.queueServiceCall("playlist", "execute", kparams);
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

    public List<KalturaBaseEntry> executeFromContent(KalturaPlaylistType playlistType, String playlistContent) throws KalturaApiException {
        return this.executeFromContent(playlistType, playlistContent, "");
    }

    public List<KalturaBaseEntry> executeFromContent(KalturaPlaylistType playlistType, String playlistContent, String detailed) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("playlistType", playlistType.getHashCode());
        kparams.addStringIfNotNull("playlistContent", playlistContent);
        kparams.addStringIfNotNull("detailed", detailed);
        this.kalturaClient.queueServiceCall("playlist", "executeFromContent", kparams);
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

    public List<KalturaBaseEntry> executeFromFilters(ArrayList<KalturaMediaEntryFilterForPlaylist> filters, int totalResults) throws KalturaApiException {
        return this.executeFromFilters(filters, totalResults, "");
    }

    public List<KalturaBaseEntry> executeFromFilters(ArrayList<KalturaMediaEntryFilterForPlaylist> filters, int totalResults, String detailed) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        for(KalturaMediaEntryFilterForPlaylist obj : filters) {
            kparams.add("filters", obj.toParams());
        }
        kparams.addIntIfNotNull("totalResults", totalResults);
        kparams.addStringIfNotNull("detailed", detailed);
        this.kalturaClient.queueServiceCall("playlist", "executeFromFilters", kparams);
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

    public KalturaPlaylist getStatsFromContent(KalturaPlaylistType playlistType, String playlistContent) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("playlistType", playlistType.getHashCode());
        kparams.addStringIfNotNull("playlistContent", playlistContent);
        this.kalturaClient.queueServiceCall("playlist", "getStatsFromContent", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaPlaylist)KalturaObjectFactory.create(resultXmlElement);
    }
}
