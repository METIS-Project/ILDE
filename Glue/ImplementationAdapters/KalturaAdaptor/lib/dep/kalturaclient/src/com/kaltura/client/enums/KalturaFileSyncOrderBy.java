package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaFileSyncOrderBy {
    CREATED_AT_ASC ("+createdAt"),
    CREATED_AT_DESC ("-createdAt"),
    UPDATED_AT_ASC ("+updatedAt"),
    UPDATED_AT_DESC ("-updatedAt"),
    READY_AT_ASC ("+readyAt"),
    READY_AT_DESC ("-readyAt"),
    SYNC_TIME_ASC ("+syncTime"),
    SYNC_TIME_DESC ("-syncTime"),
    FILE_SIZE_ASC ("+fileSize"),
    FILE_SIZE_DESC ("-fileSize");

    String hashCode;

    KalturaFileSyncOrderBy(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaFileSyncOrderBy get(String hashCode) {
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
        if (hashCode.equals("+readyAt"))
        {
           return READY_AT_ASC;
        }
        else 
        if (hashCode.equals("-readyAt"))
        {
           return READY_AT_DESC;
        }
        else 
        if (hashCode.equals("+syncTime"))
        {
           return SYNC_TIME_ASC;
        }
        else 
        if (hashCode.equals("-syncTime"))
        {
           return SYNC_TIME_DESC;
        }
        else 
        if (hashCode.equals("+fileSize"))
        {
           return FILE_SIZE_ASC;
        }
        else 
        if (hashCode.equals("-fileSize"))
        {
           return FILE_SIZE_DESC;
        }
        else 
        {
           return CREATED_AT_ASC;
        }
    }
}
