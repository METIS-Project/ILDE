package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaEntryDistributionSunStatus {
    BEFORE_SUNRISE (1),
    AFTER_SUNRISE (2),
    AFTER_SUNSET (3);

    int hashCode;

    KalturaEntryDistributionSunStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaEntryDistributionSunStatus get(int hashCode) {
        switch(hashCode) {
            case 1: return BEFORE_SUNRISE;
            case 2: return AFTER_SUNRISE;
            case 3: return AFTER_SUNSET;
            default: return BEFORE_SUNRISE;
        }
    }
}
