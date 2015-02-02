package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaStorageProfileProtocol {
    KALTURA_DC (0),
    FTP (1),
    SCP (2),
    SFTP (3);

    int hashCode;

    KalturaStorageProfileProtocol(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaStorageProfileProtocol get(int hashCode) {
        switch(hashCode) {
            case 0: return KALTURA_DC;
            case 1: return FTP;
            case 2: return SCP;
            case 3: return SFTP;
            default: return KALTURA_DC;
        }
    }
}
