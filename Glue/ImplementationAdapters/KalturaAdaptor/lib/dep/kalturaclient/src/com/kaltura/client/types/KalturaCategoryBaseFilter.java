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

public abstract class KalturaCategoryBaseFilter extends KalturaFilter {
    public int idEqual = Integer.MIN_VALUE;
    public String idIn;
    public int parentIdEqual = Integer.MIN_VALUE;
    public String parentIdIn;
    public int depthEqual = Integer.MIN_VALUE;
    public String fullNameEqual;
    public String fullNameStartsWith;

    public KalturaCategoryBaseFilter() {
    }

    public KalturaCategoryBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("idEqual")) {
                try {
                    if (!txt.equals("")) this.idEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("idIn")) {
                this.idIn = txt;
                continue;
            } else if (nodeName.equals("parentIdEqual")) {
                try {
                    if (!txt.equals("")) this.parentIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("parentIdIn")) {
                this.parentIdIn = txt;
                continue;
            } else if (nodeName.equals("depthEqual")) {
                try {
                    if (!txt.equals("")) this.depthEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("fullNameEqual")) {
                this.fullNameEqual = txt;
                continue;
            } else if (nodeName.equals("fullNameStartsWith")) {
                this.fullNameStartsWith = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaCategoryBaseFilter");
        kparams.addIntIfNotNull("idEqual", this.idEqual);
        kparams.addStringIfNotNull("idIn", this.idIn);
        kparams.addIntIfNotNull("parentIdEqual", this.parentIdEqual);
        kparams.addStringIfNotNull("parentIdIn", this.parentIdIn);
        kparams.addIntIfNotNull("depthEqual", this.depthEqual);
        kparams.addStringIfNotNull("fullNameEqual", this.fullNameEqual);
        kparams.addStringIfNotNull("fullNameStartsWith", this.fullNameStartsWith);
        return kparams;
    }
}

