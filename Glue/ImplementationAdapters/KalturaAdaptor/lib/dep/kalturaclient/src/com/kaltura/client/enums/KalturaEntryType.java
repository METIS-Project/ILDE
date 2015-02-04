package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaEntryType {
    AUTOMATIC ("-1"),
    MEDIA_CLIP ("1"),
    MIX ("2"),
    PLAYLIST ("5"),
    DATA ("6"),
    LIVE_STREAM ("7"),
    DOCUMENT ("10");

    String hashCode;

    KalturaEntryType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaEntryType get(String hashCode) {
        if (hashCode.equals("-1"))
        {
           return AUTOMATIC;
        }
        else 
        if (hashCode.equals("1"))
        {
           return MEDIA_CLIP;
        }
        else 
        if (hashCode.equals("2"))
        {
           return MIX;
        }
        else 
        if (hashCode.equals("5"))
        {
           return PLAYLIST;
        }
        else 
        if (hashCode.equals("6"))
        {
           return DATA;
        }
        else 
        if (hashCode.equals("7"))
        {
           return LIVE_STREAM;
        }
        else 
        if (hashCode.equals("10"))
        {
           return DOCUMENT;
        }
        else 
        {
           return AUTOMATIC;
        }
    }
}
