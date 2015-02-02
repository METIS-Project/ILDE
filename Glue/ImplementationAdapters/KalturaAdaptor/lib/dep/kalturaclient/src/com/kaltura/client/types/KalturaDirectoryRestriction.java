package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaDirectoryRestrictionType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaDirectoryRestriction extends KalturaBaseRestriction {
    public KalturaDirectoryRestrictionType directoryRestrictionType;

    public KalturaDirectoryRestriction() {
    }

    public KalturaDirectoryRestriction(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("directoryRestrictionType")) {
                try {
                    if (!txt.equals("")) this.directoryRestrictionType = KalturaDirectoryRestrictionType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaDirectoryRestriction");
        if (directoryRestrictionType != null) kparams.addIntIfNotNull("directoryRestrictionType", this.directoryRestrictionType.getHashCode());
        return kparams;
    }
}

