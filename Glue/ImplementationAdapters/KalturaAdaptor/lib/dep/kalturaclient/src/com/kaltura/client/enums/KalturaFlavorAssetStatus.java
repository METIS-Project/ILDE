package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaFlavorAssetStatus {
    ERROR (-1),
    QUEUED (0),
    CONVERTING (1),
    READY (2),
    DELETED (3),
    NOT_APPLICABLE (4),
    TEMP (5),
    WAIT_FOR_CONVERT (6),
    IMPORTING (7),
    VALIDATING (8);

    int hashCode;

    KalturaFlavorAssetStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaFlavorAssetStatus get(int hashCode) {
        switch(hashCode) {
            case -1: return ERROR;
            case 0: return QUEUED;
            case 1: return CONVERTING;
            case 2: return READY;
            case 3: return DELETED;
            case 4: return NOT_APPLICABLE;
            case 5: return TEMP;
            case 6: return WAIT_FOR_CONVERT;
            case 7: return IMPORTING;
            case 8: return VALIDATING;
            default: return ERROR;
        }
    }
}
