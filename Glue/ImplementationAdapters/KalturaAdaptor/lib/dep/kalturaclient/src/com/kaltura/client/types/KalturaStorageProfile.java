package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaStorageProfileStatus;
import com.kaltura.client.enums.KalturaStorageProfileProtocol;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaStorageProfile extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public String name;
    public String systemName;
    public String desciption;
    public KalturaStorageProfileStatus status;
    public KalturaStorageProfileProtocol protocol;
    public String storageUrl;
    public String storageBaseDir;
    public String storageUsername;
    public String storagePassword;
    public boolean storageFtpPassiveMode;
    public String deliveryHttpBaseUrl;
    public String deliveryRmpBaseUrl;
    public String deliveryIisBaseUrl;
    public int minFileSize = Integer.MIN_VALUE;
    public int maxFileSize = Integer.MIN_VALUE;
    public String flavorParamsIds;
    public int maxConcurrentConnections = Integer.MIN_VALUE;
    public String pathManagerClass;
    public String urlManagerClass;
    public int trigger = Integer.MIN_VALUE;

    public KalturaStorageProfile() {
    }

    public KalturaStorageProfile(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("name")) {
                this.name = txt;
                continue;
            } else if (nodeName.equals("systemName")) {
                this.systemName = txt;
                continue;
            } else if (nodeName.equals("desciption")) {
                this.desciption = txt;
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaStorageProfileStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("protocol")) {
                try {
                    if (!txt.equals("")) this.protocol = KalturaStorageProfileProtocol.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("storageUrl")) {
                this.storageUrl = txt;
                continue;
            } else if (nodeName.equals("storageBaseDir")) {
                this.storageBaseDir = txt;
                continue;
            } else if (nodeName.equals("storageUsername")) {
                this.storageUsername = txt;
                continue;
            } else if (nodeName.equals("storagePassword")) {
                this.storagePassword = txt;
                continue;
            } else if (nodeName.equals("storageFtpPassiveMode")) {
                if (!txt.equals("")) this.storageFtpPassiveMode = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("deliveryHttpBaseUrl")) {
                this.deliveryHttpBaseUrl = txt;
                continue;
            } else if (nodeName.equals("deliveryRmpBaseUrl")) {
                this.deliveryRmpBaseUrl = txt;
                continue;
            } else if (nodeName.equals("deliveryIisBaseUrl")) {
                this.deliveryIisBaseUrl = txt;
                continue;
            } else if (nodeName.equals("minFileSize")) {
                try {
                    if (!txt.equals("")) this.minFileSize = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("maxFileSize")) {
                try {
                    if (!txt.equals("")) this.maxFileSize = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("flavorParamsIds")) {
                this.flavorParamsIds = txt;
                continue;
            } else if (nodeName.equals("maxConcurrentConnections")) {
                try {
                    if (!txt.equals("")) this.maxConcurrentConnections = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("pathManagerClass")) {
                this.pathManagerClass = txt;
                continue;
            } else if (nodeName.equals("urlManagerClass")) {
                this.urlManagerClass = txt;
                continue;
            } else if (nodeName.equals("trigger")) {
                try {
                    if (!txt.equals("")) this.trigger = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaStorageProfile");
        kparams.addStringIfNotNull("name", this.name);
        kparams.addStringIfNotNull("systemName", this.systemName);
        kparams.addStringIfNotNull("desciption", this.desciption);
        if (status != null) kparams.addIntIfNotNull("status", this.status.getHashCode());
        if (protocol != null) kparams.addIntIfNotNull("protocol", this.protocol.getHashCode());
        kparams.addStringIfNotNull("storageUrl", this.storageUrl);
        kparams.addStringIfNotNull("storageBaseDir", this.storageBaseDir);
        kparams.addStringIfNotNull("storageUsername", this.storageUsername);
        kparams.addStringIfNotNull("storagePassword", this.storagePassword);
        kparams.addBoolIfNotNull("storageFtpPassiveMode", this.storageFtpPassiveMode);
        kparams.addStringIfNotNull("deliveryHttpBaseUrl", this.deliveryHttpBaseUrl);
        kparams.addStringIfNotNull("deliveryRmpBaseUrl", this.deliveryRmpBaseUrl);
        kparams.addStringIfNotNull("deliveryIisBaseUrl", this.deliveryIisBaseUrl);
        kparams.addIntIfNotNull("minFileSize", this.minFileSize);
        kparams.addIntIfNotNull("maxFileSize", this.maxFileSize);
        kparams.addStringIfNotNull("flavorParamsIds", this.flavorParamsIds);
        kparams.addIntIfNotNull("maxConcurrentConnections", this.maxConcurrentConnections);
        kparams.addStringIfNotNull("pathManagerClass", this.pathManagerClass);
        kparams.addStringIfNotNull("urlManagerClass", this.urlManagerClass);
        kparams.addIntIfNotNull("trigger", this.trigger);
        return kparams;
    }
}

