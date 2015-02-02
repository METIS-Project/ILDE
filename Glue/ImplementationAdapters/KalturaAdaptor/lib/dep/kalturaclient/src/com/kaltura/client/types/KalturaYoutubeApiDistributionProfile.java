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

public class KalturaYoutubeApiDistributionProfile extends KalturaDistributionProfile {
    public String username;
    public String password;
    public String defaultCategory;
    public String allowComments;
    public String allowEmbedding;
    public String allowRatings;
    public String allowResponses;
    public int metadataProfileId = Integer.MIN_VALUE;

    public KalturaYoutubeApiDistributionProfile() {
    }

    public KalturaYoutubeApiDistributionProfile(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("password")) {
                this.password = txt;
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
            } else if (nodeName.equals("metadataProfileId")) {
                try {
                    if (!txt.equals("")) this.metadataProfileId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaYoutubeApiDistributionProfile");
        kparams.addStringIfNotNull("username", this.username);
        kparams.addStringIfNotNull("password", this.password);
        kparams.addStringIfNotNull("defaultCategory", this.defaultCategory);
        kparams.addStringIfNotNull("allowComments", this.allowComments);
        kparams.addStringIfNotNull("allowEmbedding", this.allowEmbedding);
        kparams.addStringIfNotNull("allowRatings", this.allowRatings);
        kparams.addStringIfNotNull("allowResponses", this.allowResponses);
        kparams.addIntIfNotNull("metadataProfileId", this.metadataProfileId);
        return kparams;
    }
}

