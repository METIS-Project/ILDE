package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaIpAddressRestrictionType {
    RESTRICT_LIST (0),
    ALLOW_LIST (1);

    int hashCode;

    KalturaIpAddressRestrictionType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaIpAddressRestrictionType get(int hashCode) {
        switch(hashCode) {
            case 0: return RESTRICT_LIST;
            case 1: return ALLOW_LIST;
            default: return RESTRICT_LIST;
        }
    }
}
