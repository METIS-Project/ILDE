package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaSearchOperatorType {
    SEARCH_AND (1),
    SEARCH_OR (2);

    int hashCode;

    KalturaSearchOperatorType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaSearchOperatorType get(int hashCode) {
        switch(hashCode) {
            case 1: return SEARCH_AND;
            case 2: return SEARCH_OR;
            default: return SEARCH_AND;
        }
    }
}
