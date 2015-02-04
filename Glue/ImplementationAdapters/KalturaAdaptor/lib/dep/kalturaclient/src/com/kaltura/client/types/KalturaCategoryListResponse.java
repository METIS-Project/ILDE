package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import java.util.ArrayList;
import com.kaltura.client.KalturaObjectFactory;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaCategoryListResponse extends KalturaObjectBase {
    public ArrayList<KalturaCategory> objects;
    public int totalCount = Integer.MIN_VALUE;

    public KalturaCategoryListResponse() {
    }

    public KalturaCategoryListResponse(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("objects")) {
                this.objects = new ArrayList<KalturaCategory>();
                NodeList subNodeList = aNode.getChildNodes();
                for (int j = 0; j < subNodeList.getLength(); j++) {
                    Node arrayNode = subNodeList.item(j);
                    this.objects.add((KalturaCategory)KalturaObjectFactory.create((Element)arrayNode));
                }
                continue;
            } else if (nodeName.equals("totalCount")) {
                try {
                    if (!txt.equals("")) this.totalCount = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaCategoryListResponse");
        return kparams;
    }
}

