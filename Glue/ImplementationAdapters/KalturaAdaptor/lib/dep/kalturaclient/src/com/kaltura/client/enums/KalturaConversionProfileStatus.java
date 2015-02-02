package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaConversionProfileStatus {
    DISABLED ("1"),
    ENABLED ("2"),
    DELETED ("3");

    String hashCode;

    KalturaConversionProfileStatus(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaConversionProfileStatus get(String hashCode) {
        if (hashCode.equals("1"))
        {
           return DISABLED;
        }
        else 
        if (hashCode.equals("2"))
        {
           return ENABLED;
        }
        else 
        if (hashCode.equals("3"))
        {
           return DELETED;
        }
        else 
        {
           return DISABLED;
        }
    }
}
