package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaControlPanelCommandType {
    STOP (1),
    START (2),
    CONFIG (3),
    KILL (4);

    int hashCode;

    KalturaControlPanelCommandType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaControlPanelCommandType get(int hashCode) {
        switch(hashCode) {
            case 1: return STOP;
            case 2: return START;
            case 3: return CONFIG;
            case 4: return KILL;
            default: return STOP;
        }
    }
}
