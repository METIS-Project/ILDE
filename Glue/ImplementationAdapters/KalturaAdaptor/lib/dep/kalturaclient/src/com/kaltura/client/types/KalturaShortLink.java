package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaShortLinkStatus;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaShortLink extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
    public int expiresAt = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public String userId;
    public String name;
    public String systemName;
    public String fullUrl;
    public KalturaShortLinkStatus status;

    public KalturaShortLink() {
    }

    public KalturaShortLink(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("updatedAt")) {
                try {
                    if (!txt.equals("")) this.updatedAt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("expiresAt")) {
                try {
                    if (!txt.equals("")) this.expiresAt = Integer.parseInt(txt);
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
            } else if (nodeName.equals("name")) {
                this.name = txt;
                continue;
            } else if (nodeName.equals("systemName")) {
                this.systemName = txt;
                continue;
            } else if (nodeName.equals("fullUrl")) {
                this.fullUrl = txt;
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaShortLinkStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaShortLink");
        kparams.addIntIfNotNull("expiresAt", this.expiresAt);
        kparams.addStringIfNotNull("userId", this.userId);
        kparams.addStringIfNotNull("name", this.name);
        kparams.addStringIfNotNull("systemName", this.systemName);
        kparams.addStringIfNotNull("fullUrl", this.fullUrl);
        if (status != null) kparams.addIntIfNotNull("status", this.status.getHashCode());
        return kparams;
    }
}

