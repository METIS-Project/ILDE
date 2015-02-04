package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaBatchJobStatus;
import java.util.ArrayList;
import com.kaltura.client.KalturaObjectFactory;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaBulkUpload extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public String uploadedBy;
    public String uploadedByUserId;
    public int uploadedOn = Integer.MIN_VALUE;
    public int numOfEntries = Integer.MIN_VALUE;
    public KalturaBatchJobStatus status;
    public String logFileUrl;
    public String csvFileUrl;
    public String bulkFileUrl;
    public ArrayList<KalturaBulkUploadResult> results;

    public KalturaBulkUpload() {
    }

    public KalturaBulkUpload(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("uploadedBy")) {
                this.uploadedBy = txt;
                continue;
            } else if (nodeName.equals("uploadedByUserId")) {
                this.uploadedByUserId = txt;
                continue;
            } else if (nodeName.equals("uploadedOn")) {
                try {
                    if (!txt.equals("")) this.uploadedOn = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("numOfEntries")) {
                try {
                    if (!txt.equals("")) this.numOfEntries = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaBatchJobStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("logFileUrl")) {
                this.logFileUrl = txt;
                continue;
            } else if (nodeName.equals("csvFileUrl")) {
                this.csvFileUrl = txt;
                continue;
            } else if (nodeName.equals("bulkFileUrl")) {
                this.bulkFileUrl = txt;
                continue;
            } else if (nodeName.equals("results")) {
                this.results = new ArrayList<KalturaBulkUploadResult>();
                NodeList subNodeList = aNode.getChildNodes();
                for (int j = 0; j < subNodeList.getLength(); j++) {
                    Node arrayNode = subNodeList.item(j);
                    this.results.add((KalturaBulkUploadResult)KalturaObjectFactory.create((Element)arrayNode));
                }
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaBulkUpload");
        kparams.addIntIfNotNull("id", this.id);
        kparams.addStringIfNotNull("uploadedBy", this.uploadedBy);
        kparams.addStringIfNotNull("uploadedByUserId", this.uploadedByUserId);
        kparams.addIntIfNotNull("uploadedOn", this.uploadedOn);
        kparams.addIntIfNotNull("numOfEntries", this.numOfEntries);
        if (status != null) kparams.addIntIfNotNull("status", this.status.getHashCode());
        kparams.addStringIfNotNull("logFileUrl", this.logFileUrl);
        kparams.addStringIfNotNull("csvFileUrl", this.csvFileUrl);
        kparams.addStringIfNotNull("bulkFileUrl", this.bulkFileUrl);
        kparams.addObjectIfNotNull("results", this.results);
        return kparams;
    }
}

