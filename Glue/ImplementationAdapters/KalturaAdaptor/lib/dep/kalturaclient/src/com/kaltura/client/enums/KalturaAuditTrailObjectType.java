package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaAuditTrailObjectType {
    ACCESS_CONTROL ("accessControl"),
    ADMIN_KUSER ("adminKuser"),
    BATCH_JOB ("BatchJob"),
    CATEGORY ("category"),
    CONVERSION_PROFILE_2 ("conversionProfile2"),
    EMAIL_INGESTION_PROFILE ("EmailIngestionProfile"),
    ENTRY ("entry"),
    FILE_SYNC ("FileSync"),
    FLAVOR_ASSET ("flavorAsset"),
    FLAVOR_PARAMS ("flavorParams"),
    FLAVOR_PARAMS_CONVERSION_PROFILE ("flavorParamsConversionProfile"),
    FLAVOR_PARAMS_OUTPUT ("flavorParamsOutput"),
    KSHOW ("kshow"),
    KSHOW_KUSER ("KshowKuser"),
    KUSER ("kuser"),
    MEDIA_INFO ("mediaInfo"),
    MODERATION ("moderation"),
    PARTNER ("Partner"),
    ROUGHCUT ("roughcutEntry"),
    SYNDICATION ("syndicationFeed"),
    UI_CONF ("uiConf"),
    UPLOAD_TOKEN ("UploadToken"),
    WIDGET ("widget"),
    METADATA ("Metadata"),
    METADATA_PROFILE ("MetadataProfile"),
    USER_LOGIN_DATA ("UserLoginData"),
    USER_ROLE ("UserRole"),
    PERMISSION ("Permission");

    String hashCode;

    KalturaAuditTrailObjectType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaAuditTrailObjectType get(String hashCode) {
        if (hashCode.equals("accessControl"))
        {
           return ACCESS_CONTROL;
        }
        else 
        if (hashCode.equals("adminKuser"))
        {
           return ADMIN_KUSER;
        }
        else 
        if (hashCode.equals("BatchJob"))
        {
           return BATCH_JOB;
        }
        else 
        if (hashCode.equals("category"))
        {
           return CATEGORY;
        }
        else 
        if (hashCode.equals("conversionProfile2"))
        {
           return CONVERSION_PROFILE_2;
        }
        else 
        if (hashCode.equals("EmailIngestionProfile"))
        {
           return EMAIL_INGESTION_PROFILE;
        }
        else 
        if (hashCode.equals("entry"))
        {
           return ENTRY;
        }
        else 
        if (hashCode.equals("FileSync"))
        {
           return FILE_SYNC;
        }
        else 
        if (hashCode.equals("flavorAsset"))
        {
           return FLAVOR_ASSET;
        }
        else 
        if (hashCode.equals("flavorParams"))
        {
           return FLAVOR_PARAMS;
        }
        else 
        if (hashCode.equals("flavorParamsConversionProfile"))
        {
           return FLAVOR_PARAMS_CONVERSION_PROFILE;
        }
        else 
        if (hashCode.equals("flavorParamsOutput"))
        {
           return FLAVOR_PARAMS_OUTPUT;
        }
        else 
        if (hashCode.equals("kshow"))
        {
           return KSHOW;
        }
        else 
        if (hashCode.equals("KshowKuser"))
        {
           return KSHOW_KUSER;
        }
        else 
        if (hashCode.equals("kuser"))
        {
           return KUSER;
        }
        else 
        if (hashCode.equals("mediaInfo"))
        {
           return MEDIA_INFO;
        }
        else 
        if (hashCode.equals("moderation"))
        {
           return MODERATION;
        }
        else 
        if (hashCode.equals("Partner"))
        {
           return PARTNER;
        }
        else 
        if (hashCode.equals("roughcutEntry"))
        {
           return ROUGHCUT;
        }
        else 
        if (hashCode.equals("syndicationFeed"))
        {
           return SYNDICATION;
        }
        else 
        if (hashCode.equals("uiConf"))
        {
           return UI_CONF;
        }
        else 
        if (hashCode.equals("UploadToken"))
        {
           return UPLOAD_TOKEN;
        }
        else 
        if (hashCode.equals("widget"))
        {
           return WIDGET;
        }
        else 
        if (hashCode.equals("Metadata"))
        {
           return METADATA;
        }
        else 
        if (hashCode.equals("MetadataProfile"))
        {
           return METADATA_PROFILE;
        }
        else 
        if (hashCode.equals("UserLoginData"))
        {
           return USER_LOGIN_DATA;
        }
        else 
        if (hashCode.equals("UserRole"))
        {
           return USER_ROLE;
        }
        else 
        if (hashCode.equals("Permission"))
        {
           return PERMISSION;
        }
        else 
        {
           return ACCESS_CONTROL;
        }
    }
}
