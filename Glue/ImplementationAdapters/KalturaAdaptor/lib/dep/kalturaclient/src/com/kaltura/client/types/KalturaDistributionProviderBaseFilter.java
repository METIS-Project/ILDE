package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaDistributionProviderType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaDistributionProviderBaseFilter extends KalturaFilter {
    public KalturaDistributionProviderType typeEqual;
    public String typeIn;

    public KalturaDistributionProviderBaseFilter() {
    }

    public KalturaDistributionProviderBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("typeEqual")) {
                try {
                    if (!txt.equals("")) this.typeEqual = KalturaDistributionProviderType.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("typeIn")) {
                this.typeIn = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaDistributionProviderBaseFilter");
        if (typeEqual != null) kparams.addStringIfNotNull("typeEqual", this.typeEqual.getHashCode());
        kparams.addStringIfNotNull("typeIn", this.typeIn);
        return kparams;
    }
}

