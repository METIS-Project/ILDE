package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaUploadTokenStatus {
    PENDING (0),
    PARTIAL_UPLOAD (1),
    FULL_UPLOAD (2),
    CLOSED (3),
    TIMED_OUT (4),
    DELETED (5);

    int hashCode;

    KalturaUploadTokenStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaUploadTokenStatus get(int hashCode) {
        switch(hashCode) {
            case 0: return PENDING;
            case 1: return PARTIAL_UPLOAD;
            case 2: return FULL_UPLOAD;
            case 3: return CLOSED;
            case 4: return TIMED_OUT;
            case 5: return DELETED;
            default: return PENDING;
        }
    }
}
