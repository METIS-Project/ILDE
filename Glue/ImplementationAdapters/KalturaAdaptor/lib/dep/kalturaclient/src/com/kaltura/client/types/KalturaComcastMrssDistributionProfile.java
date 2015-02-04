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

public class KalturaComcastMrssDistributionProfile extends KalturaConfigurableDistributionProfile {
    public int metadataProfileId = Integer.MIN_VALUE;
    public String feedUrl;
    public String feedTitle;
    public String feedLink;
    public String feedDescription;
    public String feedLastBuildDate;
    public String itemLink;
    public ArrayList<KalturaKeyValue> cPlatformTvSeries;
    public String cPlatformTvSeriesField;

    public KalturaComcastMrssDistributionProfile() {
    }

    public KalturaComcastMrssDistributionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("metadataProfileId")) {
                try {
                    if (!txt.equals("")) this.metadataProfileId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("feedUrl")) {
                this.feedUrl = txt;
                continue;
            } else if (nodeName.equals("feedTitle")) {
                this.feedTitle = txt;
                continue;
            } else if (nodeName.equals("feedLink")) {
                this.feedLink = txt;
                continue;
            } else if (nodeName.equals("feedDescription")) {
                this.feedDescription = txt;
                continue;
            } else if (nodeName.equals("feedLastBuildDate")) {
                this.feedLastBuildDate = txt;
                continue;
            } else if (nodeName.equals("itemLink")) {
                this.itemLink = txt;
                continue;
            } else if (nodeName.equals("cPlatformTvSeries")) {
                this.cPlatformTvSeries = new ArrayList<KalturaKeyValue>();
                NodeList subNodeList = aNode.getChildNodes();
                for (int j = 0; j < subNodeList.getLength(); j++) {
                    Node arrayNode = subNodeList.item(j);
                    this.cPlatformTvSeries.add((KalturaKeyValue)KalturaObjectFactory.create((Element)arrayNode));
                }
                continue;
            } else if (nodeName.equals("cPlatformTvSeriesField")) {
                this.cPlatformTvSeriesField = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaComcastMrssDistributionProfile");
        kparams.addIntIfNotNull("metadataProfileId", this.metadataProfileId);
        kparams.addStringIfNotNull("feedTitle", this.feedTitle);
        kparams.addStringIfNotNull("feedLink", this.feedLink);
        kparams.addStringIfNotNull("feedDescription", this.feedDescription);
        kparams.addStringIfNotNull("feedLastBuildDate", this.feedLastBuildDate);
        kparams.addStringIfNotNull("itemLink", this.itemLink);
        kparams.addObjectIfNotNull("cPlatformTvSeries", this.cPlatformTvSeries);
        kparams.addStringIfNotNull("cPlatformTvSeriesField", this.cPlatformTvSeriesField);
        return kparams;
    }
}

