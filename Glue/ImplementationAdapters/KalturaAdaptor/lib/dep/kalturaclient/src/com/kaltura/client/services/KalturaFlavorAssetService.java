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

public class KalturaFlavorAssetService extends KalturaServiceBase {
    public KalturaFlavorAssetService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaFlavorAsset add(String entryId, KalturaFlavorAsset flavorAsset) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        if (flavorAsset != null) kparams.add("flavorAsset", flavorAsset.toParams());
        this.kalturaClient.queueServiceCall("flavorasset", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaFlavorAsset)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaFlavorAsset update(String id, KalturaFlavorAsset flavorAsset) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        if (flavorAsset != null) kparams.add("flavorAsset", flavorAsset.toParams());
        this.kalturaClient.queueServiceCall("flavorasset", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaFlavorAsset)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaFlavorAsset setContent(String id, KalturaContentResource contentResource) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        if (contentResource != null) kparams.add("contentResource", contentResource.toParams());
        this.kalturaClient.queueServiceCall("flavorasset", "setContent", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaFlavorAsset)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaFlavorAsset get(String id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("flavorasset", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaFlavorAsset)KalturaObjectFactory.create(resultXmlElement);
    }

    public List<KalturaFlavorAsset> getByEntryId(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("flavorasset", "getByEntryId", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        List<KalturaFlavorAsset> list = new ArrayList<KalturaFlavorAsset>();
        for(int i = 0; i < resultXmlElement.getChildNodes().getLength(); i++) {
            Element node = (Element)resultXmlElement.getChildNodes().item(i);
            list.add((KalturaFlavorAsset)KalturaObjectFactory.create(node));
        }
        return list;
    }

    public KalturaFlavorAssetListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaFlavorAssetListResponse list(KalturaAssetFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaFlavorAssetListResponse list(KalturaAssetFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("flavorasset", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaFlavorAssetListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public List<KalturaFlavorAsset> getWebPlayableByEntryId(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("flavorasset", "getWebPlayableByEntryId", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        List<KalturaFlavorAsset> list = new ArrayList<KalturaFlavorAsset>();
        for(int i = 0; i < resultXmlElement.getChildNodes().getLength(); i++) {
            Element node = (Element)resultXmlElement.getChildNodes().item(i);
            list.add((KalturaFlavorAsset)KalturaObjectFactory.create(node));
        }
        return list;
    }

    public void convert(String entryId, int flavorParamsId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addIntIfNotNull("flavorParamsId", flavorParamsId);
        this.kalturaClient.queueServiceCall("flavorasset", "convert", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public void reconvert(String id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("flavorasset", "reconvert", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public void delete(String id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("flavorasset", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public String getDownloadUrl(String id) throws KalturaApiException {
        return this.getDownloadUrl(id, false);
    }

    public String getDownloadUrl(String id, boolean useCdn) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("id", id);
        kparams.addBoolIfNotNull("useCdn", useCdn);
        this.kalturaClient.queueServiceCall("flavorasset", "getDownloadUrl", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return resultText;
    }

    public List<KalturaFlavorAssetWithParams> getFlavorAssetsWithParams(String entryId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        this.kalturaClient.queueServiceCall("flavorasset", "getFlavorAssetsWithParams", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        List<KalturaFlavorAssetWithParams> list = new ArrayList<KalturaFlavorAssetWithParams>();
        for(int i = 0; i < resultXmlElement.getChildNodes().getLength(); i++) {
            Element node = (Element)resultXmlElement.getChildNodes().item(i);
            list.add((KalturaFlavorAssetWithParams)KalturaObjectFactory.create(node));
        }
        return list;
    }
}
