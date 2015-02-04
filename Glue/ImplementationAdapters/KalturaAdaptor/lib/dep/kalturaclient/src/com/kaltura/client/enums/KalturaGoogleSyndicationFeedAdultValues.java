package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaGoogleSyndicationFeedAdultValues {
    YES ("Yes"),
    NO ("No");

    String hashCode;

    KalturaGoogleSyndicationFeedAdultValues(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaGoogleSyndicationFeedAdultValues get(String hashCode) {
        if (hashCode.equals("Yes"))
        {
           return YES;
        }
        else 
        if (hashCode.equals("No"))
        {
           return NO;
        }
        else 
        {
           return YES;
        }
    }
}
