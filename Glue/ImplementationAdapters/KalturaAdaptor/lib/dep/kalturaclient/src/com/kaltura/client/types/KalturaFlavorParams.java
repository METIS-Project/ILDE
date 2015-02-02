package com.kaltura.client.types;

import java.util.IllegalFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kaltura.client.KalturaObjectBase;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaObjectFactory;
import com.kaltura.client.enums.KalturaVideoCodec;
import com.kaltura.client.enums.KalturaAudioCodec;


/**
 * This class was generated using generate.php
 * against an XML schema provided by Kaltura.
 * @date Sun, 31 Jul 11 02:26:32 -0400
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

public class KalturaFlavorParams extends KalturaAssetParams {
    public KalturaVideoCodec videoCodec;
    public int videoBitrate = Integer.MIN_VALUE;
    public KalturaAudioCodec audioCodec;
    public int audioBitrate = Integer.MIN_VALUE;
    public int audioChannels = Integer.MIN_VALUE;
    public int audioSampleRate = Integer.MIN_VALUE;
    public int width = Integer.MIN_VALUE;
    public int height = Integer.MIN_VALUE;
    public int frameRate = Integer.MIN_VALUE;
    public int gopSize = Integer.MIN_VALUE;
    public String conversionEngines;
    public String conversionEnginesExtraParams;
    public boolean twoPass;
    public int deinterlice = Integer.MIN_VALUE;
    public int rotate = Integer.MIN_VALUE;
    public String operators;
    public int engineVersion = Integer.MIN_VALUE;

    public KalturaFlavorParams() {
    }

    public KalturaFlavorParams(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String txt = aNode.getTextContent();
            String nodeName = aNode.getNodeName();
            if (false) {
                // noop
            } else if (nodeName.equals("videoCodec")) {
                try {
                    if (!txt.equals("")) this.videoCodec = KalturaVideoCodec.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("videoBitrate")) {
                try {
                    if (!txt.equals("")) this.videoBitrate = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("audioCodec")) {
                try {
                    if (!txt.equals("")) this.audioCodec = KalturaAudioCodec.get(txt);
                } catch (IllegalFormatException ife) {}
                continue;
            } else if (nodeName.equals("audioBitrate")) {
                try {
                    if (!txt.equals("")) this.audioBitrate = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("audioChannels")) {
                try {
                    if (!txt.equals("")) this.audioChannels = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("audioSampleRate")) {
                try {
                    if (!txt.equals("")) this.audioSampleRate = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("width")) {
                try {
                    if (!txt.equals("")) this.width = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("height")) {
                try {
                    if (!txt.equals("")) this.height = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("frameRate")) {
                try {
                    if (!txt.equals("")) this.frameRate = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("gopSize")) {
                try {
                    if (!txt.equals("")) this.gopSize = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("conversionEngines")) {
                this.conversionEngines = txt;
                continue;
            } else if (nodeName.equals("conversionEnginesExtraParams")) {
                this.conversionEnginesExtraParams = txt;
                continue;
            } else if (nodeName.equals("twoPass")) {
                if (!txt.equals("")) this.twoPass = ((txt.equals("0")) ? false : true);
                continue;
            } else if (nodeName.equals("deinterlice")) {
                try {
                    if (!txt.equals("")) this.deinterlice = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("rotate")) {
                try {
                    if (!txt.equals("")) this.rotate = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } else if (nodeName.equals("operators")) {
                this.operators = txt;
                continue;
            } else if (nodeName.equals("engineVersion")) {
                try {
                    if (!txt.equals("")) this.engineVersion = Integer.parseInt(txt);
                } catch (NumberFormatException nfe) {}
                continue;
            } 

        }
    }

    public KalturaParams toParams() {
        KalturaParams kparams = super.toParams();
        kparams.setString("objectType", "KalturaFlavorParams");
        if (videoCodec != null) kparams.addStringIfNotNull("videoCodec", this.videoCodec.getHashCode());
        kparams.addIntIfNotNull("videoBitrate", this.videoBitrate);
        if (audioCodec != null) kparams.addStringIfNotNull("audioCodec", this.audioCodec.getHashCode());
        kparams.addIntIfNotNull("audioBitrate", this.audioBitrate);
        kparams.addIntIfNotNull("audioChannels", this.audioChannels);
        kparams.addIntIfNotNull("audioSampleRate", this.audioSampleRate);
        kparams.addIntIfNotNull("width", this.width);
        kparams.addIntIfNotNull("height", this.height);
        kparams.addIntIfNotNull("frameRate", this.frameRate);
        kparams.addIntIfNotNull("gopSize", this.gopSize);
        kparams.addStringIfNotNull("conversionEngines", this.conversionEngines);
        kparams.addStringIfNotNull("conversionEnginesExtraParams", this.conversionEnginesExtraParams);
        kparams.addBoolIfNotNull("twoPass", this.twoPass);
        kparams.addIntIfNotNull("deinterlice", this.deinterlice);
        kparams.addIntIfNotNull("rotate", this.rotate);
        kparams.addStringIfNotNull("operators", this.operators);
        kparams.addIntIfNotNull("engineVersion", this.engineVersion);
        return kparams;
    }
}

