package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaSearchAuthData extends KalturaObjectBase {
    public String authData;
    public String loginUrl;
    public String message;

    public KalturaSearchAuthData() {
    }

    public KalturaSearchAuthData(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("authData")) {
                this.authData = txt;
                continue;
            } else if (nodeName.equals("loginUrl")) {
                this.loginUrl = txt;
                continue;
            } else if (nodeName.equals("message")) {
                this.message = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaSearchAuthData");
        kparams.addStringIfNotNull("authData", this.authData);
        kparams.addStringIfNotNull("loginUrl", this.loginUrl);
        kparams.addStringIfNotNull("message", this.message);
        return kparams;
    }
}

