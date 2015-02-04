package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaUiConfCreationMode {
    WIZARD (2),
    ADVANCED (3);

    int hashCode;

    KalturaUiConfCreationMode(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaUiConfCreationMode get(int hashCode) {
        switch(hashCode) {
            case 2: return WIZARD;
            case 3: return ADVANCED;
            default: return WIZARD;
        }
    }
}
