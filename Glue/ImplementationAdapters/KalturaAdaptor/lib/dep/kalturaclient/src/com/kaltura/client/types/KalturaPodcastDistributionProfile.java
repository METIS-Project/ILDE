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

public class KalturaPodcastDistributionProfile extends KalturaDistributionProfile {
    public String xsl;
    public String feedId;
    public int metadataProfileId = Integer.MIN_VALUE;

    public KalturaPodcastDistributionProfile() {
    }

    public KalturaPodcastDistributionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("xsl")) {
                this.xsl = txt;
                continue;
            } else if (nodeName.equals("feedId")) {
                this.feedId = txt;
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
        kparams.setString("objectType", "KalturaPodcastDistributionProfile");
        kparams.addStringIfNotNull("xsl", this.xsl);
        kparams.addIntIfNotNull("metadataProfileId", this.metadataProfileId);
        return kparams;
    }
}

