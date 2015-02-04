package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaITunesSyndicationFeedAdultValues {
    YES ("yes"),
    NO ("no"),
    CLEAN ("clean");

    String hashCode;

    KalturaITunesSyndicationFeedAdultValues(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaITunesSyndicationFeedAdultValues get(String hashCode) {
        if (hashCode.equals("yes"))
        {
           return YES;
        }
        else 
        if (hashCode.equals("no"))
        {
           return NO;
        }
        else 
        if (hashCode.equals("clean"))
        {
           return CLEAN;
        }
        else 
        {
           return YES;
        }
    }
}
