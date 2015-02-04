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

public class KalturaMetadataProfileService extends KalturaServiceBase {
    public KalturaMetadataProfileService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaMetadataProfileListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaMetadataProfileListResponse list(KalturaMetadataProfileFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaMetadataProfileListResponse list(KalturaMetadataProfileFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("metadata_metadataprofile", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMetadataProfileListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMetadataProfileFieldListResponse listFields(int metadataProfileId) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("metadataProfileId", metadataProfileId);
        this.kalturaClient.queueServiceCall("metadata_metadataprofile", "listFields", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMetadataProfileFieldListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMetadataProfile add(KalturaMetadataProfile metadataProfile, String xsdData) throws KalturaApiException {
        return this.add(metadataProfile, xsdData, null);
    }

    public KalturaMetadataProfile add(KalturaMetadataProfile metadataProfile, String xsdData, String viewsData) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (metadataProfile != null) kparams.add("metadataProfile", metadataProfile.toParams());
        kparams.addStringIfNotNull("xsdData", xsdData);
        kparams.addStringIfNotNull("viewsData", viewsData);
        this.kalturaClient.queueServiceCall("metadata_metadataprofile", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMetadataProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMetadataProfile addFromFile(KalturaMetadataProfile metadataProfile, File xsdFile) throws KalturaApiException {
        return this.addFromFile(metadataProfile, xsdFile, null);
    }

    public KalturaMetadataProfile addFromFile(KalturaMetadataProfile metadataProfile, File xsdFile, File viewsFile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (metadataProfile != null) kparams.add("metadataProfile", metadataProfile.toParams());
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.put("xsdFile", xsdFile);
        kfiles.put("viewsFile", viewsFile);
        this.kalturaClient.queueServiceCall("metadata_metadataprofile", "addFromFile", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMetadataProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public void delete(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("metadata_metadataprofile", "delete", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaMetadataProfile get(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("metadata_metadataprofile", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMetadataProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMetadataProfile update(int id, KalturaMetadataProfile metadataProfile) throws KalturaApiException {
        return this.update(id, metadataProfile, null);
    }

    public KalturaMetadataProfile update(int id, KalturaMetadataProfile metadataProfile, String xsdData) throws KalturaApiException {
        return this.update(id, metadataProfile, xsdData, null);
    }

    public KalturaMetadataProfile update(int id, KalturaMetadataProfile metadataProfile, String xsdData, String viewsData) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        if (metadataProfile != null) kparams.add("metadataProfile", metadataProfile.toParams());
        kparams.addStringIfNotNull("xsdData", xsdData);
        kparams.addStringIfNotNull("viewsData", viewsData);
        this.kalturaClient.queueServiceCall("metadata_metadataprofile", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMetadataProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMetadataProfile revert(int id, int toVersion) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        kparams.addIntIfNotNull("toVersion", toVersion);
        this.kalturaClient.queueServiceCall("metadata_metadataprofile", "revert", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMetadataProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMetadataProfile updateDefinitionFromFile(int id, File xsdFile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.put("xsdFile", xsdFile);
        this.kalturaClient.queueServiceCall("metadata_metadataprofile", "updateDefinitionFromFile", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMetadataProfile)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaMetadataProfile updateViewsFromFile(int id, File viewsFile) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        KalturaFiles kfiles = new KalturaFiles();
        kfiles.put("viewsFile", viewsFile);
        this.kalturaClient.queueServiceCall("metadata_metadataprofile", "updateViewsFromFile", kparams, kfiles);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaMetadataProfile)KalturaObjectFactory.create(resultXmlElement);
    }
}
