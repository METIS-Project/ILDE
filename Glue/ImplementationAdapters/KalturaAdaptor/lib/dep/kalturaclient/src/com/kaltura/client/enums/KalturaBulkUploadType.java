package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaBulkUploadType {
    CSV ("bulkUploadCsv.CSV"),
    XML ("bulkUploadXml.XML"),
    DROP_FOLDER_XML ("dropFolderXmlBulkUpload.DROP_FOLDER_XML");

    String hashCode;

    KalturaBulkUploadType(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaBulkUploadType get(String hashCode) {
        if (hashCode.equals("bulkUploadCsv.CSV"))
        {
           return CSV;
        }
        else 
        if (hashCode.equals("bulkUploadXml.XML"))
        {
           return XML;
        }
        else 
        if (hashCode.equals("dropFolderXmlBulkUpload.DROP_FOLDER_XML"))
        {
           return DROP_FOLDER_XML;
        }
        else 
        {
           return CSV;
        }
    }
}
