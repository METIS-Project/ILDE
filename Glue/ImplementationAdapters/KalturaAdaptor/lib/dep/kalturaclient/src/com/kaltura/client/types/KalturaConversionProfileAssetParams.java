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

public class KalturaConversionProfileAssetParams extends KalturaObjectBase {
    public int conversionProfileId = Integer.MIN_VALUE;
    public int assetParamsId = Integer.MIN_VALUE;
    public KalturaFlavorReadyBehaviorType readyBehavior;
    public KalturaAssetParamsOrigin origin;
    public String systemName;

    public KalturaConversionProfileAssetParams() {
    }

    public KalturaConversionProfileAssetParams(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("conversionProfileId")) {
                try {
                    if (!txt.equals("")) this.conversionProfileId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("assetParamsId")) {
                try {
                    if (!txt.equals("")) this.assetParamsId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("readyBehavior")) {
                try {
                    if (!txt.equals("")) this.readyBehavior = KalturaFlavorReadyBehaviorType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("origin")) {
                try {
                    if (!txt.equals("")) this.origin = KalturaAssetParamsOrigin.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("systemName")) {
                this.systemName = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaConversionProfileAssetParams");
        if (readyBehavior != null) kparams.addIntIfNotNull("readyBehavior", this.readyBehavior.getHashCode());
        if (origin != null) kparams.addIntIfNotNull("origin", this.origin.getHashCode());
        kparams.addStringIfNotNull("systemName", this.systemName);
        return kparams;
    }
}

