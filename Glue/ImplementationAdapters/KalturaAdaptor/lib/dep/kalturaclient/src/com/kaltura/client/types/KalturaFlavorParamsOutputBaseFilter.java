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

public abstract class KalturaFlavorParamsOutputBaseFilter extends KalturaFlavorParamsFilter {
    public int flavorParamsIdEqual = Integer.MIN_VALUE;
    public String flavorParamsVersionEqual;
    public String flavorAssetIdEqual;
    public String flavorAssetVersionEqual;

    public KalturaFlavorParamsOutputBaseFilter() {
    }

    public KalturaFlavorParamsOutputBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("flavorParamsIdEqual")) {
                try {
                    if (!txt.equals("")) this.flavorParamsIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("flavorParamsVersionEqual")) {
                this.flavorParamsVersionEqual = txt;
                continue;
            } else if (nodeName.equals("flavorAssetIdEqual")) {
                this.flavorAssetIdEqual = txt;
                continue;
            } else if (nodeName.equals("flavorAssetVersionEqual")) {
                this.flavorAssetVersionEqual = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaFlavorParamsOutputBaseFilter");
        kparams.addIntIfNotNull("flavorParamsIdEqual", this.flavorParamsIdEqual);
        kparams.addStringIfNotNull("flavorParamsVersionEqual", this.flavorParamsVersionEqual);
        kparams.addStringIfNotNull("flavorAssetIdEqual", this.flavorAssetIdEqual);
        kparams.addStringIfNotNull("flavorAssetVersionEqual", this.flavorAssetVersionEqual);
        return kparams;
    }
}

