package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaAudioCodec {
    NONE (""),
    MP3 ("mp3"),
    AAC ("aac"),
    VORBIS ("vorbis"),
    WMA ("wma"),
    COPY ("copy");

    String hashCode;

    KalturaAudioCodec(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaAudioCodec get(String hashCode) {
        if (hashCode.equals(""))
        {
           return NONE;
        }
        else 
        if (hashCode.equals("mp3"))
        {
           return MP3;
        }
        else 
        if (hashCode.equals("aac"))
        {
           return AAC;
        }
        else 
        if (hashCode.equals("vorbis"))
        {
           return VORBIS;
        }
        else 
        if (hashCode.equals("wma"))
        {
           return WMA;
        }
        else 
        if (hashCode.equals("copy"))
        {
           return COPY;
        }
        else 
        {
           return NONE;
        }
    }
}
