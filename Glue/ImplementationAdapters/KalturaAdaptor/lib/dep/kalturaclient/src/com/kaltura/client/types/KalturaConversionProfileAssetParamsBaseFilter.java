package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaFlavorReadyBehaviorType;
import com.kaltura.client.enums.KalturaAssetParamsOrigin;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaConversionProfileAssetParamsBaseFilter extends KalturaFilter {
    public int conversionProfileIdEqual = Integer.MIN_VALUE;
    public String conversionProfileIdIn;
    public int assetParamsIdEqual = Integer.MIN_VALUE;
    public String assetParamsIdIn;
    public KalturaFlavorReadyBehaviorType readyBehaviorEqual;
    public String readyBehaviorIn;
    public KalturaAssetParamsOrigin originEqual;
    public String originIn;
    public String systemNameEqual;
    public String systemNameIn;

    public KalturaConversionProfileAssetParamsBaseFilter() {
    }

    public KalturaConversionProfileAssetParamsBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("conversionProfileIdEqual")) {
                try {
                    if (!txt.equals("")) this.conversionProfileIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("conversionProfileIdIn")) {
                this.conversionProfileIdIn = txt;
                continue;
            } else if (nodeName.equals("assetParamsIdEqual")) {
                try {
                    if (!txt.equals("")) this.assetParamsIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("assetParamsIdIn")) {
                this.assetParamsIdIn = txt;
                continue;
            } else if (nodeName.equals("readyBehaviorEqual")) {
                try {
                    if (!txt.equals("")) this.readyBehaviorEqual = KalturaFlavorReadyBehaviorType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("readyBehaviorIn")) {
                this.readyBehaviorIn = txt;
                continue;
            } else if (nodeName.equals("originEqual")) {
                try {
                    if (!txt.equals("")) this.originEqual = KalturaAssetParamsOrigin.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("originIn")) {
                this.originIn = txt;
                continue;
            } else if (nodeName.equals("systemNameEqual")) {
                this.systemNameEqual = txt;
                continue;
            } else if (nodeName.equals("systemNameIn")) {
                this.systemNameIn = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaConversionProfileAssetParamsBaseFilter");
        kparams.addIntIfNotNull("conversionProfileIdEqual", this.conversionProfileIdEqual);
        kparams.addStringIfNotNull("conversionProfileIdIn", this.conversionProfileIdIn);
        kparams.addIntIfNotNull("assetParamsIdEqual", this.assetParamsIdEqual);
        kparams.addStringIfNotNull("assetParamsIdIn", this.assetParamsIdIn);
        if (readyBehaviorEqual != null) kparams.addIntIfNotNull("readyBehaviorEqual", this.readyBehaviorEqual.getHashCode());
        kparams.addStringIfNotNull("readyBehaviorIn", this.readyBehaviorIn);
        if (originEqual != null) kparams.addIntIfNotNull("originEqual", this.originEqual.getHashCode());
        kparams.addStringIfNotNull("originIn", this.originIn);
        kparams.addStringIfNotNull("systemNameEqual", this.systemNameEqual);
        kparams.addStringIfNotNull("systemNameIn", this.systemNameIn);
        return kparams;
    }
}

