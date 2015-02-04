package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaPermissionType;
import com.kaltura.client.enums.KalturaPermissionStatus;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaPermission extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public KalturaPermissionType type;
    public String name;
    public String friendlyName;
    public String description;
    public KalturaPermissionStatus status;
    public int partnerId = Integer.MIN_VALUE;
    public String dependsOnPermissionNames;
    public String tags;
    public String permissionItemsIds;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
    public String partnerGroup;

    public KalturaPermission() {
    }

    public KalturaPermission(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("type")) {
                try {
                    if (!txt.equals("")) this.type = KalturaPermissionType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("name")) {
                this.name = txt;
                continue;
            } else if (nodeName.equals("friendlyName")) {
                this.friendlyName = txt;
                continue;
            } else if (nodeName.equals("description")) {
                this.description = txt;
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaPermissionStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("dependsOnPermissionNames")) {
                this.dependsOnPermissionNames = txt;
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = txt;
                continue;
            } else if (nodeName.equals("permissionItemsIds")) {
                this.permissionItemsIds = txt;
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
            } else if (nodeName.equals("partnerGroup")) {
                this.partnerGroup = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaPermission");
        kparams.addStringIfNotNull("name", this.name);
        kparams.addStringIfNotNull("friendlyName", this.friendlyName);
        kparams.addStringIfNotNull("description", this.description);
        if (status != null) kparams.addIntIfNotNull("status", this.status.getHashCode());
        kparams.addStringIfNotNull("dependsOnPermissionNames", this.dependsOnPermissionNames);
        kparams.addStringIfNotNull("tags", this.tags);
        kparams.addStringIfNotNull("permissionItemsIds", this.permissionItemsIds);
        kparams.addStringIfNotNull("partnerGroup", this.partnerGroup);
        return kparams;
    }
}

