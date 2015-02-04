package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaUiConfObjType {
    PLAYER (1),
    CONTRIBUTION_WIZARD (2),
    SIMPLE_EDITOR (3),
    ADVANCED_EDITOR (4),
    PLAYLIST (5),
    APP_STUDIO (6),
    KRECORD (7),
    PLAYER_V3 (8),
    KMC_ACCOUNT (9),
    KMC_ANALYTICS (10),
    KMC_CONTENT (11),
    KMC_DASHBOARD (12),
    KMC_LOGIN (13),
    PLAYER_SL (14),
    CLIENTSIDE_ENCODER (15),
    KMC_GENERAL (16),
    KMC_ROLES_AND_PERMISSIONS (17);

    int hashCode;

    KalturaUiConfObjType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaUiConfObjType get(int hashCode) {
        switch(hashCode) {
            case 1: return PLAYER;
            case 2: return CONTRIBUTION_WIZARD;
            case 3: return SIMPLE_EDITOR;
            case 4: return ADVANCED_EDITOR;
            case 5: return PLAYLIST;
            case 6: return APP_STUDIO;
            case 7: return KRECORD;
            case 8: return PLAYER_V3;
            case 9: return KMC_ACCOUNT;
            case 10: return KMC_ANALYTICS;
            case 11: return KMC_CONTENT;
            case 12: return KMC_DASHBOARD;
            case 13: return KMC_LOGIN;
            case 14: return PLAYER_SL;
            case 15: return CLIENTSIDE_ENCODER;
            case 16: return KMC_GENERAL;
            case 17: return KMC_ROLES_AND_PERMISSIONS;
            default: return PLAYER;
        }
    }
}
