package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaPermissionItemType {
    API_ACTION_ITEM ("kApiActionPermissionItem"),
    API_PARAMETER_ITEM ("kApiParameterPermissionItem");

    String hashCode;

    KalturaPermissionItemType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaPermissionItemType get(String hashCode) {
        if (hashCode.equals("kApiActionPermissionItem"))
        {
           return API_ACTION_ITEM;
        }
        else 
        if (hashCode.equals("kApiParameterPermissionItem"))
        {
           return API_PARAMETER_ITEM;
        }
        else 
        {
           return API_ACTION_ITEM;
        }
    }
}
