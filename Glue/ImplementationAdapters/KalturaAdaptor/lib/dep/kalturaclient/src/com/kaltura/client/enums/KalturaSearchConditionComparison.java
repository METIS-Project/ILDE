package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaSearchConditionComparison {
    EQUEL (1),
    GREATER_THAN (2),
    GREATER_THAN_OR_EQUEL (3),
    LESS_THAN (4),
    LESS_THAN_OR_EQUEL (5);

    int hashCode;

    KalturaSearchConditionComparison(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaSearchConditionComparison get(int hashCode) {
        switch(hashCode) {
            case 1: return EQUEL;
            case 2: return GREATER_THAN;
            case 3: return GREATER_THAN_OR_EQUEL;
            case 4: return LESS_THAN;
            case 5: return LESS_THAN_OR_EQUEL;
            default: return EQUEL;
        }
    }
}
