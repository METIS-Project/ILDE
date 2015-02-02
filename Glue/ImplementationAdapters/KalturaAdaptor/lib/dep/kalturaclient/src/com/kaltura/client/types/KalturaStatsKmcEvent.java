package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaStatsKmcEventType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaStatsKmcEvent extends KalturaObjectBase {
    public String clientVer;
    public String kmcEventActionPath;
    public KalturaStatsKmcEventType kmcEventType;
    public float eventTimestamp = Float.MIN_VALUE;
    public String sessionId;
    public int partnerId = Integer.MIN_VALUE;
    public String entryId;
    public String widgetId;
    public int uiconfId = Integer.MIN_VALUE;
    public String userId;
    public String userIp;

    public KalturaStatsKmcEvent() {
    }

    public KalturaStatsKmcEvent(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("clientVer")) {
                this.clientVer = txt;
                continue;
            } else if (nodeName.equals("kmcEventActionPath")) {
                this.kmcEventActionPath = txt;
                continue;
            } else if (nodeName.equals("kmcEventType")) {
                try {
                    if (!txt.equals("")) this.kmcEventType = KalturaStatsKmcEventType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("eventTimestamp")) {
                try {
                    if (!txt.equals("")) this.eventTimestamp = Float.parseFloat(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("sessionId")) {
                this.sessionId = txt;
                continue;
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("entryId")) {
                this.entryId = txt;
                continue;
            } else if (nodeName.equals("widgetId")) {
                this.widgetId = txt;
                continue;
            } else if (nodeName.equals("uiconfId")) {
                try {
                    if (!txt.equals("")) this.uiconfId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("userId")) {
                this.userId = txt;
                continue;
            } else if (nodeName.equals("userIp")) {
                this.userIp = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaStatsKmcEvent");
        kparams.addStringIfNotNull("clientVer", this.clientVer);
        kparams.addStringIfNotNull("kmcEventActionPath", this.kmcEventActionPath);
        if (kmcEventType != null) kparams.addIntIfNotNull("kmcEventType", this.kmcEventType.getHashCode());
        kparams.addFloatIfNotNull("eventTimestamp", this.eventTimestamp);
        kparams.addStringIfNotNull("sessionId", this.sessionId);
        kparams.addIntIfNotNull("partnerId", this.partnerId);
        kparams.addStringIfNotNull("entryId", this.entryId);
        kparams.addStringIfNotNull("widgetId", this.widgetId);
        kparams.addIntIfNotNull("uiconfId", this.uiconfId);
        kparams.addStringIfNotNull("userId", this.userId);
        return kparams;
    }
}

