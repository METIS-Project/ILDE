package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaPlaylistType {
    DYNAMIC (10),
    STATIC_LIST (3),
    EXTERNAL (101);

    int hashCode;

    KalturaPlaylistType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaPlaylistType get(int hashCode) {
        switch(hashCode) {
            case 10: return DYNAMIC;
            case 3: return STATIC_LIST;
            case 101: return EXTERNAL;
            default: return DYNAMIC;
        }
    }
}
