package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaPartnerStatus {
    ACTIVE (1),
    BLOCKED (2),
    FULL_BLOCK (3);

    int hashCode;

    KalturaPartnerStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaPartnerStatus get(int hashCode) {
        switch(hashCode) {
            case 1: return ACTIVE;
            case 2: return BLOCKED;
            case 3: return FULL_BLOCK;
            default: return ACTIVE;
        }
    }
}
