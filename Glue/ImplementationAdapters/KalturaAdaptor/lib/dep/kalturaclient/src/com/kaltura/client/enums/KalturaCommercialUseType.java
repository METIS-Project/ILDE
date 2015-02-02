package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaCommercialUseType {
    COMMERCIAL_USE (1),
    NON_COMMERCIAL_USE (0);

    int hashCode;

    KalturaCommercialUseType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaCommercialUseType get(int hashCode) {
        switch(hashCode) {
            case 1: return COMMERCIAL_USE;
            case 0: return NON_COMMERCIAL_USE;
            default: return COMMERCIAL_USE;
        }
    }
}
