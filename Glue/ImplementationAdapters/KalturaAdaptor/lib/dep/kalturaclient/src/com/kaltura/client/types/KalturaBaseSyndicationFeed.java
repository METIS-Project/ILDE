package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaSyndicationFeedStatus;
import com.kaltura.client.enums.KalturaSyndicationFeedType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaBaseSyndicationFeed extends KalturaObjectBase {
    public String id;
    public String feedUrl;
    public int partnerId = Integer.MIN_VALUE;
    public String playlistId;
    public String name;
    public KalturaSyndicationFeedStatus status;
    public KalturaSyndicationFeedType type;
    public String landingPage;
    public int createdAt = Integer.MIN_VALUE;
    public boolean allowEmbed;
    public int playerUiconfId = Integer.MIN_VALUE;
    public int flavorParamId = Integer.MIN_VALUE;
    public boolean transcodeExistingContent;
    public boolean addToDefaultConversionProfile;
    public String categories;

    public KalturaBaseSyndicationFeed() {
    }

    public KalturaBaseSyndicationFeed(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("feedUrl")) {
                this.feedUrl = txt;
                continue;
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("playlistId")) {
                this.playlistId = txt;
                continue;
            } else if (nodeName.equals("name")) {
                this.name = txt;
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaSyndicationFeedStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("type")) {
                try {
                    if (!txt.equals("")) this.type = KalturaSyndicationFeedType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("landingPage")) {
                this.landingPage = txt;
                continue;
            } else if (nodeName.equals("createdAt")) {
                try {
                    if (!txt.equals("")) this.createdAt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("allowEmbed")) {
                if (!txt.equals("")) this.allowEmbed = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("playerUiconfId")) {
                try {
                    if (!txt.equals("")) this.playerUiconfId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("flavorParamId")) {
                try {
                    if (!txt.equals("")) this.flavorParamId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("transcodeExistingContent")) {
                if (!txt.equals("")) this.transcodeExistingContent = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("addToDefaultConversionProfile")) {
                if (!txt.equals("")) this.addToDefaultConversionProfile = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("categories")) {
                this.categories = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaBaseSyndicationFeed");
        kparams.addStringIfNotNull("playlistId", this.playlistId);
        kparams.addStringIfNotNull("name", this.name);
        if (type != null) kparams.addIntIfNotNull("type", this.type.getHashCode());
        kparams.addStringIfNotNull("landingPage", this.landingPage);
        kparams.addBoolIfNotNull("allowEmbed", this.allowEmbed);
        kparams.addIntIfNotNull("playerUiconfId", this.playerUiconfId);
        kparams.addIntIfNotNull("flavorParamId", this.flavorParamId);
        kparams.addBoolIfNotNull("transcodeExistingContent", this.transcodeExistingContent);
        kparams.addBoolIfNotNull("addToDefaultConversionProfile", this.addToDefaultConversionProfile);
        kparams.addStringIfNotNull("categories", this.categories);
        return kparams;
    }
}

