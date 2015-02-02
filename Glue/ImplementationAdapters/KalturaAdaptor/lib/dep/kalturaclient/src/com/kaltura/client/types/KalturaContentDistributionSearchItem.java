package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaEntryDistributionSunStatus;
import com.kaltura.client.enums.KalturaEntryDistributionFlag;
import com.kaltura.client.enums.KalturaEntryDistributionStatus;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaContentDistributionSearchItem extends KalturaSearchItem {
    public boolean noDistributionProfiles;
    public int distributionProfileId = Integer.MIN_VALUE;
    public KalturaEntryDistributionSunStatus distributionSunStatus;
    public KalturaEntryDistributionFlag entryDistributionFlag;
    public KalturaEntryDistributionStatus entryDistributionStatus;
    public boolean hasEntryDistributionValidationErrors;
    public String entryDistributionValidationErrors;

    public KalturaContentDistributionSearchItem() {
    }

    public KalturaContentDistributionSearchItem(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("noDistributionProfiles")) {
                if (!txt.equals("")) this.noDistributionProfiles = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("distributionProfileId")) {
                try {
                    if (!txt.equals("")) this.distributionProfileId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("distributionSunStatus")) {
                try {
                    if (!txt.equals("")) this.distributionSunStatus = KalturaEntryDistributionSunStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("entryDistributionFlag")) {
                try {
                    if (!txt.equals("")) this.entryDistributionFlag = KalturaEntryDistributionFlag.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("entryDistributionStatus")) {
                try {
                    if (!txt.equals("")) this.entryDistributionStatus = KalturaEntryDistributionStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("hasEntryDistributionValidationErrors")) {
                if (!txt.equals("")) this.hasEntryDistributionValidationErrors = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("entryDistributionValidationErrors")) {
                this.entryDistributionValidationErrors = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaContentDistributionSearchItem");
        kparams.addBoolIfNotNull("noDistributionProfiles", this.noDistributionProfiles);
        kparams.addIntIfNotNull("distributionProfileId", this.distributionProfileId);
        if (distributionSunStatus != null) kparams.addIntIfNotNull("distributionSunStatus", this.distributionSunStatus.getHashCode());
        if (entryDistributionFlag != null) kparams.addIntIfNotNull("entryDistributionFlag", this.entryDistributionFlag.getHashCode());
        if (entryDistributionStatus != null) kparams.addIntIfNotNull("entryDistributionStatus", this.entryDistributionStatus.getHashCode());
        kparams.addBoolIfNotNull("hasEntryDistributionValidationErrors", this.hasEntryDistributionValidationErrors);
        kparams.addStringIfNotNull("entryDistributionValidationErrors", this.entryDistributionValidationErrors);
        return kparams;
    }
}

