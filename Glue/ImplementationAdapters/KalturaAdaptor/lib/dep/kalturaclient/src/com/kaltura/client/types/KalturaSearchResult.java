package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaLicenseType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaSearchResult extends KalturaSearch {
    public String id;
    public String title;
    public String thumbUrl;
    public String description;
    public String tags;
    public String url;
    public String sourceLink;
    public String credit;
    public KalturaLicenseType licenseType;
    public String flashPlaybackType;

    public KalturaSearchResult() {
    }

    public KalturaSearchResult(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("id")) {
                this.id = txt;
                continue;
            } else if (nodeName.equals("title")) {
                this.title = txt;
                continue;
            } else if (nodeName.equals("thumbUrl")) {
                this.thumbUrl = txt;
                continue;
            } else if (nodeName.equals("description")) {
                this.description = txt;
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = txt;
                continue;
            } else if (nodeName.equals("url")) {
                this.url = txt;
                continue;
            } else if (nodeName.equals("sourceLink")) {
                this.sourceLink = txt;
                continue;
            } else if (nodeName.equals("credit")) {
                this.credit = txt;
                continue;
            } else if (nodeName.equals("licenseType")) {
                try {
                    if (!txt.equals("")) this.licenseType = KalturaLicenseType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("flashPlaybackType")) {
                this.flashPlaybackType = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaSearchResult");
        kparams.addStringIfNotNull("id", this.id);
        kparams.addStringIfNotNull("title", this.title);
        kparams.addStringIfNotNull("thumbUrl", this.thumbUrl);
        kparams.addStringIfNotNull("description", this.description);
        kparams.addStringIfNotNull("tags", this.tags);
        kparams.addStringIfNotNull("url", this.url);
        kparams.addStringIfNotNull("sourceLink", this.sourceLink);
        kparams.addStringIfNotNull("credit", this.credit);
        if (licenseType != null) kparams.addIntIfNotNull("licenseType", this.licenseType.getHashCode());
        kparams.addStringIfNotNull("flashPlaybackType", this.flashPlaybackType);
        return kparams;
    }
}

