package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaGoogleSyndicationFeedAdultValues;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaGoogleVideoSyndicationFeed extends KalturaBaseSyndicationFeed {
    public KalturaGoogleSyndicationFeedAdultValues adultContent;

    public KalturaGoogleVideoSyndicationFeed() {
    }

    public KalturaGoogleVideoSyndicationFeed(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("adultContent")) {
                try {
                    if (!txt.equals("")) this.adultContent = KalturaGoogleSyndicationFeedAdultValues.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaGoogleVideoSyndicationFeed");
        if (adultContent != null) kparams.addStringIfNotNull("adultContent", this.adultContent.getHashCode());
        return kparams;
    }
}

