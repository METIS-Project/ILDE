package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDropFolderFileOrderBy {
    ID_ASC ("+id"),
    ID_DESC ("-id"),
    FILE_NAME_ASC ("+fileName"),
    FILE_NAME_DESC ("-fileName"),
    FILE_SIZE_ASC ("+fileSize"),
    FILE_SIZE_DESC ("-fileSize"),
    FILE_SIZE_LAST_SET_AT_ASC ("+fileSizeLastSetAt"),
    FILE_SIZE_LAST_SET_AT_DESC ("-fileSizeLastSetAt"),
    PARSED_SLUG_ASC ("+parsedSlug"),
    PARSED_SLUG_DESC ("-parsedSlug"),
    PARSED_FLAVOR_ASC ("+parsedFlavor"),
    PARSED_FLAVOR_DESC ("-parsedFlavor"),
    CREATED_AT_ASC ("+createdAt"),
    CREATED_AT_DESC ("-createdAt"),
    UPDATED_AT_ASC ("+updatedAt"),
    UPDATED_AT_DESC ("-updatedAt");

    String hashCode;

    KalturaDropFolderFileOrderBy(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaDropFolderFileOrderBy get(String hashCode) {
        if (hashCode.equals("+id"))
        {
           return ID_ASC;
        }
        else 
        if (hashCode.equals("-id"))
        {
           return ID_DESC;
        }
        else 
        if (hashCode.equals("+fileName"))
        {
           return FILE_NAME_ASC;
        }
        else 
        if (hashCode.equals("-fileName"))
        {
           return FILE_NAME_DESC;
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
        if (hashCode.equals("+fileSizeLastSetAt"))
        {
           return FILE_SIZE_LAST_SET_AT_ASC;
        }
        else 
        if (hashCode.equals("-fileSizeLastSetAt"))
        {
           return FILE_SIZE_LAST_SET_AT_DESC;
        }
        else 
        if (hashCode.equals("+parsedSlug"))
        {
           return PARSED_SLUG_ASC;
        }
        else 
        if (hashCode.equals("-parsedSlug"))
        {
           return PARSED_SLUG_DESC;
        }
        else 
        if (hashCode.equals("+parsedFlavor"))
        {
           return PARSED_FLAVOR_ASC;
        }
        else 
        if (hashCode.equals("-parsedFlavor"))
        {
           return PARSED_FLAVOR_DESC;
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
           return ID_ASC;
        }
    }
}
