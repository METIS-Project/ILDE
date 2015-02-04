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

public class KalturaGenericDistributionProviderActionService extends KalturaServiceBase {
    public KalturaGenericDistributionProviderActionService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaGenericDistributionProviderAction add(KalturaGenericDistributionProviderAction genericDistributionProviderAction) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (genericDistributionProviderAction != null) kparams.add("genericDistributionProviderAction", genericDistributionProviderAction.toParams());
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovideraction", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaGenericDistributionProviderAction)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaGenericDistributionProviderAction addMrssTransform(int id, String xslData) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        kparams.addStringIfNotNull("xslData", xslData);
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovideraction", "addMrssTransform", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaGenericDistributionProviderAction)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaGenericDistributionProviderAction addMrssTransformFromFile(int id, File xslFile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.put("xslFile", xslFile);
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovideraction", "addMrssTransformFromFile", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaGenericDistributionProviderAction)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaGenericDistributionProviderAction addMrssValidate(int id, String xsdData) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        kparams.addStringIfNotNull("xsdData", xsdData);
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovideraction", "addMrssValidate", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaGenericDistributionProviderAction)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaGenericDistributionProviderAction addMrssValidateFromFile(int id, File xsdFile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.put("xsdFile", xsdFile);
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovideraction", "addMrssValidateFromFile", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaGenericDistributionProviderAction)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaGenericDistributionProviderAction addResultsTransform(int id, String transformData) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        kparams.addStringIfNotNull("transformData", transformData);
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovideraction", "addResultsTransform", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaGenericDistributionProviderAction)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaGenericDistributionProviderAction addResultsTransformFromFile(int id, File transformFile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.put("transformFile", transformFile);
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovideraction", "addResultsTransformFromFile", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaGenericDistributionProviderAction)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaGenericDistributionProviderAction get(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovideraction", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaGenericDistributionProviderAction)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaGenericDistributionProviderAction getByProviderId(int genericDistributionProviderId, KalturaDistributionAction actionType) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("genericDistributionProviderId", genericDistributionProviderId);
        kparams.addIntIfNotNull("actionType", actionType.getHashCode());
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovideraction", "getByProviderId", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaGenericDistributionProviderAction)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaGenericDistributionProviderAction updateByProviderId(int genericDistributionProviderId, KalturaDistributionAction actionType, KalturaGenericDistributionProviderAction genericDistributionProviderAction) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("genericDistributionProviderId", genericDistributionProviderId);
        kparams.addIntIfNotNull("actionType", actionType.getHashCode());
        if (genericDistributionProviderAction != null) kparams.add("genericDistributionProviderAction", genericDistributionProviderAction.toParams());
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovideraction", "updateByProviderId", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaGenericDistributionProviderAction)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaGenericDistributionProviderAction update(int id, KalturaGenericDistributionProviderAction genericDistributionProviderAction) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        if (genericDistributionProviderAction != null) kparams.add("genericDistributionProviderAction", genericDistributionProviderAction.toParams());
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovideraction", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaGenericDistributionProviderAction)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovideraction", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public void deleteByProviderId(int genericDistributionProviderId, KalturaDistributionAction actionType) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("genericDistributionProviderId", genericDistributionProviderId);
        kparams.addIntIfNotNull("actionType", actionType.getHashCode());
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovideraction", "deleteByProviderId", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaGenericDistributionProviderActionListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaGenericDistributionProviderActionListResponse list(KalturaGenericDistributionProviderActionFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaGenericDistributionProviderActionListResponse list(KalturaGenericDistributionProviderActionFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("contentdistribution_genericdistributionprovideraction", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaGenericDistributionProviderActionListResponse)KalturaObjectFactory.create(resultXmlElement);
    }
}
