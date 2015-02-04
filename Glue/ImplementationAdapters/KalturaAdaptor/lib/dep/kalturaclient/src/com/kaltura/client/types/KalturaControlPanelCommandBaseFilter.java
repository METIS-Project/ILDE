package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaControlPanelCommandType;
import com.kaltura.client.enums.KalturaControlPanelCommandTargetType;
import com.kaltura.client.enums.KalturaControlPanelCommandStatus;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaControlPanelCommandBaseFilter extends KalturaFilter {
    public int idEqual = Integer.MIN_VALUE;
    public String idIn;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int createdByIdEqual = Integer.MIN_VALUE;
    public KalturaControlPanelCommandType typeEqual;
    public String typeIn;
    public KalturaControlPanelCommandTargetType targetTypeEqual;
    public String targetTypeIn;
    public KalturaControlPanelCommandStatus statusEqual;
    public String statusIn;

    public KalturaControlPanelCommandBaseFilter() {
    }

    public KalturaControlPanelCommandBaseFilter(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("createdByIdEqual")) {
                try {
                    if (!txt.equals("")) this.createdByIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("typeEqual")) {
                try {
                    if (!txt.equals("")) this.typeEqual = KalturaControlPanelCommandType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("typeIn")) {
                this.typeIn = txt;
                continue;
            } else if (nodeName.equals("targetTypeEqual")) {
                try {
                    if (!txt.equals("")) this.targetTypeEqual = KalturaControlPanelCommandTargetType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("targetTypeIn")) {
                this.targetTypeIn = txt;
                continue;
            } else if (nodeName.equals("statusEqual")) {
                try {
                    if (!txt.equals("")) this.statusEqual = KalturaControlPanelCommandStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaControlPanelCommandBaseFilter");
        kparams.addIntIfNotNull("idEqual", this.idEqual);
        kparams.addStringIfNotNull("idIn", this.idIn);
        kparams.addIntIfNotNull("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.addIntIfNotNull("createdByIdEqual", this.createdByIdEqual);
        if (typeEqual != null) kparams.addIntIfNotNull("typeEqual", this.typeEqual.getHashCode());
        kparams.addStringIfNotNull("typeIn", this.typeIn);
        if (targetTypeEqual != null) kparams.addIntIfNotNull("targetTypeEqual", this.targetTypeEqual.getHashCode());
        kparams.addStringIfNotNull("targetTypeIn", this.targetTypeIn);
        if (statusEqual != null) kparams.addIntIfNotNull("statusEqual", this.statusEqual.getHashCode());
        kparams.addStringIfNotNull("statusIn", this.statusIn);
        return kparams;
    }
}

