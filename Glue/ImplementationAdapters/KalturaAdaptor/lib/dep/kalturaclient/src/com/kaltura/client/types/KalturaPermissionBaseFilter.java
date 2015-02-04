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

public abstract class KalturaPermissionBaseFilter extends KalturaFilter {
    public int idEqual = Integer.MIN_VALUE;
    public String idIn;
    public KalturaPermissionType typeEqual;
    public String typeIn;
    public String nameEqual;
    public String nameIn;
    public String friendlyNameLike;
    public String descriptionLike;
    public KalturaPermissionStatus statusEqual;
    public String statusIn;
    public int partnerIdEqual = Integer.MIN_VALUE;
    public String partnerIdIn;
    public String dependsOnPermissionNamesMultiLikeOr;
    public String dependsOnPermissionNamesMultiLikeAnd;
    public String tagsMultiLikeOr;
    public String tagsMultiLikeAnd;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;

    public KalturaPermissionBaseFilter() {
    }

    public KalturaPermissionBaseFilter(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("idIn")) {
                this.idIn = txt;
                continue;
            } else if (nodeName.equals("typeEqual")) {
                try {
                    if (!txt.equals("")) this.typeEqual = KalturaPermissionType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("typeIn")) {
                this.typeIn = txt;
                continue;
            } else if (nodeName.equals("nameEqual")) {
                this.nameEqual = txt;
                continue;
            } else if (nodeName.equals("nameIn")) {
                this.nameIn = txt;
                continue;
            } else if (nodeName.equals("friendlyNameLike")) {
                this.friendlyNameLike = txt;
                continue;
            } else if (nodeName.equals("descriptionLike")) {
                this.descriptionLike = txt;
                continue;
            } else if (nodeName.equals("statusEqual")) {
                try {
                    if (!txt.equals("")) this.statusEqual = KalturaPermissionStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = txt;
                continue;
            } else if (nodeName.equals("partnerIdEqual")) {
                try {
                    if (!txt.equals("")) this.partnerIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerIdIn")) {
                this.partnerIdIn = txt;
                continue;
            } else if (nodeName.equals("dependsOnPermissionNamesMultiLikeOr")) {
                this.dependsOnPermissionNamesMultiLikeOr = txt;
                continue;
            } else if (nodeName.equals("dependsOnPermissionNamesMultiLikeAnd")) {
                this.dependsOnPermissionNamesMultiLikeAnd = txt;
                continue;
            } else if (nodeName.equals("tagsMultiLikeOr")) {
                this.tagsMultiLikeOr = txt;
                continue;
            } else if (nodeName.equals("tagsMultiLikeAnd")) {
                this.tagsMultiLikeAnd = txt;
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
            } else if (nodeName.equals("updatedAtGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.updatedAtGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("updatedAtLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.updatedAtLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaPermissionBaseFilter");
        kparams.addIntIfNotNull("idEqual", this.idEqual);
        kparams.addStringIfNotNull("idIn", this.idIn);
        if (typeEqual != null) kparams.addIntIfNotNull("typeEqual", this.typeEqual.getHashCode());
        kparams.addStringIfNotNull("typeIn", this.typeIn);
        kparams.addStringIfNotNull("nameEqual", this.nameEqual);
        kparams.addStringIfNotNull("nameIn", this.nameIn);
        kparams.addStringIfNotNull("friendlyNameLike", this.friendlyNameLike);
        kparams.addStringIfNotNull("descriptionLike", this.descriptionLike);
        if (statusEqual != null) kparams.addIntIfNotNull("statusEqual", this.statusEqual.getHashCode());
        kparams.addStringIfNotNull("statusIn", this.statusIn);
        kparams.addIntIfNotNull("partnerIdEqual", this.partnerIdEqual);
        kparams.addStringIfNotNull("partnerIdIn", this.partnerIdIn);
        kparams.addStringIfNotNull("dependsOnPermissionNamesMultiLikeOr", this.dependsOnPermissionNamesMultiLikeOr);
        kparams.addStringIfNotNull("dependsOnPermissionNamesMultiLikeAnd", this.dependsOnPermissionNamesMultiLikeAnd);
        kparams.addStringIfNotNull("tagsMultiLikeOr", this.tagsMultiLikeOr);
        kparams.addStringIfNotNull("tagsMultiLikeAnd", this.tagsMultiLikeAnd);
        kparams.addIntIfNotNull("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.addIntIfNotNull("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        return kparams;
    }
}

