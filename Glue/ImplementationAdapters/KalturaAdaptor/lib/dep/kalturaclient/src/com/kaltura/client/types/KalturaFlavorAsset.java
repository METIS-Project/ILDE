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

public class KalturaFlavorAsset extends KalturaAsset {
    public int flavorParamsId = Integer.MIN_VALUE;
    public int width = Integer.MIN_VALUE;
    public int height = Integer.MIN_VALUE;
    public int bitrate = Integer.MIN_VALUE;
    public int frameRate = Integer.MIN_VALUE;
    public boolean isOriginal;
    public boolean isWeb;
    public String containerFormat;
    public String videoCodecId;

    public KalturaFlavorAsset() {
    }

    public KalturaFlavorAsset(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("width")) {
                try {
                    if (!txt.equals("")) this.width = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("height")) {
                try {
                    if (!txt.equals("")) this.height = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("bitrate")) {
                try {
                    if (!txt.equals("")) this.bitrate = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("frameRate")) {
                try {
                    if (!txt.equals("")) this.frameRate = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("isOriginal")) {
                if (!txt.equals("")) this.isOriginal = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("isWeb")) {
                if (!txt.equals("")) this.isWeb = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("containerFormat")) {
                this.containerFormat = txt;
                continue;
            } else if (nodeName.equals("videoCodecId")) {
                this.videoCodecId = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaFlavorAsset");
        kparams.addIntIfNotNull("flavorParamsId", this.flavorParamsId);
        return kparams;
    }
}

