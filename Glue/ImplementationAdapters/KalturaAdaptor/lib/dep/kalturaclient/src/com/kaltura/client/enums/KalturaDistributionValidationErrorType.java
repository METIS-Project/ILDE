package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDistributionValidationErrorType {
    CUSTOM_ERROR (0),
    STRING_EMPTY (1),
    STRING_TOO_LONG (2),
    STRING_TOO_SHORT (3),
    INVALID_FORMAT (4);

    int hashCode;

    KalturaDistributionValidationErrorType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaDistributionValidationErrorType get(int hashCode) {
        switch(hashCode) {
            case 0: return CUSTOM_ERROR;
            case 1: return STRING_EMPTY;
            case 2: return STRING_TOO_LONG;
            case 3: return STRING_TOO_SHORT;
            case 4: return INVALID_FORMAT;
            default: return CUSTOM_ERROR;
        }
    }
}
