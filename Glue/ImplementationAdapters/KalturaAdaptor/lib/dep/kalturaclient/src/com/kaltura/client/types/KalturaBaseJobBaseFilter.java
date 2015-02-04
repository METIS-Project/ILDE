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

public abstract class KalturaBaseJobBaseFilter extends KalturaFilter {
    public int idEqual = Integer.MIN_VALUE;
    public int idGreaterThanOrEqual = Integer.MIN_VALUE;
    public int partnerIdEqual = Integer.MIN_VALUE;
    public String partnerIdIn;
    public String partnerIdNotIn;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;
    public int processorExpirationGreaterThanOrEqual = Integer.MIN_VALUE;
    public int processorExpirationLessThanOrEqual = Integer.MIN_VALUE;
    public int executionAttemptsGreaterThanOrEqual = Integer.MIN_VALUE;
    public int executionAttemptsLessThanOrEqual = Integer.MIN_VALUE;
    public int lockVersionGreaterThanOrEqual = Integer.MIN_VALUE;
    public int lockVersionLessThanOrEqual = Integer.MIN_VALUE;

    public KalturaBaseJobBaseFilter() {
    }

    public KalturaBaseJobBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("idEqual")) {
                try {
                    if (!txt.equals("")) this.idEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("idGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.idGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerIdEqual")) {
                try {
                    if (!txt.equals("")) this.partnerIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerIdIn")) {
                this.partnerIdIn = txt;
                continue;
            } else if (nodeName.equals("partnerIdNotIn")) {
                this.partnerIdNotIn = txt;
                continue;
            } else if (nodeName.equals("createdAtGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.createdAtGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("createdAtLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.createdAtLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("updatedAtGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.updatedAtGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("updatedAtLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.updatedAtLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("processorExpirationGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.processorExpirationGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("processorExpirationLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.processorExpirationLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("executionAttemptsGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.executionAttemptsGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("executionAttemptsLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.executionAttemptsLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("lockVersionGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.lockVersionGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("lockVersionLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.lockVersionLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaBaseJobBaseFilter");
        kparams.addIntIfNotNull("idEqual", this.idEqual);
        kparams.addIntIfNotNull("idGreaterThanOrEqual", this.idGreaterThanOrEqual);
        kparams.addIntIfNotNull("partnerIdEqual", this.partnerIdEqual);
        kparams.addStringIfNotNull("partnerIdIn", this.partnerIdIn);
        kparams.addStringIfNotNull("partnerIdNotIn", this.partnerIdNotIn);
        kparams.addIntIfNotNull("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.addIntIfNotNull("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        kparams.addIntIfNotNull("processorExpirationGreaterThanOrEqual", this.processorExpirationGreaterThanOrEqual);
        kparams.addIntIfNotNull("processorExpirationLessThanOrEqual", this.processorExpirationLessThanOrEqual);
        kparams.addIntIfNotNull("executionAttemptsGreaterThanOrEqual", this.executionAttemptsGreaterThanOrEqual);
        kparams.addIntIfNotNull("executionAttemptsLessThanOrEqual", this.executionAttemptsLessThanOrEqual);
        kparams.addIntIfNotNull("lockVersionGreaterThanOrEqual", this.lockVersionGreaterThanOrEqual);
        kparams.addIntIfNotNull("lockVersionLessThanOrEqual", this.lockVersionLessThanOrEqual);
        return kparams;
    }
}

