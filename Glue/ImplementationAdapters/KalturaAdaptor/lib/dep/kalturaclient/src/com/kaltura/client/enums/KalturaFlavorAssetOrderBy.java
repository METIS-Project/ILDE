package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaFlavorAssetOrderBy {
    SIZE_ASC ("+size"),
    SIZE_DESC ("-size"),
    CREATED_AT_ASC ("+createdAt"),
    CREATED_AT_DESC ("-createdAt"),
    UPDATED_AT_ASC ("+updatedAt"),
    UPDATED_AT_DESC ("-updatedAt"),
    DELETED_AT_ASC ("+deletedAt"),
    DELETED_AT_DESC ("-deletedAt");

    String hashCode;

    KalturaFlavorAssetOrderBy(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaFlavorAssetOrderBy get(String hashCode) {
        if (hashCode.equals("+size"))
        {
           return SIZE_ASC;
        }
        else 
        if (hashCode.equals("-size"))
        {
           return SIZE_DESC;
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
        if (hashCode.equals("+deletedAt"))
        {
           return DELETED_AT_ASC;
        }
        else 
        if (hashCode.equals("-deletedAt"))
        {
           return DELETED_AT_DESC;
        }
        else 
        {
           return SIZE_ASC;
        }
    }
}
