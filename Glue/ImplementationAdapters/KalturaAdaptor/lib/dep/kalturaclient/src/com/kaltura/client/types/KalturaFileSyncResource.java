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

public class KalturaFileSyncResource extends KalturaContentResource {
    public int fileSyncObjectType = Integer.MIN_VALUE;
    public int objectSubType = Integer.MIN_VALUE;
    public String objectId;
    public String version;

    public KalturaFileSyncResource() {
    }

    public KalturaFileSyncResource(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("fileSyncObjectType")) {
                try {
                    if (!txt.equals("")) this.fileSyncObjectType = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("objectSubType")) {
                try {
                    if (!txt.equals("")) this.objectSubType = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("objectId")) {
                this.objectId = txt;
                continue;
            } else if (nodeName.equals("version")) {
                this.version = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaFileSyncResource");
        kparams.addIntIfNotNull("fileSyncObjectType", this.fileSyncObjectType);
        kparams.addIntIfNotNull("objectSubType", this.objectSubType);
        kparams.addStringIfNotNull("objectId", this.objectId);
        kparams.addStringIfNotNull("version", this.version);
        return kparams;
    }
}

