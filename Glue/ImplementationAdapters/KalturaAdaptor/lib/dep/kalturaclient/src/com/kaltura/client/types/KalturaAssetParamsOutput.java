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

public class KalturaAssetParamsOutput extends KalturaAssetParams {
    public int assetParamsId = Integer.MIN_VALUE;
    public String assetParamsVersion;
    public String assetId;
    public String assetVersion;
    public int readyBehavior = Integer.MIN_VALUE;

    public KalturaAssetParamsOutput() {
    }

    public KalturaAssetParamsOutput(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("assetParamsId")) {
                try {
                    if (!txt.equals("")) this.assetParamsId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("assetParamsVersion")) {
                this.assetParamsVersion = txt;
                continue;
            } else if (nodeName.equals("assetId")) {
                this.assetId = txt;
                continue;
            } else if (nodeName.equals("assetVersion")) {
                this.assetVersion = txt;
                continue;
            } else if (nodeName.equals("readyBehavior")) {
                try {
                    if (!txt.equals("")) this.readyBehavior = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaAssetParamsOutput");
        kparams.addIntIfNotNull("assetParamsId", this.assetParamsId);
        kparams.addStringIfNotNull("assetParamsVersion", this.assetParamsVersion);
        kparams.addStringIfNotNull("assetId", this.assetId);
        kparams.addStringIfNotNull("assetVersion", this.assetVersion);
        kparams.addIntIfNotNull("readyBehavior", this.readyBehavior);
        return kparams;
    }
}

