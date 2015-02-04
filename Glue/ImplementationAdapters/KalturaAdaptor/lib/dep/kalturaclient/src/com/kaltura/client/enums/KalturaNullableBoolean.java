package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaNullableBoolean {
    NULL_VALUE (-1),
    FALSE_VALUE (0),
    TRUE_VALUE (1);

    int hashCode;

    KalturaNullableBoolean(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaNullableBoolean get(int hashCode) {
        switch(hashCode) {
            case -1: return NULL_VALUE;
            case 0: return FALSE_VALUE;
            case 1: return TRUE_VALUE;
            default: return NULL_VALUE;
        }
    }
}
