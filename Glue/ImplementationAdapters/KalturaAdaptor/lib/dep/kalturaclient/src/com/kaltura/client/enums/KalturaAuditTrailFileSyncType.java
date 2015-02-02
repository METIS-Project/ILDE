package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaAuditTrailFileSyncType {
    FILE (1),
    LINK (2),
    URL (3);

    int hashCode;

    KalturaAuditTrailFileSyncType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaAuditTrailFileSyncType get(int hashCode) {
        switch(hashCode) {
            case 1: return FILE;
            case 2: return LINK;
            case 3: return URL;
            default: return FILE;
        }
    }
}
