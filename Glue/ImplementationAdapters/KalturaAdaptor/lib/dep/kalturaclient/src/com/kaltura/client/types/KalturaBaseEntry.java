package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaEntryStatus;
import com.kaltura.client.enums.KalturaEntryModerationStatus;
import com.kaltura.client.enums.KalturaEntryType;
import com.kaltura.client.enums.KalturaLicenseType;
import com.kaltura.client.enums.KalturaEntryReplacementStatus;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaBaseEntry extends KalturaObjectBase {
    public String id;
    public String name;
    public String description;
    public int partnerId = Integer.MIN_VALUE;
    public String userId;
    public String tags;
    public String adminTags;
    public String categories;
    public String categoriesIds;
    public KalturaEntryStatus status;
    public KalturaEntryModerationStatus moderationStatus;
    public int moderationCount = Integer.MIN_VALUE;
    public KalturaEntryType type;
    public int createdAt = Integer.MIN_VALUE;
    public int updatedAt = Integer.MIN_VALUE;
    public float rank = Float.MIN_VALUE;
    public int totalRank = Integer.MIN_VALUE;
    public int votes = Integer.MIN_VALUE;
    public int groupId = Integer.MIN_VALUE;
    public String partnerData;
    public String downloadUrl;
    public String searchText;
    public KalturaLicenseType licenseType;
    public int version = Integer.MIN_VALUE;
    public String thumbnailUrl;
    public int accessControlId = Integer.MIN_VALUE;
    public int startDate = Integer.MIN_VALUE;
    public int endDate = Integer.MIN_VALUE;
    public String referenceId;
    public String replacingEntryId;
    public String replacedEntryId;
    public KalturaEntryReplacementStatus replacementStatus;
    public int partnerSortValue = Integer.MIN_VALUE;
    public int conversionProfileId = Integer.MIN_VALUE;

    public KalturaBaseEntry() {
    }

    public KalturaBaseEntry(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("name")) {
                this.name = txt;
                continue;
            } else if (nodeName.equals("description")) {
                this.description = txt;
                continue;
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("userId")) {
                this.userId = txt;
                continue;
            } else if (nodeName.equals("tags")) {
                this.tags = txt;
                continue;
            } else if (nodeName.equals("adminTags")) {
                this.adminTags = txt;
                continue;
            } else if (nodeName.equals("categories")) {
                this.categories = txt;
                continue;
            } else if (nodeName.equals("categoriesIds")) {
                this.categoriesIds = txt;
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaEntryStatus.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("moderationStatus")) {
                try {
                    if (!txt.equals("")) this.moderationStatus = KalturaEntryModerationStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("moderationCount")) {
                try {
                    if (!txt.equals("")) this.moderationCount = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("type")) {
                try {
                    if (!txt.equals("")) this.type = KalturaEntryType.get(txt);
                } catch (IllegalFormatException ife) {}
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
            } else if (nodeName.equals("rank")) {
                try {
                    if (!txt.equals("")) this.rank = Float.parseFloat(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("totalRank")) {
                try {
                    if (!txt.equals("")) this.totalRank = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("votes")) {
                try {
                    if (!txt.equals("")) this.votes = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("groupId")) {
                try {
                    if (!txt.equals("")) this.groupId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerData")) {
                this.partnerData = txt;
                continue;
            } else if (nodeName.equals("downloadUrl")) {
                this.downloadUrl = txt;
                continue;
            } else if (nodeName.equals("searchText")) {
                this.searchText = txt;
                continue;
            } else if (nodeName.equals("licenseType")) {
                try {
                    if (!txt.equals("")) this.licenseType = KalturaLicenseType.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("version")) {
                try {
                    if (!txt.equals("")) this.version = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("thumbnailUrl")) {
                this.thumbnailUrl = txt;
                continue;
            } else if (nodeName.equals("accessControlId")) {
                try {
                    if (!txt.equals("")) this.accessControlId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("startDate")) {
                try {
                    if (!txt.equals("")) this.startDate = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("endDate")) {
                try {
                    if (!txt.equals("")) this.endDate = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("referenceId")) {
                this.referenceId = txt;
                continue;
            } else if (nodeName.equals("replacingEntryId")) {
                this.replacingEntryId = txt;
                continue;
            } else if (nodeName.equals("replacedEntryId")) {
                this.replacedEntryId = txt;
                continue;
            } else if (nodeName.equals("replacementStatus")) {
                try {
                    if (!txt.equals("")) this.replacementStatus = KalturaEntryReplacementStatus.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("partnerSortValue")) {
                try {
                    if (!txt.equals("")) this.partnerSortValue = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("conversionProfileId")) {
                try {
                    if (!txt.equals("")) this.conversionProfileId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaBaseEntry");
        kparams.addStringIfNotNull("name", this.name);
        kparams.addStringIfNotNull("description", this.description);
        kparams.addStringIfNotNull("userId", this.userId);
        kparams.addStringIfNotNull("tags", this.tags);
        kparams.addStringIfNotNull("adminTags", this.adminTags);
        kparams.addStringIfNotNull("categories", this.categories);
        kparams.addStringIfNotNull("categoriesIds", this.categoriesIds);
        if (type != null) kparams.addStringIfNotNull("type", this.type.getHashCode());
        kparams.addIntIfNotNull("groupId", this.groupId);
        kparams.addStringIfNotNull("partnerData", this.partnerData);
        if (licenseType != null) kparams.addIntIfNotNull("licenseType", this.licenseType.getHashCode());
        kparams.addStringIfNotNull("thumbnailUrl", this.thumbnailUrl);
        kparams.addIntIfNotNull("accessControlId", this.accessControlId);
        kparams.addIntIfNotNull("startDate", this.startDate);
        kparams.addIntIfNotNull("endDate", this.endDate);
        kparams.addStringIfNotNull("referenceId", this.referenceId);
        kparams.addIntIfNotNull("partnerSortValue", this.partnerSortValue);
        kparams.addIntIfNotNull("conversionProfileId", this.conversionProfileId);
        return kparams;
    }
}

