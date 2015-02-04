package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaSourceType {
    FILE (1),
    WEBCAM (2),
    URL (5),
    SEARCH_PROVIDER (6),
    AKAMAI_LIVE (29);

    int hashCode;

    KalturaSourceType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaSourceType get(int hashCode) {
        switch(hashCode) {
            case 1: return FILE;
            case 2: return WEBCAM;
            case 5: return URL;
            case 6: return SEARCH_PROVIDER;
            case 29: return AKAMAI_LIVE;
            default: return FILE;
        }
    }
}
