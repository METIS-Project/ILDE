package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaReportType {
    TOP_CONTENT (1),
    CONTENT_DROPOFF (2),
    CONTENT_INTERACTIONS (3),
    MAP_OVERLAY (4),
    TOP_CONTRIBUTORS (5),
    TOP_SYNDICATION (6),
    CONTENT_CONTRIBUTIONS (7);

    int hashCode;

    KalturaReportType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaReportType get(int hashCode) {
        switch(hashCode) {
            case 1: return TOP_CONTENT;
            case 2: return CONTENT_DROPOFF;
            case 3: return CONTENT_INTERACTIONS;
            case 4: return MAP_OVERLAY;
            case 5: return TOP_CONTRIBUTORS;
            case 6: return TOP_SYNDICATION;
            case 7: return CONTENT_CONTRIBUTIONS;
            default: return TOP_CONTENT;
        }
    }
}
