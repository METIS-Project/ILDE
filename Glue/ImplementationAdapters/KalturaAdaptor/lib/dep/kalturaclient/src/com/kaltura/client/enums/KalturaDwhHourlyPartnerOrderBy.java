package com.kaltura.client.enums;

/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaDwhHourlyPartnerOrderBy {
    AGGREGATED_TIME_ASC ("+aggregatedTime"),
    AGGREGATED_TIME_DESC ("-aggregatedTime"),
    SUM_TIME_VIEWED_ASC ("+sumTimeViewed"),
    SUM_TIME_VIEWED_DESC ("-sumTimeViewed"),
    AVERAGE_TIME_VIEWED_ASC ("+averageTimeViewed"),
    AVERAGE_TIME_VIEWED_DESC ("-averageTimeViewed"),
    COUNT_PLAYS_ASC ("+countPlays"),
    COUNT_PLAYS_DESC ("-countPlays"),
    COUNT_LOADS_ASC ("+countLoads"),
    COUNT_LOADS_DESC ("-countLoads"),
    COUNT_PLAYS25_ASC ("+countPlays25"),
    COUNT_PLAYS25_DESC ("-countPlays25"),
    COUNT_PLAYS50_ASC ("+countPlays50"),
    COUNT_PLAYS50_DESC ("-countPlays50"),
    COUNT_PLAYS75_ASC ("+countPlays75"),
    COUNT_PLAYS75_DESC ("-countPlays75"),
    COUNT_PLAYS100_ASC ("+countPlays100"),
    COUNT_PLAYS100_DESC ("-countPlays100"),
    COUNT_EDIT_ASC ("+countEdit"),
    COUNT_EDIT_DESC ("-countEdit"),
    COUNT_SHARES_ASC ("+countShares"),
    COUNT_SHARES_DESC ("-countShares"),
    COUNT_DOWNLOAD_ASC ("+countDownload"),
    COUNT_DOWNLOAD_DESC ("-countDownload"),
    COUNT_REPORT_ABUSE_ASC ("+countReportAbuse"),
    COUNT_REPORT_ABUSE_DESC ("-countReportAbuse"),
    COUNT_MEDIA_ENTRIES_ASC ("+countMediaEntries"),
    COUNT_MEDIA_ENTRIES_DESC ("-countMediaEntries"),
    COUNT_VIDEO_ENTRIES_ASC ("+countVideoEntries"),
    COUNT_VIDEO_ENTRIES_DESC ("-countVideoEntries"),
    COUNT_IMAGE_ENTRIES_ASC ("+countImageEntries"),
    COUNT_IMAGE_ENTRIES_DESC ("-countImageEntries"),
    COUNT_AUDIO_ENTRIES_ASC ("+countAudioEntries"),
    COUNT_AUDIO_ENTRIES_DESC ("-countAudioEntries"),
    COUNT_MIX_ENTRIES_ASC ("+countMixEntries"),
    COUNT_MIX_ENTRIES_DESC ("-countMixEntries"),
    COUNT_PLAYLISTS_ASC ("+countPlaylists"),
    COUNT_PLAYLISTS_DESC ("-countPlaylists"),
    COUNT_BANDWIDTH_ASC ("+countBandwidth"),
    COUNT_BANDWIDTH_DESC ("-countBandwidth"),
    COUNT_STORAGE_ASC ("+countStorage"),
    COUNT_STORAGE_DESC ("-countStorage"),
    COUNT_USERS_ASC ("+countUsers"),
    COUNT_USERS_DESC ("-countUsers"),
    COUNT_WIDGETS_ASC ("+countWidgets"),
    COUNT_WIDGETS_DESC ("-countWidgets"),
    AGGREGATED_STORAGE_ASC ("+aggregatedStorage"),
    AGGREGATED_STORAGE_DESC ("-aggregatedStorage"),
    AGGREGATED_BANDWIDTH_ASC ("+aggregatedBandwidth"),
    AGGREGATED_BANDWIDTH_DESC ("-aggregatedBandwidth"),
    COUNT_BUFFER_START_ASC ("+countBufferStart"),
    COUNT_BUFFER_START_DESC ("-countBufferStart"),
    COUNT_BUFFER_END_ASC ("+countBufferEnd"),
    COUNT_BUFFER_END_DESC ("-countBufferEnd"),
    COUNT_OPEN_FULL_SCREEN_ASC ("+countOpenFullScreen"),
    COUNT_OPEN_FULL_SCREEN_DESC ("-countOpenFullScreen"),
    COUNT_CLOSE_FULL_SCREEN_ASC ("+countCloseFullScreen"),
    COUNT_CLOSE_FULL_SCREEN_DESC ("-countCloseFullScreen"),
    COUNT_REPLAY_ASC ("+countReplay"),
    COUNT_REPLAY_DESC ("-countReplay"),
    COUNT_SEEK_ASC ("+countSeek"),
    COUNT_SEEK_DESC ("-countSeek"),
    COUNT_OPEN_UPLOAD_ASC ("+countOpenUpload"),
    COUNT_OPEN_UPLOAD_DESC ("-countOpenUpload"),
    COUNT_SAVE_PUBLISH_ASC ("+countSavePublish"),
    COUNT_SAVE_PUBLISH_DESC ("-countSavePublish"),
    COUNT_CLOSE_EDITOR_ASC ("+countCloseEditor"),
    COUNT_CLOSE_EDITOR_DESC ("-countCloseEditor"),
    COUNT_PRE_BUMPER_PLAYED_ASC ("+countPreBumperPlayed"),
    COUNT_PRE_BUMPER_PLAYED_DESC ("-countPreBumperPlayed"),
    COUNT_POST_BUMPER_PLAYED_ASC ("+countPostBumperPlayed"),
    COUNT_POST_BUMPER_PLAYED_DESC ("-countPostBumperPlayed"),
    COUNT_BUMPER_CLICKED_ASC ("+countBumperClicked"),
    COUNT_BUMPER_CLICKED_DESC ("-countBumperClicked"),
    COUNT_PREROLL_STARTED_ASC ("+countPrerollStarted"),
    COUNT_PREROLL_STARTED_DESC ("-countPrerollStarted"),
    COUNT_MIDROLL_STARTED_ASC ("+countMidrollStarted"),
    COUNT_MIDROLL_STARTED_DESC ("-countMidrollStarted"),
    COUNT_POSTROLL_STARTED_ASC ("+countPostrollStarted"),
    COUNT_POSTROLL_STARTED_DESC ("-countPostrollStarted"),
    COUNT_OVERLAY_STARTED_ASC ("+countOverlayStarted"),
    COUNT_OVERLAY_STARTED_DESC ("-countOverlayStarted"),
    COUNT_PREROLL_CLICKED_ASC ("+countPrerollClicked"),
    COUNT_PREROLL_CLICKED_DESC ("-countPrerollClicked"),
    COUNT_MIDROLL_CLICKED_ASC ("+countMidrollClicked"),
    COUNT_MIDROLL_CLICKED_DESC ("-countMidrollClicked"),
    COUNT_POSTROLL_CLICKED_ASC ("+countPostrollClicked"),
    COUNT_POSTROLL_CLICKED_DESC ("-countPostrollClicked"),
    COUNT_OVERLAY_CLICKED_ASC ("+countOverlayClicked"),
    COUNT_OVERLAY_CLICKED_DESC ("-countOverlayClicked"),
    COUNT_PREROLL25_ASC ("+countPreroll25"),
    COUNT_PREROLL25_DESC ("-countPreroll25"),
    COUNT_PREROLL50_ASC ("+countPreroll50"),
    COUNT_PREROLL50_DESC ("-countPreroll50"),
    COUNT_PREROLL75_ASC ("+countPreroll75"),
    COUNT_PREROLL75_DESC ("-countPreroll75"),
    COUNT_MIDROLL25_ASC ("+countMidroll25"),
    COUNT_MIDROLL25_DESC ("-countMidroll25"),
    COUNT_MIDROLL50_ASC ("+countMidroll50"),
    COUNT_MIDROLL50_DESC ("-countMidroll50"),
    COUNT_MIDROLL75_ASC ("+countMidroll75"),
    COUNT_MIDROLL75_DESC ("-countMidroll75"),
    COUNT_POSTROLL25_ASC ("+countPostroll25"),
    COUNT_POSTROLL25_DESC ("-countPostroll25"),
    COUNT_POSTROLL50_ASC ("+countPostroll50"),
    COUNT_POSTROLL50_DESC ("-countPostroll50"),
    COUNT_POSTROLL75_ASC ("+countPostroll75"),
    COUNT_POSTROLL75_DESC ("-countPostroll75"),
    COUNT_LIVE_STREAMING_BANDWIDTH_ASC ("+countLiveStreamingBandwidth"),
    COUNT_LIVE_STREAMING_BANDWIDTH_DESC ("-countLiveStreamingBandwidth"),
    AGGREGATED_LIVE_STREAMING_BANDWIDTH_ASC ("+aggregatedLiveStreamingBandwidth"),
    AGGREGATED_LIVE_STREAMING_BANDWIDTH_DESC ("-aggregatedLiveStreamingBandwidth");

    String hashCode;

    KalturaDwhHourlyPartnerOrderBy(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public static KalturaDwhHourlyPartnerOrderBy get(String hashCode) {
        if (hashCode.equals("+aggregatedTime"))
        {
           return AGGREGATED_TIME_ASC;
        }
        else 
        if (hashCode.equals("-aggregatedTime"))
        {
           return AGGREGATED_TIME_DESC;
        }
        else 
        if (hashCode.equals("+sumTimeViewed"))
        {
           return SUM_TIME_VIEWED_ASC;
        }
        else 
        if (hashCode.equals("-sumTimeViewed"))
        {
           return SUM_TIME_VIEWED_DESC;
        }
        else 
        if (hashCode.equals("+averageTimeViewed"))
        {
           return AVERAGE_TIME_VIEWED_ASC;
        }
        else 
        if (hashCode.equals("-averageTimeViewed"))
        {
           return AVERAGE_TIME_VIEWED_DESC;
        }
        else 
        if (hashCode.equals("+countPlays"))
        {
           return COUNT_PLAYS_ASC;
        }
        else 
        if (hashCode.equals("-countPlays"))
        {
           return COUNT_PLAYS_DESC;
        }
        else 
        if (hashCode.equals("+countLoads"))
        {
           return COUNT_LOADS_ASC;
        }
        else 
        if (hashCode.equals("-countLoads"))
        {
           return COUNT_LOADS_DESC;
        }
        else 
        if (hashCode.equals("+countPlays25"))
        {
           return COUNT_PLAYS25_ASC;
        }
        else 
        if (hashCode.equals("-countPlays25"))
        {
           return COUNT_PLAYS25_DESC;
        }
        else 
        if (hashCode.equals("+countPlays50"))
        {
           return COUNT_PLAYS50_ASC;
        }
        else 
        if (hashCode.equals("-countPlays50"))
        {
           return COUNT_PLAYS50_DESC;
        }
        else 
        if (hashCode.equals("+countPlays75"))
        {
           return COUNT_PLAYS75_ASC;
        }
        else 
        if (hashCode.equals("-countPlays75"))
        {
           return COUNT_PLAYS75_DESC;
        }
        else 
        if (hashCode.equals("+countPlays100"))
        {
           return COUNT_PLAYS100_ASC;
        }
        else 
        if (hashCode.equals("-countPlays100"))
        {
           return COUNT_PLAYS100_DESC;
        }
        else 
        if (hashCode.equals("+countEdit"))
        {
           return COUNT_EDIT_ASC;
        }
        else 
        if (hashCode.equals("-countEdit"))
        {
           return COUNT_EDIT_DESC;
        }
        else 
        if (hashCode.equals("+countShares"))
        {
           return COUNT_SHARES_ASC;
        }
        else 
        if (hashCode.equals("-countShares"))
        {
           return COUNT_SHARES_DESC;
        }
        else 
        if (hashCode.equals("+countDownload"))
        {
           return COUNT_DOWNLOAD_ASC;
        }
        else 
        if (hashCode.equals("-countDownload"))
        {
           return COUNT_DOWNLOAD_DESC;
        }
        else 
        if (hashCode.equals("+countReportAbuse"))
        {
           return COUNT_REPORT_ABUSE_ASC;
        }
        else 
        if (hashCode.equals("-countReportAbuse"))
        {
           return COUNT_REPORT_ABUSE_DESC;
        }
        else 
        if (hashCode.equals("+countMediaEntries"))
        {
           return COUNT_MEDIA_ENTRIES_ASC;
        }
        else 
        if (hashCode.equals("-countMediaEntries"))
        {
           return COUNT_MEDIA_ENTRIES_DESC;
        }
        else 
        if (hashCode.equals("+countVideoEntries"))
        {
           return COUNT_VIDEO_ENTRIES_ASC;
        }
        else 
        if (hashCode.equals("-countVideoEntries"))
        {
           return COUNT_VIDEO_ENTRIES_DESC;
        }
        else 
        if (hashCode.equals("+countImageEntries"))
        {
           return COUNT_IMAGE_ENTRIES_ASC;
        }
        else 
        if (hashCode.equals("-countImageEntries"))
        {
           return COUNT_IMAGE_ENTRIES_DESC;
        }
        else 
        if (hashCode.equals("+countAudioEntries"))
        {
           return COUNT_AUDIO_ENTRIES_ASC;
        }
        else 
        if (hashCode.equals("-countAudioEntries"))
        {
           return COUNT_AUDIO_ENTRIES_DESC;
        }
        else 
        if (hashCode.equals("+countMixEntries"))
        {
           return COUNT_MIX_ENTRIES_ASC;
        }
        else 
        if (hashCode.equals("-countMixEntries"))
        {
           return COUNT_MIX_ENTRIES_DESC;
        }
        else 
        if (hashCode.equals("+countPlaylists"))
        {
           return COUNT_PLAYLISTS_ASC;
        }
        else 
        if (hashCode.equals("-countPlaylists"))
        {
           return COUNT_PLAYLISTS_DESC;
        }
        else 
        if (hashCode.equals("+countBandwidth"))
        {
           return COUNT_BANDWIDTH_ASC;
        }
        else 
        if (hashCode.equals("-countBandwidth"))
        {
           return COUNT_BANDWIDTH_DESC;
        }
        else 
        if (hashCode.equals("+countStorage"))
        {
           return COUNT_STORAGE_ASC;
        }
        else 
        if (hashCode.equals("-countStorage"))
        {
           return COUNT_STORAGE_DESC;
        }
        else 
        if (hashCode.equals("+countUsers"))
        {
           return COUNT_USERS_ASC;
        }
        else 
        if (hashCode.equals("-countUsers"))
        {
           return COUNT_USERS_DESC;
        }
        else 
        if (hashCode.equals("+countWidgets"))
        {
           return COUNT_WIDGETS_ASC;
        }
        else 
        if (hashCode.equals("-countWidgets"))
        {
           return COUNT_WIDGETS_DESC;
        }
        else 
        if (hashCode.equals("+aggregatedStorage"))
        {
           return AGGREGATED_STORAGE_ASC;
        }
        else 
        if (hashCode.equals("-aggregatedStorage"))
        {
           return AGGREGATED_STORAGE_DESC;
        }
        else 
        if (hashCode.equals("+aggregatedBandwidth"))
        {
           return AGGREGATED_BANDWIDTH_ASC;
        }
        else 
        if (hashCode.equals("-aggregatedBandwidth"))
        {
           return AGGREGATED_BANDWIDTH_DESC;
        }
        else 
        if (hashCode.equals("+countBufferStart"))
        {
           return COUNT_BUFFER_START_ASC;
        }
        else 
        if (hashCode.equals("-countBufferStart"))
        {
           return COUNT_BUFFER_START_DESC;
        }
        else 
        if (hashCode.equals("+countBufferEnd"))
        {
           return COUNT_BUFFER_END_ASC;
        }
        else 
        if (hashCode.equals("-countBufferEnd"))
        {
           return COUNT_BUFFER_END_DESC;
        }
        else 
        if (hashCode.equals("+countOpenFullScreen"))
        {
           return COUNT_OPEN_FULL_SCREEN_ASC;
        }
        else 
        if (hashCode.equals("-countOpenFullScreen"))
        {
           return COUNT_OPEN_FULL_SCREEN_DESC;
        }
        else 
        if (hashCode.equals("+countCloseFullScreen"))
        {
           return COUNT_CLOSE_FULL_SCREEN_ASC;
        }
        else 
        if (hashCode.equals("-countCloseFullScreen"))
        {
           return COUNT_CLOSE_FULL_SCREEN_DESC;
        }
        else 
        if (hashCode.equals("+countReplay"))
        {
           return COUNT_REPLAY_ASC;
        }
        else 
        if (hashCode.equals("-countReplay"))
        {
           return COUNT_REPLAY_DESC;
        }
        else 
        if (hashCode.equals("+countSeek"))
        {
           return COUNT_SEEK_ASC;
        }
        else 
        if (hashCode.equals("-countSeek"))
        {
           return COUNT_SEEK_DESC;
        }
        else 
        if (hashCode.equals("+countOpenUpload"))
        {
           return COUNT_OPEN_UPLOAD_ASC;
        }
        else 
        if (hashCode.equals("-countOpenUpload"))
        {
           return COUNT_OPEN_UPLOAD_DESC;
        }
        else 
        if (hashCode.equals("+countSavePublish"))
        {
           return COUNT_SAVE_PUBLISH_ASC;
        }
        else 
        if (hashCode.equals("-countSavePublish"))
        {
           return COUNT_SAVE_PUBLISH_DESC;
        }
        else 
        if (hashCode.equals("+countCloseEditor"))
        {
           return COUNT_CLOSE_EDITOR_ASC;
        }
        else 
        if (hashCode.equals("-countCloseEditor"))
        {
           return COUNT_CLOSE_EDITOR_DESC;
        }
        else 
        if (hashCode.equals("+countPreBumperPlayed"))
        {
           return COUNT_PRE_BUMPER_PLAYED_ASC;
        }
        else 
        if (hashCode.equals("-countPreBumperPlayed"))
        {
           return COUNT_PRE_BUMPER_PLAYED_DESC;
        }
        else 
        if (hashCode.equals("+countPostBumperPlayed"))
        {
           return COUNT_POST_BUMPER_PLAYED_ASC;
        }
        else 
        if (hashCode.equals("-countPostBumperPlayed"))
        {
           return COUNT_POST_BUMPER_PLAYED_DESC;
        }
        else 
        if (hashCode.equals("+countBumperClicked"))
        {
           return COUNT_BUMPER_CLICKED_ASC;
        }
        else 
        if (hashCode.equals("-countBumperClicked"))
        {
           return COUNT_BUMPER_CLICKED_DESC;
        }
        else 
        if (hashCode.equals("+countPrerollStarted"))
        {
           return COUNT_PREROLL_STARTED_ASC;
        }
        else 
        if (hashCode.equals("-countPrerollStarted"))
        {
           return COUNT_PREROLL_STARTED_DESC;
        }
        else 
        if (hashCode.equals("+countMidrollStarted"))
        {
           return COUNT_MIDROLL_STARTED_ASC;
        }
        else 
        if (hashCode.equals("-countMidrollStarted"))
        {
           return COUNT_MIDROLL_STARTED_DESC;
        }
        else 
        if (hashCode.equals("+countPostrollStarted"))
        {
           return COUNT_POSTROLL_STARTED_ASC;
        }
        else 
        if (hashCode.equals("-countPostrollStarted"))
        {
           return COUNT_POSTROLL_STARTED_DESC;
        }
        else 
        if (hashCode.equals("+countOverlayStarted"))
        {
           return COUNT_OVERLAY_STARTED_ASC;
        }
        else 
        if (hashCode.equals("-countOverlayStarted"))
        {
           return COUNT_OVERLAY_STARTED_DESC;
        }
        else 
        if (hashCode.equals("+countPrerollClicked"))
        {
           return COUNT_PREROLL_CLICKED_ASC;
        }
        else 
        if (hashCode.equals("-countPrerollClicked"))
        {
           return COUNT_PREROLL_CLICKED_DESC;
        }
        else 
        if (hashCode.equals("+countMidrollClicked"))
        {
           return COUNT_MIDROLL_CLICKED_ASC;
        }
        else 
        if (hashCode.equals("-countMidrollClicked"))
        {
           return COUNT_MIDROLL_CLICKED_DESC;
        }
        else 
        if (hashCode.equals("+countPostrollClicked"))
        {
           return COUNT_POSTROLL_CLICKED_ASC;
        }
        else 
        if (hashCode.equals("-countPostrollClicked"))
        {
           return COUNT_POSTROLL_CLICKED_DESC;
        }
        else 
        if (hashCode.equals("+countOverlayClicked"))
        {
           return COUNT_OVERLAY_CLICKED_ASC;
        }
        else 
        if (hashCode.equals("-countOverlayClicked"))
        {
           return COUNT_OVERLAY_CLICKED_DESC;
        }
        else 
        if (hashCode.equals("+countPreroll25"))
        {
           return COUNT_PREROLL25_ASC;
        }
        else 
        if (hashCode.equals("-countPreroll25"))
        {
           return COUNT_PREROLL25_DESC;
        }
        else 
        if (hashCode.equals("+countPreroll50"))
        {
           return COUNT_PREROLL50_ASC;
        }
        else 
        if (hashCode.equals("-countPreroll50"))
        {
           return COUNT_PREROLL50_DESC;
        }
        else 
        if (hashCode.equals("+countPreroll75"))
        {
           return COUNT_PREROLL75_ASC;
        }
        else 
        if (hashCode.equals("-countPreroll75"))
        {
           return COUNT_PREROLL75_DESC;
        }
        else 
        if (hashCode.equals("+countMidroll25"))
        {
           return COUNT_MIDROLL25_ASC;
        }
        else 
        if (hashCode.equals("-countMidroll25"))
        {
           return COUNT_MIDROLL25_DESC;
        }
        else 
        if (hashCode.equals("+countMidroll50"))
        {
           return COUNT_MIDROLL50_ASC;
        }
        else 
        if (hashCode.equals("-countMidroll50"))
        {
           return COUNT_MIDROLL50_DESC;
        }
        else 
        if (hashCode.equals("+countMidroll75"))
        {
           return COUNT_MIDROLL75_ASC;
        }
        else 
        if (hashCode.equals("-countMidroll75"))
        {
           return COUNT_MIDROLL75_DESC;
        }
        else 
        if (hashCode.equals("+countPostroll25"))
        {
           return COUNT_POSTROLL25_ASC;
        }
        else 
        if (hashCode.equals("-countPostroll25"))
        {
           return COUNT_POSTROLL25_DESC;
        }
        else 
        if (hashCode.equals("+countPostroll50"))
        {
           return COUNT_POSTROLL50_ASC;
        }
        else 
        if (hashCode.equals("-countPostroll50"))
        {
           return COUNT_POSTROLL50_DESC;
        }
        else 
        if (hashCode.equals("+countPostroll75"))
        {
           return COUNT_POSTROLL75_ASC;
        }
        else 
        if (hashCode.equals("-countPostroll75"))
        {
           return COUNT_POSTROLL75_DESC;
        }
        else 
        if (hashCode.equals("+countLiveStreamingBandwidth"))
        {
           return COUNT_LIVE_STREAMING_BANDWIDTH_ASC;
        }
        else 
        if (hashCode.equals("-countLiveStreamingBandwidth"))
        {
           return COUNT_LIVE_STREAMING_BANDWIDTH_DESC;
        }
        else 
        if (hashCode.equals("+aggregatedLiveStreamingBandwidth"))
        {
           return AGGREGATED_LIVE_STREAMING_BANDWIDTH_ASC;
        }
        else 
        if (hashCode.equals("-aggregatedLiveStreamingBandwidth"))
        {
           return AGGREGATED_LIVE_STREAMING_BANDWIDTH_DESC;
        }
        else 
        {
           return AGGREGATED_TIME_ASC;
        }
    }
}
