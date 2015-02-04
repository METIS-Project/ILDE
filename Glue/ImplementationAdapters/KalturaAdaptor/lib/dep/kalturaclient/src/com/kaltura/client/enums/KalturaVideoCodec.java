package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaVideoCodec {
    NONE (""),
    VP6 ("vp6"),
    H263 ("h263"),
    H264 ("h264"),
    H264B ("h264b"),
    H264M ("h264m"),
    H264H ("h264h"),
    FLV ("flv"),
    MPEG4 ("mpeg4"),
    THEORA ("theora"),
    WMV2 ("wmv2"),
    WMV3 ("wmv3"),
    WVC1A ("wvc1a"),
    VP8 ("vp8"),
    MPEG2 ("mpeg2"),
    COPY ("copy");

    String hashCode;

    KalturaVideoCodec(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaVideoCodec get(String hashCode) {
        if (hashCode.equals(""))
        {
           return NONE;
        }
        else 
        if (hashCode.equals("vp6"))
        {
           return VP6;
        }
        else 
        if (hashCode.equals("h263"))
        {
           return H263;
        }
        else 
        if (hashCode.equals("h264"))
        {
           return H264;
        }
        else 
        if (hashCode.equals("h264b"))
        {
           return H264B;
        }
        else 
        if (hashCode.equals("h264m"))
        {
           return H264M;
        }
        else 
        if (hashCode.equals("h264h"))
        {
           return H264H;
        }
        else 
        if (hashCode.equals("flv"))
        {
           return FLV;
        }
        else 
        if (hashCode.equals("mpeg4"))
        {
           return MPEG4;
        }
        else 
        if (hashCode.equals("theora"))
        {
           return THEORA;
        }
        else 
        if (hashCode.equals("wmv2"))
        {
           return WMV2;
        }
        else 
        if (hashCode.equals("wmv3"))
        {
           return WMV3;
        }
        else 
        if (hashCode.equals("wvc1a"))
        {
           return WVC1A;
        }
        else 
        if (hashCode.equals("vp8"))
        {
           return VP8;
        }
        else 
        if (hashCode.equals("mpeg2"))
        {
           return MPEG2;
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
