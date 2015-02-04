package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaCategoryOrderBy {
    DEPTH_ASC ("+depth"),
    DEPTH_DESC ("-depth"),
    FULL_NAME_ASC ("+fullName"),
    FULL_NAME_DESC ("-fullName"),
    CREATED_AT_ASC ("+createdAt"),
    CREATED_AT_DESC ("-createdAt");

    String hashCode;

    KalturaCategoryOrderBy(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaCategoryOrderBy get(String hashCode) {
        if (hashCode.equals("+depth"))
        {
           return DEPTH_ASC;
        }
        else 
        if (hashCode.equals("-depth"))
        {
           return DEPTH_DESC;
        }
        else 
        if (hashCode.equals("+fullName"))
        {
           return FULL_NAME_ASC;
        }
        else 
        if (hashCode.equals("-fullName"))
        {
           return FULL_NAME_DESC;
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
           return DEPTH_ASC;
        }
    }
}
