package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaSessionType {
    USER (0),
    ADMIN (2);

    int hashCode;

    KalturaSessionType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaSessionType get(int hashCode) {
        switch(hashCode) {
            case 0: return USER;
            case 2: return ADMIN;
            default: return USER;
        }
    }
}
