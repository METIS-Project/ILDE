package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaGender {
    UNKNOWN (0),
    MALE (1),
    FEMALE (2);

    int hashCode;

    KalturaGender(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaGender get(int hashCode) {
        switch(hashCode) {
            case 0: return UNKNOWN;
            case 1: return MALE;
            case 2: return FEMALE;
            default: return UNKNOWN;
        }
    }
}
