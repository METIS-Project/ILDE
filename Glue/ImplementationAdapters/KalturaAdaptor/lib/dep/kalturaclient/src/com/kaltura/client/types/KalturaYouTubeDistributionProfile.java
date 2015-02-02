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

public class KalturaYouTubeDistributionProfile extends KalturaConfigurableDistributionProfile {
    public String username;
    public String notificationEmail;
    public String sftpHost;
    public String sftpLogin;
    public String sftpPublicKey;
    public String sftpPrivateKey;
    public String sftpBaseDir;
    public String ownerName;
    public String defaultCategory;
    public String allowComments;
    public String allowEmbedding;
    public String allowRatings;
    public String allowResponses;
    public String commercialPolicy;
    public String ugcPolicy;
    public String target;

    public KalturaYouTubeDistributionProfile() {
    }

    public KalturaYouTubeDistributionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("username")) {
                this.username = txt;
                continue;
            } else if (nodeName.equals("notificationEmail")) {
                this.notificationEmail = txt;
                continue;
            } else if (nodeName.equals("sftpHost")) {
                this.sftpHost = txt;
                continue;
            } else if (nodeName.equals("sftpLogin")) {
                this.sftpLogin = txt;
                continue;
            } else if (nodeName.equals("sftpPublicKey")) {
                this.sftpPublicKey = txt;
                continue;
            } else if (nodeName.equals("sftpPrivateKey")) {
                this.sftpPrivateKey = txt;
                continue;
            } else if (nodeName.equals("sftpBaseDir")) {
                this.sftpBaseDir = txt;
                continue;
            } else if (nodeName.equals("ownerName")) {
                this.ownerName = txt;
                continue;
            } else if (nodeName.equals("defaultCategory")) {
                this.defaultCategory = txt;
                continue;
            } else if (nodeName.equals("allowComments")) {
                this.allowComments = txt;
                continue;
            } else if (nodeName.equals("allowEmbedding")) {
                this.allowEmbedding = txt;
                continue;
            } else if (nodeName.equals("allowRatings")) {
                this.allowRatings = txt;
                continue;
            } else if (nodeName.equals("allowResponses")) {
                this.allowResponses = txt;
                continue;
            } else if (nodeName.equals("commercialPolicy")) {
                this.commercialPolicy = txt;
                continue;
            } else if (nodeName.equals("ugcPolicy")) {
                this.ugcPolicy = txt;
                continue;
            } else if (nodeName.equals("target")) {
                this.target = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaYouTubeDistributionProfile");
        kparams.addStringIfNotNull("username", this.username);
        kparams.addStringIfNotNull("notificationEmail", this.notificationEmail);
        kparams.addStringIfNotNull("sftpHost", this.sftpHost);
        kparams.addStringIfNotNull("sftpLogin", this.sftpLogin);
        kparams.addStringIfNotNull("sftpPublicKey", this.sftpPublicKey);
        kparams.addStringIfNotNull("sftpPrivateKey", this.sftpPrivateKey);
        kparams.addStringIfNotNull("sftpBaseDir", this.sftpBaseDir);
        kparams.addStringIfNotNull("ownerName", this.ownerName);
        kparams.addStringIfNotNull("defaultCategory", this.defaultCategory);
        kparams.addStringIfNotNull("allowComments", this.allowComments);
        kparams.addStringIfNotNull("allowEmbedding", this.allowEmbedding);
        kparams.addStringIfNotNull("allowRatings", this.allowRatings);
        kparams.addStringIfNotNull("allowResponses", this.allowResponses);
        kparams.addStringIfNotNull("commercialPolicy", this.commercialPolicy);
        kparams.addStringIfNotNull("ugcPolicy", this.ugcPolicy);
        kparams.addStringIfNotNull("target", this.target);
        return kparams;
    }
}

