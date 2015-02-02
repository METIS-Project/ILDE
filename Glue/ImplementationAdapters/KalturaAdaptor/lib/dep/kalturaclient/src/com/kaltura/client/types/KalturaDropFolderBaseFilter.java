package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaDropFolderType;
import com.kaltura.client.enums.KalturaDropFolderStatus;
import com.kaltura.client.enums.KalturaDropFolderFileHandlerType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaDropFolderBaseFilter extends KalturaFilter {
    public int idEqual = Integer.MIN_VALUE;
    public String idIn;
    public int partnerIdEqual = Integer.MIN_VALUE;
    public String partnerIdIn;
    public String nameLike;
    public KalturaDropFolderType typeEqual;
    public String typeIn;
    public KalturaDropFolderStatus statusEqual;
    public String statusIn;
    public int conversionProfileIdEqual = Integer.MIN_VALUE;
    public String conversionProfileIdIn;
    public int dcEqual = Integer.MIN_VALUE;
    public String dcIn;
    public String pathLike;
    public KalturaDropFolderFileHandlerType fileHandlerTypeEqual;
    public String fileHandlerTypeIn;
    public String fileNamePatternsLike;
    public String fileNamePatternsMultiLikeOr;
    public String fileNamePatternsMultiLikeAnd;
    public String tagsLike;
    public String tagsMultiLikeOr;
    public String tagsMultiLikeAnd;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;

    public KalturaDropFolderBaseFilter() {
    }

    public KalturaDropFolderBaseFilter(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("nameLike")) {
                this.nameLike = txt;
                continue;
            } else if (nodeName.equals("typeEqual")) {
                try {
                    if (!txt.equals("")) this.typeEqual = KalturaDropFolderType.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("typeIn")) {
                this.typeIn = txt;
                continue;
            } else if (nodeName.equals("statusEqual")) {
                try {
                    if (!txt.equals("")) this.statusEqual = KalturaDropFolderStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = txt;
                continue;
            } else if (nodeName.equals("conversionProfileIdEqual")) {
                try {
                    if (!txt.equals("")) this.conversionProfileIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("conversionProfileIdIn")) {
                this.conversionProfileIdIn = txt;
                continue;
            } else if (nodeName.equals("dcEqual")) {
                try {
                    if (!txt.equals("")) this.dcEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("dcIn")) {
                this.dcIn = txt;
                continue;
            } else if (nodeName.equals("pathLike")) {
                this.pathLike = txt;
                continue;
            } else if (nodeName.equals("fileHandlerTypeEqual")) {
                try {
                    if (!txt.equals("")) this.fileHandlerTypeEqual = KalturaDropFolderFileHandlerType.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("fileHandlerTypeIn")) {
                this.fileHandlerTypeIn = txt;
                continue;
            } else if (nodeName.equals("fileNamePatternsLike")) {
                this.fileNamePatternsLike = txt;
                continue;
            } else if (nodeName.equals("fileNamePatternsMultiLikeOr")) {
                this.fileNamePatternsMultiLikeOr = txt;
                continue;
            } else if (nodeName.equals("fileNamePatternsMultiLikeAnd")) {
                this.fileNamePatternsMultiLikeAnd = txt;
                continue;
            } else if (nodeName.equals("tagsLike")) {
                this.tagsLike = txt;
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
        kparams.setString("objectType", "KalturaDropFolderBaseFilter");
        kparams.addIntIfNotNull("idEqual", this.idEqual);
        kparams.addStringIfNotNull("idIn", this.idIn);
        kparams.addIntIfNotNull("partnerIdEqual", this.partnerIdEqual);
        kparams.addStringIfNotNull("partnerIdIn", this.partnerIdIn);
        kparams.addStringIfNotNull("nameLike", this.nameLike);
        if (typeEqual != null) kparams.addStringIfNotNull("typeEqual", this.typeEqual.getHashCode());
        kparams.addStringIfNotNull("typeIn", this.typeIn);
        if (statusEqual != null) kparams.addIntIfNotNull("statusEqual", this.statusEqual.getHashCode());
        kparams.addStringIfNotNull("statusIn", this.statusIn);
        kparams.addIntIfNotNull("conversionProfileIdEqual", this.conversionProfileIdEqual);
        kparams.addStringIfNotNull("conversionProfileIdIn", this.conversionProfileIdIn);
        kparams.addIntIfNotNull("dcEqual", this.dcEqual);
        kparams.addStringIfNotNull("dcIn", this.dcIn);
        kparams.addStringIfNotNull("pathLike", this.pathLike);
        if (fileHandlerTypeEqual != null) kparams.addStringIfNotNull("fileHandlerTypeEqual", this.fileHandlerTypeEqual.getHashCode());
        kparams.addStringIfNotNull("fileHandlerTypeIn", this.fileHandlerTypeIn);
        kparams.addStringIfNotNull("fileNamePatternsLike", this.fileNamePatternsLike);
        kparams.addStringIfNotNull("fileNamePatternsMultiLikeOr", this.fileNamePatternsMultiLikeOr);
        kparams.addStringIfNotNull("fileNamePatternsMultiLikeAnd", this.fileNamePatternsMultiLikeAnd);
        kparams.addStringIfNotNull("tagsLike", this.tagsLike);
        kparams.addStringIfNotNull("tagsMultiLikeOr", this.tagsMultiLikeOr);
        kparams.addStringIfNotNull("tagsMultiLikeAnd", this.tagsMultiLikeAnd);
        kparams.addIntIfNotNull("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.addIntIfNotNull("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        return kparams;
    }
}

