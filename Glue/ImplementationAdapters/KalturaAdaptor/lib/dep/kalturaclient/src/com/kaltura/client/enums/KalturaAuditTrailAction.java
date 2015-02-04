package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaAuditTrailAction {
    CREATED ("CREATED"),
    COPIED ("COPIED"),
    CHANGED ("CHANGED"),
    DELETED ("DELETED"),
    VIEWED ("VIEWED"),
    CONTENT_VIEWED ("CONTENT_VIEWED"),
    FILE_SYNC_CREATED ("FILE_SYNC_CREATED"),
    RELATION_ADDED ("RELATION_ADDED"),
    RELATION_REMOVED ("RELATION_REMOVED");

    String hashCode;

    KalturaAuditTrailAction(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaAuditTrailAction get(String hashCode) {
        if (hashCode.equals("CREATED"))
        {
           return CREATED;
        }
        else 
        if (hashCode.equals("COPIED"))
        {
           return COPIED;
        }
        else 
        if (hashCode.equals("CHANGED"))
        {
           return CHANGED;
        }
        else 
        if (hashCode.equals("DELETED"))
        {
           return DELETED;
        }
        else 
        if (hashCode.equals("VIEWED"))
        {
           return VIEWED;
        }
        else 
        if (hashCode.equals("CONTENT_VIEWED"))
        {
           return CONTENT_VIEWED;
        }
        else 
        if (hashCode.equals("FILE_SYNC_CREATED"))
        {
           return FILE_SYNC_CREATED;
        }
        else 
        if (hashCode.equals("RELATION_ADDED"))
        {
           return RELATION_ADDED;
        }
        else 
        if (hashCode.equals("RELATION_REMOVED"))
        {
           return RELATION_REMOVED;
        }
        else 
        {
           return CREATED;
        }
    }
}
