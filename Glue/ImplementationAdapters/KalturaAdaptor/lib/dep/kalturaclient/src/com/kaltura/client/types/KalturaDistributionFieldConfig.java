package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaDistributionFieldRequiredStatus;
import java.util.ArrayList;
import com.kaltura.client.KalturaObjectFactory;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaDistributionFieldConfig extends KalturaObjectBase {
    public String fieldName;
    public String userFriendlyFieldName;
    public String entryMrssXslt;
    public KalturaDistributionFieldRequiredStatus isRequired;
    public boolean updateOnChange;
    public ArrayList<KalturaString> updateParams;
    public boolean isDefault;

    public KalturaDistributionFieldConfig() {
    }

    public KalturaDistributionFieldConfig(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("fieldName")) {
                this.fieldName = txt;
                continue;
            } else if (nodeName.equals("userFriendlyFieldName")) {
                this.userFriendlyFieldName = txt;
                continue;
            } else if (nodeName.equals("entryMrssXslt")) {
                this.entryMrssXslt = txt;
                continue;
            } else if (nodeName.equals("isRequired")) {
                try {
                    if (!txt.equals("")) this.isRequired = KalturaDistributionFieldRequiredStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("updateOnChange")) {
                if (!txt.equals("")) this.updateOnChange = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("updateParams")) {
                this.updateParams = new ArrayList<KalturaString>();
                NodeList subNodeList = aNode.getChildNodes();
                for (int j = 0; j < subNodeList.getLength(); j++) {
                    Node arrayNode = subNodeList.item(j);
                    this.updateParams.add((KalturaString)KalturaObjectFactory.create((Element)arrayNode));
                }
                continue;
            } else if (nodeName.equals("isDefault")) {
                if (!txt.equals("")) this.isDefault = ((txt.equals("0")) ? false : true);
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaDistributionFieldConfig");
        kparams.addStringIfNotNull("fieldName", this.fieldName);
        kparams.addStringIfNotNull("userFriendlyFieldName", this.userFriendlyFieldName);
        kparams.addStringIfNotNull("entryMrssXslt", this.entryMrssXslt);
        if (isRequired != null) kparams.addIntIfNotNull("isRequired", this.isRequired.getHashCode());
        kparams.addBoolIfNotNull("updateOnChange", this.updateOnChange);
        kparams.addObjectIfNotNull("updateParams", this.updateParams);
        return kparams;
    }
}

