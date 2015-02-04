package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaModerationFlagType {
    SEXUAL_CONTENT (1),
    VIOLENT_REPULSIVE (2),
    HARMFUL_DANGEROUS (3),
    SPAM_COMMERCIALS (4);

    int hashCode;

    KalturaModerationFlagType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaModerationFlagType get(int hashCode) {
        switch(hashCode) {
            case 1: return SEXUAL_CONTENT;
            case 2: return VIOLENT_REPULSIVE;
            case 3: return HARMFUL_DANGEROUS;
            case 4: return SPAM_COMMERCIALS;
            default: return SEXUAL_CONTENT;
        }
    }
}
