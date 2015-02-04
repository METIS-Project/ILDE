package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaWidgetSecurityType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaWidget extends KalturaObjectBase {
    public String id;
    public String sourceWidgetId;
    public String rootWidgetId;
    public int partnerId = Integer.MIN_VALUE;
    public String entryId;
    public int uiConfId = Integer.MIN_VALUE;
    public KalturaWidgetSecurityType securityType;
    public int securityPolicy = Integer.MIN_VALUE;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
    public String partnerData;
    public String widgetHTML;

    public KalturaWidget() {
    }

    public KalturaWidget(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("id")) {
                this.id = txt;
                continue;
            } else if (nodeName.equals("sourceWidgetId")) {
                this.sourceWidgetId = txt;
                continue;
            } else if (nodeName.equals("rootWidgetId")) {
                this.rootWidgetId = txt;
                continue;
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("entryId")) {
                this.entryId = txt;
                continue;
            } else if (nodeName.equals("uiConfId")) {
                try {
                    if (!txt.equals("")) this.uiConfId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("securityType")) {
                try {
                    if (!txt.equals("")) this.securityType = KalturaWidgetSecurityType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("securityPolicy")) {
                try {
                    if (!txt.equals("")) this.securityPolicy = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("createdAt")) {
                try {
                    if (!txt.equals("")) this.createdAt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("updatedAt")) {
                try {
                    if (!txt.equals("")) this.updatedAt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerData")) {
                this.partnerData = txt;
                continue;
            } else if (nodeName.equals("widgetHTML")) {
                this.widgetHTML = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaWidget");
        kparams.addStringIfNotNull("sourceWidgetId", this.sourceWidgetId);
        kparams.addStringIfNotNull("entryId", this.entryId);
        kparams.addIntIfNotNull("uiConfId", this.uiConfId);
        if (securityType != null) kparams.addIntIfNotNull("securityType", this.securityType.getHashCode());
        kparams.addIntIfNotNull("securityPolicy", this.securityPolicy);
        kparams.addStringIfNotNull("partnerData", this.partnerData);
        return kparams;
    }
}

