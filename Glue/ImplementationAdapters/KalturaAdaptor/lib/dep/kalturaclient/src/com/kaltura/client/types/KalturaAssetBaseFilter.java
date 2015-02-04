package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaFlavorAssetStatus;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaAssetBaseFilter extends KalturaFilter {
    public String idEqual;
    public String idIn;
    public String entryIdEqual;
    public String entryIdIn;
    public int partnerIdEqual = Integer.MIN_VALUE;
    public String partnerIdIn;
    public KalturaFlavorAssetStatus statusEqual;
    public String statusIn;
    public String statusNotIn;
    public int sizeGreaterThanOrEqual = Integer.MIN_VALUE;
    public int sizeLessThanOrEqual = Integer.MIN_VALUE;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;
    public int deletedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int deletedAtLessThanOrEqual = Integer.MIN_VALUE;

    public KalturaAssetBaseFilter() {
    }

    public KalturaAssetBaseFilter(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("entryIdEqual")) {
                this.entryIdEqual = txt;
                continue;
            } else if (nodeName.equals("entryIdIn")) {
                this.entryIdIn = txt;
                continue;
            } else if (nodeName.equals("partnerIdEqual")) {
                try {
                    if (!txt.equals("")) this.partnerIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerIdIn")) {
                this.partnerIdIn = txt;
                continue;
            } else if (nodeName.equals("statusEqual")) {
                try {
                    if (!txt.equals("")) this.statusEqual = KalturaFlavorAssetStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = txt;
                continue;
            } else if (nodeName.equals("statusNotIn")) {
                this.statusNotIn = txt;
                continue;
            } else if (nodeName.equals("sizeGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.sizeGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("sizeLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.sizeLessThanOrEqual = Integer.parseInt(txt);
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
            } else if (nodeName.equals("deletedAtGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.deletedAtGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("deletedAtLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.deletedAtLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaAssetBaseFilter");
        kparams.addStringIfNotNull("idEqual", this.idEqual);
        kparams.addStringIfNotNull("idIn", this.idIn);
        kparams.addStringIfNotNull("entryIdEqual", this.entryIdEqual);
        kparams.addStringIfNotNull("entryIdIn", this.entryIdIn);
        kparams.addIntIfNotNull("partnerIdEqual", this.partnerIdEqual);
        kparams.addStringIfNotNull("partnerIdIn", this.partnerIdIn);
        if (statusEqual != null) kparams.addIntIfNotNull("statusEqual", this.statusEqual.getHashCode());
        kparams.addStringIfNotNull("statusIn", this.statusIn);
        kparams.addStringIfNotNull("statusNotIn", this.statusNotIn);
        kparams.addIntIfNotNull("sizeGreaterThanOrEqual", this.sizeGreaterThanOrEqual);
        kparams.addIntIfNotNull("sizeLessThanOrEqual", this.sizeLessThanOrEqual);
        kparams.addIntIfNotNull("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.addIntIfNotNull("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        kparams.addIntIfNotNull("deletedAtGreaterThanOrEqual", this.deletedAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("deletedAtLessThanOrEqual", this.deletedAtLessThanOrEqual);
        return kparams;
    }
}

