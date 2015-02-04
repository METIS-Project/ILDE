package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaEntryModerationStatus;
import com.kaltura.client.enums.KalturaEmailIngestionProfileStatus;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaEmailIngestionProfile extends KalturaObjectBase {
    public int id = Integer.MIN_VALUE;
    public String name;
    public String description;
    public String emailAddress;
    public String mailboxId;
    public int partnerId = Integer.MIN_VALUE;
    public int conversionProfile2Id = Integer.MIN_VALUE;
    public KalturaEntryModerationStatus moderationStatus;
    public KalturaEmailIngestionProfileStatus status;
    public String createdAt;
    public String defaultCategory;
    public String defaultUserId;
    public String defaultTags;
    public String defaultAdminTags;
    public int maxAttachmentSizeKbytes = Integer.MIN_VALUE;
    public int maxAttachmentsPerMail = Integer.MIN_VALUE;

    public KalturaEmailIngestionProfile() {
    }

    public KalturaEmailIngestionProfile(Element node) throws KalturaApiException {
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
            } else if (nodeName.equals("description")) {
                this.description = txt;
                continue;
            } else if (nodeName.equals("emailAddress")) {
                this.emailAddress = txt;
                continue;
            } else if (nodeName.equals("mailboxId")) {
                this.mailboxId = txt;
                continue;
            } else if (nodeName.equals("partnerId")) {
                try {
                    if (!txt.equals("")) this.partnerId = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("conversionProfile2Id")) {
                try {
                    if (!txt.equals("")) this.conversionProfile2Id = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("moderationStatus")) {
                try {
                    if (!txt.equals("")) this.moderationStatus = KalturaEntryModerationStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("status")) {
                try {
                    if (!txt.equals("")) this.status = KalturaEmailIngestionProfileStatus.get(Integer.parseInt(txt));
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("createdAt")) {
                this.createdAt = txt;
                continue;
            } else if (nodeName.equals("defaultCategory")) {
                this.defaultCategory = txt;
                continue;
            } else if (nodeName.equals("defaultUserId")) {
                this.defaultUserId = txt;
                continue;
            } else if (nodeName.equals("defaultTags")) {
                this.defaultTags = txt;
                continue;
            } else if (nodeName.equals("defaultAdminTags")) {
                this.defaultAdminTags = txt;
                continue;
            } else if (nodeName.equals("maxAttachmentSizeKbytes")) {
                try {
                    if (!txt.equals("")) this.maxAttachmentSizeKbytes = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("maxAttachmentsPerMail")) {
                try {
                    if (!txt.equals("")) this.maxAttachmentsPerMail = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaEmailIngestionProfile");
        kparams.addStringIfNotNull("name", this.name);
        kparams.addStringIfNotNull("description", this.description);
        kparams.addStringIfNotNull("emailAddress", this.emailAddress);
        kparams.addStringIfNotNull("mailboxId", this.mailboxId);
        kparams.addIntIfNotNull("conversionProfile2Id", this.conversionProfile2Id);
        if (moderationStatus != null) kparams.addIntIfNotNull("moderationStatus", this.moderationStatus.getHashCode());
        kparams.addStringIfNotNull("defaultCategory", this.defaultCategory);
        kparams.addStringIfNotNull("defaultUserId", this.defaultUserId);
        kparams.addStringIfNotNull("defaultTags", this.defaultTags);
        kparams.addStringIfNotNull("defaultAdminTags", this.defaultAdminTags);
        kparams.addIntIfNotNull("maxAttachmentSizeKbytes", this.maxAttachmentSizeKbytes);
        kparams.addIntIfNotNull("maxAttachmentsPerMail", this.maxAttachmentsPerMail);
        return kparams;
    }
}

