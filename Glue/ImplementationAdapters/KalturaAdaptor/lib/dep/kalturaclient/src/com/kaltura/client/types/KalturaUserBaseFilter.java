package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaUserStatus;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaUserBaseFilter extends KalturaFilter {
    public int partnerIdEqual = Integer.MIN_VALUE;
    public String screenNameLike;
    public String screenNameStartsWith;
    public String emailLike;
    public String emailStartsWith;
    public String tagsMultiLikeOr;
    public String tagsMultiLikeAnd;
    public KalturaUserStatus statusEqual;
    public String statusIn;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public boolean isAdminEqual;

    public KalturaUserBaseFilter() {
    }

    public KalturaUserBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("partnerIdEqual")) {
                try {
                    if (!txt.equals("")) this.partnerIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("screenNameLike")) {
                this.screenNameLike = txt;
                continue;
            } else if (nodeName.equals("screenNameStartsWith")) {
                this.screenNameStartsWith = txt;
                continue;
            } else if (nodeName.equals("emailLike")) {
                this.emailLike = txt;
                continue;
            } else if (nodeName.equals("emailStartsWith")) {
                this.emailStartsWith = txt;
                continue;
            } else if (nodeName.equals("tagsMultiLikeOr")) {
                this.tagsMultiLikeOr = txt;
                continue;
            } else if (nodeName.equals("tagsMultiLikeAnd")) {
                this.tagsMultiLikeAnd = txt;
                continue;
            } else if (nodeName.equals("statusEqual")) {
                try {
                    if (!txt.equals("")) this.statusEqual = KalturaUserStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = txt;
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
            } else if (nodeName.equals("isAdminEqual")) {
                if (!txt.equals("")) this.isAdminEqual = ((txt.equals("0")) ? false : true);
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaUserBaseFilter");
        kparams.addIntIfNotNull("partnerIdEqual", this.partnerIdEqual);
        kparams.addStringIfNotNull("screenNameLike", this.screenNameLike);
        kparams.addStringIfNotNull("screenNameStartsWith", this.screenNameStartsWith);
        kparams.addStringIfNotNull("emailLike", this.emailLike);
        kparams.addStringIfNotNull("emailStartsWith", this.emailStartsWith);
        kparams.addStringIfNotNull("tagsMultiLikeOr", this.tagsMultiLikeOr);
        kparams.addStringIfNotNull("tagsMultiLikeAnd", this.tagsMultiLikeAnd);
        if (statusEqual != null) kparams.addIntIfNotNull("statusEqual", this.statusEqual.getHashCode());
        kparams.addStringIfNotNull("statusIn", this.statusIn);
        kparams.addIntIfNotNull("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.addBoolIfNotNull("isAdminEqual", this.isAdminEqual);
        return kparams;
    }
}

