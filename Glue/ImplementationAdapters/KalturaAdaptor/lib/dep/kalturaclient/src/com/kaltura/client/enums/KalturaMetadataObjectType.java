package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaMetadataObjectType {
    ENTRY (1);

    int hashCode;

    KalturaMetadataObjectType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaMetadataObjectType get(int hashCode) {
        switch(hashCode) {
            case 1: return ENTRY;
            default: return ENTRY;
        }
    }
}
