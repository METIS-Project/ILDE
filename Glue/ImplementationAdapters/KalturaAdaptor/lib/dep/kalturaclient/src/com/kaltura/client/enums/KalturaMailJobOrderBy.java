package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaMailJobOrderBy {
    CREATED_AT_ASC ("+createdAt"),
    CREATED_AT_DESC ("-createdAt"),
    UPDATED_AT_ASC ("+updatedAt"),
    UPDATED_AT_DESC ("-updatedAt"),
    PROCESSOR_EXPIRATION_ASC ("+processorExpiration"),
    PROCESSOR_EXPIRATION_DESC ("-processorExpiration"),
    EXECUTION_ATTEMPTS_ASC ("+executionAttempts"),
    EXECUTION_ATTEMPTS_DESC ("-executionAttempts"),
    LOCK_VERSION_ASC ("+lockVersion"),
    LOCK_VERSION_DESC ("-lockVersion");

    String hashCode;

    KalturaMailJobOrderBy(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaMailJobOrderBy get(String hashCode) {
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
        if (hashCode.equals("+processorExpiration"))
        {
           return PROCESSOR_EXPIRATION_ASC;
        }
        else 
        if (hashCode.equals("-processorExpiration"))
        {
           return PROCESSOR_EXPIRATION_DESC;
        }
        else 
        if (hashCode.equals("+executionAttempts"))
        {
           return EXECUTION_ATTEMPTS_ASC;
        }
        else 
        if (hashCode.equals("-executionAttempts"))
        {
           return EXECUTION_ATTEMPTS_DESC;
        }
        else 
        if (hashCode.equals("+lockVersion"))
        {
           return LOCK_VERSION_ASC;
        }
        else 
        if (hashCode.equals("-lockVersion"))
        {
           return LOCK_VERSION_DESC;
        }
        else 
        {
           return CREATED_AT_ASC;
        }
    }
}
