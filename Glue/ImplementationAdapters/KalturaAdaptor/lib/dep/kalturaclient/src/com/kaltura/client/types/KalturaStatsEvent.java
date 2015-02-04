package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaStatsEventType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaStatsEvent extends KalturaObjectBase {
    public String clientVer;
    public KalturaStatsEventType eventType;
    public float eventTimestamp = Float.MIN_VALUE;
    public String sessionId;
    public int partnerId = Integer.MIN_VALUE;
    public String entryId;
    public String uniqueViewer;
    public String widgetId;
    public int uiconfId = Integer.MIN_VALUE;
    public String userId;
    public int currentPoint = Integer.MIN_VALUE;
    public int duration = Integer.MIN_VALUE;
    public String userIp;
    public int processDuration = Integer.MIN_VALUE;
    public String controlId;
    public boolean seek;
    public int newPoint = Integer.MIN_VALUE;
    public String referrer;
    public boolean isFirstInSession;

    public KalturaStatsEvent() {
    }

    public KalturaStatsEvent(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("eventType")) {
                try {
                    if (!txt.equals("")) this.eventType = KalturaStatsEventType.get(Integer.parseInt(txt));
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
            } else if (nodeName.equals("uniqueViewer")) {
                this.uniqueViewer = txt;
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
            } else if (nodeName.equals("currentPoint")) {
                try {
                    if (!txt.equals("")) this.currentPoint = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("duration")) {
                try {
                    if (!txt.equals("")) this.duration = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("userIp")) {
                this.userIp = txt;
                continue;
            } else if (nodeName.equals("processDuration")) {
                try {
                    if (!txt.equals("")) this.processDuration = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("controlId")) {
                this.controlId = txt;
                continue;
            } else if (nodeName.equals("seek")) {
                if (!txt.equals("")) this.seek = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("newPoint")) {
                try {
                    if (!txt.equals("")) this.newPoint = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("referrer")) {
                this.referrer = txt;
                continue;
            } else if (nodeName.equals("isFirstInSession")) {
                if (!txt.equals("")) this.isFirstInSession = ((txt.equals("0")) ? false : true);
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaStatsEvent");
        kparams.addStringIfNotNull("clientVer", this.clientVer);
        if (eventType != null) kparams.addIntIfNotNull("eventType", this.eventType.getHashCode());
        kparams.addFloatIfNotNull("eventTimestamp", this.eventTimestamp);
        kparams.addStringIfNotNull("sessionId", this.sessionId);
        kparams.addIntIfNotNull("partnerId", this.partnerId);
        kparams.addStringIfNotNull("entryId", this.entryId);
        kparams.addStringIfNotNull("uniqueViewer", this.uniqueViewer);
        kparams.addStringIfNotNull("widgetId", this.widgetId);
        kparams.addIntIfNotNull("uiconfId", this.uiconfId);
        kparams.addStringIfNotNull("userId", this.userId);
        kparams.addIntIfNotNull("currentPoint", this.currentPoint);
        kparams.addIntIfNotNull("duration", this.duration);
        kparams.addIntIfNotNull("processDuration", this.processDuration);
        kparams.addStringIfNotNull("controlId", this.controlId);
        kparams.addBoolIfNotNull("seek", this.seek);
        kparams.addIntIfNotNull("newPoint", this.newPoint);
        kparams.addStringIfNotNull("referrer", this.referrer);
        kparams.addBoolIfNotNull("isFirstInSession", this.isFirstInSession);
        return kparams;
    }
}

