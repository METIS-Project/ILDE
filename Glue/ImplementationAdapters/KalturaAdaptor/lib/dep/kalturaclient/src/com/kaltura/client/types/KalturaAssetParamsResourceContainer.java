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

public class KalturaAssetParamsResourceContainer extends KalturaResource {
    public KalturaContentResource resource;
    public int assetParamsId = Integer.MIN_VALUE;

    public KalturaAssetParamsResourceContainer() {
    }

    public KalturaAssetParamsResourceContainer(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("resource")) {
                this.resource = (KalturaContentResource)KalturaObjectFactory.create((Element)aNode);
                continue;
            } else if (nodeName.equals("assetParamsId")) {
                try {
                    if (!txt.equals("")) this.assetParamsId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaAssetParamsResourceContainer");
        kparams.addObjectIfNotNull("resource", this.resource);
        kparams.addIntIfNotNull("assetParamsId", this.assetParamsId);
        return kparams;
    }
}

