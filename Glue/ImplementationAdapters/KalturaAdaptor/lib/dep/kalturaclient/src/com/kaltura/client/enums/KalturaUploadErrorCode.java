package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaUploadErrorCode {
    NO_ERROR (0),
    GENERAL_ERROR (1),
    PARTIAL_UPLOAD (2);

    int hashCode;

    KalturaUploadErrorCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaUploadErrorCode get(int hashCode) {
        switch(hashCode) {
            case 0: return NO_ERROR;
            case 1: return GENERAL_ERROR;
            case 2: return PARTIAL_UPLOAD;
            default: return NO_ERROR;
        }
    }
}
