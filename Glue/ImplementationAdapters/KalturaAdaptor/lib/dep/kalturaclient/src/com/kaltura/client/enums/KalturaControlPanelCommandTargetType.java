package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaControlPanelCommandTargetType {
    DATA_CENTER (1),
    SCHEDULER (2),
    JOB_TYPE (3),
    JOB (4),
    BATCH (5);

    int hashCode;

    KalturaControlPanelCommandTargetType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaControlPanelCommandTargetType get(int hashCode) {
        switch(hashCode) {
            case 1: return DATA_CENTER;
            case 2: return SCHEDULER;
            case 3: return JOB_TYPE;
            case 4: return JOB;
            case 5: return BATCH;
            default: return DATA_CENTER;
        }
    }
}
