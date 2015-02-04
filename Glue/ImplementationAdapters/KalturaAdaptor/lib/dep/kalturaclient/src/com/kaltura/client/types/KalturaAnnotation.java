package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaAnnotation extends KalturaObjectBase {
    public String id;
    public String entryId;
    public int partnerId = Integer.MIN_VALUE;
    public String parentId;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
    public String text;
    public String tags;
    public int startTime = Integer.MIN_VALUE;
    public int endTime = Integer.MIN_VALUE;
    public String userId;
    public String partnerData;

    public KalturaAnnotation() {
    }

    public KalturaAnnotation(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("entryId")) {
                this.entryId = txt;
                continue;
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("parentId")) {
                this.parentId = txt;
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
            } else if (nodeName.equals("text")) {
                this.text = txt;
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = txt;
                continue;
            } else if (nodeName.equals("startTime")) {
                try {
                    if (!txt.equals("")) this.startTime = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("endTime")) {
                try {
                    if (!txt.equals("")) this.endTime = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("userId")) {
                this.userId = txt;
                continue;
            } else if (nodeName.equals("partnerData")) {
                this.partnerData = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaAnnotation");
        kparams.addStringIfNotNull("entryId", this.entryId);
        kparams.addStringIfNotNull("parentId", this.parentId);
        kparams.addStringIfNotNull("text", this.text);
        kparams.addStringIfNotNull("tags", this.tags);
        kparams.addIntIfNotNull("startTime", this.startTime);
        kparams.addIntIfNotNull("endTime", this.endTime);
        kparams.addStringIfNotNull("partnerData", this.partnerData);
        return kparams;
    }
}

