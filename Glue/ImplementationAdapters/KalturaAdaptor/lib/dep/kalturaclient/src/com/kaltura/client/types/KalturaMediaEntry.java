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
import com.kaltura.client.enums.KalturaSourceType;
import com.kaltura.client.enums.KalturaSearchProviderType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaMediaEntry extends KalturaPlayableEntry {
    public KalturaMediaType mediaType;
    public String conversionQuality;
    public KalturaSourceType sourceType;
    public KalturaSearchProviderType searchProviderType;
    public String searchProviderId;
    public String creditUserName;
    public String creditUrl;
    public int mediaDate = Integer.MIN_VALUE;
    public String dataUrl;
    public String flavorParamsIds;

    public KalturaMediaEntry() {
    }

    public KalturaMediaEntry(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("mediaType")) {
                try {
                    if (!txt.equals("")) this.mediaType = KalturaMediaType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("conversionQuality")) {
                this.conversionQuality = txt;
                continue;
            } else if (nodeName.equals("sourceType")) {
                try {
                    if (!txt.equals("")) this.sourceType = KalturaSourceType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("searchProviderType")) {
                try {
                    if (!txt.equals("")) this.searchProviderType = KalturaSearchProviderType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("searchProviderId")) {
                this.searchProviderId = txt;
                continue;
            } else if (nodeName.equals("creditUserName")) {
                this.creditUserName = txt;
                continue;
            } else if (nodeName.equals("creditUrl")) {
                this.creditUrl = txt;
                continue;
            } else if (nodeName.equals("mediaDate")) {
                try {
                    if (!txt.equals("")) this.mediaDate = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("dataUrl")) {
                this.dataUrl = txt;
                continue;
            } else if (nodeName.equals("flavorParamsIds")) {
                this.flavorParamsIds = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaMediaEntry");
        if (mediaType != null) kparams.addIntIfNotNull("mediaType", this.mediaType.getHashCode());
        kparams.addStringIfNotNull("conversionQuality", this.conversionQuality);
        if (sourceType != null) kparams.addIntIfNotNull("sourceType", this.sourceType.getHashCode());
        if (searchProviderType != null) kparams.addIntIfNotNull("searchProviderType", this.searchProviderType.getHashCode());
        kparams.addStringIfNotNull("searchProviderId", this.searchProviderId);
        kparams.addStringIfNotNull("creditUserName", this.creditUserName);
        kparams.addStringIfNotNull("creditUrl", this.creditUrl);
        return kparams;
    }
}

