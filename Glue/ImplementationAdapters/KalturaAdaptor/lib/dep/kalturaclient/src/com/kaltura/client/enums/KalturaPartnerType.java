package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaPartnerType {
    KMC (1),
    WIKI (100),
    WORDPRESS (101),
    DRUPAL (102),
    DEKIWIKI (103),
    MOODLE (104),
    COMMUNITY_EDITION (105),
    JOOMLA (106),
    BLACKBOARD (107),
    SAKAI (108);

    int hashCode;

    KalturaPartnerType(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public static KalturaPartnerType get(int hashCode) {
        switch(hashCode) {
            case 1: return KMC;
            case 100: return WIKI;
            case 101: return WORDPRESS;
            case 102: return DRUPAL;
            case 103: return DEKIWIKI;
            case 104: return MOODLE;
            case 105: return COMMUNITY_EDITION;
            case 106: return JOOMLA;
            case 107: return BLACKBOARD;
            case 108: return SAKAI;
            default: return KMC;
        }
    }
}
