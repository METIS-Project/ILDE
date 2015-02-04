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

public class KalturaThumbAssetService extends KalturaServiceBase {
    public KalturaThumbAssetService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaThumbAsset add(String entryId, KalturaThumbAsset thumbAsset) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        if (thumbAsset != null) kparams.add("thumbAsset", thumbAsset.toParams());
        this.kalturaClient.queueServiceCall("thumbasset", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaThumbAsset)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaThumbAsset setContent(String id, KalturaContentResource contentResource) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        if (contentResource != null) kparams.add("contentResource", contentResource.toParams());
        this.kalturaClient.queueServiceCall("thumbasset", "setContent", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaThumbAsset)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaThumbAsset update(String id, KalturaThumbAsset thumbAsset) throws KalturaApiException {
        return this.update(id, thumbAsset, null);
    }

    public KalturaThumbAsset update(String id, KalturaThumbAsset thumbAsset, KalturaContentResource contentResource) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        if (thumbAsset != null) kparams.add("thumbAsset", thumbAsset.toParams());
        if (contentResource != null) kparams.add("contentResource", contentResource.toParams());
        this.kalturaClient.queueServiceCall("thumbasset", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaThumbAsset)KalturaObjectFactory.create(resultXmlElement);
    }

    public void setAsDefault(String thumbAssetId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("thumbAssetId", thumbAssetId);
        this.kalturaClient.queueServiceCall("thumbasset", "setAsDefault", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaThumbAsset generateByEntryId(String entryId, int destThumbParamsId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addIntIfNotNull("destThumbParamsId", destThumbParamsId);
        this.kalturaClient.queueServiceCall("thumbasset", "generateByEntryId", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaThumbAsset)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaThumbAsset generate(String entryId, KalturaThumbParams thumbParams) throws KalturaApiException {
        return this.generate(entryId, thumbParams, null);
    }

    public KalturaThumbAsset generate(String entryId, KalturaThumbParams thumbParams, String sourceAssetId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        if (thumbParams != null) kparams.add("thumbParams", thumbParams.toParams());
        kparams.addStringIfNotNull("sourceAssetId", sourceAssetId);
        this.kalturaClient.queueServiceCall("thumbasset", "generate", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaThumbAsset)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaThumbAsset regenerate(String thumbAssetId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("thumbAssetId", thumbAssetId);
        this.kalturaClient.queueServiceCall("thumbasset", "regenerate", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaThumbAsset)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaThumbAsset get(String thumbAssetId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("thumbAssetId", thumbAssetId);
        this.kalturaClient.queueServiceCall("thumbasset", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaThumbAsset)KalturaObjectFactory.create(resultXmlElement);
    }

    public List<KalturaThumbAsset> getByEntryId(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("thumbasset", "getByEntryId", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        List<KalturaThumbAsset> list = new ArrayList<KalturaThumbAsset>();
        for(int i = 0; i < resultXmlElement.getChildNodes().getLength(); i++) {
            Element node = (Element)resultXmlElement.getChildNodes().item(i);
            list.add((KalturaThumbAsset)KalturaObjectFactory.create(node));
        }
        return list;
    }

    public KalturaThumbAssetListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaThumbAssetListResponse list(KalturaAssetFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaThumbAssetListResponse list(KalturaAssetFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("thumbasset", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaThumbAssetListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaThumbAsset addFromUrl(String entryId, String url) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addStringIfNotNull("url", url);
        this.kalturaClient.queueServiceCall("thumbasset", "addFromUrl", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaThumbAsset)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaThumbAsset addFromImage(String entryId, File fileData) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.put("fileData", fileData);
        this.kalturaClient.queueServiceCall("thumbasset", "addFromImage", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaThumbAsset)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(String thumbAssetId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("thumbAssetId", thumbAssetId);
        this.kalturaClient.queueServiceCall("thumbasset", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }
}
