package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaMetadataOrderBy {
    METADATA_PROFILE_VERSION_ASC ("+metadataProfileVersion"),
    METADATA_PROFILE_VERSION_DESC ("-metadataProfileVersion"),
    VERSION_ASC ("+version"),
    VERSION_DESC ("-version"),
    CREATED_AT_ASC ("+createdAt"),
    CREATED_AT_DESC ("-createdAt"),
    UPDATED_AT_ASC ("+updatedAt"),
    UPDATED_AT_DESC ("-updatedAt");

    String hashCode;

    KalturaMetadataOrderBy(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaMetadataOrderBy get(String hashCode) {
        if (hashCode.equals("+metadataProfileVersion"))
        {
           return METADATA_PROFILE_VERSION_ASC;
        }
        else 
        if (hashCode.equals("-metadataProfileVersion"))
        {
           return METADATA_PROFILE_VERSION_DESC;
        }
        else 
        if (hashCode.equals("+version"))
        {
           return VERSION_ASC;
        }
        else 
        if (hashCode.equals("-version"))
        {
           return VERSION_DESC;
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
        if (hashCode.equals("+updatedAt"))
        {
           return UPDATED_AT_ASC;
        }
        else 
        if (hashCode.equals("-updatedAt"))
        {
           return UPDATED_AT_DESC;
        }
        else 
        {
           return METADATA_PROFILE_VERSION_ASC;
        }
    }
}
