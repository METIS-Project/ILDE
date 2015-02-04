package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaEntryReplacementStatus {
    NONE ("0"),
    APPROVED_BUT_NOT_READY ("1"),
    READY_BUT_NOT_APPROVED ("2"),
    NOT_READY_AND_NOT_APPROVED ("3");

    String hashCode;

    KalturaEntryReplacementStatus(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaEntryReplacementStatus get(String hashCode) {
        if (hashCode.equals("0"))
        {
           return NONE;
        }
        else 
        if (hashCode.equals("1"))
        {
           return APPROVED_BUT_NOT_READY;
        }
        else 
        if (hashCode.equals("2"))
        {
           return READY_BUT_NOT_APPROVED;
        }
        else 
        if (hashCode.equals("3"))
        {
           return NOT_READY_AND_NOT_APPROVED;
        }
        else 
        {
           return NONE;
        }
    }
}
