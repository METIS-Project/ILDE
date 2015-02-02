package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaDistributionProtocol;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaGenericDistributionProfileAction extends KalturaObjectBase {
    public KalturaDistributionProtocol protocol;
    public String serverUrl;
    public String serverPath;
    public String username;
    public String password;
    public boolean ftpPassiveMode;
    public String httpFieldName;
    public String httpFileName;

    public KalturaGenericDistributionProfileAction() {
    }

    public KalturaGenericDistributionProfileAction(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("protocol")) {
                try {
                    if (!txt.equals("")) this.protocol = KalturaDistributionProtocol.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("serverUrl")) {
                this.serverUrl = txt;
                continue;
            } else if (nodeName.equals("serverPath")) {
                this.serverPath = txt;
                continue;
            } else if (nodeName.equals("username")) {
                this.username = txt;
                continue;
            } else if (nodeName.equals("password")) {
                this.password = txt;
                continue;
            } else if (nodeName.equals("ftpPassiveMode")) {
                if (!txt.equals("")) this.ftpPassiveMode = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("httpFieldName")) {
                this.httpFieldName = txt;
                continue;
            } else if (nodeName.equals("httpFileName")) {
                this.httpFileName = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaGenericDistributionProfileAction");
        if (protocol != null) kparams.addIntIfNotNull("protocol", this.protocol.getHashCode());
        kparams.addStringIfNotNull("serverUrl", this.serverUrl);
        kparams.addStringIfNotNull("serverPath", this.serverPath);
        kparams.addStringIfNotNull("username", this.username);
        kparams.addStringIfNotNull("password", this.password);
        kparams.addBoolIfNotNull("ftpPassiveMode", this.ftpPassiveMode);
        kparams.addStringIfNotNull("httpFieldName", this.httpFieldName);
        kparams.addStringIfNotNull("httpFileName", this.httpFileName);
        return kparams;
    }
}

