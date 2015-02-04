package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDistributionFieldRequiredStatus {
    NOT_REQUIRED (0),
    REQUIRED_BY_PROVIDER (1),
    REQUIRED_BY_PARTNER (2);

    int hashCode;

    KalturaDistributionFieldRequiredStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaDistributionFieldRequiredStatus get(int hashCode) {
        switch(hashCode) {
            case 0: return NOT_REQUIRED;
            case 1: return REQUIRED_BY_PROVIDER;
            case 2: return REQUIRED_BY_PARTNER;
            default: return NOT_REQUIRED;
        }
    }
}
