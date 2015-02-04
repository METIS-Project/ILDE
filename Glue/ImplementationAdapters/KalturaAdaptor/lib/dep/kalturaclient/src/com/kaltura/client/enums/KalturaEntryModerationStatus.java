package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaEntryModerationStatus {
    PENDING_MODERATION (1),
    APPROVED (2),
    REJECTED (3),
    FLAGGED_FOR_REVIEW (5),
    AUTO_APPROVED (6);

    int hashCode;

    KalturaEntryModerationStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaEntryModerationStatus get(int hashCode) {
        switch(hashCode) {
            case 1: return PENDING_MODERATION;
            case 2: return APPROVED;
            case 3: return REJECTED;
            case 5: return FLAGGED_FOR_REVIEW;
            case 6: return AUTO_APPROVED;
            default: return PENDING_MODERATION;
        }
    }
}
