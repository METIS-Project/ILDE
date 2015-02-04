package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaFileSyncStatus {
    ERROR (-1),
    PENDING (1),
    READY (2),
    DELETED (3),
    PURGED (4);

    int hashCode;

    KalturaFileSyncStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaFileSyncStatus get(int hashCode) {
        switch(hashCode) {
            case -1: return ERROR;
            case 1: return PENDING;
            case 2: return READY;
            case 3: return DELETED;
            case 4: return PURGED;
            default: return ERROR;
        }
    }
}
