package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaMetadataObjectType;
import com.kaltura.client.enums.KalturaMetadataStatus;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaMetadata extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public int metadataProfileId = Integer.MIN_VALUE;
    public int metadataProfileVersion = Integer.MIN_VALUE;
    public KalturaMetadataObjectType metadataObjectType;
    public String objectId;
    public int version = Integer.MIN_VALUE;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
    public KalturaMetadataStatus status;
    public String xml;

    public KalturaMetadata() {
    }

    public KalturaMetadata(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("metadataProfileId")) {
                try {
                    if (!txt.equals("")) this.metadataProfileId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("metadataProfileVersion")) {
                try {
                    if (!txt.equals("")) this.metadataProfileVersion = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("metadataObjectType")) {
                try {
                    if (!txt.equals("")) this.metadataObjectType = KalturaMetadataObjectType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("objectId")) {
                this.objectId = txt;
                continue;
            } else if (nodeName.equals("version")) {
                try {
                    if (!txt.equals("")) this.version = Integer.parseInt(txt);
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
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaMetadataStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("xml")) {
                this.xml = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaMetadata");
        return kparams;
    }
}

