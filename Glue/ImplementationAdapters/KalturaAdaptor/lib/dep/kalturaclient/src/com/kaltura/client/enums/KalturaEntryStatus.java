package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaEntryStatus {
    ERROR_IMPORTING ("-2"),
    ERROR_CONVERTING ("-1"),
    IMPORT ("0"),
    PRECONVERT ("1"),
    READY ("2"),
    DELETED ("3"),
    PENDING ("4"),
    MODERATE ("5"),
    BLOCKED ("6"),
    NO_CONTENT ("7"),
    INFECTED ("virusScan.Infected");

    String hashCode;

    KalturaEntryStatus(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaEntryStatus get(String hashCode) {
        if (hashCode.equals("-2"))
        {
           return ERROR_IMPORTING;
        }
        else 
        if (hashCode.equals("-1"))
        {
           return ERROR_CONVERTING;
        }
        else 
        if (hashCode.equals("0"))
        {
           return IMPORT;
        }
        else 
        if (hashCode.equals("1"))
        {
           return PRECONVERT;
        }
        else 
        if (hashCode.equals("2"))
        {
           return READY;
        }
        else 
        if (hashCode.equals("3"))
        {
           return DELETED;
        }
        else 
        if (hashCode.equals("4"))
        {
           return PENDING;
        }
        else 
        if (hashCode.equals("5"))
        {
           return MODERATE;
        }
        else 
        if (hashCode.equals("6"))
        {
           return BLOCKED;
        }
        else 
        if (hashCode.equals("7"))
        {
           return NO_CONTENT;
        }
        else 
        if (hashCode.equals("virusScan.Infected"))
        {
           return INFECTED;
        }
        else 
        {
           return ERROR_IMPORTING;
        }
    }
}
