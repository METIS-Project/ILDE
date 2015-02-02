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
import com.kaltura.client.enums.KalturaGenericDistributionProviderStatus;
import com.kaltura.client.enums.KalturaGenericDistributionProviderParser;
import com.kaltura.client.enums.KalturaDistributionProtocol;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaGenericDistributionProviderAction extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
    public int genericDistributionProviderId = Integer.MIN_VALUE;
    public KalturaDistributionAction action;
    public KalturaGenericDistributionProviderStatus status;
    public KalturaGenericDistributionProviderParser resultsParser;
    public KalturaDistributionProtocol protocol;
    public String serverAddress;
    public String remotePath;
    public String remoteUsername;
    public String remotePassword;
    public String editableFields;
    public String mandatoryFields;
    public String mrssTransformer;
    public String mrssValidator;
    public String resultsTransformer;

    public KalturaGenericDistributionProviderAction() {
    }

    public KalturaGenericDistributionProviderAction(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("genericDistributionProviderId")) {
                try {
                    if (!txt.equals("")) this.genericDistributionProviderId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("action")) {
                try {
                    if (!txt.equals("")) this.action = KalturaDistributionAction.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaGenericDistributionProviderStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("resultsParser")) {
                try {
                    if (!txt.equals("")) this.resultsParser = KalturaGenericDistributionProviderParser.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("protocol")) {
                try {
                    if (!txt.equals("")) this.protocol = KalturaDistributionProtocol.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("serverAddress")) {
                this.serverAddress = txt;
                continue;
            } else if (nodeName.equals("remotePath")) {
                this.remotePath = txt;
                continue;
            } else if (nodeName.equals("remoteUsername")) {
                this.remoteUsername = txt;
                continue;
            } else if (nodeName.equals("remotePassword")) {
                this.remotePassword = txt;
                continue;
            } else if (nodeName.equals("editableFields")) {
                this.editableFields = txt;
                continue;
            } else if (nodeName.equals("mandatoryFields")) {
                this.mandatoryFields = txt;
                continue;
            } else if (nodeName.equals("mrssTransformer")) {
                this.mrssTransformer = txt;
                continue;
            } else if (nodeName.equals("mrssValidator")) {
                this.mrssValidator = txt;
                continue;
            } else if (nodeName.equals("resultsTransformer")) {
                this.resultsTransformer = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaGenericDistributionProviderAction");
        kparams.addIntIfNotNull("genericDistributionProviderId", this.genericDistributionProviderId);
        if (action != null) kparams.addIntIfNotNull("action", this.action.getHashCode());
        if (resultsParser != null) kparams.addIntIfNotNull("resultsParser", this.resultsParser.getHashCode());
        if (protocol != null) kparams.addIntIfNotNull("protocol", this.protocol.getHashCode());
        kparams.addStringIfNotNull("serverAddress", this.serverAddress);
        kparams.addStringIfNotNull("remotePath", this.remotePath);
        kparams.addStringIfNotNull("remoteUsername", this.remoteUsername);
        kparams.addStringIfNotNull("remotePassword", this.remotePassword);
        kparams.addStringIfNotNull("editableFields", this.editableFields);
        kparams.addStringIfNotNull("mandatoryFields", this.mandatoryFields);
        return kparams;
    }
}

