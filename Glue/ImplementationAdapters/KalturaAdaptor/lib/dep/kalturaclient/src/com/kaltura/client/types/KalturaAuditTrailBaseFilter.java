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

public abstract class KalturaAuditTrailBaseFilter extends KalturaFilter {
    public int idEqual = Integer.MIN_VALUE;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int parsedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int parsedAtLessThanOrEqual = Integer.MIN_VALUE;
    public KalturaAuditTrailStatus statusEqual;
    public String statusIn;
    public KalturaAuditTrailObjectType auditObjectTypeEqual;
    public String auditObjectTypeIn;
    public String objectIdEqual;
    public String objectIdIn;
    public String relatedObjectIdEqual;
    public String relatedObjectIdIn;
    public KalturaAuditTrailObjectType relatedObjectTypeEqual;
    public String relatedObjectTypeIn;
    public String entryIdEqual;
    public String entryIdIn;
    public int masterPartnerIdEqual = Integer.MIN_VALUE;
    public String masterPartnerIdIn;
    public int partnerIdEqual = Integer.MIN_VALUE;
    public String partnerIdIn;
    public String requestIdEqual;
    public String requestIdIn;
    public String userIdEqual;
    public String userIdIn;
    public KalturaAuditTrailAction actionEqual;
    public String actionIn;
    public String ksEqual;
    public KalturaAuditTrailContext contextEqual;
    public String contextIn;
    public String entryPointEqual;
    public String entryPointIn;
    public String serverNameEqual;
    public String serverNameIn;
    public String ipAddressEqual;
    public String ipAddressIn;
    public String clientTagEqual;

    public KalturaAuditTrailBaseFilter() {
    }

    public KalturaAuditTrailBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("idEqual")) {
                try {
                    if (!txt.equals("")) this.idEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("createdAtGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.createdAtGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("createdAtLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.createdAtLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("parsedAtGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.parsedAtGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("parsedAtLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.parsedAtLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("statusEqual")) {
                try {
                    if (!txt.equals("")) this.statusEqual = KalturaAuditTrailStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = txt;
                continue;
            } else if (nodeName.equals("auditObjectTypeEqual")) {
                try {
                    if (!txt.equals("")) this.auditObjectTypeEqual = KalturaAuditTrailObjectType.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("auditObjectTypeIn")) {
                this.auditObjectTypeIn = txt;
                continue;
            } else if (nodeName.equals("objectIdEqual")) {
                this.objectIdEqual = txt;
                continue;
            } else if (nodeName.equals("objectIdIn")) {
                this.objectIdIn = txt;
                continue;
            } else if (nodeName.equals("relatedObjectIdEqual")) {
                this.relatedObjectIdEqual = txt;
                continue;
            } else if (nodeName.equals("relatedObjectIdIn")) {
                this.relatedObjectIdIn = txt;
                continue;
            } else if (nodeName.equals("relatedObjectTypeEqual")) {
                try {
                    if (!txt.equals("")) this.relatedObjectTypeEqual = KalturaAuditTrailObjectType.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("relatedObjectTypeIn")) {
                this.relatedObjectTypeIn = txt;
                continue;
            } else if (nodeName.equals("entryIdEqual")) {
                this.entryIdEqual = txt;
                continue;
            } else if (nodeName.equals("entryIdIn")) {
                this.entryIdIn = txt;
                continue;
            } else if (nodeName.equals("masterPartnerIdEqual")) {
                try {
                    if (!txt.equals("")) this.masterPartnerIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("masterPartnerIdIn")) {
                this.masterPartnerIdIn = txt;
                continue;
            } else if (nodeName.equals("partnerIdEqual")) {
                try {
                    if (!txt.equals("")) this.partnerIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerIdIn")) {
                this.partnerIdIn = txt;
                continue;
            } else if (nodeName.equals("requestIdEqual")) {
                this.requestIdEqual = txt;
                continue;
            } else if (nodeName.equals("requestIdIn")) {
                this.requestIdIn = txt;
                continue;
            } else if (nodeName.equals("userIdEqual")) {
                this.userIdEqual = txt;
                continue;
            } else if (nodeName.equals("userIdIn")) {
                this.userIdIn = txt;
                continue;
            } else if (nodeName.equals("actionEqual")) {
                try {
                    if (!txt.equals("")) this.actionEqual = KalturaAuditTrailAction.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("actionIn")) {
                this.actionIn = txt;
                continue;
            } else if (nodeName.equals("ksEqual")) {
                this.ksEqual = txt;
                continue;
            } else if (nodeName.equals("contextEqual")) {
                try {
                    if (!txt.equals("")) this.contextEqual = KalturaAuditTrailContext.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("contextIn")) {
                this.contextIn = txt;
                continue;
            } else if (nodeName.equals("entryPointEqual")) {
                this.entryPointEqual = txt;
                continue;
            } else if (nodeName.equals("entryPointIn")) {
                this.entryPointIn = txt;
                continue;
            } else if (nodeName.equals("serverNameEqual")) {
                this.serverNameEqual = txt;
                continue;
            } else if (nodeName.equals("serverNameIn")) {
                this.serverNameIn = txt;
                continue;
            } else if (nodeName.equals("ipAddressEqual")) {
                this.ipAddressEqual = txt;
                continue;
            } else if (nodeName.equals("ipAddressIn")) {
                this.ipAddressIn = txt;
                continue;
            } else if (nodeName.equals("clientTagEqual")) {
                this.clientTagEqual = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaAuditTrailBaseFilter");
        kparams.addIntIfNotNull("idEqual", this.idEqual);
        kparams.addIntIfNotNull("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.addIntIfNotNull("parsedAtGreaterThanOrEqual", this.parsedAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("parsedAtLessThanOrEqual", this.parsedAtLessThanOrEqual);
        if (statusEqual != null) kparams.addIntIfNotNull("statusEqual", this.statusEqual.getHashCode());
        kparams.addStringIfNotNull("statusIn", this.statusIn);
        if (auditObjectTypeEqual != null) kparams.addStringIfNotNull("auditObjectTypeEqual", this.auditObjectTypeEqual.getHashCode());
        kparams.addStringIfNotNull("auditObjectTypeIn", this.auditObjectTypeIn);
        kparams.addStringIfNotNull("objectIdEqual", this.objectIdEqual);
        kparams.addStringIfNotNull("objectIdIn", this.objectIdIn);
        kparams.addStringIfNotNull("relatedObjectIdEqual", this.relatedObjectIdEqual);
        kparams.addStringIfNotNull("relatedObjectIdIn", this.relatedObjectIdIn);
        if (relatedObjectTypeEqual != null) kparams.addStringIfNotNull("relatedObjectTypeEqual", this.relatedObjectTypeEqual.getHashCode());
        kparams.addStringIfNotNull("relatedObjectTypeIn", this.relatedObjectTypeIn);
        kparams.addStringIfNotNull("entryIdEqual", this.entryIdEqual);
        kparams.addStringIfNotNull("entryIdIn", this.entryIdIn);
        kparams.addIntIfNotNull("masterPartnerIdEqual", this.masterPartnerIdEqual);
        kparams.addStringIfNotNull("masterPartnerIdIn", this.masterPartnerIdIn);
        kparams.addIntIfNotNull("partnerIdEqual", this.partnerIdEqual);
        kparams.addStringIfNotNull("partnerIdIn", this.partnerIdIn);
        kparams.addStringIfNotNull("requestIdEqual", this.requestIdEqual);
        kparams.addStringIfNotNull("requestIdIn", this.requestIdIn);
        kparams.addStringIfNotNull("userIdEqual", this.userIdEqual);
        kparams.addStringIfNotNull("userIdIn", this.userIdIn);
        if (actionEqual != null) kparams.addStringIfNotNull("actionEqual", this.actionEqual.getHashCode());
        kparams.addStringIfNotNull("actionIn", this.actionIn);
        kparams.addStringIfNotNull("ksEqual", this.ksEqual);
        if (contextEqual != null) kparams.addIntIfNotNull("contextEqual", this.contextEqual.getHashCode());
        kparams.addStringIfNotNull("contextIn", this.contextIn);
        kparams.addStringIfNotNull("entryPointEqual", this.entryPointEqual);
        kparams.addStringIfNotNull("entryPointIn", this.entryPointIn);
        kparams.addStringIfNotNull("serverNameEqual", this.serverNameEqual);
        kparams.addStringIfNotNull("serverNameIn", this.serverNameIn);
        kparams.addStringIfNotNull("ipAddressEqual", this.ipAddressEqual);
        kparams.addStringIfNotNull("ipAddressIn", this.ipAddressIn);
        kparams.addStringIfNotNull("clientTagEqual", this.clientTagEqual);
        return kparams;
    }
}

