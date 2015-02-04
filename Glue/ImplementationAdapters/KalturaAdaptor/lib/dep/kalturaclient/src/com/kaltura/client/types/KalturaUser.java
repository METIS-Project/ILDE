package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaGender;
import com.kaltura.client.enums.KalturaUserStatus;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaUser extends KalturaObjectBase {
    public String id;
    public int partnerId = Integer.MIN_VALUE;
    public String screenName;
    public String fullName;
    public String email;
    public int dateOfBirth = Integer.MIN_VALUE;
    public String country;
    public String state;
    public String city;
    public String zip;
    public String thumbnailUrl;
    public String description;
    public String tags;
    public String adminTags;
    public KalturaGender gender;
    public KalturaUserStatus status;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
    public String partnerData;
    public int indexedPartnerDataInt = Integer.MIN_VALUE;
    public String indexedPartnerDataString;
    public int storageSize = Integer.MIN_VALUE;
    public String password;
    public String firstName;
    public String lastName;
    public boolean isAdmin;
    public int lastLoginTime = Integer.MIN_VALUE;
    public int statusUpdatedAt = Integer.MIN_VALUE;
    public int deletedAt = Integer.MIN_VALUE;
    public boolean loginEnabled;
    public String roleIds;
    public String roleNames;
    public boolean isAccountOwner;

    public KalturaUser() {
    }

    public KalturaUser(Element node) throws KalturaApiException {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("id")) {
                this.id = txt;
                continue;
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("screenName")) {
                this.screenName = txt;
                continue;
            } else if (nodeName.equals("fullName")) {
                this.fullName = txt;
                continue;
            } else if (nodeName.equals("email")) {
                this.email = txt;
                continue;
            } else if (nodeName.equals("dateOfBirth")) {
                try {
                    if (!txt.equals("")) this.dateOfBirth = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("country")) {
                this.country = txt;
                continue;
            } else if (nodeName.equals("state")) {
                this.state = txt;
                continue;
            } else if (nodeName.equals("city")) {
                this.city = txt;
                continue;
            } else if (nodeName.equals("zip")) {
                this.zip = txt;
                continue;
            } else if (nodeName.equals("thumbnailUrl")) {
                this.thumbnailUrl = txt;
                continue;
            } else if (nodeName.equals("description")) {
                this.description = txt;
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = txt;
                continue;
            } else if (nodeName.equals("adminTags")) {
                this.adminTags = txt;
                continue;
            } else if (nodeName.equals("gender")) {
                try {
                    if (!txt.equals("")) this.gender = KalturaGender.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaUserStatus.get(Integer.parseInt(txt));
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
            } else if (nodeName.equals("partnerData")) {
                this.partnerData = txt;
                continue;
            } else if (nodeName.equals("indexedPartnerDataInt")) {
                try {
                    if (!txt.equals("")) this.indexedPartnerDataInt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("indexedPartnerDataString")) {
                this.indexedPartnerDataString = txt;
                continue;
            } else if (nodeName.equals("storageSize")) {
                try {
                    if (!txt.equals("")) this.storageSize = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("password")) {
                this.password = txt;
                continue;
            } else if (nodeName.equals("firstName")) {
                this.firstName = txt;
                continue;
            } else if (nodeName.equals("lastName")) {
                this.lastName = txt;
                continue;
            } else if (nodeName.equals("isAdmin")) {
                if (!txt.equals("")) this.isAdmin = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("lastLoginTime")) {
                try {
                    if (!txt.equals("")) this.lastLoginTime = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("statusUpdatedAt")) {
                try {
                    if (!txt.equals("")) this.statusUpdatedAt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("deletedAt")) {
                try {
                    if (!txt.equals("")) this.deletedAt = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("loginEnabled")) {
                if (!txt.equals("")) this.loginEnabled = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("roleIds")) {
                this.roleIds = txt;
                continue;
            } else if (nodeName.equals("roleNames")) {
                this.roleNames = txt;
                continue;
            } else if (nodeName.equals("isAccountOwner")) {
                if (!txt.equals("")) this.isAccountOwner = ((txt.equals("0")) ? false : true);
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaUser");
        kparams.addStringIfNotNull("id", this.id);
        kparams.addStringIfNotNull("screenName", this.screenName);
        kparams.addStringIfNotNull("fullName", this.fullName);
        kparams.addStringIfNotNull("email", this.email);
        kparams.addIntIfNotNull("dateOfBirth", this.dateOfBirth);
        kparams.addStringIfNotNull("country", this.country);
        kparams.addStringIfNotNull("state", this.state);
        kparams.addStringIfNotNull("city", this.city);
        kparams.addStringIfNotNull("zip", this.zip);
        kparams.addStringIfNotNull("thumbnailUrl", this.thumbnailUrl);
        kparams.addStringIfNotNull("description", this.description);
        kparams.addStringIfNotNull("tags", this.tags);
        kparams.addStringIfNotNull("adminTags", this.adminTags);
        if (gender != null) kparams.addIntIfNotNull("gender", this.gender.getHashCode());
        if (status != null) kparams.addIntIfNotNull("status", this.status.getHashCode());
        kparams.addStringIfNotNull("partnerData", this.partnerData);
        kparams.addIntIfNotNull("indexedPartnerDataInt", this.indexedPartnerDataInt);
        kparams.addStringIfNotNull("indexedPartnerDataString", this.indexedPartnerDataString);
        kparams.addStringIfNotNull("password", this.password);
        kparams.addStringIfNotNull("firstName", this.firstName);
        kparams.addStringIfNotNull("lastName", this.lastName);
        kparams.addBoolIfNotNull("isAdmin", this.isAdmin);
        kparams.addStringIfNotNull("roleIds", this.roleIds);
        return kparams;
    }
}

