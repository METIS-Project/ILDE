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
import com.kaltura.client.enums.KalturaDropFolderFileDeletePolicy;
import com.kaltura.client.enums.KalturaDropFolderFileHandlerType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaDropFolder extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public String name;
    public String description;
    public KalturaDropFolderType type;
    public KalturaDropFolderStatus status;
    public int conversionProfileId = Integer.MIN_VALUE;
    public int dc = Integer.MIN_VALUE;
    public String path;
    public int fileSizeCheckInterval = Integer.MIN_VALUE;
    public KalturaDropFolderFileDeletePolicy fileDeletePolicy;
    public int autoFileDeleteDays = Integer.MIN_VALUE;
    public KalturaDropFolderFileHandlerType fileHandlerType;
    public String fileNamePatterns;
    public KalturaDropFolderFileHandlerConfig fileHandlerConfig;
    public String tags;
    public String ignoreFileNamePatterns;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;

    public KalturaDropFolder() {
    }

    public KalturaDropFolder(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("description")) {
                this.description = txt;
                continue;
            } else if (nodeName.equals("type")) {
                try {
                    if (!txt.equals("")) this.type = KalturaDropFolderType.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaDropFolderStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("conversionProfileId")) {
                try {
                    if (!txt.equals("")) this.conversionProfileId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("dc")) {
                try {
                    if (!txt.equals("")) this.dc = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("path")) {
                this.path = txt;
                continue;
            } else if (nodeName.equals("fileSizeCheckInterval")) {
                try {
                    if (!txt.equals("")) this.fileSizeCheckInterval = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("fileDeletePolicy")) {
                try {
                    if (!txt.equals("")) this.fileDeletePolicy = KalturaDropFolderFileDeletePolicy.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("autoFileDeleteDays")) {
                try {
                    if (!txt.equals("")) this.autoFileDeleteDays = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("fileHandlerType")) {
                try {
                    if (!txt.equals("")) this.fileHandlerType = KalturaDropFolderFileHandlerType.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("fileNamePatterns")) {
                this.fileNamePatterns = txt;
                continue;
            } else if (nodeName.equals("fileHandlerConfig")) {
                this.fileHandlerConfig = (KalturaDropFolderFileHandlerConfig)KalturaObjectFactory.create((Element)aNode);
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = txt;
                continue;
            } else if (nodeName.equals("ignoreFileNamePatterns")) {
                this.ignoreFileNamePatterns = txt;
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
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaDropFolder");
        kparams.addIntIfNotNull("partnerId", this.partnerId);
        kparams.addStringIfNotNull("name", this.name);
        kparams.addStringIfNotNull("description", this.description);
        if (type != null) kparams.addStringIfNotNull("type", this.type.getHashCode());
        if (status != null) kparams.addIntIfNotNull("status", this.status.getHashCode());
        kparams.addIntIfNotNull("conversionProfileId", this.conversionProfileId);
        kparams.addIntIfNotNull("dc", this.dc);
        kparams.addStringIfNotNull("path", this.path);
        kparams.addIntIfNotNull("fileSizeCheckInterval", this.fileSizeCheckInterval);
        if (fileDeletePolicy != null) kparams.addIntIfNotNull("fileDeletePolicy", this.fileDeletePolicy.getHashCode());
        kparams.addIntIfNotNull("autoFileDeleteDays", this.autoFileDeleteDays);
        if (fileHandlerType != null) kparams.addStringIfNotNull("fileHandlerType", this.fileHandlerType.getHashCode());
        kparams.addStringIfNotNull("fileNamePatterns", this.fileNamePatterns);
        kparams.addObjectIfNotNull("fileHandlerConfig", this.fileHandlerConfig);
        kparams.addStringIfNotNull("tags", this.tags);
        kparams.addStringIfNotNull("ignoreFileNamePatterns", this.ignoreFileNamePatterns);
        return kparams;
    }
}

