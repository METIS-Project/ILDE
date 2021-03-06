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

public class KalturaSharepointExtensionService extends KalturaServiceBase {
    public KalturaSharepointExtensionService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public boolean isVersionSupported(int serverMajor, int serverMinor, int serverBuild) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("serverMajor", serverMajor);
        kparams.addIntIfNotNull("serverMinor", serverMinor);
        kparams.addIntIfNotNull("serverBuild", serverBuild);
        this.kalturaClient.queueServiceCall("kalturasharepointextension_sharepointextension", "isVersionSupported", kparams);
        if (this.kalturaClient.isMultiRequest())
            return false;
        Element resultXmlElement = this.kalturaClient.doQueue();
        String resultText = XmlUtils.getTextValue(resultXmlElement, "result");
        return ((resultText.equals("0")) ? false : true);
    }

    public KalturaUiConfListResponse listUiconfs() throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        this.kalturaClient.queueServiceCall("kalturasharepointextension_sharepointextension", "listUiconfs", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaUiConfListResponse)KalturaObjectFactory.create(resultXmlElement);
    }
}
