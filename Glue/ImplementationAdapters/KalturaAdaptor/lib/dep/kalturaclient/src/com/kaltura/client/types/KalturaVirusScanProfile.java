package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaVirusScanProfileStatus;
import com.kaltura.client.enums.KalturaVirusScanEngineType;
import com.kaltura.client.enums.KalturaVirusFoundAction;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaVirusScanProfile extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
    public int partnerId = Integer.MIN_VALUE;
    public String name;
    public KalturaVirusScanProfileStatus status;
    public KalturaVirusScanEngineType engineType;
    public KalturaBaseEntryFilter entryFilter;
    public KalturaVirusFoundAction actionIfInfected;

    public KalturaVirusScanProfile() {
    }

    public KalturaVirusScanProfile(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("name")) {
                this.name = txt;
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaVirusScanProfileStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("engineType")) {
                try {
                    if (!txt.equals("")) this.engineType = KalturaVirusScanEngineType.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("entryFilter")) {
                this.entryFilter = (KalturaBaseEntryFilter)KalturaObjectFactory.create((Element)aNode);
                continue;
            } else if (nodeName.equals("actionIfInfected")) {
                try {
                    if (!txt.equals("")) this.actionIfInfected = KalturaVirusFoundAction.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaVirusScanProfile");
        kparams.addStringIfNotNull("name", this.name);
        if (status != null) kparams.addIntIfNotNull("status", this.status.getHashCode());
        if (engineType != null) kparams.addStringIfNotNull("engineType", this.engineType.getHashCode());
        kparams.addObjectIfNotNull("entryFilter", this.entryFilter);
        if (actionIfInfected != null) kparams.addIntIfNotNull("actionIfInfected", this.actionIfInfected.getHashCode());
        return kparams;
    }
}

