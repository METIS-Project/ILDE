package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaCountryRestrictionType {
    RESTRICT_COUNTRY_LIST (0),
    ALLOW_COUNTRY_LIST (1);

    int hashCode;

    KalturaCountryRestrictionType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaCountryRestrictionType get(int hashCode) {
        switch(hashCode) {
            case 0: return RESTRICT_COUNTRY_LIST;
            case 1: return ALLOW_COUNTRY_LIST;
            default: return RESTRICT_COUNTRY_LIST;
        }
    }
}
