package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaFileSyncObjectType;
import com.kaltura.client.enums.KalturaFileSyncStatus;
import com.kaltura.client.enums.KalturaFileSyncType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaFileSyncBaseFilter extends KalturaFilter {
    public int partnerIdEqual = Integer.MIN_VALUE;
    public KalturaFileSyncObjectType fileObjectTypeEqual;
    public String fileObjectTypeIn;
    public String objectIdEqual;
    public String objectIdIn;
    public String versionEqual;
    public String versionIn;
    public int objectSubTypeEqual = Integer.MIN_VALUE;
    public String objectSubTypeIn;
    public String dcEqual;
    public String dcIn;
    public int originalEqual = Integer.MIN_VALUE;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;
    public int readyAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int readyAtLessThanOrEqual = Integer.MIN_VALUE;
    public int syncTimeGreaterThanOrEqual = Integer.MIN_VALUE;
    public int syncTimeLessThanOrEqual = Integer.MIN_VALUE;
    public KalturaFileSyncStatus statusEqual;
    public String statusIn;
    public KalturaFileSyncType fileTypeEqual;
    public String fileTypeIn;
    public int linkedIdEqual = Integer.MIN_VALUE;
    public int linkCountGreaterThanOrEqual = Integer.MIN_VALUE;
    public int linkCountLessThanOrEqual = Integer.MIN_VALUE;
    public int fileSizeGreaterThanOrEqual = Integer.MIN_VALUE;
    public int fileSizeLessThanOrEqual = Integer.MIN_VALUE;

    public KalturaFileSyncBaseFilter() {
    }

    public KalturaFileSyncBaseFilter(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("fileObjectTypeEqual")) {
                try {
                    if (!txt.equals("")) this.fileObjectTypeEqual = KalturaFileSyncObjectType.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("fileObjectTypeIn")) {
                this.fileObjectTypeIn = txt;
                continue;
            } else if (nodeName.equals("objectIdEqual")) {
                this.objectIdEqual = txt;
                continue;
            } else if (nodeName.equals("objectIdIn")) {
                this.objectIdIn = txt;
                continue;
            } else if (nodeName.equals("versionEqual")) {
                this.versionEqual = txt;
                continue;
            } else if (nodeName.equals("versionIn")) {
                this.versionIn = txt;
                continue;
            } else if (nodeName.equals("objectSubTypeEqual")) {
                try {
                    if (!txt.equals("")) this.objectSubTypeEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("objectSubTypeIn")) {
                this.objectSubTypeIn = txt;
                continue;
            } else if (nodeName.equals("dcEqual")) {
                this.dcEqual = txt;
                continue;
            } else if (nodeName.equals("dcIn")) {
                this.dcIn = txt;
                continue;
            } else if (nodeName.equals("originalEqual")) {
                try {
                    if (!txt.equals("")) this.originalEqual = Integer.parseInt(txt);
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
            } else if (nodeName.equals("readyAtGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.readyAtGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("readyAtLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.readyAtLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("syncTimeGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.syncTimeGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("syncTimeLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.syncTimeLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("statusEqual")) {
                try {
                    if (!txt.equals("")) this.statusEqual = KalturaFileSyncStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = txt;
                continue;
            } else if (nodeName.equals("fileTypeEqual")) {
                try {
                    if (!txt.equals("")) this.fileTypeEqual = KalturaFileSyncType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("fileTypeIn")) {
                this.fileTypeIn = txt;
                continue;
            } else if (nodeName.equals("linkedIdEqual")) {
                try {
                    if (!txt.equals("")) this.linkedIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("linkCountGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.linkCountGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("linkCountLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.linkCountLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("fileSizeGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.fileSizeGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("fileSizeLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.fileSizeLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaFileSyncBaseFilter");
        kparams.addIntIfNotNull("partnerIdEqual", this.partnerIdEqual);
        if (fileObjectTypeEqual != null) kparams.addStringIfNotNull("fileObjectTypeEqual", this.fileObjectTypeEqual.getHashCode());
        kparams.addStringIfNotNull("fileObjectTypeIn", this.fileObjectTypeIn);
        kparams.addStringIfNotNull("objectIdEqual", this.objectIdEqual);
        kparams.addStringIfNotNull("objectIdIn", this.objectIdIn);
        kparams.addStringIfNotNull("versionEqual", this.versionEqual);
        kparams.addStringIfNotNull("versionIn", this.versionIn);
        kparams.addIntIfNotNull("objectSubTypeEqual", this.objectSubTypeEqual);
        kparams.addStringIfNotNull("objectSubTypeIn", this.objectSubTypeIn);
        kparams.addStringIfNotNull("dcEqual", this.dcEqual);
        kparams.addStringIfNotNull("dcIn", this.dcIn);
        kparams.addIntIfNotNull("originalEqual", this.originalEqual);
        kparams.addIntIfNotNull("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.addIntIfNotNull("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        kparams.addIntIfNotNull("readyAtGreaterThanOrEqual", this.readyAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("readyAtLessThanOrEqual", this.readyAtLessThanOrEqual);
        kparams.addIntIfNotNull("syncTimeGreaterThanOrEqual", this.syncTimeGreaterThanOrEqual);
        kparams.addIntIfNotNull("syncTimeLessThanOrEqual", this.syncTimeLessThanOrEqual);
        if (statusEqual != null) kparams.addIntIfNotNull("statusEqual", this.statusEqual.getHashCode());
        kparams.addStringIfNotNull("statusIn", this.statusIn);
        if (fileTypeEqual != null) kparams.addIntIfNotNull("fileTypeEqual", this.fileTypeEqual.getHashCode());
        kparams.addStringIfNotNull("fileTypeIn", this.fileTypeIn);
        kparams.addIntIfNotNull("linkedIdEqual", this.linkedIdEqual);
        kparams.addIntIfNotNull("linkCountGreaterThanOrEqual", this.linkCountGreaterThanOrEqual);
        kparams.addIntIfNotNull("linkCountLessThanOrEqual", this.linkCountLessThanOrEqual);
        kparams.addIntIfNotNull("fileSizeGreaterThanOrEqual", this.fileSizeGreaterThanOrEqual);
        kparams.addIntIfNotNull("fileSizeLessThanOrEqual", this.fileSizeLessThanOrEqual);
        return kparams;
    }
}

