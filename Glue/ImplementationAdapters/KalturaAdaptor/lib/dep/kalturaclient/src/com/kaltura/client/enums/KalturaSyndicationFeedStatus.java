package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaSyndicationFeedStatus {
    DELETED (-1),
    ACTIVE (1);

    int hashCode;

    KalturaSyndicationFeedStatus(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaSyndicationFeedStatus get(int hashCode) {
        switch(hashCode) {
            case -1: return DELETED;
            case 1: return ACTIVE;
            default: return DELETED;
        }
    }
}
