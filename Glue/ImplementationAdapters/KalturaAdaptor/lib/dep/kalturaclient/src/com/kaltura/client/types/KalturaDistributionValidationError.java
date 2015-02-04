package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaDistributionAction;
import com.kaltura.client.enums.KalturaDistributionErrorType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaDistributionValidationError extends KalturaObjectBase {
    public KalturaDistributionAction action;
    public KalturaDistributionErrorType errorType;
    public String description;

    public KalturaDistributionValidationError() {
    }

    public KalturaDistributionValidationError(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("action")) {
                try {
                    if (!txt.equals("")) this.action = KalturaDistributionAction.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("errorType")) {
                try {
                    if (!txt.equals("")) this.errorType = KalturaDistributionErrorType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("description")) {
                this.description = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaDistributionValidationError");
        if (action != null) kparams.addIntIfNotNull("action", this.action.getHashCode());
        if (errorType != null) kparams.addIntIfNotNull("errorType", this.errorType.getHashCode());
        kparams.addStringIfNotNull("description", this.description);
        return kparams;
    }
}

