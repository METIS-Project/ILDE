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

public class KalturaTVComDistributionProfile extends KalturaConfigurableDistributionProfile {
    public int metadataProfileId = Integer.MIN_VALUE;
    public String feedUrl;
    public String feedTitle;
    public String feedLink;
    public String feedDescription;
    public String feedLanguage;
    public String feedCopyright;
    public String feedImageTitle;
    public String feedImageUrl;
    public String feedImageLink;
    public int feedImageWidth = Integer.MIN_VALUE;
    public int feedImageHeight = Integer.MIN_VALUE;

    public KalturaTVComDistributionProfile() {
    }

    public KalturaTVComDistributionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("metadataProfileId")) {
                try {
                    if (!txt.equals("")) this.metadataProfileId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("feedUrl")) {
                this.feedUrl = txt;
                continue;
            } else if (nodeName.equals("feedTitle")) {
                this.feedTitle = txt;
                continue;
            } else if (nodeName.equals("feedLink")) {
                this.feedLink = txt;
                continue;
            } else if (nodeName.equals("feedDescription")) {
                this.feedDescription = txt;
                continue;
            } else if (nodeName.equals("feedLanguage")) {
                this.feedLanguage = txt;
                continue;
            } else if (nodeName.equals("feedCopyright")) {
                this.feedCopyright = txt;
                continue;
            } else if (nodeName.equals("feedImageTitle")) {
                this.feedImageTitle = txt;
                continue;
            } else if (nodeName.equals("feedImageUrl")) {
                this.feedImageUrl = txt;
                continue;
            } else if (nodeName.equals("feedImageLink")) {
                this.feedImageLink = txt;
                continue;
            } else if (nodeName.equals("feedImageWidth")) {
                try {
                    if (!txt.equals("")) this.feedImageWidth = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("feedImageHeight")) {
                try {
                    if (!txt.equals("")) this.feedImageHeight = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaTVComDistributionProfile");
        kparams.addIntIfNotNull("metadataProfileId", this.metadataProfileId);
        kparams.addStringIfNotNull("feedTitle", this.feedTitle);
        kparams.addStringIfNotNull("feedLink", this.feedLink);
        kparams.addStringIfNotNull("feedDescription", this.feedDescription);
        kparams.addStringIfNotNull("feedLanguage", this.feedLanguage);
        kparams.addStringIfNotNull("feedCopyright", this.feedCopyright);
        kparams.addStringIfNotNull("feedImageTitle", this.feedImageTitle);
        kparams.addStringIfNotNull("feedImageUrl", this.feedImageUrl);
        kparams.addStringIfNotNull("feedImageLink", this.feedImageLink);
        kparams.addIntIfNotNull("feedImageWidth", this.feedImageWidth);
        kparams.addIntIfNotNull("feedImageHeight", this.feedImageHeight);
        return kparams;
    }
}

