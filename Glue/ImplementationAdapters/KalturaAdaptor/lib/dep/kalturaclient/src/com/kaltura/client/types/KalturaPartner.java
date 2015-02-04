package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaCommercialUseType;
import com.kaltura.client.enums.KalturaPartnerType;
import com.kaltura.client.enums.KalturaPartnerStatus;
import java.util.ArrayList;
import com.kaltura.client.KalturaObjectFactory;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaPartner extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public String name;
    public String website;
    public String notificationUrl;
    public int appearInSearch = Integer.MIN_VALUE;
    public int createdAt = Integer.MIN_VALUE;
    public String adminName;
    public String adminEmail;
    public String description;
    public KalturaCommercialUseType commercialUse;
    public String landingPage;
    public String userLandingPage;
    public String contentCategories;
    public KalturaPartnerType type;
    public String phone;
    public String describeYourself;
    public boolean adultContent;
    public String defConversionProfileType;
    public int notify = Integer.MIN_VALUE;
    public KalturaPartnerStatus status;
    public int allowQuickEdit = Integer.MIN_VALUE;
    public int mergeEntryLists = Integer.MIN_VALUE;
    public String notificationsConfig;
    public int maxUploadSize = Integer.MIN_VALUE;
    public int partnerPackage = Integer.MIN_VALUE;
    public String secret;
    public String adminSecret;
    public String cmsPassword;
    public int allowMultiNotification = Integer.MIN_VALUE;
    public int adminLoginUsersQuota = Integer.MIN_VALUE;
    public String adminUserId;
    public String firstName;
    public String lastName;
    public String country;
    public String state;
    public ArrayList<KalturaKeyValue> additionalParams;

    public KalturaPartner() {
    }

    public KalturaPartner(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("name")) {
                this.name = txt;
                continue;
            } else if (nodeName.equals("website")) {
                this.website = txt;
                continue;
            } else if (nodeName.equals("notificationUrl")) {
                this.notificationUrl = txt;
                continue;
            } else if (nodeName.equals("appearInSearch")) {
                try {
                    if (!txt.equals("")) this.appearInSearch = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("createdAt")) {
                try {
                    if (!txt.equals("")) this.createdAt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("adminName")) {
                this.adminName = txt;
                continue;
            } else if (nodeName.equals("adminEmail")) {
                this.adminEmail = txt;
                continue;
            } else if (nodeName.equals("description")) {
                this.description = txt;
                continue;
            } else if (nodeName.equals("commercialUse")) {
                try {
                    if (!txt.equals("")) this.commercialUse = KalturaCommercialUseType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("landingPage")) {
                this.landingPage = txt;
                continue;
            } else if (nodeName.equals("userLandingPage")) {
                this.userLandingPage = txt;
                continue;
            } else if (nodeName.equals("contentCategories")) {
                this.contentCategories = txt;
                continue;
            } else if (nodeName.equals("type")) {
                try {
                    if (!txt.equals("")) this.type = KalturaPartnerType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("phone")) {
                this.phone = txt;
                continue;
            } else if (nodeName.equals("describeYourself")) {
                this.describeYourself = txt;
                continue;
            } else if (nodeName.equals("adultContent")) {
                if (!txt.equals("")) this.adultContent = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("defConversionProfileType")) {
                this.defConversionProfileType = txt;
                continue;
            } else if (nodeName.equals("notify")) {
                try {
                    if (!txt.equals("")) this.notify = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaPartnerStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("allowQuickEdit")) {
                try {
                    if (!txt.equals("")) this.allowQuickEdit = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("mergeEntryLists")) {
                try {
                    if (!txt.equals("")) this.mergeEntryLists = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("notificationsConfig")) {
                this.notificationsConfig = txt;
                continue;
            } else if (nodeName.equals("maxUploadSize")) {
                try {
                    if (!txt.equals("")) this.maxUploadSize = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerPackage")) {
                try {
                    if (!txt.equals("")) this.partnerPackage = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("secret")) {
                this.secret = txt;
                continue;
            } else if (nodeName.equals("adminSecret")) {
                this.adminSecret = txt;
                continue;
            } else if (nodeName.equals("cmsPassword")) {
                this.cmsPassword = txt;
                continue;
            } else if (nodeName.equals("allowMultiNotification")) {
                try {
                    if (!txt.equals("")) this.allowMultiNotification = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("adminLoginUsersQuota")) {
                try {
                    if (!txt.equals("")) this.adminLoginUsersQuota = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("adminUserId")) {
                this.adminUserId = txt;
                continue;
            } else if (nodeName.equals("firstName")) {
                this.firstName = txt;
                continue;
            } else if (nodeName.equals("lastName")) {
                this.lastName = txt;
                continue;
            } else if (nodeName.equals("country")) {
                this.country = txt;
                continue;
            } else if (nodeName.equals("state")) {
                this.state = txt;
                continue;
            } else if (nodeName.equals("additionalParams")) {
                this.additionalParams = new ArrayList<KalturaKeyValue>();
                NodeList subNodeList = aNode.getChildNodes();
                for (int j = 0; j < subNodeList.getLength(); j++) {
                    Node arrayNode = subNodeList.item(j);
                    this.additionalParams.add((KalturaKeyValue)KalturaObjectFactory.create((Element)arrayNode));
                }
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaPartner");
        kparams.addStringIfNotNull("name", this.name);
        kparams.addStringIfNotNull("website", this.website);
        kparams.addStringIfNotNull("notificationUrl", this.notificationUrl);
        kparams.addIntIfNotNull("appearInSearch", this.appearInSearch);
        kparams.addStringIfNotNull("adminName", this.adminName);
        kparams.addStringIfNotNull("adminEmail", this.adminEmail);
        kparams.addStringIfNotNull("description", this.description);
        if (commercialUse != null) kparams.addIntIfNotNull("commercialUse", this.commercialUse.getHashCode());
        kparams.addStringIfNotNull("landingPage", this.landingPage);
        kparams.addStringIfNotNull("userLandingPage", this.userLandingPage);
        kparams.addStringIfNotNull("contentCategories", this.contentCategories);
        if (type != null) kparams.addIntIfNotNull("type", this.type.getHashCode());
        kparams.addStringIfNotNull("phone", this.phone);
        kparams.addStringIfNotNull("describeYourself", this.describeYourself);
        kparams.addBoolIfNotNull("adultContent", this.adultContent);
        kparams.addStringIfNotNull("defConversionProfileType", this.defConversionProfileType);
        kparams.addIntIfNotNull("notify", this.notify);
        kparams.addIntIfNotNull("allowQuickEdit", this.allowQuickEdit);
        kparams.addIntIfNotNull("mergeEntryLists", this.mergeEntryLists);
        kparams.addStringIfNotNull("notificationsConfig", this.notificationsConfig);
        kparams.addIntIfNotNull("maxUploadSize", this.maxUploadSize);
        kparams.addIntIfNotNull("allowMultiNotification", this.allowMultiNotification);
        kparams.addStringIfNotNull("adminUserId", this.adminUserId);
        kparams.addStringIfNotNull("firstName", this.firstName);
        kparams.addStringIfNotNull("lastName", this.lastName);
        kparams.addStringIfNotNull("country", this.country);
        kparams.addStringIfNotNull("state", this.state);
        kparams.addObjectIfNotNull("additionalParams", this.additionalParams);
        return kparams;
    }
}

