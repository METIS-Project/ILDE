package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDropFolderFileErrorCode {
    ERROR_UPDATE_ENTRY ("1"),
    ERROR_ADD_ENTRY ("2"),
    FLAVOR_NOT_FOUND ("3"),
    FLAVOR_MISSING_IN_FILE_NAME ("4"),
    SLUG_REGEX_NO_MATCH ("5"),
    ERROR_READING_FILE ("6"),
    LOCAL_FILE_WRONG_SIZE ("dropFolderXmlBulkUpload.LOCAL_FILE_WRONG_SIZE"),
    LOCAL_FILE_WRONG_CHECKSUM ("dropFolderXmlBulkUpload.LOCAL_FILE_WRONG_CHECKSUM"),
    ERROR_WRITING_TEMP_FILE ("dropFolderXmlBulkUpload.ERROR_WRITING_TEMP_FILE"),
    ERROR_ADDING_BULK_UPLOAD ("dropFolderXmlBulkUpload.ERROR_ADDING_BULK_UPLOAD");

    String hashCode;

    KalturaDropFolderFileErrorCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaDropFolderFileErrorCode get(String hashCode) {
        if (hashCode.equals("1"))
        {
           return ERROR_UPDATE_ENTRY;
        }
        else 
        if (hashCode.equals("2"))
        {
           return ERROR_ADD_ENTRY;
        }
        else 
        if (hashCode.equals("3"))
        {
           return FLAVOR_NOT_FOUND;
        }
        else 
        if (hashCode.equals("4"))
        {
           return FLAVOR_MISSING_IN_FILE_NAME;
        }
        else 
        if (hashCode.equals("5"))
        {
           return SLUG_REGEX_NO_MATCH;
        }
        else 
        if (hashCode.equals("6"))
        {
           return ERROR_READING_FILE;
        }
        else 
        if (hashCode.equals("dropFolderXmlBulkUpload.LOCAL_FILE_WRONG_SIZE"))
        {
           return LOCAL_FILE_WRONG_SIZE;
        }
        else 
        if (hashCode.equals("dropFolderXmlBulkUpload.LOCAL_FILE_WRONG_CHECKSUM"))
        {
           return LOCAL_FILE_WRONG_CHECKSUM;
        }
        else 
        if (hashCode.equals("dropFolderXmlBulkUpload.ERROR_WRITING_TEMP_FILE"))
        {
           return ERROR_WRITING_TEMP_FILE;
        }
        else 
        if (hashCode.equals("dropFolderXmlBulkUpload.ERROR_ADDING_BULK_UPLOAD"))
        {
           return ERROR_ADDING_BULK_UPLOAD;
        }
        else 
        {
           return ERROR_UPDATE_ENTRY;
        }
    }
}
