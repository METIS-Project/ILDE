package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaMetadataProfileStatus {
    ACTIVE (1),
    DEPRECATED (2),
    TRANSFORMING (3);

    int hashCode;

    KalturaMetadataProfileStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaMetadataProfileStatus get(int hashCode) {
        switch(hashCode) {
            case 1: return ACTIVE;
            case 2: return DEPRECATED;
            case 3: return TRANSFORMING;
            default: return ACTIVE;
        }
    }
}
