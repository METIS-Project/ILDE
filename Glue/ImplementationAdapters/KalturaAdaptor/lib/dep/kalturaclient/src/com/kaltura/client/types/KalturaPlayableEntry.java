package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaDurationType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaPlayableEntry extends KalturaBaseEntry {
    public int plays = Integer.MIN_VALUE;
    public int views = Integer.MIN_VALUE;
    public int width = Integer.MIN_VALUE;
    public int height = Integer.MIN_VALUE;
    public int duration = Integer.MIN_VALUE;
    public int msDuration = Integer.MIN_VALUE;
    public KalturaDurationType durationType;

    public KalturaPlayableEntry() {
    }

    public KalturaPlayableEntry(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("plays")) {
                try {
                    if (!txt.equals("")) this.plays = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("views")) {
                try {
                    if (!txt.equals("")) this.views = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("width")) {
                try {
                    if (!txt.equals("")) this.width = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("height")) {
                try {
                    if (!txt.equals("")) this.height = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("duration")) {
                try {
                    if (!txt.equals("")) this.duration = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("msDuration")) {
                try {
                    if (!txt.equals("")) this.msDuration = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("durationType")) {
                try {
                    if (!txt.equals("")) this.durationType = KalturaDurationType.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaPlayableEntry");
        return kparams;
    }
}

