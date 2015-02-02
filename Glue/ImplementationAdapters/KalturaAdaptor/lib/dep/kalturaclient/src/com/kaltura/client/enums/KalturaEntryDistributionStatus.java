package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaEntryDistributionStatus {
    PENDING (0),
    QUEUED (1),
    READY (2),
    DELETED (3),
    SUBMITTING (4),
    UPDATING (5),
    DELETING (6),
    ERROR_SUBMITTING (7),
    ERROR_UPDATING (8),
    ERROR_DELETING (9),
    REMOVED (10);

    int hashCode;

    KalturaEntryDistributionStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaEntryDistributionStatus get(int hashCode) {
        switch(hashCode) {
            case 0: return PENDING;
            case 1: return QUEUED;
            case 2: return READY;
            case 3: return DELETED;
            case 4: return SUBMITTING;
            case 5: return UPDATING;
            case 6: return DELETING;
            case 7: return ERROR_SUBMITTING;
            case 8: return ERROR_UPDATING;
            case 9: return ERROR_DELETING;
            case 10: return REMOVED;
            default: return PENDING;
        }
    }
}
