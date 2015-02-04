package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaModerationObjectType {
    ENTRY (2),
    USER (3);

    int hashCode;

    KalturaModerationObjectType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaModerationObjectType get(int hashCode) {
        switch(hashCode) {
            case 2: return ENTRY;
            case 3: return USER;
            default: return ENTRY;
        }
    }
}
