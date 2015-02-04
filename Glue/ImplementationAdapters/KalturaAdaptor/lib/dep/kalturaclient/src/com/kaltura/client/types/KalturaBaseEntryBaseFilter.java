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
import com.kaltura.client.enums.KalturaEntryStatus;
import com.kaltura.client.enums.KalturaEntryModerationStatus;
import com.kaltura.client.enums.KalturaEntryModerationStatus;
import com.kaltura.client.enums.KalturaEntryType;
import com.kaltura.client.enums.KalturaEntryReplacementStatus;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaBaseEntryBaseFilter extends KalturaFilter {
    public String idEqual;
    public String idIn;
    public String idNotIn;
    public String nameLike;
    public String nameMultiLikeOr;
    public String nameMultiLikeAnd;
    public String nameEqual;
    public int partnerIdEqual = Integer.MIN_VALUE;
    public String partnerIdIn;
    public String userIdEqual;
    public String tagsLike;
    public String tagsMultiLikeOr;
    public String tagsMultiLikeAnd;
    public String adminTagsLike;
    public String adminTagsMultiLikeOr;
    public String adminTagsMultiLikeAnd;
    public String categoriesMatchAnd;
    public String categoriesMatchOr;
    public String categoriesIdsMatchAnd;
    public String categoriesIdsMatchOr;
    public KalturaEntryStatus statusEqual;
    public KalturaEntryStatus statusNotEqual;
    public String statusIn;
    public String statusNotIn;
    public KalturaEntryModerationStatus moderationStatusEqual;
    public KalturaEntryModerationStatus moderationStatusNotEqual;
    public String moderationStatusIn;
    public String moderationStatusNotIn;
    public KalturaEntryType typeEqual;
    public String typeIn;
    public int createdAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int createdAtLessThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtGreaterThanOrEqual = Integer.MIN_VALUE;
    public int updatedAtLessThanOrEqual = Integer.MIN_VALUE;
    public int groupIdEqual = Integer.MIN_VALUE;
    public String searchTextMatchAnd;
    public String searchTextMatchOr;
    public int accessControlIdEqual = Integer.MIN_VALUE;
    public String accessControlIdIn;
    public int startDateGreaterThanOrEqual = Integer.MIN_VALUE;
    public int startDateLessThanOrEqual = Integer.MIN_VALUE;
    public int startDateGreaterThanOrEqualOrNull = Integer.MIN_VALUE;
    public int startDateLessThanOrEqualOrNull = Integer.MIN_VALUE;
    public int endDateGreaterThanOrEqual = Integer.MIN_VALUE;
    public int endDateLessThanOrEqual = Integer.MIN_VALUE;
    public int endDateGreaterThanOrEqualOrNull = Integer.MIN_VALUE;
    public int endDateLessThanOrEqualOrNull = Integer.MIN_VALUE;
    public String referenceIdEqual;
    public String referenceIdIn;
    public String replacingEntryIdEqual;
    public String replacingEntryIdIn;
    public String replacedEntryIdEqual;
    public String replacedEntryIdIn;
    public KalturaEntryReplacementStatus replacementStatusEqual;
    public String replacementStatusIn;
    public int partnerSortValueGreaterThanOrEqual = Integer.MIN_VALUE;
    public int partnerSortValueLessThanOrEqual = Integer.MIN_VALUE;
    public String tagsNameMultiLikeOr;
    public String tagsAdminTagsMultiLikeOr;
    public String tagsAdminTagsNameMultiLikeOr;
    public String tagsNameMultiLikeAnd;
    public String tagsAdminTagsMultiLikeAnd;
    public String tagsAdminTagsNameMultiLikeAnd;

    public KalturaBaseEntryBaseFilter() {
    }

    public KalturaBaseEntryBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("idEqual")) {
                this.idEqual = txt;
                continue;
            } else if (nodeName.equals("idIn")) {
                this.idIn = txt;
                continue;
            } else if (nodeName.equals("idNotIn")) {
                this.idNotIn = txt;
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
            } else if (nodeName.equals("partnerIdEqual")) {
                try {
                    if (!txt.equals("")) this.partnerIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerIdIn")) {
                this.partnerIdIn = txt;
                continue;
            } else if (nodeName.equals("userIdEqual")) {
                this.userIdEqual = txt;
                continue;
            } else if (nodeName.equals("tagsLike")) {
                this.tagsLike = txt;
                continue;
            } else if (nodeName.equals("tagsMultiLikeOr")) {
                this.tagsMultiLikeOr = txt;
                continue;
            } else if (nodeName.equals("tagsMultiLikeAnd")) {
                this.tagsMultiLikeAnd = txt;
                continue;
            } else if (nodeName.equals("adminTagsLike")) {
                this.adminTagsLike = txt;
                continue;
            } else if (nodeName.equals("adminTagsMultiLikeOr")) {
                this.adminTagsMultiLikeOr = txt;
                continue;
            } else if (nodeName.equals("adminTagsMultiLikeAnd")) {
                this.adminTagsMultiLikeAnd = txt;
                continue;
            } else if (nodeName.equals("categoriesMatchAnd")) {
                this.categoriesMatchAnd = txt;
                continue;
            } else if (nodeName.equals("categoriesMatchOr")) {
                this.categoriesMatchOr = txt;
                continue;
            } else if (nodeName.equals("categoriesIdsMatchAnd")) {
                this.categoriesIdsMatchAnd = txt;
                continue;
            } else if (nodeName.equals("categoriesIdsMatchOr")) {
                this.categoriesIdsMatchOr = txt;
                continue;
            } else if (nodeName.equals("statusEqual")) {
                try {
                    if (!txt.equals("")) this.statusEqual = KalturaEntryStatus.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("statusNotEqual")) {
                try {
                    if (!txt.equals("")) this.statusNotEqual = KalturaEntryStatus.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("statusIn")) {
                this.statusIn = txt;
                continue;
            } else if (nodeName.equals("statusNotIn")) {
                this.statusNotIn = txt;
                continue;
            } else if (nodeName.equals("moderationStatusEqual")) {
                try {
                    if (!txt.equals("")) this.moderationStatusEqual = KalturaEntryModerationStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("moderationStatusNotEqual")) {
                try {
                    if (!txt.equals("")) this.moderationStatusNotEqual = KalturaEntryModerationStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("moderationStatusIn")) {
                this.moderationStatusIn = txt;
                continue;
            } else if (nodeName.equals("moderationStatusNotIn")) {
                this.moderationStatusNotIn = txt;
                continue;
            } else if (nodeName.equals("typeEqual")) {
                try {
                    if (!txt.equals("")) this.typeEqual = KalturaEntryType.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("typeIn")) {
                this.typeIn = txt;
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
            } else if (nodeName.equals("updatedAtGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.updatedAtGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("updatedAtLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.updatedAtLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("groupIdEqual")) {
                try {
                    if (!txt.equals("")) this.groupIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("searchTextMatchAnd")) {
                this.searchTextMatchAnd = txt;
                continue;
            } else if (nodeName.equals("searchTextMatchOr")) {
                this.searchTextMatchOr = txt;
                continue;
            } else if (nodeName.equals("accessControlIdEqual")) {
                try {
                    if (!txt.equals("")) this.accessControlIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("accessControlIdIn")) {
                this.accessControlIdIn = txt;
                continue;
            } else if (nodeName.equals("startDateGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.startDateGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("startDateLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.startDateLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("startDateGreaterThanOrEqualOrNull")) {
                try {
                    if (!txt.equals("")) this.startDateGreaterThanOrEqualOrNull = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("startDateLessThanOrEqualOrNull")) {
                try {
                    if (!txt.equals("")) this.startDateLessThanOrEqualOrNull = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("endDateGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.endDateGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("endDateLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.endDateLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("endDateGreaterThanOrEqualOrNull")) {
                try {
                    if (!txt.equals("")) this.endDateGreaterThanOrEqualOrNull = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("endDateLessThanOrEqualOrNull")) {
                try {
                    if (!txt.equals("")) this.endDateLessThanOrEqualOrNull = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("referenceIdEqual")) {
                this.referenceIdEqual = txt;
                continue;
            } else if (nodeName.equals("referenceIdIn")) {
                this.referenceIdIn = txt;
                continue;
            } else if (nodeName.equals("replacingEntryIdEqual")) {
                this.replacingEntryIdEqual = txt;
                continue;
            } else if (nodeName.equals("replacingEntryIdIn")) {
                this.replacingEntryIdIn = txt;
                continue;
            } else if (nodeName.equals("replacedEntryIdEqual")) {
                this.replacedEntryIdEqual = txt;
                continue;
            } else if (nodeName.equals("replacedEntryIdIn")) {
                this.replacedEntryIdIn = txt;
                continue;
            } else if (nodeName.equals("replacementStatusEqual")) {
                try {
                    if (!txt.equals("")) this.replacementStatusEqual = KalturaEntryReplacementStatus.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("replacementStatusIn")) {
                this.replacementStatusIn = txt;
                continue;
            } else if (nodeName.equals("partnerSortValueGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.partnerSortValueGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("partnerSortValueLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.partnerSortValueLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("tagsNameMultiLikeOr")) {
                this.tagsNameMultiLikeOr = txt;
                continue;
            } else if (nodeName.equals("tagsAdminTagsMultiLikeOr")) {
                this.tagsAdminTagsMultiLikeOr = txt;
                continue;
            } else if (nodeName.equals("tagsAdminTagsNameMultiLikeOr")) {
                this.tagsAdminTagsNameMultiLikeOr = txt;
                continue;
            } else if (nodeName.equals("tagsNameMultiLikeAnd")) {
                this.tagsNameMultiLikeAnd = txt;
                continue;
            } else if (nodeName.equals("tagsAdminTagsMultiLikeAnd")) {
                this.tagsAdminTagsMultiLikeAnd = txt;
                continue;
            } else if (nodeName.equals("tagsAdminTagsNameMultiLikeAnd")) {
                this.tagsAdminTagsNameMultiLikeAnd = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaBaseEntryBaseFilter");
        kparams.addStringIfNotNull("idEqual", this.idEqual);
        kparams.addStringIfNotNull("idIn", this.idIn);
        kparams.addStringIfNotNull("idNotIn", this.idNotIn);
        kparams.addStringIfNotNull("nameLike", this.nameLike);
        kparams.addStringIfNotNull("nameMultiLikeOr", this.nameMultiLikeOr);
        kparams.addStringIfNotNull("nameMultiLikeAnd", this.nameMultiLikeAnd);
        kparams.addStringIfNotNull("nameEqual", this.nameEqual);
        kparams.addIntIfNotNull("partnerIdEqual", this.partnerIdEqual);
        kparams.addStringIfNotNull("partnerIdIn", this.partnerIdIn);
        kparams.addStringIfNotNull("userIdEqual", this.userIdEqual);
        kparams.addStringIfNotNull("tagsLike", this.tagsLike);
        kparams.addStringIfNotNull("tagsMultiLikeOr", this.tagsMultiLikeOr);
        kparams.addStringIfNotNull("tagsMultiLikeAnd", this.tagsMultiLikeAnd);
        kparams.addStringIfNotNull("adminTagsLike", this.adminTagsLike);
        kparams.addStringIfNotNull("adminTagsMultiLikeOr", this.adminTagsMultiLikeOr);
        kparams.addStringIfNotNull("adminTagsMultiLikeAnd", this.adminTagsMultiLikeAnd);
        kparams.addStringIfNotNull("categoriesMatchAnd", this.categoriesMatchAnd);
        kparams.addStringIfNotNull("categoriesMatchOr", this.categoriesMatchOr);
        kparams.addStringIfNotNull("categoriesIdsMatchAnd", this.categoriesIdsMatchAnd);
        kparams.addStringIfNotNull("categoriesIdsMatchOr", this.categoriesIdsMatchOr);
        if (statusEqual != null) kparams.addStringIfNotNull("statusEqual", this.statusEqual.getHashCode());
        if (statusNotEqual != null) kparams.addStringIfNotNull("statusNotEqual", this.statusNotEqual.getHashCode());
        kparams.addStringIfNotNull("statusIn", this.statusIn);
        kparams.addStringIfNotNull("statusNotIn", this.statusNotIn);
        if (moderationStatusEqual != null) kparams.addIntIfNotNull("moderationStatusEqual", this.moderationStatusEqual.getHashCode());
        if (moderationStatusNotEqual != null) kparams.addIntIfNotNull("moderationStatusNotEqual", this.moderationStatusNotEqual.getHashCode());
        kparams.addStringIfNotNull("moderationStatusIn", this.moderationStatusIn);
        kparams.addStringIfNotNull("moderationStatusNotIn", this.moderationStatusNotIn);
        if (typeEqual != null) kparams.addStringIfNotNull("typeEqual", this.typeEqual.getHashCode());
        kparams.addStringIfNotNull("typeIn", this.typeIn);
        kparams.addIntIfNotNull("createdAtGreaterThanOrEqual", this.createdAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("createdAtLessThanOrEqual", this.createdAtLessThanOrEqual);
        kparams.addIntIfNotNull("updatedAtGreaterThanOrEqual", this.updatedAtGreaterThanOrEqual);
        kparams.addIntIfNotNull("updatedAtLessThanOrEqual", this.updatedAtLessThanOrEqual);
        kparams.addIntIfNotNull("groupIdEqual", this.groupIdEqual);
        kparams.addStringIfNotNull("searchTextMatchAnd", this.searchTextMatchAnd);
        kparams.addStringIfNotNull("searchTextMatchOr", this.searchTextMatchOr);
        kparams.addIntIfNotNull("accessControlIdEqual", this.accessControlIdEqual);
        kparams.addStringIfNotNull("accessControlIdIn", this.accessControlIdIn);
        kparams.addIntIfNotNull("startDateGreaterThanOrEqual", this.startDateGreaterThanOrEqual);
        kparams.addIntIfNotNull("startDateLessThanOrEqual", this.startDateLessThanOrEqual);
        kparams.addIntIfNotNull("startDateGreaterThanOrEqualOrNull", this.startDateGreaterThanOrEqualOrNull);
        kparams.addIntIfNotNull("startDateLessThanOrEqualOrNull", this.startDateLessThanOrEqualOrNull);
        kparams.addIntIfNotNull("endDateGreaterThanOrEqual", this.endDateGreaterThanOrEqual);
        kparams.addIntIfNotNull("endDateLessThanOrEqual", this.endDateLessThanOrEqual);
        kparams.addIntIfNotNull("endDateGreaterThanOrEqualOrNull", this.endDateGreaterThanOrEqualOrNull);
        kparams.addIntIfNotNull("endDateLessThanOrEqualOrNull", this.endDateLessThanOrEqualOrNull);
        kparams.addStringIfNotNull("referenceIdEqual", this.referenceIdEqual);
        kparams.addStringIfNotNull("referenceIdIn", this.referenceIdIn);
        kparams.addStringIfNotNull("replacingEntryIdEqual", this.replacingEntryIdEqual);
        kparams.addStringIfNotNull("replacingEntryIdIn", this.replacingEntryIdIn);
        kparams.addStringIfNotNull("replacedEntryIdEqual", this.replacedEntryIdEqual);
        kparams.addStringIfNotNull("replacedEntryIdIn", this.replacedEntryIdIn);
        if (replacementStatusEqual != null) kparams.addStringIfNotNull("replacementStatusEqual", this.replacementStatusEqual.getHashCode());
        kparams.addStringIfNotNull("replacementStatusIn", this.replacementStatusIn);
        kparams.addIntIfNotNull("partnerSortValueGreaterThanOrEqual", this.partnerSortValueGreaterThanOrEqual);
        kparams.addIntIfNotNull("partnerSortValueLessThanOrEqual", this.partnerSortValueLessThanOrEqual);
        kparams.addStringIfNotNull("tagsNameMultiLikeOr", this.tagsNameMultiLikeOr);
        kparams.addStringIfNotNull("tagsAdminTagsMultiLikeOr", this.tagsAdminTagsMultiLikeOr);
        kparams.addStringIfNotNull("tagsAdminTagsNameMultiLikeOr", this.tagsAdminTagsNameMultiLikeOr);
        kparams.addStringIfNotNull("tagsNameMultiLikeAnd", this.tagsNameMultiLikeAnd);
        kparams.addStringIfNotNull("tagsAdminTagsMultiLikeAnd", this.tagsAdminTagsMultiLikeAnd);
        kparams.addStringIfNotNull("tagsAdminTagsNameMultiLikeAnd", this.tagsAdminTagsNameMultiLikeAnd);
        return kparams;
    }
}

