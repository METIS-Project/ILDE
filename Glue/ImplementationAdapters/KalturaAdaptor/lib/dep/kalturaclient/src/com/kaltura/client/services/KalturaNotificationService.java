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

public class KalturaNotificationService extends KalturaServiceBase {
    public KalturaNotificationService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaClientNotification getClientNotification(String entryId, KalturaNotificationType type) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addStringIfNotNull("entryId", entryId);
        kparams.addIntIfNotNull("type", type.getHashCode());
        this.kalturaClient.queueServiceCall("notification", "getClientNotification", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaClientNotification)KalturaObjectFactory.create(resultXmlElement);
    }
}
