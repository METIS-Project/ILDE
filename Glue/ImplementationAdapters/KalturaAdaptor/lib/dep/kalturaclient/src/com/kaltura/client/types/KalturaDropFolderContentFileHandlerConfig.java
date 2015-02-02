package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaDropFolderContentFileHandlerMatchPolicy;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaDropFolderContentFileHandlerConfig extends KalturaDropFolderFileHandlerConfig {
    public KalturaDropFolderContentFileHandlerMatchPolicy contentMatchPolicy;
    public String slugRegex;

    public KalturaDropFolderContentFileHandlerConfig() {
    }

    public KalturaDropFolderContentFileHandlerConfig(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("contentMatchPolicy")) {
                try {
                    if (!txt.equals("")) this.contentMatchPolicy = KalturaDropFolderContentFileHandlerMatchPolicy.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("slugRegex")) {
                this.slugRegex = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaDropFolderContentFileHandlerConfig");
        if (contentMatchPolicy != null) kparams.addIntIfNotNull("contentMatchPolicy", this.contentMatchPolicy.getHashCode());
        kparams.addStringIfNotNull("slugRegex", this.slugRegex);
        return kparams;
    }
}

