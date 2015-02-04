package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaEditorType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaMixEntry extends KalturaPlayableEntry {
    public boolean hasRealThumbnail;
    public KalturaEditorType editorType;
    public String dataContent;

    public KalturaMixEntry() {
    }

    public KalturaMixEntry(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("hasRealThumbnail")) {
                if (!txt.equals("")) this.hasRealThumbnail = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("editorType")) {
                try {
                    if (!txt.equals("")) this.editorType = KalturaEditorType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("dataContent")) {
                this.dataContent = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaMixEntry");
        if (editorType != null) kparams.addIntIfNotNull("editorType", this.editorType.getHashCode());
        kparams.addStringIfNotNull("dataContent", this.dataContent);
        return kparams;
    }
}

