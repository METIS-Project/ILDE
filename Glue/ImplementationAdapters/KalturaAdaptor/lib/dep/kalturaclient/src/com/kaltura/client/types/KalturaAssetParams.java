package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaNullableBoolean;
import com.kaltura.client.enums.KalturaContainerFormat;
import java.util.ArrayList;
import com.kaltura.client.KalturaObjectFactory;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaAssetParams extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public String name;
    public String systemName;
    public String description;
    public int createdAt = Integer.MIN_VALUE;
    public KalturaNullableBoolean isSystemDefault;
    public String tags;
    public KalturaContainerFormat format;
    public ArrayList<KalturaString> requiredPermissions;

    public KalturaAssetParams() {
    }

    public KalturaAssetParams(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("name")) {
                this.name = txt;
                continue;
            } else if (nodeName.equals("systemName")) {
                this.systemName = txt;
                continue;
            } else if (nodeName.equals("description")) {
                this.description = txt;
                continue;
            } else if (nodeName.equals("createdAt")) {
                try {
                    if (!txt.equals("")) this.createdAt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("isSystemDefault")) {
                try {
                    if (!txt.equals("")) this.isSystemDefault = KalturaNullableBoolean.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = txt;
                continue;
            } else if (nodeName.equals("format")) {
                try {
                    if (!txt.equals("")) this.format = KalturaContainerFormat.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("requiredPermissions")) {
                this.requiredPermissions = new ArrayList<KalturaString>();
                NodeList subNodeList = aNode.getChildNodes();
                for (int j = 0; j < subNodeList.getLength(); j++) {
                    Node arrayNode = subNodeList.item(j);
                    this.requiredPermissions.add((KalturaString)KalturaObjectFactory.create((Element)arrayNode));
                }
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaAssetParams");
        kparams.addStringIfNotNull("name", this.name);
        kparams.addStringIfNotNull("systemName", this.systemName);
        kparams.addStringIfNotNull("description", this.description);
        kparams.addStringIfNotNull("tags", this.tags);
        if (format != null) kparams.addStringIfNotNull("format", this.format.getHashCode());
        kparams.addObjectIfNotNull("requiredPermissions", this.requiredPermissions);
        return kparams;
    }
}

