package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaUserStatus {
    BLOCKED (0),
    ACTIVE (1),
    DELETED (2);

    int hashCode;

    KalturaUserStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaUserStatus get(int hashCode) {
        switch(hashCode) {
            case 0: return BLOCKED;
            case 1: return ACTIVE;
            case 2: return DELETED;
            default: return BLOCKED;
        }
    }
}
