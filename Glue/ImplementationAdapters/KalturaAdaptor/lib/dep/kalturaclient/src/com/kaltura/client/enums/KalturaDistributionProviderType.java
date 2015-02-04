package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDistributionProviderType {
    GENERIC ("1"),
    SYNDICATION ("2"),
    YOUTUBE ("youTubeDistribution.YOUTUBE"),
    YOUTUBE_API ("youtubeApiDistribution.YOUTUBE_API"),
    DAILYMOTION ("dailymotionDistribution.DAILYMOTION"),
    PODCAST ("podcastDistribution.PODCAST"),
    TVCOM ("tvComDistribution.TVCOM");

    String hashCode;

    KalturaDistributionProviderType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaDistributionProviderType get(String hashCode) {
        if (hashCode.equals("1"))
        {
           return GENERIC;
        }
        else 
        if (hashCode.equals("2"))
        {
           return SYNDICATION;
        }
        else 
        if (hashCode.equals("youTubeDistribution.YOUTUBE"))
        {
           return YOUTUBE;
        }
        else 
        if (hashCode.equals("youtubeApiDistribution.YOUTUBE_API"))
        {
           return YOUTUBE_API;
        }
        else 
        if (hashCode.equals("dailymotionDistribution.DAILYMOTION"))
        {
           return DAILYMOTION;
        }
        else 
        if (hashCode.equals("podcastDistribution.PODCAST"))
        {
           return PODCAST;
        }
        else 
        if (hashCode.equals("tvComDistribution.TVCOM"))
        {
           return TVCOM;
        }
        else 
        {
           return GENERIC;
        }
    }
}
