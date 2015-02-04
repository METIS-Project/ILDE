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

public abstract class KalturaAssetParamsOutputBaseFilter extends KalturaAssetParamsFilter {
    public int assetParamsIdEqual = Integer.MIN_VALUE;
    public String assetParamsVersionEqual;
    public String assetIdEqual;
    public String assetVersionEqual;

    public KalturaAssetParamsOutputBaseFilter() {
    }

    public KalturaAssetParamsOutputBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("assetParamsIdEqual")) {
                try {
                    if (!txt.equals("")) this.assetParamsIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("assetParamsVersionEqual")) {
                this.assetParamsVersionEqual = txt;
                continue;
            } else if (nodeName.equals("assetIdEqual")) {
                this.assetIdEqual = txt;
                continue;
            } else if (nodeName.equals("assetVersionEqual")) {
                this.assetVersionEqual = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaAssetParamsOutputBaseFilter");
        kparams.addIntIfNotNull("assetParamsIdEqual", this.assetParamsIdEqual);
        kparams.addStringIfNotNull("assetParamsVersionEqual", this.assetParamsVersionEqual);
        kparams.addStringIfNotNull("assetIdEqual", this.assetIdEqual);
        kparams.addStringIfNotNull("assetVersionEqual", this.assetVersionEqual);
        return kparams;
    }
}

