package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaEntryDistributionOrderBy {
    CREATED_AT_ASC ("+createdAt"),
    CREATED_AT_DESC ("-createdAt"),
    UPDATED_AT_ASC ("+updatedAt"),
    UPDATED_AT_DESC ("-updatedAt"),
    SUBMITTED_AT_ASC ("+submittedAt"),
    SUBMITTED_AT_DESC ("-submittedAt"),
    SUNRISE_ASC ("+sunrise"),
    SUNRISE_DESC ("-sunrise"),
    SUNSET_ASC ("+sunset"),
    SUNSET_DESC ("-sunset");

    String hashCode;

    KalturaEntryDistributionOrderBy(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaEntryDistributionOrderBy get(String hashCode) {
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
        if (hashCode.equals("+submittedAt"))
        {
           return SUBMITTED_AT_ASC;
        }
        else 
        if (hashCode.equals("-submittedAt"))
        {
           return SUBMITTED_AT_DESC;
        }
        else 
        if (hashCode.equals("+sunrise"))
        {
           return SUNRISE_ASC;
        }
        else 
        if (hashCode.equals("-sunrise"))
        {
           return SUNRISE_DESC;
        }
        else 
        if (hashCode.equals("+sunset"))
        {
           return SUNSET_ASC;
        }
        else 
        if (hashCode.equals("-sunset"))
        {
           return SUNSET_DESC;
        }
        else 
        {
           return CREATED_AT_ASC;
        }
    }
}
