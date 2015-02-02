package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDocumentType {
    DOCUMENT (11),
    SWF (12),
    PDF (13);

    int hashCode;

    KalturaDocumentType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaDocumentType get(int hashCode) {
        switch(hashCode) {
            case 11: return DOCUMENT;
            case 12: return SWF;
            case 13: return PDF;
            default: return DOCUMENT;
        }
    }
}
