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

public class KalturaThumbParamsOutput extends KalturaThumbParams {
    public int thumbParamsId = Integer.MIN_VALUE;
    public String thumbParamsVersion;
    public String thumbAssetId;
    public String thumbAssetVersion;

    public KalturaThumbParamsOutput() {
    }

    public KalturaThumbParamsOutput(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("thumbParamsId")) {
                try {
                    if (!txt.equals("")) this.thumbParamsId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("thumbParamsVersion")) {
                this.thumbParamsVersion = txt;
                continue;
            } else if (nodeName.equals("thumbAssetId")) {
                this.thumbAssetId = txt;
                continue;
            } else if (nodeName.equals("thumbAssetVersion")) {
                this.thumbAssetVersion = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaThumbParamsOutput");
        kparams.addIntIfNotNull("thumbParamsId", this.thumbParamsId);
        kparams.addStringIfNotNull("thumbParamsVersion", this.thumbParamsVersion);
        kparams.addStringIfNotNull("thumbAssetId", this.thumbAssetId);
        kparams.addStringIfNotNull("thumbAssetVersion", this.thumbAssetVersion);
        return kparams;
    }
}

