package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaBatchJobErrorTypes {
    APP (0),
    RUNTIME (1),
    HTTP (2),
    CURL (3),
    KALTURA_API (4),
    KALTURA_CLIENT (5);

    int hashCode;

    KalturaBatchJobErrorTypes(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaBatchJobErrorTypes get(int hashCode) {
        switch(hashCode) {
            case 0: return APP;
            case 1: return RUNTIME;
            case 2: return HTTP;
            case 3: return CURL;
            case 4: return KALTURA_API;
            case 5: return KALTURA_CLIENT;
            default: return APP;
        }
    }
}
