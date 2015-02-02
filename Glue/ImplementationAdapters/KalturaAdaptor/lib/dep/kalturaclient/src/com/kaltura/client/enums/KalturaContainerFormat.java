package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaContainerFormat {
    FLV ("flv"),
    MP4 ("mp4"),
    AVI ("avi"),
    MOV ("mov"),
    MP3 ("mp3"),
    _3GP ("3gp"),
    OGG ("ogg"),
    WMV ("wmv"),
    WMA ("wma"),
    ISMV ("ismv"),
    MKV ("mkv"),
    WEBM ("webm"),
    MPEG ("mpeg"),
    MPEGTS ("mpegts"),
    APPLEHTTP ("applehttp"),
    SWF ("swf"),
    PDF ("pdf"),
    JPG ("jpg");

    String hashCode;

    KalturaContainerFormat(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaContainerFormat get(String hashCode) {
        if (hashCode.equals("flv"))
        {
           return FLV;
        }
        else 
        if (hashCode.equals("mp4"))
        {
           return MP4;
        }
        else 
        if (hashCode.equals("avi"))
        {
           return AVI;
        }
        else 
        if (hashCode.equals("mov"))
        {
           return MOV;
        }
        else 
        if (hashCode.equals("mp3"))
        {
           return MP3;
        }
        else 
        if (hashCode.equals("3gp"))
        {
           return _3GP;
        }
        else 
        if (hashCode.equals("ogg"))
        {
           return OGG;
        }
        else 
        if (hashCode.equals("wmv"))
        {
           return WMV;
        }
        else 
        if (hashCode.equals("wma"))
        {
           return WMA;
        }
        else 
        if (hashCode.equals("ismv"))
        {
           return ISMV;
        }
        else 
        if (hashCode.equals("mkv"))
        {
           return MKV;
        }
        else 
        if (hashCode.equals("webm"))
        {
           return WEBM;
        }
        else 
        if (hashCode.equals("mpeg"))
        {
           return MPEG;
        }
        else 
        if (hashCode.equals("mpegts"))
        {
           return MPEGTS;
        }
        else 
        if (hashCode.equals("applehttp"))
        {
           return APPLEHTTP;
        }
        else 
        if (hashCode.equals("swf"))
        {
           return SWF;
        }
        else 
        if (hashCode.equals("pdf"))
        {
           return PDF;
        }
        else 
        if (hashCode.equals("jpg"))
        {
           return JPG;
        }
        else 
        {
           return FLV;
        }
    }
}
