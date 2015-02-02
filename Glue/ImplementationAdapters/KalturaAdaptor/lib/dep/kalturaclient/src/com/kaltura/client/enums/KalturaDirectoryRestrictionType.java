package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDirectoryRestrictionType {
    DONT_DISPLAY (0),
    DISPLAY_WITH_LINK (1);

    int hashCode;

    KalturaDirectoryRestrictionType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaDirectoryRestrictionType get(int hashCode) {
        switch(hashCode) {
            case 0: return DONT_DISPLAY;
            case 1: return DISPLAY_WITH_LINK;
            default: return DONT_DISPLAY;
        }
    }
}
