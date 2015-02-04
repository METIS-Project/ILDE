package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaBatchJobOrderBy {
    STATUS_ASC ("+status"),
    STATUS_DESC ("-status"),
    CHECK_AGAIN_TIMEOUT_ASC ("+checkAgainTimeout"),
    CHECK_AGAIN_TIMEOUT_DESC ("-checkAgainTimeout"),
    PROGRESS_ASC ("+progress"),
    PROGRESS_DESC ("-progress"),
    UPDATES_COUNT_ASC ("+updatesCount"),
    UPDATES_COUNT_DESC ("-updatesCount"),
    PRIORITY_ASC ("+priority"),
    PRIORITY_DESC ("-priority"),
    QUEUE_TIME_ASC ("+queueTime"),
    QUEUE_TIME_DESC ("-queueTime"),
    FINISH_TIME_ASC ("+finishTime"),
    FINISH_TIME_DESC ("-finishTime"),
    FILE_SIZE_ASC ("+fileSize"),
    FILE_SIZE_DESC ("-fileSize"),
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

    KalturaBatchJobOrderBy(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaBatchJobOrderBy get(String hashCode) {
        if (hashCode.equals("+status"))
        {
           return STATUS_ASC;
        }
        else 
        if (hashCode.equals("-status"))
        {
           return STATUS_DESC;
        }
        else 
        if (hashCode.equals("+checkAgainTimeout"))
        {
           return CHECK_AGAIN_TIMEOUT_ASC;
        }
        else 
        if (hashCode.equals("-checkAgainTimeout"))
        {
           return CHECK_AGAIN_TIMEOUT_DESC;
        }
        else 
        if (hashCode.equals("+progress"))
        {
           return PROGRESS_ASC;
        }
        else 
        if (hashCode.equals("-progress"))
        {
           return PROGRESS_DESC;
        }
        else 
        if (hashCode.equals("+updatesCount"))
        {
           return UPDATES_COUNT_ASC;
        }
        else 
        if (hashCode.equals("-updatesCount"))
        {
           return UPDATES_COUNT_DESC;
        }
        else 
        if (hashCode.equals("+priority"))
        {
           return PRIORITY_ASC;
        }
        else 
        if (hashCode.equals("-priority"))
        {
           return PRIORITY_DESC;
        }
        else 
        if (hashCode.equals("+queueTime"))
        {
           return QUEUE_TIME_ASC;
        }
        else 
        if (hashCode.equals("-queueTime"))
        {
           return QUEUE_TIME_DESC;
        }
        else 
        if (hashCode.equals("+finishTime"))
        {
           return FINISH_TIME_ASC;
        }
        else 
        if (hashCode.equals("-finishTime"))
        {
           return FINISH_TIME_DESC;
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
           return STATUS_ASC;
        }
    }
}
