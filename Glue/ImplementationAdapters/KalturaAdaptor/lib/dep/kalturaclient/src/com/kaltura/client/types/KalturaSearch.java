package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaSearchProviderType;
import com.kaltura.client.enums.KalturaMediaType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaSearch extends KalturaObjectBase {
    public String keyWords;
    public KalturaSearchProviderType searchSource;
    public KalturaMediaType mediaType;
    public String extraData;
    public String authData;

    public KalturaSearch() {
    }

    public KalturaSearch(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("keyWords")) {
                this.keyWords = txt;
                continue;
            } else if (nodeName.equals("searchSource")) {
                try {
                    if (!txt.equals("")) this.searchSource = KalturaSearchProviderType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("mediaType")) {
                try {
                    if (!txt.equals("")) this.mediaType = KalturaMediaType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("extraData")) {
                this.extraData = txt;
                continue;
            } else if (nodeName.equals("authData")) {
                this.authData = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaSearch");
        kparams.addStringIfNotNull("keyWords", this.keyWords);
        if (searchSource != null) kparams.addIntIfNotNull("searchSource", this.searchSource.getHashCode());
        if (mediaType != null) kparams.addIntIfNotNull("mediaType", this.mediaType.getHashCode());
        kparams.addStringIfNotNull("extraData", this.extraData);
        kparams.addStringIfNotNull("authData", this.authData);
        return kparams;
    }
}

