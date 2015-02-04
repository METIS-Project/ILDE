package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaPartnerOrderBy {
    ID_ASC ("+id"),
    ID_DESC ("-id"),
    NAME_ASC ("+name"),
    NAME_DESC ("-name"),
    WEBSITE_ASC ("+website"),
    WEBSITE_DESC ("-website"),
    CREATED_AT_ASC ("+createdAt"),
    CREATED_AT_DESC ("-createdAt"),
    ADMIN_NAME_ASC ("+adminName"),
    ADMIN_NAME_DESC ("-adminName"),
    ADMIN_EMAIL_ASC ("+adminEmail"),
    ADMIN_EMAIL_DESC ("-adminEmail"),
    STATUS_ASC ("+status"),
    STATUS_DESC ("-status");

    String hashCode;

    KalturaPartnerOrderBy(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaPartnerOrderBy get(String hashCode) {
        if (hashCode.equals("+id"))
        {
           return ID_ASC;
        }
        else 
        if (hashCode.equals("-id"))
        {
           return ID_DESC;
        }
        else 
        if (hashCode.equals("+name"))
        {
           return NAME_ASC;
        }
        else 
        if (hashCode.equals("-name"))
        {
           return NAME_DESC;
        }
        else 
        if (hashCode.equals("+website"))
        {
           return WEBSITE_ASC;
        }
        else 
        if (hashCode.equals("-website"))
        {
           return WEBSITE_DESC;
        }
        else 
        if (hashCode.equals("+createdAt"))
        {
           return CREATED_AT_ASC;
        }
        else 
        if (hashCode.equals("-createdAt"))
        {
           return CREATED_AT_DESC;
        }
        else 
        if (hashCode.equals("+adminName"))
        {
           return ADMIN_NAME_ASC;
        }
        else 
        if (hashCode.equals("-adminName"))
        {
           return ADMIN_NAME_DESC;
        }
        else 
        if (hashCode.equals("+adminEmail"))
        {
           return ADMIN_EMAIL_ASC;
        }
        else 
        if (hashCode.equals("-adminEmail"))
        {
           return ADMIN_EMAIL_DESC;
        }
        else 
        if (hashCode.equals("+status"))
        {
           return STATUS_ASC;
        }
        else 
        if (hashCode.equals("-status"))
        {
           return STATUS_DESC;
        }
        else 
        {
           return ID_ASC;
        }
    }
}
