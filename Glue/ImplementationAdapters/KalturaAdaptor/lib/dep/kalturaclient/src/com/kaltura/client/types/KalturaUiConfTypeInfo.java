package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaUiConfObjType;
import java.util.ArrayList;
import com.kaltura.client.KalturaObjectFactory;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaUiConfTypeInfo extends KalturaObjectBase {
    public KalturaUiConfObjType type;
    public ArrayList<KalturaString> versions;
    public String directory;
    public String filename;

    public KalturaUiConfTypeInfo() {
    }

    public KalturaUiConfTypeInfo(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("type")) {
                try {
                    if (!txt.equals("")) this.type = KalturaUiConfObjType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("versions")) {
                this.versions = new ArrayList<KalturaString>();
                NodeList subNodeList = aNode.getChildNodes();
                for (int j = 0; j < subNodeList.getLength(); j++) {
                    Node arrayNode = subNodeList.item(j);
                    this.versions.add((KalturaString)KalturaObjectFactory.create((Element)arrayNode));
                }
                continue;
            } else if (nodeName.equals("directory")) {
                this.directory = txt;
                continue;
            } else if (nodeName.equals("filename")) {
                this.filename = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaUiConfTypeInfo");
        if (type != null) kparams.addIntIfNotNull("type", this.type.getHashCode());
        kparams.addObjectIfNotNull("versions", this.versions);
        kparams.addStringIfNotNull("directory", this.directory);
        kparams.addStringIfNotNull("filename", this.filename);
        return kparams;
    }
}

