package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaGenericDistributionProviderStatus;
import java.util.ArrayList;
import com.kaltura.client.KalturaObjectFactory;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaGenericDistributionProvider extends KalturaDistributionProvider {
    public int id = Integer.MIN_VALUE;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public boolean isDefault;
    public KalturaGenericDistributionProviderStatus status;
    public String optionalFlavorParamsIds;
    public String requiredFlavorParamsIds;
    public ArrayList<KalturaDistributionThumbDimensions> optionalThumbDimensions;
    public ArrayList<KalturaDistributionThumbDimensions> requiredThumbDimensions;
    public String editableFields;
    public String mandatoryFields;

    public KalturaGenericDistributionProvider() {
    }

    public KalturaGenericDistributionProvider(Element node) throws KalturaApiException {
        super(node);
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
            } else if (nodeName.equals("isDefault")) {
                if (!txt.equals("")) this.isDefault = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaGenericDistributionProviderStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
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
            } else if (nodeName.equals("editableFields")) {
                this.editableFields = txt;
                continue;
            } else if (nodeName.equals("mandatoryFields")) {
                this.mandatoryFields = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaGenericDistributionProvider");
        kparams.addBoolIfNotNull("isDefault", this.isDefault);
        kparams.addStringIfNotNull("optionalFlavorParamsIds", this.optionalFlavorParamsIds);
        kparams.addStringIfNotNull("requiredFlavorParamsIds", this.requiredFlavorParamsIds);
        kparams.addObjectIfNotNull("optionalThumbDimensions", this.optionalThumbDimensions);
        kparams.addObjectIfNotNull("requiredThumbDimensions", this.requiredThumbDimensions);
        kparams.addStringIfNotNull("editableFields", this.editableFields);
        kparams.addStringIfNotNull("mandatoryFields", this.mandatoryFields);
        return kparams;
    }
}

