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

public class KalturaFilter extends KalturaObjectBase {
    public String orderBy;
    public KalturaSearchItem advancedSearch;

    public KalturaFilter() {
    }

    public KalturaFilter(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("orderBy")) {
                this.orderBy = txt;
                continue;
            } else if (nodeName.equals("advancedSearch")) {
                this.advancedSearch = (KalturaSearchItem)KalturaObjectFactory.create((Element)aNode);
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaFilter");
        kparams.addStringIfNotNull("orderBy", this.orderBy);
        kparams.addObjectIfNotNull("advancedSearch", this.advancedSearch);
        return kparams;
    }
}

