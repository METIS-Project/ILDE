package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaFileSyncObjectType {
    ENTRY ("1"),
    UICONF ("2"),
    BATCHJOB ("3"),
    FLAVOR_ASSET ("4"),
    METADATA ("5"),
    METADATA_PROFILE ("6"),
    SYNDICATION_FEED ("7"),
    CONVERSION_PROFILE ("8"),
    GENERIC_DISTRIBUTION_ACTION ("contentDistribution.GenericDistributionAction"),
    ENTRY_DISTRIBUTION ("contentDistribution.EntryDistribution"),
    DISTRIBUTION_PROFILE ("contentDistribution.DistributionProfile");

    String hashCode;

    KalturaFileSyncObjectType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaFileSyncObjectType get(String hashCode) {
        if (hashCode.equals("1"))
        {
           return ENTRY;
        }
        else 
        if (hashCode.equals("2"))
        {
           return UICONF;
        }
        else 
        if (hashCode.equals("3"))
        {
           return BATCHJOB;
        }
        else 
        if (hashCode.equals("4"))
        {
           return FLAVOR_ASSET;
        }
        else 
        if (hashCode.equals("5"))
        {
           return METADATA;
        }
        else 
        if (hashCode.equals("6"))
        {
           return METADATA_PROFILE;
        }
        else 
        if (hashCode.equals("7"))
        {
           return SYNDICATION_FEED;
        }
        else 
        if (hashCode.equals("8"))
        {
           return CONVERSION_PROFILE;
        }
        else 
        if (hashCode.equals("contentDistribution.GenericDistributionAction"))
        {
           return GENERIC_DISTRIBUTION_ACTION;
        }
        else 
        if (hashCode.equals("contentDistribution.EntryDistribution"))
        {
           return ENTRY_DISTRIBUTION;
        }
        else 
        if (hashCode.equals("contentDistribution.DistributionProfile"))
        {
           return DISTRIBUTION_PROFILE;
        }
        else 
        {
           return ENTRY;
        }
    }
}
