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

public class KalturaDropFolderFile extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public int dropFolderId = Integer.MIN_VALUE;
    public String fileName;
    public int fileSize = Integer.MIN_VALUE;
    public int fileSizeLastSetAt = Integer.MIN_VALUE;
    public KalturaDropFolderFileStatus status;
    public String parsedSlug;
    public String parsedFlavor;
    public KalturaDropFolderFileErrorCode errorCode;
    public String errorDescription;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;

    public KalturaDropFolderFile() {
    }

    public KalturaDropFolderFile(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("dropFolderId")) {
                try {
                    if (!txt.equals("")) this.dropFolderId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("fileName")) {
                this.fileName = txt;
                continue;
            } else if (nodeName.equals("fileSize")) {
                try {
                    if (!txt.equals("")) this.fileSize = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("fileSizeLastSetAt")) {
                try {
                    if (!txt.equals("")) this.fileSizeLastSetAt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaDropFolderFileStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("parsedSlug")) {
                this.parsedSlug = txt;
                continue;
            } else if (nodeName.equals("parsedFlavor")) {
                this.parsedFlavor = txt;
                continue;
            } else if (nodeName.equals("errorCode")) {
                try {
                    if (!txt.equals("")) this.errorCode = KalturaDropFolderFileErrorCode.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("errorDescription")) {
                this.errorDescription = txt;
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
        kparams.setString("objectType", "KalturaDropFolderFile");
        kparams.addIntIfNotNull("dropFolderId", this.dropFolderId);
        kparams.addStringIfNotNull("fileName", this.fileName);
        kparams.addIntIfNotNull("fileSize", this.fileSize);
        if (status != null) kparams.addIntIfNotNull("status", this.status.getHashCode());
        kparams.addStringIfNotNull("parsedSlug", this.parsedSlug);
        kparams.addStringIfNotNull("parsedFlavor", this.parsedFlavor);
        if (errorCode != null) kparams.addStringIfNotNull("errorCode", this.errorCode.getHashCode());
        kparams.addStringIfNotNull("errorDescription", this.errorDescription);
        return kparams;
    }
}

