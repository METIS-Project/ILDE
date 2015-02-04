package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaMetadataStatus {
    VALID (1),
    INVALID (2),
    DELETED (3);

    int hashCode;

    KalturaMetadataStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaMetadataStatus get(int hashCode) {
        switch(hashCode) {
            case 1: return VALID;
            case 2: return INVALID;
            case 3: return DELETED;
            default: return VALID;
        }
    }
}
