package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaEntryDistributionFlag {
    NONE (0),
    SUBMIT_REQUIRED (1),
    DELETE_REQUIRED (2),
    UPDATE_REQUIRED (3),
    ENABLE_REQUIRED (4),
    DISABLE_REQUIRED (5);

    int hashCode;

    KalturaEntryDistributionFlag(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaEntryDistributionFlag get(int hashCode) {
        switch(hashCode) {
            case 0: return NONE;
            case 1: return SUBMIT_REQUIRED;
            case 2: return DELETE_REQUIRED;
            case 3: return UPDATE_REQUIRED;
            case 4: return ENABLE_REQUIRED;
            case 5: return DISABLE_REQUIRED;
            default: return NONE;
        }
    }
}
