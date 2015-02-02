package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDistributionProfileActionStatus {
    DISABLED (1),
    AUTOMATIC (2),
    MANUAL (3);

    int hashCode;

    KalturaDistributionProfileActionStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaDistributionProfileActionStatus get(int hashCode) {
        switch(hashCode) {
            case 1: return DISABLED;
            case 2: return AUTOMATIC;
            case 3: return MANUAL;
            default: return DISABLED;
        }
    }
}
