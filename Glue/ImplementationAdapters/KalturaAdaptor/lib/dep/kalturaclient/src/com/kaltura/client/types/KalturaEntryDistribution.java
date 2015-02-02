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
import com.kaltura.client.enums.KalturaEntryDistributionSunStatus;
import com.kaltura.client.enums.KalturaEntryDistributionFlag;
import com.kaltura.client.enums.KalturaBatchJobErrorTypes;
import com.kaltura.client.enums.KalturaNullableBoolean;
import com.kaltura.client.enums.KalturaNullableBoolean;
import com.kaltura.client.enums.KalturaNullableBoolean;
import com.kaltura.client.enums.KalturaNullableBoolean;
import com.kaltura.client.enums.KalturaNullableBoolean;
import com.kaltura.client.enums.KalturaNullableBoolean;
import java.util.ArrayList;
import com.kaltura.client.KalturaObjectFactory;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaEntryDistribution extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
    public int submittedAt = Integer.MIN_VALUE;
    public String entryId;
    public int partnerId = Integer.MIN_VALUE;
    public int distributionProfileId = Integer.MIN_VALUE;
    public KalturaEntryDistributionStatus status;
    public KalturaEntryDistributionSunStatus sunStatus;
    public KalturaEntryDistributionFlag dirtyStatus;
    public String thumbAssetIds;
    public String flavorAssetIds;
    public int sunrise = Integer.MIN_VALUE;
    public int sunset = Integer.MIN_VALUE;
    public String remoteId;
    public int plays = Integer.MIN_VALUE;
    public int views = Integer.MIN_VALUE;
    public ArrayList<KalturaDistributionValidationError> validationErrors;
    public KalturaBatchJobErrorTypes errorType;
    public int errorNumber = Integer.MIN_VALUE;
    public String errorDescription;
    public KalturaNullableBoolean hasSubmitResultsLog;
    public KalturaNullableBoolean hasSubmitSentDataLog;
    public KalturaNullableBoolean hasUpdateResultsLog;
    public KalturaNullableBoolean hasUpdateSentDataLog;
    public KalturaNullableBoolean hasDeleteResultsLog;
    public KalturaNullableBoolean hasDeleteSentDataLog;

    public KalturaEntryDistribution() {
    }

    public KalturaEntryDistribution(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("submittedAt")) {
                try {
                    if (!txt.equals("")) this.submittedAt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("entryId")) {
                this.entryId = txt;
                continue;
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("distributionProfileId")) {
                try {
                    if (!txt.equals("")) this.distributionProfileId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaEntryDistributionStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("sunStatus")) {
                try {
                    if (!txt.equals("")) this.sunStatus = KalturaEntryDistributionSunStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("dirtyStatus")) {
                try {
                    if (!txt.equals("")) this.dirtyStatus = KalturaEntryDistributionFlag.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("thumbAssetIds")) {
                this.thumbAssetIds = txt;
                continue;
            } else if (nodeName.equals("flavorAssetIds")) {
                this.flavorAssetIds = txt;
                continue;
            } else if (nodeName.equals("sunrise")) {
                try {
                    if (!txt.equals("")) this.sunrise = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("sunset")) {
                try {
                    if (!txt.equals("")) this.sunset = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("remoteId")) {
                this.remoteId = txt;
                continue;
            } else if (nodeName.equals("plays")) {
                try {
                    if (!txt.equals("")) this.plays = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("views")) {
                try {
                    if (!txt.equals("")) this.views = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("validationErrors")) {
                this.validationErrors = new ArrayList<KalturaDistributionValidationError>();
                NodeList subNodeList = aNode.getChildNodes();
                for (int j = 0; j < subNodeList.getLength(); j++) {
                    Node arrayNode = subNodeList.item(j);
                    this.validationErrors.add((KalturaDistributionValidationError)KalturaObjectFactory.create((Element)arrayNode));
                }
                continue;
            } else if (nodeName.equals("errorType")) {
                try {
                    if (!txt.equals("")) this.errorType = KalturaBatchJobErrorTypes.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("errorNumber")) {
                try {
                    if (!txt.equals("")) this.errorNumber = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("errorDescription")) {
                this.errorDescription = txt;
                continue;
            } else if (nodeName.equals("hasSubmitResultsLog")) {
                try {
                    if (!txt.equals("")) this.hasSubmitResultsLog = KalturaNullableBoolean.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("hasSubmitSentDataLog")) {
                try {
                    if (!txt.equals("")) this.hasSubmitSentDataLog = KalturaNullableBoolean.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("hasUpdateResultsLog")) {
                try {
                    if (!txt.equals("")) this.hasUpdateResultsLog = KalturaNullableBoolean.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("hasUpdateSentDataLog")) {
                try {
                    if (!txt.equals("")) this.hasUpdateSentDataLog = KalturaNullableBoolean.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("hasDeleteResultsLog")) {
                try {
                    if (!txt.equals("")) this.hasDeleteResultsLog = KalturaNullableBoolean.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("hasDeleteSentDataLog")) {
                try {
                    if (!txt.equals("")) this.hasDeleteSentDataLog = KalturaNullableBoolean.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaEntryDistribution");
        kparams.addStringIfNotNull("entryId", this.entryId);
        kparams.addIntIfNotNull("distributionProfileId", this.distributionProfileId);
        kparams.addStringIfNotNull("thumbAssetIds", this.thumbAssetIds);
        kparams.addStringIfNotNull("flavorAssetIds", this.flavorAssetIds);
        kparams.addIntIfNotNull("sunrise", this.sunrise);
        kparams.addIntIfNotNull("sunset", this.sunset);
        return kparams;
    }
}

