package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaAuditTrailFileSyncType;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaAuditTrailFileSyncCreateInfo extends KalturaAuditTrailInfo {
    public String version;
    public int objectSubType = Integer.MIN_VALUE;
    public int dc = Integer.MIN_VALUE;
    public boolean original;
    public KalturaAuditTrailFileSyncType fileType;

    public KalturaAuditTrailFileSyncCreateInfo() {
    }

    public KalturaAuditTrailFileSyncCreateInfo(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("version")) {
                this.version = txt;
                continue;
            } else if (nodeName.equals("objectSubType")) {
                try {
                    if (!txt.equals("")) this.objectSubType = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("dc")) {
                try {
                    if (!txt.equals("")) this.dc = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("original")) {
                if (!txt.equals("")) this.original = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("fileType")) {
                try {
                    if (!txt.equals("")) this.fileType = KalturaAuditTrailFileSyncType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaAuditTrailFileSyncCreateInfo");
        kparams.addStringIfNotNull("version", this.version);
        kparams.addIntIfNotNull("objectSubType", this.objectSubType);
        kparams.addIntIfNotNull("dc", this.dc);
        kparams.addBoolIfNotNull("original", this.original);
        if (fileType != null) kparams.addIntIfNotNull("fileType", this.fileType.getHashCode());
        return kparams;
    }
}

