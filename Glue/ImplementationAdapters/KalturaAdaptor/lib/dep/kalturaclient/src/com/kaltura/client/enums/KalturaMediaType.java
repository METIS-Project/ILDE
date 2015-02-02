package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaMediaType {
    VIDEO (1),
    IMAGE (2),
    AUDIO (5),
    LIVE_STREAM_FLASH (201),
    LIVE_STREAM_WINDOWS_MEDIA (202),
    LIVE_STREAM_REAL_MEDIA (203),
    LIVE_STREAM_QUICKTIME (204);

    int hashCode;

    KalturaMediaType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaMediaType get(int hashCode) {
        switch(hashCode) {
            case 1: return VIDEO;
            case 2: return IMAGE;
            case 5: return AUDIO;
            case 201: return LIVE_STREAM_FLASH;
            case 202: return LIVE_STREAM_WINDOWS_MEDIA;
            case 203: return LIVE_STREAM_REAL_MEDIA;
            case 204: return LIVE_STREAM_QUICKTIME;
            default: return VIDEO;
        }
    }
}
