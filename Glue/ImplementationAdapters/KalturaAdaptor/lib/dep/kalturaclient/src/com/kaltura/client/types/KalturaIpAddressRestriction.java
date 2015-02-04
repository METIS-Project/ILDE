package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaIpAddressRestrictionType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaIpAddressRestriction extends KalturaBaseRestriction {
    public KalturaIpAddressRestrictionType ipAddressRestrictionType;
    public String ipAddressList;

    public KalturaIpAddressRestriction() {
    }

    public KalturaIpAddressRestriction(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("ipAddressRestrictionType")) {
                try {
                    if (!txt.equals("")) this.ipAddressRestrictionType = KalturaIpAddressRestrictionType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("ipAddressList")) {
                this.ipAddressList = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaIpAddressRestriction");
        if (ipAddressRestrictionType != null) kparams.addIntIfNotNull("ipAddressRestrictionType", this.ipAddressRestrictionType.getHashCode());
        kparams.addStringIfNotNull("ipAddressList", this.ipAddressList);
        return kparams;
    }
}

