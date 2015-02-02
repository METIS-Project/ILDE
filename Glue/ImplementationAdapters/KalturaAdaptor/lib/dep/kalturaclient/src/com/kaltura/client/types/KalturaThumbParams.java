package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaThumbCropType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaThumbParams extends KalturaAssetParams {
    public KalturaThumbCropType cropType;
    public int quality = Integer.MIN_VALUE;
    public int cropX = Integer.MIN_VALUE;
    public int cropY = Integer.MIN_VALUE;
    public int cropWidth = Integer.MIN_VALUE;
    public int cropHeight = Integer.MIN_VALUE;
    public float videoOffset = Float.MIN_VALUE;
    public int width = Integer.MIN_VALUE;
    public int height = Integer.MIN_VALUE;
    public float scaleWidth = Float.MIN_VALUE;
    public float scaleHeight = Float.MIN_VALUE;
    public String backgroundColor;
    public int sourceParamsId = Integer.MIN_VALUE;

    public KalturaThumbParams() {
    }

    public KalturaThumbParams(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("cropType")) {
                try {
                    if (!txt.equals("")) this.cropType = KalturaThumbCropType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("quality")) {
                try {
                    if (!txt.equals("")) this.quality = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("cropX")) {
                try {
                    if (!txt.equals("")) this.cropX = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("cropY")) {
                try {
                    if (!txt.equals("")) this.cropY = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("cropWidth")) {
                try {
                    if (!txt.equals("")) this.cropWidth = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("cropHeight")) {
                try {
                    if (!txt.equals("")) this.cropHeight = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("videoOffset")) {
                try {
                    if (!txt.equals("")) this.videoOffset = Float.parseFloat(txt);
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
            } else if (nodeName.equals("scaleWidth")) {
                try {
                    if (!txt.equals("")) this.scaleWidth = Float.parseFloat(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("scaleHeight")) {
                try {
                    if (!txt.equals("")) this.scaleHeight = Float.parseFloat(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("backgroundColor")) {
                this.backgroundColor = txt;
                continue;
            } else if (nodeName.equals("sourceParamsId")) {
                try {
                    if (!txt.equals("")) this.sourceParamsId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaThumbParams");
        if (cropType != null) kparams.addIntIfNotNull("cropType", this.cropType.getHashCode());
        kparams.addIntIfNotNull("quality", this.quality);
        kparams.addIntIfNotNull("cropX", this.cropX);
        kparams.addIntIfNotNull("cropY", this.cropY);
        kparams.addIntIfNotNull("cropWidth", this.cropWidth);
        kparams.addIntIfNotNull("cropHeight", this.cropHeight);
        kparams.addFloatIfNotNull("videoOffset", this.videoOffset);
        kparams.addIntIfNotNull("width", this.width);
        kparams.addIntIfNotNull("height", this.height);
        kparams.addFloatIfNotNull("scaleWidth", this.scaleWidth);
        kparams.addFloatIfNotNull("scaleHeight", this.scaleHeight);
        kparams.addStringIfNotNull("backgroundColor", this.backgroundColor);
        kparams.addIntIfNotNull("sourceParamsId", this.sourceParamsId);
        return kparams;
    }
}

