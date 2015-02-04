package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaConversionProfileStatus;
import com.kaltura.client.enums.KalturaNullableBoolean;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaConversionProfile extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public KalturaConversionProfileStatus status;
    public String name;
    public String systemName;
    public String tags;
    public String description;
    public String defaultEntryId;
    public int createdAt = Integer.MIN_VALUE;
    public String flavorParamsIds;
    public KalturaNullableBoolean isDefault;
    public boolean isPartnerDefault;
    public KalturaCropDimensions cropDimensions;
    public int clipStart = Integer.MIN_VALUE;
    public int clipDuration = Integer.MIN_VALUE;
    public String xslTransformation;
    public int storageProfileId = Integer.MIN_VALUE;

    public KalturaConversionProfile() {
    }

    public KalturaConversionProfile(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaConversionProfileStatus.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("name")) {
                this.name = txt;
                continue;
            } else if (nodeName.equals("systemName")) {
                this.systemName = txt;
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = txt;
                continue;
            } else if (nodeName.equals("description")) {
                this.description = txt;
                continue;
            } else if (nodeName.equals("defaultEntryId")) {
                this.defaultEntryId = txt;
                continue;
            } else if (nodeName.equals("createdAt")) {
                try {
                    if (!txt.equals("")) this.createdAt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("flavorParamsIds")) {
                this.flavorParamsIds = txt;
                continue;
            } else if (nodeName.equals("isDefault")) {
                try {
                    if (!txt.equals("")) this.isDefault = KalturaNullableBoolean.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("isPartnerDefault")) {
                if (!txt.equals("")) this.isPartnerDefault = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("cropDimensions")) {
                this.cropDimensions = (KalturaCropDimensions)KalturaObjectFactory.create((Element)aNode);
                continue;
            } else if (nodeName.equals("clipStart")) {
                try {
                    if (!txt.equals("")) this.clipStart = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("clipDuration")) {
                try {
                    if (!txt.equals("")) this.clipDuration = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("xslTransformation")) {
                this.xslTransformation = txt;
                continue;
            } else if (nodeName.equals("storageProfileId")) {
                try {
                    if (!txt.equals("")) this.storageProfileId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaConversionProfile");
        if (status != null) kparams.addStringIfNotNull("status", this.status.getHashCode());
        kparams.addStringIfNotNull("name", this.name);
        kparams.addStringIfNotNull("systemName", this.systemName);
        kparams.addStringIfNotNull("tags", this.tags);
        kparams.addStringIfNotNull("description", this.description);
        kparams.addStringIfNotNull("defaultEntryId", this.defaultEntryId);
        kparams.addStringIfNotNull("flavorParamsIds", this.flavorParamsIds);
        if (isDefault != null) kparams.addIntIfNotNull("isDefault", this.isDefault.getHashCode());
        kparams.addObjectIfNotNull("cropDimensions", this.cropDimensions);
        kparams.addIntIfNotNull("clipStart", this.clipStart);
        kparams.addIntIfNotNull("clipDuration", this.clipDuration);
        kparams.addStringIfNotNull("xslTransformation", this.xslTransformation);
        kparams.addIntIfNotNull("storageProfileId", this.storageProfileId);
        return kparams;
    }
}

