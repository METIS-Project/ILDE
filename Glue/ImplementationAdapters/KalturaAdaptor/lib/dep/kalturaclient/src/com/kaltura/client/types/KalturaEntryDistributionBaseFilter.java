package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaEntryDistributionStatus;
import com.kaltura.client.enums.KalturaEntryDistributionFlag;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaEntryDistributionBaseFilter extends KalturaFilter {
    public int idEqual = Integer.MIN_VALUE;
    public String idIn;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;
    public int submittedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int submittedAtLessThanOrEqual = Integer.MIN_VALUE;
    public String entryIdEqual;
    public String entryIdIn;
    public int distributionProfileIdEqual = Integer.MIN_VALUE;
    public String distributionProfileIdIn;
    public KalturaEntryDistributionStatus statusEqual;
    public String statusIn;
    public KalturaEntryDistributionFlag dirtyStatusEqual;
    public String dirtyStatusIn;
    public int sunriseGreaterThanOrEqual = Integer.MIN_VALUE;
    public int sunriseLessThanOrEqual = Integer.MIN_VALUE;
    public int sunsetGreaterThanOrEqual = Integer.MIN_VALUE;
    public int sunsetLessThanOrEqual = Integer.MIN_VALUE;

    public KalturaEntryDistributionBaseFilter() {
    }

    public KalturaEntryDistributionBaseFilter(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("idIn")) {
                this.idIn = txt;
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
            } else if (nodeName.equals("submittedAtGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.submittedAtGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("submittedAtLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.submittedAtLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("entryIdEqual")) {
                this.entryIdEqual = txt;
                continue;
            } else if (nodeName.equals("entryIdIn")) {
                this.entryIdIn = txt;
                continue;
            } else if (nodeName.equals("distributionProfileIdEqual")) {
                try {
                    if (!txt.equals("")) this.distributionProfileIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("distributionProfileIdIn")) {
                this.distributionProfileIdIn = txt;
                continue;
            } else if (nodeName.equals("statusEqual")) {
                try {
                    if (!txt.equals("")) this.statusEqual = KalturaEntryDistributionStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = txt;
                continue;
            } else if (nodeName.equals("dirtyStatusEqual")) {
                try {
                    if (!txt.equals("")) this.dirtyStatusEqual = KalturaEntryDistributionFlag.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("dirtyStatusIn")) {
                this.dirtyStatusIn = txt;
                continue;
            } else if (nodeName.equals("sunriseGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.sunriseGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("sunriseLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.sunriseLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("sunsetGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.sunsetGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("sunsetLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.sunsetLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaEntryDistributionBaseFilter");
        kparams.addIntIfNotNull("idEqual", this.idEqual);
        kparams.addStringIfNotNull("idIn", this.idIn);
        kparams.addIntIfNotNull("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.addIntIfNotNull("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        kparams.addIntIfNotNull("submittedAtGreaterThanOrEqual", this.submittedAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("submittedAtLessThanOrEqual", this.submittedAtLessThanOrEqual);
        kparams.addStringIfNotNull("entryIdEqual", this.entryIdEqual);
        kparams.addStringIfNotNull("entryIdIn", this.entryIdIn);
        kparams.addIntIfNotNull("distributionProfileIdEqual", this.distributionProfileIdEqual);
        kparams.addStringIfNotNull("distributionProfileIdIn", this.distributionProfileIdIn);
        if (statusEqual != null) kparams.addIntIfNotNull("statusEqual", this.statusEqual.getHashCode());
        kparams.addStringIfNotNull("statusIn", this.statusIn);
        if (dirtyStatusEqual != null) kparams.addIntIfNotNull("dirtyStatusEqual", this.dirtyStatusEqual.getHashCode());
        kparams.addStringIfNotNull("dirtyStatusIn", this.dirtyStatusIn);
        kparams.addIntIfNotNull("sunriseGreaterThanOrEqual", this.sunriseGreaterThanOrEqual);
        kparams.addIntIfNotNull("sunriseLessThanOrEqual", this.sunriseLessThanOrEqual);
        kparams.addIntIfNotNull("sunsetGreaterThanOrEqual", this.sunsetGreaterThanOrEqual);
        kparams.addIntIfNotNull("sunsetLessThanOrEqual", this.sunsetLessThanOrEqual);
        return kparams;
    }
}

