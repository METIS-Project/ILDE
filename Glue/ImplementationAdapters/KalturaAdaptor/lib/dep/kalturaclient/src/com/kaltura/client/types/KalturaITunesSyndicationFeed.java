package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaITunesSyndicationFeedCategories;
import com.kaltura.client.enums.KalturaITunesSyndicationFeedAdultValues;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaITunesSyndicationFeed extends KalturaBaseSyndicationFeed {
    public String feedDescription;
    public String language;
    public String feedLandingPage;
    public String ownerName;
    public String ownerEmail;
    public String feedImageUrl;
    public KalturaITunesSyndicationFeedCategories category;
    public KalturaITunesSyndicationFeedAdultValues adultContent;
    public String feedAuthor;

    public KalturaITunesSyndicationFeed() {
    }

    public KalturaITunesSyndicationFeed(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("feedDescription")) {
                this.feedDescription = txt;
                continue;
            } else if (nodeName.equals("language")) {
                this.language = txt;
                continue;
            } else if (nodeName.equals("feedLandingPage")) {
                this.feedLandingPage = txt;
                continue;
            } else if (nodeName.equals("ownerName")) {
                this.ownerName = txt;
                continue;
            } else if (nodeName.equals("ownerEmail")) {
                this.ownerEmail = txt;
                continue;
            } else if (nodeName.equals("feedImageUrl")) {
                this.feedImageUrl = txt;
                continue;
            } else if (nodeName.equals("category")) {
                try {
                    if (!txt.equals("")) this.category = KalturaITunesSyndicationFeedCategories.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("adultContent")) {
                try {
                    if (!txt.equals("")) this.adultContent = KalturaITunesSyndicationFeedAdultValues.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("feedAuthor")) {
                this.feedAuthor = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaITunesSyndicationFeed");
        kparams.addStringIfNotNull("feedDescription", this.feedDescription);
        kparams.addStringIfNotNull("language", this.language);
        kparams.addStringIfNotNull("feedLandingPage", this.feedLandingPage);
        kparams.addStringIfNotNull("ownerName", this.ownerName);
        kparams.addStringIfNotNull("ownerEmail", this.ownerEmail);
        kparams.addStringIfNotNull("feedImageUrl", this.feedImageUrl);
        if (adultContent != null) kparams.addStringIfNotNull("adultContent", this.adultContent.getHashCode());
        kparams.addStringIfNotNull("feedAuthor", this.feedAuthor);
        return kparams;
    }
}

