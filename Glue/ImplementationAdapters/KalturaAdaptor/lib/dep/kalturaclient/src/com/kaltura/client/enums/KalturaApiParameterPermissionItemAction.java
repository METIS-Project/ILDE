package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaApiParameterPermissionItemAction {
    READ ("read"),
    UPDATE ("update"),
    INSERT ("insert");

    String hashCode;

    KalturaApiParameterPermissionItemAction(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaApiParameterPermissionItemAction get(String hashCode) {
        if (hashCode.equals("read"))
        {
           return READ;
        }
        else 
        if (hashCode.equals("update"))
        {
           return UPDATE;
        }
        else 
        if (hashCode.equals("insert"))
        {
           return INSERT;
        }
        else 
        {
           return READ;
        }
    }
}
