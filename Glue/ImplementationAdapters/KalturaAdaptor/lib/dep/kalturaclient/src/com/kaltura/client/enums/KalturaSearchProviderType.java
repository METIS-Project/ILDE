package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaSearchProviderType {
    FLICKR (3),
    YOUTUBE (4),
    MYSPACE (7),
    PHOTOBUCKET (8),
    JAMENDO (9),
    CCMIXTER (10),
    NYPL (11),
    CURRENT (12),
    MEDIA_COMMONS (13),
    KALTURA (20),
    KALTURA_USER_CLIPS (21),
    ARCHIVE_ORG (22),
    KALTURA_PARTNER (23),
    METACAFE (24),
    SEARCH_PROXY (28),
    PARTNER_SPECIFIC (100);

    int hashCode;

    KalturaSearchProviderType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaSearchProviderType get(int hashCode) {
        switch(hashCode) {
            case 3: return FLICKR;
            case 4: return YOUTUBE;
            case 7: return MYSPACE;
            case 8: return PHOTOBUCKET;
            case 9: return JAMENDO;
            case 10: return CCMIXTER;
            case 11: return NYPL;
            case 12: return CURRENT;
            case 13: return MEDIA_COMMONS;
            case 20: return KALTURA;
            case 21: return KALTURA_USER_CLIPS;
            case 22: return ARCHIVE_ORG;
            case 23: return KALTURA_PARTNER;
            case 24: return METACAFE;
            case 28: return SEARCH_PROXY;
            case 100: return PARTNER_SPECIFIC;
            default: return FLICKR;
        }
    }
}
