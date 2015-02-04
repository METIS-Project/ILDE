package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDistributionProtocol {
    FTP (1),
    SCP (2),
    SFTP (3),
    HTTP (4),
    HTTPS (5);

    int hashCode;

    KalturaDistributionProtocol(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaDistributionProtocol get(int hashCode) {
        switch(hashCode) {
            case 1: return FTP;
            case 2: return SCP;
            case 3: return SFTP;
            case 4: return HTTP;
            case 5: return HTTPS;
            default: return FTP;
        }
    }
}
