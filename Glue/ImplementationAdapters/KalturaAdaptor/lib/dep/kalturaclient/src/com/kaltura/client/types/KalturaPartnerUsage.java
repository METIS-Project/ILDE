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

public class KalturaPartnerUsage extends KalturaObjectBase {
    public float hostingGB = Float.MIN_VALUE;
    public float Percent = Float.MIN_VALUE;
    public int packageBW = Integer.MIN_VALUE;
    public int usageGB = Integer.MIN_VALUE;
    public int reachedLimitDate = Integer.MIN_VALUE;
    public String usageGraph;

    public KalturaPartnerUsage() {
    }

    public KalturaPartnerUsage(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("hostingGB")) {
                try {
                    if (!txt.equals("")) this.hostingGB = Float.parseFloat(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("Percent")) {
                try {
                    if (!txt.equals("")) this.Percent = Float.parseFloat(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("packageBW")) {
                try {
                    if (!txt.equals("")) this.packageBW = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("usageGB")) {
                try {
                    if (!txt.equals("")) this.usageGB = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("reachedLimitDate")) {
                try {
                    if (!txt.equals("")) this.reachedLimitDate = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("usageGraph")) {
                this.usageGraph = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaPartnerUsage");
        return kparams;
    }
}

