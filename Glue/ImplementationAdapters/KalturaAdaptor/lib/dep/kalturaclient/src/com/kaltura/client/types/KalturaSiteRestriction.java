package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaSiteRestrictionType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaSiteRestriction extends KalturaBaseRestriction {
    public KalturaSiteRestrictionType siteRestrictionType;
    public String siteList;

    public KalturaSiteRestriction() {
    }

    public KalturaSiteRestriction(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("siteRestrictionType")) {
                try {
                    if (!txt.equals("")) this.siteRestrictionType = KalturaSiteRestrictionType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("siteList")) {
                this.siteList = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaSiteRestriction");
        if (siteRestrictionType != null) kparams.addIntIfNotNull("siteRestrictionType", this.siteRestrictionType.getHashCode());
        kparams.addStringIfNotNull("siteList", this.siteList);
        return kparams;
    }
}

