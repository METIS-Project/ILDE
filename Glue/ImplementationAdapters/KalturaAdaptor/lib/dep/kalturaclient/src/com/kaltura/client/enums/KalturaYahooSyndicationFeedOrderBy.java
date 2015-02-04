package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaYahooSyndicationFeedOrderBy {
    PLAYLIST_ID_ASC ("+playlistId"),
    PLAYLIST_ID_DESC ("-playlistId"),
    NAME_ASC ("+name"),
    NAME_DESC ("-name"),
    TYPE_ASC ("+type"),
    TYPE_DESC ("-type"),
    CREATED_AT_ASC ("+createdAt"),
    CREATED_AT_DESC ("-createdAt");

    String hashCode;

    KalturaYahooSyndicationFeedOrderBy(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaYahooSyndicationFeedOrderBy get(String hashCode) {
        if (hashCode.equals("+playlistId"))
        {
           return PLAYLIST_ID_ASC;
        }
        else 
        if (hashCode.equals("-playlistId"))
        {
           return PLAYLIST_ID_DESC;
        }
        else 
        if (hashCode.equals("+name"))
        {
           return NAME_ASC;
        }
        else 
        if (hashCode.equals("-name"))
        {
           return NAME_DESC;
        }
        else 
        if (hashCode.equals("+type"))
        {
           return TYPE_ASC;
        }
        else 
        if (hashCode.equals("-type"))
        {
           return TYPE_DESC;
        }
        else 
        if (hashCode.equals("+createdAt"))
        {
           return CREATED_AT_ASC;
        }
        else 
        if (hashCode.equals("-createdAt"))
        {
           return CREATED_AT_DESC;
        }
        else 
        {
           return PLAYLIST_ID_ASC;
        }
    }
}
