package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaPermissionType {
    NORMAL (1),
    SPECIAL_FEATURE (2),
    PLUGIN (3),
    PARTNER_GROUP (4);

    int hashCode;

    KalturaPermissionType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaPermissionType get(int hashCode) {
        switch(hashCode) {
            case 1: return NORMAL;
            case 2: return SPECIAL_FEATURE;
            case 3: return PLUGIN;
            case 4: return PARTNER_GROUP;
            default: return NORMAL;
        }
    }
}
