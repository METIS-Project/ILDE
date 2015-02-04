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

public class KalturaSyndicationFeedEntryCount extends KalturaObjectBase {
    public int totalEntryCount = Integer.MIN_VALUE;
    public int actualEntryCount = Integer.MIN_VALUE;
    public int requireTranscodingCount = Integer.MIN_VALUE;

    public KalturaSyndicationFeedEntryCount() {
    }

    public KalturaSyndicationFeedEntryCount(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("totalEntryCount")) {
                try {
                    if (!txt.equals("")) this.totalEntryCount = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("actualEntryCount")) {
                try {
                    if (!txt.equals("")) this.actualEntryCount = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("requireTranscodingCount")) {
                try {
                    if (!txt.equals("")) this.requireTranscodingCount = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaSyndicationFeedEntryCount");
        kparams.addIntIfNotNull("totalEntryCount", this.totalEntryCount);
        kparams.addIntIfNotNull("actualEntryCount", this.actualEntryCount);
        kparams.addIntIfNotNull("requireTranscodingCount", this.requireTranscodingCount);
        return kparams;
    }
}

