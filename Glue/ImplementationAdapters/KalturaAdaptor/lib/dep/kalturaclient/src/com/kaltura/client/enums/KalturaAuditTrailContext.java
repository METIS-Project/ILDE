package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaAuditTrailContext {
    CLIENT (-1),
    SCRIPT (0),
    PS2 (1),
    API_V3 (2);

    int hashCode;

    KalturaAuditTrailContext(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaAuditTrailContext get(int hashCode) {
        switch(hashCode) {
            case -1: return CLIENT;
            case 0: return SCRIPT;
            case 1: return PS2;
            case 2: return API_V3;
            default: return CLIENT;
        }
    }
}
