package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaMetadataProfileCreateMode {
    API (1),
    KMC (2);

    int hashCode;

    KalturaMetadataProfileCreateMode(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaMetadataProfileCreateMode get(int hashCode) {
        switch(hashCode) {
            case 1: return API;
            case 2: return KMC;
            default: return API;
        }
    }
}
