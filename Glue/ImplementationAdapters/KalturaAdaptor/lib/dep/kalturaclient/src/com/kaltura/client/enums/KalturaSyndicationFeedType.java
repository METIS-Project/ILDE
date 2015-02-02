package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaSyndicationFeedType {
    GOOGLE_VIDEO (1),
    YAHOO (2),
    ITUNES (3),
    TUBE_MOGUL (4),
    KALTURA (5),
    KALTURA_XSLT (6);

    int hashCode;

    KalturaSyndicationFeedType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaSyndicationFeedType get(int hashCode) {
        switch(hashCode) {
            case 1: return GOOGLE_VIDEO;
            case 2: return YAHOO;
            case 3: return ITUNES;
            case 4: return TUBE_MOGUL;
            case 5: return KALTURA;
            case 6: return KALTURA_XSLT;
            default: return GOOGLE_VIDEO;
        }
    }
}
