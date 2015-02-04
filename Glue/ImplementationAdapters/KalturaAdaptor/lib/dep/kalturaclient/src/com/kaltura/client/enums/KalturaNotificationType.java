package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaNotificationType {
    ENTRY_ADD (1),
    ENTR_UPDATE_PERMISSIONS (2),
    ENTRY_DELETE (3),
    ENTRY_BLOCK (4),
    ENTRY_UPDATE (5),
    ENTRY_UPDATE_THUMBNAIL (6),
    ENTRY_UPDATE_MODERATION (7),
    USER_ADD (21),
    USER_BANNED (26);

    int hashCode;

    KalturaNotificationType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaNotificationType get(int hashCode) {
        switch(hashCode) {
            case 1: return ENTRY_ADD;
            case 2: return ENTR_UPDATE_PERMISSIONS;
            case 3: return ENTRY_DELETE;
            case 4: return ENTRY_BLOCK;
            case 5: return ENTRY_UPDATE;
            case 6: return ENTRY_UPDATE_THUMBNAIL;
            case 7: return ENTRY_UPDATE_MODERATION;
            case 21: return USER_ADD;
            case 26: return USER_BANNED;
            default: return ENTRY_ADD;
        }
    }
}
