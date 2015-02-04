package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaYahooSyndicationFeedAdultValues {
    ADULT ("adult"),
    NON_ADULT ("nonadult");

    String hashCode;

    KalturaYahooSyndicationFeedAdultValues(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaYahooSyndicationFeedAdultValues get(String hashCode) {
        if (hashCode.equals("adult"))
        {
           return ADULT;
        }
        else 
        if (hashCode.equals("nonadult"))
        {
           return NON_ADULT;
        }
        else 
        {
           return ADULT;
        }
    }
}
