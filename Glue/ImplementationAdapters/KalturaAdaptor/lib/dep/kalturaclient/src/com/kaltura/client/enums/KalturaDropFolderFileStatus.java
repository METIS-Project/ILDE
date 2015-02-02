package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDropFolderFileStatus {
    UPLOADING (1),
    PENDING (2),
    WAITING (3),
    HANDLED (4),
    IGNORE (5),
    DELETED (6),
    PURGED (7),
    NO_MATCH (8),
    ERROR_HANDLING (9),
    ERROR_DELETING (10);

    int hashCode;

    KalturaDropFolderFileStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaDropFolderFileStatus get(int hashCode) {
        switch(hashCode) {
            case 1: return UPLOADING;
            case 2: return PENDING;
            case 3: return WAITING;
            case 4: return HANDLED;
            case 5: return IGNORE;
            case 6: return DELETED;
            case 7: return PURGED;
            case 8: return NO_MATCH;
            case 9: return ERROR_HANDLING;
            case 10: return ERROR_DELETING;
            default: return UPLOADING;
        }
    }
}
