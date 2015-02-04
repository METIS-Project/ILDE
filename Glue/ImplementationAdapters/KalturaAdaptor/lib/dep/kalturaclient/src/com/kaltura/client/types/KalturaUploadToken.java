package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaUploadTokenStatus;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaUploadToken extends KalturaObjectBase {
    public String id;
    public int partnerId = Integer.MIN_VALUE;
    public String userId;
    public KalturaUploadTokenStatus status;
    public String fileName;
    public float fileSize = Float.MIN_VALUE;
    public float uploadedFileSize = Float.MIN_VALUE;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;

    public KalturaUploadToken() {
    }

    public KalturaUploadToken(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("id")) {
                this.id = txt;
                continue;
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("userId")) {
                this.userId = txt;
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaUploadTokenStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("fileName")) {
                this.fileName = txt;
                continue;
            } else if (nodeName.equals("fileSize")) {
                try {
                    if (!txt.equals("")) this.fileSize = Float.parseFloat(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("uploadedFileSize")) {
                try {
                    if (!txt.equals("")) this.uploadedFileSize = Float.parseFloat(txt);
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
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaUploadToken");
        kparams.addStringIfNotNull("fileName", this.fileName);
        kparams.addFloatIfNotNull("fileSize", this.fileSize);
        return kparams;
    }
}

