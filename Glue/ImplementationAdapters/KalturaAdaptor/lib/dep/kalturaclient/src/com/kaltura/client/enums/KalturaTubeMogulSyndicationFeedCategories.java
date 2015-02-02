package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaTubeMogulSyndicationFeedCategories {
    ARTS_AND_ANIMATION ("Arts &amp; Animation"),
    COMEDY ("Comedy"),
    ENTERTAINMENT ("Entertainment"),
    MUSIC ("Music"),
    NEWS_AND_BLOGS ("News &amp; Blogs"),
    SCIENCE_AND_TECHNOLOGY ("Science &amp; Technology"),
    SPORTS ("Sports"),
    TRAVEL_AND_PLACES ("Travel &amp; Places"),
    VIDEO_GAMES ("Video Games"),
    ANIMALS_AND_PETS ("Animals &amp; Pets"),
    AUTOS ("Autos"),
    VLOGS_PEOPLE ("Vlogs &amp; People"),
    HOW_TO_INSTRUCTIONAL_DIY ("How To/Instructional/DIY"),
    COMMERCIALS_PROMOTIONAL ("Commercials/Promotional"),
    FAMILY_AND_KIDS ("Family &amp; Kids");

    String hashCode;

    KalturaTubeMogulSyndicationFeedCategories(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaTubeMogulSyndicationFeedCategories get(String hashCode) {
        if (hashCode.equals("Arts &amp; Animation"))
        {
           return ARTS_AND_ANIMATION;
        }
        else 
        if (hashCode.equals("Comedy"))
        {
           return COMEDY;
        }
        else 
        if (hashCode.equals("Entertainment"))
        {
           return ENTERTAINMENT;
        }
        else 
        if (hashCode.equals("Music"))
        {
           return MUSIC;
        }
        else 
        if (hashCode.equals("News &amp; Blogs"))
        {
           return NEWS_AND_BLOGS;
        }
        else 
        if (hashCode.equals("Science &amp; Technology"))
        {
           return SCIENCE_AND_TECHNOLOGY;
        }
        else 
        if (hashCode.equals("Sports"))
        {
           return SPORTS;
        }
        else 
        if (hashCode.equals("Travel &amp; Places"))
        {
           return TRAVEL_AND_PLACES;
        }
        else 
        if (hashCode.equals("Video Games"))
        {
           return VIDEO_GAMES;
        }
        else 
        if (hashCode.equals("Animals &amp; Pets"))
        {
           return ANIMALS_AND_PETS;
        }
        else 
        if (hashCode.equals("Autos"))
        {
           return AUTOS;
        }
        else 
        if (hashCode.equals("Vlogs &amp; People"))
        {
           return VLOGS_PEOPLE;
        }
        else 
        if (hashCode.equals("How To/Instructional/DIY"))
        {
           return HOW_TO_INSTRUCTIONAL_DIY;
        }
        else 
        if (hashCode.equals("Commercials/Promotional"))
        {
           return COMMERCIALS_PROMOTIONAL;
        }
        else 
        if (hashCode.equals("Family &amp; Kids"))
        {
           return FAMILY_AND_KIDS;
        }
        else 
        {
           return ARTS_AND_ANIMATION;
        }
    }
}
