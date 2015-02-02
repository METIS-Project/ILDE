package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDropFolderContentFileHandlerMatchPolicy {
    ADD_AS_NEW (1),
    MATCH_EXISTING_OR_ADD_AS_NEW (2),
    MATCH_EXISTING_OR_KEEP_IN_FOLDER (3);

    int hashCode;

    KalturaDropFolderContentFileHandlerMatchPolicy(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaDropFolderContentFileHandlerMatchPolicy get(int hashCode) {
        switch(hashCode) {
            case 1: return ADD_AS_NEW;
            case 2: return MATCH_EXISTING_OR_ADD_AS_NEW;
            case 3: return MATCH_EXISTING_OR_KEEP_IN_FOLDER;
            default: return ADD_AS_NEW;
        }
    }
}
