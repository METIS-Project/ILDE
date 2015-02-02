package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaAuditTrailStatus;
import com.kaltura.client.enums.KalturaAuditTrailObjectType;
import com.kaltura.client.enums.KalturaAuditTrailObjectType;
import com.kaltura.client.enums.KalturaAuditTrailAction;
import com.kaltura.client.enums.KalturaAuditTrailContext;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaAuditTrail extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int createdAt = Integer.MIN_VALUE;
    public int parsedAt = Integer.MIN_VALUE;
    public KalturaAuditTrailStatus status;
    public KalturaAuditTrailObjectType auditObjectType;
    public String objectId;
    public String relatedObjectId;
    public KalturaAuditTrailObjectType relatedObjectType;
    public String entryId;
    public int masterPartnerId = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public String requestId;
    public String userId;
    public KalturaAuditTrailAction action;
    public KalturaAuditTrailInfo data;
    public String ks;
    public KalturaAuditTrailContext context;
    public String entryPoint;
    public String serverName;
    public String ipAddress;
    public String userAgent;
    public String clientTag;
    public String description;
    public String errorDescription;

    public KalturaAuditTrail() {
    }

    public KalturaAuditTrail(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("id")) {
                try {
                    if (!txt.equals("")) this.id = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("createdAt")) {
                try {
                    if (!txt.equals("")) this.createdAt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("parsedAt")) {
                try {
                    if (!txt.equals("")) this.parsedAt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaAuditTrailStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("auditObjectType")) {
                try {
                    if (!txt.equals("")) this.auditObjectType = KalturaAuditTrailObjectType.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("objectId")) {
                this.objectId = txt;
                continue;
            } else if (nodeName.equals("relatedObjectId")) {
                this.relatedObjectId = txt;
                continue;
            } else if (nodeName.equals("relatedObjectType")) {
                try {
                    if (!txt.equals("")) this.relatedObjectType = KalturaAuditTrailObjectType.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("entryId")) {
                this.entryId = txt;
                continue;
            } else if (nodeName.equals("masterPartnerId")) {
                try {
                    if (!txt.equals("")) this.masterPartnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("requestId")) {
                this.requestId = txt;
                continue;
            } else if (nodeName.equals("userId")) {
                this.userId = txt;
                continue;
            } else if (nodeName.equals("action")) {
                try {
                    if (!txt.equals("")) this.action = KalturaAuditTrailAction.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("data")) {
                this.data = (KalturaAuditTrailInfo)KalturaObjectFactory.create((Element)aNode);
                continue;
            } else if (nodeName.equals("ks")) {
                this.ks = txt;
                continue;
            } else if (nodeName.equals("context")) {
                try {
                    if (!txt.equals("")) this.context = KalturaAuditTrailContext.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("entryPoint")) {
                this.entryPoint = txt;
                continue;
            } else if (nodeName.equals("serverName")) {
                this.serverName = txt;
                continue;
            } else if (nodeName.equals("ipAddress")) {
                this.ipAddress = txt;
                continue;
            } else if (nodeName.equals("userAgent")) {
                this.userAgent = txt;
                continue;
            } else if (nodeName.equals("clientTag")) {
                this.clientTag = txt;
                continue;
            } else if (nodeName.equals("description")) {
                this.description = txt;
                continue;
            } else if (nodeName.equals("errorDescription")) {
                this.errorDescription = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaAuditTrail");
        if (auditObjectType != null) kparams.addStringIfNotNull("auditObjectType", this.auditObjectType.getHashCode());
        kparams.addStringIfNotNull("objectId", this.objectId);
        kparams.addStringIfNotNull("relatedObjectId", this.relatedObjectId);
        if (relatedObjectType != null) kparams.addStringIfNotNull("relatedObjectType", this.relatedObjectType.getHashCode());
        kparams.addStringIfNotNull("entryId", this.entryId);
        kparams.addStringIfNotNull("userId", this.userId);
        if (action != null) kparams.addStringIfNotNull("action", this.action.getHashCode());
        kparams.addObjectIfNotNull("data", this.data);
        kparams.addStringIfNotNull("clientTag", this.clientTag);
        kparams.addStringIfNotNull("description", this.description);
        return kparams;
    }
}

