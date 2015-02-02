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

public class KalturaStatsService extends KalturaServiceBase {
    public KalturaStatsService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public void collect(KalturaStatsEvent event) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (event != null) kparams.add("event", event.toParams());
        this.kalturaClient.queueServiceCall("stats", "collect", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public void kmcCollect(KalturaStatsKmcEvent kmcEvent) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (kmcEvent != null) kparams.add("kmcEvent", kmcEvent.toParams());
        this.kalturaClient.queueServiceCall("stats", "kmcCollect", kparams);
        if (this.kalturaClient.isMultiRequest())
            return;
        Element resultXmlElement = this.kalturaClient.doQueue();
    }

    public KalturaCEError reportKceError(KalturaCEError kalturaCEError) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (kalturaCEError != null) kparams.add("kalturaCEError", kalturaCEError.toParams());
        this.kalturaClient.queueServiceCall("stats", "reportKceError", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaCEError)KalturaObjectFactory.create(resultXmlElement);
    }
}
