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

public abstract class KalturaWidgetBaseFilter extends KalturaFilter {
    public String idEqual;
    public String idIn;
    public String sourceWidgetIdEqual;
    public String rootWidgetIdEqual;
    public int partnerIdEqual = Integer.MIN_VALUE;
    public String entryIdEqual;
    public int uiConfIdEqual = Integer.MIN_VALUE;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;
    public String partnerDataLike;

    public KalturaWidgetBaseFilter() {
    }

    public KalturaWidgetBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("idEqual")) {
                this.idEqual = txt;
                continue;
            } else if (nodeName.equals("idIn")) {
                this.idIn = txt;
                continue;
            } else if (nodeName.equals("sourceWidgetIdEqual")) {
                this.sourceWidgetIdEqual = txt;
                continue;
            } else if (nodeName.equals("rootWidgetIdEqual")) {
                this.rootWidgetIdEqual = txt;
                continue;
            } else if (nodeName.equals("partnerIdEqual")) {
                try {
                    if (!txt.equals("")) this.partnerIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("entryIdEqual")) {
                this.entryIdEqual = txt;
                continue;
            } else if (nodeName.equals("uiConfIdEqual")) {
                try {
                    if (!txt.equals("")) this.uiConfIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("createdAtGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.createdAtGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("createdAtLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.createdAtLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("updatedAtGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.updatedAtGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("updatedAtLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.updatedAtLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerDataLike")) {
                this.partnerDataLike = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaWidgetBaseFilter");
        kparams.addStringIfNotNull("idEqual", this.idEqual);
        kparams.addStringIfNotNull("idIn", this.idIn);
        kparams.addStringIfNotNull("sourceWidgetIdEqual", this.sourceWidgetIdEqual);
        kparams.addStringIfNotNull("rootWidgetIdEqual", this.rootWidgetIdEqual);
        kparams.addIntIfNotNull("partnerIdEqual", this.partnerIdEqual);
        kparams.addStringIfNotNull("entryIdEqual", this.entryIdEqual);
        kparams.addIntIfNotNull("uiConfIdEqual", this.uiConfIdEqual);
        kparams.addIntIfNotNull("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.addIntIfNotNull("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        kparams.addStringIfNotNull("partnerDataLike", this.partnerDataLike);
        return kparams;
    }
}

