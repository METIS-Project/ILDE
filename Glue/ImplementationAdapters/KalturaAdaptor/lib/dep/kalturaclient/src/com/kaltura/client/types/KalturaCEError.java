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

public class KalturaCEError extends KalturaObjectBase {
    public String id;
    public int partnerId = Integer.MIN_VALUE;
    public String browser;
    public String serverIp;
    public String serverOs;
    public String phpVersion;
    public String ceAdminEmail;
    public String type;
    public String description;
    public String data;

    public KalturaCEError() {
    }

    public KalturaCEError(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("browser")) {
                this.browser = txt;
                continue;
            } else if (nodeName.equals("serverIp")) {
                this.serverIp = txt;
                continue;
            } else if (nodeName.equals("serverOs")) {
                this.serverOs = txt;
                continue;
            } else if (nodeName.equals("phpVersion")) {
                this.phpVersion = txt;
                continue;
            } else if (nodeName.equals("ceAdminEmail")) {
                this.ceAdminEmail = txt;
                continue;
            } else if (nodeName.equals("type")) {
                this.type = txt;
                continue;
            } else if (nodeName.equals("description")) {
                this.description = txt;
                continue;
            } else if (nodeName.equals("data")) {
                this.data = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaCEError");
        kparams.addIntIfNotNull("partnerId", this.partnerId);
        kparams.addStringIfNotNull("browser", this.browser);
        kparams.addStringIfNotNull("serverIp", this.serverIp);
        kparams.addStringIfNotNull("serverOs", this.serverOs);
        kparams.addStringIfNotNull("phpVersion", this.phpVersion);
        kparams.addStringIfNotNull("ceAdminEmail", this.ceAdminEmail);
        kparams.addStringIfNotNull("type", this.type);
        kparams.addStringIfNotNull("description", this.description);
        kparams.addStringIfNotNull("data", this.data);
        return kparams;
    }
}

