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

public class KalturaFlavorParamsOutput extends KalturaFlavorParams {
    public int flavorParamsId = Integer.MIN_VALUE;
    public String commandLinesStr;
    public String flavorParamsVersion;
    public String flavorAssetId;
    public String flavorAssetVersion;
    public int readyBehavior = Integer.MIN_VALUE;

    public KalturaFlavorParamsOutput() {
    }

    public KalturaFlavorParamsOutput(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("flavorParamsId")) {
                try {
                    if (!txt.equals("")) this.flavorParamsId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("commandLinesStr")) {
                this.commandLinesStr = txt;
                continue;
            } else if (nodeName.equals("flavorParamsVersion")) {
                this.flavorParamsVersion = txt;
                continue;
            } else if (nodeName.equals("flavorAssetId")) {
                this.flavorAssetId = txt;
                continue;
            } else if (nodeName.equals("flavorAssetVersion")) {
                this.flavorAssetVersion = txt;
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
        kparams.setString("objectType", "KalturaFlavorParamsOutput");
        kparams.addIntIfNotNull("flavorParamsId", this.flavorParamsId);
        kparams.addStringIfNotNull("commandLinesStr", this.commandLinesStr);
        kparams.addStringIfNotNull("flavorParamsVersion", this.flavorParamsVersion);
        kparams.addStringIfNotNull("flavorAssetId", this.flavorAssetId);
        kparams.addStringIfNotNull("flavorAssetVersion", this.flavorAssetVersion);
        kparams.addIntIfNotNull("readyBehavior", this.readyBehavior);
        return kparams;
    }
}

