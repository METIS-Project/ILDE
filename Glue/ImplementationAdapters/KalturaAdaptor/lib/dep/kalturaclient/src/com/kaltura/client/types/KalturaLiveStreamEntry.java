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

public class KalturaLiveStreamEntry extends KalturaMediaEntry {
    public String offlineMessage;
    public String streamRemoteId;
    public String streamRemoteBackupId;
    public ArrayList<KalturaLiveStreamBitrate> bitrates;
    public String primaryBroadcastingUrl;
    public String secondaryBroadcastingUrl;
    public String streamName;

    public KalturaLiveStreamEntry() {
    }

    public KalturaLiveStreamEntry(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("offlineMessage")) {
                this.offlineMessage = txt;
                continue;
            } else if (nodeName.equals("streamRemoteId")) {
                this.streamRemoteId = txt;
                continue;
            } else if (nodeName.equals("streamRemoteBackupId")) {
                this.streamRemoteBackupId = txt;
                continue;
            } else if (nodeName.equals("bitrates")) {
                this.bitrates = new ArrayList<KalturaLiveStreamBitrate>();
                NodeList subNodeList = aNode.getChildNodes();
                for (int j = 0; j < subNodeList.getLength(); j++) {
                    Node arrayNode = subNodeList.item(j);
                    this.bitrates.add((KalturaLiveStreamBitrate)KalturaObjectFactory.create((Element)arrayNode));
                }
                continue;
            } else if (nodeName.equals("primaryBroadcastingUrl")) {
                this.primaryBroadcastingUrl = txt;
                continue;
            } else if (nodeName.equals("secondaryBroadcastingUrl")) {
                this.secondaryBroadcastingUrl = txt;
                continue;
            } else if (nodeName.equals("streamName")) {
                this.streamName = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaLiveStreamEntry");
        kparams.addStringIfNotNull("offlineMessage", this.offlineMessage);
        kparams.addObjectIfNotNull("bitrates", this.bitrates);
        kparams.addStringIfNotNull("primaryBroadcastingUrl", this.primaryBroadcastingUrl);
        kparams.addStringIfNotNull("secondaryBroadcastingUrl", this.secondaryBroadcastingUrl);
        kparams.addStringIfNotNull("streamName", this.streamName);
        return kparams;
    }
}

