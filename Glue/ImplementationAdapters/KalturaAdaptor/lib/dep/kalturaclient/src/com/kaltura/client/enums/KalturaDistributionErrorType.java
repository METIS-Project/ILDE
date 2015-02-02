package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDistributionErrorType {
    MISSING_FLAVOR (1),
    MISSING_THUMBNAIL (2),
    MISSING_METADATA (3),
    INVALID_DATA (4);

    int hashCode;

    KalturaDistributionErrorType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaDistributionErrorType get(int hashCode) {
        switch(hashCode) {
            case 1: return MISSING_FLAVOR;
            case 2: return MISSING_THUMBNAIL;
            case 3: return MISSING_METADATA;
            case 4: return INVALID_DATA;
            default: return MISSING_FLAVOR;
        }
    }
}
