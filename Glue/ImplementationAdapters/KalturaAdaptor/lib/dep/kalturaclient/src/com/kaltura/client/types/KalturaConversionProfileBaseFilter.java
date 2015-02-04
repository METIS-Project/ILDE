package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaConversionProfileStatus;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaConversionProfileBaseFilter extends KalturaFilter {
    public int idEqual = Integer.MIN_VALUE;
    public String idIn;
    public KalturaConversionProfileStatus statusEqual;
    public String statusIn;
    public String nameEqual;
    public String systemNameEqual;
    public String systemNameIn;
    public String tagsMultiLikeOr;
    public String tagsMultiLikeAnd;
    public String defaultEntryIdEqual;
    public String defaultEntryIdIn;

    public KalturaConversionProfileBaseFilter() {
    }

    public KalturaConversionProfileBaseFilter(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("statusEqual")) {
                try {
                    if (!txt.equals("")) this.statusEqual = KalturaConversionProfileStatus.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = txt;
                continue;
            } else if (nodeName.equals("nameEqual")) {
                this.nameEqual = txt;
                continue;
            } else if (nodeName.equals("systemNameEqual")) {
                this.systemNameEqual = txt;
                continue;
            } else if (nodeName.equals("systemNameIn")) {
                this.systemNameIn = txt;
                continue;
            } else if (nodeName.equals("tagsMultiLikeOr")) {
                this.tagsMultiLikeOr = txt;
                continue;
            } else if (nodeName.equals("tagsMultiLikeAnd")) {
                this.tagsMultiLikeAnd = txt;
                continue;
            } else if (nodeName.equals("defaultEntryIdEqual")) {
                this.defaultEntryIdEqual = txt;
                continue;
            } else if (nodeName.equals("defaultEntryIdIn")) {
                this.defaultEntryIdIn = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaConversionProfileBaseFilter");
        kparams.addIntIfNotNull("idEqual", this.idEqual);
        kparams.addStringIfNotNull("idIn", this.idIn);
        if (statusEqual != null) kparams.addStringIfNotNull("statusEqual", this.statusEqual.getHashCode());
        kparams.addStringIfNotNull("statusIn", this.statusIn);
        kparams.addStringIfNotNull("nameEqual", this.nameEqual);
        kparams.addStringIfNotNull("systemNameEqual", this.systemNameEqual);
        kparams.addStringIfNotNull("systemNameIn", this.systemNameIn);
        kparams.addStringIfNotNull("tagsMultiLikeOr", this.tagsMultiLikeOr);
        kparams.addStringIfNotNull("tagsMultiLikeAnd", this.tagsMultiLikeAnd);
        kparams.addStringIfNotNull("defaultEntryIdEqual", this.defaultEntryIdEqual);
        kparams.addStringIfNotNull("defaultEntryIdIn", this.defaultEntryIdIn);
        return kparams;
    }
}

