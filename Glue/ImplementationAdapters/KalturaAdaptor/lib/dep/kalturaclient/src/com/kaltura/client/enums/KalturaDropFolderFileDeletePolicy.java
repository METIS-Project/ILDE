package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDropFolderFileDeletePolicy {
    MANUAL_DELETE (1),
    AUTO_DELETE (2);

    int hashCode;

    KalturaDropFolderFileDeletePolicy(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaDropFolderFileDeletePolicy get(int hashCode) {
        switch(hashCode) {
            case 1: return MANUAL_DELETE;
            case 2: return AUTO_DELETE;
            default: return MANUAL_DELETE;
        }
    }
}
