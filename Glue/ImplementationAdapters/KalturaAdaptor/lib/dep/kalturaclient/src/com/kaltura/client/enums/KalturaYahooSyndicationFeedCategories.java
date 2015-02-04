package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaYahooSyndicationFeedCategories {
    ACTION ("Action"),
    ART_AND_ANIMATION ("Art &amp; Animation"),
    ENTERTAINMENT_AND_TV ("Entertainment &amp; TV"),
    FOOD ("Food"),
    GAMES ("Games"),
    HOW_TO ("How-To"),
    MUSIC ("Music"),
    PEOPLE_AND_VLOGS ("People &amp; Vlogs"),
    SCIENCE_AND_ENVIRONMENT ("Science &amp; Environment"),
    TRANSPORTATION ("Transportation"),
    ANIMALS ("Animals"),
    COMMERCIALS ("Commercials"),
    FAMILY ("Family"),
    FUNNY_VIDEOS ("Funny Videos"),
    HEALTH_AND_BEAUTY ("Health &amp; Beauty"),
    MOVIES_AND_SHORTS ("Movies &amp; Shorts"),
    NEWS_AND_POLITICS ("News &amp; Politics"),
    PRODUCTS_AND_TECH ("Products &amp; Tech."),
    SPORTS ("Sports"),
    TRAVEL ("Travel");

    String hashCode;

    KalturaYahooSyndicationFeedCategories(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaYahooSyndicationFeedCategories get(String hashCode) {
        if (hashCode.equals("Action"))
        {
           return ACTION;
        }
        else 
        if (hashCode.equals("Art &amp; Animation"))
        {
           return ART_AND_ANIMATION;
        }
        else 
        if (hashCode.equals("Entertainment &amp; TV"))
        {
           return ENTERTAINMENT_AND_TV;
        }
        else 
        if (hashCode.equals("Food"))
        {
           return FOOD;
        }
        else 
        if (hashCode.equals("Games"))
        {
           return GAMES;
        }
        else 
        if (hashCode.equals("How-To"))
        {
           return HOW_TO;
        }
        else 
        if (hashCode.equals("Music"))
        {
           return MUSIC;
        }
        else 
        if (hashCode.equals("People &amp; Vlogs"))
        {
           return PEOPLE_AND_VLOGS;
        }
        else 
        if (hashCode.equals("Science &amp; Environment"))
        {
           return SCIENCE_AND_ENVIRONMENT;
        }
        else 
        if (hashCode.equals("Transportation"))
        {
           return TRANSPORTATION;
        }
        else 
        if (hashCode.equals("Animals"))
        {
           return ANIMALS;
        }
        else 
        if (hashCode.equals("Commercials"))
        {
           return COMMERCIALS;
        }
        else 
        if (hashCode.equals("Family"))
        {
           return FAMILY;
        }
        else 
        if (hashCode.equals("Funny Videos"))
        {
           return FUNNY_VIDEOS;
        }
        else 
        if (hashCode.equals("Health &amp; Beauty"))
        {
           return HEALTH_AND_BEAUTY;
        }
        else 
        if (hashCode.equals("Movies &amp; Shorts"))
        {
           return MOVIES_AND_SHORTS;
        }
        else 
        if (hashCode.equals("News &amp; Politics"))
        {
           return NEWS_AND_POLITICS;
        }
        else 
        if (hashCode.equals("Products &amp; Tech."))
        {
           return PRODUCTS_AND_TECH;
        }
        else 
        if (hashCode.equals("Sports"))
        {
           return SPORTS;
        }
        else 
        if (hashCode.equals("Travel"))
        {
           return TRAVEL;
        }
        else 
        {
           return ACTION;
        }
    }
}
