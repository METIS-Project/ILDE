package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaDistributionValidationErrorType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaDistributionValidationErrorInvalidData extends KalturaDistributionValidationError {
    public String fieldName;
    public KalturaDistributionValidationErrorType validationErrorType;
    public String validationErrorParam;

    public KalturaDistributionValidationErrorInvalidData() {
    }

    public KalturaDistributionValidationErrorInvalidData(Element node) throws KalturaApiException {
        super(node);
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
            } else if (nodeName.equals("validationErrorType")) {
                try {
                    if (!txt.equals("")) this.validationErrorType = KalturaDistributionValidationErrorType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("validationErrorParam")) {
                this.validationErrorParam = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaDistributionValidationErrorInvalidData");
        kparams.addStringIfNotNull("fieldName", this.fieldName);
        if (validationErrorType != null) kparams.addIntIfNotNull("validationErrorType", this.validationErrorType.getHashCode());
        kparams.addStringIfNotNull("validationErrorParam", this.validationErrorParam);
        return kparams;
    }
}

