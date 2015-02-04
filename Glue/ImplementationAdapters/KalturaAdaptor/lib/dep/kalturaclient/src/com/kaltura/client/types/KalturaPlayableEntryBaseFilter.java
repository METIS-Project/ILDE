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

public abstract class KalturaPlayableEntryBaseFilter extends KalturaBaseEntryFilter {
    public int durationLessThan = Integer.MIN_VALUE;
    public int durationGreaterThan = Integer.MIN_VALUE;
    public int durationLessThanOrEqual = Integer.MIN_VALUE;
    public int durationGreaterThanOrEqual = Integer.MIN_VALUE;
    public int msDurationLessThan = Integer.MIN_VALUE;
    public int msDurationGreaterThan = Integer.MIN_VALUE;
    public int msDurationLessThanOrEqual = Integer.MIN_VALUE;
    public int msDurationGreaterThanOrEqual = Integer.MIN_VALUE;
    public String durationTypeMatchOr;

    public KalturaPlayableEntryBaseFilter() {
    }

    public KalturaPlayableEntryBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("durationLessThan")) {
                try {
                    if (!txt.equals("")) this.durationLessThan = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("durationGreaterThan")) {
                try {
                    if (!txt.equals("")) this.durationGreaterThan = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("durationLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.durationLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("durationGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.durationGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("msDurationLessThan")) {
                try {
                    if (!txt.equals("")) this.msDurationLessThan = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("msDurationGreaterThan")) {
                try {
                    if (!txt.equals("")) this.msDurationGreaterThan = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("msDurationLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.msDurationLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("msDurationGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.msDurationGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("durationTypeMatchOr")) {
                this.durationTypeMatchOr = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaPlayableEntryBaseFilter");
        kparams.addIntIfNotNull("durationLessThan", this.durationLessThan);
        kparams.addIntIfNotNull("durationGreaterThan", this.durationGreaterThan);
        kparams.addIntIfNotNull("durationLessThanOrEqual", this.durationLessThanOrEqual);
        kparams.addIntIfNotNull("durationGreaterThanOrEqual", this.durationGreaterThanOrEqual);
        kparams.addIntIfNotNull("msDurationLessThan", this.msDurationLessThan);
        kparams.addIntIfNotNull("msDurationGreaterThan", this.msDurationGreaterThan);
        kparams.addIntIfNotNull("msDurationLessThanOrEqual", this.msDurationLessThanOrEqual);
        kparams.addIntIfNotNull("msDurationGreaterThanOrEqual", this.msDurationGreaterThanOrEqual);
        kparams.addStringIfNotNull("durationTypeMatchOr", this.durationTypeMatchOr);
        return kparams;
    }
}

