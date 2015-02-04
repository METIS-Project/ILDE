package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaAuditTrailStatus {
    PENDING (1),
    READY (2),
    FAILED (3);

    int hashCode;

    KalturaAuditTrailStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaAuditTrailStatus get(int hashCode) {
        switch(hashCode) {
            case 1: return PENDING;
            case 2: return READY;
            case 3: return FAILED;
            default: return PENDING;
        }
    }
}
