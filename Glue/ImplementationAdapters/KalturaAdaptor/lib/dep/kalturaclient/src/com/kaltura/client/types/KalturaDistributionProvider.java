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


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaDistributionProvider extends KalturaObjectBase {
    public KalturaDistributionProviderType type;
    public String name;
    public boolean scheduleUpdateEnabled;
    public boolean availabilityUpdateEnabled;
    public boolean deleteInsteadUpdate;
    public int intervalBeforeSunrise = Integer.MIN_VALUE;
    public int intervalBeforeSunset = Integer.MIN_VALUE;
    public String updateRequiredEntryFields;
    public String updateRequiredMetadataXPaths;

    public KalturaDistributionProvider() {
    }

    public KalturaDistributionProvider(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("type")) {
                try {
                    if (!txt.equals("")) this.type = KalturaDistributionProviderType.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("name")) {
                this.name = txt;
                continue;
            } else if (nodeName.equals("scheduleUpdateEnabled")) {
                if (!txt.equals("")) this.scheduleUpdateEnabled = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("availabilityUpdateEnabled")) {
                if (!txt.equals("")) this.availabilityUpdateEnabled = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("deleteInsteadUpdate")) {
                if (!txt.equals("")) this.deleteInsteadUpdate = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("intervalBeforeSunrise")) {
                try {
                    if (!txt.equals("")) this.intervalBeforeSunrise = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("intervalBeforeSunset")) {
                try {
                    if (!txt.equals("")) this.intervalBeforeSunset = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("updateRequiredEntryFields")) {
                this.updateRequiredEntryFields = txt;
                continue;
            } else if (nodeName.equals("updateRequiredMetadataXPaths")) {
                this.updateRequiredMetadataXPaths = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaDistributionProvider");
        kparams.addStringIfNotNull("name", this.name);
        kparams.addBoolIfNotNull("scheduleUpdateEnabled", this.scheduleUpdateEnabled);
        kparams.addBoolIfNotNull("availabilityUpdateEnabled", this.availabilityUpdateEnabled);
        kparams.addBoolIfNotNull("deleteInsteadUpdate", this.deleteInsteadUpdate);
        kparams.addIntIfNotNull("intervalBeforeSunrise", this.intervalBeforeSunrise);
        kparams.addIntIfNotNull("intervalBeforeSunset", this.intervalBeforeSunset);
        kparams.addStringIfNotNull("updateRequiredEntryFields", this.updateRequiredEntryFields);
        kparams.addStringIfNotNull("updateRequiredMetadataXPaths", this.updateRequiredMetadataXPaths);
        return kparams;
    }
}

