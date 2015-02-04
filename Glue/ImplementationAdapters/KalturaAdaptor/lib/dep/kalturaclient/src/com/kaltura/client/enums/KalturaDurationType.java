package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDurationType {
    NOT_AVAILABLE ("notavailable"),
    SHORT ("short"),
    MEDIUM ("medium"),
    LONG ("long");

    String hashCode;

    KalturaDurationType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaDurationType get(String hashCode) {
        if (hashCode.equals("notavailable"))
        {
           return NOT_AVAILABLE;
        }
        else 
        if (hashCode.equals("short"))
        {
           return SHORT;
        }
        else 
        if (hashCode.equals("medium"))
        {
           return MEDIUM;
        }
        else 
        if (hashCode.equals("long"))
        {
           return LONG;
        }
        else 
        {
           return NOT_AVAILABLE;
        }
    }
}
