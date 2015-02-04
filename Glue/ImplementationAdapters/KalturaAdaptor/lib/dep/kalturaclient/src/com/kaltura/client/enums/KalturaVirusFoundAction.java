package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaVirusFoundAction {
    NONE (0),
    DELETE (1),
    CLEAN_NONE (2),
    CLEAN_DELETE (3);

    int hashCode;

    KalturaVirusFoundAction(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaVirusFoundAction get(int hashCode) {
        switch(hashCode) {
            case 0: return NONE;
            case 1: return DELETE;
            case 2: return CLEAN_NONE;
            case 3: return CLEAN_DELETE;
            default: return NONE;
        }
    }
}
