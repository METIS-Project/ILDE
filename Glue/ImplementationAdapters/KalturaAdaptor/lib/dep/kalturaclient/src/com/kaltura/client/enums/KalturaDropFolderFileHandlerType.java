package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDropFolderFileHandlerType {
    CONTENT ("1"),
    XML ("dropFolderXmlBulkUpload.XML");

    String hashCode;

    KalturaDropFolderFileHandlerType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaDropFolderFileHandlerType get(String hashCode) {
        if (hashCode.equals("1"))
        {
           return CONTENT;
        }
        else 
        if (hashCode.equals("dropFolderXmlBulkUpload.XML"))
        {
           return XML;
        }
        else 
        {
           return CONTENT;
        }
    }
}
