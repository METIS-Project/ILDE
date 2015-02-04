package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaGenericDistributionProviderParser {
    XSL (1),
    XPATH (2),
    REGEX (3);

    int hashCode;

    KalturaGenericDistributionProviderParser(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaGenericDistributionProviderParser get(int hashCode) {
        switch(hashCode) {
            case 1: return XSL;
            case 2: return XPATH;
            case 3: return REGEX;
            default: return XSL;
        }
    }
}
