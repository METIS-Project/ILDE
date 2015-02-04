package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaMediaType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaMediaEntryBaseFilter extends KalturaPlayableEntryFilter {
    public KalturaMediaType mediaTypeEqual;
    public String mediaTypeIn;
    public int mediaDateGreaterThanOrEqual = Integer.MIN_VALUE;
    public int mediaDateLessThanOrEqual = Integer.MIN_VALUE;
    public String flavorParamsIdsMatchOr;
    public String flavorParamsIdsMatchAnd;

    public KalturaMediaEntryBaseFilter() {
    }

    public KalturaMediaEntryBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("mediaTypeEqual")) {
                try {
                    if (!txt.equals("")) this.mediaTypeEqual = KalturaMediaType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("mediaTypeIn")) {
                this.mediaTypeIn = txt;
                continue;
            } else if (nodeName.equals("mediaDateGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.mediaDateGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("mediaDateLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.mediaDateLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("flavorParamsIdsMatchOr")) {
                this.flavorParamsIdsMatchOr = txt;
                continue;
            } else if (nodeName.equals("flavorParamsIdsMatchAnd")) {
                this.flavorParamsIdsMatchAnd = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaMediaEntryBaseFilter");
        if (mediaTypeEqual != null) kparams.addIntIfNotNull("mediaTypeEqual", this.mediaTypeEqual.getHashCode());
        kparams.addStringIfNotNull("mediaTypeIn", this.mediaTypeIn);
        kparams.addIntIfNotNull("mediaDateGreaterThanOrEqual", this.mediaDateGreaterThanOrEqual);
        kparams.addIntIfNotNull("mediaDateLessThanOrEqual", this.mediaDateLessThanOrEqual);
        kparams.addStringIfNotNull("flavorParamsIdsMatchOr", this.flavorParamsIdsMatchOr);
        kparams.addStringIfNotNull("flavorParamsIdsMatchAnd", this.flavorParamsIdsMatchAnd);
        return kparams;
    }
}

