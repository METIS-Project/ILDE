package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaModerationObjectType;
import com.kaltura.client.enums.KalturaModerationFlagStatus;
import com.kaltura.client.enums.KalturaModerationFlagType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaModerationFlag extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public String userId;
    public KalturaModerationObjectType moderationObjectType;
    public String flaggedEntryId;
    public String flaggedUserId;
    public KalturaModerationFlagStatus status;
    public String comments;
    public KalturaModerationFlagType flagType;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;

    public KalturaModerationFlag() {
    }

    public KalturaModerationFlag(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("userId")) {
                this.userId = txt;
                continue;
            } else if (nodeName.equals("moderationObjectType")) {
                try {
                    if (!txt.equals("")) this.moderationObjectType = KalturaModerationObjectType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("flaggedEntryId")) {
                this.flaggedEntryId = txt;
                continue;
            } else if (nodeName.equals("flaggedUserId")) {
                this.flaggedUserId = txt;
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaModerationFlagStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("comments")) {
                this.comments = txt;
                continue;
            } else if (nodeName.equals("flagType")) {
                try {
                    if (!txt.equals("")) this.flagType = KalturaModerationFlagType.get(Integer.parseInt(txt));
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
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaModerationFlag");
        kparams.addStringIfNotNull("flaggedEntryId", this.flaggedEntryId);
        kparams.addStringIfNotNull("flaggedUserId", this.flaggedUserId);
        kparams.addStringIfNotNull("comments", this.comments);
        if (flagType != null) kparams.addIntIfNotNull("flagType", this.flagType.getHashCode());
        return kparams;
    }
}

