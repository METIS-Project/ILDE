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

public abstract class KalturaThumbParamsOutputBaseFilter extends KalturaThumbParamsFilter {
    public int thumbParamsIdEqual = Integer.MIN_VALUE;
    public String thumbParamsVersionEqual;
    public String thumbAssetIdEqual;
    public String thumbAssetVersionEqual;

    public KalturaThumbParamsOutputBaseFilter() {
    }

    public KalturaThumbParamsOutputBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("thumbParamsIdEqual")) {
                try {
                    if (!txt.equals("")) this.thumbParamsIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("thumbParamsVersionEqual")) {
                this.thumbParamsVersionEqual = txt;
                continue;
            } else if (nodeName.equals("thumbAssetIdEqual")) {
                this.thumbAssetIdEqual = txt;
                continue;
            } else if (nodeName.equals("thumbAssetVersionEqual")) {
                this.thumbAssetVersionEqual = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaThumbParamsOutputBaseFilter");
        kparams.addIntIfNotNull("thumbParamsIdEqual", this.thumbParamsIdEqual);
        kparams.addStringIfNotNull("thumbParamsVersionEqual", this.thumbParamsVersionEqual);
        kparams.addStringIfNotNull("thumbAssetIdEqual", this.thumbAssetIdEqual);
        kparams.addStringIfNotNull("thumbAssetVersionEqual", this.thumbAssetVersionEqual);
        return kparams;
    }
}

