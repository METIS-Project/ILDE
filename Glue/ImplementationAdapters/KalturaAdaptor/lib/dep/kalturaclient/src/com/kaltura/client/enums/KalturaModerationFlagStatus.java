package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaModerationFlagStatus {
    PENDING (1),
    MODERATED (2);

    int hashCode;

    KalturaModerationFlagStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaModerationFlagStatus get(int hashCode) {
        switch(hashCode) {
            case 1: return PENDING;
            case 2: return MODERATED;
            default: return PENDING;
        }
    }
}
