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

public class KalturaEntryContextDataResult extends KalturaObjectBase {
    public boolean isSiteRestricted;
    public boolean isCountryRestricted;
    public boolean isSessionRestricted;
    public boolean isIpAddressRestricted;
    public int previewLength = Integer.MIN_VALUE;
    public boolean isScheduledNow;
    public boolean isAdmin;

    public KalturaEntryContextDataResult() {
    }

    public KalturaEntryContextDataResult(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("isSiteRestricted")) {
                if (!txt.equals("")) this.isSiteRestricted = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("isCountryRestricted")) {
                if (!txt.equals("")) this.isCountryRestricted = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("isSessionRestricted")) {
                if (!txt.equals("")) this.isSessionRestricted = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("isIpAddressRestricted")) {
                if (!txt.equals("")) this.isIpAddressRestricted = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("previewLength")) {
                try {
                    if (!txt.equals("")) this.previewLength = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("isScheduledNow")) {
                if (!txt.equals("")) this.isScheduledNow = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("isAdmin")) {
                if (!txt.equals("")) this.isAdmin = ((txt.equals("0")) ? false : true);
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaEntryContextDataResult");
        kparams.addBoolIfNotNull("isSiteRestricted", this.isSiteRestricted);
        kparams.addBoolIfNotNull("isCountryRestricted", this.isCountryRestricted);
        kparams.addBoolIfNotNull("isSessionRestricted", this.isSessionRestricted);
        kparams.addBoolIfNotNull("isIpAddressRestricted", this.isIpAddressRestricted);
        kparams.addIntIfNotNull("previewLength", this.previewLength);
        kparams.addBoolIfNotNull("isScheduledNow", this.isScheduledNow);
        kparams.addBoolIfNotNull("isAdmin", this.isAdmin);
        return kparams;
    }
}

