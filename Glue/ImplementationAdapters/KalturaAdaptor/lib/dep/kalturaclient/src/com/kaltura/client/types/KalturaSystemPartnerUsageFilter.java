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

public class KalturaSystemPartnerUsageFilter extends KalturaFilter {
    public int fromDate = Integer.MIN_VALUE;
    public int toDate = Integer.MIN_VALUE;

    public KalturaSystemPartnerUsageFilter() {
    }

    public KalturaSystemPartnerUsageFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("fromDate")) {
                try {
                    if (!txt.equals("")) this.fromDate = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("toDate")) {
                try {
                    if (!txt.equals("")) this.toDate = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaSystemPartnerUsageFilter");
        kparams.addIntIfNotNull("fromDate", this.fromDate);
        kparams.addIntIfNotNull("toDate", this.toDate);
        return kparams;
    }
}

