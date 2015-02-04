package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDropFolderType {
    LOCAL ("1");

    String hashCode;

    KalturaDropFolderType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaDropFolderType get(String hashCode) {
        if (hashCode.equals("1"))
        {
           return LOCAL;
        }
        else 
        {
           return LOCAL;
        }
    }
}
