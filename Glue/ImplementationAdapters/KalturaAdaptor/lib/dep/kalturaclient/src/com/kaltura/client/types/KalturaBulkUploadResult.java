package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import java.util.ArrayList;
import com.kaltura.client.KalturaObjectFactory;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaBulkUploadResult extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int bulkUploadJobId = Integer.MIN_VALUE;
    public int lineIndex = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public String entryId;
    public int entryStatus = Integer.MIN_VALUE;
    public String rowData;
    public String title;
    public String description;
    public String tags;
    public String url;
    public String contentType;
    public int conversionProfileId = Integer.MIN_VALUE;
    public int accessControlProfileId = Integer.MIN_VALUE;
    public String category;
    public int scheduleStartDate = Integer.MIN_VALUE;
    public int scheduleEndDate = Integer.MIN_VALUE;
    public String thumbnailUrl;
    public boolean thumbnailSaved;
    public String partnerData;
    public String errorDescription;
    public ArrayList<KalturaBulkUploadPluginData> pluginsData;

    public KalturaBulkUploadResult() {
    }

    public KalturaBulkUploadResult(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("bulkUploadJobId")) {
                try {
                    if (!txt.equals("")) this.bulkUploadJobId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("lineIndex")) {
                try {
                    if (!txt.equals("")) this.lineIndex = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("entryId")) {
                this.entryId = txt;
                continue;
            } else if (nodeName.equals("entryStatus")) {
                try {
                    if (!txt.equals("")) this.entryStatus = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("rowData")) {
                this.rowData = txt;
                continue;
            } else if (nodeName.equals("title")) {
                this.title = txt;
                continue;
            } else if (nodeName.equals("description")) {
                this.description = txt;
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = txt;
                continue;
            } else if (nodeName.equals("url")) {
                this.url = txt;
                continue;
            } else if (nodeName.equals("contentType")) {
                this.contentType = txt;
                continue;
            } else if (nodeName.equals("conversionProfileId")) {
                try {
                    if (!txt.equals("")) this.conversionProfileId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("accessControlProfileId")) {
                try {
                    if (!txt.equals("")) this.accessControlProfileId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("category")) {
                this.category = txt;
                continue;
            } else if (nodeName.equals("scheduleStartDate")) {
                try {
                    if (!txt.equals("")) this.scheduleStartDate = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("scheduleEndDate")) {
                try {
                    if (!txt.equals("")) this.scheduleEndDate = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("thumbnailUrl")) {
                this.thumbnailUrl = txt;
                continue;
            } else if (nodeName.equals("thumbnailSaved")) {
                if (!txt.equals("")) this.thumbnailSaved = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("partnerData")) {
                this.partnerData = txt;
                continue;
            } else if (nodeName.equals("errorDescription")) {
                this.errorDescription = txt;
                continue;
            } else if (nodeName.equals("pluginsData")) {
                this.pluginsData = new ArrayList<KalturaBulkUploadPluginData>();
                NodeList subNodeList = aNode.getChildNodes();
                for (int j = 0; j < subNodeList.getLength(); j++) {
                    Node arrayNode = subNodeList.item(j);
                    this.pluginsData.add((KalturaBulkUploadPluginData)KalturaObjectFactory.create((Element)arrayNode));
                }
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaBulkUploadResult");
        kparams.addIntIfNotNull("bulkUploadJobId", this.bulkUploadJobId);
        kparams.addIntIfNotNull("lineIndex", this.lineIndex);
        kparams.addIntIfNotNull("partnerId", this.partnerId);
        kparams.addStringIfNotNull("entryId", this.entryId);
        kparams.addIntIfNotNull("entryStatus", this.entryStatus);
        kparams.addStringIfNotNull("rowData", this.rowData);
        kparams.addStringIfNotNull("title", this.title);
        kparams.addStringIfNotNull("description", this.description);
        kparams.addStringIfNotNull("tags", this.tags);
        kparams.addStringIfNotNull("url", this.url);
        kparams.addStringIfNotNull("contentType", this.contentType);
        kparams.addIntIfNotNull("conversionProfileId", this.conversionProfileId);
        kparams.addIntIfNotNull("accessControlProfileId", this.accessControlProfileId);
        kparams.addStringIfNotNull("category", this.category);
        kparams.addIntIfNotNull("scheduleStartDate", this.scheduleStartDate);
        kparams.addIntIfNotNull("scheduleEndDate", this.scheduleEndDate);
        kparams.addStringIfNotNull("thumbnailUrl", this.thumbnailUrl);
        kparams.addBoolIfNotNull("thumbnailSaved", this.thumbnailSaved);
        kparams.addStringIfNotNull("partnerData", this.partnerData);
        kparams.addStringIfNotNull("errorDescription", this.errorDescription);
        kparams.addObjectIfNotNull("pluginsData", this.pluginsData);
        return kparams;
    }
}

