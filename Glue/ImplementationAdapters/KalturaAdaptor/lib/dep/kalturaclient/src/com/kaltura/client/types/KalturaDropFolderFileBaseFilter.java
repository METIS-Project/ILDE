package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaDropFolderFileStatus;
import com.kaltura.client.enums.KalturaDropFolderFileErrorCode;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaDropFolderFileBaseFilter extends KalturaFilter {
    public int idEqual = Integer.MIN_VALUE;
    public String idIn;
    public int partnerIdEqual = Integer.MIN_VALUE;
    public String partnerIdIn;
    public int dropFolderIdEqual = Integer.MIN_VALUE;
    public String dropFolderIdIn;
    public String fileNameEqual;
    public String fileNameIn;
    public String fileNameLike;
    public KalturaDropFolderFileStatus statusEqual;
    public String statusIn;
    public String parsedSlugEqual;
    public String parsedSlugIn;
    public String parsedSlugLike;
    public String parsedFlavorEqual;
    public String parsedFlavorIn;
    public String parsedFlavorLike;
    public KalturaDropFolderFileErrorCode errorCodeEqual;
    public String errorCodeIn;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;

    public KalturaDropFolderFileBaseFilter() {
    }

    public KalturaDropFolderFileBaseFilter(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("partnerIdEqual")) {
                try {
                    if (!txt.equals("")) this.partnerIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerIdIn")) {
                this.partnerIdIn = txt;
                continue;
            } else if (nodeName.equals("dropFolderIdEqual")) {
                try {
                    if (!txt.equals("")) this.dropFolderIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("dropFolderIdIn")) {
                this.dropFolderIdIn = txt;
                continue;
            } else if (nodeName.equals("fileNameEqual")) {
                this.fileNameEqual = txt;
                continue;
            } else if (nodeName.equals("fileNameIn")) {
                this.fileNameIn = txt;
                continue;
            } else if (nodeName.equals("fileNameLike")) {
                this.fileNameLike = txt;
                continue;
            } else if (nodeName.equals("statusEqual")) {
                try {
                    if (!txt.equals("")) this.statusEqual = KalturaDropFolderFileStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = txt;
                continue;
            } else if (nodeName.equals("parsedSlugEqual")) {
                this.parsedSlugEqual = txt;
                continue;
            } else if (nodeName.equals("parsedSlugIn")) {
                this.parsedSlugIn = txt;
                continue;
            } else if (nodeName.equals("parsedSlugLike")) {
                this.parsedSlugLike = txt;
                continue;
            } else if (nodeName.equals("parsedFlavorEqual")) {
                this.parsedFlavorEqual = txt;
                continue;
            } else if (nodeName.equals("parsedFlavorIn")) {
                this.parsedFlavorIn = txt;
                continue;
            } else if (nodeName.equals("parsedFlavorLike")) {
                this.parsedFlavorLike = txt;
                continue;
            } else if (nodeName.equals("errorCodeEqual")) {
                try {
                    if (!txt.equals("")) this.errorCodeEqual = KalturaDropFolderFileErrorCode.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("errorCodeIn")) {
                this.errorCodeIn = txt;
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
        kparams.setString("objectType", "KalturaDropFolderFileBaseFilter");
        kparams.addIntIfNotNull("idEqual", this.idEqual);
        kparams.addStringIfNotNull("idIn", this.idIn);
        kparams.addIntIfNotNull("partnerIdEqual", this.partnerIdEqual);
        kparams.addStringIfNotNull("partnerIdIn", this.partnerIdIn);
        kparams.addIntIfNotNull("dropFolderIdEqual", this.dropFolderIdEqual);
        kparams.addStringIfNotNull("dropFolderIdIn", this.dropFolderIdIn);
        kparams.addStringIfNotNull("fileNameEqual", this.fileNameEqual);
        kparams.addStringIfNotNull("fileNameIn", this.fileNameIn);
        kparams.addStringIfNotNull("fileNameLike", this.fileNameLike);
        if (statusEqual != null) kparams.addIntIfNotNull("statusEqual", this.statusEqual.getHashCode());
        kparams.addStringIfNotNull("statusIn", this.statusIn);
        kparams.addStringIfNotNull("parsedSlugEqual", this.parsedSlugEqual);
        kparams.addStringIfNotNull("parsedSlugIn", this.parsedSlugIn);
        kparams.addStringIfNotNull("parsedSlugLike", this.parsedSlugLike);
        kparams.addStringIfNotNull("parsedFlavorEqual", this.parsedFlavorEqual);
        kparams.addStringIfNotNull("parsedFlavorIn", this.parsedFlavorIn);
        kparams.addStringIfNotNull("parsedFlavorLike", this.parsedFlavorLike);
        if (errorCodeEqual != null) kparams.addStringIfNotNull("errorCodeEqual", this.errorCodeEqual.getHashCode());
        kparams.addStringIfNotNull("errorCodeIn", this.errorCodeIn);
        kparams.addIntIfNotNull("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.addIntIfNotNull("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        return kparams;
    }
}

