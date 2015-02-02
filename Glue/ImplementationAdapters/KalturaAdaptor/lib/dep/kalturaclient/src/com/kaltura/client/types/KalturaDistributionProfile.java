package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaDistributionProviderType;
import com.kaltura.client.enums.KalturaDistributionProfileStatus;
import com.kaltura.client.enums.KalturaDistributionProfileActionStatus;
import com.kaltura.client.enums.KalturaDistributionProfileActionStatus;
import com.kaltura.client.enums.KalturaDistributionProfileActionStatus;
import com.kaltura.client.enums.KalturaDistributionProfileActionStatus;
import java.util.ArrayList;
import com.kaltura.client.KalturaObjectFactory;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaDistributionProfile extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public KalturaDistributionProviderType providerType;
    public String name;
    public KalturaDistributionProfileStatus status;
    public KalturaDistributionProfileActionStatus submitEnabled;
    public KalturaDistributionProfileActionStatus updateEnabled;
    public KalturaDistributionProfileActionStatus deleteEnabled;
    public KalturaDistributionProfileActionStatus reportEnabled;
    public String autoCreateFlavors;
    public String autoCreateThumb;
    public String optionalFlavorParamsIds;
    public String requiredFlavorParamsIds;
    public ArrayList<KalturaDistributionThumbDimensions> optionalThumbDimensions;
    public ArrayList<KalturaDistributionThumbDimensions> requiredThumbDimensions;
    public int sunriseDefaultOffset = Integer.MIN_VALUE;
    public int sunsetDefaultOffset = Integer.MIN_VALUE;

    public KalturaDistributionProfile() {
    }

    public KalturaDistributionProfile(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("id")) {
                try {
                    if (!txt.equals("")) this.id = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("createdAt")) {
                try {
                    if (!txt.equals("")) this.createdAt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("updatedAt")) {
                try {
                    if (!txt.equals("")) this.updatedAt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("providerType")) {
                try {
                    if (!txt.equals("")) this.providerType = KalturaDistributionProviderType.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("name")) {
                this.name = txt;
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaDistributionProfileStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("submitEnabled")) {
                try {
                    if (!txt.equals("")) this.submitEnabled = KalturaDistributionProfileActionStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("updateEnabled")) {
                try {
                    if (!txt.equals("")) this.updateEnabled = KalturaDistributionProfileActionStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("deleteEnabled")) {
                try {
                    if (!txt.equals("")) this.deleteEnabled = KalturaDistributionProfileActionStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("reportEnabled")) {
                try {
                    if (!txt.equals("")) this.reportEnabled = KalturaDistributionProfileActionStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("autoCreateFlavors")) {
                this.autoCreateFlavors = txt;
                continue;
            } else if (nodeName.equals("autoCreateThumb")) {
                this.autoCreateThumb = txt;
                continue;
            } else if (nodeName.equals("optionalFlavorParamsIds")) {
                this.optionalFlavorParamsIds = txt;
                continue;
            } else if (nodeName.equals("requiredFlavorParamsIds")) {
                this.requiredFlavorParamsIds = txt;
                continue;
            } else if (nodeName.equals("optionalThumbDimensions")) {
                this.optionalThumbDimensions = new ArrayList<KalturaDistributionThumbDimensions>();
                NodeList subNodeList = aNode.getChildNodes();
                for (int j = 0; j < subNodeList.getLength(); j++) {
                    Node arrayNode = subNodeList.item(j);
                    this.optionalThumbDimensions.add((KalturaDistributionThumbDimensions)KalturaObjectFactory.create((Element)arrayNode));
                }
                continue;
            } else if (nodeName.equals("requiredThumbDimensions")) {
                this.requiredThumbDimensions = new ArrayList<KalturaDistributionThumbDimensions>();
                NodeList subNodeList = aNode.getChildNodes();
                for (int j = 0; j < subNodeList.getLength(); j++) {
                    Node arrayNode = subNodeList.item(j);
                    this.requiredThumbDimensions.add((KalturaDistributionThumbDimensions)KalturaObjectFactory.create((Element)arrayNode));
                }
                continue;
            } else if (nodeName.equals("sunriseDefaultOffset")) {
                try {
                    if (!txt.equals("")) this.sunriseDefaultOffset = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("sunsetDefaultOffset")) {
                try {
                    if (!txt.equals("")) this.sunsetDefaultOffset = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaDistributionProfile");
        if (providerType != null) kparams.addStringIfNotNull("providerType", this.providerType.getHashCode());
        kparams.addStringIfNotNull("name", this.name);
        if (status != null) kparams.addIntIfNotNull("status", this.status.getHashCode());
        if (submitEnabled != null) kparams.addIntIfNotNull("submitEnabled", this.submitEnabled.getHashCode());
        if (updateEnabled != null) kparams.addIntIfNotNull("updateEnabled", this.updateEnabled.getHashCode());
        if (deleteEnabled != null) kparams.addIntIfNotNull("deleteEnabled", this.deleteEnabled.getHashCode());
        if (reportEnabled != null) kparams.addIntIfNotNull("reportEnabled", this.reportEnabled.getHashCode());
        kparams.addStringIfNotNull("autoCreateFlavors", this.autoCreateFlavors);
        kparams.addStringIfNotNull("autoCreateThumb", this.autoCreateThumb);
        kparams.addStringIfNotNull("optionalFlavorParamsIds", this.optionalFlavorParamsIds);
        kparams.addStringIfNotNull("requiredFlavorParamsIds", this.requiredFlavorParamsIds);
        kparams.addObjectIfNotNull("optionalThumbDimensions", this.optionalThumbDimensions);
        kparams.addObjectIfNotNull("requiredThumbDimensions", this.requiredThumbDimensions);
        kparams.addIntIfNotNull("sunriseDefaultOffset", this.sunriseDefaultOffset);
        kparams.addIntIfNotNull("sunsetDefaultOffset", this.sunsetDefaultOffset);
        return kparams;
    }
}

