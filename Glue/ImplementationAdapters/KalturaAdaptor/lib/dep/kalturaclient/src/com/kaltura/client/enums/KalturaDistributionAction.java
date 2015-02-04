package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDistributionAction {
    SUBMIT (1),
    UPDATE (2),
    DELETE (3),
    FETCH_REPORT (4);

    int hashCode;

    KalturaDistributionAction(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaDistributionAction get(int hashCode) {
        switch(hashCode) {
            case 1: return SUBMIT;
            case 2: return UPDATE;
            case 3: return DELETE;
            case 4: return FETCH_REPORT;
            default: return SUBMIT;
        }
    }
}
