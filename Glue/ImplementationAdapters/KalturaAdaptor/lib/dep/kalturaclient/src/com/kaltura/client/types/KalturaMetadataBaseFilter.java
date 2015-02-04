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

public abstract class KalturaMetadataBaseFilter extends KalturaFilter {
    public int partnerIdEqual = Integer.MIN_VALUE;
    public int metadataProfileIdEqual = Integer.MIN_VALUE;
    public int metadataProfileVersionEqual = Integer.MIN_VALUE;
    public int metadataProfileVersionGreaterThanOrEqual = Integer.MIN_VALUE;
    public int metadataProfileVersionLessThanOrEqual = Integer.MIN_VALUE;
    public KalturaMetadataObjectType metadataObjectTypeEqual;
    public String objectIdEqual;
    public String objectIdIn;
    public int versionEqual = Integer.MIN_VALUE;
    public int versionGreaterThanOrEqual = Integer.MIN_VALUE;
    public int versionLessThanOrEqual = Integer.MIN_VALUE;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;
    public KalturaMetadataStatus statusEqual;
    public String statusIn;

    public KalturaMetadataBaseFilter() {
    }

    public KalturaMetadataBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("partnerIdEqual")) {
                try {
                    if (!txt.equals("")) this.partnerIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("metadataProfileIdEqual")) {
                try {
                    if (!txt.equals("")) this.metadataProfileIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("metadataProfileVersionEqual")) {
                try {
                    if (!txt.equals("")) this.metadataProfileVersionEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("metadataProfileVersionGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.metadataProfileVersionGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("metadataProfileVersionLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.metadataProfileVersionLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("metadataObjectTypeEqual")) {
                try {
                    if (!txt.equals("")) this.metadataObjectTypeEqual = KalturaMetadataObjectType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("objectIdEqual")) {
                this.objectIdEqual = txt;
                continue;
            } else if (nodeName.equals("objectIdIn")) {
                this.objectIdIn = txt;
                continue;
            } else if (nodeName.equals("versionEqual")) {
                try {
                    if (!txt.equals("")) this.versionEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("versionGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.versionGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("versionLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.versionLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
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
            } else if (nodeName.equals("statusEqual")) {
                try {
                    if (!txt.equals("")) this.statusEqual = KalturaMetadataStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaMetadataBaseFilter");
        kparams.addIntIfNotNull("partnerIdEqual", this.partnerIdEqual);
        kparams.addIntIfNotNull("metadataProfileIdEqual", this.metadataProfileIdEqual);
        kparams.addIntIfNotNull("metadataProfileVersionEqual", this.metadataProfileVersionEqual);
        kparams.addIntIfNotNull("metadataProfileVersionGreaterThanOrEqual", this.metadataProfileVersionGreaterThanOrEqual);
        kparams.addIntIfNotNull("metadataProfileVersionLessThanOrEqual", this.metadataProfileVersionLessThanOrEqual);
        if (metadataObjectTypeEqual != null) kparams.addIntIfNotNull("metadataObjectTypeEqual", this.metadataObjectTypeEqual.getHashCode());
        kparams.addStringIfNotNull("objectIdEqual", this.objectIdEqual);
        kparams.addStringIfNotNull("objectIdIn", this.objectIdIn);
        kparams.addIntIfNotNull("versionEqual", this.versionEqual);
        kparams.addIntIfNotNull("versionGreaterThanOrEqual", this.versionGreaterThanOrEqual);
        kparams.addIntIfNotNull("versionLessThanOrEqual", this.versionLessThanOrEqual);
        kparams.addIntIfNotNull("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.addIntIfNotNull("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        if (statusEqual != null) kparams.addIntIfNotNull("statusEqual", this.statusEqual.getHashCode());
        kparams.addStringIfNotNull("statusIn", this.statusIn);
        return kparams;
    }
}

