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

public class KalturaConversionProfileAssetParamsFilter extends KalturaConversionProfileAssetParamsBaseFilter {
    public KalturaConversionProfileFilter conversionProfileIdFilter;
    public KalturaAssetParamsFilter assetParamsIdFilter;

    public KalturaConversionProfileAssetParamsFilter() {
    }

    public KalturaConversionProfileAssetParamsFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("conversionProfileIdFilter")) {
                this.conversionProfileIdFilter = (KalturaConversionProfileFilter)KalturaObjectFactory.create((Element)aNode);
                continue;
            } else if (nodeName.equals("assetParamsIdFilter")) {
                this.assetParamsIdFilter = (KalturaAssetParamsFilter)KalturaObjectFactory.create((Element)aNode);
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaConversionProfileAssetParamsFilter");
        kparams.addObjectIfNotNull("conversionProfileIdFilter", this.conversionProfileIdFilter);
        kparams.addObjectIfNotNull("assetParamsIdFilter", this.assetParamsIdFilter);
        return kparams;
    }
}

