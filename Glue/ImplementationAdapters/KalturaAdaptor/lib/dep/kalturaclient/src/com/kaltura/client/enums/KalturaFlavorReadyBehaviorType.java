package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaFlavorReadyBehaviorType {
    INHERIT_FLAVOR_PARAMS (0),
    REQUIRED (1),
    OPTIONAL (2);

    int hashCode;

    KalturaFlavorReadyBehaviorType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaFlavorReadyBehaviorType get(int hashCode) {
        switch(hashCode) {
            case 0: return INHERIT_FLAVOR_PARAMS;
            case 1: return REQUIRED;
            case 2: return OPTIONAL;
            default: return INHERIT_FLAVOR_PARAMS;
        }
    }
}
