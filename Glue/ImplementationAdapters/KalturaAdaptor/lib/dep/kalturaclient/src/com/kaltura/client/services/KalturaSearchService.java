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

public class KalturaSearchService extends KalturaServiceBase {
    public KalturaSearchService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaSearchResultResponse search(KalturaSearch search) throws KalturaApiException {
        return this.search(search, null);
    }

    public KalturaSearchResultResponse search(KalturaSearch search, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (search != null) kparams.add("search", search.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("search", "search", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaSearchResultResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaSearchResult getMediaInfo(KalturaSearchResult searchResult) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (searchResult != null) kparams.add("searchResult", searchResult.toParams());
        this.kalturaClient.queueServiceCall("search", "getMediaInfo", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaSearchResult)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaSearchResult searchUrl(KalturaMediaType mediaType, String url) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("mediaType", mediaType.getHashCode());
        kparams.addStringIfNotNull("url", url);
        this.kalturaClient.queueServiceCall("search", "searchUrl", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaSearchResult)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaSearchAuthData externalLogin(KalturaSearchProviderType searchSource, String userName, String password) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("searchSource", searchSource.getHashCode());
        kparams.addStringIfNotNull("userName", userName);
        kparams.addStringIfNotNull("password", password);
        this.kalturaClient.queueServiceCall("search", "externalLogin", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaSearchAuthData)KalturaObjectFactory.create(resultXmlElement);
    }
}
