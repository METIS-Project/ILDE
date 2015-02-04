package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaAssetParamsOrigin {
    CONVERT (0),
    INGEST (1),
    CONVERT_WHEN_MISSING (2);

    int hashCode;

    KalturaAssetParamsOrigin(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaAssetParamsOrigin get(int hashCode) {
        switch(hashCode) {
            case 0: return CONVERT;
            case 1: return INGEST;
            case 2: return CONVERT_WHEN_MISSING;
            default: return CONVERT;
        }
    }
}
