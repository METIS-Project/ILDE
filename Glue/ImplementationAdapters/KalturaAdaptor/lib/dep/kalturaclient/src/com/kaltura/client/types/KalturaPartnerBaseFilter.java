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

public abstract class KalturaPartnerBaseFilter extends KalturaFilter {
    public int idEqual = Integer.MIN_VALUE;
    public String idIn;
    public String nameLike;
    public String nameMultiLikeOr;
    public String nameMultiLikeAnd;
    public String nameEqual;
    public int statusEqual = Integer.MIN_VALUE;
    public String statusIn;
    public int partnerPackageEqual = Integer.MIN_VALUE;
    public int partnerPackageGreaterThanOrEqual = Integer.MIN_VALUE;
    public int partnerPackageLessThanOrEqual = Integer.MIN_VALUE;
    public String partnerNameDescriptionWebsiteAdminNameAdminEmailLike;

    public KalturaPartnerBaseFilter() {
    }

    public KalturaPartnerBaseFilter(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("nameLike")) {
                this.nameLike = txt;
                continue;
            } else if (nodeName.equals("nameMultiLikeOr")) {
                this.nameMultiLikeOr = txt;
                continue;
            } else if (nodeName.equals("nameMultiLikeAnd")) {
                this.nameMultiLikeAnd = txt;
                continue;
            } else if (nodeName.equals("nameEqual")) {
                this.nameEqual = txt;
                continue;
            } else if (nodeName.equals("statusEqual")) {
                try {
                    if (!txt.equals("")) this.statusEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = txt;
                continue;
            } else if (nodeName.equals("partnerPackageEqual")) {
                try {
                    if (!txt.equals("")) this.partnerPackageEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerPackageGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.partnerPackageGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerPackageLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.partnerPackageLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerNameDescriptionWebsiteAdminNameAdminEmailLike")) {
                this.partnerNameDescriptionWebsiteAdminNameAdminEmailLike = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaPartnerBaseFilter");
        kparams.addIntIfNotNull("idEqual", this.idEqual);
        kparams.addStringIfNotNull("idIn", this.idIn);
        kparams.addStringIfNotNull("nameLike", this.nameLike);
        kparams.addStringIfNotNull("nameMultiLikeOr", this.nameMultiLikeOr);
        kparams.addStringIfNotNull("nameMultiLikeAnd", this.nameMultiLikeAnd);
        kparams.addStringIfNotNull("nameEqual", this.nameEqual);
        kparams.addIntIfNotNull("statusEqual", this.statusEqual);
        kparams.addStringIfNotNull("statusIn", this.statusIn);
        kparams.addIntIfNotNull("partnerPackageEqual", this.partnerPackageEqual);
        kparams.addIntIfNotNull("partnerPackageGreaterThanOrEqual", this.partnerPackageGreaterThanOrEqual);
        kparams.addIntIfNotNull("partnerPackageLessThanOrEqual", this.partnerPackageLessThanOrEqual);
        kparams.addStringIfNotNull("partnerNameDescriptionWebsiteAdminNameAdminEmailLike", this.partnerNameDescriptionWebsiteAdminNameAdminEmailLike);
        return kparams;
    }
}

