package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaAuditTrailOrderBy {
    CREATED_AT_ASC ("+createdAt"),
    CREATED_AT_DESC ("-createdAt"),
    PARSED_AT_ASC ("+parsedAt"),
    PARSED_AT_DESC ("-parsedAt");

    String hashCode;

    KalturaAuditTrailOrderBy(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaAuditTrailOrderBy get(String hashCode) {
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
        if (hashCode.equals("+parsedAt"))
        {
           return PARSED_AT_ASC;
        }
        else 
        if (hashCode.equals("-parsedAt"))
        {
           return PARSED_AT_DESC;
        }
        else 
        {
           return CREATED_AT_ASC;
        }
    }
}
