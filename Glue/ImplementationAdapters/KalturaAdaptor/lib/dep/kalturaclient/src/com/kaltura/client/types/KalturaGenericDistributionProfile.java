package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaGenericDistributionProfile extends KalturaDistributionProfile {
    public int genericProviderId = Integer.MIN_VALUE;
    public KalturaGenericDistributionProfileAction submitAction;
    public KalturaGenericDistributionProfileAction updateAction;
    public KalturaGenericDistributionProfileAction deleteAction;
    public KalturaGenericDistributionProfileAction fetchReportAction;
    public String updateRequiredEntryFields;
    public String updateRequiredMetadataXPaths;

    public KalturaGenericDistributionProfile() {
    }

    public KalturaGenericDistributionProfile(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("genericProviderId")) {
                try {
                    if (!txt.equals("")) this.genericProviderId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("submitAction")) {
                this.submitAction = (KalturaGenericDistributionProfileAction)KalturaObjectFactory.create((Element)aNode);
                continue;
            } else if (nodeName.equals("updateAction")) {
                this.updateAction = (KalturaGenericDistributionProfileAction)KalturaObjectFactory.create((Element)aNode);
                continue;
            } else if (nodeName.equals("deleteAction")) {
                this.deleteAction = (KalturaGenericDistributionProfileAction)KalturaObjectFactory.create((Element)aNode);
                continue;
            } else if (nodeName.equals("fetchReportAction")) {
                this.fetchReportAction = (KalturaGenericDistributionProfileAction)KalturaObjectFactory.create((Element)aNode);
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
        kparams.setString("objectType", "KalturaGenericDistributionProfile");
        kparams.addIntIfNotNull("genericProviderId", this.genericProviderId);
        kparams.addObjectIfNotNull("submitAction", this.submitAction);
        kparams.addObjectIfNotNull("updateAction", this.updateAction);
        kparams.addObjectIfNotNull("deleteAction", this.deleteAction);
        kparams.addObjectIfNotNull("fetchReportAction", this.fetchReportAction);
        kparams.addStringIfNotNull("updateRequiredEntryFields", this.updateRequiredEntryFields);
        kparams.addStringIfNotNull("updateRequiredMetadataXPaths", this.updateRequiredMetadataXPaths);
        return kparams;
    }
}

