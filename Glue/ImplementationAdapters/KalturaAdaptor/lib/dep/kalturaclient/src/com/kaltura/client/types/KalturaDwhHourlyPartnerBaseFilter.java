package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public abstract class KalturaDwhHourlyPartnerBaseFilter extends KalturaFilter {
    public int partnerIdEqual = Integer.MIN_VALUE;
    public int aggregatedTimeLessThanOrEqual = Integer.MIN_VALUE;
    public int aggregatedTimeGreaterThanOrEqual = Integer.MIN_VALUE;
    public float sumTimeViewedLessThanOrEqual = Float.MIN_VALUE;
    public float sumTimeViewedGreaterThanOrEqual = Float.MIN_VALUE;
    public float averageTimeViewedLessThanOrEqual = Float.MIN_VALUE;
    public float averageTimeViewedGreaterThanOrEqual = Float.MIN_VALUE;
    public int countPlaysLessThanOrEqual = Integer.MIN_VALUE;
    public int countPlaysGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countLoadsLessThanOrEqual = Integer.MIN_VALUE;
    public int countLoadsGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countPlays25LessThanOrEqual = Integer.MIN_VALUE;
    public int countPlays25GreaterThanOrEqual = Integer.MIN_VALUE;
    public int countPlays50LessThanOrEqual = Integer.MIN_VALUE;
    public int countPlays50GreaterThanOrEqual = Integer.MIN_VALUE;
    public int countPlays75LessThanOrEqual = Integer.MIN_VALUE;
    public int countPlays75GreaterThanOrEqual = Integer.MIN_VALUE;
    public int countPlays100LessThanOrEqual = Integer.MIN_VALUE;
    public int countPlays100GreaterThanOrEqual = Integer.MIN_VALUE;
    public int countEditLessThanOrEqual = Integer.MIN_VALUE;
    public int countEditGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countSharesLessThanOrEqual = Integer.MIN_VALUE;
    public int countSharesGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countDownloadLessThanOrEqual = Integer.MIN_VALUE;
    public int countDownloadGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countReportAbuseLessThanOrEqual = Integer.MIN_VALUE;
    public int countReportAbuseGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countMediaEntriesLessThanOrEqual = Integer.MIN_VALUE;
    public int countMediaEntriesGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countVideoEntriesLessThanOrEqual = Integer.MIN_VALUE;
    public int countVideoEntriesGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countImageEntriesLessThanOrEqual = Integer.MIN_VALUE;
    public int countImageEntriesGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countAudioEntriesLessThanOrEqual = Integer.MIN_VALUE;
    public int countAudioEntriesGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countMixEntriesLessThanOrEqual = Integer.MIN_VALUE;
    public int countMixEntriesGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countPlaylistsLessThanOrEqual = Integer.MIN_VALUE;
    public int countPlaylistsGreaterThanOrEqual = Integer.MIN_VALUE;
    public String countBandwidthLessThanOrEqual;
    public String countBandwidthGreaterThanOrEqual;
    public String countStorageLessThanOrEqual;
    public String countStorageGreaterThanOrEqual;
    public int countUsersLessThanOrEqual = Integer.MIN_VALUE;
    public int countUsersGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countWidgetsLessThanOrEqual = Integer.MIN_VALUE;
    public int countWidgetsGreaterThanOrEqual = Integer.MIN_VALUE;
    public String aggregatedStorageLessThanOrEqual;
    public String aggregatedStorageGreaterThanOrEqual;
    public String aggregatedBandwidthLessThanOrEqual;
    public String aggregatedBandwidthGreaterThanOrEqual;
    public int countBufferStartLessThanOrEqual = Integer.MIN_VALUE;
    public int countBufferStartGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countBufferEndLessThanOrEqual = Integer.MIN_VALUE;
    public int countBufferEndGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countOpenFullScreenLessThanOrEqual = Integer.MIN_VALUE;
    public int countOpenFullScreenGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countCloseFullScreenLessThanOrEqual = Integer.MIN_VALUE;
    public int countCloseFullScreenGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countReplayLessThanOrEqual = Integer.MIN_VALUE;
    public int countReplayGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countSeekLessThanOrEqual = Integer.MIN_VALUE;
    public int countSeekGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countOpenUploadLessThanOrEqual = Integer.MIN_VALUE;
    public int countOpenUploadGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countSavePublishLessThanOrEqual = Integer.MIN_VALUE;
    public int countSavePublishGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countCloseEditorLessThanOrEqual = Integer.MIN_VALUE;
    public int countCloseEditorGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countPreBumperPlayedLessThanOrEqual = Integer.MIN_VALUE;
    public int countPreBumperPlayedGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countPostBumperPlayedLessThanOrEqual = Integer.MIN_VALUE;
    public int countPostBumperPlayedGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countBumperClickedLessThanOrEqual = Integer.MIN_VALUE;
    public int countBumperClickedGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countPrerollStartedLessThanOrEqual = Integer.MIN_VALUE;
    public int countPrerollStartedGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countMidrollStartedLessThanOrEqual = Integer.MIN_VALUE;
    public int countMidrollStartedGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countPostrollStartedLessThanOrEqual = Integer.MIN_VALUE;
    public int countPostrollStartedGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countOverlayStartedLessThanOrEqual = Integer.MIN_VALUE;
    public int countOverlayStartedGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countPrerollClickedLessThanOrEqual = Integer.MIN_VALUE;
    public int countPrerollClickedGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countMidrollClickedLessThanOrEqual = Integer.MIN_VALUE;
    public int countMidrollClickedGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countPostrollClickedLessThanOrEqual = Integer.MIN_VALUE;
    public int countPostrollClickedGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countOverlayClickedLessThanOrEqual = Integer.MIN_VALUE;
    public int countOverlayClickedGreaterThanOrEqual = Integer.MIN_VALUE;
    public int countPreroll25LessThanOrEqual = Integer.MIN_VALUE;
    public int countPreroll25GreaterThanOrEqual = Integer.MIN_VALUE;
    public int countPreroll50LessThanOrEqual = Integer.MIN_VALUE;
    public int countPreroll50GreaterThanOrEqual = Integer.MIN_VALUE;
    public int countPreroll75LessThanOrEqual = Integer.MIN_VALUE;
    public int countPreroll75GreaterThanOrEqual = Integer.MIN_VALUE;
    public int countMidroll25LessThanOrEqual = Integer.MIN_VALUE;
    public int countMidroll25GreaterThanOrEqual = Integer.MIN_VALUE;
    public int countMidroll50LessThanOrEqual = Integer.MIN_VALUE;
    public int countMidroll50GreaterThanOrEqual = Integer.MIN_VALUE;
    public int countMidroll75LessThanOrEqual = Integer.MIN_VALUE;
    public int countMidroll75GreaterThanOrEqual = Integer.MIN_VALUE;
    public int countPostroll25LessThanOrEqual = Integer.MIN_VALUE;
    public int countPostroll25GreaterThanOrEqual = Integer.MIN_VALUE;
    public int countPostroll50LessThanOrEqual = Integer.MIN_VALUE;
    public int countPostroll50GreaterThanOrEqual = Integer.MIN_VALUE;
    public int countPostroll75LessThanOrEqual = Integer.MIN_VALUE;
    public int countPostroll75GreaterThanOrEqual = Integer.MIN_VALUE;
    public String countLiveStreamingBandwidthLessThanOrEqual;
    public String countLiveStreamingBandwidthGreaterThanOrEqual;
    public String aggregatedLiveStreamingBandwidthLessThanOrEqual;
    public String aggregatedLiveStreamingBandwidthGreaterThanOrEqual;

    public KalturaDwhHourlyPartnerBaseFilter() {
    }

    public KalturaDwhHourlyPartnerBaseFilter(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("partnerIdEqual")) {
                try {
                    if (!txt.equals("")) this.partnerIdEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("aggregatedTimeLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.aggregatedTimeLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("aggregatedTimeGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.aggregatedTimeGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("sumTimeViewedLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.sumTimeViewedLessThanOrEqual = Float.parseFloat(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("sumTimeViewedGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.sumTimeViewedGreaterThanOrEqual = Float.parseFloat(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("averageTimeViewedLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.averageTimeViewedLessThanOrEqual = Float.parseFloat(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("averageTimeViewedGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.averageTimeViewedGreaterThanOrEqual = Float.parseFloat(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPlaysLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPlaysLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPlaysGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPlaysGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countLoadsLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countLoadsLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countLoadsGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countLoadsGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPlays25LessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPlays25LessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPlays25GreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPlays25GreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPlays50LessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPlays50LessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPlays50GreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPlays50GreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPlays75LessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPlays75LessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPlays75GreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPlays75GreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPlays100LessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPlays100LessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPlays100GreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPlays100GreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countEditLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countEditLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countEditGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countEditGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countSharesLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countSharesLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countSharesGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countSharesGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countDownloadLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countDownloadLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countDownloadGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countDownloadGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countReportAbuseLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countReportAbuseLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countReportAbuseGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countReportAbuseGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countMediaEntriesLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countMediaEntriesLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countMediaEntriesGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countMediaEntriesGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countVideoEntriesLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countVideoEntriesLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countVideoEntriesGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countVideoEntriesGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countImageEntriesLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countImageEntriesLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countImageEntriesGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countImageEntriesGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countAudioEntriesLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countAudioEntriesLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countAudioEntriesGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countAudioEntriesGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countMixEntriesLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countMixEntriesLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countMixEntriesGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countMixEntriesGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPlaylistsLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPlaylistsLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPlaylistsGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPlaylistsGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countBandwidthLessThanOrEqual")) {
                this.countBandwidthLessThanOrEqual = txt;
                continue;
            } else if (nodeName.equals("countBandwidthGreaterThanOrEqual")) {
                this.countBandwidthGreaterThanOrEqual = txt;
                continue;
            } else if (nodeName.equals("countStorageLessThanOrEqual")) {
                this.countStorageLessThanOrEqual = txt;
                continue;
            } else if (nodeName.equals("countStorageGreaterThanOrEqual")) {
                this.countStorageGreaterThanOrEqual = txt;
                continue;
            } else if (nodeName.equals("countUsersLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countUsersLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countUsersGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countUsersGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countWidgetsLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countWidgetsLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countWidgetsGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countWidgetsGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("aggregatedStorageLessThanOrEqual")) {
                this.aggregatedStorageLessThanOrEqual = txt;
                continue;
            } else if (nodeName.equals("aggregatedStorageGreaterThanOrEqual")) {
                this.aggregatedStorageGreaterThanOrEqual = txt;
                continue;
            } else if (nodeName.equals("aggregatedBandwidthLessThanOrEqual")) {
                this.aggregatedBandwidthLessThanOrEqual = txt;
                continue;
            } else if (nodeName.equals("aggregatedBandwidthGreaterThanOrEqual")) {
                this.aggregatedBandwidthGreaterThanOrEqual = txt;
                continue;
            } else if (nodeName.equals("countBufferStartLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countBufferStartLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countBufferStartGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countBufferStartGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countBufferEndLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countBufferEndLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countBufferEndGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countBufferEndGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countOpenFullScreenLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countOpenFullScreenLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countOpenFullScreenGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countOpenFullScreenGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countCloseFullScreenLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countCloseFullScreenLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countCloseFullScreenGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countCloseFullScreenGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countReplayLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countReplayLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countReplayGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countReplayGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countSeekLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countSeekLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countSeekGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countSeekGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countOpenUploadLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countOpenUploadLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countOpenUploadGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countOpenUploadGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countSavePublishLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countSavePublishLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countSavePublishGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countSavePublishGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countCloseEditorLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countCloseEditorLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countCloseEditorGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countCloseEditorGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPreBumperPlayedLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPreBumperPlayedLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPreBumperPlayedGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPreBumperPlayedGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPostBumperPlayedLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPostBumperPlayedLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPostBumperPlayedGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPostBumperPlayedGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countBumperClickedLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countBumperClickedLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countBumperClickedGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countBumperClickedGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPrerollStartedLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPrerollStartedLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPrerollStartedGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPrerollStartedGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countMidrollStartedLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countMidrollStartedLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countMidrollStartedGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countMidrollStartedGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPostrollStartedLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPostrollStartedLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPostrollStartedGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPostrollStartedGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countOverlayStartedLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countOverlayStartedLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countOverlayStartedGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countOverlayStartedGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPrerollClickedLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPrerollClickedLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPrerollClickedGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPrerollClickedGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countMidrollClickedLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countMidrollClickedLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countMidrollClickedGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countMidrollClickedGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPostrollClickedLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPostrollClickedLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPostrollClickedGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPostrollClickedGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countOverlayClickedLessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countOverlayClickedLessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countOverlayClickedGreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countOverlayClickedGreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPreroll25LessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPreroll25LessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPreroll25GreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPreroll25GreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPreroll50LessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPreroll50LessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPreroll50GreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPreroll50GreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPreroll75LessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPreroll75LessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPreroll75GreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPreroll75GreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countMidroll25LessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countMidroll25LessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countMidroll25GreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countMidroll25GreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countMidroll50LessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countMidroll50LessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countMidroll50GreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countMidroll50GreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countMidroll75LessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countMidroll75LessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countMidroll75GreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countMidroll75GreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPostroll25LessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPostroll25LessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPostroll25GreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPostroll25GreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPostroll50LessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPostroll50LessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPostroll50GreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPostroll50GreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPostroll75LessThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPostroll75LessThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countPostroll75GreaterThanOrEqual")) {
                try {
                    if (!txt.equals("")) this.countPostroll75GreaterThanOrEqual = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("countLiveStreamingBandwidthLessThanOrEqual")) {
                this.countLiveStreamingBandwidthLessThanOrEqual = txt;
                continue;
            } else if (nodeName.equals("countLiveStreamingBandwidthGreaterThanOrEqual")) {
                this.countLiveStreamingBandwidthGreaterThanOrEqual = txt;
                continue;
            } else if (nodeName.equals("aggregatedLiveStreamingBandwidthLessThanOrEqual")) {
                this.aggregatedLiveStreamingBandwidthLessThanOrEqual = txt;
                continue;
            } else if (nodeName.equals("aggregatedLiveStreamingBandwidthGreaterThanOrEqual")) {
                this.aggregatedLiveStreamingBandwidthGreaterThanOrEqual = txt;
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaDwhHourlyPartnerBaseFilter");
        kparams.addIntIfNotNull("partnerIdEqual", this.partnerIdEqual);
        kparams.addIntIfNotNull("aggregatedTimeLessThanOrEqual", this.aggregatedTimeLessThanOrEqual);
        kparams.addIntIfNotNull("aggregatedTimeGreaterThanOrEqual", this.aggregatedTimeGreaterThanOrEqual);
        kparams.addFloatIfNotNull("sumTimeViewedLessThanOrEqual", this.sumTimeViewedLessThanOrEqual);
        kparams.addFloatIfNotNull("sumTimeViewedGreaterThanOrEqual", this.sumTimeViewedGreaterThanOrEqual);
        kparams.addFloatIfNotNull("averageTimeViewedLessThanOrEqual", this.averageTimeViewedLessThanOrEqual);
        kparams.addFloatIfNotNull("averageTimeViewedGreaterThanOrEqual", this.averageTimeViewedGreaterThanOrEqual);
        kparams.addIntIfNotNull("countPlaysLessThanOrEqual", this.countPlaysLessThanOrEqual);
        kparams.addIntIfNotNull("countPlaysGreaterThanOrEqual", this.countPlaysGreaterThanOrEqual);
        kparams.addIntIfNotNull("countLoadsLessThanOrEqual", this.countLoadsLessThanOrEqual);
        kparams.addIntIfNotNull("countLoadsGreaterThanOrEqual", this.countLoadsGreaterThanOrEqual);
        kparams.addIntIfNotNull("countPlays25LessThanOrEqual", this.countPlays25LessThanOrEqual);
        kparams.addIntIfNotNull("countPlays25GreaterThanOrEqual", this.countPlays25GreaterThanOrEqual);
        kparams.addIntIfNotNull("countPlays50LessThanOrEqual", this.countPlays50LessThanOrEqual);
        kparams.addIntIfNotNull("countPlays50GreaterThanOrEqual", this.countPlays50GreaterThanOrEqual);
        kparams.addIntIfNotNull("countPlays75LessThanOrEqual", this.countPlays75LessThanOrEqual);
        kparams.addIntIfNotNull("countPlays75GreaterThanOrEqual", this.countPlays75GreaterThanOrEqual);
        kparams.addIntIfNotNull("countPlays100LessThanOrEqual", this.countPlays100LessThanOrEqual);
        kparams.addIntIfNotNull("countPlays100GreaterThanOrEqual", this.countPlays100GreaterThanOrEqual);
        kparams.addIntIfNotNull("countEditLessThanOrEqual", this.countEditLessThanOrEqual);
        kparams.addIntIfNotNull("countEditGreaterThanOrEqual", this.countEditGreaterThanOrEqual);
        kparams.addIntIfNotNull("countSharesLessThanOrEqual", this.countSharesLessThanOrEqual);
        kparams.addIntIfNotNull("countSharesGreaterThanOrEqual", this.countSharesGreaterThanOrEqual);
        kparams.addIntIfNotNull("countDownloadLessThanOrEqual", this.countDownloadLessThanOrEqual);
        kparams.addIntIfNotNull("countDownloadGreaterThanOrEqual", this.countDownloadGreaterThanOrEqual);
        kparams.addIntIfNotNull("countReportAbuseLessThanOrEqual", this.countReportAbuseLessThanOrEqual);
        kparams.addIntIfNotNull("countReportAbuseGreaterThanOrEqual", this.countReportAbuseGreaterThanOrEqual);
        kparams.addIntIfNotNull("countMediaEntriesLessThanOrEqual", this.countMediaEntriesLessThanOrEqual);
        kparams.addIntIfNotNull("countMediaEntriesGreaterThanOrEqual", this.countMediaEntriesGreaterThanOrEqual);
        kparams.addIntIfNotNull("countVideoEntriesLessThanOrEqual", this.countVideoEntriesLessThanOrEqual);
        kparams.addIntIfNotNull("countVideoEntriesGreaterThanOrEqual", this.countVideoEntriesGreaterThanOrEqual);
        kparams.addIntIfNotNull("countImageEntriesLessThanOrEqual", this.countImageEntriesLessThanOrEqual);
        kparams.addIntIfNotNull("countImageEntriesGreaterThanOrEqual", this.countImageEntriesGreaterThanOrEqual);
        kparams.addIntIfNotNull("countAudioEntriesLessThanOrEqual", this.countAudioEntriesLessThanOrEqual);
        kparams.addIntIfNotNull("countAudioEntriesGreaterThanOrEqual", this.countAudioEntriesGreaterThanOrEqual);
        kparams.addIntIfNotNull("countMixEntriesLessThanOrEqual", this.countMixEntriesLessThanOrEqual);
        kparams.addIntIfNotNull("countMixEntriesGreaterThanOrEqual", this.countMixEntriesGreaterThanOrEqual);
        kparams.addIntIfNotNull("countPlaylistsLessThanOrEqual", this.countPlaylistsLessThanOrEqual);
        kparams.addIntIfNotNull("countPlaylistsGreaterThanOrEqual", this.countPlaylistsGreaterThanOrEqual);
        kparams.addStringIfNotNull("countBandwidthLessThanOrEqual", this.countBandwidthLessThanOrEqual);
        kparams.addStringIfNotNull("countBandwidthGreaterThanOrEqual", this.countBandwidthGreaterThanOrEqual);
        kparams.addStringIfNotNull("countStorageLessThanOrEqual", this.countStorageLessThanOrEqual);
        kparams.addStringIfNotNull("countStorageGreaterThanOrEqual", this.countStorageGreaterThanOrEqual);
        kparams.addIntIfNotNull("countUsersLessThanOrEqual", this.countUsersLessThanOrEqual);
        kparams.addIntIfNotNull("countUsersGreaterThanOrEqual", this.countUsersGreaterThanOrEqual);
        kparams.addIntIfNotNull("countWidgetsLessThanOrEqual", this.countWidgetsLessThanOrEqual);
        kparams.addIntIfNotNull("countWidgetsGreaterThanOrEqual", this.countWidgetsGreaterThanOrEqual);
        kparams.addStringIfNotNull("aggregatedStorageLessThanOrEqual", this.aggregatedStorageLessThanOrEqual);
        kparams.addStringIfNotNull("aggregatedStorageGreaterThanOrEqual", this.aggregatedStorageGreaterThanOrEqual);
        kparams.addStringIfNotNull("aggregatedBandwidthLessThanOrEqual", this.aggregatedBandwidthLessThanOrEqual);
        kparams.addStringIfNotNull("aggregatedBandwidthGreaterThanOrEqual", this.aggregatedBandwidthGreaterThanOrEqual);
        kparams.addIntIfNotNull("countBufferStartLessThanOrEqual", this.countBufferStartLessThanOrEqual);
        kparams.addIntIfNotNull("countBufferStartGreaterThanOrEqual", this.countBufferStartGreaterThanOrEqual);
        kparams.addIntIfNotNull("countBufferEndLessThanOrEqual", this.countBufferEndLessThanOrEqual);
        kparams.addIntIfNotNull("countBufferEndGreaterThanOrEqual", this.countBufferEndGreaterThanOrEqual);
        kparams.addIntIfNotNull("countOpenFullScreenLessThanOrEqual", this.countOpenFullScreenLessThanOrEqual);
        kparams.addIntIfNotNull("countOpenFullScreenGreaterThanOrEqual", this.countOpenFullScreenGreaterThanOrEqual);
        kparams.addIntIfNotNull("countCloseFullScreenLessThanOrEqual", this.countCloseFullScreenLessThanOrEqual);
        kparams.addIntIfNotNull("countCloseFullScreenGreaterThanOrEqual", this.countCloseFullScreenGreaterThanOrEqual);
        kparams.addIntIfNotNull("countReplayLessThanOrEqual", this.countReplayLessThanOrEqual);
        kparams.addIntIfNotNull("countReplayGreaterThanOrEqual", this.countReplayGreaterThanOrEqual);
        kparams.addIntIfNotNull("countSeekLessThanOrEqual", this.countSeekLessThanOrEqual);
        kparams.addIntIfNotNull("countSeekGreaterThanOrEqual", this.countSeekGreaterThanOrEqual);
        kparams.addIntIfNotNull("countOpenUploadLessThanOrEqual", this.countOpenUploadLessThanOrEqual);
        kparams.addIntIfNotNull("countOpenUploadGreaterThanOrEqual", this.countOpenUploadGreaterThanOrEqual);
        kparams.addIntIfNotNull("countSavePublishLessThanOrEqual", this.countSavePublishLessThanOrEqual);
        kparams.addIntIfNotNull("countSavePublishGreaterThanOrEqual", this.countSavePublishGreaterThanOrEqual);
        kparams.addIntIfNotNull("countCloseEditorLessThanOrEqual", this.countCloseEditorLessThanOrEqual);
        kparams.addIntIfNotNull("countCloseEditorGreaterThanOrEqual", this.countCloseEditorGreaterThanOrEqual);
        kparams.addIntIfNotNull("countPreBumperPlayedLessThanOrEqual", this.countPreBumperPlayedLessThanOrEqual);
        kparams.addIntIfNotNull("countPreBumperPlayedGreaterThanOrEqual", this.countPreBumperPlayedGreaterThanOrEqual);
        kparams.addIntIfNotNull("countPostBumperPlayedLessThanOrEqual", this.countPostBumperPlayedLessThanOrEqual);
        kparams.addIntIfNotNull("countPostBumperPlayedGreaterThanOrEqual", this.countPostBumperPlayedGreaterThanOrEqual);
        kparams.addIntIfNotNull("countBumperClickedLessThanOrEqual", this.countBumperClickedLessThanOrEqual);
        kparams.addIntIfNotNull("countBumperClickedGreaterThanOrEqual", this.countBumperClickedGreaterThanOrEqual);
        kparams.addIntIfNotNull("countPrerollStartedLessThanOrEqual", this.countPrerollStartedLessThanOrEqual);
        kparams.addIntIfNotNull("countPrerollStartedGreaterThanOrEqual", this.countPrerollStartedGreaterThanOrEqual);
        kparams.addIntIfNotNull("countMidrollStartedLessThanOrEqual", this.countMidrollStartedLessThanOrEqual);
        kparams.addIntIfNotNull("countMidrollStartedGreaterThanOrEqual", this.countMidrollStartedGreaterThanOrEqual);
        kparams.addIntIfNotNull("countPostrollStartedLessThanOrEqual", this.countPostrollStartedLessThanOrEqual);
        kparams.addIntIfNotNull("countPostrollStartedGreaterThanOrEqual", this.countPostrollStartedGreaterThanOrEqual);
        kparams.addIntIfNotNull("countOverlayStartedLessThanOrEqual", this.countOverlayStartedLessThanOrEqual);
        kparams.addIntIfNotNull("countOverlayStartedGreaterThanOrEqual", this.countOverlayStartedGreaterThanOrEqual);
        kparams.addIntIfNotNull("countPrerollClickedLessThanOrEqual", this.countPrerollClickedLessThanOrEqual);
        kparams.addIntIfNotNull("countPrerollClickedGreaterThanOrEqual", this.countPrerollClickedGreaterThanOrEqual);
        kparams.addIntIfNotNull("countMidrollClickedLessThanOrEqual", this.countMidrollClickedLessThanOrEqual);
        kparams.addIntIfNotNull("countMidrollClickedGreaterThanOrEqual", this.countMidrollClickedGreaterThanOrEqual);
        kparams.addIntIfNotNull("countPostrollClickedLessThanOrEqual", this.countPostrollClickedLessThanOrEqual);
        kparams.addIntIfNotNull("countPostrollClickedGreaterThanOrEqual", this.countPostrollClickedGreaterThanOrEqual);
        kparams.addIntIfNotNull("countOverlayClickedLessThanOrEqual", this.countOverlayClickedLessThanOrEqual);
        kparams.addIntIfNotNull("countOverlayClickedGreaterThanOrEqual", this.countOverlayClickedGreaterThanOrEqual);
        kparams.addIntIfNotNull("countPreroll25LessThanOrEqual", this.countPreroll25LessThanOrEqual);
        kparams.addIntIfNotNull("countPreroll25GreaterThanOrEqual", this.countPreroll25GreaterThanOrEqual);
        kparams.addIntIfNotNull("countPreroll50LessThanOrEqual", this.countPreroll50LessThanOrEqual);
        kparams.addIntIfNotNull("countPreroll50GreaterThanOrEqual", this.countPreroll50GreaterThanOrEqual);
        kparams.addIntIfNotNull("countPreroll75LessThanOrEqual", this.countPreroll75LessThanOrEqual);
        kparams.addIntIfNotNull("countPreroll75GreaterThanOrEqual", this.countPreroll75GreaterThanOrEqual);
        kparams.addIntIfNotNull("countMidroll25LessThanOrEqual", this.countMidroll25LessThanOrEqual);
        kparams.addIntIfNotNull("countMidroll25GreaterThanOrEqual", this.countMidroll25GreaterThanOrEqual);
        kparams.addIntIfNotNull("countMidroll50LessThanOrEqual", this.countMidroll50LessThanOrEqual);
        kparams.addIntIfNotNull("countMidroll50GreaterThanOrEqual", this.countMidroll50GreaterThanOrEqual);
        kparams.addIntIfNotNull("countMidroll75LessThanOrEqual", this.countMidroll75LessThanOrEqual);
        kparams.addIntIfNotNull("countMidroll75GreaterThanOrEqual", this.countMidroll75GreaterThanOrEqual);
        kparams.addIntIfNotNull("countPostroll25LessThanOrEqual", this.countPostroll25LessThanOrEqual);
        kparams.addIntIfNotNull("countPostroll25GreaterThanOrEqual", this.countPostroll25GreaterThanOrEqual);
        kparams.addIntIfNotNull("countPostroll50LessThanOrEqual", this.countPostroll50LessThanOrEqual);
        kparams.addIntIfNotNull("countPostroll50GreaterThanOrEqual", this.countPostroll50GreaterThanOrEqual);
        kparams.addIntIfNotNull("countPostroll75LessThanOrEqual", this.countPostroll75LessThanOrEqual);
        kparams.addIntIfNotNull("countPostroll75GreaterThanOrEqual", this.countPostroll75GreaterThanOrEqual);
        kparams.addStringIfNotNull("countLiveStreamingBandwidthLessThanOrEqual", this.countLiveStreamingBandwidthLessThanOrEqual);
        kparams.addStringIfNotNull("countLiveStreamingBandwidthGreaterThanOrEqual", this.countLiveStreamingBandwidthGreaterThanOrEqual);
        kparams.addStringIfNotNull("aggregatedLiveStreamingBandwidthLessThanOrEqual", this.aggregatedLiveStreamingBandwidthLessThanOrEqual);
        kparams.addStringIfNotNull("aggregatedLiveStreamingBandwidthGreaterThanOrEqual", this.aggregatedLiveStreamingBandwidthGreaterThanOrEqual);
        return kparams;
    }
}

