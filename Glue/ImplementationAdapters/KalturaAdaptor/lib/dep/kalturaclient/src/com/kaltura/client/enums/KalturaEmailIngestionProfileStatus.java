package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaEmailIngestionProfileStatus {
    INACTIVE (0),
    ACTIVE (1);

    int hashCode;

    KalturaEmailIngestionProfileStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaEmailIngestionProfileStatus get(int hashCode) {
        switch(hashCode) {
            case 0: return INACTIVE;
            case 1: return ACTIVE;
            default: return INACTIVE;
        }
    }
}
