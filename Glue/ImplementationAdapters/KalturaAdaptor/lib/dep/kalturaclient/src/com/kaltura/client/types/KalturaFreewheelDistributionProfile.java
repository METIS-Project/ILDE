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

public class KalturaFreewheelDistributionProfile extends KalturaDistributionProfile {
    public String apikey;
    public String email;
    public String accountId;
    public int metadataProfileId = Integer.MIN_VALUE;

    public KalturaFreewheelDistributionProfile() {
    }

    public KalturaFreewheelDistributionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("apikey")) {
                this.apikey = txt;
                continue;
            } else if (nodeName.equals("email")) {
                this.email = txt;
                continue;
            } else if (nodeName.equals("accountId")) {
                this.accountId = txt;
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
        kparams.setString("objectType", "KalturaFreewheelDistributionProfile");
        kparams.addStringIfNotNull("apikey", this.apikey);
        kparams.addStringIfNotNull("email", this.email);
        kparams.addStringIfNotNull("accountId", this.accountId);
        kparams.addIntIfNotNull("metadataProfileId", this.metadataProfileId);
        return kparams;
    }
}

