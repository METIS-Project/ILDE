package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaUploadErrorCode;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaUploadResponse extends KalturaObjectBase {
    public String uploadTokenId;
    public int fileSize = Integer.MIN_VALUE;
    public KalturaUploadErrorCode errorCode;
    public String errorDescription;

    public KalturaUploadResponse() {
    }

    public KalturaUploadResponse(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("uploadTokenId")) {
                this.uploadTokenId = txt;
                continue;
            } else if (nodeName.equals("fileSize")) {
                try {
                    if (!txt.equals("")) this.fileSize = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("errorCode")) {
                try {
                    if (!txt.equals("")) this.errorCode = KalturaUploadErrorCode.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("errorDescription")) {
                this.errorDescription = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaUploadResponse");
        kparams.addStringIfNotNull("uploadTokenId", this.uploadTokenId);
        kparams.addIntIfNotNull("fileSize", this.fileSize);
        if (errorCode != null) kparams.addIntIfNotNull("errorCode", this.errorCode.getHashCode());
        kparams.addStringIfNotNull("errorDescription", this.errorDescription);
        return kparams;
    }
}

