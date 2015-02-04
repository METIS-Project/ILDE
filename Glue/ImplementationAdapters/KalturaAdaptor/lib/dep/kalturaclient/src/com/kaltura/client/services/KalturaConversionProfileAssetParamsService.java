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

public class KalturaConversionProfileAssetParamsService extends KalturaServiceBase {
    public KalturaConversionProfileAssetParamsService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaConversionProfileAssetParamsListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaConversionProfileAssetParamsListResponse list(KalturaConversionProfileAssetParamsFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaConversionProfileAssetParamsListResponse list(KalturaConversionProfileAssetParamsFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("conversionprofileassetparams", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaConversionProfileAssetParamsListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaConversionProfileAssetParams update(int conversionProfileId, int assetParamsId, KalturaConversionProfileAssetParams conversionProfileAssetParams) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("conversionProfileId", conversionProfileId);
        kparams.addIntIfNotNull("assetParamsId", assetParamsId);
        if (conversionProfileAssetParams != null) kparams.add("conversionProfileAssetParams", conversionProfileAssetParams.toParams());
        this.kalturaClient.queueServiceCall("conversionprofileassetparams", "update", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaConversionProfileAssetParams)KalturaObjectFactory.create(resultXmlElement);
    }
}
