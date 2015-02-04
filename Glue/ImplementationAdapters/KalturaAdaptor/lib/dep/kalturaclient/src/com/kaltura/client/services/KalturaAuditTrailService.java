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

public class KalturaAuditTrailService extends KalturaServiceBase {
    public KalturaAuditTrailService(KalturaClient client) {
        this.kalturaClient = client;
    }

    public KalturaAuditTrailListResponse list() throws KalturaApiException {
        return this.list(null);
    }

    public KalturaAuditTrailListResponse list(KalturaAuditTrailFilter filter) throws KalturaApiException {
        return this.list(filter, null);
    }

    public KalturaAuditTrailListResponse list(KalturaAuditTrailFilter filter, KalturaFilterPager pager) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (filter != null) kparams.add("filter", filter.toParams());
        if (pager != null) kparams.add("pager", pager.toParams());
        this.kalturaClient.queueServiceCall("audit_audittrail", "list", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaAuditTrailListResponse)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaAuditTrail add(KalturaAuditTrail auditTrail) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        if (auditTrail != null) kparams.add("auditTrail", auditTrail.toParams());
        this.kalturaClient.queueServiceCall("audit_audittrail", "add", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaAuditTrail)KalturaObjectFactory.create(resultXmlElement);
    }

    public KalturaAuditTrail get(int id) throws KalturaApiException {
        KalturaParams kparams = new KalturaParams();
        kparams.addIntIfNotNull("id", id);
        this.kalturaClient.queueServiceCall("audit_audittrail", "get", kparams);
        if (this.kalturaClient.isMultiRequest())
            return null;
        Element resultXmlElement = this.kalturaClient.doQueue();
        return (KalturaAuditTrail)KalturaObjectFactory.create(resultXmlElement);
    }
}
