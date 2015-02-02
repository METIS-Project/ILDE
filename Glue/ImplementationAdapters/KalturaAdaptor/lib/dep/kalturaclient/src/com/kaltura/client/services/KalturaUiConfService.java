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

public class KalturaUiConfService extends KalturaServiceBase {
    public KalturaUiConfService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaUiConf add(KalturaUiConf uiConf) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (uiConf != null) kparams.add("uiConf", uiConf.toParams());
        this.kalturaClient.queueServiceCall("uiconf", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUiConf)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaUiConf update(int id, KalturaUiConf uiConf) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        if (uiConf != null) kparams.add("uiConf", uiConf.toParams());
        this.kalturaClient.queueServiceCall("uiconf", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUiConf)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaUiConf get(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("uiconf", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUiConf)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("uiconf", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaUiConf clone(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("uiconf", "clone", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUiConf)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaUiConfListResponse listTemplates() throws KalturaApiException {
        return this.listTemplates(null);
    }

    public KalturaUiConfListResponse listTemplates(KalturaUiConfFilter filter) throws KalturaApiException {
        return this.listTemplates(filter, null);
    }

    public KalturaUiConfListResponse listTemplates(KalturaUiConfFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("uiconf", "listTemplates", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUiConfListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaUiConfListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaUiConfListResponse list(KalturaUiConfFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaUiConfListResponse list(KalturaUiConfFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("uiconf", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUiConfListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public List<KalturaUiConfTypeInfo> getAvailableTypes() throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        this.kalturaClient.queueServiceCall("uiconf", "getAvailableTypes", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        List<KalturaUiConfTypeInfo> list = new ArrayList<KalturaUiConfTypeInfo>();
        for(int i = 0; i < resultXmlElement.getChildNodes().getLength(); i++) {
            Element node = (Element)resultXmlElement.getChildNodes().item(i);
            list.add((KalturaUiConfTypeInfo)KalturaObjectFactory.create(node));
        }
        return list;
    }
}
